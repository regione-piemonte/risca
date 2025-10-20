import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { cloneDeep } from 'lodash';
import { forkJoin, Observable } from 'rxjs';
import { comuneDataFineValiditaFG } from 'src/app/shared/miscellaneous/forms-validators/soggetto/form-validators.r';
import { ComuneVo } from '../../../../core/commons/vo/comune-vo';
import { Gruppo } from '../../../../core/commons/vo/gruppo-vo';
import { IndirizzoSpedizioneVo } from '../../../../core/commons/vo/indirizzo-spedizione-vo';
import { NazioneVo } from '../../../../core/commons/vo/nazione-vo';
import { ProvinciaVo } from '../../../../core/commons/vo/provincia-vo';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { TipoSedeVo } from '../../../../core/commons/vo/tipo-sede-vo';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { IAggiornaIndirizzoSpedizione } from '../../../../features/pratiche/interfaces/indirizzi-spedizione/indirizzi-spedizione.interfaces';
import { GestioneFormsDatiAnagraficiService } from '../../../../features/pratiche/service/dati-anagrafici/gestione-forms-dati-anagrafici.service';
import { IndirizziSpedizioneUtilityService } from '../../../../features/pratiche/service/dati-anagrafici/indirizzi-spedizione-utility/indirizzi-spedizione-utility.service';
import { SoggettoDatiAnagraficiService } from '../../../../features/pratiche/service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { RiscaRecapitoConsts } from '../../../consts/risca/risca-recapito.consts';
import { LocationService } from '../../../services/location.service';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { RiscaRecapitoService } from '../../../services/risca/risca-recapito/risca-recapito.service';
import { isSBNTrue } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import {
  FormUpdatePropagation,
  ICallbackDataModal,
  IRiscaButtonAllConfig,
  RiscaComponentConfig,
  RiscaFormInputSelect,
  RiscaFormInputText,
  RiscaFormInputTypeahead,
  RiscaRecapito,
  RiscaServerError,
} from '../../../utilities';
import { GestioneRecapitoComponent } from '../risca-dati-anagrafici/gestione-recapito.component';

/**
 * Interfaccia di comodo per lo scarico delle liste del componente. Formato: request.
 */
interface IListeRReq {
  tipiSede: Observable<TipoSedeVo[]>;
  stati: Observable<NazioneVo[]>;
  comune: Observable<ComuneVo>;
  province: Observable<ProvinciaVo[]>;
}

/**
 * Interfaccia di comodo per lo scarico delle liste del componente. Formato: response.
 */
interface IListeRRes {
  tipiSede: TipoSedeVo[];
  stati: NazioneVo[];
  comune: ComuneVo;
  province: ProvinciaVo[];
}

