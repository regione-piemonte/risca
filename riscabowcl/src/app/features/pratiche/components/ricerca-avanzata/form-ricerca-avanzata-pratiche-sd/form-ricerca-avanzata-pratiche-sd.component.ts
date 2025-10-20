import {
  Component,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin, Observable } from 'rxjs';
import { ComuneVo } from '../../../../../core/commons/vo/comune-vo';
import { NazioneVo } from '../../../../../core/commons/vo/nazione-vo';
import { PraticaDTVo } from '../../../../../core/commons/vo/pratica-vo';
import { ProvinciaCompetenzaVo } from '../../../../../core/commons/vo/provincia-competenza-vo';
import { ProvinciaVo } from '../../../../../core/commons/vo/provincia-vo';
import {
  IRicercaIstanzaVo,
  IRicercaProvvedimentoVo,
} from '../../../../../core/commons/vo/riscossione-search-vo';
import { StatoRiscossioneVo } from '../../../../../core/commons/vo/stati-riscossione-vo';
import { TipoIstanzaProvvedimentoVo } from '../../../../../core/commons/vo/tipo-istanza-provvidemento-vo';
import { TipoTitoloVo } from '../../../../../core/commons/vo/tipo-titolo-vo';
import { IUsoLeggeVo } from '../../../../../core/commons/vo/uso-legge-vo';
import { LoggerService } from '../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../../core/services/user.service';
import { RAIstanzeTable } from '../../../../../shared/classes/risca-table/ricerca-avanzata/ricerca-avanzata-istanze.table';
import { RAProvvedimentiTable } from '../../../../../shared/classes/risca-table/ricerca-avanzata/ricerca-avanzata-provvedimenti.table';
import { RiscaFormChildComponent } from '../../../../../shared/components/risca/risca-form-child/risca-form-child.component';
import { RiscaTableDataConfig } from '../../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../../shared/consts/common-consts.consts';
import { dataInizioDataFineValidator } from '../../../../../shared/miscellaneous/forms-validators/forms-validators';
import { LocationService } from '../../../../../shared/services/location.service';
import { RiscaFormBuilderService } from '../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  CodIstatNazioni,
  FormUpdatePropagation,
  RiscaRegExp,
} from '../../../../../shared/utilities';
import { RiscaErrorKeys } from '../../../../../shared/utilities/classes/errors-keys';
import { RiscaErrorsMap } from '../../../../../shared/utilities/classes/errors-maps';
import { TipoSoggettoVo } from '../../../../ambito/models';
import { IFRAPInitFormFields } from '../../../class/form-ricerca-avanzata-pratiche/form-ricerca-avanzata-pratiche.classes';
import { FormRicercaAvanzataPraticheConsts } from '../../../consts/ricerca-avanzata-pratiche/form-ricerca-avanzata-pratiche.consts';
import { SoggettoDatiAnagraficiService } from '../../../service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { PraticheService } from '../../../service/pratiche.service';
import { FormRicercaAvanzataPraticheService } from '../../../service/ricerca-avanzata-pratiche/form-ricerca-avanzata-pratiche/form-ricerca-avanzata-pratiche.service';
import { GestioneFormsRicercaPraticheAvanzataService } from '../../../service/ricerca-avanzata-pratiche/form-ricerca-avanzata-pratiche/gestione-forms-ricerca-pratiche-avanzata-service.service';
import { QuadriTecniciRicercaComponent } from '../../quadri-tecnici/components/middlewares/quadri-tecnici-ricerca/quadri-tecnici-ricerca.component';
import { DatiTecniciService } from '../../quadri-tecnici/services/dati-tecnici/dati-tecnici.service';
import { QuadriTecniciRicercaService } from '../../quadri-tecnici/services/quadri-tecnici/ricerca/quadri-tecnici-ricerca.service';
import { IRicercaAvanzataPraticheSDFilters } from './utilities/form-ricerca-avanzata-pratiche-sd.classes';
import { CodModalitaRicerca } from './utilities/form-ricerca-avanzata-pratiche-sd.enums';
import { FormRicercaAvanzataPSDFieldsConfigClass } from './utilities/form-ricerca-avanzata-pratiche-sd.fields-configs';
import {
  IFRAIstanza,
  IFRAPModalitaRicerca,
  IFRAProvvedimento,
  IRicercaPraticaAvanzataForm,
  IRicercaPraticaAvanzataFormRaw,
} from './utilities/form-ricerca-avanzata-pratiche-sd.interfaces';
import { TipoRiscossioneVo } from '../../../../../core/commons/vo/tipo-riscossione-vo';

/**
 * Interfaccia di comodo per lo scarico delle liste del componente. Formato: request.
 */
interface IListeSelectReq {
  listaTipiUtente: Observable<TipoSoggettoVo[]>;
  listaNazioni: Observable<NazioneVo[]>;
  listaProvince: Observable<ProvinciaVo[]>;
  listaProvinceCompetenza: Observable<ProvinciaCompetenzaVo[]>;
  listaTipiTitoli: Observable<TipoTitoloVo[]>;
  listaTipiProvvedimenti: Observable<TipoIstanzaProvvedimentoVo[]>;
  listaTipiRiscossione: Observable<TipoRiscossioneVo[]>;
  listaStatiPratiche: Observable<StatoRiscossioneVo[]>;
  listaTipiIstanza: Observable<TipoIstanzaProvvedimentoVo[]>;
  listaUsiDiLegge: Observable<IUsoLeggeVo[]>;
}

/**
 * Interfaccia di comodo per lo scarico delle liste del componente. Formato: response.
 */
interface IListeSelectRes {
  listaTipiUtente: TipoSoggettoVo[];
  listaNazioni: NazioneVo[];
  listaProvince: ProvinciaVo[];
  listaProvinceCompetenza: ProvinciaCompetenzaVo[];
  listaTipiTitoli: TipoTitoloVo[];
  listaTipiProvvedimenti: TipoIstanzaProvvedimentoVo[];
  listaTipiRiscossione: TipoRiscossioneVo[];
  listaStatiPratiche: StatoRiscossioneVo[];
  listaTipiIstanza: TipoIstanzaProvvedimentoVo[];
  listaUsiDiLegge: IUsoLeggeVo[];
}

