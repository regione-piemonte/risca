import { RiscaMessagesService } from './../../../../shared/services/risca/risca-messages.service';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { cloneDeep } from 'lodash';
import { ElaborazioneVo } from '../../../../core/commons/vo/elaborazione-vo';
import { UserService } from '../../../../core/services/user.service';
import { FormHelperClass } from '../../../../shared/classes/form/form-helper.class';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFormBuilderService } from '../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaButtonConfig } from '../../../../shared/utilities';
import { NPModalitaModale } from '../../component/bollettini/utilities/nuova-prenotazione.enums';
import { BollettiniModalConsts } from '../../consts/bollettini-modal/bollettini-modal.consts';
import { IBollettiniModalForm } from '../../interfaces/bollettini/bollettini.interfaces';
import { BollettiniModalConverterService } from '../../service/bollettini-modal/bollettini-modal-converter.service';
import { BollettiniModalService } from '../../service/bollettini-modal/bollettini-modal.service';
import {
  BMFieldsConfigClass,
  IBMFieldsConfigClass,
} from './utilities/bollettini-modal.fields-configs';
import { IBollettiniModalDataConfigs } from './utilities/bollettini-modal.interfaces';

@Component({
  selector: 'bollettini-modal',
  templateUrl: './bollettini-modal.component.html',
  styleUrls: ['./bollettini-modal.component.scss'],
})
export class BollettiniModalComponent<T>
  extends FormHelperClass<T>
  implements OnInit
{
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts()
  /** Oggetto contenente le costanti per il componente attuale. */
  BM_C = new BollettiniModalConsts();

  /** RiscaButtonConfig che definisce la struttura di default del pulsante: ANNULLA. */
  BTN_ANNULLA: RiscaButtonConfig = { label: this.BM_C.LABEL_ANNULLA };
  /** RiscaButtonConfig che definisce la struttura di default del pulsante: CONFERMA. */
  BTN_CONFERMA: RiscaButtonConfig = { label: this.BM_C.LABEL_CONFERMA };

  /** Oggetto contenente i parametri per la modal. */
  @Input() dataModal: IBollettiniModalDataConfigs;

  /** Classe BMFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: BMFieldsConfigClass;

  /**
   * Costruttore.
   */
  constructor(
    public activeModal: NgbActiveModal,
    protected _bmConverter: BollettiniModalConverterService,
    protected _bollettiniModal: BollettiniModalService,
    protected _formBuilder: FormBuilder,
    riscaAlert: RiscaAlertService,
    protected _riscaFormBuilder: RiscaFormBuilderService,
    riscaMessages: RiscaMessagesService,
    riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {
    // Richiamo il super
    super(riscaAlert, riscaMessages, riscaUtilities);

    // Lancio il setup del componente
    this.setupComponenteBM();
  }

  ngOnInit() {
    // Init del componente
    this.initComponenteBM();
  }

  /**
   * #################
   * FUNZIONI DI SETUP
   * #################
   */

  /**
   * Funzione di setup per le informazioni del componente.
   */
  private setupComponenteBM() {
    // Lancio il setup degli errori del form
    this.setupMainFormErrors();
    // Lancio il setup delle costanti del componente
    this.setupFormInputs();
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  protected setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [];
  }

  /**
   * Funzione di setup delle inputs form del componente.
   */
  protected setupFormInputs() {
    // Definisco la configurazioni per le input
    const iConfigs: IBMFieldsConfigClass = {
      riscaFormBuilder: this._riscaFormBuilder,
    };
    // Istanzio la classe per le configurazioni
    this.formInputs = new BMFieldsConfigClass(iConfigs);
  }

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  /**
   * Funzione di setup per le informazioni del componente.
   */
  protected initComponenteBM() {
    // Lancio la funzione di init del main form
    this.initMainForm();
    // Lancio la funzione di compilazione automatica dei campi
    this.initMainFormData(this.dataModal);

    // Lancio la funzione che chiude il giro di init del componente
    this.onInitComponenteBMComplete(this.dataModal);
  }

  /**
   * Funzione pensata per essere overidata dai componenti che ereditano questa classe.
   * La funzione deve prevedere l'inizializzazione della variabile mainForm per la gestione del form d'inserimento.
   */
  protected initMainForm() {
    // Inizializzo la form
    this.mainForm = this._formBuilder.group(
      {
        tipoElaborazione: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
      },
      // Validatori campi incrociati
      {
        validators: [],
      }
    );
  }

  /**
   * Funzione pensata per essere overidata dai componenti che ereditano questa classe.
   * La funzione lancerà le logiche iniziali di set dei dati per il mainForm.
   * @param dataModal IBollettiniModalConfigs con i dati di configurazione della modale.
   */
  protected initMainFormData(dataModal: IBollettiniModalDataConfigs) {
    // Lancio il set del tipo elaborazione
    this.setTipoElaborazioneForm();
    // Lancio l'init a con la gestione a seconda della modalità
    this.initMainFormDataByModalita(dataModal);
  }

  /**
   * Funzione pensata per essere overidata dai componenti che ereditano questa classe.
   * La funzione lancerà le logiche iniziali di set dei dati per il mainForm in base alla modalità.
   * @param dataModal IBollettiniModalConfigs con i dati di configurazione della modale.
   */
  protected initMainFormDataByModalita(dataModal: IBollettiniModalDataConfigs) {
    // Verifico se la modalità della modale è quella di conferma
    if (this.isModalitaConferma) {
      // La modalità conferma deve pre-valorizzare le informazioni del form
      this.initMFDConferma(dataModal);
      // #
    } else if (this.isModalitaEmissione) {
      // La modalità emissione deve pre-valorizzare le informazioni del form
      this.initMFDEmissione(dataModal);
      // #
    }
  }

  /**
   * Funzione pensata per essere overidata dai componenti che ereditano questa classe.
   * La funzione deve prevedere l'inizializzazione dei dati specifici di una tipologia di prenotazione per la conferma.
   * @param dataModal IBollettiniModalConfigs con i dati di configurazione della modale.
   */
  protected initMFDConferma(dataModal: IBollettiniModalDataConfigs) {
    // Funzione pensata per l'override.
  }

  /**
   * Funzione pensata per essere overidata dai componenti che ereditano questa classe.
   * La funzione deve prevedere l'inizializzazione dei dati specifici di una tipologia di prenotazione per l'emissione.
   * @param dataModal IBollettiniModalConfigs con i dati di configurazione della modale.
   */
  protected initMFDEmissione(dataModal: IBollettiniModalDataConfigs) {
    // Funzione pensata per l'override.
  }

  /**
   * Funzione pensata per essere overidata dai componenti che ereditano questa classe.
   * Viene comunque definita la funzionalità di "pulizia" di possibili errori generate a seguito dell'init del mainForm.
   * La funzione deve prevedere le logiche da eseguire al termine del processo dell'hook di Angular: OnInit.
   * @param dataModal IBollettiniModalConfigs con i dati di configurazione della modale.
   */
  protected onInitComponenteBMComplete(dataModal: IBollettiniModalDataConfigs) {
    // Definisco come "pulita" la form
    this.mainForm.markAsPristine();
    this.mainForm.markAsUntouched();
  }

  /**
   * #######################
   * FUNZIONI DEL COMPONENTE
   * #######################
   */

  /**
   * Funzione di comodo che verifica e setta le informazioni all'interno della form.
   * @param f FormGroup con la referenza della form d'aggiornare.
   * @param k string con la chiave per l'inserimento del dato.
   * @param d any con i dati da impostare.
   * @param o Object contenente la configurazione per le opzioni di gestione dell'aggiornamento della form.
   */
  protected setMainFormFieldData(f: FormGroup, k: string, d: any, o?: Object) {
    // Verifico l'input
    const existF = f != null;
    const existK = k != null;

    if (existF && existK) {
      // Verifico le options
      o = o ?? { emitEvent: false };
      // Imposto il valore nel form
      this._riscaUtilities.setFormValue(f, k, d, o);
    }
  }

  /**
   * Funzione di supporto per il set del tipo elaborazione all'interno del form.
   * La modifica avverrà per referenza.
   */
  protected setTipoElaborazioneForm() {
    // Variabili di comodo
    const form = this.mainForm;
    const key = this.BM_C.TIPO_ELABORABORAZIONE;
    const value = this.tipoElaborazione?.des_tipo_elabora;

    // Imposto il valore nel form
    this.setMainFormFieldData(form, key, value);
  }

  /**
   * Funzione di comodo per recuperare i validator per la gestione dell'emissione.
   * @returns Array di ValidatorFn con la lista di validatori per la modalita: emissione.
   */
  protected getValidatorsEmissione(): ValidatorFn[] {
    // Definisco l'array dei validatori per l'emissione
    const v = [Validators.required];
    // Ritorno i validatori in base al tipo
    return this.isModalitaEmissione ? v : [];
  }

  /**
   * Funzione di comodo per recuperare i validator per la gestione della conferma.
   * @returns Array di ValidatorFn con la lista di validatori per la modalita: conferma.
   */
  protected getValidatorsConferma(): ValidatorFn[] {
    // Definisco l'array dei validatori per la conferma
    const v = [Validators.required];
    // Ritorno i validatori in base al tipo
    return this.isModalitaConferma ? v : [];
  }

  /**
   * Funzione di comodo per recuperare lo stato di abilitazione dei campi per la gestione dell'emissione.
   * @returns boolean che definisce se i campi sono da disabilitare per la modalità: emissione.
   */
  protected getDisableEmissione(): boolean {
    // Ritorno i validatori in base al tipo
    return !this.isModalitaEmissione;
  }

  /**
   * Funzione di comodo per recuperare lo stato di abilitazione dei campi per la gestione della conferma.
   * @returns boolean che definisce se i campi sono da disabilitare per la modalità: conferma.
   */
  protected getDisableConferma(): boolean {
    // Ritorno i validatori in base al tipo
    return !this.isModalitaConferma;
  }

  /**
   * Chiama la funzione che setta sulla proprietà stato_elabora un oggetto contenente lo stato con codice EMISSIONE_RICHIESTA
   * @param elabora Elaborazione da modificare.
   */
  protected addEmissioneRichiestaToElaborazione(elabora: ElaborazioneVo) {
    // Richiamo la funzione per aggiungere il dato ad Elaborazione
    this._bollettiniModal.addEmissioneRichiestaToElaborazione(elabora);
  }

  /**
   * Chiama la funzione che setta sulla proprietà stato_elabora un oggetto contenente lo stato con codice CONFERMA_RICHIESTA
   * @param elabora Elaborazione da modificare.
   */
  protected addConfermaRichiestaToElaborazione(elabora: ElaborazioneVo) {
    // Richiamo la funzione per aggiungere il dato ad Elaborazione
    this._bollettiniModal.addConfermaRichiestaToElaborazione(elabora);
  }

  /**
   * Funzione che aggiunge ai parametri dell'oggetto Elaborazione in input, un ParametroElaborazione che definisce le informazioni per un dirigente pro tempore.
   * @param elabora Elaborazione da modificare.
   * @param dirigenteProTempore string che definisce il valore d'assegnare per il dirigente pro tempore.
   */
  protected addDirigenteProTemporeToElaborazione(
    elabora: ElaborazioneVo,
    dirigenteProTempore: string
  ) {
    // Richiamo la funzione per aggiungere il dato ad Elaborazione
    this._bollettiniModal.addDPTToElaborazione(elabora, dirigenteProTempore);
  }

  /**
   * Funzione di supporto che definisce le logiche di generazione dell'oggetto Elaborazione da restituire al componente chiamante.
   * Le logiche sono specifiche per la modalità: emissione.
   * @param formData T con i dati generati dalla form.
   * @returns Elaborazione con le informazioni generate dai dati della form.
   */
  protected generateElaborazioneEmissione(formData: T): ElaborazioneVo {
    // Recupero i dati per compilare l'oggetto elaborazione
    const te = this.tipoElaborazione;
    const r = this.raggruppamento;
    // Effettuo un parse locale per il parametro
    const fd = formData as any as IBollettiniModalForm;
    // Definisco l'oggetto da restituire come elaborazione
    let e: ElaborazioneVo;

    // Converto il dato della form in un oggetto Elaborazione
    e = this._bmConverter.convertIBollettiniModalFormToElaborazione(fd, te, r);
    // Aggiungo il dato per stato_elabora
    this.addEmissioneRichiestaToElaborazione(e);

    // Ritorno l'oggetto generato
    return e;
  }

  /**
   * Funzione di supporto che definisce le logiche di generazione dell'oggetto Elaborazione da restituire al componente chiamante.
   * Le logiche sono specifiche per la modalità: conferma.
   * @param formData T con i dati generati dalla form.
   * @returns Elaborazione con le informazioni generate dai dati della form.
   */
  protected generateElaborazioneConferma(formData: T): ElaborazioneVo {
    // Effettuo un parse locale per il parametro
    const fd = formData as any as IBollettiniModalForm;
    // Recupero il dirigente pro tempore
    const dpt = fd.dirigenteProTempore;

    // Definisco l'oggetto da restituire come elaborazione
    let e: ElaborazioneVo;

    // Recupero i dati passati alla form
    e = cloneDeep(this.dataModal?.elaborazione);
    // Aggiungo ai parametri il dirigente pro tempore
    this.addDirigenteProTemporeToElaborazione(e, dpt);
    // Aggiungo il dato per stato_elabora
    this.addConfermaRichiestaToElaborazione(e);

    // Ritorno l'oggetto generato
    return e;
  }

  /**
   * ################################
   * FUNZIONI PER GESTIONE DELLA FORM
   * ################################
   */

  /**
   * Funzione che permette di gestire il caso in cui il submit della form è andato a buon fine.
   * @param formData T con i dati generati dalla form.
   */
  protected onFormSuccess(formData: T) {
    // Definisco l'oggetto da restituire come elaborazione
    let e: ElaborazioneVo;

    // Verifico la modalità della modale
    if (this.isModalitaEmissione) {
      // Converto il dato della form in un oggetto Elaborazione
      e = this.generateElaborazioneEmissione(formData);
      // #
    } else if (this.isModalitaConferma) {
      // Converto il dato della form in un oggetto Elaborazione
      e = this.generateElaborazioneConferma(formData);
      // #
    }

    // Richiamo la funzione di ritorno dati
    this.modalConfirm(e);
  }

  /**
   * Funzione che permette di gestire il caso in cui il submit della form ha generato degli errori.
   * @param errors Array di string contenente tutti gli errori generati dalla form.
   */
  protected onFormErrors(errors: string[]) {
    // Definire una logica personalizzata
  }

  /**
   * Funzione di reset del form e del componente.
   */
  onFormReset() {
    // Reset della form
    this.mainForm.reset();
    // Resetto il flag del mainForm
    this.mainFormSubmitted = false;

    // Reimposto il valore per il tipo elaborazione
    this.initMainFormData(this.dataModal);
    // Pulisco eventuali errori
    this.mainForm.markAsPristine();
    this.mainForm.markAsUntouched();
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
   * @param data Elaborazione che definisce il dato di ritorno della modale.
   */
  modalConfirm(data?: ElaborazioneVo) {
    // Close della modale
    this.activeModal.close(data);
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter per l'idAmbito dell'utente.
   */
  get idAmbito() {
    return this._user.idAmbito;
  }

  /**
   * Getter per il raggruppamento degli oggetti ParametroElaborazione dell'utente.
   */
  get raggruppamento() {
    return this._bollettiniModal.raggruppamentoByAmbito(this.idAmbito);
  }

  /**
   * Getter di comodo per recuperare la modalità del modale.
   */
  get modalitaModal() {
    // Ritorno la modalità della modale
    return this.dataModal?.modalita;
  }

  /**
   * Getter di comodo per recuperare il title della modale.
   */
  get title() {
    // Verifico la modalità della modale
    switch (this.modalitaModal) {
      case NPModalitaModale.conferma_prenotazione:
        // Il title è statico
        return 'Conferma';
      case NPModalitaModale.emissione_prenotazione:
        // Il title è il tipo di elaborazione
        return this.tipoElaborazione?.des_tipo_elabora ?? '';
      default:
        // Gestione del fallback
        return 'MODAL_TITLE_NOT_FOUND';
    }
  }

  /**
   * Getter di comodo per recuperare dato che gestisce il tipo elaborazione.
   */
  get tipoElaborazione() {
    // Verifico la modalità della modale
    switch (this.modalitaModal) {
      case NPModalitaModale.conferma_prenotazione:
        // Recupero il dato dall'oggetto "elaborazione"
        return this.dataModal?.elaborazione?.tipo_elabora;
      case NPModalitaModale.emissione_prenotazione:
        // Recupero il dato dall'oggetto "tipoElaborazione"
        return this.dataModal?.tipoElaborazione;
      default:
        // Gestione del fallback
        return undefined;
    }
  }

  /**
   * Getter di comodo per sapere se la modalità della modale è: emissione
   */
  get isModalitaEmissione() {
    return this.modalitaModal === NPModalitaModale.emissione_prenotazione;
  }

  /**
   * Getter di comodo per sapere se la modalità della modale è: conferma
   */
  get isModalitaConferma() {
    return this.modalitaModal === NPModalitaModale.conferma_prenotazione;
  }
}
