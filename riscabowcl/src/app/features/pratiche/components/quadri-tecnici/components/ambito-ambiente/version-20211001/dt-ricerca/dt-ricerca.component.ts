import { Component, Inject, Input, OnDestroy, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { clone } from 'lodash';
import { PraticaDTVo } from '../../../../../../../../core/commons/vo/pratica-vo';
import { RiduzioneAumentoVo } from '../../../../../../../../core/commons/vo/riduzione-aumento-vo';
import { IUnitaMisuraVo } from '../../../../../../../../core/commons/vo/unita-misura-vo';
import { IUsoLeggeSpecificoVo } from '../../../../../../../../core/commons/vo/uso-legge-specifico-vo';
import { IUsoLeggeVo } from '../../../../../../../../core/commons/vo/uso-legge-vo';
import { LoggerService } from '../../../../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../../../../../core/services/user.service';
import { RicercaUsiDiLeggeTable } from '../../../../../../../../shared/classes/risca-table/ricerca-avanzata/ricerca-usi-di-legge.table';
import { RiscaTableDataConfig } from '../../../../../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../../../../../shared/consts/common-consts.consts';
import {
  dataInizioDataFineValidator,
  minMaxNumberValidator,
  numberCompositionValidator,
} from '../../../../../../../../shared/miscellaneous/forms-validators/forms-validators';
import { RiscaFormBuilderService } from '../../../../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaStorageService } from '../../../../../../../../shared/services/risca/risca-storage.service';
import { RiscaUtilitiesService } from '../../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  FormUpdatePropagation,
  RiscaRegExp,
  RiscaServerError,
} from '../../../../../../../../shared/utilities';
import { RiscaErrorsMap } from '../../../../../../../../shared/utilities/classes/errors-maps';
import { PraticheService } from '../../../../../../service/pratiche.service';
import { CodModalitaRicerca } from '../../../../../ricerca-avanzata/form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.enums';
import {
  IFRAPModalitaRicerca,
  IRicercaPraticaAvanzataForm,
} from '../../../../../ricerca-avanzata/form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.interfaces';
import { DatiTecniciAmbienteConverterService } from '../../../../services/ambito-ambiente/version-20211001/dati-tecnici-ambiente-converter.service';
import { DatiTecniciAmbienteService } from '../../../../services/ambito-ambiente/version-20211001/dati-tecnici-ambiente.service';
import { DatiTecniciService } from '../../../../services/dati-tecnici/dati-tecnici.service';
import { QuadriTecniciRicercaService } from '../../../../services/quadri-tecnici/ricerca/quadri-tecnici-ricerca.service';
import {
  DTRicercaConfig,
  DT_RICERCA_CONFIG,
} from '../../../../utilities/configs/dt-ricerca.injectiontoken';
import {
  IDatiTecniciAmbiente,
  UsoLeggePSDAmbienteInfo,
} from '../../../../utilities/interfaces/dt-ambiente-pratica.interfaces';
import { UsoDiLeggeInfoRicerca } from '../../../../utilities/vo/dati-tecnici-ambiente-ricerca-vo';
import { RicercaDatiTecniciComponent } from '../../../core/ricerca-dati-tecnici/ricerca-dati-tecnici.component';
import { DTRicerca20211001Consts } from './utilities/dt-ricerca.consts';
import { DTRicerca20211001FieldsConfigClass } from './utilities/dt-ricerca.fields-configs';

/**
 * Componente per la gestione del form dati tecnici, ambito: ambiente.
 * Il form principale non ha un submit diretto.
 * Questo deve essere gestito dal padre.
 * Le informazioni del form verranno emesse registrandosi all'event emitter onFormChange.
 */
