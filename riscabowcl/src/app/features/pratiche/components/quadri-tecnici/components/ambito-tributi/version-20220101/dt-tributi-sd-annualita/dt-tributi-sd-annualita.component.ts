import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { cloneDeep, max } from 'lodash';
import { forkJoin, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AnnualitaSDVo } from '../../../../../../../../core/commons/vo/annualita-sd-vo';
import { PraticaDTVo } from '../../../../../../../../core/commons/vo/pratica-vo';
import { IUsoLeggeVo } from '../../../../../../../../core/commons/vo/uso-legge-vo';
import { LoggerService } from '../../../../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../../../../../core/services/user.service';
import { CommonConsts } from '../../../../../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  FormUpdatePropagation,
  IRiscaAnnoSelect,
  RiscaServerError,
} from '../../../../../../../../shared/utilities';
import { RiscaErrorsMap } from '../../../../../../../../shared/utilities/classes/errors-maps';
import { DatiTecniciAmbienteSDConsts } from '../../../../../../consts/quadri-tecnici/dati-tecnici-ambiente-sd/dati-tecnici-ambiente-sd.consts';
import { DatiTecniciTributiConsts } from '../../../../../../consts/quadri-tecnici/dati-tecnici-tributi/dati-tecnici-tributi.consts';
import { PraticheService } from '../../../../../../service/pratiche.service';
import { DTAmbienteSDAnnualitaService } from '../../../../services/ambito-ambiente/version-20211001/dt-ambiente-sd-annualita.service';
import { DatiTecniciTributiConverterService } from '../../../../services/ambito-tributi/version-20220101/dati-tecnici-tributi-converter.service';
import { DTTributiSDAnnualitaConverterService } from '../../../../services/ambito-tributi/version-20220101/dt-tributi-sd-annualita-converter.service';
import { DatiTecniciEventsService } from '../../../../services/dati-tecnici/dati-tecnici-events.service';
import { DatiTecniciService } from '../../../../services/dati-tecnici/dati-tecnici.service';
import { QuadriTecniciSDService } from '../../../../services/quadri-tecnici/stato-debitorio/quadri-tecnici-sd.service';
import {
  DTAnnualitaConfig,
  DT_ANNUALITA_CONFIG,
} from '../../../../utilities/configs/dt-annualita.injectiontoken';
import { IDTAmbienteASD } from '../../../../utilities/interfaces/dt-ambiente-pratica.interfaces';
import { IDatiTecniciTributi } from '../../../../utilities/interfaces/dt-tributi-pratica.interfaces';
import { DatiTecniciSDComponent } from '../../../core/dati-tecnici-sd/dati-tecnici-sd.component';
import { DTAmbienteASDData } from '../../../middlewares/quadri-tecnici-sd/utilities/quadri-tecnici-sd.component.interfaces';
import { DTTSDAFieldsConfigClass } from './classes/dt-tributi-sd-annualita.fields-configs';

/**
 * Interfaccia di comodo che definisce l'insieme di chiamate per il recupero dei dati correlati ad un uso di legge.
 */
interface IDatiUsoLeggeReq {
  usi: Observable<IUsoLeggeVo[]>;
  anni: Observable<IRiscaAnnoSelect[]>;
}

/**
 * Interfaccia di comodo che definisce l'insieme di risposte dei dati correlati ad un uso di legge.
 */
interface IDatiUsoLeggeRes {
  usi: IUsoLeggeVo[];
  anni: IRiscaAnnoSelect[];
}

/**
 * Componente per la gestione del form dati tecnici annualita, ambito: ambiente.
 * Il form principale non ha un submit diretto.
 * Questo deve essere gestito dal padre.
 * Le informazioni del form verranno emesse registrandosi all'event emitter onFormChange.
 */
