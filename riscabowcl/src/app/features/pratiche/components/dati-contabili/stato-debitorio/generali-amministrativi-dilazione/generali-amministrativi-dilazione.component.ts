import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Moment } from 'moment';
import { Subscription } from 'rxjs/index';
import { IuvVo } from 'src/app/core/commons/vo/iuv-vo';
import { RataVo } from 'src/app/core/commons/vo/rata-vo';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import { PraticaDTVo, PraticaVo } from '../../../../../../core/commons/vo/pratica-vo';
import { StatoDebitorioVo } from '../../../../../../core/commons/vo/stato-debitorio-vo';
import { TipoIstanzaProvvedimentoVo } from '../../../../../../core/commons/vo/tipo-istanza-provvidemento-vo';
import { TipoTitoloVo } from '../../../../../../core/commons/vo/tipo-titolo-vo';
import { AccessoElementiAppKeyConsts } from '../../../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { LoggerService } from '../../../../../../core/services/logger.service';
import { RiscaFormChildComponent } from '../../../../../../shared/components/risca/risca-form-child/risca-form-child.component';
import { RiscaAlertService } from '../../../../../../shared/services/risca/risca-alert.service';
import { RiscaFormBuilderService } from '../../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaLockPraticaService } from '../../../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { RiscaUtilitiesService } from '../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { AppActions, FormUpdatePropagation, IRiscaCheckboxData, IRiscaTabChanges, RiscaAlertConfigs, RiscaServerError, RiscaStatoDebitorio } from '../../../../../../shared/utilities';
import { DatiContabiliConsts } from '../../../../consts/dati-contabili/dati-contabili.consts';
import { GeneraliAmministrativiDilazioneConsts } from '../../../../consts/dati-contabili/generali-amministrativi-dilazione.consts';
import { IDCSetFormValue, IGenAmmDilSetFormValue } from '../../../../interfaces/dati-contabili/dati-contabili-utility.interfaces';
import { DatiContabiliUtilityService } from '../../../../service/dati-contabili/dati-contabili/dati-contabili-utility.service';
import { DatiContabiliService } from '../../../../service/dati-contabili/dati-contabili/dati-contabili.service';
import { GeneraliAmministrativiDilazioneService } from '../../../../service/dati-contabili/generali-amministrativi-dilazione.service';
import { StatoDebitorioService } from '../../../../service/dati-contabili/stato-debitorio.service';
import { compensazioneCanoneValidator, flagAnnullatoEMotivazioneValidator } from './../../../../../../shared/miscellaneous/forms-validators/generali-amministrativi-dilazione/form-validators.gad';
import { GADFieldsConfigClass } from './utilities/generali-amministrativi-dilazione.fields-configs';
import { GeneraliAmministrativiDilazione } from './utilities/generali-amministrativi-dilazione.interfaces';
import { AccessoElementiAppService } from '../../../../../../core/services/accesso-elementi-app.service';

