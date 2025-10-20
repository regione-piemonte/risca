import { Component, EventEmitter, Input, OnInit, Output, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { StatoElaborazioneVo } from 'src/app/core/commons/vo/stato-elaborazione-vo';
import { LoggerService } from 'src/app/core/services/logger.service';
import { NavigationHelperService } from 'src/app/core/services/navigation-helper/navigation-helper.service';
import { UserService } from 'src/app/core/services/user.service';
import { RiscaFormBuilderService } from 'src/app/shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from 'src/app/shared/services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.service';
import { IRicercaBollettini, RiscaServerError } from 'src/app/shared/utilities';
import { RiscaErrorsMap } from 'src/app/shared/utilities/classes/errors-maps';
import { RRBollettiniPagamenti } from '../../../consts/risca/risca-ricerca-bollettini.consts';
import { dataInizioDataFineValidator } from '../../../miscellaneous/forms-validators/forms-validators';
import { RiscaFormChildComponent } from '../risca-form-child/risca-form-child.component';
import { ITipoElaborazioneVo } from './../../../../core/commons/vo/tipo-elaborazione-vo';
import { BollettiniService } from './../../../../features/pagamenti/service/bollettini/bollettini.service';
import { RiscaRicercaBollettiniFieldsConfigClass } from './utilities/risca-ricerca-bollettini.fields-configs';

export interface ITipiStatiRIcercaBollettini {
  tipi: ITipoElaborazioneVo[];
  stati: StatoElaborazioneVo[];
}

@Component({
  selector: 'risca-ricerca-bollettini',
  templateUrl: './risca-ricerca-bollettini.component.html',
  styleUrls: ['./risca-ricerca-bollettini.component.scss'],
})
export class RiscaRicercaBollettiniComponent
  extends RiscaFormChildComponent<IRicercaBollettini>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente i valori costanti per il componente. */
  RRB_C = RRBollettiniPagamenti;
  /** Classe costante contenente il mapping degli errori per i form. */
  EM = new RiscaErrorsMap();

  /** Oggetto RicercaPraticaSemplice contenente le informazioni di parametrizzazione del componente. */
  @Input() ricerca?: IRicercaBollettini;
  /** Evento da emettere quando la form ha finito di caricare i parametri di default */
  @Output() fineCaricamento: EventEmitter<any> = new EventEmitter<any>();
  /** Lista dei tipi di elaborazione */
  listaTipi: ITipoElaborazioneVo[] = [];
  /** Lista degli stati di elaborazione */
  listaStati: StatoElaborazioneVo[] = [];

  /** Boolean che definisce lo stato di submit della form: ricercaPraticaForm. */
  ricercaPraticaSubmitted = false;
  /** Classe HomeFieldsConfigClass di supporto, contenente le configurazioni delle input per il componente. */
  formInputs: RiscaRicercaBollettiniFieldsConfigClass;

  /**
   *
   */
  constructor(
    private _bollettini: BollettiniService,
    logger: LoggerService,
    private _formBuilder: FormBuilder,
    navigationHelper: NavigationHelperService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    submitHandlerService: RiscaFormSubmitHandlerService,
    utilityService: RiscaUtilitiesService,
    private _user: UserService
  ) {
    super(logger, navigationHelper, submitHandlerService, utilityService);
    this.setupComponente();
  }

  ngOnInit() {
    // Init della struttura della form ricerca pratica
    this.initForms();
    // Init delle select del form
    this.initListeSelect();
    // Init dei dati del form, se passata una configurazione
    this.initRicercaPraticaFields(this.ricerca);
  }

  /**
   * ngOnDestroy.
   */
  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /*************
   *** Setup ***
   *************/

  /**
   * Crea le configurazioni interne della componente
   */
  setupComponente() {
    // Classe di supporto che definisce la configurazioni per le input
    this.formInputs = new RiscaRicercaBollettiniFieldsConfigClass(
      this._riscaFormBuilder
    );
  }

  /********************
   * Inizializzazione *
   ********************/

  /**
   * Setup del form: ricercaPraticaForm.
   */
  private initForms() {
    this.mainForm = this._formBuilder.group(
      {
        tipo: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        stato: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        dataRichiestaInizio: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        dataRichiestaFine: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
      },
      // Validatori campi incrociati
      {
        validators: [
          dataInizioDataFineValidator(
            this.RRB_C.DATA_RICHIESTA_INIZIO,
            this.RRB_C.DATA_RICHIESTA_FINE
          ),
        ],
      }
    );
  }

  /**
   * Inizializza le liste delle select
   */
  initListeSelect() {
    // Definisco un'oggetto per il recupero di tutte le informazioni delle liste
    const resources = {
      tipi: this._bollettini.getTipiBollettazione(this._user.idAmbito),
      stati: this._bollettini.getStatiBollettazione(this._user.idAmbito),
    };

    // Recupero il comune se configuratio e le province
    forkJoin(resources).subscribe({
      next: (r: ITipiStatiRIcercaBollettini) => {
        // Estraggo gli oggetti
        const { tipi, stati } = r;
        // Richiamo le funzioni di gestione delle liste
        this.onScaricoTipi(tipi);
        this.onScaricoStati(stati);

        // Verfico che esista l'oggeto del form
        if (this.mainForm) {
          // Richiamo il pristine
          this.mainForm.markAsPristine();
        }
        // Emetto l'evento di fine caricamento
        this.fineCaricamento.emit();
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
   * Funzione che assegna i valori alla select dei tipi bollettini
   * @param tipi TipoElaborazioneVo[] lista tipi bollettini
   */
  onScaricoTipi(tipi: ITipoElaborazioneVo[]) {
    // Assegno localmente le informazioni dei tipi soggetto
    this.listaTipi = tipi;
    // Recupero il tipo soggetto da inserire
    let tipoDefault: ITipoElaborazioneVo;
    // Se ho delle informazioni dalla configurazione, le uso per ottenere il tipo da settare nell'elaborazione
    if (this.ricerca != null) {
      // prendo il codice tipo elabora dalla configurazione
      const codTE = this.ricerca.tipo.cod_tipo_elabora;
      // Cerco e memorizzo il tipo elabora in base al codice
      tipoDefault = this.listaTipi.find((t) => t.cod_tipo_elabora == codTE);
    } else {
      // Cerco il tipo di default se non ho una configurazione di ricerca
      tipoDefault = this.listaTipi.find((t) => t.flg_default == 1);
    }

    // Definisco la funzione di match dati
    const find = (a: ITipoElaborazioneVo, b: ITipoElaborazioneVo) => {
      // Effettuo la verifica
      return a.cod_tipo_elabora === b.cod_tipo_elabora;
    };
    // Assegno il valore
    this._riscaUtilities.setFormValueAndSelect(
      this.mainForm,
      this.RRB_C.TIPO,
      this.listaTipi,
      tipoDefault,
      find
    );
  }

  /**
   * Funzione che assegna i valori alla select degli stati bollettini
   * @param stati StatoElaborazioneVo[] lista degli stati dei bollettini
   */
  onScaricoStati(stati: StatoElaborazioneVo[]) {
    // Assegno localmente le informazioni dei tipi soggetto
    this.listaStati = stati;
    // Recupero il tipo soggetto da inserire
    var statoDefault = this.ricerca
      ? this.listaStati.find(
          (t) => t.cod_stato_elabora == this.ricerca.stato.cod_stato_elabora
        )
      : undefined;

    // Definisco la funzione di match dati
    const find = (a: StatoElaborazioneVo, b: StatoElaborazioneVo) => {
      // Effettuo la verifica
      return a.cod_stato_elabora === b.cod_stato_elabora;
    };
    // Assegno il valore
    this._riscaUtilities.setFormValueAndSelect(
      this.mainForm,
      this.RRB_C.TIPO,
      this.listaTipi,
      statoDefault,
      find
    );
  }

  /**
   * Funzione di comodo che permette di valorizzare i campi della form se viene definito un oggetto di configurazione.
   * @param configs RicercaBollettini con i dati da caricare nel form.
   */
  private initRicercaPraticaFields(configs: IRicercaBollettini) {
    // Verifico l'input
    if (!configs) {
      // Niente da configurare
      return;
    }

    // Lancio la funzione di utility
    this._riscaUtilities.initFormFields(this.mainForm, configs);
  }

  /**
   * Avvia la ricerca dopo la pressione del pulsante.
   */
  avviaRicerca() {
    // Verifico che il form sia corretto
    if (this.mainForm?.valid) {
      // Ottengo i parametri di ricerca
      const dati = this.getMainFormActualRawValue();
      // Emetto i parametri di ricerca al padre che lancerà la ricerca
      this.onFormSubmit$.emit(dati);
    }
  }
}
