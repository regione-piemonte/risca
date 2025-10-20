import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { RicercaPraticaSemplice, RiscaRegExp } from 'src/app/shared/utilities';
import { RiscaErrorsMap } from 'src/app/shared/utilities/classes/errors-maps';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { RiscaRicercaSemplicePratiche } from '../../../consts/risca/risca-ricerca-semplice-pratica.consts';
import { RiscaFormBuilderService } from '../../../services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import { RiscaFormChildComponent } from '../risca-form-child/risca-form-child.component';
import { RiscaRicercaSempliceFieldsConfigClass } from './class/risca-ricerca-semplice.fields-configs';

@Component({
  selector: 'risca-ricerca-semplice-pratiche',
  templateUrl: './risca-ricerca-semplice-pratiche.component.html',
  styleUrls: ['./risca-ricerca-semplice-pratiche.component.scss'],
})
export class RiscaRicercaSemplicePraticheComponent
  extends RiscaFormChildComponent<RicercaPraticaSemplice>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente i valori costanti per il componente. */
  RRSP_C = RiscaRicercaSemplicePratiche;

  /** Oggetto RicercaPraticaSemplice contenente le informazioni di parametrizzazione del componente. */
  @Input('ricerca') ricercaConfig: RicercaPraticaSemplice;

  /** Boolean che definisce lo stato di submit della form: ricercaPraticaForm. */
  ricercaPraticaSubmitted = false;

  /** Classe HomeFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: RiscaRicercaSempliceFieldsConfigClass;

  /** Classe costante contenente il mapping degli errori per i form. */
  EM = new RiscaErrorsMap();

  /** Classe RiscaRegExp contenente le regular expression dell'applicazione. */
  private riscaRegExp = new RiscaRegExp();

  /**
   *
   */
  constructor(
    logger: LoggerService,
    private _formBuilder: FormBuilder,
    navigationHelper: NavigationHelperService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    utilityService: RiscaUtilitiesService,
    submitHandlerService: RiscaFormSubmitHandlerService
  ) {
    super(logger, navigationHelper, submitHandlerService, utilityService);
    // Classe di supporto che definisce la configurazioni per le input
    this.formInputs = new RiscaRicercaSempliceFieldsConfigClass(
      this._riscaFormBuilder
    );
  }

  ngOnInit() {
    // Init della struttura della form ricerca pratica
    this.initRicercaPratica();
    // Init dei dati del form, se passata una configurazione
    this.initRicercaPraticaFields(this.ricercaConfig);
  }

  /**
   * ngOnDestroy.
   */
  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * Setup del form: ricercaPraticaForm.
   */
  private initRicercaPratica() {
    this.mainForm = this._formBuilder.group({
      codiceUtenza: new FormControl(
        { value: null, disabled: false },
        { validators: [] }
      ),
      numeroAvvisoPagamento: new FormControl(
        { value: null, disabled: false },
        { validators: [] }
      ),
      numeroPratica: new FormControl(
        { value: null, disabled: false },
        { validators: [] }
      ),
      codiceFiscale: new FormControl(
        { value: null, disabled: false },
        {
          validators: [Validators.pattern(this.riscaRegExp.codiceFiscaleAll)],
        }
      ),
      ragioneSocialeOCognome: new FormControl(
        { value: null, disabled: false },
        { validators: [] }
      ),
      codiceAvviso: new FormControl(
        { value: null, disabled: false },
        { validators: [] }
      ),
      // test: new FormControl(
      //   { value: null, disabled: false },
      //   { validators: [] }
      // ),
    });
  }

  /**
   * Funzione di comodo che permette di valorizzare i campi della form se viene definito un oggetto di configurazione.
   * @param configs RicercaPraticaSemplice con i dati da caricare nel form.
   */
  private initRicercaPraticaFields(configs: RicercaPraticaSemplice) {
    // Verifico l'input
    if (!configs) {
      // Niente da configurare
      return;
    }

    // Lancio la funzione di utility
    this._riscaUtilities.initFormFields(this.mainForm, configs);
  }
}