@Component({
  selector: 'form-ricerca-avanzata-pratiche-sd',
  templateUrl: './form-ricerca-avanzata-pratiche-sd.component.html',
  styleUrls: ['./form-ricerca-avanzata-pratiche-sd.component.scss'],
  providers: [FormRicercaAvanzataPraticheService],
})
export class FormRicercaAvanzataPraticheSDComponent
  extends RiscaFormChildComponent<IRicercaAvanzataPraticheSDFilters>
  implements OnInit, OnChanges
{
  /** Oggetto contenente i valori costanti per il componente. */
  RRAP_C = new FormRicercaAvanzataPraticheConsts();
  /** Oggetto contenente i valori costanti comuni. */
  C_C = new CommonConsts();
  /** Classe costante contenente il mapping degli errori per i form. */
  EM = new RiscaErrorsMap();
  /** Classe RiscaRegExp contenente le regular expression dell'applicazione. */
  private riscaRegExp = new RiscaRegExp();

  /** Input IRiscaRicercaForm contenente le informazioni di parametrizzazione del componente. */
  @Input('ricerca') ricercaConfig: IRicercaAvanzataPraticheSDFilters;
  /** Input IFRAPModalitaRicerca che definisce qual è la modalità di ricerca impostata dal componente padre. */
  @Input('modalitaRicerca') modalitaRicercaConfig: IFRAPModalitaRicerca;

  /** Boolean che definisce lo stato di submit della form: ricercaPraticaForm. */
  ricercaPraticaSubmitted = false;

  /** Classe HomeFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: FormRicercaAvanzataPSDFieldsConfigClass;
  /** String che definisce il core dei dati tecnici, da passare poi ai componenti per pre-popolare la ricerca. */
  datiTecniciConfigs: string;

  /** Lista tipi modalità ricerca usata nella form */
  listaTipiModalitaRicerca: IFRAPModalitaRicerca[] =
    this.RRAP_C.MODALITA_RICERCA_AVANZATA;

  /** Lista tipi soggetto usata nella form */
  listaTipiSoggetto: TipoSoggettoVo[] = [];
  /** Lista nazioni usate nella form */
  listaNazioni: NazioneVo[] = [];
  /** Lista province usate nella form */
  listaProvince: ProvinciaVo[] = [];
  /** Lista di supporto contenente le informazioni per: provincia competenza. */
  listaProvinceCompetenza: ProvinciaCompetenzaVo[] = [];
  /** Lista tipi titoli */
  listaTipiTitoli: TipoTitoloVo[] = [];
  /** Lista provvedimenti */
  listaTipiProvvedimenti: TipoIstanzaProvvedimentoVo[] = [];
  /** Lista di supporto contenente le informazioni per: tipologia pratica. */
  listaTipologiePratica: TipoRiscossioneVo[] = [];
  /** Lista stati pratiche */
  listaStatiPratiche: StatoRiscossioneVo[] = [];
  /** Lista tipi istanza */
  listaTipiIstanza: TipoIstanzaProvvedimentoVo[] = [];
  /** Lista usi di legge */
  listaUsiDiLegge: IUsoLeggeVo[] = [];

  /** RAIstanzeTable contenente la definizione di struttura per la tabella delle istanze. */
  istanzeTable: RAIstanzeTable;
  /** RAProvvedimentiTable contenente la definizione di struttura per la tabella delle istanze. */
  provvedimentiTable: RAProvvedimentiTable;

  /** Boolean che definisce lo stato dell'accordion per gli usi di legge. */
  istanzaProvvedimentoApriChiudi = false;

  @ViewChild('appQuadriTecniciRicerca')
  appQuadriTecniciRicerca: QuadriTecniciRicercaComponent;

  constructor(
    private _datiTecnici: DatiTecniciService,
    private _formBuilder: FormBuilder,
    private _formRAP: FormRicercaAvanzataPraticheService,
    private _gestioneFRAP: GestioneFormsRicercaPraticheAvanzataService,
    private _location: LocationService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _pratiche: PraticheService,
    private _ricercaQuadriTecnici: QuadriTecniciRicercaService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    private _soggettoDA: SoggettoDatiAnagraficiService,
    submitHandlerService: RiscaFormSubmitHandlerService,
    private _user: UserService,
    utilityService: RiscaUtilitiesService
  ) {
    // Richiamo il setup per il super
    super(logger, navigationHelper, submitHandlerService, utilityService);

    // Lancio il setup del component
    this.setupComponente();
  }

  /**
   * ngOnInit
   */
  ngOnInit() {
    // Lancio il setup delle form
    this.initForms();
    // Setup delle liste per le select del form
    this.initListeSelect();
  }

  /**
   * ngOnChanges
   */
  ngOnChanges(changes: SimpleChanges) {
    // Variabili di comodo
    const changesMRC = changes?.modalitaRicercaConfig;

    // Verifico se ci sono da aggiornare i dati per la pratica
    if (changesMRC && !changesMRC.firstChange) {
      // Lancio il set per i dati della modalità di ricerca
      this.modalitaRicerca = changesMRC.currentValue;
    }
  }

  /**
   * ############################
   * FUNZIONI DI SETUP COMPONENTE
   * ############################
   */

  /**
   * Funzione di setup per le logiche del componente.
   */
  private setupComponente() {
    // Setep degli errori da verificare al submit della form
    this.setupMainFormErrors();
    // Setup dei campi di input del componente
    this.setupInputFields();
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  private setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [
      ...this.EM.MAP_PATTERN_COD_FISC,
      ...this.EM.MAP_PARTITA_IVA,
      ...this.EM.MAP_DATA_INI_FIN_SCADENZA_CONCESSIONE,
      ...this.EM.MAP_DATA_INI_FIN_DATA_RINUNCIA_REVOCA,
      ...this.EM.MAP_DATA_INI_FIN_DATA_ISTANZA,
      ...this.EM.MAP_DATA_INI_FIN_DATA_TITOLO,
    ];
  }

  /**
   * Funzione di setup per la configurazione dei campi di input del componente.
   */
  private setupInputFields() {
    // Classe di supporto che definisce la configurazioni per le input
    this.formInputs = new FormRicercaAvanzataPSDFieldsConfigClass(
      this._riscaFormBuilder,
      this._location
    );
  }

  /**
   * ############################
   * FUNZIONI DI SETUP COMPONENTE
   * ############################
   */

  /**
   * Funzione l'init delle informazioni per il componente.
   */
  private initForms() {
    // Setup del form
    this.initRicercaPratica();
    // Setup dei listiner dei form per la manipolazione dei dati
    this.initFormListener();
    // Lancio il setup per i validatori dei campi
    this.initValidators();
    // Lancio l'init per i campi disabled
    this.initDisabledFields();
    // Lancio l'init per le informazioni relative alla modalità di ricerca
    this.initModalitaRicerca();
  }

  /**
   * Funzione di setup per le liste per le select del form.
   */
  private initListeSelect() {
    // Recupero l'id ambiot
    const idA = this._user.idAmbito;

    // Creo la request per i dati
    const requestList: IListeSelectReq = {
      listaTipiUtente: this._soggettoDA.getTipiSoggetto(),
      listaNazioni: this._location.getNazioniAttive(),
      listaProvinceCompetenza: this._pratiche.getProvinceCompetenza(),
      listaProvince: this._location.getProvinceAttive(),
      listaTipiTitoli: this._pratiche.getTipiTitolo(idA),
      listaTipiProvvedimenti: this._pratiche.getTipiProvvedimento(idA),
      listaTipiRiscossione: this._pratiche.getTipiRiscossione(idA),
      listaStatiPratiche: this._pratiche.getStatiRiscossione(idA),
      listaTipiIstanza: this._pratiche.getTipiIstanza(idA),
      listaUsiDiLegge: this._datiTecnici.getUsiDiLegge(idA),
    };

    // Effettuo una forkjoin delle chiamate per le liste
    forkJoin(requestList).subscribe({
      next: (res: IListeSelectRes) => {
        const {
          listaTipiUtente,
          listaNazioni,
          listaProvince,
          listaProvinceCompetenza,
          listaTipiTitoli,
          listaTipiProvvedimenti,
          listaTipiRiscossione,
          listaStatiPratiche,
          listaTipiIstanza,
          listaUsiDiLegge,
        } = res;

        // Assegno le liste del componente
        this.listaTipiSoggetto = listaTipiUtente;
        this.listaNazioni = listaNazioni;
        this.listaProvince = listaProvince;
        this.listaProvinceCompetenza = listaProvinceCompetenza;
        this.listaTipiTitoli = listaTipiTitoli;
        this.listaTipiProvvedimenti = listaTipiProvvedimenti;
        this.listaTipologiePratica = listaTipiRiscossione;
        this.listaStatiPratiche = listaStatiPratiche;
        this.listaTipiIstanza = listaTipiIstanza;
        this.listaUsiDiLegge = listaUsiDiLegge;

        // Init della form con la nazione per impostare "Italia"
        this.initStato();
        // Init dei dati del form, se passata una configurazione
        this.initRicercaPraticaFields(this.ricercaConfig);
      },
      error: (err) => {
        this._logger.error('resError [initListeSelect]', err);
      },
    });
  }

  /**
   * Funzione di comodo che permette di valorizzare i campi della form se viene definito un oggetto di configurazione.
   * @param configs RicercaPraticaSemplice con i dati da caricare nel form.
   */
  private initRicercaPraticaFields(configs: IRicercaAvanzataPraticheSDFilters) {
    // Verifico l'input
    if (!configs) {
      // Niente da configurare
      return;
    }

    // Inserisco i dati nella form principale
    this.initFormFields(configs);
    // Recupero le istanze dalla snapshot
    this.initTabellaIstanze(configs.istanze);
    // Definisco la configurazione per i dati tecnici
    this.datiTecniciConfigs = configs.datiTecnici;
  }

  /**
   * Funzione di setup per lo stato.
   */
  private initStato() {
    // Setup iniziale
    this._location.setNazioneDefault(
      this.mainForm,
      this.RRAP_C.STATO_RESIDENZA,
      this.listaNazioni,
      this.C_C.COD_NAZ_ITA
    );
  }

  /**
   * Inserisce i dati della snapshot nella form principale.
   * @param source IFormRicercaAvanzataPratiche oggetto contenente i dati per la valorizzazione del form.
   */
  private initFormFields(source: IRicercaAvanzataPraticheSDFilters) {
    // Verifico l'input
    if (!source) {
      // Niente configurazione
      return;
    }

    // Genero l'oggetto da passare al servizio
    const configs: IFRAPInitFormFields = {
      form: this.mainForm,
      source: source,
      listaTipiModalitaRicerca: this.listaTipiModalitaRicerca,
      listaTipiUtente: this.listaTipiSoggetto,
      listaNazioni: this.listaNazioni,
      listaProvince: this.listaProvince,
      listaProvinceCompetenza: this.listaProvinceCompetenza,
      listaTipiTitoli: this.listaTipiTitoli,
      listaTipiProvvedimenti: this.listaTipiProvvedimenti,
      listaStatiPratiche: this.listaStatiPratiche,
      listaTipiIstanza: this.listaTipiIstanza,
    };

    // Lancio la funzione d'inizializzazione dei campi
    this._formRAP.initFormFields(configs);
  }

  /**
   * Funzione che genera i dati da visualizzare per la tabella delle istanze.
   * @param istanze Array di RicercaIstanza contenente i dati per le istanze ricercate.
   */
  private initTabellaIstanze(istanze: IRicercaIstanzaVo[]) {
    // Recupero la lista dei tipi istanza
    const listaTipiIstanza = this.listaTipiIstanza;
    // Richiamo la funzione del servizio per generare i dati da passare alla tabella
    const tipiIstanza = this._formRAP.generateIstanzeTable(
      istanze,
      listaTipiIstanza
    );

    // La tabella delle istanze potrebbe non essere istanziata
    if (this.istanzeTable == null) {
      // Creo l'istanza della tabella
      this.istanzeTable = new RAIstanzeTable(tipiIstanza);
    }

    // Pulisco la tabella istanze
    this.istanzeTable.clear();
    // Reinserisco gli elementi delle istanze alla ricerca
    this.istanzeTable.addElements(tipiIstanza);
  }

  /**
   * Setup del form: ricercaPraticaForm.
   */
  private initRicercaPratica() {
    this.mainForm = this._formBuilder.group(
      {
        modalitaRicerca: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        tipoSoggetto: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        ragioneSocialeOCognome: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        codiceFiscale: new FormControl(
          { value: null, disabled: false },
          {
            validators: [Validators.pattern(this.riscaRegExp.codiceFiscaleAll)],
          }
        ),
        partitaIVA: new FormControl(
          { value: null, disabled: false },
          { validators: [Validators.pattern(this.riscaRegExp.partitaIva)] }
        ),
        stato: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        provincia: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        indirizzo: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        comuneResidenza: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        provinciaCompetenza: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        cittaEsteraResidenza: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        corpoIdrico: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        comune: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        nomeImpiantoIdroelettrico: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        tipoTitolo: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        tipoProvvedimento: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        numeroTitolo: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        dataTitoloDa: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        dataTitoloA: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        tipologiaPratica: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        statoPratica: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        scadenzaConcessioneDa: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        scadenzaConcessioneA: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        dataRinunciaRevocaDa: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        dataRinunciaRevocaA: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        annoCanone: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        canone: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        restituitoAlMittente: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        tipoIstanza: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        dataIstanzaDa: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        dataIstanzaA: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
      },
      {
        validators: [
          dataInizioDataFineValidator(
            this.RRAP_C.SCADENZA_CONCESSIONE_DA,
            this.RRAP_C.SCADENZA_CONCESSIONE_A,
            false,
            RiscaErrorKeys.DATA_INI_FIN_SCADENZA_CONCESSIONE
          ),
          dataInizioDataFineValidator(
            this.RRAP_C.DATA_RINUNCIA_REVOCA_DA,
            this.RRAP_C.DATA_RINUNCIA_REVOCA_A,
            false,
            RiscaErrorKeys.DATA_INI_FIN_DATA_RINUNCIA_REVOCA
          ),
          dataInizioDataFineValidator(
            this.RRAP_C.DATA_ISTANZA_DA,
            this.RRAP_C.DATA_ISTANZA_A,
            false,
            RiscaErrorKeys.DATA_INI_FIN_DATA_ISTANZA
          ),
          dataInizioDataFineValidator(
            this.RRAP_C.DATA_TITOLO_DA,
            this.RRAP_C.DATA_TITOLO_A,
            false,
            RiscaErrorKeys.DATA_INI_FIN_DATA_TITOLO
          ),
        ],
      }
    );
  }

  /**
   * Funzione che assegna ai campi del form, le validazioni per i dati anagrafici.
   * E' importante che il FormGroup sia già stato instanziato.
   * @param update boolean che definisce se è necessario ricaricare e ri-validare la form a seguito del cambio dei validatori.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  private initValidators(update?: boolean, uC?: FormUpdatePropagation) {
    // Gestione delle logiche
  }

  /**
   * Funzione che assegna ai campi del form lo stato di disable per i dati anagrafici.
   * E' importante che il FormGroup sia già stato instanziato.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  private initDisabledFields(uC?: FormUpdatePropagation) {
    // Richiamo la funzione per la gestione dei campi disabilitati
    this._gestioneFRAP.setDisabledRicercaAvanzataPratiche(this.mainForm, null);
  }

  /**
   * Funzione specifica adibita al setup dei listner delle form.
   * La funzione registrerà diversi eventi sulle form, atte a gestire la logica e la manipolazione dei dati.
   */
  private initFormListener() {
    // Gestione dell'evento di modifica dati all'interno del form di ricerca
    this.mainForm.valueChanges.subscribe({
      next: (formData: IRicercaPraticaAvanzataForm) => {
        // Il form è stato modificato, lancio l'evento di propagazione
        this._ricercaQuadriTecnici.onRicercaAvanzataChange(formData);
      },
    });

    // Attivazione e disattivazione voci per modalità ricerca selezionata
    this.mainForm.get(this.RRAP_C.MODALITA_RICERCA).valueChanges.subscribe({
      next: (modalitaRicerca: IFRAPModalitaRicerca) => {
        // Lancio la funzione specifica per la gestione della modalita ricerca
        this.modalitaRicercaChange(modalitaRicerca);
        // Effettuo il reset per la gestione dei controlli sui dati
        this.mainForm.updateValueAndValidity();
      },
    });

    // Registro un ascoltatore per il cambio dello stato
    this.mainForm.get(this.RRAP_C.STATO_RESIDENZA).valueChanges.subscribe({
      next: (stato: NazioneVo) => {
        // Aggiorno i dati relativi allo stato
        this.statoSelected(stato);
      },
    });

    // Registro un ascoltatore per il cambio del comune
    this.mainForm.get(this.RRAP_C.COMUNE_RESIDENZA).valueChanges.subscribe({
      next: (comune: ComuneVo) => {
        // Aggiorno i dati relativi al comune
        this.comuneSelected(comune);
      },
    });
  }

  /**
   * Funzione che permette l'inizializzazione delle informazioni relative alla modalità di ricerca.
   */
  private initModalitaRicerca() {
    // Recupero la modalità di ricerca pratiche come default
    const modalitaPratiche = this.listaTipiModalitaRicerca[0];
    // Definisco la modalità partendo dalla configurazione in input, se non esiste imposto un default
    const modalita = this.modalitaRicercaConfig ?? modalitaPratiche;

    // Imposto il valore iniziale della modalità ricerca per triggerare la validazione
    this.modalitaRicerca = modalita;
  }

  /**
   * ###########################################
   * GESTIONE DELLA MODALITA DI RICERCA AVANZATA
   * ###########################################
   */

  /**
   * Funzione che gestisce le operazioni logiche nel momento in cui l'utente modifica la modalita di ricerca.
   * @param modalitaRicerca IFRAPModalitaRicerca con la nuova modalità selezionata.
   */
  private modalitaRicercaChange(modalitaRicerca: IFRAPModalitaRicerca) {
    // Verifico quale modalità di ricerca è stata selezionata
    switch (modalitaRicerca?.cod_modalita_ricerca) {
      // Pratica
      case CodModalitaRicerca.pratica: {
        // Gestisco pulizia e disabilitazioni dei campi
        this.onModalitaRicercaPratica();
        break;
      }
      // Stato debitorio
      case CodModalitaRicerca.statoDebitorio: {
        // Gestisco pulizia e disabilitazioni dei campi
        this.onModalitaRicercaStatoDebitorio();
        break;
      }
      // Null
      default: {
        // Variabili di comodo
        const f = this.mainForm;
        const noMod = null;
        // Disabilito senza modalita di ricerca
        this._gestioneFRAP.setDisabledRicercaAvanzataPratiche(f, noMod);
      }
    }
  }

  /**
   * Funzione invocata quando l'utente modifica la modalità di ricerca in: Pratica.
   */
  private onModalitaRicercaPratica() {
    // Variabili di comodo
    const f = this.mainForm;
    // Chiavi pre i campi d'aggiornare
    const keyCanone = this.RRAP_C.CANONE;
    const keyAnnoCanone = this.RRAP_C.ANNO_CANONE;
    const modPratica = CodModalitaRicerca.pratica;

    // Resetto i valori dei campi che non sono gestiti da questa modalita
    this._riscaUtilities.setFormValue(f, keyCanone, null);
    this._riscaUtilities.setFormValue(f, keyAnnoCanone, null);
    // Disabilito i campi per modalita
    this._gestioneFRAP.setDisabledRicercaAvanzataPratiche(f, modPratica);
  }

  /**
   * Funzione invocata quando l'utente modifica la modalità di ricerca in: Stato Debitorio.
   */
  private onModalitaRicercaStatoDebitorio() {
    // Variabili di comodo
    const f = this.mainForm;
    // Chiavi pre i campi d'aggiornare
    const keyTipoPratica = this.RRAP_C.TIPOLOGIA_PRATICA;
    const keyStatoPratica = this.RRAP_C.STATO_PRATICA;
    const keyDtRinunciaRevocaDa = this.RRAP_C.DATA_RINUNCIA_REVOCA_DA;
    const keyDtRinunciaRevocaA = this.RRAP_C.DATA_RINUNCIA_REVOCA_A;
    const modSD = CodModalitaRicerca.statoDebitorio;

    // Resetto i valori dei campi che non sono gestiti da questa modalita
    this._riscaUtilities.setFormValue(f, keyTipoPratica, null);
    this._riscaUtilities.setFormValue(f, keyStatoPratica, null);
    this._riscaUtilities.setFormValue(f, keyDtRinunciaRevocaDa, null);
    this._riscaUtilities.setFormValue(f, keyDtRinunciaRevocaA, null);
    // Disabilito i campi per modalita
    this._gestioneFRAP.setDisabledRicercaAvanzataPratiche(f, modSD);
  }

  /**
   * Funzione per l'annullamento della form di ricerca.
   */
  annullaRicercaAvanzata() {
    this.mainForm.reset();
    this.mainFormSubmitted = false;
    this.istanzeTable?.clear();
    this.provvedimentiTable?.clear();
    this.appQuadriTecniciRicerca?.onFormReset();
    this.initModalitaRicerca();
  }

  /**
   * Funzione di toggle per la visualizzazione dei dati per istanza.
   * @param stato boolean che definisce il nuovo stato dello switch accordion.
   */
  toggleIstanzaProvvedimento(stato: boolean) {
    // Effettuo il toggle per la visualizzazione
    this.istanzaProvvedimentoApriChiudi = !this.istanzaProvvedimentoApriChiudi;
  }

  /**
   * ####################################
   * FUNZIONI DI GESTIONE TABELLA ISTANZE
   * ####################################
   */

  /**
   * Funzione collegata al pulsante "AGGIUNGI" per la sezione delle istanze.
   * Le logiche della funzione andranno a recuperare i dati dalla porzione di form che interessa le istanze e aggiungerà una riga nella tabella apposita delle istanze.
   */
  aggiungiIstanza() {
    // Recupero dal form le informazioni per aggiungere un'istanza
    const tipoIstanza: TipoIstanzaProvvedimentoVo = this.tipoIstanza;
    const dataDa: NgbDateStruct = this.dataIstanzaDa;
    const dataA: NgbDateStruct = this.dataIstanzaA;

    // Verifico se esiste almeno un dato da inserire per la ricerca
    if (!tipoIstanza && !dataDa && !dataA) {
      // Mancano tutti i dati, blocco il flusso
      return;
    }

    // Verifico se esiste la classe per le istanze
    if (this.istanzeTable == null) {
      // Inizializzo la tabella
      this.istanzeTable = new RAIstanzeTable();
    }

    // Definisco l'oggetto di configurazione da passare alla tabella
    const element: IFRAIstanza = { tipoIstanza, dataDa, dataA };
    // Richiamo la add della table
    this.istanzeTable.addElement(element);
    // Resetto la sezione di form per le istanze
    this.resetFormIstanze();
  }

  /**
   * Funzione di reset della parte di form che interessa la gestione delle istanze.
   */
  resetFormIstanze() {
    // Variabili di comodo
    const mf = this.mainForm;
    const tiKey = this.RRAP_C.TIPO_ISTANZA;
    const dtIDaKey = this.RRAP_C.DATA_ISTANZA_DA;
    const dtIAKey = this.RRAP_C.DATA_ISTANZA_A;
    // Vado a resettare il valore per i form control specifici
    mf.get(tiKey)?.reset();
    mf.get(dtIDaKey)?.reset();
    mf.get(dtIAKey)?.reset();
  }

  /**
   * Funzione collegata all'HTML che permette l'eliminazione di una riga della tabella istanze.
   * @param row RiscaTableDataConfig<IFRAPIstanza> con le informazioni della riga da eliminare.
   */
  eliminaIstanza(row: RiscaTableDataConfig<IFRAIstanza>) {
    // Richiamo la remove della table
    this.istanzeTable.removeElement(row);
  }

  /**
   * Funzione che recupera le informazioni per le istanze inserite all'interno della maschera e converte gli oggetti in un formato compatibile con la ricerca.
   * @returns IRicercaIstanzaVo[] con le informazioni convertite per la ricerca.
   */
  generaIstanzeRicerca(): IRicercaIstanzaVo[] {
    // Verifico se esiste la tabella con i dati delle istanze
    if (!this.istanzeTable) {
      // Non esiste la tabella, ritorno array vuoto
      return [];
    }

    // Definisco il contenitore per gli oggetti da passare alla ricerca
    let istanze: IRicercaIstanzaVo[] = [];
    // La tabella esiste, recupero i dati "sorgente"
    const istanzeTable: IFRAIstanza[] = this.istanzeTable.getDataSource();

    // Rimappo le informazioni dall'oggetto di tabella all'oggetto di ricerca
    istanze = istanzeTable.map((iTbl: IFRAIstanza) => {
      // Recupero le informazioni per l'oggetto di ricerca
      const id_istanza: number = iTbl?.tipoIstanza?.id_tipo_provvedimento;
      const data_istanza_da: string =
        this._riscaUtilities.convertNgbDateStructToString(iTbl?.dataDa);
      const data_istanza_a: string =
        this._riscaUtilities.convertNgbDateStructToString(iTbl?.dataA);

      // Creo un nuovo oggetto per la ricerca
      let istanza: IRicercaIstanzaVo;
      istanza = { id_istanza, data_istanza_da, data_istanza_a };
      // Ritorno l'oggetto convertito
      return istanza;
      // #
    });

    // Conclusa la conversione, ritorno la lista di oggetti per le istanze
    return istanze;
  }

  /**
   * ##########################################
   * FUNZIONI DI GESTIONE TABELLA PROVVEDIMENTI
   * ##########################################
   */

  /**
   * Funzione collegata al pulsante "AGGIUNGI" per la sezione dei provvedimenti.
   * Le logiche della funzione andranno a recuperare i dati dalla porzione di form che interessa i provvedimenti e aggiungerà una riga nella tabella apposita dei provvedimenti.
   */
  aggiungiProvvedimento() {
    // Recupero dal form le informazioni per aggiungere un provvedimento
    const tipoProvvedimento: TipoIstanzaProvvedimentoVo =
      this.tipoProvvedimento;
    const tipoTitolo: TipoTitoloVo = this.tipoTitolo;
    const numeroTitolo: string = this.numeroTitolo;
    const dataDa: NgbDateStruct = this.dataTitoloDa;
    const dataA: NgbDateStruct = this.dataTitoloA;

    // Verifico se esiste almeno un dato da inserire per la ricerca
    if (
      !tipoProvvedimento &&
      !tipoTitolo &&
      !numeroTitolo &&
      !dataDa &&
      !dataA
    ) {
      // Mancano tutti i dati, blocco il flusso
      return;
    }

    // Verifico se esiste la classe per le provvedimento
    if (this.provvedimentiTable == null) {
      // Inizializzo la tabella
      this.provvedimentiTable = new RAProvvedimentiTable();
    }

    // Definisco l'oggetto di configurazione da passare alla tabella
    const element: IFRAProvvedimento = {
      tipoProvvedimento,
      tipoTitolo,
      numeroTitolo,
      dataDa,
      dataA,
    };
    // Richiamo la add della table
    this.provvedimentiTable.addElement(element);
    // Resetto la sezione di form per le istanze
    this.resetFormProvvedimenti();
  }

  /**
   * Funzione di reset della parte di form che interessa la gestione dei provvedimenti.
   */
  resetFormProvvedimenti() {
    // Variabili di comodo
    const mf = this.mainForm;
    const tpKey = this.RRAP_C.TIPO_PROVVEDIMENTO;
    const ttKey = this.RRAP_C.TIPO_TITOLO;
    const ntKey = this.RRAP_C.NUMERO_TITOLO;
    const dtTDaKey = this.RRAP_C.DATA_TITOLO_DA;
    const dtTAKey = this.RRAP_C.DATA_TITOLO_A;
    // Vado a resettare il valore per i form control specifici
    mf.get(tpKey)?.reset();
    mf.get(ttKey)?.reset();
    mf.get(ntKey)?.reset();
    mf.get(dtTDaKey)?.reset();
    mf.get(dtTAKey)?.reset();
  }

  /**
   * Funzione collegata all'HTML che permette l'eliminazione di una riga della tabella provvedimenti.
   * @param row RiscaTableDataConfig<IFRAPIstanza> con le informazioni della riga da eliminare.
   */
  eliminaProvvedimento(row: RiscaTableDataConfig<IFRAIstanza>) {
    // Richiamo la remove della table
    this.provvedimentiTable.removeElement(row);
  }

  /**
   * Funzione che recupera le informazioni per i provvedimenti inseriti all'interno della maschera e converte gli oggetti in un formato compatibile con la ricerca.
   * @returns IRicercaProvvedimentoVo[] con le informazioni convertite per la ricerca.
   */
  generaProvvedimentiRicerca(): IRicercaProvvedimentoVo[] {
    // Verifico se esiste la tabella con i dati delle provvedimenti
    if (!this.provvedimentiTable) {
      // Non esiste la tabella, ritorno array vuoto
      return [];
    }

    // Definisco il contenitore per gli oggetti da passare alla ricerca
    let provvedimenti: IRicercaProvvedimentoVo[] = [];
    // La tabella esiste, recupero i dati "sorgente"
    const provvedimentiTable: IFRAProvvedimento[] =
      this.provvedimentiTable.getDataSource();

    // Rimappo le informazioni dall'oggetto di tabella all'oggetto di ricerca
    provvedimenti = provvedimentiTable.map((pTbl: IFRAProvvedimento) => {
      // Recupero le informazioni per l'oggetto di ricerca
      const id_provvedimento: number =
        pTbl?.tipoProvvedimento?.id_tipo_provvedimento;
      const id_tipo_titolo: number = pTbl?.tipoTitolo?.id_tipo_titolo;
      const numero_titolo: string = pTbl?.numeroTitolo;
      const data_provvedimento_da: string =
        this._riscaUtilities.convertNgbDateStructToString(pTbl?.dataDa);
      const data_provvedimento_a: string =
        this._riscaUtilities.convertNgbDateStructToString(pTbl?.dataA);

      // Creo un nuovo oggetto per la ricerca
      let provvedimento: IRicercaProvvedimentoVo = {
        id_provvedimento,
        id_tipo_titolo,
        numero_titolo,
        data_provvedimento_da,
        data_provvedimento_a,
      };
      // Ritorno l'oggetto convertito
      return provvedimento;
      // #
    });

    // Conclusa la conversione, ritorno la lista di oggetti per provvedimenti
    return provvedimenti;
  }

  /**
   * ########################################
   * FUNZIONI GESTIONE STATO/COMUNE/PROVINCIA
   * ########################################
   */

  /**
   * Funzione che gestisce i dati relativi allo stato, una volta che questo è stato selezionato.
   * @param stato NazioneVo selezionato.
   */
  private statoSelected(stato: NazioneVo) {
    // Verifico l'input
    if (!stato) {
      // Nessuno stato selezionato
      return;
    }

    // Variabili di comodo
    const f = this.mainForm;
    const noEmit: FormUpdatePropagation = { emitEvent: false };
    // Variabile di comodo per il check sullo stato
    const isITASelected = stato.cod_istat_nazione === CodIstatNazioni.italia;

    // Verifico se lo stato selezionato è l'italia
    if (isITASelected) {
      // Variabile di comodo per i dati per gli esteri
      const keyCittaEstera = this.RRAP_C.CITTA_ESTERA_RESIDENZA;
      // Resetto le informazioni per gli stati esteri
      this._riscaUtilities.setFormValue(f, keyCittaEstera, null, noEmit);
      // #
    } else {
      // Variabile di comodo per i dati dell'italia
      const keyComuneRes = this.RRAP_C.COMUNE_RESIDENZA;
      const keyProvinciaRes = this.RRAP_C.PROVINCIA_RESIDENZA;
      // Resetto le informazioni per italia
      this._riscaUtilities.setFormValue(f, keyComuneRes, null, noEmit);
      this._riscaUtilities.setFormValue(f, keyProvinciaRes, null, noEmit);
      // #
    }
  }

  /**
   * Funzione che gestisce i dati relativi al comune, una volta che questo è stato selezionato.
   * @param comune Comune selezionato.
   */
  private comuneSelected(comune: ComuneVo) {
    // Verifico l'input
    if (!comune) {
      return;
    }

    // Imposto la provincia per comune
    this._location.setProvinciaByComune(
      this.mainForm,
      this.RRAP_C.PROVINCIA_RESIDENZA,
      this.listaProvince,
      comune
    );
  }

  /**
   * ###############################
   * FUNZIONI DI GESTIONE DELLA FORM
   * ###############################
   */

  /**
   * Funzione che gestisce l'evento di invio dei dati della form di ricerca
   * @override
   */
  onFormSubmit() {
    this.appQuadriTecniciRicerca.onFormSubmit();
  }

  /**
   * Funzione che permette il recupero del raw value del main form del child.
   * In questo caso il ritorno sarà any poiché le logiche dovrebbero restituire un oggetto: IRicercaPraticaAvanzataForm.
   * Essendo che però le logiche sono gestite manualmente e differentemente, si ritorna any.
   * @param c any che definisce una configurazione generica che permette di gestire logiche nell'override della funzione.
   * @returns any contente le informazioni del form in modalità raw value.
   * @override
   */
  getMainFormRawValue(c?: any): any {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      // Ritorno undefined
      return undefined;
    }

    // Il main form esiste, inizio il recupero dei dati del form
    let rawFormData: IRicercaPraticaAvanzataFormRaw;
    rawFormData = this.mainForm.getRawValue();

    // Converto le informazioni in maniera tale da ottenere un oggetto: IRicercaPraticaAvanzataForm
    let formData: IRicercaPraticaAvanzataForm;
    // Destrutturo l'oggetto "crudo" del form, ma bisogna andare a manipolare specifici dati
    formData = { ...(rawFormData as any) };

    // Resetto le informazioni i campi da convertire manualmente
    formData.restituitoAlMittente = undefined;
    // Recupero le informazioni con tipizzazione corretta
    const restituitoAlMittente =
      rawFormData.restituitoAlMittente?.check ?? false;
    // Assegno le informazioni tipizzate correttamente
    formData.restituitoAlMittente = restituitoAlMittente;

    // Il main form esiste, ritorno i dati del form
    return formData;
  }

  /**
   * Funzione che gestisce l'agglomerato di informazioni della ricerca avanzata pratiche generando l'oggetto di ricerca e inserendovi i dati tecnici.
   * Viene chiamata dopo l'evento di submit che parte dalla componente padre e arriva a quella dei dati tecnici,
   * per poi lanciare l'evento di submit che passa i dati tecnici a questa funzione che li trasforma nell'oggetto di ricerca e li passa al padre che farà l'invio dal servizio.
   * @param infoConDatiTecnici PraticaDTVo che contiene i dati generati per la ricerca.
   */
  inviaRiscossioneSearch(infoConDatiTecnici: PraticaDTVo) {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      // Non esiste la configurazione del form
      return undefined;
    }

    // Verifico che la form sia valida
    if (this.mainForm.valid) {
      // Il main form esiste, inizio il recupero dei dati del form
      const formData: IRicercaPraticaAvanzataForm = this.getMainFormRawValue();
      // Ottengo la modalità ricerca selezionata
      const modalitaRicerca: IFRAPModalitaRicerca = this.modalitaRicerca;
      // Recupero le istanze e i provvedimenti
      const istanze: IRicercaIstanzaVo[] = this.generaIstanzeRicerca();
      const provvedimenti: IRicercaProvvedimentoVo[] =
        this.generaProvvedimentiRicerca();
      // Creo un oggetto any con i campi valorizzati dei dati tecnici
      const datiTecnici: string = infoConDatiTecnici?.riscossione?.dati_tecnici;

      // Creo l'oggetto per la ricerca da emettere
      const oggettoDatiRicerca: IRicercaAvanzataPraticheSDFilters = {
        modalitaRicerca,
        datiTecnici,
        pratica: formData,
        istanze,
        provvedimenti,
      };

      // Emetto l'oggetto praticaDTVo per far fare la ricerca al componente padre
      this.onFormSubmit$.emit(oggettoDatiRicerca);
      // #
    } else {
      // Gestisco la visualizzazione degli errori del form usiForm
      this.onFormErrors(this.mainForm);
    }
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo per il recupero dell'informazione dal form: modalitaRicerca.
   * @returns IFRAPModalitaRicerca con la modalita selezionata.
   */
  get modalitaRicerca(): IFRAPModalitaRicerca {
    // Variabile con la chiave di accesso al form control
    const formCtrlName = this.RRAP_C.MODALITA_RICERCA;
    // Tento di recuperare il dato all'interno del form
    return this.mainForm?.get(formCtrlName)?.value;
  }

  /**
   * Setter di comodo per l'assegnazione dell'informazione dal form: modalitaRicerca.
   * @param modalita IFRAPModalitaRicerca con la modalita da definire.
   */
  set modalitaRicerca(modalita: IFRAPModalitaRicerca) {
    // Variabile per la chiave della modalita di ricerca
    const formCtrlName = this.RRAP_C.MODALITA_RICERCA;
    // Imposto la modalità ricerca in input
    this.mainForm?.get(formCtrlName)?.setValue(modalita);
  }

  /**
   * Getter di comodo per il recupero dell'informazione dal form: tipoIstanza.
   * @returns TipoProvvedimentoVo con il dato all'interno del form.
   */
  get tipoIstanza(): TipoIstanzaProvvedimentoVo {
    // Variabile con la chiave di accesso al form control
    const formCtrlName = this.RRAP_C.TIPO_ISTANZA;
    // Tento di recuperare il dato all'interno del form
    return this.mainForm?.get(formCtrlName)?.value;
  }

  /**
   * Getter di comodo per il recupero dell'informazione dal form: dataIstanzaDa.
   * @returns NgbDateStruct con il dato all'interno del form.
   */
  get dataIstanzaDa(): NgbDateStruct {
    // Variabile con la chiave di accesso al form control
    const formCtrlName = this.RRAP_C.DATA_ISTANZA_DA;
    // Tento di recuperare il dato all'interno del form
    return this.mainForm?.get(formCtrlName)?.value;
  }

  /**
   * Getter di comodo per il recupero dell'informazione dal form: dataIstanzaA.
   * @returns NgbDateStruct con il dato all'interno del form.
   */
  get dataIstanzaA(): NgbDateStruct {
    // Variabile con la chiave di accesso al form control
    const formCtrlName = this.RRAP_C.DATA_ISTANZA_A;
    // Tento di recuperare il dato all'interno del form
    return this.mainForm?.get(formCtrlName)?.value;
  }

  /**
   * Getter di comodo per il recupero dell'informazione dal form: tipoProvvedimento.
   * @returns TipoProvvedimentoVo con il dato all'interno del form.
   */
  get tipoProvvedimento(): TipoIstanzaProvvedimentoVo {
    // Variabile con la chiave di accesso al form control
    const formCtrlName = this.RRAP_C.TIPO_PROVVEDIMENTO;
    // Tento di recuperare il dato all'interno del form
    return this.mainForm?.get(formCtrlName)?.value;
  }

  /**
   * Getter di comodo per il recupero dell'informazione dal form: tipoTitolo.
   * @returns TipoTitoloVo con il dato all'interno del form.
   */
  get tipoTitolo(): TipoTitoloVo {
    // Variabile con la chiave di accesso al form control
    const formCtrlName = this.RRAP_C.TIPO_TITOLO;
    // Tento di recuperare il dato all'interno del form
    return this.mainForm?.get(formCtrlName)?.value;
  }

  /**
   * Getter di comodo per il recupero dell'informazione dal form: numeroTitolo.
   * @returns string con il dato all'interno del form.
   */
  get numeroTitolo(): string {
    // Variabile con la chiave di accesso al form control
    const formCtrlName = this.RRAP_C.NUMERO_TITOLO;
    // Tento di recuperare il dato all'interno del form
    return this.mainForm?.get(formCtrlName)?.value;
  }

  /**
   * Getter di comodo per il recupero dell'informazione dal form: dataTitoloDa.
   * @returns NgbDateStruct con il dato all'interno del form.
   */
  get dataTitoloDa(): NgbDateStruct {
    // Variabile con la chiave di accesso al form control
    const formCtrlName = this.RRAP_C.DATA_TITOLO_DA;
    // Tento di recuperare il dato all'interno del form
    return this.mainForm?.get(formCtrlName)?.value;
  }

  /**
   * Getter di comodo per il recupero dell'informazione dal form: dataTitoloA.
   * @returns NgbDateStruct con il dato all'interno del form.
   */
  get dataTitoloA(): NgbDateStruct {
    // Variabile con la chiave di accesso al form control
    const formCtrlName = this.RRAP_C.DATA_TITOLO_A;
    // Tento di recuperare il dato all'interno del form
    return this.mainForm?.get(formCtrlName)?.value;
  }

  /**
   * Getter che recupera e verifica se il form control è errato per i campi: dataIstanzaDa/A.
   * @returns boolean con il risultato del controllo.
   */
  get dataIstanzaInvalid(): boolean {
    // Recupero le informazioni per il check sull'errore
    const f = this.mainForm;
    const e = RiscaErrorKeys.DATA_INI_FIN_DATA_ISTANZA;
    // Ritorno il risultato del check
    return this._riscaUtilities.checkFormGroupError(f, e);
  }

  /**
   * Getter che recupera e verifica se il form control è errato per i campi: dataTitoloDa/A.
   * @returns boolean con il risultato del controllo.
   */
  get dataTitoloInvalid(): boolean {
    // Recupero le informazioni per il check sull'errore
    const f = this.mainForm;
    const e = RiscaErrorKeys.DATA_INI_FIN_DATA_TITOLO;
    // Ritorno il risultato del check
    return this._riscaUtilities.checkFormGroupError(f, e);
  }

  /**
   * Getter di comodo per verificare se nel form è impostato lo stato: Italia.
   */
  get fStatoItalia() {
    // Verifico lo stato settato se è italia
    return this._location.isNazioneInForm(
      this.mainForm,
      this.RRAP_C.STATO_RESIDENZA,
      this.C_C.COD_NAZ_ITA
    );
  }

  /**
   * Getter per il check di esistenza dei dati della tabella istenze.
   * @returns boolean con il risultato del check.
   */
  get checkIstanzeTable(): boolean {
    // Verifica sulla tabella: esiste e ha dati
    return this.istanzeTable?.source?.length > 0;
  }

  /**
   * Getter per il check di esistenza dei dati della tabella provvedimenti.
   * @returns boolean con il risultato del check.
   */
  get checkProvvedimentiTable(): boolean {
    // Verifica sulla tabella: esiste e ha dati
    return this.provvedimentiTable?.source?.length > 0;
  }

  /**
   * Getter di comodo che ritorna un flag che indica se la modalità ricerca è: pratiche.
   * @returns boolean con il risultato del check.
   */
  get isMRPratiche(): boolean {
    // Recupero la modalita di ricerca dal form
    const mr: IFRAPModalitaRicerca = this.modalitaRicerca;
    // Verifico e ritorno la nav
    return mr?.cod_modalita_ricerca === CodModalitaRicerca.pratica;
  }

  /**
   * Getter di comodo che ritorna un flag che indica se la modalità ricerca è: stati debitori.
   * @returns boolean con il risultato del check.
   */
  get isMRStatiDebitori(): boolean {
    // Recupero la modalita di ricerca dal form
    const mr: IFRAPModalitaRicerca = this.modalitaRicerca;
    // Verifico e ritorno la nav
    return mr?.cod_modalita_ricerca === CodModalitaRicerca.statoDebitorio;
  }
}
