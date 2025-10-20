import {
  Component,
  Injector,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import { ComponenteDt } from '../../../../../../../core/commons/vo/componente-dt-vo';
import { PraticaDTVo } from '../../../../../../../core/commons/vo/pratica-vo';
import { LoggerService } from '../../../../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaFormParentAndChildComponent } from '../../../../../../../shared/components/risca/risca-form-parent-and-child/risca-form-parent-and-child.component';
import { RiscaAlertService } from '../../../../../../../shared/services/risca/risca-alert.service';
import { RiscaFormSubmitHandlerService } from '../../../../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from '../../../../../../../shared/services/risca/risca-messages.service';
import { RiscaUtilitiesService } from '../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  DynamicObjAny,
  IRiscaFormTreeParent,
  IVerifyComponeteDt,
  RiscaFormStatus,
} from '../../../../../../../shared/utilities';
import { QuadriTecniciPraticaConsts } from '../../../../../consts/quadri-tecnici/quadri-tecnici-pratica/quadri-tecnici-pratica.consts';
import { QuadriTecniciRicercaService } from '../../../services/quadri-tecnici/ricerca/quadri-tecnici-ricerca.service';
import { RicercaDatiTecniciService } from '../../../services/quadri-tecnici/ricerca/ricerca-dati-tecnici.service';
import {
  DTRicercaConfig,
  DT_RICERCA_CONFIG,
} from '../../../utilities/configs/dt-ricerca.injectiontoken';
import { DTRicerca20211001Component } from '../../ambito-ambiente/version-20211001/dt-ricerca/dt-ricerca.component';
import { RicercaDatiTecniciTributiComponent } from '../../ambito-tributi/version-20220101/ricerca-dati-tecnici-tributi/ricerca-dati-tecnici-tributi.component';

/**
 * Componente per la gestione del form dati tecnici.
 * Questo componente farà da gestore per i dati tecnici, visualizzando la pagina e gestendo gli eventi di conferma del form.
 */
