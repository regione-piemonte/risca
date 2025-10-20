import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { reverse } from 'lodash';
import * as moment from 'moment';
import { AbilitazioniApp } from '../../../../core/classes/abilitazioni-ambito/abilitazioni-app.class';
import { TipoRicercaMorositaVo } from '../../../../core/commons/vo/tipo-ricerca-morosita-vo';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { AmbitoService } from '../../../../features/ambito/services';
import { MorositaService } from '../../../../features/pratiche/service/dati-contabili/morosita.service';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import {
  FormUpdatePropagation,
  IRiscaAnnoSelect,
  IRiscaRadioData,
  RiscaComponentConfig,
  RiscaFormInputRadio,
  RiscaServerError,
  IRiscaCheckboxData,
} from '../../../utilities';
import { RiscaErrorsMap } from '../../../utilities/classes/errors-maps';
import { RiscaFormChildComponent } from '../risca-form-child/risca-form-child.component';
import { RiscaRicercaMorositaConsts } from './utilities/risca-ricerca-morosita.consts';
import { RiscaRicercaMorositaFieldsConfig } from './utilities/risca-ricerca-morosita.fields-configs';
import { RiscaRicercaMorositaFormConfigs } from './utilities/risca-ricerca-morosita.form-configs';
import { IFiltriRicercaMorosita } from './utilities/risca-ricerca-morosita.interfaces';
import { RiscaTipoRicercaMorositaEnums } from './utilities/risca-ricerca-morosita.enums';

