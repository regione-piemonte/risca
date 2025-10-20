import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import * as moment from 'moment';
import { Subscription } from 'rxjs/index';
import { LoggerService } from 'src/app/core/services/logger.service';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { RiscaFormParentAndChildComponent } from 'src/app/shared/components/risca/risca-form-parent-and-child/risca-form-parent-and-child.component';
import { RiscaAlertService } from 'src/app/shared/services/risca/risca-alert.service';
import { RiscaFormBuilderService } from 'src/app/shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from 'src/app/shared/services/risca/risca-form-submit-handler.service';
import { RiscaMessagesService } from 'src/app/shared/services/risca/risca-messages.service';
import { RiscaUtilitiesService } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaServerError } from 'src/app/shared/utilities';
import { RiscaErrorsMap } from 'src/app/shared/utilities/classes/errors-maps';
import { StatoDebitorioVo } from '../../../../../core/commons/vo/stato-debitorio-vo';
import { CalcoloInteressiService } from '../../../service/dati-contabili/calcolo-interessi.service';
import { getRataPadre } from '../../../service/dati-contabili/dati-contabili/dati-contabili-utility.functions';
import { CommonConsts } from './../../../../../shared/consts/common-consts.consts';
import { CalcoloInteressiReq } from './../../../service/dati-contabili/calcolo-interessi.service';
import { CalcoloInteressiConsts } from './utilities/calcolo-interessi.consts';
import { CIFieldsConfigClass } from './utilities/calcolo-interessi.fields-configs';
import { ICalcoloInteressi } from './utilities/calcolo-interessi.interfaces';

