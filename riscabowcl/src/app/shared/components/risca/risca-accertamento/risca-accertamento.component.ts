import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { AccertamentiConsts } from 'src/app/features/pratiche/components/dati-contabili/accertamenti/utilities/accertamenti.consts';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import {
  AppActions,
  IRiscaCheckboxData,
  RiscaServerError,
} from '../../../utilities';
import { RiscaFormChildComponent } from '../risca-form-child/risca-form-child.component';
import { AccertamentoVo } from './../../../../core/commons/vo/accertamento-vo';
import { TipoAccertamentoVo } from './../../../../core/commons/vo/tipo-accertamento-vo';
import { AccertamentiService } from './../../../../features/pratiche/service/accertamenti/accertamenti.service';
import { RiscaFormBuilderService } from '../../../services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaErrorsMap } from './../../../utilities/classes/errors-maps';
import { AccertamentoFieldsConfigClass } from './utilities/risca-accertamento.fields-configs';
import { IAccertamentoForm } from './utilities/risca-accertamento.interfaces';

@Component({
  selector: 'risca-accertamento',
  templateUrl: './risca-accertamento.component.html',
  styleUrls: ['./risca-accertamento.component.scss'],
})
export class RiscaAccertamentoComponent
  extends RiscaFormChildComponent<AccertamentoVo>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente una serie di costanti per la gestione degli errori. */
  EM = new RiscaErrorsMap();
  /** Oggetto contenente una serie di costanti per la gestione dei rimborsi. */
  ACC_C = AccertamentiConsts;

  /** Oggetto da modificare/inserire */
  @Input() accertamento?: AccertamentoVo = new AccertamentoVo();

  /** Classe AccertamentoFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: AccertamentoFieldsConfigClass;

  /** Lista di oggetti TipoAccertamentoVo contenente le informazioni per la lista attività. */
  listaTipiAccertamenti: TipoAccertamentoVo[] = [];

  /**
   * Costruttore.
   */
  constructor(
    private _formBuilder: FormBuilder,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    private _riscaAccertamento: AccertamentiService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(logger, navigationHelper, riscaFormSubmitHandler, riscaUtilities);
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit(): void {
    // Lancio il setup delle form
    this.initForms();
    // Setup delle liste per le select del form
    this.initListeSelect();
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
    this.formInputs = new AccertamentoFieldsConfigClass(this._riscaFormBuilder);
  }

  /**
   * Funzione che effettua il setup per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Setup del form
    this.initAccertamentoForm();
    // Setup dei campi form in base alla parametrizzazione degli input
    this.initAccertamentoFormValues();
  }
  
  /**
   * Setup del form.
   */
  private initAccertamentoForm() {
    // Controllo se mi è stato passato un input
    if (this.modalita == AppActions.inserimento) {
      this.accertamento = new AccertamentoVo();
    }

    // Creo la main form
    this.mainForm = this._formBuilder.group({
      num_protocollo: new FormControl(
        { value: null, disabled: false },
        { validators: [Validators.required] }
      ),
      data_protocollo: new FormControl(
        { value: null, disabled: false },
        { validators: [Validators.required] }
      ),
      data_scadenza: new FormControl(
        { value: null, disabled: false },
        { validators: [] }
      ),
      data_notifica: new FormControl(
        { value: null, disabled: false },
        { validators: [] }
      ),
      tipo_accertamento: new FormControl(
        { value: null, disabled: false },
        { validators: [Validators.required] }
      ),
      flg_restituito: new FormControl(
        { value: null, disabled: false },
        { validators: [] }
      ),
      flg_annullato: new FormControl(
        { value: null, disabled: false },
        { validators: [] }
      ),
      nota: new FormControl(
        { value: null, disabled: false },
        { validators: [Validators.maxLength(500)] }
      ),
    });
    // Pulisco eventuali errori
    // this.mainForm.markAsPristine();
  }

  /**
   * Funzione di  inizializzazione dei valori della form
   */
  initAccertamentoFormValues() {
    // Prendo i dati da inserire
    const {
      num_protocollo,
      data_protocollo,
      data_scadenza,
      data_notifica,
      flg_restituito,
      flg_annullato,
      nota,
    } = this.accertamento;

    if (num_protocollo != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.ACC_C.NUM_PROTOCOLLO,
        num_protocollo
      );
    }

    if (data_protocollo != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.ACC_C.DATA_PROTOCOLLO,
        this._riscaUtilities.convertMomentToNgbDateStruct(data_protocollo)
      );
    }

    if (data_scadenza != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.ACC_C.DATA_SCADENZA,
        this._riscaUtilities.convertMomentToNgbDateStruct(data_scadenza)
      );
    }

    if (data_notifica != undefined) {
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.ACC_C.DATA_NOTIFICA,
        this._riscaUtilities.convertMomentToNgbDateStruct(data_notifica)
      );
    }

    // Checkbox Restituito
    if (flg_restituito != undefined) {
      const flgRestituito: IRiscaCheckboxData = {
        check: flg_restituito ?? false,
        value: flg_restituito ?? false,
        id: this.ACC_C.FLG_RESTITUITO,
        label: this.ACC_C.LABEL_FLG_RESTITUITO,
      };
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.ACC_C.FLG_RESTITUITO,
        flgRestituito
      );
      // Aggiorno il source della configurazione dell'input della checkbox
      this._riscaUtilities.updateRFICheckboxSource(
        this.formInputs.flgRestituitoConfig,
        flgRestituito
      );
    }

    // Checkbox Annullato
    if (flg_annullato != undefined) {
      const flgAnnullato: IRiscaCheckboxData = {
        check: flg_annullato ?? false,
        value: flg_annullato ?? false,
        id: this.ACC_C.FLG_ANNULLATO,
        label: this.ACC_C.LABEL_FLG_ANNULLATO,
      };
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.ACC_C.FLG_ANNULLATO,
        flgAnnullato
      );
      // Aggiorno il source della configurazione dell'input della checkbox
      this._riscaUtilities.updateRFICheckboxSource(
        this.formInputs.flgAnnullatoConfig,
        flgAnnullato
      );
    }

    this._riscaUtilities.setFormValue(this.mainForm, this.ACC_C.NOTE, nota);
  }

  /**
   * Funzione di setup per le liste per le select del form.
   */
  private initListeSelect() {
    // Recupero il comune se configuratio e le province
    this._riscaAccertamento.getTipiAccertamenti().subscribe({
      next: (tipiAccertamento: TipoAccertamentoVo[]) => {
        // Richiamo le funzioni di gestione delle liste
        this.onScaricoTipiAttivitaSvolteA(tipiAccertamento);

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
   * @param tipiAttivita Array di TipoSedeVo scaricati.
   */
  private onScaricoTipiAttivitaSvolteA(tipiAttivita: TipoAccertamentoVo[]) {
    // Assegno localmente la lista
    this.listaTipiAccertamenti = tipiAttivita;
    // Se sono in modifica, devo settare il tipo attività
    if (this.modalita == AppActions.modifica && this.accertamento) {
      // Prendo l'idAttivita
      const { tipo_accertamento: tipo_accertamento } = this.accertamento;
      // Funzione i ricerca
      const find = (a: TipoAccertamentoVo, b: TipoAccertamentoVo) => {
        // Controllo per codice
        return a.cod_tipo_accertamento == b.cod_tipo_accertamento;
      };
      // Se ho trovato l'attività la assegno
      this._riscaUtilities.setFormValueAndSelect(
        this.mainForm,
        this.ACC_C.TIPO_ACCERTAMENTO,
        this.listaTipiAccertamenti,
        tipo_accertamento,
        find
      );
    }
  }

  /**
   * #####################
   * METODI DEL COMPONENTE
   * #####################
   */

  /**
   * Funzione lanciata all'evento di aggiunta di una attività Accertamento
   */
  aggiungiAttivitaAccertamento() {
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
    // Verifico la modalità
    if (this.modifica) {
      // Ripristino i valori della form
      this.initAccertamentoFormValues();
      // Ripristino le select
      this.onScaricoTipiAttivitaSvolteA(this.listaTipiAccertamenti);
    } else {
      // Resetto la form
      this.mainForm.reset();
    }
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
   * Converte l'elemento ottenuto dalla form in un SoggettoVo da passare alla tabella.
   * @returns AccertamentoVo con l'oggetto convertito
   * @override
   */
  getMainFormRawValue(): AccertamentoVo {
    // Verifico esista l'oggetto
    if (!this.mainForm) {
      return undefined;
    }
    // Il main form esiste, ritorno i dati del form
    const accertamentoForm: IAccertamentoForm = this.mainForm.getRawValue();

    // Estraggo i dati
    const {
      num_protocollo,
      data_protocollo,
      data_scadenza,
      data_notifica,
      tipo_accertamento,
      flg_annullato,
      flg_restituito,
      nota: nota,
    } = accertamentoForm;

    // Prendo i dati non modificati dall'oggetto originale
    const id_accertamento = this.accertamento?.id_accertamento;
    const id_stato_debitorio = this.accertamento?.id_stato_debitorio;
    const id_file_450 = this.accertamento?.id_file_450;

    // Converto le date in string per la generazione della classe Accertamento
    const dp: string =
      this._riscaUtilities.convertNgbDateStructToServerDate(data_protocollo);
    const ds: string =
      this._riscaUtilities.convertNgbDateStructToServerDate(data_scadenza);
    const dn: string =
      this._riscaUtilities.convertNgbDateStructToServerDate(data_notifica);

    // Creo l'elemento
    const accertamento = new AccertamentoVo({
      id_accertamento,
      id_stato_debitorio,
      tipo_accertamento,
      id_file_450,
      num_protocollo,
      data_protocollo: dp,
      data_scadenza: ds,
      data_notifica: dn,
      flg_restituito: flg_restituito?.check ? 1 : 0,
      flg_annullato: flg_annullato?.check ? 1 : 0,
      nota: nota,
    });
    // ritorno l'elemento convertito
    return accertamento;
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */
}