@Component({
  selector: 'generali-amministrativi-dilazione',
  templateUrl: './generali-amministrativi-dilazione.component.html',
  styleUrls: ['./generali-amministrativi-dilazione.component.scss'],
})
export class GeneraliAmministrativiDilazioneComponent
  extends RiscaFormChildComponent<GeneraliAmministrativiDilazione>
  implements OnInit, OnDestroy
{
  /** Costante per le informazioni del componente. */
  DC_C = DatiContabiliConsts;
  /** Costante per le informazioni del componente specifico.  */
  GAD_C = GeneraliAmministrativiDilazioneConsts;
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;
  /** Costante che definisce che per il riepilogo dello stato debitorio, la modalità è sempre modifica. */
  MODALITA_SD_RIEPILOGO = AppActions.modifica;

  /** Classe GADFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: GADFieldsConfigClass;

  /** Lista di supporto contenente le informazioni per: tipo titolo. NON UTILIZZATO PER IL MOMENTO */
  listaTipiTitolo: TipoTitoloVo[] = [];
  /** Lista di supporto contenente le informazioni per: tipo istanza. */
  listaTipiIstanza: TipoIstanzaProvvedimentoVo[] = [];

  /** Rata fittizia. In inserimento, per gestire la data scadenza pagamento, si crea una rata fittizia a cui assegnare la data scadenza pagamento.
   * Questa viene passata al BE che poi genera lui le rate che popolano la data scadenza pagamento in modifica. */
  rataFittizia: RataVo;

  /** StatoDebitorioVo come copia dell'oggetto salvato nel servizio per le funzionalità locali. */
  statoDebitorio: StatoDebitorioVo;
  /** PraticaVo come copia dell'oggetto salvato nel servizio per le funzionalità locali. */
  pratica: PraticaVo;
  /** Oggetto PraticaDTVo contenente i dati per la sezione: dati tecnici. */
  datiTecniciConfigs: PraticaDTVo;
  /** Number che definisce l'id del componente dt della pratica da caricare. */
  idComponenteDtP: number;

  /** RiscaAlertConfigs contenente le configurazioni per la gestione dell'alert utente. */
  alertConfigs: RiscaAlertConfigs = new RiscaAlertConfigs();

  /** Boolean che gestisce lo stati di visualizzazione dei dati dentro l'accordion. */
  generaliSintesiApriChiudi = true;
  /** Boolean che gestisce lo stati di visualizzazione dei dati dentro l'accordion. */
  amministrativiTecniciApriChiudi = false;

  /**
   * ##############################################
   * SUBSCRIPTION AL SALVATAGGIO DELLE INFORMAZIONI
   * ##############################################
   */
  /** Subscription che permette di sapere quando la tab dello stato debitorio viene cambiato dall'utente. */
  onSezioneSDChange: Subscription;

  /** Subscription che permette la condivisione dell'evento di inserimento di un oggetto StatoDebitorioVo. */
  onSDInsSuccess: Subscription;
  /** Subscription che permette la condivisione dell'evento d'errore durante un inserimento di un oggetto StatoDebitorioVo. */
  onSDInsError: Subscription;
  /** Subscription che permette la condivisione dell'evento di annuallamento della funzionalità di inserimento di un oggetto StatoDebitorioVo. */
  onSDInsCancel: Subscription;

  /** Subscription che permette la condivisione dell'evento di modifica di un oggetto StatoDebitorioVo. */
  onSDModSuccess: Subscription;
  /** Subscription che permette la condivisione dell'evento d'errore durante una modifica di un oggetto StatoDebitorioVo. */
  onSDModError: Subscription;
  /** Subscription che permette la condivisione dell'evento di annuallamento della funzionalità di modifica di un oggetto StatoDebitorioVo. */
  onSDModCancel: Subscription;

  /**
   * Costruttore
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _datiContabili: DatiContabiliService,
    private _datiContabiliUtility: DatiContabiliUtilityService,
    private _formBuilder: FormBuilder,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _genAmmDil: GeneraliAmministrativiDilazioneService,
    private _riscaAlert: RiscaAlertService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _riscaLockP: RiscaLockPraticaService,
    protected _riscaMessages: RiscaMessagesService,
    riscaUtilities: RiscaUtilitiesService,
    private _statoDebitorioServ: StatoDebitorioService
  ) {
    // Richiamo il super
    super(logger, navigationHelper, riscaFormSubmitHandler, riscaUtilities);
    // Lancio il setup del componente
    this.setupComponente();
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Funzione di setup per il componente.
   */
  private setupComponente() {
    // Setep degli errori da verificare al submit della form
    this.setupMainFormErrors();
    // Setup delle input form del componente
    this.setupFormInputs();
    // Setup delle informazioni per pratica e componente tecnico
    this.setupPraticaEDTRiepilogo();
    // Setup delle informazioni per lo stato debitorio
    this.setupStatoDebitorio();
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  private setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [...this.EM.MAP_STATO_DEBITORIO];
  }

  /**
   * Funzione di setup delle inputs form del componente.
   */
  private setupFormInputs() {
    // Essendo le configurazioni parecchie, si utilizza una classe di supporto
    this.formInputs = new GADFieldsConfigClass(this._riscaFormBuilder);
  }

  /**
   * Funzione di setup per le configurazioni del componente tecnico di riepilogo.
   * Essendo che il servizio crea una copia in tutto e per tutto, e l'operazione può risultare onerosa, viene fatta una singola inizializzazione del dato quando il componente viene instanziato, così d'agevolare tutte le possibili elaborazioni locali.
   */
  private setupPraticaEDTRiepilogo() {
    // Recupero il dato della pratica
    this.pratica = this._datiContabiliUtility.pratica;
    // Recupero il dato tecnico della pratica
    this.datiTecniciConfigs = this._datiContabiliUtility.datiTecnici;
    // Recupero l'id del componente dt della pratica
    this.idComponenteDtP = this._datiContabiliUtility.pratica?.id_componente_dt;
  }

  /**
   * Funzione di setup per le configurazioni dati per lo stato debitorio.
   * Essendo che il servizio crea una copia in tutto e per tutto, e l'operazione può risultare onerosa, viene fatta una singola inizializzazione del dato quando il componente viene instanziato, così d'agevolare tutte le possibili elaborazioni locali.
   */
  setupStatoDebitorio() {
    // Recupero il dato dello stato debitorio e lo tengo localmente
    this.statoDebitorio = this._datiContabiliUtility.statoDebitorio;

    // Verifico se sono in modalita modifica
    if (this.modifica) {
      // Sono in modifica, tento di recuperare le annualità dallo stato debitorio
      const annualita = this.statoDebitorio?.annualita_sd;
      // Verifico se esistono annualità, dovrebbe essercene almeno 1, ma controlliamo
      if (annualita && annualita.length > 0) {
        // Recupero dalla prima annualità l'id del componente dt
        this.idComponenteDtP = annualita[0].id_componente_dt;
      }
    }
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  ngOnInit() {
    // Lancio l'init del componente
    this.initComponente();
    // Lancio l'init dei listener del componente
    this.initListeners();
  }

  /**
   * Destroy
   * @override
   */
  ngOnDestroy(): void {
    // Invoco il destroy del padre
    super.ngOnDestroy();
    // Tento di fare l'unsubscribe degli events
    try {
      // Verifico che esista l'event emitter
      if (this.onSezioneSDChange) {
        this.onSezioneSDChange.unsubscribe();
      }
      if (this.onSDInsSuccess) {
        this.onSDInsSuccess.unsubscribe();
      }
      if (this.onSDInsError) {
        this.onSDInsError.unsubscribe();
      }
      if (this.onSDInsCancel) {
        this.onSDInsCancel.unsubscribe();
      }
      if (this.onSDModSuccess) {
        this.onSDModSuccess.unsubscribe();
      }
      if (this.onSDModError) {
        this.onSDModError.unsubscribe();
      }
      if (this.onSDModCancel) {
        this.onSDModCancel.unsubscribe();
      }
    } catch (e) {}
  }

  /**
   * Funzione di init per il componente.
   */
  private initComponente() {
    // Init della struttura della form principale
    this.initFormDatiGenAmmDil();
    // Init delle informazioni per la form dei generali amministrativi
    this.initFormValues();
    // Init delle informazioni per il form che risultano asincrone
    this.initFormValuesAsync();
  }

  /**
   * Init del form: mainForm.
   */
  private initFormDatiGenAmmDil() {
    this.mainForm = this._formBuilder.group(
      {
        codiceUtenza: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        dataUltimaModifica: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        numRichiestaProtocollo: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        dataRichiestaProtocollo: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        restituitoMittente: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        periodoPagamento: new FormControl(
          { value: null, disabled: false },
          { validators: [Validators.required] }
        ),
        scadenzaPagamento: new FormControl(
          { value: null, disabled: false },
          { validators: [Validators.required] }
        ),
        codiceAvviso: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        stato: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        invioSpecialePostel: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        annullato: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        motivazione: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        canoneAnnualitaCorrente: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        annualitaPrecedente: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        canoneDovuto: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        addebitoAnnoSuccessivo: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        interessiMaturati: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        speseNotifica: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        importoCompensazione: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        dilazione: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        tipoTitolo: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        numeroTitolo: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        dataTitolo: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        inizioConcessione: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        scadenzaConcessione: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        istanzaRinnovo: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        noteSD: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        utenzeCompensazione: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
      },
      // Validatori campi incrociati
      {
        validators: [
          flagAnnullatoEMotivazioneValidator(
            this.GAD_C.ANNULLATO,
            this.GAD_C.MOTIVAZIONE
          ),
          compensazioneCanoneValidator(
            this.GAD_C.IMPORTO_COMPENSAZIONE,
            this.modalita
          ),
        ],
      }
    );

    // Lancio la funzione di init per i campi a seconda della modalità del componente
    this.initFormEnableByModalita();

    // Verifico le condizioni di disabilitazione del form
    if (this.disableUserInputs) {
      // Disabilito il form
      this.mainForm.disable();
    }
  }

  /**
   * Funzione che abilita o disabilita i campi a seconda della modalità del componente.
   */
  private initFormEnableByModalita() {
    // Definisco un oggetto di configurazione
    const noEmit = { emitEvent: false };

    // Verifico se siamo in inserimento
    if (this.inserimento) {
      // Importo da compensazione: disabilitato
      this.importoCompensazioneFC?.disable(noEmit);
      // #
    }
  }

  /**
   * Funzione che pre-valorizza i dati del form, partendo da un oggetto "StatoDebitorioVo".
   */
  initFormValues() {
    // Creo l'oggetto di configurazione per popolare i campi del form
    const configs: IGenAmmDilSetFormValue = {
      mainForm: this.mainForm,
      statoDebitorio: this.statoDebitorio,
      pratica: this.pratica,
      modalita: this.modalita,
      options: { emitEvent: true },
      formInputs: this.formInputs,
    };

    // Richiamo la funzione del servizio per pre-popolare le informazioni
    this._genAmmDil.setMainFormValues(configs);
    // Popolo la rata fittizia se sto ancora modificando i dati di uno stato debitorio in inserimento
    this.setRataFittiziaOnPageChange(configs);
    // Inizializzo tutte le informazioni relative alle check box
    this._genAmmDil.setMainFormChecboxes(configs);

    // RISCA-545: il flg_dilazione deve essere sempre abilitato
    // // Controllo se sono in modifica e se la dilazione è a true.
    // if (this.modifica && this.statoDebitorio?.flg_dilazione) {
    //   // Se lo è, devo disabilitare il controllo
    //   this.dilazioneFC?.disable();
    //   // #
    // }

    // Imposto il mark as touched per evidenziare, eventualmente, la mancanza della motivazione
    this.motivazioneFC.markAsTouched();
  }

  /**
   * Funzione che pre-valorizza i dati del form, ma che richiedono l'intervento di API di scarico dati.
   */
  initFormValuesAsync() {
    // Recupero l'id della pratica in gestione ed un eventuale id stato debitorio
    const idPratica = this.idPratica;
    const idStatoDebitorio = this.idStatoDebitorio;
    // Verifico se per la pratica corrente esiste già uno stato debitorio con invio speciale
    this.praticaConSDInvioSpeciale(idPratica, idStatoDebitorio);

    // Verifico la modalita del componente
    if (this.modifica) {
      // Recupero il nap dallo stato debitorio
      const nap = this.statoDebitorio?.nap;
      // Inizializzo lo Iuv
      this.retrieveIuv(nap);

      // Recupero l'id della pratica
      const idSD = this.statoDebitorio?.id_stato_debitorio;
      // Inizializzo le utenze da compensazione
      this.retrieveUtenzeCompensazione(idSD);
    }
  }

  /**
   * #######################################
   * GESTIONE PER PRATICA CON INVIO SPECIALE
   * #######################################
   */

  /**
   * Funzione che verifica se per la pratica corrente esiste già uno stato debitorio con invio speciale.
   * @param idPratica number che specifica l'id pratica per la verifica.
   * @param idStatoDebitorio number che specifica l'id stato debitorio da escludere dalla verifica per i flag invio speciale.
   */
  private praticaConSDInvioSpeciale(
    idPratica: number,
    idStatoDebitorio?: number
  ) {
    // Verifico se esiste l'id pratica
    if (idPratica == undefined) {
      // Non c'è l'id pratica, niente controll
      return;
    }

    // Lancio il servizio che verifichi se la pratica ha già uno stato debitorio con invio speciale
    this._datiContabili
      .praticaConSDInvioSpeciale(idPratica, idStatoDebitorio)
      .subscribe({
        next: (pSDInvioSpeciale: boolean) => {
          // Richiamo la funzione per la gestione del flag invio speciale postel
          this.onPraticaConSDInvioSpeciale(pSDInvioSpeciale);
          // #
        },
        error: (error: RiscaServerError) => {
          // Richiamo la funzione per la gestione del flag invio speciale postel, forzando la disabilitazione
          this.onPraticaConSDInvioSpeciale(true);
          // Richiamo la funzione di gestione per gli errori
          this.onServiziError(error);
          // #
        },
      });
  }

  /**
   * Funzione di gestione del campo invioSpecialePostel a seguito dello scarico del dato di controllo per la pratica.
   * @param pSDInvioSpeciale boolean con il boolean che definisce se la pratica ha già uno stato debitorio con invio speciale.
   */
  private onPraticaConSDInvioSpeciale(pSDInvioSpeciale: boolean) {
    // Verifico la gestione del flag di disabilitazione
    if (this.disableUserInputs) {
      // Blocco altre logiche
      return;
    }

    // RISCA-754: non c'è da gestire più inserimento/modifica, ma basarsi sempre su cosa ritorna il servizio (in questo caso, come parametro in input)
    // Verifico il flag d'invio speciale
    if (!pSDInvioSpeciale) {
      // La pratica NON ha stati debitori con invio speciale, abilito il check
      this.abilitaInvioSpeciale();
      // #
    } else {
      // La pratica HA GIA' uno stato debitorio con invio speciale, disabilito il check
      this.disabilitaInvioSpeciale();
    }
  }

  /**
   * Funzione di comodo che abilita il checkbox per l'invio speciale postel.
   */
  private abilitaInvioSpeciale() {
    // Definisco la configurazione per la gestione della propagazione dell'evento
    const o: FormUpdatePropagation = { emitEvent: false };
    // Abilito il campo
    this.invioSpecialePostel?.enable(o);
  }

  /**
   * Funzione di comodo che disabilita il checkbox per l'invio speciale postel.
   */
  private disabilitaInvioSpeciale() {
    // Definisco la configurazione per la gestione della propagazione dell'evento
    const o: FormUpdatePropagation = { emitEvent: false };
    // Disabilito il campo
    this.invioSpecialePostel?.disable(o);
  }

  /**
   * ###################################
   * FUNZIONI DI SET PER CAMPI ASINCRONI
   * ###################################
   */

  /**
   * Funzione di comodo che valorizza le informazioni derivate dall'oggetto Iuv.
   * @param nap string che definisce il nap dello stato debitorio da inizializzare.
   */
  private retrieveIuv(nap: string) {
    // Verifico che esista il nap
    if (nap != undefined) {
      // Scarico l'oggetto IuvVo
      this._datiContabili.getIuv(nap).subscribe({
        next: (iuv: IuvVo) => {
          // Vado a valorizzare le informazioni per lo iuv
          this.evaluateIuv(iuv);
          // #
        },
        error: (error: RiscaServerError) => {
          // Richiamo la funzione di gestione per gli errori
          this.onServiziError(error);
          // #
        },
      });
    }
  }

  /**
   * Funzione che valorizza le informazioni riguardati lo Iuv all'interno della form dati.
   * @param iuv IuvVo con i dati da valorizzare.
   */
  private evaluateIuv(iuv: IuvVo) {
    // Verifico l'input
    if (!iuv) {
      // Niente configurazione
      return;
    }

    // Recupero le informazioni per aggiornare i campi
    const mainForm = this.mainForm;
    const options = { emitEvent: true, onlySelf: true };

    // ####### CODICE AVVISO - IUV #######
    /**
     * Il codice avviso viene scaricato asincrono tramite API /iuv?nap={nap}.
     * IuvVo.codice_avviso
     */
    const codiceAvviso: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.CODICE_AVVISO,
      value: iuv?.codice_avviso,
    };

    // ####### STATO - IUV #######
    /**
     * Lo stato viene scaricato asincrono tramite API /iuv?nap={nap}.
     * IuvVo.StatoIuvVo.des_stato_iuv
     */
    const statoAvviso: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.STATO,
      value: iuv?.stato_iuv?.des_stato_iuv,
    };

    // Vado a lanciare il set di tutti i campi con le configurazioni
    this._datiContabiliUtility.setFormControlValue(codiceAvviso);
    this._datiContabiliUtility.setFormControlValue(statoAvviso);
  }

  /**
   * Funzione di comodo che valorizza le informazioni per le utenze compesazione.
   * @param idStatoDebitorio number che definisce l'id dello stato debitorio da inizializzare.
   */
  private retrieveUtenzeCompensazione(idStatoDebitorio: number) {
    // Verifico che esista il nap
    if (idStatoDebitorio != undefined) {
      // Scarico l'oggetto IuvVo
      this._datiContabili.getUtenzeCompensazione(idStatoDebitorio).subscribe({
        next: (codiceUtenza: string) => {
          // Vado a valorizzare le informazioni per il codice utenza
          this.evaluateUtenzeCompensazione(codiceUtenza);
          // #
        },
        error: (error: RiscaServerError) => {
          // Richiamo la funzione di gestione per gli errori
          this.onServiziError(error);
          // #
        },
      });
    }
  }

  /**
   * Funzione che valorizza le informazioni riguardati il campo utenze compesazione della form dati.
   * @param utenzeCompesazione string con i dati da valorizzare.
   */
  private evaluateUtenzeCompensazione(utenzeCompesazione: string) {
    // Verifico l'input
    if (utenzeCompesazione == undefined) {
      // Niente configurazione
      return;
    }

    // Recupero le informazioni per aggiornare i campi
    const mainForm = this.mainForm;
    const options = { emitEvent: true, onlySelf: true };

    // ####### UTENZE DA COMPENSAZIONE #######
    /**
     * L'utenza da compensazione viene scaricata asincrona tramite API /riscossione?idStatoDebitorio={idStatoDebitorio}.
     */
    const utenzaCompensazione: IDCSetFormValue = {
      mainForm,
      options,
      formControlKey: this.GAD_C.UTENZE_COMPENSAZIONE,
      value: utenzeCompesazione,
    };

    // Vado a lanciare il set di tutti i campi con le configurazioni
    this._datiContabiliUtility.setFormControlValue(utenzaCompensazione);
  }

  /**
   * Funzione di init per i listener del componente.
   */
  private initListeners() {
    /** Listener per il cambio della data scadenza pagamento */
    // Solo in inserimento devo gestire una rata fittizia per il BE
    this.mainForm
      .get(this.GAD_C.SCADENZA_PAGAMENTO)
      .valueChanges.subscribe((data: NgbDateStruct) => {
        // Richiamo la funzione di gestione dati per la scadenza pagamento
        this.onScadenzaPagamentoChange(data);
      });

    this.mainForm
      .get(this.GAD_C.ANNULLATO)
      .valueChanges.subscribe((annullato: IRiscaCheckboxData) => {
        // Lancio la gestione del flag annullato
        this.onAnnullatoChanged(annullato);
        // #
      });

    /**
     * #############################################
     * LISTENER PER GLI EVENTI DELLO STATO DEBITORIO
     * #############################################
     */
    this.onSezioneSDChange =
      this._statoDebitorioServ.onSezioneSDChanges$.subscribe({
        next: (tabs: IRiscaTabChanges) => {
          // Lancio la funzione di cambio tab
          this.tabSDChanged(tabs);
        },
      });

    this.onSDInsSuccess = this._datiContabili.onSDInsSuccess$.subscribe({
      next: (statoDebitorio: StatoDebitorioVo) => {
        // Gestisco l'evento di success
        this.onSDSuccessResult(statoDebitorio);
      },
    });

    this.onSDInsError = this._datiContabili.onSDInsError$.subscribe({
      next: (error: RiscaServerError) => {
        // Gestisco l'errore anche nel caso sia multiplo
        this.onSDErrorResult(error);
      },
    });

    this.onSDInsCancel = this._datiContabili.onSDInsCancel$.subscribe({
      next: () => {
        // Gestisco l'evento di cancellazione
        this.onSDCancelResult();
      },
    });

    this.onSDModSuccess = this._datiContabili.onSDModSuccess$.subscribe({
      next: (statoDebitorio: StatoDebitorioVo) => {
        // Gestisco l'evento di success
        this.onSDSuccessResult(statoDebitorio);
      },
    });

    this.onSDModError = this._datiContabili.onSDModError$.subscribe({
      next: (error: RiscaServerError) => {
        // Gestisco l'errore anche nel caso sia multiplo
        this.onSDErrorResult(error);
      },
    });

    this.onSDModCancel = this._datiContabili.onSDModCancel$.subscribe({
      next: () => {
        // Gestisco l'evento di cancellazione
        this.onSDCancelResult();
      },
    });
  }

  /**
   * Funzione collegata al cambio della data scadenza pagamento dello stato debitorio.
   * La data scadenza pagamento è collegata alla rate, nello specifico, alla rata padre.
   * @param data NgbDateStruct contenente la data scadenza impostata dall'utente.
   */
  private onScadenzaPagamentoChange(data: NgbDateStruct) {
    // Verifico le condizioni per la gestione della data
    const existData = data != undefined;

    // Controllo se ci sono informazioni ritornate dal form
    if (existData) {
      // Richiamo la funzione di supporto per gestire la casistica di esistenza del dato
      this.onSPCExist(data);
      // #
    } else {
      // Richiamo la funzione di supporto per gestire la casistica di NON esistenza del dato
      this.onSPCNotExist();
      // #
    }
  }

  /**
   * Funzione di gestione della casistica di data scadenza pagamento: esistente.
   * @param data NgbDateStruct contenente la data scadenza impostata dall'utente.
   */
  private onSPCExist(data: NgbDateStruct) {
    // Converto la data da NgbDateStruct a string compatibile con il server
    let dtSPMoment: Moment;
    dtSPMoment = this._riscaUtilities.convertNgbDateStructToMoment(data);

    // Controllo se ci troviamo in inserimento o se ci troviamo in modifica
    if (this.inserimento) {
      // Per la rata fittizia ho bisogno di una data string formato server
      let dtSP: string;
      dtSP = this._riscaUtilities.convertMomentToServerDate(dtSPMoment);
      // Creo la rata fittizia che verrà poi definita nello stato debitorio
      this.rataFittizia = new RataVo({ data_scadenza_pagamento: dtSP });
      // #
    } else if (this.modifica) {
      // Mi trovo in modifica, devo aggiornare la rata padre dello stato debitorio istanziato localmente
      const rateSD: RataVo[] = this.statoDebitorio?.rate ?? [];
      // Recupero dalle rate dello sd la rata padre
      const rataPadre: RataVo = this.rataPadre;

      // Verifico se esiste la rata padre
      if (rataPadre) {
        // Esiste l'oggetto, aggiorno la data di scadenza
        rataPadre.data_scadenza_pagamento = dtSPMoment;
        // Aggiorno l'oggetto della rata nella lista
        let rUPD: RataVo[];
        rUPD = this._datiContabiliUtility.updateRataInList(rateSD, rataPadre);
        // Riassegno le rate allo stato debitorio
        this.statoDebitorio.rate = rUPD;
      }
    }
  }

  /**
   * Funzione di gestione della casistica di data scadenza pagamento: inesistente.
   */
  private onSPCNotExist() {
    // Controllo se ci troviamo in inserimento o se ci troviamo in modifica
    if (this.inserimento) {
      // Se esiste una rata fittizia, devo rimettere nella form il suo valore
      if (this.rataFittizia) {
        // Recupero la data dalla rata fittizia
        let dtRF: Moment;
        dtRF = this.rataFittizia.data_scadenza_pagamento;
        // Converto la data in string
        let dtSP: string;
        dtSP = this._riscaUtilities.convertMomentToViewDate(dtRF);
        // Popolo la data scadenza dal form
        this.scadenzaPagamento = dtSP;
      }
      // #
    } else if (this.modifica) {
      // Recupero dalle rate dello sd la rata padre
      const rataPadre: RataVo = this.rataPadre;
      // Se esiste la rata padre, recupero la data e la imposto nel form
      if (rataPadre) {
        // Recupero la data dalla rata padre
        let dtRP: Moment;
        dtRP = rataPadre.data_scadenza_pagamento;
        // Converto la data in string
        let dtSP: string;
        dtSP = this._riscaUtilities.convertMomentToViewDate(dtRP);
        // Popolo la data scadenza dal form
        this.scadenzaPagamento = dtSP;
      }
    }
  }

  /**
   * Funzione che gestisce il cambio di valore del flag "annuallato".
   * @param annullato IRiscaCheckboxData con il valore del campo.
   */
  private onAnnullatoChanged(annullato: IRiscaCheckboxData) {
    // Verifico l'input
    if (!annullato || !annullato.check) {
      // Il flag annuallto è false, motivazione non è obbligatorio
      this.motivazioneFC.setValidators([]);
      // #
    } else {
      // Flag annullato esiste ed è true, motivazione diventa obbligatoria
      this.motivazioneFC.setValidators([Validators.required]);
    }
  }

  /**
   * Gestisce le operazioni da eseguire alla fine del successo nel salvataggio dello stato debitorio.
   * @param statoDebitorio StatoDebitorioVo salvato.
   */
  onSDSuccessResult(statoDebitorio: StatoDebitorioVo) {
    // Il componente attiva la modalità modifica
    this.modalita = AppActions.modifica;
    // Devo copiarmelo in questo componente e ripristinare la pagina
    this.aggiornaPagina();
  }

  /**
   * Gestisce le operazioni da eseguire in caso di errore nel salvataggio dello stato debitorio.
   */
  onSDErrorResult(error: RiscaServerError) {
    // Gestione della risposta dall'ascoltatore
  }

  /**
   * Gestisce le operazioni da eseguire alla fine della cancellazione nel salvataggio dello stato debitorio.
   */
  onSDCancelResult() {
    // Gestione della risposta dall'ascoltatore
  }

  /**
   * Funzione che ripristina le informazioni della pagina al termine delle operazioni di
   * salvataggio, gestione errori e annullamento della modifica
   * dello stato debitorio con le informazioni di questa pagina.
   */
  aggiornaPagina() {
    // Setup delle informazioni per lo stato debitorio
    this.setupStatoDebitorio();

    // Rilancio la gestione del flag invio speciale
    const idPratica = this.idPratica;
    const idStatoDebitorio = this.idStatoDebitorio;
    // Verifico se per la pratica corrente esiste già uno stato debitorio con invio speciale
    this.praticaConSDInvioSpeciale(idPratica, idStatoDebitorio);
  }

  /**
   * Setta la rata fittizia in caso di cambio della pagina in inserimento per ripopolare il campo data scadenza.
   * @param configs IGenAmmDilSetFormValue con i dati di configurazione della form.
   */
  private setRataFittiziaOnPageChange(configs: IGenAmmDilSetFormValue) {
    // Solo se sono in inserimento. In modifica ho rate vere.
    if (this.inserimento) {
      // Prendo le rate
      const rates = configs?.statoDebitorio?.rate ?? [];
      // Controllo se ho almeno una rata
      if (rates.length > 0) {
        // Prendo la prima rata, che è sempre l'unica, fittizia
        this.rataFittizia = rates[0];
      }
    }
  }

  /**
   * ##########################
   * FUNZIONALITA' DEI PULSANTI
   * ##########################
   */

  /**
   * Funzione collegata al click del pulsante: Dettaglio dilazione.
   * La funzione aprirà una modale con il dettaglio per la dilazione.
   * Se il flag dilazione è attivo, verranno visualizzate anche le rate.
   */
  dettaglioDilazione() {
    // Recupero i dati dello stato debitorio
    const statoDebitorio = this.statoDebitorio;
    // Richiamo la funzione di apertura della modale di dettaglio
    this._genAmmDil.apriDettaglioDilazione(statoDebitorio);
  }

  /**
   * Funzione di supporto che gestisce il success del submit della form.
   * @param data GeneraliAmministrativiDilazione contenente le informazioni del form.
   * @override
   */
  protected onFormSuccess(data: GeneraliAmministrativiDilazione) {
    // Recupero le rate da settare nello stato debitorio
    const rate = this.rateSD;
    // Chiamo la funzione di aggiornamento dello stato debitorio
    this._datiContabiliUtility.updateGeneraliAmminitrativiDilazione(data, rate);
    // Recuperare lo stato debitorio aggiornato
    const statoDebitorio = this._datiContabiliUtility.statoDebitorio;

    // In base alla modalità eseguo verifica e salvataggio
    if (this.inserimento) {
      // Lancio il servizio di verifica ed inserimento dello stato debitorio
      this._datiContabili.verifyAndInsertStatoDebitorio(statoDebitorio);
    } else {
      // Lancio il servizio di verifica ed aggiornamento dello stato debitorio
      this._datiContabili.verifyAndUpdateStatoDebitorio(statoDebitorio);
    }
  }

  /**
   * ################################################
   * FUNZIONI PER LA COMUNICAZIONE DI ERRORI AL PADRE
   * ################################################
   */

  /**
   * Funzione di comodo che imposta il messaggio d'errore
   * @param error RiscaServerError che definisce il corpo dell'errore.
   */
  protected onServiziError(error: RiscaServerError) {
    // Emetto l'evento per la gestione degli errori per i figli dello stato debitorio
    this._statoDebitorioServ.onGADError(error);
  }

  /**
   * ##################################
   * FUNZIONI DI UTILITY DEL COMPONENTE
   * ##################################
   */

  /**
   * Funzione di comodo che crea i dati ed emette l'evento di cambio tab dello stato debitorio.
   * @param tabs IRiscaTabChanges che definisce l'oggetto per il cambio della tab.
   */
  private tabSDChanged(tabs: IRiscaTabChanges) {
    // Recupero l'identificativo di questo componente
    const tabComp = RiscaStatoDebitorio.generaliAmministrativiDilazione;

    // Verifico tramite servizio se ci si sta spostando su un'altra tab
    if (this._statoDebitorioServ.checkTabSD(tabs, tabComp)) {
      // Mi sto spostando, aggiorno i dati dello stato debitorio nel servizio
      const data = this.getMainFormActualRawValue();
      // Recupero il dato della rata
      const rate = this.rateSD;
      // Chiamo la funzione di aggiornamento dello stato debitorio
      this._datiContabiliUtility.updateGeneraliAmminitrativiDilazione(
        data,
        rate
      );
    }
  }

  /**
   * Funzione di intercettazione delle notifiche dai figli.
   * @param notify RiscaAlertConfigs | any con i dati della notifica.
   */
  protected notificationFromChild(notify: RiscaAlertConfigs | any) {
    // Emetto l'evento per la gestione degli errori per i figli dello stato debitorio
    this._statoDebitorioServ.onGADError(notify);
  }

  /**
   * ########################
   * GESTIONE DEGLI ACCORDION
   * ########################
   */

  /**
   * Funzione agganciata all'evento di change dell'accordion per: generali/sintesi.
   * @param isOpen boolean che definisce lo stato dell'accordion.
   */
  toggleGeneraliSintesi(isOpen: boolean) {
    // Gestisco localmente il flag
    this.generaliSintesiApriChiudi = !this.generaliSintesiApriChiudi;
  }

  /**
   * Funzione agganciata all'evento di change dell'accordion per: amministrativi/tecnici.
   * @param isOpen boolean che definisce lo stato dell'accordion.
   */
  toggleAmministrativiTecnici(isOpen: boolean) {
    // Gestisco localmente il flag
    this.amministrativiTecniciApriChiudi =
      !this.amministrativiTecniciApriChiudi;
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
   */
  get alertConfigsCheck() {
    // Verifico e ritorno la condizione
    return this._riscaAlert.alertConfigsCheck(this.alertConfigs);
  }

  /**
   * Getter di comodo per l'id della pratica attualmente in uso.
   * @returns number con l'id della pratica in uso.
   */
  get idPratica(): number {
    // Recupero tramite servizio l'id della pratica attiva
    return this._datiContabiliUtility.idPratica;
  }

  /**
   * Getter di comodo per l'id dello stato debitorio attualmente in uso.
   * @returns number con l'id dello stato debitorio in uso.
   */
  get idStatoDebitorio(): number {
    // Recupero tramite servizio l'id dello stato debitorio in uso
    return this.statoDebitorio?.id_stato_debitorio;
  }

  /**
   * Getter che nasconde il valore di istanza di rinnovo se non c'è il valore
   */
  get hideIstanzaDiRinnovo() {
    // Prendo il valore dell'istanza di rinnovo
    const istRinn = this._riscaUtilities.getFormValue(
      this.mainForm,
      this.GAD_C.ISTANZA_RINNOVO
    ) as string;
    // Determino se c'è o meno
    return istRinn == undefined || istRinn.length == 0;
  }

  /**
   * Getter per l'id pratica dell'oggetto riscossione.
   */
  get idRiscossione(): number {
    // Recupero, se esiste, l'id pratica
    return this.pratica?.id_riscossione;
  }

  /**
   * Getter di comodo per il recupero del form control: Importo da compensazione.
   */
  get importoCompensazioneFC() {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.GAD_C.IMPORTO_COMPENSAZIONE;
    // Ritorno il form control
    return f?.get(k);
  }

  /**
   * Getter di comodo per il recupero del form control: motivazione.
   */
  get motivazioneFC() {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.GAD_C.MOTIVAZIONE;
    // Ritorno il form control
    return f?.get(k);
  }

  /**
   * Getter di comodo per il recupero del form control: motivazione.
   */
  get dilazioneFC() {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.GAD_C.DILAZIONE;
    // Ritorno il form control
    return f?.get(k);
  }

  /**
   * Getter per la label del campo "motivazione".
   * Per una questione grafica non è stata gestita come label dell'input, ma come informazione esterna.
   */
  get labelMotivazione(): string {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.GAD_C.ANNULLATO;
    // Recupero il valore del flag "annullato"
    let annullato: IRiscaCheckboxData;
    annullato = this._riscaUtilities.getFormValue(f, k);

    // Definisco la label da ritornare per la motivazione
    let motivazione = this.GAD_C.LABEL_MOTIVAZIONE;

    // Verifico se esiste il dato
    if (annullato?.check) {
      // Il flag esiste ed è true, motivazione è obbligatoria
      motivazione = `*${motivazione}`;
    }

    // Ritorno la label
    return motivazione;
  }

  /**
   * Getter di comodo per il recupero del form control: invioSpecialePostel.
   */
  get invioSpecialePostel() {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.GAD_C.INVIO_SPECIALE_POSTEL;
    // Ritorno il form control
    return f?.get(k);
  }

  /**
   * Setter di comodo per la valorizzazione del form control: scadenzaPagamento.
   */
  set scadenzaPagamento(dataSP: string) {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.GAD_C.SCADENZA_PAGAMENTO;
    const v = dataSP;
    // Aggiorno il valore nella form
    this._riscaUtilities.setFormValue(f, k, v);
  }

  /**
   * Getter di comodo che gestisce e definisce i dati per le rate in base alla modalità del componente.
   * @returns RataVo[] con la lista di rate dello stato debitorio.
   */
  get rateSD(): RataVo[] {
    // Verifico la modalità del componente
    if (this.inserimento && this.rataFittizia) {
      // In inserimento non esistono rate, quindi ritorniamo una lista con la rata fittizzia (contiene data scadenza pagamento)
      return [this.rataFittizia];
      // #
    } else if (this.modifica) {
      // Siamo in modifica, quindi le rate già esistono per lo stato debitorio
      return this.statoDebitorio?.rate;
      // #
    } else {
      // Fallback
      return [];
    }
  }

  /**
   * Getter di comodo per la rata padre definita all'interno dello stato debitorio.
   * @returns RataVo con l'oggetto della rata padre, o undefined.
   */
  get rataPadre(): RataVo {
    // Mi trovo in modifica, devo aggiornare la rata padre dello stato debitorio istanziato localmente
    const rateSD: RataVo[] = this.statoDebitorio?.rate ?? [];
    // Recupero dalle rate dello sd la rata padre
    const rataPadre: RataVo = this._datiContabiliUtility.getRataPadre(rateSD);

    // Ritorno la rata padre
    return rataPadre;
  }

  /**
   * Getter di comodo che raccoglie le logiche per il blocco dell'interfaccia utente.
   * @returns boolean con l'insieme di condizioni di blocco.
   */
  get disableUserInputs(): boolean {
    // Ritorno le condizioni di disabilitazione
    return this.isPraticaLocked || this.AEA_detSDDisabled;
  }

  /**
   * Getter di comodo che verifica se la pratica è lockata.
   * @returns boolean con il risultato del check.
   */
  get isPraticaLocked(): boolean {
    // Verifico che esista nel servizio la configurazione del lock pratica
    const existLockStatus = this._riscaLockP.isPraticaInLockStatus();
    // Verifico se un altro utente sta lockando la pratica
    const anotherUserLocker = this._riscaLockP.isAnotherUserLockingPratica();
    // Ritorno le condizioni di disabilitazione
    return existLockStatus && anotherUserLocker;
  }

  /**
   * Getter per il recupero del flag per AEA: dettaglio stato debitorio.
   * @returns boolean con il valore della configurazione.
   */
  get AEA_detSDDisabled(): boolean {
    // Verifico se la configurazione di accesso agli elementi è attivo
    let AEA_detSDDisabled: boolean;
    AEA_detSDDisabled = this._accessoElementiApp.isAEADisabled(
      this.AEAK_C.DET_STATO_DEB
    );

    // Ritorno la configurazione
    return AEA_detSDDisabled;
  }
}
