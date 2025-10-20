import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { reverse } from 'lodash';
import * as moment from 'moment';
import { TipoRicercaRimborsoVo } from '../../../../core/commons/vo/tipo-ricerca-rimborso-vo';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RimborsiService } from '../../../../features/pratiche/service/rimborsi/rimborsi.service';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import {
  FormUpdatePropagation,
  IRiscaAnnoSelect,
  RiscaServerError,
} from '../../../utilities';
import { RiscaErrorsMap } from '../../../utilities/classes/errors-maps';
import { RiscaFormChildComponent } from '../risca-form-child/risca-form-child.component';
import { RiscaRicercaRimborsiConsts } from './utilities/risca-ricerca-rimborsi.consts';
import { RiscaRicercaRimborsiFieldsConfig } from './utilities/risca-ricerca-rimborsi.fields-configs';
import { RiscaRicercaRimborsiFormConfigs } from './utilities/risca-ricerca-rimborsi.form-configs';
import { IFiltriRicercaRimborsi } from './utilities/risca-ricerca-rimborsi.interfaces';

@Component({
  selector: 'risca-ricerca-rimborsi',
  templateUrl: './risca-ricerca-rimborsi.component.html',
  styleUrls: ['./risca-ricerca-rimborsi.component.scss'],
})
export class RiscaRicercaRimborsiComponent
  extends RiscaFormChildComponent<IFiltriRicercaRimborsi>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** RiscaRicercaRimborsiConsts come classe che definisce le costanti del componente. */
  RRR_C = new RiscaRicercaRimborsiConsts();
  /** RiscaErrorsMap come classe che definisce la mappatura degli errori per il form di ricerca. */
  EM = new RiscaErrorsMap();

  /** IFiltriRicercaRimborsi che definisce i filtri per la pre-popolazione dei campi del form. */
  @Input('filtriIniziali') filtriInit: IFiltriRicercaRimborsi;

  /** RiscaRicercaRimborsiFieldsConfig come classe che definisce la struttura dei campi di ricerca del form. */
  formInputs: RiscaRicercaRimborsiFieldsConfig;
  /** RiscaRicercaRimborsiFormConfigs come classe che definisce la struttura di configurazione del form. */
  formConfigs: RiscaRicercaRimborsiFormConfigs;

  /** TipoRicercaRimborsoVo[] con la lista dei possibili valori per la ricerca. */
  listaTRRimborsi: TipoRicercaRimborsoVo[] = [];
  /** IRiscaAnnoSelect[] contenente la lista degli anni selezionabili per il campo: anno. */
  listaAnni: IRiscaAnnoSelect[] = [];

  /**
   * Costruttore.
   */
  constructor(
    private _formBuilder: FormBuilder,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _rimborsi: RimborsiService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    submitHandlerService: RiscaFormSubmitHandlerService,
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
    // Lancio la funzione di gestione per gli errori del form
    this.setupMainFormErrors();
    // Lancio la funzione di setup delle input del form
    this.setupFormInputs();
    // Lancio la funzione di setup per la struttura del form
    this.setupFormConfigs();
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  private setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [...this.EM.MAP_FORM_GROUP_REQUIRED];
  }

  /**
   * Funzione di setup per la configurazione dei dati per la costruzione delle input del form.
   */
  private setupFormInputs() {
    // Recupero il servizio di configurazione
    const s = this._riscaFormBuilder;
    // Classe di supporto che definisce la configurazioni per le input
    this.formInputs = new RiscaRicercaRimborsiFieldsConfig(s);
  }

  /**
   * Funzione di setup per la configurazione strutturale del form group.
   */
  private setupFormConfigs() {
    // Recupero il servizio di configurazione
    const s = this._formBuilder;
    // Istanzio l'oggetto di configurazione
    this.formConfigs = new RiscaRicercaRimborsiFormConfigs(s);
    // Tramite la classe di configurazione, definisco la struttura per il main form
    this.mainForm = this.formConfigs.instantiateForm();
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
	  // Gestione delle logiche
	}

  /**
   * Funzione di comodo che permette di valorizzare i campi della form se viene definito un oggetto di configurazione.
   * L'oggetto in input deve avere la stessa struttura del form. Le proprietà devono conincidere.
   * @param filtriInit IFiltriRicercaRimborsi con i dati da caricare nel form.
   */
  private initFormFields(filtriInit: IFiltriRicercaRimborsi) {
    // Verifico l'input
    if (!filtriInit) {
      // Niente da configurare
      return;
    }
  }

  /**
   * Funzione di init per lo scarico dei dati delle liste del componente.
   */
  private initLists() {
    // Lancio il set della lista per gli anni
    this.initListaAnni();
    // Lancio il set delle lista con funzioni asincrone
    this.initListeAsincrone();
  }

  /**
   * Funzione di setup di comodo per la lista degli anni disponibili.
   */
  private initListaAnni() {
    // Definisco l'anno minimo
    const min = this.RRR_C.MIN_ANNO;
    // Definisco l'anno di partenza, come anno corrente
    const start = parseInt(moment().format('YYYY'));
    // Definisco il contatore degli anni all'indietro per il set
    const countMin = start - min;

    // Assegno la lista degli anni alla variabile locale
    const yearList: IRiscaAnnoSelect[] = this.generateAnni(start, countMin, 0);
    // Inverto l'ordine dell'elenco degli anni
    reverse(yearList);

    // Assegno l'array di anni
    this.listaAnni = yearList;

    // Verifico se esiste una configurazione iniziale per la lista anni
    if (this.filtriInit?.anno) {
      // Esiste, definisco le informazioni per il set iniziale
      const f = this.mainForm;
      const fcn = this.RRR_C.ANNO;
      const list = this.listaAnni;
      const y = this.filtriInit?.anno?.anno;
      const fp: FormUpdatePropagation = { emitEvent: false };
      // Lancio l'aggiornamento form e select per l'anno
      this._riscaUtilities.setFormAnnoAndSelect(f, fcn, list, y, fp);
    }
  }

  /**
   * Funzione di setup che racchiude le chiamate asincrone per lo scarico deti per le liste del componente.
   */
  private initListeAsincrone() {
    // Richiamo lo scarico dei tipi ricerca rimborsi
    this._rimborsi.getTipiRicercaRimborsi().subscribe({
      next: (trRimborsi: TipoRicercaRimborsoVo[]) => {
        // Lancio la funzione di gestione dati
        this.onGetTipiRicercaRimborsi(trRimborsi);
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
   * Funzione invocata nel momento in cui la lista dei tipi ricerca rimborsi per il popolamento della select risulta pronto.
   * @param trRimborsi TipoRicercaRimborsoVo[] con la lista stati debitori scaricati.
   */
  private onGetTipiRicercaRimborsi(trRimborsi: TipoRicercaRimborsoVo[]) {
    // Setto localmente i dati
    this.listaTRRimborsi = trRimborsi;

    // Verifico se esiste un ogetto d'inizializzazione
    if (this.filtriInit?.tipoRicercaRimborsi) {
      // Definisco le informazioni per il set dati
      const f = this.mainForm;
      const field = this.RRR_C.TIPO_RICERCA_RIMBORSI;
      const list = this.listaTRRimborsi;
      const data = this.filtriInit.tipoRicercaRimborsi;
      // Funzione di find
      const find = (a: TipoRicercaRimborsoVo, b: TipoRicercaRimborsoVo) => {
        // Se uno dei due è undefined, non li confronto nemmeno
        if (a == undefined || b == undefined) {
          // Sono certamente diversi
          return false;
        }
        // Altrimenti controllo se sono uguali
        return a.id_tipo_ricerca_rimborso == b.id_tipo_ricerca_rimborso;
      };

      // Seleziono il valore
      this._riscaUtilities.setFormValueAndSelect(f, field, list, data, find);
    }
  }

  /**
   * ################################
   * FUNZIONI DI GESTIONE SUBMIT FORM
   * ################################
   */

  /**
   * Funzione che permette il recupero del raw value del main form del child.
   * @param c any che definisce una configurazione generica che permette di gestire logiche nell'override della funzione.
   * @returns IFiltriRicercaRimborsi contente le informazioni del form in modalità raw value.
   */
  getMainFormRawValue(c?: any): IFiltriRicercaRimborsi {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      return undefined;
    }
    // Il main form esiste, ritorno i dati del form
    return this.mainForm.getRawValue();
  }

  /**
   * ##################
   * FUNZIONI DI COMODO
   * ##################
   */

  /**
   * Genera un array di anni a partire dalla data start-fromBefore fino all'anno start+tillAfter.
   * @param start number come data di riferimento.
   * @param fromBefore number con il numero di anni prima della data di riferimento.
   * @param tillAfter number con il numero di anni dopo della data di riferimento.
   * @returns IRiscaAnnoSelect[] con la lista di anni generati.
   */
  generateAnni(
    start: number,
    fromBefore?: number,
    tillAfter?: number
  ): IRiscaAnnoSelect[] {
    // Lancio la funzione di utility
    return this._riscaUtilities.generateAnni(start, fromBefore, tillAfter);
  }
}