@Component({
  selector: 'risca-recapito',
  templateUrl: './risca-recapito.component.html',
  styleUrls: ['./risca-recapito.component.scss'],
})
export class RiscaRecapitoComponent
  extends GestioneRecapitoComponent<RiscaRecapito>
  implements OnInit, OnChanges
{
  /** Costante RiscaRecapitoConsts contenente le costanti utilizzate dal componente. */
  RR_C = RiscaRecapitoConsts;

  /** Input che specifica il gruppo assegnato alla riscossione, da utilizzare per ottenere l'indirizo del recapito */
  @Input() gruppoIS?: Gruppo;
  // Output necessaria per la componente padre per aggiornare il soggetto
  @Output() updateDA = new EventEmitter<SoggettoVo>();

  /** Oggetto di configurazione per il campo: tipoSede. */
  tipoSedeConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: presso. */
  pressoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: stato. */
  statoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: comune. */
  comuneConfig: RiscaComponentConfig<RiscaFormInputTypeahead>;
  /** Oggetto di configurazione per il campo: provincia. */
  provinciaConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: cittaEsteraRecapito. */
  cittaEsteraRecapitoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: localita. */
  localitaConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: indirizzo. */
  indirizzoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: numeroCivico. */
  numeroCivicoConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: cap. */
  capConfig: RiscaComponentConfig<RiscaFormInputText>;

  /** Lista di oggetti TipoSedeVo contenente le informazioni per i tipi sede. */
  listaTipiSede: TipoSedeVo[] = [];
  /** Lista di oggetti NazioneVo contenente le informazioni per gli stati. */
  listaStati: NazioneVo[] = [];
  /** Lista di oggetti ProvinciaVo contenente le informazioni per le province. */
  listaProvince: ProvinciaVo[] = [];

  /** Indirizzo di spedizione per questo recapito */
  indirizzoSpedizione: IndirizzoSpedizioneVo;

  /**
   * Costruttore.
   */
  constructor(
    private _formBuilder: FormBuilder,
    private _gestioneFormsDA: GestioneFormsDatiAnagraficiService,
    private _isUtility: IndirizziSpedizioneUtilityService,
    private _location: LocationService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _riscaRecapito: RiscaRecapitoService,
    riscaUtilities: RiscaUtilitiesService,
    soggettoDA: SoggettoDatiAnagraficiService
  ) {
    // Richiamo il super
    super(
      logger,
      navigationHelper,
      riscaFormSubmitHandler,
      riscaUtilities,
      soggettoDA
    );
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit(): void {
    // Lancio il setup delle form
    this.initForms();
    // Setup delle liste per le select del form
    this.initListeSelect();
  }

  ngOnChanges(changes: SimpleChanges) {
    // Verifico se la modalità è stata cambiata
    if (changes.modalita && !changes.modalita.firstChange) {
      // Richiamo i validatori
      this.initValidators();
      this.updateStatoValidator();
      this.indirizzoSpedizioneFromRecapito();
    }
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
    this.formErrors = [
      ...this.EM.MAP_FORM_GROUP_REQUIRED,
      ...this.EM.MAP_COMUNE_VALIDO_FC,
      ...this.EM.MAP_COMUNE_VALIDO_FG,
    ];
  }

  /**
   * Funzione di setup per le input del form.
   */
  private setupFormInputs() {
    // Genero il setup per i campi
    const {
      tipoSedeConfig,
      pressoConfig,
      statoConfig,
      comuneConfig,
      provinciaConfig,
      cittaEsteraRecapitoConfig,
      localitaConfig,
      indirizzoConfig,
      numeroCivicoConfig,
      capConfig,
    } = this._riscaRecapito.setupFormInputs();

    // Assegno locamente il setup
    this.tipoSedeConfig = tipoSedeConfig;
    this.pressoConfig = pressoConfig;
    this.statoConfig = statoConfig;
    this.comuneConfig = comuneConfig;
    this.provinciaConfig = provinciaConfig;
    this.cittaEsteraRecapitoConfig = cittaEsteraRecapitoConfig;
    this.localitaConfig = localitaConfig;
    this.indirizzoConfig = indirizzoConfig;
    this.numeroCivicoConfig = numeroCivicoConfig;
    this.capConfig = capConfig;
  }

  /**
   * Funzione che effettua il setup per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Setup del form
    this.initRecapitoForm();
    // Setup dei listiner dei form per la manipolazione dei dati
    this.initFormListener();
    // Setup dei campi form in base alla parametrizzazione degli input
    this.initCampiForm();
    // Lancio il setup per i validatori dei campi
    this.initValidators();
  }

  /**
   * Setup del form.
   */
  private initRecapitoForm() {
    this.mainForm = this._formBuilder.group(
      {
        tipoSede: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        presso: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        stato: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        comune: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        provincia: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        cittaEsteraRecapito: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        localita: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        indirizzo: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        numeroCivico: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        cap: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        indirizziSpedizione: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
      },
      {
        validators: [comuneDataFineValiditaFG(this.RR_C.COMUNE)],
      }
    );

    // Verifico i flag
    if (this.isISFormDisabilitato) {
      // Disabilito il form
      this.mainForm.disable();
    }
  }

  /**
   * Funzione specifica adibita al setup dei listner delle form.
   * La funzione registrerà diversi eventi sulle form, atte a gestire la logica e la manipolazione dei dati.
   */
  private initFormListener() {
    // Registro un ascoltatore per il cambio del comune
    this.mainForm.get(this.RR_C.COMUNE).valueChanges.subscribe({
      next: (comune: ComuneVo) => {
        // Aggiorno i dati relativi al comune e aggiorno il cap
        this.comuneSelected(comune, true);
        // #
      },
    });
    // Registro un ascoltatore per il cambio dello stato
    this.mainForm.get(this.RR_C.STATO).valueChanges.subscribe({
      next: (stato: NazioneVo) => {
        // Aggiorno i dati relativi allo stato
        this.statoSelected(stato);
        // #
      },
    });
  }

  /**
   * Funzione di setup dei campi del form in base alla parametrizzazione degli input.
   * @param isReset boolean che definisce che è richiesto un reset totale.
   */
  private initCampiForm(isReset?: boolean) {
    // Imposto l'indirizzo spedizione
    this.indirizzoSpedizioneFromRecapito();
    // Definisco una variabile per il recapito principale
    let recapito = this.recapito;
    // Verifico esista
    if (!recapito) {
      return;
    }

    // Verifico la configurazione per il presso
    if (recapito.presso && !isReset) {
      // Setto il valore
      this.initCampoForm(this.RR_C.PRESSO, recapito.presso);
    }
    // Verifico la configurazione per localita
    if (recapito.des_localita && !isReset) {
      // Setto il valore
      this.initCampoForm(this.RR_C.LOCALITA, recapito.des_localita);
    }
    // Verifico la configurazione per citta estera
    if (recapito.citta_estera_recapito && !isReset) {
      // Setto il valore
      this.initCampoForm(
        this.RR_C.CITTA_ESTERA_RECAPITO,
        recapito.citta_estera_recapito
      );
    }
    // Verifico la configurazione per indirizzo
    if (recapito.indirizzo && !isReset) {
      // Setto il valore
      this.initCampoForm(this.RR_C.INDIRIZZO, recapito.indirizzo);
    }
    // Verifico la configurazione numero civico
    if (recapito.num_civico && !isReset) {
      // Setto il valore
      this.initCampoForm(this.RR_C.NUMERO_CIVICO, recapito.num_civico);
    }
    // Verifico la configurazione per cap
    if (recapito.cap_recapito && !isReset) {
      // Setto il valore
      this.initCampoForm(this.RR_C.CAP, recapito.cap_recapito);
    }
    // Verifico la configurazione per la lista di indirizzi spedizione
    if (recapito.indirizzi_spedizione && !isReset) {
      // Setto il valore
      this.initCampoForm(
        this.RR_C.INDIRIZZI_SPEDIZIONE,
        recapito.indirizzi_spedizione
      );
    }
  }

  /**
   * Funzione di comodo usato per l'init dei campi del form.
   * @param campo string form control name per il set del valore.
   * @param valore any valore da settare.
   */
  private initCampoForm(campo: string, valore: any) {
    // Imposto il campo secondo i parametri
    this._riscaUtilities.setFormValue(this.mainForm, campo, valore);
  }

  /**
   * Funzione che assegna ai campi del form, le validazioni per i dati anagrafici.
   * E' importante che il FormGroup sia già stato instanziato.
   * @param update boolean che definisce se è necessario ricaricare e ri-validare la form a seguito del cambio dei validatori.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  private initValidators(update?: boolean, uC?: FormUpdatePropagation) {
    // Variabili di comodo
    const f = this.mainForm;
    const m = this.modalita;
    const u = update;

    // Richiamo la funzione per la gestione dei validatori
    this._gestioneFormsDA.setValidatorsRecapiti(f, m, u, uC);
  }

  /**
   * Funzione di setup per le liste per le select del form.
   */
  private initListeSelect() {
    // Definisco un'oggetto per il recupero di tutte le informazioni delle liste
    const resources: IListeRReq = {
      tipiSede: this._location.getTipiSede(),
      stati: this._location.getNazioniAttive(),
      comune: this.scaricoComune(false),
      province: this._location.getProvinceAttive(),
    };

    // Recupero il comune se configuratio e le province
    forkJoin(resources).subscribe({
      next: (r: IListeRRes) => {
        // Estraggo gli oggetti
        const { tipiSede, stati, comune, province } = r;
        // Richiamo le funzioni di gestione delle liste
        this.onScaricoTipiSede(tipiSede);
        this.onScaricoStati(stati);
        this.onScaricoComune(comune, province);

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
   * @param tipiSede Array di TipoSedeVo scaricati.
   */
  private onScaricoTipiSede(tipiSede: TipoSedeVo[]) {
    // Assegno localmente la lista
    this.listaTipiSede = tipiSede;
    // Setup iniziale
    this.initTipoSede();
  }

  /**
   * Funzione invocata allo scarico delle nature giuridiche.
   * @param stati Array di NazioneVo scaricati.
   */
  private onScaricoStati(stati: NazioneVo[]) {
    // Assegno localmente la lista
    this.listaStati = stati;
    // Setup iniziale
    this.initStato();
  }

  /**
   * Funzione di comodo per l'aggiornamento del comune.
   */
  private aggiornaComune() {
    // Scarico il comune
    this.scaricoComune(true).subscribe({
      next: (comune: ComuneVo) => {
        // Richiamo il setup
        this.onScaricoComune(comune, this.listaProvince);
        // #
      },
      error: (e: RiscaServerError) => {
        // Loggo l'errore
        this._logger.error('onFormRestore', e);
        // #
      },
    });
  }

  /**
   * Funzione che imposta le informazioni per lo scarico del comune.
   * @param attivo boolean che definisce se ricercare solo i comuni attivi (true) o tutti i comuni (false).
   */
  private scaricoComune(attivo: boolean): Observable<ComuneVo> {
    // Tento di recuperare il comune configurato
    const comuneRecapito = this.recapito?.comune_recapito;
    // Definisco i parametri per le chiamate
    const ciCR = comuneRecapito?.cod_istat_comune;

    // Ritorno la chiamata al servizio
    if (attivo) {
      // Richiamo lo scarico comune tra gli attivi
      return this._location.getComuneAttivo(ciCR);
      // #
    } else {
      // Richiamo lo scarico comune tra tutti i comuni
      return this._location.getComuneAll(ciCR);
      // #
    }
  }

  /**
   * Funzione invocata allo scarico del comune e delle province.
   * @param comune Comune scaricato.
   * @param province Array di ProvinciaVo scaricate.
   */
  private onScaricoComune(comune: ComuneVo, province: ProvinciaVo[]) {
    // Setto il valore per la lista delle province
    this.listaProvince = province;

    // Se esiste il comune recupero l'id provincia
    const idProvincia = comune?.id_provincia;
    // Lancio il setup del comune
    this.initComune(comune);
    // Lancio l'init della provincia
    this.initProvincia(idProvincia);
  }

  /**
   * Funzione di setup per il tipo soggetto.
   * @param isReset boolean che definisce che è richiesto un reset totale.
   */
  private initTipoSede(isReset?: boolean) {
    // Verifico se è stato richiesto un reset
    if (isReset) {
      // Resetto il valore per il tipo sede
      this.initCampoForm(this.RR_C.TIPO_SEDE, null);
      // Blocco il flusso
      return;
    }

    // Setup per il valore di default del form
    this._location.setTipoSedeDefaultByTipoSoggetto(
      this.mainForm,
      this.RR_C.TIPO_SEDE,
      this.listaTipiSede,
      this.datiAnagraficiConfig?.tipo_soggetto,
      this.recapito?.tipo_sede
    );
  }

  /**
   * Funzione di setup per la nazione.
   * @param isReset boolean che definisce che è richiesto un reset totale.
   */
  private initStato(isReset?: boolean) {
    // Recupero l'id nazione
    let codNazione: string;
    // Verifico se è stato richiesto un reset
    if (isReset || !this.recapito) {
      // Imposto la nazione di default
      codNazione = this.C_C.COD_NAZ_ITA;
      // #
    } else {
      // Recupero lo stato del recapito
      const nazioneRecapito = this.recapito.nazione_recapito;
      // Verifico se esiste una configurazione per il default
      codNazione = nazioneRecapito.cod_istat_nazione || this.C_C.COD_NAZ_ITA;
    }

    // Setup iniziale
    this._location.setNazioneDefault(
      this.mainForm,
      this.RR_C.STATO,
      this.listaStati,
      codNazione
    );

    // Aggiorno i validatori
    this.updateStatoValidator();
  }

  /**
   * Funzione di setup per il comune
   * @param comune Comune contente i dati del comune.
   */
  private initComune(comune: ComuneVo) {
    // Verifico l'input
    if (!comune) {
      return;
    }

    // Definisco la configurazione di non propagazione dell'evento
    const c = { emitEvent: false };
    // Imposto il comune nel form bloccando la propagazione dell'evento, che andrebbe a richiarare le logiche del listener
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.RR_C.COMUNE,
      comune,
      c
    );
  }

  /**
   * Funzione di setup per la nazione.
   * @param idProvincia number che definisce l'id della provincia, se non definito il dato verrà resettato.
   */
  private initProvincia(idProvincia?: number) {
    // Verifico se è un reset
    if (!idProvincia) {
      // Aggiorno il valore della provincia
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RR_C.PROVINCIA,
        null
      );
      // Blocco il flusso
      return;
    }

    // Setup iniziale
    this._location.setProvinciaDefault(
      this.mainForm,
      this.RR_C.PROVINCIA,
      this.listaProvince,
      idProvincia
    );
  }

  /**
   * #####################
   * METODI DEL COMPONENTE
   * #####################
   */

  /**
   * Funzione che assegna ai campi del form, le validazioni per i validatori dello stato.
   * E' importante che il FormGroup sia già stato instanziato.
   * @param update boolean che definisce se è necessario ricaricare e ri-validare la form a seguito del cambio dei validatori.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  private updateStatoValidator(
    update = true,
    uC: FormUpdatePropagation = { onlySelf: true, emitEvent: false }
  ) {
    // Lancio i validatori a seguito della selezione del tipo soggetto
    this._riscaRecapito.validatoriStato(
      this.mainForm,
      this.modalita,
      update,
      uC
    );
  }

  /**
   * Funzione che gestisce i dati relativi al comune, una volta che questo è stato selezionato.
   * @param comune Comune selezionato.
   * @param updateCAP boolean che definisce se è d'aggiornare anche il CAP, recuperandolo dal comune. Per default è: false.
   */
  private comuneSelected(comune: ComuneVo, updateCAP: boolean = false) {
    // Verifico l'input
    if (!comune) {
      return;
    }

    // Imposto la provincia per comune
    this._location.setProvinciaByComune(
      this.mainForm,
      this.RR_C.PROVINCIA,
      this.listaProvince,
      comune
    );

    // Verifico il flag d'aggiornamento del cap
    if (updateCAP) {
      // Imposto il cap per il comune
      this._location.setCAPByComune(this.mainForm, this.RR_C.CAP, comune);
    }
  }

  /**
   * Funzione che gestisce i dati relativi allo stato, una volta che questo è stato selezionato.
   * @param stato NazioneVo selezionato.
   */
  private statoSelected(stato: NazioneVo) {
    // Verifico l'input
    if (!stato) return;

    // Verifico se lo stato selezionato è l'italia
    if (stato.cod_istat_nazione === this.C_C.COD_NAZ_ITA) {
      // Resetto le informazioni
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RR_C.CITTA_ESTERA_RECAPITO,
        null
      );
      // #
    } else {
      // Resetto le informazioni
      this._riscaUtilities.setFormValue(this.mainForm, this.RR_C.COMUNE, null);
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RR_C.PROVINCIA,
        null
      );
    }

    // Richiamo il gestore dei validatori
    this.updateStatoValidator();
  }

  /**
   * #####################################
   * GESTIONE DELL'INDIRIZZO DI SPEDIZIONE
   * #####################################
   */

  /**
   * Funzione avviata al click del pulsante "Indirizzo spedizione"
   */
  aggiornaIndirizzoSpedizione() {
    // Prendo i dati che mi servono
    const soggetto = this.datiAnagraficiConfig;
    const idRecapito = this.recapito?.id_recapito;
    const idGruppo = this.gruppoIS?.id_gruppo_soggetto;
    const isFormDisabilitato = this.isISFormDisabilitato;

    // Creo la configurazione per le callbacks della modale
    const callbacks: ICallbackDataModal = {
      onConfirm: (indirizziOK: IndirizzoSpedizioneVo[]) => {
        // Invoco la funzione alla chiusura della modale
        this.aggiornaIndirizziSpedizioneInForm(indirizziOK);
      },
    };

    // Oggetto di configurazione per la modale di modifica degli indirizzi di spedizione
    const config: IAggiornaIndirizzoSpedizione = {
      soggetto,
      idRecapito,
      idGruppo,
      callbacks,
      isFormDisabilitato,
    };

    // Apertura della modale
    this._isUtility.modificaIndirizziSpedizioneSoggetto(config);
  }

  /**
   * Aggiorna l'indirizzo di spedizione in base all'aggiornamento fatto dalla modale di modifica degli indirizzi di spedizione del soggetto
   * @param indirizziOK IndirizzoSpedizioneVo[] indirizzi corretti
   */
  private aggiornaIndirizziSpedizioneInForm(
    indirizziOK: IndirizzoSpedizioneVo[]
  ) {
    // Prendo l'oggetto aggiornato
    this.indirizzoSpedizione = this.onIndirizziSpedizioneCorretti(indirizziOK);
    // Prendo la lista degli indirizzi dalla form
    const indirizziForm: IndirizzoSpedizioneVo[] =
      this._riscaUtilities.getFormValue(
        this.mainForm,
        this.RR_C.INDIRIZZI_SPEDIZIONE
      );

    // Trovo l'oggetto da aggiornare nel form
    const indexOggettoDaAggiornare_form = indirizziForm.findIndex(
      (ind) =>
        ind.id_recapito_postel == this.indirizzoSpedizione.id_recapito_postel
    );

    // Controllo se l'ho trovato
    if (indexOggettoDaAggiornare_form >= 0) {
      // Aggiorno la lista degli indirizzi
      indirizziForm[indexOggettoDaAggiornare_form] = this.indirizzoSpedizione;
      // Aggiorno la form
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RR_C.INDIRIZZI_SPEDIZIONE,
        indirizziForm
      );
    }

    // Trovo l'oggetto da aggiornare nel recapito
    const indexOggettoDaAggiornare_recapito =
      this.recapito?.indirizzi_spedizione?.findIndex(
        (ind) =>
          ind.id_recapito_postel == this.indirizzoSpedizione.id_recapito_postel
      );

    // Controllo se l'ho trovato
    if (indexOggettoDaAggiornare_recapito >= 0) {
      // Aggiorno l'oggetto corrente
      this.recapito.indirizzi_spedizione[indexOggettoDaAggiornare_recapito] =
        this.indirizzoSpedizione;
    }

    // Aggiorno i dati anagrafici del componente padre
    if (this.datiAnagraficiConfig == null) {
      return;
    }

    // Creo l'oggetto da inviare
    const datiAnagraficiUpd = cloneDeep(this.datiAnagraficiConfig);

    // Trovo il recapito dei dati anagrafici da aggiornare
    const indexOggettoDaAggiornare_DA = datiAnagraficiUpd.recapiti?.findIndex(
      (rec) => this.recapito.id_recapito == rec.id_recapito
    );

    if (indexOggettoDaAggiornare_DA >= 0) {
      // Trovo l'indice dell'indirizzo spedizione nel recapito da aggiornare
      const indexOggettoDaAggiornare_IndSped = datiAnagraficiUpd.recapiti[
        indexOggettoDaAggiornare_DA
      ].indirizzi_spedizione.findIndex(
        (ind) =>
          ind.id_recapito_postel == this.indirizzoSpedizione.id_recapito_postel
      );

      if (indexOggettoDaAggiornare_IndSped >= 0) {
        // Aggiorno l'indirizzo spedizione
        datiAnagraficiUpd.recapiti[
          indexOggettoDaAggiornare_DA
        ].indirizzi_spedizione[indexOggettoDaAggiornare_IndSped] =
          this.indirizzoSpedizione;
      }
    }
    // Emetto l'oggetto aggiornato
    this.updateDA.emit(datiAnagraficiUpd);
  }

  /**
   * Estrae il primo elemento dagli indirizzi ottenuti dalla modale degli indirizzi di spedizione.
   * Poi lo inserisce nell'indirizzo corrente.
   * @param indirizziOK IndirizzoSpedizioneVo[] con gli indirizzi ottenuti dalla chiamata precedente.
   * @returns IndirizzoSpedizioneVo primo elemento dell'input.
   */
  onIndirizziSpedizioneCorretti(
    indirizziOK: IndirizzoSpedizioneVo[]
  ): IndirizzoSpedizioneVo {
    // Controlli di esistenza
    if (indirizziOK == null || indirizziOK.length == 0) {
      return null;
    }
    // Ritorno il primo elemento
    return indirizziOK[0];
  }

  /**
   * Setta l'indirizzo spedizione dal recapito
   */
  indirizzoSpedizioneFromRecapito() {
    // Setto l'indirizzo di spedizione
    this.indirizzoSpedizione = this._isUtility.indirizzoSpedizioneFromRecapito(
      this.recapito,
      this.gruppoIS?.id_gruppo_soggetto
    );
  }

  /**
   * ######################
   * FUNZIONALITA' DEL FORM
   * ######################
   */

  /**
   * Funzione di reset manuale richiamabile dal componente padre.
   * @override
   */
  onFormReset() {
    // Resetto la form
    this.mainForm.reset();
    // Resetto il flag del form submitted
    this.mainFormSubmitted = false;

    // Rilancio i setup/init del componente
    this.initTipoSede(true);
    this.initStato(true);
  }

  /**
   * Funzione di restore manuale richiamabile dal componente padre.
   */
  onFormRestore() {
    // Resetto il flag del form submitted
    this.mainFormSubmitted = false;

    // Rilancio i setup/init del componente
    this.initCampiForm();
    this.initTipoSede();
    this.initStato();
    this.aggiornaComune();
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Determina se il pulsante "Indirizzo spedizione" è visibile o meno.
   */
  get showISBtn(): boolean {
    // Richiamo il getter di controllo per il recapito
    return this.isRecapitoPrinciapale;
  }

  /**
   * Getter di comodo per verificare se nel form è impostato lo stato: Italia.
   */
  get fStatoItalia() {
    // Verifico lo stato settato se è italia
    return this._location.isNazioneInForm(
      this.mainForm,
      this.RR_C.STATO,
      this.C_C.COD_NAZ_ITA
    );
  }

  /**
   * Determina lo stile con cui appare il pulsante "Indirizzo spedizione".
   */
  get configISBtn(): IRiscaButtonAllConfig {
    let customButton = '';

    if (this.indirizzoSpedizione != undefined) {
      if (isSBNTrue(this.indirizzoSpedizione.ind_valido_postel)) {
        customButton = 'btn-success';
      } else {
        customButton = 'btn-error';
      }
    }

    return {
      cssConfig: { customButton },
      dataConfig: {
        label: this.RR_C.BUTTON_INDIRIZZO_SPEDIZIONE,
        // codeAEA: this.AEAK_C.DETTAGLIO_IND_SPED,
      },
    };
  }

  /**
   * Getter di comodo che verifica le condizioni per abilitare il form e le sue componenti.
   * @returns boolean che specifica se il form e le sue componenti sono disabilitate.
   */
  get isISFormDisabilitato() {
    // Verifico il flag di abilitazione per "isGestioneAbilitata"
    const disabledIGA = !this.isGestioneAbilitata;
    // Verifico i flag di abilitazione per "accesso elementi app"
    const disabledAEA =
      this.insModSoggettoDisabled || this.praticaSoggettoDisabled;

    // Verifico i flag
    return disabledIGA || disabledAEA;
  }

  /**
   * Getter che definisce se il button dell'indirizzo di spedizione è da disabilitare.
   */
  get disableISBtn(): boolean {
    return this.indirizzoSpedizione == undefined;
  }
}