@Component({
  selector: 'risca-ricerca-morosita',
  templateUrl: './risca-ricerca-morosita.component.html',
  styleUrls: ['./risca-ricerca-morosita.component.scss'],
})
export class RiscaRicercaMorositaComponent
  extends RiscaFormChildComponent<IFiltriRicercaMorosita>
  implements OnInit, OnDestroy {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** RiscaRicercaMorositaConsts come classe che definisce le costanti del componente. */
  RRM_C = new RiscaRicercaMorositaConsts();
  /** RiscaErrorsMap come classe che definisce la mappatura degli errori per il form di ricerca. */
  EM = new RiscaErrorsMap();

  /** IFiltriRicercaMorosita che definisce i filtri per la pre-popolazione dei campi del form. */
  @Input('filtriIniziali') filtriInit: IFiltriRicercaMorosita;

  /** RiscaRicercaMorositaFieldsConfig come classe che definisce la struttura dei campi di ricerca del form. */
  formInputs: RiscaRicercaMorositaFieldsConfig;
  /** RiscaRicercaMorositaFormConfigs come classe che definisce la struttura di configurazione del form. */
  formConfigs: RiscaRicercaMorositaFormConfigs;

  /** TipoRicercaMorositaVo[] con la lista dei possibili valori per la ricerca. */
  listaTRMorosita: TipoRicercaMorositaVo[] = [];
  /** IRiscaAnnoSelect[] contenente la lista degli anni selezionabili per il campo: anno. */
  listaAnni: IRiscaAnnoSelect[] = [];

  /**
   * Costruttore.
   */
  constructor(
    private _ambito: AmbitoService,
    private _formBuilder: FormBuilder,
    logger: LoggerService,
    private _morosita: MorositaService,
    navigationHelper: NavigationHelperService,
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
    // Controllo se il tipo ricerca morosità è tra quelli che permettono di abilitare o meno le checkbox
    this.mainForm.get(this.RRM_C.TIPO_RICERCA_MOROSITA).valueChanges.subscribe(value => {
      const selectedValue: TipoRicercaMorositaVo = value;
      this.onTipoRicercaMorositaChange(selectedValue);
    });
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
    this.formInputs = new RiscaRicercaMorositaFieldsConfig(s);
  }

  /**
   * Funzione di setup per la configurazione strutturale del form group.
   */
  private setupFormConfigs() {
    // Recupero il servizio di configurazione
    const s = this._formBuilder;
    // Istanzio l'oggetto di configurazione
    this.formConfigs = new RiscaRicercaMorositaFormConfigs(s);
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

    // Disabilita la checkbox restituito al mittente
    this.mainForm.get(this.RRM_C.RESTITUITO_MITTENTE).disable();
    // Disabilita la checkbox annullato
    this.mainForm.get(this.RRM_C.ANNULLATO).disable();
  }

  /**
   * Funzione di comodo che permette di valorizzare i campi della form se viene definito un oggetto di configurazione.
   * L'oggetto in input deve avere la stessa struttura del form. Le proprietà devono conincidere.
   * @param filtriInit IFiltriRicercaMorosita con i dati da caricare nel form.
   */
  private initFormFields(filtriInit: IFiltriRicercaMorosita) {
    // Verifico l'input
    if (!filtriInit) {
      // Niente da configurare
      return;
    }

    // Recupero dall'oggetto in input le informazioni
    const flgAnnullato = filtriInit.annullato?.check ?? false;
    const flgRestituitoAM = filtriInit.restituitoAlMittente?.check ?? false;
    // Imposto i dati per i flag e per il form
    this.flagRestituitoAlMittente = flgAnnullato;
    this.flagAnnullato = flgRestituitoAM;
  }

  /**
   * Funzione di init per lo scarico dei dati delle liste del componente.
   */
  private initLists() {
    // Lancio il set della lista per gli anni
    this.initListaAnni();
    // Lancio il set della lista per i limiti accertamento
    this.initLimitiAccertamento();

    // Lancio il set delle lista con funzioni asincrone
    this.initListeAsincrone();
  }

  /**
   * Funzione di setup di comodo per la lista degli anni disponibili.
   */
  private initListaAnni() {
    // Definisco l'anno minimo
    const min = this.RRM_C.MIN_ANNO;
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
      const fcn = this.RRM_C.ANNO;
      const list = this.listaAnni;
      const y = this.filtriInit?.anno?.anno;
      const fp: FormUpdatePropagation = { emitEvent: false };
      // Lancio l'aggiornamento form e select per l'anno
      this._riscaUtilities.setFormAnnoAndSelect(f, fcn, list, y, fp);
    }
  }

  /**
   * Funzione che effettua il setup dei dati per quanto riguarda i limiti per invio accertamento.
   * La funzione gestisce logicamente la struttura dei radio button con le informazioni.
   */
  private initLimitiAccertamento() {
    // Recupero dalle configurazioni per ambito il valore per limite accertamento
    const abilitazioniApp: AbilitazioniApp = this._ambito.abilitazioniApp;
    const invioAccertamento: number = abilitazioniApp.limiteAccertamento;

    // Recupero le informazioni dei radio button
    let limiteIAC: RiscaComponentConfig<RiscaFormInputRadio>;
    limiteIAC = this.formInputs.limiteInvioAccertamentoConfig;

    // Estraggo la lista degli elementi del radio button
    const source: IRiscaRadioData[] = limiteIAC.source;
    // La struttura è composta da sempre SOLO 2 oggetti, il minimo uguale ed il maggiore
    const minEq: IRiscaRadioData = source[0];
    const mag: IRiscaRadioData = source[1];
    // Aggiorno gli oggetti
    minEq.value = invioAccertamento;
    minEq.label = `${minEq.label} ${invioAccertamento}`;
    mag.value = invioAccertamento;
    mag.label = `${mag.label} ${invioAccertamento}`;

    // Verifico se esiste un ogetto d'inizializzazione
    if (this.filtriInit?.limiteInvioAccertamento) {
      // Esiste, recupero il dato
      const limit: IRiscaRadioData = this.filtriInit.limiteInvioAccertamento;
      // Verifico quale oggetto è stato selezionato
      const isMinEq = limit.id === minEq.id;
      const isMag = limit.id === mag.id;

      // Definisco un contenitore per l'oggetto da impostare come selezionato
      let value: IRiscaRadioData;
      // Verifico e assegno a value a seconda della condizione
      if (isMinEq) {
        // Valore minore selezionato
        value = source[0];
        // #
      } else if (isMag) {
        // Valore maggiore selezionato
        value = source[1];
      }
      // Verifico se effettivamente è stato selezionato un valore
      if (value) {
        // Recupero le informazioni per l'assegnazione
        const f = this.mainForm;
        const fcn = this.RRM_C.LIMITE_INVIO_ACCERTAMENTO;
        // Aggiorno il valore per il form
        this._riscaUtilities.setFormValue(f, fcn, value);
      }
    }
  }

  /**
   * Funzione di setup che racchiude le chiamate asincrone per lo scarico deti per le liste del componente.
   */
  private initListeAsincrone() {
    // Richiamo lo scarico dei tipi ricerca morosita
    this._morosita.getTipiRicercaMorosita().subscribe({
      next: (trMorosita: TipoRicercaMorositaVo[]) => {
        // Lancio la funzione di gestione dati
        this.onGetTipiRicercaMorosita(trMorosita);
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
   * Funzione invocata nel momento in cui la lista dei tipi ricerca morosita per il popolamento della select risulta pronto.
   * @param trMorosita TipoRicercaMorositaVo[] con la lista stati debitori scaricati.
   */
  private onGetTipiRicercaMorosita(trMorosita: TipoRicercaMorositaVo[]) {
    // Setto localmente i dati
    this.listaTRMorosita = trMorosita;

    // Verifico se esiste un ogetto d'inizializzazione
    if (this.filtriInit?.tipoRicercaMorosita) {
      // Definisco le informazioni per il set dati
      const f = this.mainForm;
      const field = this.RRM_C.TIPO_RICERCA_MOROSITA;
      const list = this.listaTRMorosita;
      const data = this.filtriInit.tipoRicercaMorosita;
      // Funzione di find
      const find = (a: TipoRicercaMorositaVo, b: TipoRicercaMorositaVo) => {
        // Se uno dei due è undefined, non li confronto nemmeno
        if (a == undefined || b == undefined) {
          // Sono certamente diversi
          return false;
        }
        // Altrimenti controllo se sono uguali
        return a.id_tipo_ricerca_morosita == b.id_tipo_ricerca_morosita;
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
   * @returns IFiltriRicercaMorosita contente le informazioni del form in modalità raw value.
   */
  getMainFormRawValue(c?: any): IFiltriRicercaMorosita {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      return undefined;
    }
    // Il main form esiste, ritorno i dati del form
    return this.mainForm.getRawValue();
  }

  /**
   * ###############################
   * FUNZIONI DI GESTIONE RESET FORM
   * ###############################
   */

  /**
   * Funzione di reset del form e del componente.
   * @override
   */
  onFormReset() {
    // Forzo manualmente i valori "false" per le checkbox
    this.flagRestituitoAlMittente = false;
    this.flagAnnullato = false;

    // Resetto il flag di submit
    this.mainFormSubmitted = false;
    // Submit manuale della form
    this.mainForm.reset();
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

  /**
   * Funzione di comodo per la gestione dell'evento di cambio del tipo ricerca morosità.
   * @param value TipoRicercaMorositaVo con il valore selezionato.
   */
  private onTipoRicercaMorositaChange(value: TipoRicercaMorositaVo) {
    //lista di codici che abilitano o disabilitano le checkbox
    const codiciTipoRicercaMorosita: String[] = [
      RiscaTipoRicercaMorositaEnums.RICMOR03,
      RiscaTipoRicercaMorositaEnums.RICMOR04,
      RiscaTipoRicercaMorositaEnums.RICMOR06,
      RiscaTipoRicercaMorositaEnums.RICMOR07,
      RiscaTipoRicercaMorositaEnums.RICMOR09,
      RiscaTipoRicercaMorositaEnums.RICMOR14,
    ];

    const shouldEnable = value && codiciTipoRicercaMorosita.includes(value.cod_tipo_ricerca_morosita);
    this.mainForm.get(this.RRM_C.RESTITUITO_MITTENTE)[shouldEnable ? 'enable' : 'disable']();
    this.mainForm.get(this.RRM_C.ANNULLATO)[shouldEnable ? 'enable' : 'disable']();
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Setter per il valore del form: restituito al mittente.
   * @param flagRestituitoAM boolean che definisce il valore da definire per la checkbox.
   */
  set flagRestituitoAlMittente(flagRestituitoAM: boolean) {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.RRM_C.RESTITUITO_MITTENTE;
    const fiRAM = this.formInputs.restituitoMittenteConfig;

    // Definisco l'oggetto per gestire la checkbox
    const chekboxConfig: IRiscaCheckboxData = {
      id: this.RRM_C.LABEL_RESTITUITO_MITTENTE,
      label: this.RRM_C.LABEL_RESTITUITO_MITTENTE,
      value: this.RRM_C.RESTITUITO_MITTENTE,
      check: flagRestituitoAM,
    };

    // Aggiorno il valore per il form
    this._riscaUtilities.setFormValue(f, k, chekboxConfig);
    // Aggiorno il source della configurazione dell'input della checkbox
    this._riscaUtilities.updateRFICheckboxSource(fiRAM, chekboxConfig);
  }

  /**
   * Setter per il valore del form: annullato.
   * @param flagAnnullato boolean che definisce il valore da definire per la checkbox.
   */
  set flagAnnullato(flagAnnullato: boolean) {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.RRM_C.ANNULLATO;
    const fiA = this.formInputs.annullatoConfig;

    // Definisco l'oggetto per gestire la checkbox
    const chekboxConfig: IRiscaCheckboxData = {
      id: this.RRM_C.LABEL_ANNULLATO,
      label: this.RRM_C.LABEL_ANNULLATO,
      value: this.RRM_C.ANNULLATO,
      check: flagAnnullato,
    };

    // Aggiorno il valore per il form
    this._riscaUtilities.setFormValue(f, k, chekboxConfig);
    // Aggiorno il source della configurazione dell'input della checkbox
    this._riscaUtilities.updateRFICheckboxSource(fiA, chekboxConfig);
  }
}