@Component({
  selector: 'calcolo-interessi',
  templateUrl: './calcolo-interessi.component.html',
  styleUrls: ['./calcolo-interessi.component.scss'],
})
export class CalcoloInteressiComponent
  extends RiscaFormParentAndChildComponent<ICalcoloInteressi>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto contenente una serie di costanti per la gestione dei accertamenti. */
  CI_C = CalcoloInteressiConsts;

  /** StatoDebitorioVo con le informazioni dello stato debitorio per i accertamenti. */
  @Input() statoDebitorio: StatoDebitorioVo;

  /** Classe RFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: CIFieldsConfigClass;

  /**
   * ##############################################
   * SUBSCRIPTION AL SALVATAGGIO DELLE INFORMAZIONI
   * ##############################################
   */
  /** Subscription che permette la condivisione dell'evento di modifica di un oggetto StatoDebitorioVo. */
  onSDModSuccess: Subscription;
  /** Subscription che permette la condivisione dell'evento d'errore durante una modifica di un oggetto StatoDebitorioVo. */
  onSDModError: Subscription;
  /** Subscription che permette la condivisione dell'evento di annuallamento della funzionalità di modifica di un oggetto StatoDebitorioVo. */
  onSDModCancel: Subscription;

  /**
   * Costruttore
   */
  constructor(
    private _calcoloInteressi: CalcoloInteressiService,
    private _formBuilder: FormBuilder,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaAlert: RiscaAlertService,
    private _riscaFormBuilder: RiscaFormBuilderService,
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

    // Lancio il setup del componente
    this.setupComponente();
  }

  /**
   * ################################
   * FUNZIONI DI SETUP DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione di setup del componente.
   */
  private setupComponente() {
    // Lancio il setup delle forms
    this.setupFields();
  }

  /**
   * Funzione di setup per i campi del form del componente.
   */
  private setupFields() {
    // Definisco in una variabile il servizio del builder
    const riscaFormBuilder = this._riscaFormBuilder;
    // Inizializzo le configurazioni per i campi
    this.formInputs = new CIFieldsConfigClass({ riscaFormBuilder });
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  /**
   * Init
   */
  ngOnInit() {
    // Inizializzazione della form principale
    this.initMainForm();
    // Avvio la ricerca dei dati contabili
    this.initComponente();
  }

  /**
   * Destroy
   * @override
   */
  ngOnDestroy(): void {
    // Invoco il destroy del padre
    super.ngOnDestroy();
    // Tento di fare l'unsubscribe degli events
    try {
      // Verifico che esista l'event emitter
      if (this.onSDModSuccess) {
        this.onSDModSuccess.unsubscribe();
      }
      if (this.onSDModError) {
        this.onSDModError.unsubscribe();
      }
      if (this.onSDModCancel) {
        this.onSDModCancel.unsubscribe();
      }
    } catch (e) {}
  }

  /**
   * Funzione che prevede l'inizializzazione della variabile mainForm per la gestione del form d'inserimento.
   * @override
   */
  protected initMainForm() {
    // Inizializzo la form
    this.mainForm = this._formBuilder.group({
      canoneMancante: new FormControl(
        { value: null, disabled: false },
        { validators: [Validators.required] }
      ),
      dataScadenza: new FormControl(
        { value: null, disabled: false },
        { validators: [Validators.required] }
      ),
      dataVersamento: new FormControl(
        { value: null, disabled: false },
        { validators: [Validators.required] }
      ),
      interessiDovuti: new FormControl(
        { value: null, disabled: false },
        { validators: [] }
      ),
      totaleDaVersare: new FormControl(
        { value: null, disabled: false },
        { validators: [] }
      ),
    });
  }

  /**
   * Questa funzione è pensata per valorizzare le componenti della pagina, recuperando le informazioni dalla configurazione in ingresso.
   */
  private initComponente() {
    // Recupero le proprietà dallo stato debitorio per il calcolo del canone mancante
    const canoneDovuto = this.statoDebitorio.canone_dovuto ?? 0;
    const importoCompensato = this.statoDebitorio.imp_compensazione_canone ?? 0;
    const importoVersato = this.statoDebitorio.importo_versato ?? 0;
    // Calcolo il canone dovuto
    let canoneMancante: number =
      canoneDovuto - (importoCompensato + importoVersato);
    // Se il valore è negativo lo porto a 0
    canoneMancante = canoneMancante < 0 ? 0 : canoneMancante;
    // Formatto il valore
    canoneMancante = this._riscaUtilities.arrotondaEccesso(canoneMancante, 2);
    // Popolo la form con il canone dovuto calcolato
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.CI_C.CANONE_MANCANTE,
      canoneMancante
    );

    // Prendo la rata padre
    const rata_padre = getRataPadre(this.statoDebitorio.rate);
    // Prendo la data scadenza pagamento dalla rata padre
    const data_scadenza_pagamento = rata_padre?.data_scadenza_pagamento;
    // Converto la data nel formato della form
    const data_scadenza_pagamento_view =
      this._riscaUtilities.convertMomentToNgbDateStruct(
        data_scadenza_pagamento
      );
    // Popolo la form con la data scadenza
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.CI_C.DATA_SCADENZA_PAGAMENTO,
      data_scadenza_pagamento_view
    );

    /**
     * Data versamento
     */
    // Data versamento di default = sysdate
    const data_versamento_default = moment();
    // Converto la data versamento nel formato della form
    const data_versamento = this._riscaUtilities.convertMomentToNgbDateStruct(
      data_versamento_default
    );
    // Popolo la form con la data scadenza
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.CI_C.DATA_VERSAMENTO,
      data_versamento
    );

    // Stato della form
    this.mainForm.markAsPristine();
  }

  /**
   * ################
   * FUNZIONI BOTTONI
   * ################
   */

  /**
   * Funzione di supporto che gestisce il success del submit della form.
   * @param data ICalcoloInteressi contenente le informazioni del form.
   * @override
   */
  protected onFormSuccess(data: ICalcoloInteressi) {
    // Prendo i dati della form
    let {
      canoneMancante: canone_dovuto,
      dataScadenza: data_scadenza,
      dataVersamento: data_versamento,
    } = data;
    // Il canone dovuto deve essere >= 0
    canone_dovuto = canone_dovuto < 0 ? 0 : canone_dovuto;
    // Trasformo i dati nel formato utile al server
    const request = new CalcoloInteressiReq({
      canone_dovuto,
      data_scadenza,
      data_versamento,
    });

    // Eseguo la chiamata al servizio
    this._calcoloInteressi.calcoloInteressi(request).subscribe({
      next: (interessiDovuti: number) => {
        // Popolo la form con i dati trovati
        this.onCalcoloInteressi(interessiDovuti);
        // Pulisco l'alert
        this.resetAlertConfigs();
        //#
      },
      error: (err: RiscaServerError) => {
        // Gestisco l'errore
        this.onServiziError(err);
        //#
      },
    });
  }

  /**
   * Popola la form con gli interessi dovuti calcolati dal BE.
   * @param interessi_dovuti number con il valore calcolato degli interessi dovuti.
   */
  onCalcoloInteressi(interessi_dovuti: number) {
    // Prendo il canone dovuto dato in input
    const canone_dovuto = this._riscaUtilities.getFormValue(
      this.mainForm,
      this.CI_C.CANONE_MANCANTE
    );
    // Calcolo il totale da versare
    let totale_da_versare = canone_dovuto + interessi_dovuti;

    // Setto il valore degli interessi dovuti
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.CI_C.INTERESSI_DOVUTI,
      interessi_dovuti
    );

    // Setto il valore del totale da versare
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.CI_C.TOTALE_DA_VERSARE,
      totale_da_versare
    );
  }

  /**
   * ###################################################
   * GESTIONE DELL'ACCORDION E DELL'INSERIMENTO RIMBORSO
   * ###################################################
   */

  /**
   * Funzione di reset del form e del componente.
   * @override
   */
  onFormReset() {
    // Resetto il flag di submit
    this.mainFormSubmitted = false;
    // Ripristino dei valori
    this.initComponente();
    // Ripristino lo stato della form
    this.mainForm.markAsPristine();
  }

  /**
   * #####################
   * FUNZIONI DI UTILITIES
   * #####################
   */

  /**
   * Formatta un importo mettendo una virgola a separare i decimali ed il punto per separare le migliaia.
   * La formattazione troncherà i numeri decimali.
   * @param importo number | string da formattare.
   * @param decimal number con il numero di decimali da mandatenere.
   * @param decimaliNonSignificativi boolean che definisce se, dalla gestione dei decimali, bisogna completare i decimali con gli 0 mancanti (non significativi). Per default è false.
   * @returns string con la formattazione applicata.
   */
  formatImportoITA(
    importo: number | string,
    decimals?: number,
    decimaliNonSignificativi: boolean = false
  ): string {
    // Richiamo la funzione di comodo
    return this._riscaUtilities.formatoImportoITA(
      importo,
      decimals,
      decimaliNonSignificativi
    );
    // #
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter che verifica se l'oggetto alertConfigs esiste e ha messaggi.
   */
  get alertConfigsCheck() {
    // Verifico e ritorno la condizione
    return this._riscaAlert.alertConfigsCheck(this.alertConfigs);
  }

  /**
   * Getter di comodo per il recupero della configurazione per il pulsante: Versamenti.
   */
  get btnVersamenti() {
    // Recupero la configurazione del pulsante
    return this.CI_C.BTN_CONFIG_VERSAMENTI;
  }
}