@Component({
  selector: 'dt-ricerca',
  templateUrl: './dt-ricerca.component.html',
  styleUrls: ['./dt-ricerca.component.scss'],
})
export class DTRicerca20211001Component
  extends RicercaDatiTecniciComponent<PraticaDTVo>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti utilizzate comuni. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti utilizzate nel componente. */
  RDTA_C = new DTRicerca20211001Consts();
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();
  /** Oggetto RiscaRegExp utilizzate come supporto per la validazione dei campi. */
  private riscaRegExp: RiscaRegExp = new RiscaRegExp();

  /** Input per i dati in caso di modifica. */
  @Input('datiTecniciCore') configs: string;

  /** Form group che definisce la struttura della sotto form "usi". */
  usiForm: FormGroup;
  /** Flag che tiene traccia se il form è stato submittato per: usiForm. */
  usiFormSubmitted = false;
  /** Boolean che definisce lo stato dell'accordion per gli usi di legge. */
  usiApriChiudi = false;

  /** Array di oggetti UsoLeggeVo recuperata dal server. */
  listaUsiDiLegge: IUsoLeggeVo[] = [];
  /** Array di oggetti UsoLeggeSpecificoVo recuperata dal server tramite informazioni dentro oggetto UsoLeggeVo. */
  listaUsiDiLeggeSpecifici: IUsoLeggeSpecificoVo[] = [];
  /** Array di oggetti RiduzioneAumentoVo recuperata dal server tramite informazioni dentro oggetto UsoLeggeVo. */
  listaPercRiduzione: RiduzioneAumentoVo[] = [];
  /** Array di oggetti RiduzioneAumentoVo recuperata dal server tramite informazioni dentro oggetto UsoLeggeVo. */
  listaPercAumento: RiduzioneAumentoVo[] = [];

  /** Classe DTAFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: DTRicerca20211001FieldsConfigClass;
  /** Oggetto UsiDiLeggeTable che contiene le informazioni della tabella usi di legge. */
  usiTable: RicercaUsiDiLeggeTable;

  /**
   * Costruttore.
   */
  constructor(
    @Inject(DT_RICERCA_CONFIG) injConfig: DTRicercaConfig,
    private _formBuilder: FormBuilder,
    private _datiTecniciAmbiente: DatiTecniciAmbienteService,
    private _datiTecnici: DatiTecniciService,
    private _dtaConverter: DatiTecniciAmbienteConverterService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    pratiche: PraticheService,
    ricercaQuadriTecnici: QuadriTecniciRicercaService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaStorage: RiscaStorageService,
    riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {
    // Richiamo il super per il setup della classe estesa
    super(
      injConfig,
      logger,
      navigationHelper,
      pratiche,
      ricercaQuadriTecnici,
      riscaFormSubmitHandler,
      riscaStorage,
      riscaUtilities
    );
    // Funzione di setup generico
    this.setupComponente();
    // Caricamento dei dati per la lista degli usi di legge
    this.getUsiDiLegge();
  }

  ngOnInit() {
    // Funzione di setup per la form
    this.initForms();
    // Iniizializzazioni delle configurazioni
    this.initCoreConfigs();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
    // Il codice seguente serve a ripristinare gli usi disattivati dalle selezioni della ricerca dopo che si è cambiata la pagina
    // Svuoto la tabella degli usi
    this.usiTable.source = [];
    // Aggiorno il form
    this.aggiornaUsiLeggeInForm();
    // Aggiorno la struttura della listaUsiDiLegge
    this.disabilitaUsiDiLeggeInseriti();
  }

  /**
   * Funzione di comodo che racchiude le funzioni di setup per il componente.
   */
  private setupComponente() {
    // Setep degli errori da verificare al submit della form
    this.setupMainFormErrors();
    // Setup delle input del calendario
    this.setupFormInputs();
    // Funzione di setup per le tabelle del componente
    this.setupTabelle();
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  private setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [
      ...this.EM.MAP_FORM_GROUP_REQUIRED,
      ...this.EM.MAP_FORMATO_NUMERO_6_INT_4_DEC,
      ...this.EM.MAP_ALMENO_UN_ELEMENTO,
      ...this.EM.MAP_DATA_INIZIO_DATA_FINE_INVALID,
      ...this.EM.MAP_MIN_MAX_CHECK,
    ];
  }

  /**
   * Funzione di setup per le input del form.
   */
  private setupFormInputs() {
    // Essendo le configurazioni parecchie, si utilizza una classe di supporto
    this.formInputs = new DTRicerca20211001FieldsConfigClass(
      this._riscaFormBuilder
    );
  }

  /**
   * Funzione che inizializza le tabelle del componente.
   */
  private setupTabelle() {
    // Setup della tabella usiTable
    this.usiTable = new RicercaUsiDiLeggeTable();
  }

  /**
   * Funzione che effettua il setup per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Setup della struttura della form principale #####
    this.initFormDatiTecniciAmbiente();
    // Setup della struttura della sotto form usi #####
    this.initFormUsi();
    // Setup dei listiner dei form per la manipolazione dei dati
    this.initFormListener();
  }

  /**
   * Funzione che definisce le logiche di init per le configurazioni del componente.
   */
  private initCoreConfigs() {
    // Verifico che esista la configurazione
    if (!this.configs) {
      // Nessuna configurazione
      return;
    }

    // Variabile di comodo
    const c = this.configs;
    // Effettuo la conversione dell'oggetto
    this._dtaConverter
      .convertDatiTecniciCoreRicercaToDatiTecniciAmbienteRicerca(c)
      .subscribe({
        next: (dta: IDatiTecniciAmbiente) => {
          // Lancio il setup delle tabelle
          this.initComponenteConfigs(dta);
        },
        error: (e: RiscaServerError) => {
          // Loggo l'errore
          this._logger.error('initDatiTecniciAmbiente', e);
        },
      });
  }

  /**
   * Funzione specifica adibita al setup del form mainForm.
   */
  private initFormDatiTecniciAmbiente() {
    this.mainForm = this._formBuilder.group({
      corpoIdricoCaptazione: new FormControl(
        { value: '', disabled: false },
        { validators: [] }
      ),
      comune: new FormControl(
        { value: '', disabled: false },
        { validators: [] }
      ),
      nomeImpiantoIdroElettrico: new FormControl(
        { value: '', disabled: false },
        { validators: [] }
      ),
      portataDaAssegnare: new FormControl(
        { value: null, disabled: false },
        {
          validators: [numberCompositionValidator(6, 4)],
        }
      ),
      usiDiLegge: new FormControl(
        { value: null, disabled: false },
        {
          validators: [],
        }
      ),
    });
  }

  /**
   * Funzione specifica adibita al setup del form usiForm.
   */
  private initFormUsi() {
    this.usiForm = this._formBuilder.group(
      {
        usoDiLegge: new FormControl(
          { value: '', disabled: false },
          { validators: [] }
        ),
        usiDiLeggeSpecifici: new FormControl(
          { value: [], disabled: true },
          { validators: [] }
        ),
        /**
         * Per la gestione di input che hanno un modello dati come oggetto, si crea un form group innestato.
         * I form control saranno i nomi delle proprietà dell'oggetto: UnitaMisuraVo.
         */
        unitaDiMisura: this._formBuilder.group({
          des_unita_misura: new FormControl({ value: '', disabled: true }),
          id_unita_misura: new FormControl({ value: null, disabled: true }),
          ordina_unita_misura: new FormControl({ value: null, disabled: true }),
          sigla_unita_misura: new FormControl({ value: '', disabled: true }),
        }),
        unitaDiMisuraDesc: new FormControl(
          { value: [], disabled: true },
          { validators: [] }
        ),
        quantitaDa: new FormControl(
          { value: null, disabled: false },
          {
            validators: [
              Validators.min(0),
              Validators.pattern(this.riscaRegExp.onlyNumberWDecimal),
              numberCompositionValidator(6, 4),
            ],
          }
        ),
        quantitaA: new FormControl(
          { value: null, disabled: false },
          {
            validators: [
              Validators.min(0),
              Validators.pattern(this.riscaRegExp.onlyNumberWDecimal),
              numberCompositionValidator(6, 4),
            ],
          }
        ),
        quantitaFaldaProfonda: new FormControl(
          { value: null, disabled: false },
          {
            validators: [
              Validators.min(0),
              Validators.pattern(this.riscaRegExp.onlyNumberWDecimal),
              numberCompositionValidator(6, 4),
            ],
          }
        ),
        dataScadenzaEmasIsoDa: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        dataScadenzaEmasIsoA: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
      },
      {
        validators: [
          dataInizioDataFineValidator(
            this.RDTA_C.DATA_SCADENZA_EMAS_ISO_DA,
            this.RDTA_C.DATA_SCADENZA_EMAS_ISO_A
          ),
          minMaxNumberValidator(
            this.RDTA_C.QUANTITA_DA,
            this.RDTA_C.QUANTITA_A
          ),
        ],
      }
    );
  }

  /**
   * Funzione specifica adibita al setup dei listner delle form.
   * La funzione registrerà diversi eventi sulle form, atte a gestire la logica e la manipolazione dei dati.
   */
  private initFormListener() {
    // Listener al cambio di un uso di legge, verranno popolate le informazioni dei campi connessi
    this.usiForm
      .get(this.RDTA_C.USO_DI_LEGGE)
      .valueChanges.subscribe((usoDiLeggeUpd: IUsoLeggeVo) => {
        // Controllo che l'oggetto esista e abbia le informazioni per lo scarico dei dati connessi
        if (!usoDiLeggeUpd || !usoDiLeggeUpd.id_tipo_uso) {
          return;
        }

        // Chiamo la funzione per la gestione di usi di legge specifici
        this.getUsiDiLeggeSpecifici(usoDiLeggeUpd);
        // Chiamo la funzione per la gestione dell'unita di misura
        this.getUnitaDiMisura(usoDiLeggeUpd);
      });
  }

  /**
   * Popola i campi della form in base all'oggetto da modificare che é stato passato.
   * @param dta DatiTecniciAmbiente con i dati di configurazione per il componente.
   */
  private initComponenteConfigs(dta: IDatiTecniciAmbiente) {
    // verifico esistano configurazioni
    if (!dta) {
      // Niente configurazioni
      return;
    }

    // In questa parte si controllano tutte le proprietà dell'oggetto dta e prevalorizzare
    // i relativi campi di mainForm

    // Esistono 2 passaggi:
    //- questa funzione va ad assegnare i valori del form che non dipendono da liste
    // tutto ciò che non è dipendente dalle liste scaricate lo metto qui
    //- tutto ciò che è dipendente dalle liste lo devo andare a gestire all'interno dell'onScarico()

    if (dta.comune != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RDTA_C.COMUNE,
        dta.comune
      );
    }

    if (dta.corpoIdricoCaptazione != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RDTA_C.CORPO_IDRICO_CAPTAZIONE,
        dta.corpoIdricoCaptazione
      );
    }

    if (dta.nomeImpiantoIdroElettrico != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RDTA_C.NOME_IMPIANTO_IDROELETTRICO,
        dta.nomeImpiantoIdroElettrico
      );
    }

    if (dta.portataDaAssegnare != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RDTA_C.PORTATA_DA_ASSEGNARE,
        dta.portataDaAssegnare
      );
    }

    // Popolo le liste degli usi di legge
    if (dta.usiDiLegge != undefined) {
      // Resetto gli usi di legge
      this.usiTable.source = [];
      // Reinserisco gli usi di legge
      this.usiTable.addElements(dta.usiDiLegge).subscribe({
        next: (insert: boolean) => {
          // Se l'add di tutti gli elementi è andato a buon fine, aggiorno tutte le strutture correlate
          if (insert) {
            // Aggiorno il form con i dati della tabella
            this.aggiornaUsiLeggeInForm();
            // Aggiorno la struttura della listaUsiDiLegge disabilitando le voci prensenti negli usi di legge
            this.disabilitaUsiDiLeggeInseriti();
          }
        },
        error: (e: any) => {
          // Log di errore
          this._logger.error('initComponenteConfigs', e);
        },
      });
    }
  }

  /**
   * Funzione adibita al recupero dati dal server per gli usi di legge.
   */
  private getUsiDiLegge() {
    // Recupero l'id dell'ambito
    const idAmbito = this._user.idAmbito;
    // Chiamata al server per il recupero dati per gli usi di legge (null permette di recuperare tutti gli usi di legge)
    this._datiTecnici.getUsiDiLegge(idAmbito).subscribe((res) => {
      // Aggiorno la lista degli usi di legge
      this.listaUsiDiLegge = clone(res);
    });
  }

  /**
   * Funzione adibita al recupero dati dal server per gli usi di legge specifici, dato un uso di legge.
   * @param usoDiLegge UsoLeggeVo contenente i dati per il recupero dati.
   */
  private getUsiDiLeggeSpecifici(usoDiLegge: IUsoLeggeVo) {
    // Recupero l'idAmbito
    const idUso = usoDiLegge.id_tipo_uso;
    const idAmbito = this._user.idAmbito;
    // Chiamata al server per il recupero dati per gli usi di legge (null permette di recuperare tutti gli usi di legge)
    this._datiTecnici.getUsiDiLegge(idAmbito, idUso).subscribe({
      next: (res) => {
        // Aggiorno la lista degli usi di legge specifici
        this.listaUsiDiLeggeSpecifici = res;

        // Controllo che ci siano dei valori selezionabili
        if (this.listaUsiDiLeggeSpecifici?.length > 0) {
          // Abilito la multi select tramite chiave form control
          this.usiForm.get(this.RDTA_C.USI_DI_LEGGE_SPECIFICI).enable();
        } else {
          // Disabilito la multi select tramite chiave form control
          this.usiForm.get(this.RDTA_C.USI_DI_LEGGE_SPECIFICI).disable();
        }
      },
      error: (err) => {
        // Loggo l'azione
        this._logger.error('getUsiDiLeggeSpecifici', err);
      },
    });
  }

  /**
   * Funzione adibita al recupero dati dal server per l'unita di misura, dato un uso di legge.
   * @param usoDiLegge UsoLeggeVo contenente i dati per il recupero dati.
   */
  private getUnitaDiMisura(usoDiLegge: IUsoLeggeVo) {
    // Chiamata al server per il recupero dati per l'unita di misura
    this._datiTecnici.getUnitaDiMisura(usoDiLegge.id_unita_misura).subscribe(
      (res: IUnitaMisuraVo) => {
        // Recupero i dati per l'aggiornamento
        const f = this.usiForm;
        const uDM = this.RDTA_C.UNITA_DI_MISURA;
        const uDMD = this.RDTA_C.UNITA_DI_MISURA_DESC;
        const v = res;

        // Aggiorno le informazioni per il form control del form group
        this._riscaUtilities.setFormValue(f, uDM, v);
        // Aggiorno anche la descrizione per la visualizzazione all'utente
        this._riscaUtilities.setFormValue(f, uDMD, v.des_unita_misura);
      },
      (err) => {
        // Loggo l'azione
        this._logger.error('getUnitaDiMisura', err);
      }
    );
  }

  /**
   * #############################
   * FUNZIONI DI GESTIONE DEL FORM
   * #############################
   */

  prepareMainFormForValidation() {
    if (!this.mainForm || !this.usiForm) {
      return undefined;
    }
  }

  /**
   * ##########################
   * GESTIONE FORM USI DI LEGGE
   * ##########################
   */

  /**
   * Funzione di toggle per la visualizzazione della form usiDiLegge.
   * @param stato boolean che definisce il nuovo stato dello switch accordion.
   */
  toggleUsiDiLegge(stato: boolean) {
    // Effettuo il toggle per la visualizzazione
    this.usiApriChiudi = !this.usiApriChiudi;
  }

  /**
   * Funzione di comodo che aggiorna i dati del FormGroup inserendo le informazioni degli usi di legge.
   */
  private aggiornaUsiLeggeInForm() {
    // Recupero i dati della tabella usi
    const usiLegge: UsoLeggePSDAmbienteInfo[] =
      this.usiTable.getDataSource() || [];
    // Definisco la chiave del form per il campo
    const formKey = this.RDTA_C.USI_DI_LEGGE;

    // Aggiungo i dati della tabella all'interno del form per gestire la validazione
    this._riscaUtilities.setFormValue(this.mainForm, formKey, usiLegge);
  }

  /**
   * Funzione che aggiunge i dati Uso all'interno della tabella usiTable.
   * @param usoDiLegge UsoDiLeggeInfo contenente i dati della form usiForm.
   */
  private aggiungiUso(usoDiLegge: UsoLeggePSDAmbienteInfo) {
    // Aggiungo alla tabella un nuovo elemento
    this.usiTable.addElement(usoDiLegge);
    // Aggiorno il form con i dati della tabella
    this.aggiornaUsiLeggeInForm();

    // Aggiorno la struttura della listaUsiDiLegge disabilitando le voci prensenti negli usi di legge
    this.disabilitaUsiDiLeggeInseriti();
  }

  /**
   * Funzione che rimuove i dati dalla tabella usi.
   * @param row RiscaTableDataConfig<any> contenente i dati della riga.
   */
  eliminaUso(row: RiscaTableDataConfig<any>) {
    // Effettuo la rimozione della riga
    this.usiTable.removeElement(row, (a, b) => {
      return a.id == b.id;
    });
    // Aggiorno il form con i dati della tabella
    this.aggiornaUsiLeggeInForm();

    // Aggiorno la struttura della listaUsiDiLegge disabilitando le voci prensenti negli usi di legge
    this.disabilitaUsiDiLeggeInseriti();
  }

  /**
   * Funzione che aggiunge un flag "disabled" agli oggetti "UsoDiLegge" per impedire il multi-inserimento.
   */
  private disabilitaUsiDiLeggeInseriti() {
    // Recupero i dati per gli usi di legge inseriti
    const usiLeggeIns = this.usiTable.getDataSource();
    // Aggiorno la lista degli usi di legge
    this.listaUsiDiLegge = this._datiTecniciAmbiente.disabilitaUsiDiLegge(
      this.listaUsiDiLegge,
      usiLeggeIns
    );
  }

  /**
   * Funzione agganciata al bottone di Submit per la form usiForm.
   */
  usiSubmit() {
    // Il form è stato submittato
    this.usiFormSubmitted = true;
    // Verifico che la form sia valida
    if (this.usiForm.valid) {
      // Ottiene l'uso dalla form
      const uso: UsoDiLeggeInfoRicerca = this.usiForm.getRawValue();
      // Controllo se l'uso è stato valorizzato
      if (this._riscaUtilities.isEmptyObjectDeep(uso)) {
        return;
      }
      // Recupero i dati dalla form mediante la getRawValue (recupera i dati anche delle input disattivate)
      this.aggiungiUso(uso);
      // Resetto la form
      this.resetUsiForm();
    }
  }

  /**
   * Funzione di reset della form: usiForm.
   */
  private resetUsiForm() {
    // Resetto i campi della form usiForm
    this.usiForm.reset();
    // Resetto il flag di submit
    this.usiFormSubmitted = false;
    // Resetto le liste di supporto
    this.listaUsiDiLeggeSpecifici = [];
    this.listaPercRiduzione = [];
    this.listaPercAumento = [];
    // Disabilito la multi select tramite chiave form control
    this.usiForm.get(this.RDTA_C.USI_DI_LEGGE_SPECIFICI).disable();
  }

  /**
   * ########################
   * GESTIONE FORM PRINCIPALE
   * ########################
   */

  /**
   * Funzione che effettua il reset dei dati della form datti tecnici ambiente.
   * Richiamabile dal padre.
   * @override
   */
  onFormReset() {
    // Resetto gli usi di legge disabilitati
    this.listaUsiDiLegge.forEach((x: any) => (x.__disabled = false));
    // Resetto il flag di submit
    this.mainFormSubmitted = false;
    // Richiamo il reset sulla form dati tecnici ambiente
    this.mainForm.reset();

    // Resetto la form Usi
    this.resetUsiForm();
    // Resetto la tabella degli usi
    this.setupTabelle();
  }

  /**
   * Funzione che permette il recupero del raw value del main form del child.
   * @param c any che definisce una configurazione generica che permette di gestire logiche nell'override della funzione.
   * @returns T contente le informazioni del form in modalità raw value.
   * @override
   */
  getMainFormRawValue(c?: any): PraticaDTVo {
    // Verifico esista l'oggetto
    if (!this.mainForm || !this.usiForm) {
      return undefined;
    }
    // Recupero i dati dal form di localizzazione
    const dtaLocalizzazione: any = this.mainForm.getRawValue();
    // Recupero i dati dalla tabella degli usi
    dtaLocalizzazione.usiDiLegge = this.usiTable.getDataSource();
    // Converto l'oggetto dta in un oggetto PraticaDTVo
    const pdtVo =
      this._ricercaQuadriTecnici.convertRicercaDatiTecniciAmbienteToPraticaDTVoRicerca(
        dtaLocalizzazione
      );

    // Ritorno il dato aggiornato
    return pdtVo;
  }

  /**
   * #############################################
   * OVERRIDE DELLE FUNZIONI DEL COMPONENTE ESTESO
   * #############################################
   */

  /**
   * La funzione viene invocata quando l'evento di cambio dati del form della ricerca avanzata è attivato.
   * @param data IRicercaPraticaAvanzataReq con le informazioni del form di ricerca avanzata modificate.
   * @override
   */
  protected onRicercaAvanzataChange(data: IRicercaPraticaAvanzataForm) {
    // Recupero dall'oggetto passato in input la modalita ricerca
    const modalitaRicerca: IFRAPModalitaRicerca = data?.modalitaRicerca;
    // Richiamo la funzione di gestione della modalita ricerca
    this.modalitaRicercaChange(modalitaRicerca);
  }

  /**
   * ########################################
   * GESTIONE DELLA MODALITA RICERCA AVANZATA
   * ########################################
   */

  /**
   * Funzione che gestisce le operazioni logiche nel momento in cui l'utente modifica la modalita di ricerca.
   * @param modalitaRicerca IFRAPModalitaRicerca con la nuova modalità selezionata.
   */
  private modalitaRicercaChange(modalitaRicerca: IFRAPModalitaRicerca) {
    // Verifico quale modalità di ricerca è stata selezionata
    switch (modalitaRicerca?.cod_modalita_ricerca) {
      // Pratica
      case CodModalitaRicerca.pratica:
        // Gestisco pulizia e disabilitazioni dei campi
        this.onModalitaRicercaPratica();
        break;
      // Stato debitorio
      case CodModalitaRicerca.statoDebitorio:
        // Gestisco pulizia e disabilitazioni dei campi
        this.onModalitaRicercaStatoDebitorio();
        break;
    }
  }

  /**
   * Funzione invocata quando l'utente modifica la modalità di ricerca in: Pratica.
   */
  private onModalitaRicercaPratica() {
    // Chiavi pre i campi d'aggiornare
    const dataScadenzaEmasIsoDa: AbstractControl = this.dataScadenzaEmasIsoDa;
    const dataScadenzaEmasIsoA: AbstractControl = this.dataScadenzaEmasIsoA;
    // Definisco la configurazione per l'aggiornamento del form
    const update: FormUpdatePropagation = { emitEvent: false };

    // Resetto i valori dei campi che non sono gestiti da questa modalita
    dataScadenzaEmasIsoDa?.setValue(null, update);
    dataScadenzaEmasIsoA?.setValue(null, update);
    // Abilito la data emas iso, per "da" e "a"
    dataScadenzaEmasIsoDa?.enable(update);
    dataScadenzaEmasIsoA?.enable(update);
  }

  /**
   * Funzione invocata quando l'utente modifica la modalità di ricerca in: Stato Debitorio.
   */
  private onModalitaRicercaStatoDebitorio() {
    // Chiavi pre i campi d'aggiornare
    const dataScadenzaEmasIsoDa: AbstractControl = this.dataScadenzaEmasIsoDa;
    const dataScadenzaEmasIsoA: AbstractControl = this.dataScadenzaEmasIsoA;
    // Definisco la configurazione per l'aggiornamento del form
    const update: FormUpdatePropagation = { emitEvent: false };

    // Resetto i valori dei campi che non sono gestiti da questa modalita
    dataScadenzaEmasIsoDa?.setValue(null, update);
    dataScadenzaEmasIsoA?.setValue(null, update);
    // Disabilito la data emas iso, per "da" e "a"
    dataScadenzaEmasIsoDa?.disable(update);
    dataScadenzaEmasIsoA?.disable(update);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  get existTableUsi() {
    return this.usiTable?.source?.length > 0;
  }

  /**
   * Getter di comodo per il form control di: Data Scadenza Emas Iso Da.
   * @returns AbstractControl con l'istanza del form control.
   */
  get dataScadenzaEmasIsoDa(): AbstractControl {
    // Definisco la chiave per il form control
    const keyFC = this.RDTA_C.DATA_SCADENZA_EMAS_ISO_DA;
    // Ritorno il form control
    return this.usiForm?.get(keyFC);
  }

  /**
   * Getter di comodo per il form control di: Data Scadenza Emas Iso A.
   * @returns AbstractControl con l'istanza del form control.
   */
  get dataScadenzaEmasIsoA(): AbstractControl {
    // Definisco la chiave per il form control
    const keyFC = this.RDTA_C.DATA_SCADENZA_EMAS_ISO_A;
    // Ritorno il form control
    return this.usiForm?.get(keyFC);
  }
}
