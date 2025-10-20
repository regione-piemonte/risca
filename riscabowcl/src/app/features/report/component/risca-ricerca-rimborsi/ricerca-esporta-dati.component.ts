import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { cloneDeep } from 'lodash';
import * as moment from 'moment';
import { TipoElaborazioneVo } from '../../../../core/commons/vo/tipo-elaborazione-vo';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../core/services/user.service';
import { RiscaFormChildComponent } from '../../../../shared/components/risca/risca-form-child/risca-form-child.component';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  RiscaServerError,
  ServerNumberAsBoolean,
} from '../../../../shared/utilities';
import { RiscaErrorsMap } from '../../../../shared/utilities/classes/errors-maps';
import { ReportService } from '../../service/report/report.service';
import { RicercaEsportaDatiConsts } from './utilities/ricerca-esporta-dati.consts';
import { RicercaEsportaDatiFieldsConfig } from './utilities/ricerca-esporta-dati.fields-configs';
import { RicercaEsportaDatiFormConfigs } from './utilities/ricerca-esporta-dati.form-configs';
import { IFiltriRicercaEsportaDatiFE } from './utilities/ricerca-esporta-dati.interfaces';
import { AmbitoService } from '../../../ambito/services';

@Component({
  selector: 'ricerca-esporta-dati',
  templateUrl: './ricerca-esporta-dati.component.html',
  styleUrls: ['./ricerca-esporta-dati.component.scss'],
})
export class RicercaEsportaDatiComponent
  extends RiscaFormChildComponent<IFiltriRicercaEsportaDatiFE>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** RicercaEsportaDatiConsts come classe che definisce le costanti del componente. */
  RED_C = new RicercaEsportaDatiConsts();
  /** RiscaErrorsMap come classe che definisce la mappatura degli errori per il form di ricerca. */
  EM = new RiscaErrorsMap();
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** IFiltriRicercaEsportaDati che definisce i filtri per la pre-popolazione dei campi del form. */
  @Input('filtriIniziali') filtriInit: IFiltriRicercaEsportaDatiFE;

  /** RicercaEsportaDatiFieldsConfig come classe che definisce la struttura dei campi di ricerca del form. */
  formInputs: RicercaEsportaDatiFieldsConfig;
  /** RicercaEsportaDatiFormConfigs come classe che definisce la struttura di configurazione del form. */
  formConfigs: RicercaEsportaDatiFormConfigs;

  /** TipoElaborazione[] con la lista dei possibili valori per la ricerca. */
  listaTEReport: TipoElaborazioneVo[] = [];

  /** Boolean che definisce se la form deve risultare bloccata per la configurazione di accesso all'app. */
  AEA_EDDisabled: boolean = false;

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    private _ambito: AmbitoService,
    private _formBuilder: FormBuilder,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _report: ReportService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    submitHandlerService: RiscaFormSubmitHandlerService,
    private _user: UserService,
    utilityService: RiscaUtilitiesService
  ) {
    // Lancio il super del componente
    super(logger, navigationHelper, submitHandlerService, utilityService);
    // Lancio il setup del componente
    this.setupComponente();
  }

  /**
   * ngOnInit.
   */
  ngOnInit() {
    // Lancio l'init del componente
    this.initComponente();
  }

  /**
   * ngOnDestroy.
   */
  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * ################################
   * FUNZIONI DI SETUP DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione di setup per i dati del componente.
   */
  private setupComponente() {
    // Lancio la funzione di set per la chiave di abilitazioni elementi app
    this.setupAEAConfigs();
    // Lancio la funzione di gestione per gli errori del form
    this.setupMainFormErrors();
    // Lancio la funzione di setup delle input del form
    this.setupFormInputs();
    // Lancio la funzione di setup per la struttura del form
    this.setupFormConfigs();
  }

  /**
   * Funzione che recupera la configurazione per l'abilitazione della pagina.
   */
  private setupAEAConfigs() {
    // Recupero la chiave per la configurazione della form
    const edKey = this.AEAK_C.ESPORTA_DATI;
    // Recupero la configurazione della form dal servizio
    this.AEA_EDDisabled = this._accessoElementiApp.isAEADisabled(edKey);
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  private setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [
      ...this.EM.MAP_FORM_GROUP_REQUIRED,
      ...this.EM.MAP_DATA_INIZIO_DATA_FINE_INVALID,
    ];
  }

  /**
   * Funzione di setup per la configurazione dei dati per la costruzione delle input del form.
   */
  private setupFormInputs() {
    // Recupero il servizio di configurazione
    const s = this._riscaFormBuilder;
    // Classe di supporto che definisce la configurazioni per le input
    this.formInputs = new RicercaEsportaDatiFieldsConfig(s);
  }

  /**
   * Funzione di setup per la configurazione strutturale del form group.
   */
  private setupFormConfigs() {
    // Recupero il servizio di configurazione
    const s = this._formBuilder;
    // Istanzio l'oggetto di configurazione
    this.formConfigs = new RicercaEsportaDatiFormConfigs(s);
    // Tramite la classe di configurazione, definisco la struttura per il main form
    this.mainForm = this.formConfigs.instantiateForm();

    // Verifico la configurazione di accesso
    if (this.AEA_EDDisabled || !this.enableReportTrasversali) {
      // La configurazione richiede il blocco
      this.mainForm.disable();
    }
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Funzione di init principale per il componente.
   */
  private initComponente() {
    // Init della struttura della form ricerca
    this.initFormConfigs();
    // Init dei dati del form, se passata una configurazione
    this.initFormFields(this.filtriInit);
    // Init delle liste e dei dati relativi per il form
    this.initLists();
  }

  /**
   * Funzione di init per la configurazione strutturale del form group.
   * La funzione avrà a disposizione le informazioni passati tramite @Input().
   */
  private initFormConfigs() {
    // Gestione logiche
  }

  /**
   * Funzione di comodo che permette di valorizzare i campi della form se viene definito un oggetto di configurazione.
   * L'oggetto in input deve avere la stessa struttura del form. Le proprietà devono conincidere.
   * @param filtriInit IFiltriRicercaEsportaDati con i dati da caricare nel form.
   */
  private initFormFields(filtriInit: IFiltriRicercaEsportaDatiFE) {
    // Verifico l'input
    if (!filtriInit) {
      // Definisco un oggetto di default
      filtriInit = {
        dataDa: null,
        dataA: null,
        tipoElaboraReport: null,
      };
    }

    // Verifico se sono definite le date di inizio/fine ricerca
    if (!filtriInit.dataDa) {
      // Per default metto il primo giorno dell'anno
      const primo = moment().startOf('year');
      // Converto il primo giorno dell'anno e lo metto nella configurazione
      filtriInit.dataDa =
        this._riscaUtilities.convertMomentToNgbDateStruct(primo);
    }
    if (!filtriInit.dataA) {
      // Per default metto il primo giorno dell'anno
      const ultimo = moment().endOf('year');
      // Converto il primo giorno dell'anno e lo metto nella configurazione
      filtriInit.dataA =
        this._riscaUtilities.convertMomentToNgbDateStruct(ultimo);
    }

    // Lancio la funzione di utility
    this._riscaUtilities.initFormFields(this.mainForm, filtriInit);
  }

  /**
   * Funzione di init per lo scarico dei dati delle liste del componente.
   */
  private initLists() {
    // Lancio il set delle lista con funzioni asincrone
    this.initListeAsincrone();
  }

  /**
   * Funzione di setup che racchiude le chiamate asincrone per lo scarico deti per le liste del componente.
   */
  private initListeAsincrone() {
    // Recupero le informazioni per la chiamata
    const idA = this.idAmbito;
    const flgVis = ServerNumberAsBoolean.true;

    // Richiamo lo scarico dei tipi ricerca rimborsi
    this._report.getTipiElaboraReport(idA, flgVis).subscribe({
      next: (trReport: TipoElaborazioneVo[]) => {
        // Lancio la funzione di gestione dati
        this.onGetTipiElaboraReport(trReport);
        // #
      },
      error: (e: RiscaServerError) => {
        // Lancio l'errore
        this.onServiziError(e);
      },
    });
  }

  /**
   * #######################################
   * FUNZIONI DI COLLEGATE ALLO SCARICO DATI
   * #######################################
   */

  /**
   * Funzione invocata nel momento in cui la lista dei tipi elabora rimborsi per il popolamento della select risulta pronto.
   * @param trReport TipoElaborazione[] con la lista stati debitori scaricati.
   */
  private onGetTipiElaboraReport(trReport: TipoElaborazioneVo[]) {
    // Setto localmente i dati
    this.listaTEReport = trReport;

    // Verifico se esiste un ogetto d'inizializzazione
    if (this.filtriInit?.tipoElaboraReport) {
      // Richiamo il set dati passando il tipo elabora report
      this.setTipoElaboraReport(this.filtriInit.tipoElaboraReport);
    }
  }

  /**
   * Funzione di set dati per la select dei tipi elaborazione report.
   * @param elabora TipoElaborazione con l'oggetto sulla quale fare il match dati rispetto alla lista scaricata.
   */
  private setTipoElaboraReport(elabora: TipoElaborazioneVo) {
    // Verifico l'input
    if (!elabora) {
      // Niente configurazione
      return;
    }

    // Definisco le informazioni per il set dati
    const f = this.mainForm;
    const field = this.RED_C.TIPO_ELABORA_REPORT;
    const list = this.listaTEReport;
    const data = elabora;
    // Funzione di find
    const find = (a: TipoElaborazioneVo, b: TipoElaborazioneVo) => {
      // Se uno dei due è undefined, non li confronto nemmeno
      if (a == undefined || b == undefined) {
        // Sono certamente diversi
        return false;
      }
      // Altrimenti controllo se sono uguali
      return a.id_tipo_elabora == b.id_tipo_elabora;
    };

    // Seleziono il valore
    this._riscaUtilities.setFormValueAndSelect(f, field, list, data, find);
  }

  /**
   * ################################
   * FUNZIONI DI GESTIONE SUBMIT FORM
   * ################################
   */

  /**
   * Funzione che permette il recupero del raw value del main form del child.
   * @param c any che definisce una configurazione generica che permette di gestire logiche nell'override della funzione.
   * @returns IFiltriRicercaEsportaDati contente le informazioni del form in modalità raw value.
   */
  getMainFormRawValue(c?: any): IFiltriRicercaEsportaDatiFE {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      return undefined;
    }
    // Il main form esiste, ritorno i dati del form
    return this.mainForm.getRawValue();
  }

  /**
   * Funzione di reset del form e del componente.
   * @override
   */
  onFormReset() {
    // Resetto il flag di submit
    this.mainFormSubmitted = false;
    // Submit manuale della form
    this.mainForm.reset();

    // Recupero l'oggetto di configurazione iniziale e rilancio l'init delle informazioni
    this.initFormFields(this.filtriInit);
    // Riassegno le liste delle select per riattivare il flusso di selezione automatica
    this.listaTEReport = cloneDeep(this.listaTEReport);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo per idAmbito dell'utente.
   * @returns number con l'id ambito collegato all'utente.
   */
  get idAmbito(): number {
    // Richiamo e ritorno il valore dal servizio
    return this._user.idAmbito;
  }

  /**
   * Getter di comodo che verifica l'abilitazione applicativa per i report trasversali.
   * @returns boolean con il valore dell'abilitazione.
   */
  get enableReportTrasversali(): boolean {
    // Recupero dalla configurazione degli ambiti l'accesso ai report
    let allowReport: boolean;
    allowReport = this._ambito.abilitazioniApp?.reportTrasversali;

    // Ritorno il risultato del flag
    return allowReport;
  }
}
