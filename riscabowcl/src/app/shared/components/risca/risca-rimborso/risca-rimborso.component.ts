import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { Moment } from 'moment';
import { Gruppo, GruppoVo } from 'src/app/core/commons/vo/gruppo-vo';
import { RimborsoVo } from 'src/app/core/commons/vo/rimborso-vo';
import { SoggettoVo } from 'src/app/core/commons/vo/soggetto-vo';
import { StatoDebitorioVo } from 'src/app/core/commons/vo/stato-debitorio-vo';
import { RimborsiConsts } from 'src/app/features/pratiche/components/dati-contabili/rimborsi/utilities/rimborsi.consts';
import { TipoRimborsoVo } from '../../../../core/commons/vo/tipo-rimborso-vo';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { DatiContabiliUtilityService } from '../../../../features/pratiche/service/dati-contabili/dati-contabili/dati-contabili-utility.service';
import { RiscaFormBuilderService } from '../../../services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  IFormCercaSoggetto,
  RiscaServerError,
} from '../../../utilities';
import { RiscaFormChildComponent } from '../risca-form-child/risca-form-child.component';
import { RimborsiService } from './../../../../features/pratiche/service/rimborsi/rimborsi.service';
import { RiscaErrorsMap } from './../../../utilities/classes/errors-maps';
import { RimborsoFieldsConfigClass } from './utilities/risca-rimborso.fields-configs';
import { IRimborsoForm } from './utilities/risca-rimborso.interfaces';

