import { Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { cloneDeep } from 'lodash';
import {
  IJsonRegolaRangeVo,
  JsonRegolaRangeVo,
} from '../../../../core/commons/vo/json-regola-range-vo';
import { JsonRegolaSogliaVo } from '../../../../core/commons/vo/json-regola-soglia-vo';
import { JsonRegolaVo } from '../../../../core/commons/vo/json-regola-vo';
import { RegolaUsoVo } from '../../../../core/commons/vo/regola-uso-vo';
import { UsoLeggeVo } from '../../../../core/commons/vo/uso-legge-vo';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../core/services/user.service';
import {
  RegoleUsoRangeTable,
  RegoleUsoRangeTableConfigs,
} from '../../../../shared/classes/risca-table/regole-uso-range/regole-uso-range.table';
import { RegoleUsoRangeTableConsts } from '../../../../shared/classes/risca-table/regole-uso-range/utilities/regole-uso-range.consts';
import { RiscaFormParentComponent } from '../../../../shared/components/risca/risca-form-parent/risca-form-parent.component';
import { RiscaTableDataConfig } from '../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFormBuilderService } from '../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { AppActions, RiscaServerError } from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { RegolaCanoneMinimoFormComponent } from '../../components/regola-canone-minimo-form/regola-canone-minimo-form.component';
import { IRegolaCanoneMinimoForm } from '../../components/regola-canone-minimo-form/utilities/regola-canone-minimo-form.interfaces';
import { RegolaRangeFormComponent } from '../../components/regola-range-form/regola-range-form.component';
import { IRegolaRangeForm } from '../../components/regola-range-form/utilities/regola-range-form.interfaces';
import { RegolaSogliaFormComponent } from '../../components/regola-soglia-form/regola-soglia-form.component';
import { IRegolaSogliaForm } from '../../components/regola-soglia-form/utilities/regola-soglia-form.interfaces';
import { JsonRegolaFormComponent } from '../../components/regola-uso-form/json-regola-form.component';
import { IJsonRegolaForm } from '../../components/regola-uso-form/utilities/json-regola-form.interfaces';
import { ConfigurazioniUtilitiesService } from '../../services/configurazioni/configurazioni-utilities.service';
import { ConfigurazioniService } from '../../services/configurazioni/configurazioni.service';
import { DettaglioRegolaUsoModalService } from '../../services/dettaglio-regola-uso-modal/dettaglio-regola-uso-modal.service';
import { DettaglioRegolaUsoModalConsts } from './utilities/dettaglio-regola-uso-modal.consts';
import {
  IDettaglioRegolaUsoModal,
  IDettaglioRegolaUsoModalConfirm,
  IDRUDatiModalita,
} from './utilities/dettaglio-regola-uso-modal.interfaces';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { IAlertConfigsFromCode } from '../../../../shared/utilities/interfaces/risca/risca-alert/risca-alert.interface';

@Component({
  selector: 'dettaglio-regola-uso-modal',
  templateUrl: './dettaglio-regola-uso-modal.component.html',
  styleUrls: ['./dettaglio-regola-uso-modal.component.scss'],
})
export class DettaglioRegolaUsoModalComponent
  extends RiscaFormParentComponent
  implements OnInit, OnDestroy
{
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** DettaglioRegolaUsoModalConsts contenente le costanti per il componente attuale. */
  DRUM_C = new DettaglioRegolaUsoModalConsts();
  /** RegoleUsoRangeTableConsts contenente le costanti per la tabella omonima. */
  RURT_C = new RegoleUsoRangeTableConsts();
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /** RiscaNotifyCodes con i valori dell'enumeratore per usarlo nel template HTML. */
  riscaNotifyCodes = RiscaNotifyCodes;
  /** AppActions con i valori dell'enumeratore per usarlo nel template HTML */
  appActions = AppActions;

  /** RegolaCanoneMinimoFormComponent con la referenza al componente che gestisce i dati tramite form. */
  @ViewChild('regolaCanoneMinimoFormComp')
  regolaCanoneMinimoFormComp: RegolaCanoneMinimoFormComponent;
  /** RegolaSogliaFormComponent con la referenza al componente che gestisce i dati tramite form. */
  @ViewChild('regolaSogliaFormComp')
  regolaSogliaFormComp: RegolaSogliaFormComponent;
  /** RegolaRangeFormComponent con la referenza al componente che gestisce i dati tramite form. */
  @ViewChild('jsonRegolaFormComp')
  jsonRegolaFormComp: JsonRegolaFormComponent;
  /** RegolaRangeFormComponent con la referenza al componente che gestisce i dati tramite form. */
  @ViewChild('regolaRangeFormComp')
  regolaRangeFormComp: RegolaRangeFormComponent;

  /** ICreaRegolaUsoModal che definisce i dati di configurazione per la modale. */
  @Input() dataModal: IDettaglioRegolaUsoModal;

  /** Boolean che definisce l'abilitazione di tutte le componenti della pagina. */
  AEA_pagina_canoni_disabled: boolean;

  /** RegolaUsoVo con le informazioni da gestire per l'aggiornamento dati. */
  regolaUsoUpd: RegolaUsoVo;

  /** Boolean che definisce lo stato dell'accordion per gli usi effettivi. */
  usiEffettiviApriChiudi = false;
  /** Boolean che definisce lo stato dell'accordion per la legenda. */
  legendaApriChiudi = false;

  /** UsoLeggeVo[] con la lista degli usi di legge effettivi per l'uso in modale. */
  usiEffettivi: UsoLeggeVo[] = [];

  /** RegoleUsoRangeTable con le configurazioni della tabella dati per le regole gestite tramite range. */
  regoleUsoRangeTable: RegoleUsoRangeTable;

  /**
   * Costruttore.
   */
  constructor(
    private _accessoElementiApp: AccessoElementiAppService,
    public activeModal: NgbActiveModal,
    private _configurazioni: ConfigurazioniService,
    private _configurazioniUtilities: ConfigurazioniUtilitiesService,
    private _dettaglioRegolaUsoModal: DettaglioRegolaUsoModalService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    private _riscaFormBuilderService: RiscaFormBuilderService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaMessages: RiscaMessagesService,
    private _user: UserService
  ) {
    super(
      logger,
      navigationHelper,
      riscaAlert,
      riscaFormSubmitHandler,
      riscaMessages
    );

    // Lancio il setup del componente
    this.setupComponente();
  }

  ngOnInit() {
    // Lancio l'init del componente
    this.initComponente();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * #################
   * FUNZIONI DI SETUP
   * #################
   */

  /**
   * Funzione di setup per le informazioni del componente.
   */
  private setupComponente() {
    // Richiamo l'inizializzazione delle chiavi per l'accesso agli elementi
    this.setupAEADisabled();
  }

  /**
   * Funzione di setup per l'inizializzazione dei flag di abilitazione agli elementi.
   */
  private setupAEADisabled() {
    // Recupero le chiavi per la verifica sulla disabilitazione della nav
    const PC_KEY = this.AEAK_C.PAGINA_CANONI;
    // Definisco disabled per le nav
    this.AEA_pagina_canoni_disabled =
      this._accessoElementiApp.isAEADisabled(PC_KEY);
  }

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  /**
   * Funzione di init per le informazioni del componente.
   */
  private initComponente() {
    // Lancio la funzione per definire l'oggetto sulla quale definire gli aggiornamenti
    this.initRegolaUsoUpd();
    // Lancio la funzione per definire la tabella per le regole con range
    this.initRegoleRange();
    // Lancio la funzione di caricamento dei dati asincroni
    this.initAsyncData();
  }

  /**
   * Funzione di init che crea una copia della regola uso in input, come base per gli aggiornamenti.
   */
  private initRegolaUsoUpd() {
    // Tento di clonare la regola uso in input
    this.regolaUsoUpd = this.regolaUso?.clone();
  }

  /**
   * Funzione di init che verifica e genera una tabella per la gestione delle regole: ranges.
   */
  private initRegoleRange() {
    // Verifico se siamo nella modalità specifica
    if (this.modalitaRanges) {
      // Recupero dall'oggetto della regola i ranges
      let jsonRegolaRanges: JsonRegolaRangeVo[];
      jsonRegolaRanges = this.jsonRegola?.rangesFE;
      // Recupero il flag di abilitazione elementi app
      const AEA_pagina_canoni_disabled = this.AEA_pagina_canoni_disabled;

      // Definisco l'oggetto di configurazione per la tabella
      let c: RegoleUsoRangeTableConfigs;
      c = { jsonRegolaRanges, AEA_pagina_canoni_disabled };
      let riscaFormBuilder = this._riscaFormBuilderService;

      // Istanzio la tabella
      this.regoleUsoRangeTable = new RegoleUsoRangeTable(c, riscaFormBuilder);
    }
  }

  /**
   * Funzione di setup che definisce le logiche di scarico per tutte le informazioni asincrone del componente.
   */
  private initAsyncData() {
    // Recupero le informazioni di configurazione per lo scarico dati
    const regolaUso: RegolaUsoVo = this.regolaUso;
    const uso: UsoLeggeVo = regolaUso?.tipo_uso;
    const idUso: number = uso?.id_tipo_uso;

    // Lancio le chiamate per lo scarico dati
    this._dettaglioRegolaUsoModal.getUsiEffettivi(idUso).subscribe({
      next: (usiEffettivi: UsoLeggeVo[]) => {
        // Richiamo la funzione di smistamento delle informazioni
        this.initListaUsiEffettivi(usiEffettivi);
        // #
      },
      error: (e: RiscaServerError) => {
        // Gestisco gli errori
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione di init che gestisce le informazioni scaricate per: usi effettivi.
   * @param usiEffettivi UsoLeggeVo[] con le informazioni per gli usi effettivi.
   */
  private initListaUsiEffettivi(usiEffettivi: UsoLeggeVo[]) {
    // Verifico l'input
    if (!usiEffettivi || usiEffettivi.length == 0) {
      // // Imposto la modale come chiusa
      // this.usiEffettiviApriChiudi = false;
      // Non ci sono dati, blocco il flusso
      return;
    }
    // Assegno localmente la lista
    this.usiEffettivi = usiEffettivi;
    // // Imposto la modale come aperta
    // this.usiEffettiviApriChiudi = true;
  }

  /**
   * ###############################################
   * FUNZIONE PER LA GESTIONE DEI PULSANTI IN PAGINA
   * ###############################################
   */

  /**
   * Funzione agganciata al pulsante di annulla.
   * Richiede il reset delle informazioni delle form.
   */
  annullaModificaRegolaUso() {
    // Resetto l'alert
    this.resetAlertConfigs();

    // Riassegno i valori iniziali alla variabile di modifica
    this.initRegolaUsoUpd();
    // Lancio il reset del form, tramite referenza del componente
    this.resetRegolaUsoFormComp();
  }

  /**
   * Funzione di supporto che, a seconda della modalità dati della modale, gestisce il reset del form.
   */
  private resetRegolaUsoFormComp() {
    // Verifico la modalità e gestisco il reset specifico
    if (this.modalitaCanoneMinimo) {
      // Lancio la funzione di reset
      this.regolaCanoneMinimoFormComp?.onFormReset();
      // #
    } else if (this.modalitaSoglia) {
      // Lancio la funzione di reset
      this.regolaSogliaFormComp?.onFormReset();
      // #
    } else if (this.modalitaRanges) {
      // Lancio la funzione di reset
      this.jsonRegolaFormComp?.onFormReset();
      this.regolaRangeFormComp?.onFormReset();
      // Lancio la funzione per re-istanziare la tabella range
      this.initRegoleRange();
      // #
    }
  }

  /**
   * Funzione agganciata al pulsante di conferma.
   * Richiede il submit/salvataggio della regola uso.
   */
  salvaRegolaUso() {
    // Resetto l'alert
    this.resetAlertConfigs();
    // Lancio il submit del form, tramite referenza del componente
    this.submitRegolaUsoFormComp();
  }

  /**
   * Funzione di supporto che, a seconda della modalità dati della modale, gestisce il submit del form.
   */
  private submitRegolaUsoFormComp() {
    // Verifico la modalità e gestisco il submit specifico
    if (this.modalitaCanoneMinimo) {
      // Lancio la funzione di submit
      this.regolaCanoneMinimoFormComp?.onFormSubmit();
      // #
    } else if (this.modalitaSoglia) {
      // Lancio la funzione di submit
      this.regolaSogliaFormComp?.onFormSubmit();
      // #
    } else if (this.modalitaRanges) {
      // Lancio la funzione di submit
      this.jsonRegolaFormComp?.onFormSubmit();
      // #
    }
  }

  /**
   * Funzione che gestisce le logiche di submit per le informazioni gestite come: regole range.
   */
  private submitRegoleUsoRange() {
    // Recupero le informazioni dalla tabella
    let ranges: JsonRegolaRangeVo[];
    ranges = this.regoleUsoRangeTable.getDataSource();

    // Lancio la funzione di verifica dei dati
    let error: RiscaNotifyCodes;
    error = this._configurazioniUtilities.validJsonRegolaRanges(ranges);
    // Verifico che la validazione sui ranges sia andata a buon fine
    if (error) {
      // Sì è presentato un errore, gestisco la segnalazione
      const c: IAlertConfigsFromCode = { code: error };
      this.alertConfigs = this._riscaAlert.createAlertFromMsgCode(c);
      // Interrompo il flusso
      return;
      // #
    }

    // Validazione ok, recupero l'oggetto d'aggiornare
    const regolaUsoUpd: RegolaUsoVo = this.regolaUsoUpd;
    // Recupero l'oggetto range che definisce i dati per il minimo principale
    let rangePrincipale: JsonRegolaRangeVo;
    rangePrincipale = regolaUsoUpd.json_regola_obj.rangeMinimoPrincipale;
    // Aggiorno i ranges con i dati della tabella
    regolaUsoUpd.json_regola_obj.ranges = ranges;

    // Verifico se è stato definito un range con minimo principale
    if (rangePrincipale) {
      // Esiste, lo aggiungo alla lista che è stata reimpostata
      regolaUsoUpd.json_regola_obj.ranges?.push(rangePrincipale);
    }

    // Lancio la funzione di salvataggio
    this.aggiornaRegola(regolaUsoUpd);
  }

  /**
   * ######################################
   * FUNZIONI PER LA GESTIONE DEL FORM DATI
   * ######################################
   */

  /**
   * Funzione collegata al submit della form dati per il form: canone minimo.
   * Verrà gestita la consolidazione ed il salvataggio dei dati della regola.
   * @param data IRegolaCanoneMinimoForm con le informazioni submittate dall'utente.
   */
  onRegolaCanoneMinimoSubmit(data: IRegolaCanoneMinimoForm) {
    // Verifico l'input
    if (!data) {
      // Non è stata ritornata nessuna configurazione
      return;
    }

    // Recupero la regola per l'aggiornamento dati
    const regolaUsoUpd: RegolaUsoVo = this.regolaUsoUpd;
    // Estraggo dai dati del form le informazioni d'aggiornare
    const canoneMinimo: number = data.canoneMinimo;

    // Aggiorno le informazioni sull'oggetto
    regolaUsoUpd.json_regola_obj.canone_minimo = canoneMinimo;

    // Lancio l'aggiornamento della regola
    this.aggiornaRegola(regolaUsoUpd);
  }

  /**
   * Funzione collegata al submit della form dati per il form: soglia.
   * Verrà gestita la consolidazione ed il salvataggio dei dati della regola.
   * @param data IRegolaSogliaForm con le informazioni submittate dall'utente.
   */
  onRegolaSogliaSubmit(data: IRegolaSogliaForm) {
    // Verifico l'input
    if (!data) {
      // Non è stata ritornata nessuna configurazione
      return;
    }

    // Recupero la regola per l'aggiornamento dati
    const regolaUsoUpd: RegolaUsoVo = this.regolaUsoUpd;
    // Estraggo dai dati del form le informazioni d'aggiornare
    const soglia: number = data.soglia;
    const minimoInferiore: number = data.minimoInferiore;
    const minimoSuperiore: number = data.minimoSuperiore;

    // Aggiorno le informazioni sull'oggetto
    regolaUsoUpd.json_regola_obj.soglia.soglia = soglia;
    regolaUsoUpd.json_regola_obj.soglia.canone_minimo_soglia_inf =
      minimoInferiore;
    regolaUsoUpd.json_regola_obj.soglia.canone_minimo_soglia_sup =
      minimoSuperiore;

    // Lancio l'aggiornamento della regola
    this.aggiornaRegola(regolaUsoUpd);
  }

  /**
   * Funzione collegata al submit della form dati per il form: json regola.
   * Verrà gestita la consolidazione ed il salvataggio dei dati della regola.
   * @param data IJsonRegolaForm con le informazioni submittate dall'utente.
   */
  onJsonRegolaSubmit(data: IJsonRegolaForm) {
    // Verifico l'input
    if (!data) {
      // Non è stata ritornata nessuna configurazione
      return;
    }

    // Recupero la regola per l'aggiornamento dati
    const regolaUsoUpd: RegolaUsoVo = this.regolaUsoUpd;
    // Estraggo dai dati del form le informazioni d'aggiornare
    const minimoPrincipale: number = data.minimoPrincipale;
    // Aggiorno le informazioni sull'oggetto
    regolaUsoUpd.json_regola_obj.minimoPrincipale = minimoPrincipale;

    // Lancio l'aggiornamento della regola
    this.submitRegoleUsoRange();
  }

  /**
   * Funzione collegata al submit della form dati per il form: ranges.
   * A differenza delle altre modalità, il submit form aggiunge un nuovo range alla tabella di ranges.
   * @param data IRegolaRangeForm con le informazioni submittate dall'utente.
   */
  onRegolaRangeSubmit(data: IRegolaRangeForm) {
    // Verifico l'input
    if (!data) {
      // Non è stata ritornata nessuna configurazione
      return;
    }

    // Definisco i parametri per un oggetto range, dai dati del form
    const nuovoRangeParams: IJsonRegolaRangeVo = {
      canone_minimo: data.canoneMinimo,
      soglia_min: data.valore1,
      soglia_max: data.valore2,
    };
    // Definisco un nuovo oggetto ranges
    const nuovoRange = new JsonRegolaRangeVo(nuovoRangeParams);
    // Aggiungo una nuova riga alla tabella di ranges
    this.regoleUsoRangeTable.addElement(nuovoRange);

    // Effettuo il reset dati della form dati
    this.regolaRangeFormComp?.onFormReset(true);
  }

  /**
   * Funzione adibita al salvataggio delle informazioni per la regola presente nella modale.
   * @param regola RegolaUsoVo con i dati d'aggiornare per la regola.
   */
  private aggiornaRegola(regola: RegolaUsoVo) {
    // Verifico l'input
    if (!regola) {
      // Nessun input
      return;
    }

    // Lancio la funzione di aggiornamento della regola
    this._configurazioni.putRegola(regola).subscribe({
      next: (regolaUpd: RegolaUsoVo) => {
        // Lancio la funzione per preparare l'oggetto alla tabella
        regolaUpd?.prepareRegolaUsoVoToTable();
        // Regola aggiornata, ritorno le informazioni al chiamate
        this.onRegolaAggiornata(regolaUpd);
        // #
      },
      error: (e: RiscaServerError) => {
        // Gestisco l'errore
        this.onServiziError(e);
      },
    });
  }

  /**
   * Funzione invocata nel momento in cui il dato per la regola uso è stato aggiornato.
   * @param regolaUso RegolaUsoVo con le informazioni aggiornate dal servizio.
   */
  private onRegolaAggiornata(regolaUso: RegolaUsoVo) {
    // Definisco le informazioni per la gestione della chiusura della modale
    let modalConfirm: IDettaglioRegolaUsoModalConfirm;
    modalConfirm = { regolaUso };

    // Chiudo la modale
    this.modalConfirm(modalConfirm);
  }

  /**
   * ######################
   * GESTIONE TABELLA RANGE
   * ######################
   */

  /**
   * Funzione agganciata all'evento: cancellazione riga; per la tabella: range.
   * @param row RiscaTableDataConfig<JsonRegolaRangeVo> con riga cancellata.
   */
  onDeleteRange(row: RiscaTableDataConfig<JsonRegolaRangeVo>) {
    // Verifico l'input
    if (!row || !this.regoleUsoRangeTable) {
      // Manca la configurazione minima
      return;
    }

    // Effettuo la remove dalla tabella
    this.regoleUsoRangeTable.removeRow(row);
    // Vado a recuperare i dati dalla tabella
    let jsonRanges: JsonRegolaRangeVo[];
    jsonRanges = this.regoleUsoRangeTable.getDataSource();

    // Aggiorno all'interno dell'oggetto per la modifica
    this.regolaUsoUpd.json_regola_obj.ranges = jsonRanges;
  }

  /**
   * ##################
   * GESTIONE ACCORDION
   * ##################
   */

  /**
   * Funzione di toggle per la visualizzazione dell'accordion: "Usi effettivi".
   * @param stato boolean che definisce il nuovo stato dello switch accordion.
   */
  toggleUsiEffettivi(stato: boolean) {
    // Effettuo il toggle per la visualizzazione
    this.usiEffettiviApriChiudi = !this.usiEffettiviApriChiudi;
  }

  /**
   * Funzione di toggle per la visualizzazione dell'accordion: "Legenda".
   * @param stato boolean che definisce il nuovo stato dello switch accordion.
   */
  toggleLegenda(stato: boolean) {
    // Effettuo il toggle per la visualizzazione
    this.legendaApriChiudi = !this.legendaApriChiudi;
  }

  /**
   * ##################################
   * FUNZIONI PER GESTIONE DELLA MODALE
   * ##################################
   */

  /**
   * Funzione che effettua la dismiss (e quindi la reject) della modale.
   */
  modalDismiss() {
    // Dismiss della modale
    this.activeModal.dismiss();
  }

  /**
   * Funzione che effettua la close (e quindi la resolved) della modale.
   * @param payload any con i dati da restitutire al chiamate.
   */
  modalConfirm(payload: IDettaglioRegolaUsoModalConfirm) {
    // Close della modale
    this.activeModal.close(payload);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter per l'id dell'ambito.
   * @returns number con l'id.
   */
  get idAmbito(): number {
    // Ritorno l'id ambito
    return this._user.idAmbito;
  }

  /**
   * Getter per l'informazione dall'oggetto di configurazione.
   * @returns RegolaUsoVo con le informazioni di configurazione.
   */
  get regolaUso(): RegolaUsoVo {
    // Recupero le informazioni dalla configurazione
    return cloneDeep(this.dataModal?.regola);
  }

  /**
   * Getter per l'informazione omonima.
   * @returns string con la descrizione dell'uso.
   */
  get usoPrincipale(): string {
    // Recupero la regola
    const regola: RegolaUsoVo = this.regolaUso;
    // Tento di recuperare e ritornare la descrizione
    return regola?.tipo_uso?.des_tipo_uso;
  }

  /**
   * Getter di check per la condizione di esistenza della struttura dati omonima.
   * @returns boolean con il risultato del check.
   */
  get checkUsiEffettivi(): boolean {
    // Verifico e ritorno che esista il dato
    return this.usiEffettivi?.length > 0;
  }

  /**
   * Getter che ritorna la struttura del json regola.
   * @returns JsonRegolaVo con i dati per il json regola.
   */
  get jsonRegola(): JsonRegolaVo {
    // Cerco di ritornare le informazioni per json_regola_obj
    return this.regolaUso?.json_regola_obj;
  }

  /**
   * Getter che ritorna la proprietà omonima dal json regola.
   * @returns number con l'informazione dalla json regola.
   */
  get canoneMinimo(): number {
    // Ritorno l'informazione
    return this.jsonRegola?.canone_minimo;
  }

  /**
   * Getter che ritorna la proprietà omonima dal json regola.
   * @returns JsonRegolaSogliaVo con l'informazione dalla json regola.
   */
  get soglia(): JsonRegolaSogliaVo {
    // Ritorno l'informazione
    return this.jsonRegola?.soglia;
  }

  /**
   * Getter che ritorna la proprietà omonima dal json regola.
   * @returns JsonRegolaRangeVo[] con le informazioni dalla json regola.
   */
  get ranges(): JsonRegolaRangeVo[] {
    // Ritorno l'informazione
    return this.jsonRegola?.ranges;
  }

  /**
   * Getter che ritorna il tipo le informazioni per la gestione della modalità.
   * @returns IDRUDatiModalita con i dati per definire la modalità.
   */
  get datiModalita(): IDRUDatiModalita {
    // Recupero i dati per gestire la modalità
    let canoneMinimo: number;
    canoneMinimo = this.canoneMinimo;
    let soglia: JsonRegolaSogliaVo;
    soglia = this.soglia;
    let ranges: JsonRegolaRangeVo[];
    ranges = this.ranges;
    // Ritorno l'oggetto composto
    return { canoneMinimo, soglia, ranges };
  }

  /**
   * Getter che ritorna il tipo di modalità di gestione per la modale.
   * La modalità è: canone minimo.
   * @returns boolean con il flag per attivazione della modalità.
   */
  get modalitaCanoneMinimo(): boolean {
    // Recupero i dati per gestire la modalità
    let datiModalita: IDRUDatiModalita = this.datiModalita;

    // Questa modalità è attiva se esiste solo la proprietà "canone_minimo"
    let canoneMinimo: number = datiModalita.canoneMinimo;
    let soglia: JsonRegolaSogliaVo = datiModalita.soglia;
    let ranges: JsonRegolaRangeVo[] = datiModalita.ranges;

    // Definisco le informazioni per gestire questa modalità
    const check1: boolean = canoneMinimo != undefined;
    const check2: boolean = soglia?.soglia == undefined;
    const check3: boolean = !ranges || ranges.length == 0;

    // Ritorno l'insieme dei check modalità
    return check1 && check2 && check3;
  }

  /**
   * Getter che ritorna il tipo di modalità di gestione per la modale.
   * La modalità è: soglia.
   * @returns boolean con il flag per attivazione della modalità.
   */
  get modalitaSoglia(): boolean {
    // Recupero i dati per gestire la modalità
    let datiModalita: IDRUDatiModalita = this.datiModalita;

    // Questa modalità è attiva se esiste solo la proprietà "soglia"
    let canoneMinimo: number = datiModalita.canoneMinimo;
    let soglia: JsonRegolaSogliaVo = datiModalita.soglia;
    let ranges: JsonRegolaRangeVo[] = datiModalita.ranges;

    // Definisco le informazioni per gestire questa modalità
    const check1: boolean = canoneMinimo == undefined;
    const check2: boolean = soglia?.soglia != undefined;
    const check3: boolean = !ranges || ranges.length == 0;

    // Ritorno l'insieme dei check modalità
    return check1 && check2 && check3;
  }

  /**
   * Getter che ritorna il tipo di modalità di gestione per la modale.
   * La modalità è: ranges.
   * @returns boolean con il flag per attivazione della modalità.
   */
  get modalitaRanges(): boolean {
    // Recupero i dati per gestire la modalità
    let datiModalita: IDRUDatiModalita = this.datiModalita;

    // Questa modalità è attiva se esiste solo la proprietà "ranges"
    let canoneMinimo: number = datiModalita.canoneMinimo;
    let soglia: JsonRegolaSogliaVo = datiModalita.soglia;
    let ranges: JsonRegolaRangeVo[] = datiModalita.ranges;

    // Definisco le informazioni per gestire questa modalità
    const check1: boolean = canoneMinimo == undefined;
    const check2: boolean = soglia?.soglia == undefined;
    const check3: boolean = ranges?.length > 0;

    // Ritorno l'insieme dei check modalità
    return check1 && check2 && check3;
  }

  /**
   * Getter per la legenda generata per la regola uso.
   * @returns string con la legenda generata.
   */
  get legendaMinimi(): string {
    // Recupero l'oggetto della regola uso
    const regolaUso: RegolaUsoVo = this.regolaUso;
    // Richiamo la funzione di generazione della legenda
    return this._configurazioniUtilities.legendaMinimiRegoleUso(regolaUso);
  }

  /**
   * Getter per la definizione della label per i campi obbligatori.
   * @returns string con la label per i campi obbligatori, dipendente dalla modalità della modale.
   */
  get labelCampiObbligatori(): string {
    // Verifico la modalità della modale
    if (this.modalitaCanoneMinimo) {
      // Ritorno la label specifica
      return this.DRUM_C.LABEL_CAMPI_OBBLIGATORI_CANONE_MINIMO;
      // #
    } else if (this.modalitaSoglia) {
      // Ritorno la label specifica
      return this.DRUM_C.LABEL_CAMPI_OBBLIGATORI_SOGLIA;
      // #
    } else if (this.modalitaRanges) {
      // Ritorno la label specifica
      return this.DRUM_C.LABEL_CAMPI_OBBLIGATORI_RANGES;
      // #
    } else {
      // Ritorno la label default
      return '';
      // #
    }
  }

  /**
   * Getter che ritorna lo stile grafico per la gestione della sezione: minimo principale; per la sezione: row.
   * @returns any compatibile con la direttiva NgStyle per la gestione della grafica.
   */
  get rowMinimoPrincipale(): any {
    // Verifico e gestisco lo stile per la sezione
    return this.DRUM_C.STYLE_ROW_MINIMO_PRINCIPALE;
  }

  /**
   * Getter che effettua un check di esistenza sulla tabella omonima.
   * @returns boolean con il risultato del check.
   */
  get checkRegoleUsoRangeTable(): boolean {
    // Verifico e ritorno l'esistenza della tabella
    return this.regoleUsoRangeTable?.source?.length > 0;
  }

  /**
   * Getter che raccoglie le condizioni per disabilitare le form dati.
   * @returns boolean con l'insieme dei flag.
   */
  get disableForm(): boolean {
    // Recupero i flag di disabilitazione
    const d1: boolean = this.AEA_pagina_canoni_disabled;

    // Ritorno l'insieme delle condizioni
    return d1;
  }
}