@Component({
  selector: 'quadri-tecnici-ricerca',
  templateUrl: './quadri-tecnici-ricerca.component.html',
  styleUrls: ['./quadri-tecnici-ricerca.component.scss'],
})
export class QuadriTecniciRicercaComponent
  extends RiscaFormParentAndChildComponent<PraticaDTVo>
  implements OnInit, OnChanges
{
  /** Oggetto contenente le costanti utilizzate nel componente. */
  QT_C = QuadriTecniciPraticaConsts;

  /** Input per la pre-valorizzazione dei dati del componente. */
  @Input('datiTecnici') configs: string;

  /** ComponenteDt che definisce l'oggetto di configurazione per caricare il componente dei dati tecnici specifico. */
  private _componenteDt: ComponenteDt;
  /** Oggetto contenente una coppia di chiavi valore per il caricamento dei dati tecnici. */
  private _datiTecniciComponents: DynamicObjAny;
  /** Oggetto contenente la configurazione da caricare per i dati tecnici. */
  dtComponent: any;
  /** Injector utilizzato per configurare il caricamento dinamico dei dati tecnici. */
  injectorDatiTecnici: Injector;
  /** Boolean per gestire la parte di visualizzazione dei dati del componente tecnico. */
  showDt = false;

  /**
   * Costruttore
   */
  constructor(
    private _datiTecniciRicerca: RicercaDatiTecniciService,
    private _injector: Injector,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _quadriTecniciRicerca: QuadriTecniciRicercaService,
    riscaAlert: RiscaAlertService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessages: RiscaMessagesService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(
      logger,
      navigationHelper,
      riscaAlert,
      riscaFormSubmitHandler,
      riscaMessages,
      riscaUtilities
    );
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit() {
    // Lancio l'init per il caricamento del componente dei dati tecnici
    this.initComponente();
  }

  ngOnChanges(changes: SimpleChanges): void {
    // Il caricamento dei dati tecnici va fatto nell'onchange perchè quando viene inizializzato i coreconfig sono undefined.
    if (changes.configs && !changes.configs.firstChange) {
      // Carico i dati tecnici del componente
      this.initDatiTecnici();
    }
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
    // Lancio il setup per la configurazione dei possibili componenti per i dati tecnici
    this.setupComponentiDatiTecnici();
    // Effettuo il setup del servizio
    this.setupFormSubmitHandlerService();
  }

  /**
   * Funzione di setup che imposta, all'interno del componente, i possibili dati tecnici da caricare.
   */
  private setupComponentiDatiTecnici() {
    // Un vincolo imposto dal framework è quello di definire direttamente i componenti che potrebbero essere caricati
    this._datiTecniciComponents = {
      // NOTA BENE: il nome della proprietà deve essere identico al nome che viene scaricato dal server e con la referenza al componente specifico
      RicercaDatiTecniciAmbiente: DTRicerca20211001Component,
      RicercaDatiTecniciTributi: RicercaDatiTecniciTributiComponent,
    };
  }

  /**
   * Funzione di setup per il servizio RiscaFormSubmitHandlerService.
   */
  private setupFormSubmitHandlerService() {
    // Definisco la chiave per il parent
    const parent = this.QT_C.FORM_KEY_PARENT;
    // Definisco le chiavi per i figli
    const children = [this.QT_C.FORM_KEY_CHILD];

    // Richiamo il super
    this.setupFormsSubmitHandler(parent, children);
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Funzione di init del componente.
   */
  initComponente() {
    // Lancio l'init per il recupero della configurazione del componente dt da visualizzare
    this.initComponenteDt();
    // Lancio l'init per il caricamento del componente dei dati tecnici
    this.initDatiTecnici();
  }

  /**
   * Funzione di init che definisce il componente dt da caricare per la pagina della pratica.
   */
  private initComponenteDt() {
    // Sono in inserimento, recupero il componente dal servizio
    this._componenteDt = this._datiTecniciRicerca.ricercaDt;
  }

  /**
   * Funzione di controllo e di init del componente per i dati tecnici.
   */
  private initDatiTecnici() {
    // Gestisco la variabile di visualizzazione
    this.showDt = false;

    // Verifico che sia definito un componente da caricare per i dati tecnici
    const dtComponent = this.verifyAndGetComponentDt(
      this._datiTecniciComponents,
      this._componenteDt
    );

    // La configurazione esiste, estraggo il componente
    this.dtComponent = dtComponent;
    // Definisco i parametri per il componente da caricare
    let inputs = new DTRicercaConfig();
    inputs.parentFormKey = this.QT_C.FORM_KEY_PARENT;
    inputs.childFormKey = this.QT_C.FORM_KEY_CHILD;
    inputs.configs = this.configs;

    // Definisco il nome per le opzioni
    const nameCDt = `${this._componenteDt.nome_componente_dt} | ${this._componenteDt.des_componente_dt}`;
    // Definisco le optioni per il caricamento del componente
    const options = {
      providers: [{ provide: DT_RICERCA_CONFIG, useValue: inputs }],
      parent: this._injector,
      name: nameCDt,
    };
    // Creo l'oggetto injector per il caricamento del componente
    this.injectorDatiTecnici = Injector.create(options);

    // Gestisco la variabile di visualizzazione
    this.showDt = true;
  }

  /**
   * Funzione di verifica formale sulle informazioni per i dati tecnici per il componente dt.
   * Se la verifica va a buon fine, viene ritornata la referenza del componente definita nella configurazione.
   * @param componentiLocali DynamicObjAny con i dati per verifica e recupero.
   * @param componenteDt ComponenteDt con i dati per verifica e recupero.
   * @returns any che contiene la referenza del componente definito nelle configurazioni.
   */
  private verifyAndGetComponentDt(
    componentiLocali: DynamicObjAny,
    componenteDt: ComponenteDt
  ) {
    // Creo la configurazione per la gestione dati tecnici
    const config: IVerifyComponeteDt = {
      componentiLocali,
      componenteDt,
    };

    // Richiamo il servizio di utility per il controllo e recupero del componente
    return this._riscaUtilities.verifyAndGetComponentDt(config);
  }

  /**
   * ########################################
   * FUNZIONI DI FUNZIONALITA' DEL COMPONENTE
   * ########################################
   */

  /**
   * Funzione che gestisce la funzionalità di submit del form.
   * In questo caso verrà utilizzata dal componente padre per triggerare il submit dei sotto form di questo componente.
   * @override
   */
  onFormSubmit() {
    // Emetto l'evento di submit tramite servizio
    this._quadriTecniciRicerca.onFormSubmit$.next(true);
  }

  /**
   * Funzione che gestisce la funzionalità di reset del form.
   * In questo caso verrà utilizzata dal componente padre per triggerare il reset dei sotto form di questo componente.
   * @override
   */
  onFormReset() {
    // Emetto l'evento di reset tramite servizio
    this._quadriTecniciRicerca.onFormReset$.next(true);
  }

  /**
   * Funzione richiamata nel momento in cui viene emesso l'evento: onFormsValid.
   * In questo caso la funzionalità sarà di semplice passacarte.
   * Verranno recuperati i dati e saranno "inviati" al componente padre.
   * @param formsValid IRiscaFormTreeParent con i dati validi.
   * @override
   */
  formsValid(formsValid: IRiscaFormTreeParent) {
    // Recupero la mappa dati dei componenti
    const mapData = this.getRiscaFormTreedData(formsValid);
    // Recupero i dati tramite chiavi
    const datiTecnici = mapData.get(this.QT_C.FORM_KEY_CHILD);

    // Emetto l'evento con i dati del form
    this.onFormSubmit$.emit(datiTecnici);
    // Lancio la funzione per l'emissione dell'evento gestito tramite servizio
    this.formSubmitHandler(datiTecnici, RiscaFormStatus.valid);
  }

  /**
   * Funzione richiamata nel momento in cui viene emesso l'evento: onFormsInvalid.
   * In questo caso la funzionalità sarà di semplice passacarte.
   * Verranno recuperati i messaggi d'errore e saranno "inviati" al componente padre.
   * @param formsInvalid IRiscaFormTreeParent con i dati invalidi.
   * @override
   */
  formsInvalid(formsInvalid: IRiscaFormTreeParent) {
    // Definisco un array per i messaggi di errore
    let messaggi = [];
    // Genero una mappa con gli errori da visualizzare
    const mapErr = this.getRiscaFormTreedData(formsInvalid);

    // Ciclo la mappa e concateno i messaggi d'errore
    for (let messForm of mapErr.values()) {
      // Verifico che esista un valore per la chiave
      const messFormCheck = messForm ?? [];
      // Concateno gli array
      messaggi = messaggi.concat(messFormCheck);
    }

    // Emetto l'evento comunicando i messaggi
    this.onFormErrors$.emit(messaggi);
    // Lancio la funzione per l'emissione dell'evento gestito tramite servizio
    this.formSubmitHandler(messaggi, RiscaFormStatus.invalid);
  }
}
