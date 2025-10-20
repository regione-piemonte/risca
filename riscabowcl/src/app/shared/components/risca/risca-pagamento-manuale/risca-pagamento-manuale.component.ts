import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Moment } from 'moment';
import { PagamentoVo } from 'src/app/core/commons/vo/pagamento-vo';
import { TipoModalitaPagamentoVo } from 'src/app/core/commons/vo/tipo-modalita-pagamento-vo';
import { DettaglioPagSearchResultVo } from '../../../../core/commons/vo/dettaglio-pag-search-result-vo';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../core/services/user.service';
import { PagamentiSDConsts } from '../../../../features/pratiche/components/dati-contabili/pagamenti-sd/utilities/pagamenti-sd.consts';
import { RiscaFormBuilderService } from '../../../services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { convertMomentToNgbDateStruct } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import { RiscaErrorsMap } from '../../../utilities/classes/errors-maps';
import { RiscaFormChildComponent } from '../risca-form-child/risca-form-child.component';
import { RiscaPagamentoManualeFieldsConfigClass } from './utilities/risca-pagamento-manuale.fields-configs';
import { IPagamentoManualeForm } from './utilities/risca-pagamento-manuale.interfaces';

@Component({
  selector: 'risca-pagamento-manuale',
  templateUrl: './risca-pagamento-manuale.component.html',
  styleUrls: ['./risca-pagamento-manuale.component.scss'],
})
export class RiscaPagamentoManualeComponent
  extends RiscaFormChildComponent<PagamentoVo>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente una serie di costanti per la gestione degli errori. */
  EM = new RiscaErrorsMap();
  /** Oggetto contenente una serie di costanti per la gestione dei versamenti. */
  V_C = PagamentiSDConsts;

  /** Classe RimborsoFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: RiscaPagamentoManualeFieldsConfigClass;
  /** Oggetto del pagamento da gestire */
  @Input() pagamento: PagamentoVo = new PagamentoVo({
    tipo_modalita_pag: new TipoModalitaPagamentoVo(
      undefined,
      this.V_C.MODALITA_PAGAMENTO_MANUALE_COD,
      this.V_C.MODALITA_PAGAMENTO_MANUALE
    ),
  });
  /** Input DettaglioPagSearchResultVo[] cone le informazioni di dettaglio del versamento. */
  @Input() dettagliPagamento: DettaglioPagSearchResultVo[];

  /**
   * Costruttore.
   */
  constructor(
    public activeModal: NgbActiveModal,
    private _formBuilder: FormBuilder,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {
    // Richiamo il super
    super(logger, navigationHelper, riscaFormSubmitHandler, riscaUtilities);
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit(): void {
    // Lancio il setup delle form
    this.initForms();
  }

  /**
   * ngOnDestroy.
   */
  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
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
    this.formErrors = [...this.EM.MAP_FORM_GROUP_REQUIRED];
  }

  /**
   * Funzione di setup per le input del form.
   */
  private setupFormInputs() {
    // Genero il setup per i campi
    this.formInputs = new RiscaPagamentoManualeFieldsConfigClass(
      this._riscaFormBuilder
    );
  }

  /**
   * Funzione che effettua il setup per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Setup del form
    this.initRecapitoForm();
  }

  /**
   * Setup del form.
   */
  private initRecapitoForm() {
    // Converto la data_op_val per la form
    const dataOpVal = convertMomentToNgbDateStruct(
      this.pagamento.data_op_val as Moment
    );
    // Creo la form con i dati inseriti
    this.mainForm = this._formBuilder.group({
      data_op_val: new FormControl(
        { value: dataOpVal, disabled: false },
        { validators: [Validators.required] }
      ),
      importo_versato: new FormControl(
        {
          value: this.pagamento.importo_versato ?? undefined,
          disabled: false,
        },
        { validators: [Validators.required] }
      ),
      tipo_modalita_pag: new FormControl(
        {
          value: this.pagamento.tipo_modalita_pag.des_modalita_pag,
          disabled: true,
        },
        { validators: [] }
      ),
      note: new FormControl(
        { value: this.pagamento.note ?? undefined, disabled: false },
        { validators: [Validators.maxLength(500)] }
      ),
    });
  }

  /**
   * #####################
   * METODI DEL COMPONENTE
   * #####################
   */

  /**
   * Funzione lanciata all'evento di aggiunta di un versamento
   */
  aggiungiVersamento() {
    // Se la form è valida la committo, altrimenti no
    this.onFormSubmit();
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
  }

  /**
   * Funzione di restore delle informazioni richiamabile dal componente padre.
   */
  onFormRestore() {
    // Resetto il flag del form submitted
    this.mainFormSubmitted = false;
    // Rilancio i setup/init del componente
    this.initForms();
  }

  /**
   * Converte l'elemento ottenuto dalla form in un PagamentoVo da passare alla tabella.
   * @returns PagamentoVo con l'oggetto convertito
   * @override
   */
  getMainFormRawValue(): PagamentoVo {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      return undefined;
    }
    // Il main form esiste, ritorno i dati del form
    const rimborsoForm: IPagamentoManualeForm = this.mainForm.getRawValue();

    // Estraggo i dati
    const { data_op_val, importo_versato, note } = rimborsoForm;

    // Converto gli oggetti
    const data_versamento_sd =
      this._riscaUtilities.convertNgbDateStructToServerDate(data_op_val);

    // Creo l'oggetto tipo modalità pagamento
    const tipo_modalita_pag = new TipoModalitaPagamentoVo(
      0,
      this.V_C.MODALITA_PAGAMENTO_MANUALE_COD,
      this.V_C.MODALITA_PAGAMENTO_MANUALE
    );

    // Prendo l'id pagamento, anche se undefined
    let { id_pagamento, id_tipologia_pag, id_tipo_modalita_pag, ambito, dettaglio_pag } =
      this.pagamento;

    /** PER ORA LO FA IL BE, SE IL PAGAMENTO NON HA L'OGGETTO AMBITO E VIENE GESTITA UNA POST. */
    // // Definisco l'id ambito per il pagamento
    // if (!ambito) {
    //   // Ambito non definito, recupero l'id_ambito dalla configurazione
    //   const idAmbito = this._user.idAmbito;
    //   // Genero un finto oggetto AmbitoVo
    //   ambito = new AmbitoVo(idAmbito, '', '');
    // }

    // Creo l'elemento
    const rimborso = new PagamentoVo({
      ambito,
      id_pagamento,
      id_tipologia_pag,
      id_tipo_modalita_pag,
      data_op_val: data_versamento_sd,
      importo_versato,
      tipo_modalita_pag,
      note,
      dettaglio_pag
    });
    // ritorno l'elemento convertito
    return rimborso;
  }

  /**
   * ##################################################################################
   * FUNZIONI DI SET DELLE INFORMAZIONI LEGATE AI DATI ANAGRAFICI DEL SOGGETTO E GRUPPO
   * ##################################################################################
   */

  /**
   * Funzione che effettua la dismiss (e quindi la reject) della modale.
   */
  modalDismiss() {
    // Dismiss della modale
    this.activeModal.dismiss();
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Variabile di comodo che dice se la form principale è valida
   */
  get valid() {
    return this.mainForm?.valid;
  }
}