@Component({
  selector: 'dt-tributi-sd-annualita',
  templateUrl: './dt-tributi-sd-annualita.component.html',
  styleUrls: ['./dt-tributi-sd-annualita.component.scss'],
})
export class DTTributiSDAnnualitaComponent
  extends DatiTecniciSDComponent<DTAmbienteASDData>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti utilizzate comuni. */
  C_C = new CommonConsts();
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTA_SD_C = new DatiTecniciAmbienteSDConsts();
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTT_C = new DatiTecniciTributiConsts();

  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();

  /** DatiTecniciAmbienteSD che conterrà la configurazione dei dati generati dall'input. */
  datiTecniciAmbienteSD: IDTAmbienteASD;
  /** DatiTecniciAmbienteSD che conterrà la configurazione dei dati generati dall'input, al suo stato iniziale. */
  datiTecniciAmbienteSDInitial: IDTAmbienteASD;

  /** Lista degli anni da mostrare nelle annualità. */
  listaAnni: IRiscaAnnoSelect[] = [];
  /** Array di oggetti UsoLeggeVo recuperata dal server. */
  listaUsiDiLegge: IUsoLeggeVo[] = [];

  /** Classe DTASDAFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: DTTSDAFieldsConfigClass;

  /**
   * Costruttore.
   */
  constructor(
    @Inject(DT_ANNUALITA_CONFIG) injConfig: DTAnnualitaConfig,
    private _datiTecnici: DatiTecniciService,
    private _datiTecniciEvents: DatiTecniciEventsService,
    private _dttConverter: DatiTecniciTributiConverterService,
    private _dttSDAnnualita: DTAmbienteSDAnnualitaService,
    private _dttSDAConverter: DTTributiSDAnnualitaConverterService,
    private _formBuilder: FormBuilder,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    pratiche: PraticheService,
    quadriTecniciSD: QuadriTecniciSDService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {
    // Richiamo il super per il setup della classe estesa
    super(
      injConfig,
      logger,
      navigationHelper,
      pratiche,
      quadriTecniciSD,
      riscaFormSubmitHandler,
      riscaUtilities
    );
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit() {
    // Funzione che converte un oggetto PraticaDTVo in DatiTecniciAmbienteSD
    this.initDatiTecniciTributiSD();
  }

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
   * Funzione di comodo che racchiude le funzioni di setup per il componente.
   */
  private setupComponente() {
    // Setup degli errori da verificare al submit della form
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
      ...this.EM.MAP_FORMATO_NUMERO_6_INT_4_DEC,
      ...this.EM.MAP_ALMENO_UN_ELEMENTO,
      ...this.EM.MAP_STATO_DEBITORIO,
    ];
  }

  /**
   * Funzione di setup per le input del form.
   */
  private setupFormInputs() {
    // Essendo le configurazioni parecchie, si utilizza una classe di supporto
    this.formInputs = new DTTSDAFieldsConfigClass(
      this._riscaFormBuilder,
      this._riscaUtilities
    );
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Funzione che prepara possibili dati in input per la gestione di dati utilizzabili dal componente.
   */
  private initDatiTecniciTributiSD() {
    // Inizializzo il componente
    this.initForms();
    // Iniizializzazioni delle configurazioni
    this.initConfigs();
  }

  /**
   * Funzione che effettua il setup per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Setup della struttura della form principale #####
    this.initFormDTTributiAnnualita();
  }

  /**
   * Funzione che definisce le logiche di init per le configurazioni del componente.
   */
  private initConfigs() {
    // A seconda della modalità di gestione, ci troveremo differenti configurazioni
    if (this.inserimento) {
      // La configurazione deriva dall'inserimento
      this.initConfigsInsert();
      // #
    } else if (this.modifica) {
      // La configurazione deriva dalla modifica
      this.initConfigsUpdate();
      // #
    } else {
      // Configurazione mancante, è un errore
      const e = 'dt-ambiente-sd-annualita.component.ts modalita undefined';
      throw new Error(e);
    }
  }

  /**
   * Inizializzazione dati per l'inserimento dei dati tecnici annualità.
   * L'inserimento ha come sorgente dati iniziale i dati della pratica.
   */
  private initConfigsInsert() {
    // Verifico che esista la configurazione
    if (!this.praticaDT) {
      // Nessuna configurazione
      return;
    }

    // Variabile di comodo
    const pDT = this.praticaDT;
    const jsonDT = pDT?.riscossione?.dati_tecnici;
    // Effettuo la conversione dell'oggetto
    this._dttSDAConverter
      .convertPraticaDTVoToDatiTecniciTributiSD(pDT)
      .subscribe({
        next: (dtaSD: IDTAmbienteASD) => {
          // Inizializzo i dati
          this.iniDatiTecniciAmbienteSD(dtaSD, jsonDT);
          // #
        },
        error: (e: RiscaServerError) => {
          // Richiamo la funzione di gestione errori
          this.onServiziErrorAnnualita(e);
          // #
        },
      });
  }

  /**
   * Inizializzazione dati per la modifica dei dati tecnici annualità.
   * La modifica ha come sorgente dati iniziale i dati dell'annualità.
   */
  private initConfigsUpdate() {
    // Verifico che esista la configurazione
    if (!this.annualitaDT) {
      // Nessuna configurazione
      return;
    }

    // Variabile di comodo
    const aDT = this.annualitaDT;
    const jsonDT = aDT?.json_dt_annualita_sd;
    // Effettuo la conversione dell'oggetto
    this._dttSDAConverter.convertAnnualitaSDVoToDTAmbienteASD(aDT).subscribe({
      next: (dtaSD: IDTAmbienteASD) => {
        // Inizializzo i dati
        this.iniDatiTecniciAmbienteSD(dtaSD, jsonDT);
        // #
      },
      error: (e: RiscaServerError) => {
        // Richiamo la funzione di gestione errori
        this.onServiziErrorAnnualita(e);
        // #
      },
    });
  }

  /**
   * Funzione di comodo che inizializza le informazioni del dato tecnico ambiente stato debitorio.
   * @param dtaSD DatiTecniciAmbienteSD come informazione d'inizializzazione dati del componente.
   * @param jsonDT string che definisce il dato tecnico della pratica/annualità.
   */
  private iniDatiTecniciAmbienteSD(dtaSD: IDTAmbienteASD, jsonDT: string) {
    // Assegno l'oggetto convertito
    this.datiTecniciAmbienteSD = dtaSD;
    // Creo una copia per l'oggetto originale
    this.datiTecniciAmbienteSDInitial = cloneDeep(dtaSD);
    // Lancio l'init iniziale per tutti i dati del componente
    this.initComponenteFirstConfigs(this.datiTecniciAmbienteSD, jsonDT);
  }

  /**
   * Funzione specifica adibita al setup del form mainForm.
   */
  private initFormDTTributiAnnualita() {
    this.mainForm = this._formBuilder.group({
      annualita: new FormControl(
        { value: null, disabled: false },
        { validators: [Validators.required] }
      ),
      popolazione: new FormControl(
        { value: null, disabled: false },
        { validators: [Validators.required] }
      ),
      uso: new FormControl(
        { value: null, disabled: false },
        { validators: [Validators.required] }
      ),
    });
  }

  /**
   * Funzione specifica che gestisce in maniera sequenziale le fasi d'init del componente.
   * @param dta DTAmbienteASD che definisce l'oggetto per la popolazione delle informazioni della form.
   * @param jsonDT string che definisce il dato tecnico della pratica/annualità.
   */
  private initComponenteFirstConfigs(dta: IDTAmbienteASD, jsonDT: string) {
    // Estraggo l'anno per l'init annualità
    const annualita = dta?.annualita;
    // Richiamo la funzione di set dell'anno annualità e rimango in ascolto della conclusione
    this.initListeSelect(annualita).subscribe({
      next: (res: IDatiUsoLeggeRes) => {
        // La lista è già settata dentro la funzione, lancio l'init delle altre configurazioni componente
        this.initComponenteConfigs(dta, jsonDT);
        // #
      },
    });
  }

  /**
   * Popola i campi della form in base all'oggetto da modificare che é stato passato.
   * @param dta DTAmbienteASD che definisce l'oggetto per la popolazione delle informazioni della form.
   * @param jsonDT string che definisce il dato tecnico della pratica/annualità.
   */
  private initComponenteConfigs(dta: IDTAmbienteASD, jsonDT: string) {
    // Verifico esista la configurazione principale
    if (dta) {
      // Estraggo l'anno per l'init annualità
      const annualita = dta?.annualita;
      // Setto l'annualità di default
      this.initAnnualitaDefault(annualita);
    }

    // verifico esistano configurazioni dei dati tecnici
    if (jsonDT == undefined || jsonDT == '') {
      // Niente configurazioni
      return;
    }

    // Converto i dati tecnici e popolo i campi dei dati tecnici
    this._dttConverter
      .convertDatiTecniciCoreToDatiTecniciTributi(jsonDT)
      .subscribe({
        next: (dtt: IDatiTecniciTributi) => {
          // Chiamo la funzione che inizializza i campi dei dati tecnici
          this.initDTConfigs(dtt);
        },
        error: (error: RiscaServerError) => {
          // Gestisco l'errore
          this.onServiziErrorAnnualita(error);
        },
      });
  }

  /**
   * Funzione che inizializza popolazione ed uso a partire dai dati tecnici
   * @param dtTributi DatiTecniciTributi che contiene i dati di popolazione ed uso da inserire nella form
   */
  private initDTConfigs(dtTributi: IDatiTecniciTributi) {
    // Estraggo le informazioni
    const { popolazione, uso } = dtTributi;

    // Variabili di comodo per la gestione di select e dato nel form
    const mf = this.mainForm;
    const k = this.DTT_C.USO;
    const l = this.listaUsiDiLegge;
    const v = uso;
    const f = (a: IUsoLeggeVo, b: IUsoLeggeVo) => {
      return b.cod_tipo_uso == a.cod_tipo_uso;
    };
    const o: FormUpdatePropagation = { emitEvent: false };
    // Setto l'uso di legge
    this._riscaUtilities.setFormValueAndSelect(mf, k, l, v, f, o);

    // Setto la popolazione
    if (popolazione != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.DTT_C.POPOLAZIONE,
        popolazione,
        { emitEvent: false }
      );
    }
  }

  /**
   * Funzione di setup per le liste per le select del form.
   * @param annoAnnualita IRiscaAnnoSelect che definisce l'anno dell'annualità selezione. Verrà escloso dai blocchi e verrà impostato come valore di default nella form.
   * @returns Observable<IRiscaAnnoSelect[]> contenente la lista degli anni per la select.
   */
  private initListeSelect(
    annoAnnualita?: IRiscaAnnoSelect
  ): Observable<IDatiUsoLeggeRes> {
    // Recupero l'id dell'ambito
    const idAmbito = this._user.idAmbito;
    // Recupero l'anno dall'annualità
    const anno = annoAnnualita?.anno;

    // Creo le chiamate
    const usi: Observable<IUsoLeggeVo[]> =
      this._datiTecnici.getUsiDiLegge(idAmbito);

    const anni: Observable<IRiscaAnnoSelect[]> =
      this._dttSDAnnualita.generaListaAnnualita(
        idAmbito,
        this.statoDebitorio,
        undefined,
        anno
      );

    // Creo l'oggetto per la chiamata
    const calls: IDatiUsoLeggeReq = { usi, anni };

    // Genero la lista degli anni nella select 'anni'
    return forkJoin(calls).pipe(
      tap((res: IDatiUsoLeggeRes) => {
        const { anni, usi } = res;
        // Asegno la lista degli usi di legge
        this.listaUsiDiLegge = usi;
        // Assegno la lista degli anni generata
        this.listaAnni = anni;
        // Ritorno la response
        return res;
      })
    );
  }

  /**
   * Funzione che setta l'annualità di default subito dopo lo scarico della lista degli anni.
   * Se non ho un valore presente nell'annualità, prende la data maggiore.
   * @param annoAnnualita IRiscaAnnoSelect che definisce l'oggetto dell'annualità di default.
   */
  private initAnnualitaDefault(annoAnnualita?: IRiscaAnnoSelect) {
    // Prendo l'annualità presente
    const annualita = annoAnnualita ?? this.annoAnnualita;
    // Definisco la proprietà per l'anno da settare
    let anno = annualita ? annualita.anno : undefined;

    // Verifico se non c'è nun anno di default
    if (!anno) {
      // Non c'è, estraggo l'anno non disabilitato più recente/futuro
      const anniSel: IRiscaAnnoSelect[] = this.listaAnni ?? [];
      // Estraggo solo gli anni d'interesse
      const anniOk: number[] = anniSel
        // Filtro recuperdando solo gli anni abilitati
        .filter((aSel: any | IRiscaAnnoSelect) => !aSel.__disabled)
        // Estraggo solo gli anni dagli oggetti
        .map((aOk: IRiscaAnnoSelect) => aOk.anno);

      // Recupero il valore maggiore oppure undefined
      anno = max(anniOk);
    }

    // Variabili di comodo per la gestione di select e dato nel form
    const mf = this.mainForm;
    const k = this.DTA_SD_C.ANNUALITA;
    const l = this.listaAnni;
    const v = { anno };
    const f = (a: IRiscaAnnoSelect, b: IRiscaAnnoSelect) => {
      return b.anno == a.anno;
    };
    const o: FormUpdatePropagation = { emitEvent: false };
    // Setto l'annualità massima trovata
    this._riscaUtilities.setFormValueAndSelect(mf, k, l, v, f, o);
  }

  /**
   * ############################################
   * FUNZIONI DI UTILITY GENERICHE DEL COMPONENTE
   * ############################################
   */

  /**
   * Funzione di comodo per la gestione degli errori dei servizi.
   */
  onServiziErrorAnnualita(e: RiscaServerError) {
    // Emetto l'errore tramite evento
    this._datiTecniciEvents.erroreServiziAnnualita(e);
  }

  /**
   * Funzione invocata quando l'evento dei dati tecnici viene attivato.
   * @param praticaDTVo PraticaDTVo con i dati tecnici aggiornati.
   * @override
   */
  datiTecniciUpdated(praticaDTVo: PraticaDTVo) {
    // Assegno localmente i dati aggiornati
    this.praticaDT = praticaDTVo;
    // Richiamo il processo di aggiornamento dei dati
    this.initConfigs();
  }

  /**
   * #############################
   * FUNZIONI DI GESTIONE DEL FORM
   * #############################
   */

  /**
   * Funzione che permette il recupero del raw value del main form del child.
   * @returns DTAmbienteAnnualitaSD contente le informazioni del form in modalità raw value.
   */
  getMainFormRawValue(): DTAmbienteASDData {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      // Niente informazione
      return undefined;
    }

    // Recupero il dato dal form
    const dtt: IDatiTecniciTributi = this.mainForm.getRawValue();

    // Converto l'oggetto del form in un oggetto annualità
    const annualita: AnnualitaSDVo =
      this._dttSDAConverter.convertDTTributiASDToAnnualitaSDVo(dtt);

    // Aggiungo l'idComponenteDt alle informazioni del form
    annualita.id_componente_dt = this.idComponenteDt;
    annualita.canone_annuo = 0;

    // Creo l'oggetto da ritornare
    const dtaASD: DTAmbienteASDData = { annualita };
    // Ritorno l'oggetto generato
    return dtaASD;
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
    // Resetto il flag di submit
    this.mainFormSubmitted = false;

    // Verifico la modalita
    if (this.inserimento) {
      // Richiamo il reset per l'inserimento
      this.onFormResetIns();
      // #
    } else if (this.modifica) {
      // Richiamo il reset per l'inserimento
      this.onFormResetMod();
      // #
    }
  }

  /**
   * Funzione di supporto invocata quando è necessario resettare le informazioni del form quando si è in modalità: inserimento.
   */
  private onFormResetIns() {
    /** RICHIAMO IN ORDINE LE FUNZIONI DI PER RESETTARE I DATI */

    // Resetto i form
    this.mainForm?.reset();

    // Setto l'annualità di default
    this.initAnnualitaDefault();
    // Rilancio la configurazione dati
    this.initConfigs();
  }

  /**
   * Funzione di supporto invocata quando è necessario resettare le informazioni del form quando si è in modalità: modifica.
   */
  private onFormResetMod() {
    /** RICHIAMO IN ORDINE LE FUNZIONI DI PER RESETTARE I DATI */

    // Resetto i form
    this.mainForm?.reset();

    // Setto l'annualità di default
    this.initAnnualitaDefault();
    // Rilancio la configurazione dati
    this.initConfigs();
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo che restituisce l'anno annualità dalla form.
   * @returns IRiscaAnnoSelect con il valore del campo.
   */
  get annoAnnualita(): IRiscaAnnoSelect {
    // Variabili di comodo
    const f = this.mainForm;
    const k = this.DTA_SD_C.ANNUALITA;

    // Recupero il dato dal form control
    let anno: IRiscaAnnoSelect;
    anno = this._riscaUtilities.getFormValue(f, k);

    // Ritorno il valore del campo
    return anno;
  }
}
