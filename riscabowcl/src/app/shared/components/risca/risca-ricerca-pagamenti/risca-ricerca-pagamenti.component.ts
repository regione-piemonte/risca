import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { forkJoin, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { TipoModalitaPagamentoVo } from '../../../../core/commons/vo/tipo-modalita-pagamento-vo';
import {
  TipiRicercaPagamentoConst,
  TipoRicercaPagamentoVo,
} from '../../../../core/commons/vo/tipo-ricerca-pagamento-vo';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { PagamentiService } from '../../../../features/pagamenti/service/pagamenti/pagamenti.service';
import { RiscaFormBuilderService } from '../../../services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import { RiscaServerError } from '../../../utilities';
import { RiscaErrorsMap } from '../../../utilities/classes/errors-maps';
import { RiscaFormChildComponent } from '../risca-form-child/risca-form-child.component';
import { RiscaRicercaPagamentiConsts } from './utilities/risca-ricerca-pagamenti.consts';
import { RiscaRicercaPagamentiFieldsConfig } from './utilities/risca-ricerca-pagamenti.fields-configs';
import { RiscaRicercaPagamentiFormConfigs } from './utilities/risca-ricerca-pagamenti.form-configs';
import { IFiltriRicercaPagamenti } from './utilities/risca-ricerca-pagamenti.interfaces';

/**
 * Interfaccia locale per la gestione delle richieste di scarico dati delle liste del componente.
 */
interface IRRPListReq {
  tipiRicercaPagamenti: Observable<TipoRicercaPagamentoVo[]>;
  modalitaPagamenti: Observable<TipoModalitaPagamentoVo[]>;
}

/**
 * Interfaccia locale con il risultato delle richieste di scarico dati delle liste del componente.
 */
interface IRRPListRes {
  tipiRicercaPagamenti: TipoRicercaPagamentoVo[];
  modalitaPagamenti: TipoModalitaPagamentoVo[];
}

@Component({
  selector: 'risca-ricerca-pagamenti',
  templateUrl: './risca-ricerca-pagamenti.component.html',
  styleUrls: ['./risca-ricerca-pagamenti.component.scss'],
})
export class RiscaRicercaPagamentiComponent
  extends RiscaFormChildComponent<IFiltriRicercaPagamenti>
  implements OnInit, OnDestroy
{
  /** RiscaRicercaPagamentiConsts come classe che definisce le costanti del componente. */
  RRP_C = new RiscaRicercaPagamentiConsts();
  /** RiscaErrorsMap come classe che definisce la mappatura degli errori per il form di ricerca. */
  EM = new RiscaErrorsMap();

  /** IFiltriRicercaPagamenti che definisce i filtri per la pre-popolazione dei campi del form. */
  @Input('filtriIniziali') filtriInit: IFiltriRicercaPagamenti;

  /** RiscaRicercaPagamentiFieldsConfig come classe che definisce la struttura dei campi di ricerca del form. */
  formInputs: RiscaRicercaPagamentiFieldsConfig;
  /** RiscaRicercaPagamentiFormConfigs come classe che definisce la struttura di configurazione del form. */
  formConfigs: RiscaRicercaPagamentiFormConfigs;

  /** TipoModalitaPagamentoVo[] con la lista dei possibili valori per la ricerca. */
  listaModalitaPagamenti: TipoModalitaPagamentoVo[] = [];
  /** TipoRicercaPagamentoVo[] con la lista dei possibili valori per la ricerca. */
  listaTRPagamenti: TipoRicercaPagamentoVo[] = [];

  /**
   * Costruttore.
   */
  constructor(
    logger: LoggerService,
    private _formBuilder: FormBuilder,
    navigationHelper: NavigationHelperService,
    private _pagamenti: PagamentiService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    utilityService: RiscaUtilitiesService,
    submitHandlerService: RiscaFormSubmitHandlerService
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
    // Init delle logiche del componente
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
    // Lancio il setup per gli errori del form
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
    this.formErrors = [
      ...this.EM.MAP_MIN_MAX_CHECK,
      ...this.EM.MAP_DATA_INIZIO_DATA_FINE_INVALID,
      ...this.EM.MAP_RICERCA_PAGAMENTI_FILTRI_VALORIZZATI,
    ];
  }

  /**
   * Funzione di setup per la configurazione dei dati per la costruzione delle input del form.
   */
  private setupFormInputs() {
    // Recupero il servizio di configurazione
    const s = this._riscaFormBuilder;
    // Classe di supporto che definisce la configurazioni per le input
    this.formInputs = new RiscaRicercaPagamentiFieldsConfig(s);
  }

  /**
   * Funzione di setup per la configurazione strutturale del form group.
   */
  private setupFormConfigs() {
    // Recupero il servizio di configurazione
    const s = this._formBuilder;
    // Istanzio l'oggetto di configurazione
    this.formConfigs = new RiscaRicercaPagamentiFormConfigs(s);
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
   * @param filtriInit IFiltriRicercaPagamenti con i dati da caricare nel form.
   */
  private initFormFields(filtriInit: IFiltriRicercaPagamenti) {
    // Verifico l'input
    if (!filtriInit) {
      // Niente da configurare
      return;
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
   * Funzione che raccoglie le chiamate asincrone per il recupero delle liste dati per le select.
   */
  private initListeAsincrone() {
    // Imposto l'oggetto con le richieste
    const listReq: IRRPListReq = {
      tipiRicercaPagamenti: this.getTipiRicercaPagamenti(),
      modalitaPagamenti: this._pagamenti.getModalitaPagamento(),
    };

    // Lancio il blocco di request delle chiamate
    forkJoin(listReq).subscribe({
      next: (listRes: IRRPListRes) => {
        // Lancio la funzione di gestione delle liste
        this.onListsResponse(listRes);
        // #
      },
      error: (e: RiscaServerError) => {
        // Gestisco l'errore
        this.onServiziError(e);
      },
    });
  }

  /**
   * ########################
   * FUNZIONI DI SCARICO DATI
   * ########################
   */

  /**
   * Funzione adibita allo scarico dei tipi ricerca pagamento del componente.
   * La lista verrà manipolata per gestire delle condizioni specifiche per questo componente.
   */
  private getTipiRicercaPagamenti(): Observable<TipoRicercaPagamentoVo[]> {
    // Richiamo la get del servizio di ricerca
    return this._pagamenti.getTipiRicercaPagamento().pipe(
      map((trPagamenti: TipoRicercaPagamentoVo[]) => {
        // Per questa sezione, bisogna rimuovere l'oggetto con codice "DA_VISIONARE"
        let trpComponent: TipoRicercaPagamentoVo[];
        trpComponent = trPagamenti.filter((trp: TipoRicercaPagamentoVo) => {
          // Recupero dalla costante applicativa l'oggetto "da visionare"
          let daVisionare: TipoRicercaPagamentoVo;
          daVisionare = TipiRicercaPagamentoConst.daVisionare;
          // Recupero il codice dell'oggetto ciclato
          const codElem = trp?.cod_tipo_ricerca_pagamento;
          // Effettuo la verifica sul codice, se diverso mantengo l'oggetto
          return codElem !== daVisionare.cod_tipo_ricerca_pagamento;
        });

        // Ritorno la lista filtrata
        return trpComponent;
      })
    );
  }

  /**
   * Funzione invocata nel momento in cui le liste delle select sono state scaricate.
   * @param listRes IRRPListRes come oggetto di risposta delle liste scaricate
   */
  private onListsResponse(listRes: IRRPListRes) {
    // Verifico l'input
    if (!listRes) {
      // Non c'è l'oggetto di configurazione
      return;
    }

    // Estraggo dalla response le liste
    const { modalitaPagamenti, tipiRicercaPagamenti } = listRes;

    // Vado a settare le informazioni per le liste
    this.onGetModalitaPagamenti(modalitaPagamenti);
    this.onGetTipiRicercaPagamenti(tipiRicercaPagamenti);
  }

  /**
   * #######################################
   * FUNZIONI DI COLLEGATE ALLO SCARICO DATI
   * #######################################
   */

  /**
   * Funzione invocata nel momento in cui la lista delle modalita pagamenti per il popolamento della select risulta pronto.
   * @param modalitaPagamenti TipoModalitaPagamentoVo[] con la lista stati debitori scaricati.
   */
  private onGetModalitaPagamenti(modalitaPagamenti: TipoModalitaPagamentoVo[]) {
    // Setto localmente i dati
    this.listaModalitaPagamenti = modalitaPagamenti;

    // Verifico se esiste un ogetto d'inizializzazione
    if (this.filtriInit?.modalitaPagamento) {
      // Definisco le informazioni per il set dati
      const f = this.mainForm;
      const field = this.RRP_C.MODALITA_PAGAMENTO;
      const list = this.listaModalitaPagamenti;
      const data = this.filtriInit.modalitaPagamento;
      // Funzione di find
      const find = (a: TipoModalitaPagamentoVo, b: TipoModalitaPagamentoVo) => {
        // Se uno dei due è undefined, non li confronto nemmeno
        if (a == undefined || b == undefined) {
          // Sono certamente diversi
          return false;
        }
        // Altrimenti controllo se sono uguali
        return a.id_tipo_modalita_pag == b.id_tipo_modalita_pag;
      };

      // Seleziono il valore
      this._riscaUtilities.setFormValueAndSelect(f, field, list, data, find);
    }
  }

  /**
   * Funzione invocata nel momento in cui la lista dei tipi ricerca pagamenti per il popolamento della select risulta pronto.
   * @param trPagamenti TipoRicercaPagamentoVo[] con la lista stati debitori scaricati.
   */
  private onGetTipiRicercaPagamenti(trPagamenti: TipoRicercaPagamentoVo[]) {
    // Setto localmente i dati
    this.listaTRPagamenti = trPagamenti;

    // Verifico se esiste un ogetto d'inizializzazione
    if (this.filtriInit?.statoPagamento) {
      // Definisco le informazioni per il set dati
      const f = this.mainForm;
      const field = this.RRP_C.STATO_PAGAMENTO;
      const list = this.listaTRPagamenti;
      const data = this.filtriInit.statoPagamento;
      // Funzione di find
      const find = (a: TipoRicercaPagamentoVo, b: TipoRicercaPagamentoVo) => {
        // Se uno dei due è undefined, non li confronto nemmeno
        if (a == undefined || b == undefined) {
          // Sono certamente diversi
          return false;
        }
        // Altrimenti controllo se sono uguali
        return a.id_tipo_ricerca_pagamento == b.id_tipo_ricerca_pagamento;
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
   * Funzione collegata al pulsante CERCA della pagina.
   * La funzione prevede il submit dei filtri di ricerca.
   */
  avviaRicerca() {
    // Tramite referenza, chiedo il submit dei filtri
    this.onFormSubmit();
  }

  /**
   * Funzione di comodo che richiama l'utility di rimozione dati di FE dagli oggetti delle select.
   * @param obj any con l'oggetto da sanitizzare.
   * @returns any con l'oggetto sanitizzato.
   */
  private sanitizeFEProperties(obj: any): any {
    // Richiamo la funzione di utility e ritorno il suo valore
    return this._riscaUtilities.sanitizeFEProperties(obj);
  }

  /**
   * Funzione che permette il recupero del raw value del main form del child.
   * @param c any che definisce una configurazione generica che permette di gestire logiche nell'override della funzione.
   * @returns IFiltriRicercaPagamenti contente le informazioni del form in modalità raw value.
   */
  getMainFormRawValue(c?: any): IFiltriRicercaPagamenti {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      return undefined;
    }

    // Il main form esiste, ritorno i dati del form
    let formData: IFiltriRicercaPagamenti;
    formData = this.mainForm.getRawValue();

    // Ritorno le informazioni
    return formData;
  }
}