@Component({
  selector: 'risca-rimborso',
  templateUrl: './risca-rimborso.component.html',
  styleUrls: ['./risca-rimborso.component.scss'],
})
export class RiscaRimborsoComponent
  extends RiscaFormChildComponent<RimborsoVo>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente una serie di costanti per la gestione degli errori. */
  EM = new RiscaErrorsMap();
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  RMB_C = RimborsiConsts;

  /** Oggetto da modificare/inserire */
  @Input() rimborso?: RimborsoVo;
  /** Soggetto di default a cui associare il rimborso in inserimento. */
  @Input() soggettoDefault?: SoggettoVo;
  /** number di default a cui associare il rimborso in inserimento. */
  @Input() idGruppoDefault?: number;
  /** Evento di click sul pulsante per il cerca creditore. Sarà il padre a decidere le logiche di apertura e ricerca. */
  @Output() onCercaCreditore = new EventEmitter<any>();

  /** Classe RimborsoFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: RimborsoFieldsConfigClass;

  /** Lista di oggetti TipoRimborsoVo contenente le informazioni per la lista attività. */
  listaTipiRimborsi: TipoRimborsoVo[] = [];

  /** Boolean che definisce lo stato d'attivazione del pulsante cerca creditore. */
  disableCercaCreditore: boolean = false;

  /** StatoDebitorioVo con le informazioni dello stato debitorio per i accertamenti. */
  statoDebitorio: StatoDebitorioVo;

  /**
   * Costruttore.
   */
  constructor(
    private _formBuilder: FormBuilder,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    private _riscaRimborso: RimborsiService,
    private _datiContabiliUtility: DatiContabiliUtilityService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(logger, navigationHelper, riscaFormSubmitHandler, riscaUtilities);
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit(): void {
    // Lancio il setup delle form
    this.initForms();
    // Setup delle liste per le select del form
    this.initListeSelect();
  }

  /**
   * ngOnDestroy.
   */
  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * Funzione di comodo che racchiude le funzioni di setup per il componente.
   */
  private setupComponente() {
    // Setep degli errori da verificare al submit della form
    this.setupMainFormErrors();
    // Setup delle input del calendario
    this.setupFormInputs();
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  private setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [...this.EM.MAP_FORM_GROUP_REQUIRED];
  }

  /**
   * Funzione di setup per le input del form.
   */
  private setupFormInputs() {
    // Genero il setup per i campi
    this.formInputs = new RimborsoFieldsConfigClass(this._riscaFormBuilder);
  }

  /**
   * Funzione che effettua il setup per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Setup del form
    this.initRecapitoForm();
    // Setup dello stato debitorio
    this.setupStatoDebitorio();
  }

  /**
   * Setup del form.
   */
  private initRecapitoForm() {
    // Controllo se mi è stato passato un input
    if (this.modalita == AppActions.inserimento) {
      // Creo un oggetto rimborso vuoto
      this.rimborso = new RimborsoVo();
      // Assegno al rimborso vuoto il soggetto di default
      this.rimborso.soggetto_rimborso = this.soggettoDefault;
      // Assegno al rimborso vuoto il gruppo di default
      this.rimborso.id_gruppo_soggetto = this.idGruppoDefault;
    }

    const {
      soggetto_rimborso,
      tipo_rimborso,
      imp_rimborso,
      imp_restituito,
      num_determina,
      data_determina,
      causale,
      id_gruppo_soggetto,
    } = this.rimborso;

    const tipoRimborso = this.listaTipiRimborsi.find(
      (a) => a.cod_tipo_rimborso == tipo_rimborso.cod_tipo_rimborso
    );

    const dataDetermina = this._riscaUtilities.convertMomentToNgbDateStruct(
      data_determina as Moment
    );

    this.mainForm = this._formBuilder.group({
      soggetto: new FormControl(
        { value: soggetto_rimborso ?? null, disabled: false },
        { validators: [] }
      ),
      codiceFiscale: new FormControl(
        { value: soggetto_rimborso?.cf_soggetto ?? null, disabled: false },
        { validators: [] }
      ),
      ragioneSociale: new FormControl(
        { value: soggetto_rimborso?.den_soggetto ?? null, disabled: true },
        { validators: [] }
      ),
      tipoRimborso: new FormControl(
        { value: tipoRimborso ?? null, disabled: false },
        { validators: [Validators.required] }
      ),
      importoEccedente: new FormControl(
        { value: imp_rimborso ?? null, disabled: false },
        { validators: [Validators.required] }
      ),
      numeroProvvedimento: new FormControl(
        { value: num_determina ?? null, disabled: false },
        { validators: [Validators.required, Validators.maxLength(30)] }
      ),
      dataProvvedimento: new FormControl(
        { value: dataDetermina ?? null, disabled: false },
        { validators: [Validators.required] }
      ),
      causaleRimborso: new FormControl(
        { value: causale ?? null, disabled: false },
        { validators: [Validators.required, Validators.maxLength(300)] }
      ),
      idGruppo: new FormControl(
        { value: id_gruppo_soggetto ?? null, disabled: true },
        { validators: [] }
      ),
    });
    // Pulisco eventuali errori
    // this.mainForm.markAsPristine();
  }

  /**
   * Funzione di setup per le liste per le select del form.
   */
  private initListeSelect() {
    // Recupero il comune se configuratio e le province
    this._riscaRimborso.getTipiRimborso().subscribe({
      next: (tipiRimborso: TipoRimborsoVo[]) => {
        // Richiamo le funzioni di gestione delle liste
        this.onScaricoTipiAttivitaSvolteR(tipiRimborso);

        // Verfico che esista l'oggeto del form
        if (this.mainForm) {
          // Richiamo il pristine
          this.mainForm.markAsPristine();
        }
        // #
      },
      error: (e: RiscaServerError) => {
        // Loggo l'errore
        this._logger.error('initListeSelect', e);
        // #
      },
    });
  }

  /**
   * Funzione invocata allo scarico dei tipi sede.
   * @param tipiAttivita Array di TipoSedeVo scaricati.
   */
  private onScaricoTipiAttivitaSvolteR(tipiAttivita: TipoRimborsoVo[]) {
    // Assegno localmente la lista
    this.listaTipiRimborsi = tipiAttivita;
    // Se sono in modifica, devo settare il tipo attività
    if (this.modalita == AppActions.modifica && this.rimborso) {
      // Prendo l'idAttivita
      const { tipo_rimborso } = this.rimborso;
      // Funzione i ricerca
      const find = (a: TipoRimborsoVo, b: TipoRimborsoVo) => {
        // Controllo per codice
        return a.cod_tipo_rimborso == b.cod_tipo_rimborso;
      };
      // Se ho trovato l'attività la assegno
      this._riscaUtilities.setFormValueAndSelect(
        this.mainForm,
        this.RMB_C.TIPO_RIMBORSO,
        this.listaTipiRimborsi,
        tipo_rimborso,
        find
      );
    }
  }

  /**
   * #####################
   * METODI DEL COMPONENTE
   * #####################
   */

  /**
   * Funzione lanciata all'evento di aggiunta di una attività rimborso
   */
  aggiungiAttivitaRimborso() {
    // Se la form è valida la committo, altrimenti no
    this.onFormSubmit();
  }

  /**
   * ######################
   * FUNZIONALITA' DEL FORM
   * ######################
   */

  /**
   * Funzione di apertura della modale di selezione del soggetto del rimborso
   */
  cercaCreditore() {
    // Emetto l'evento di click sul pulsante ricerca creditore
    this.onCercaCreditore.emit();
  }

  /**
   * Funzione che gestisce i dati nel momento in cui viene selezionato un creditore.
   * @param data IFormCercaSoggetto che identifica i dati del creditore trovato tramite ricerca.
   */
  aggiornaCreditoreByRicerca(data: IFormCercaSoggetto) {
    // Assegno localmente i dati del titolare
    const { gruppoSelezionato } = data || {};
    const { soggetto } = data.ricercaSoggetto || {};
    // Aggiorno i dati del creditore
    this.impostaDatiComponente(soggetto, gruppoSelezionato);
  }

  /**
   * Funzione che gestisce i dati nel momento in cui viene selezionato un creditore.
   * @param soggetto SoggettoVo che identifica i dati del creditore.
   * @param gruppo Gruppo che identifica il gruppo del creditore.
   */
  aggiornaCreditore(soggetto: SoggettoVo, gruppo?: Gruppo) {
    // Aggiorno i dati del creditore
    this.impostaDatiComponente(soggetto, gruppo);
  }

  /**
   * Funzione di reset manuale richiamabile dal componente padre.
   * @override
   */
  onFormReset() {
    // Resetto la form
    this.mainForm.reset();
    // Resetto il flag del form submitted
    this.mainFormSubmitted = false;
  }

  /**
   * Funzione di restore delle informazioni richiamabile dal componente padre.
   */
  onFormRestore() {
    // Resetto il flag del form submitted
    this.mainFormSubmitted = false;
    // Rilancio i setup/init del componente
    this.initForms();
  }

  /**
   * Converte l'elemento ottenuto dalla form in un SoggettoVo da passare alla tabella.
   * @returns RimborsoVo con l'oggetto convertito
   * @override
   */
  getMainFormRawValue(): RimborsoVo {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      return undefined;
    }
    // Il main form esiste, ritorno i dati del form
    const rimborsoForm: IRimborsoForm = this.mainForm.getRawValue();

    // Estraggo i dati
    const {
      soggetto,
      tipoRimborso,
      causaleRimborso,
      dataProvvedimento,
      importoEccedente,
      numeroProvvedimento,
      idGruppo,
    } = rimborsoForm;

    // Converto gli oggetti
    const tipo_rimborso = new TipoRimborsoVo(tipoRimborso);
    const soggetto_rimborso = soggetto as SoggettoVo;
    const data_determina =
      this._riscaUtilities.convertNgbDateStructToServerDate(dataProvvedimento);

    // Creo l'elemento
    const rimborso = new RimborsoVo({
      id_rimborso: this.rimborso?.id_rimborso,
      tipo_rimborso,
      soggetto_rimborso,
      imp_rimborso: importoEccedente,
      causale: causaleRimborso,
      num_determina: numeroProvvedimento,
      data_determina,
      id_stato_debitorio: this.statoDebitorio?.id_stato_debitorio,
      id_gruppo_soggetto: idGruppo,
    });
    // ritorno l'elemento convertito
    return rimborso;
  }

  /**
   * Funzione che permette di disabilitare per intero la form del rimborso.
   */
  disableFormRimborso() {
    // Disattivo le funzionalità della form
    this.mainForm.get(this.RMB_C.TIPO_RIMBORSO)?.disable();
    this.mainForm.get(this.RMB_C.NUMERO_PROVVEDIMENTO)?.disable();
    this.mainForm.get(this.RMB_C.IMPORTO_ECCEDENTE)?.disable();
    this.mainForm.get(this.RMB_C.CAUSALE_RIMBORSO)?.disable();
    this.mainForm.get(this.RMB_C.DATA_PROVVEDIMENTO)?.disable();
    // Aggiorno la disabilitazione del pulsante cerca creditore
    this.disableCercaCreditore = false;
  }

  /**
   * Funzione che permette di abilitare per intero la form del rimborso.
   */
  enableFormRimborso() {
    // Attivo le funzionalità della form
    this.mainForm.get(this.RMB_C.TIPO_RIMBORSO)?.enable();
    this.mainForm.get(this.RMB_C.NUMERO_PROVVEDIMENTO)?.enable();
    this.mainForm.get(this.RMB_C.IMPORTO_ECCEDENTE)?.enable();
    this.mainForm.get(this.RMB_C.CAUSALE_RIMBORSO)?.enable();
    this.mainForm.get(this.RMB_C.DATA_PROVVEDIMENTO)?.enable();
    // Aggiorno la disabilitazione del pulsante cerca creditore
    this.disableCercaCreditore = false;
  }

  /**
   * ##################################################################################
   * FUNZIONI DI SET DELLE INFORMAZIONI LEGATE AI DATI ANAGRAFICI DEL SOGGETTO E GRUPPO
   * ##################################################################################
   */

  /**
   * Funzione di comodo che permette di gestione i dati riguardanti soggetto e gruppo.
   * La funzione ha una logica specifica: essendo che il set dei dati del gruppo, influenzano il set dei dati del soggetto, il set dei dati del gruppo devono avvenire PRIMA di quelli del soggetto.
   * Principalmente questo avviene perché il set dei dati del soggetto, prevede la gestione dei recapiti alternativi, che però sono affetti dalla possibilità di presenza di un gruppo.
   * @param soggetto SoggettoVo contenente i dati del soggetto trovato/configurato.
   * @param gruppo Gruppo per il caricamento dei componenti del gruppo.
   */
  private impostaDatiComponente(soggetto: SoggettoVo, gruppo?: Gruppo) {
    // Lancio il set del dato riguardante l'id del gruppo
    this.impostaDAGruppo(gruppo);
    // Poi lancio il set dei dati del soggetto
    this.impostaDAComponente(soggetto);
  }

  /**
   * Funzione di setup per le informazioni del gruppo nel componente.
   * @param gruppo Gruppo per il caricamento dei componenti del gruppo.
   */
  private impostaDAGruppo(gruppo: Gruppo) {
    // Salvo l'id del gruppo all'interno del form
    this.idGruppo = gruppo?.id_gruppo_soggetto;
  }

  /**
   * Funzione di setup per le informazioni recuperate dalla ricerca del titolare.
   * @param soggetto SoggettoVo contenente i dati del soggetto trovato/configurato.
   */
  private impostaDAComponente(soggetto: SoggettoVo) {
    // Verifico l'input
    if (!soggetto) {
      // Blocco il flusso
      return;
    }
    // Aggiorno il soggetto
    this.soggetto = soggetto;
    // Prendo i dati dal soggetto
    const { cf_soggetto, den_soggetto } = soggetto;
    // Aggiorno i valori
    this.codiceFiscale = cf_soggetto;
    this.ragioneSociale = den_soggetto;
  }

  /**
   * Funzione di comodo per il recupero dell'informazione dello stato debitorio dal servizio condiviso.
   */
  private setupStatoDebitorio() {
    // Recupero dal servizio condiviso le informazioni per lo stato debitorio
    this.statoDebitorio = this._datiContabiliUtility.statoDebitorio;
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Setter di comodo per impostare il valore dentro al form per: soggetto.
   * @param value SoggettoVo con il valore da impostare nella form.
   */
  set soggetto(value: SoggettoVo) {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.RMB_C.SOGGETTO;
    // Imposto il valore
    this._riscaUtilities.setFormValue(f, k, value);
  }

  /**
   * Setter di comodo per impostare il valore dentro al form per: codice fiscale.
   * @param value string con il valore da impostare nella form.
   */
  set codiceFiscale(value: string) {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.RMB_C.CODICE_FISCALE;
    // Imposto il valore
    this._riscaUtilities.setFormValue(f, k, value);
  }

  /**
   * Setter di comodo per impostare il valore dentro al form per: ragione sociale.
   * @param value string con il valore da impostare nella form.
   */
  set ragioneSociale(value: string) {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.RMB_C.RAGIONE_SOCIALE;
    // Imposto il valore
    this._riscaUtilities.setFormValue(f, k, value);
  }

  /**
   * Setter di comodo per impostare il valore dentro al form per: id gruppo.
   * @param value string con il valore da impostare nella form.
   */
  set idGruppo(value: number) {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.RMB_C.ID_GRUPPO;
    // Imposto il valore
    this._riscaUtilities.setFormValue(f, k, value);
  }
}
