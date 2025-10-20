import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin, Observable } from 'rxjs';
import { codiceFiscalePerTipoSoggetto } from 'src/app/shared/miscellaneous/forms-validators/soggetto/form-validators.s';
import { NgbDateCustomParserFormatter } from '../../../../core/commons/class/dateformat';
import { ComuneVo } from '../../../../core/commons/vo/comune-vo';
import { NazioneVo } from '../../../../core/commons/vo/nazione-vo';
import { ProvinciaVo } from '../../../../core/commons/vo/provincia-vo';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import {
  TipoNaturaGiuridica,
  TipoSoggettoVo,
} from '../../../../features/ambito/models';
import { GestioneFormsDatiAnagraficiService } from '../../../../features/pratiche/service/dati-anagrafici/gestione-forms-dati-anagrafici.service';
import { SoggettoDatiAnagraficiService } from '../../../../features/pratiche/service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { RiscaDatiSoggettoConsts } from '../../../consts/risca/risca-dati-soggetto.consts';
import { LocationService } from '../../../services/location.service';
import { RiscaDatiSoggettoService } from '../../../services/risca/risca-dati-soggetto/risca-dati-soggetto.service';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import {
  FormUpdatePropagation,
  RiscaAlertConfigs,
  RiscaComponentConfig,
  RiscaDatiSoggetto,
  RiscaFormInputDatepicker,
  RiscaFormInputSelect,
  RiscaFormInputText,
  RiscaFormInputTypeahead,
  RiscaServerError,
} from '../../../utilities';
import { CodiceTipoSoggetto } from '../../../utilities/enums/utilities.enums';
import { GestioneAnagraficaComponent } from '../risca-dati-anagrafici/gestione-anagrafica.component';

/**
 * Interfaccia di comodo per lo scarico delle liste del componente. Formato: request.
 */
interface IListeDSReq {
  tipiSoggetto: Observable<TipoSoggettoVo[]>;
  stati: Observable<NazioneVo[]>;
  comune: Observable<ComuneVo>;
  province: Observable<ProvinciaVo[]>;
}

/**
 * Interfaccia di comodo per lo scarico delle liste del componente. Formato: response.
 */
interface IListeDSRes {
  tipiSoggetto: TipoSoggettoVo[];
  stati: NazioneVo[];
  comune: ComuneVo;
  province: ProvinciaVo[];
}

@Component({
  selector: 'risca-dati-soggetto',
  templateUrl: './risca-dati-soggetto.component.html',
  styleUrls: ['./risca-dati-soggetto.component.scss'],
  providers: [
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
  ],
})
export class RiscaDatiSoggettoComponent
  extends GestioneAnagraficaComponent<RiscaDatiSoggetto>
  implements OnInit, OnChanges
{
  /** Costante RiscaDatiSoggettoConsts contenente le costanti utilizzate dal componente. */
  RDS_C = RiscaDatiSoggettoConsts;

  /** Oggetto di configurazione per il campo: tipo soggetto. */
  tipoSoggettoConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: codice fiscale. */
  codiceFiscaleConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: naturaGiuridica. */
  naturaGiuridicaConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: nome. */
  nomeConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: cognome. */
  cognomeConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: ragioneSociale. */
  ragioneSocialeConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: partitaIva. */
  partitaIvaConfig: RiscaComponentConfig<RiscaFormInputText>;
  /** Oggetto di configurazione per il campo: comuneDiNascita. */
  comuneDiNascitaConfig: RiscaComponentConfig<RiscaFormInputTypeahead>;
  /** Oggetto di configurazione per il campo: provinciaDiNascita. */
  provinciaDiNascitaConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione per il campo: dataDiNascita. */
  dataDiNascitaConfig: RiscaComponentConfig<RiscaFormInputDatepicker>;
  /** Oggetto di configurazione per il campo: statoDiNascita. */
  statoDiNascitaConfig: RiscaComponentConfig<RiscaFormInputSelect>;
  /** Oggetto di configurazione alternativo per il campo: cittaDiNascita. */
  cittaEsteraNascitaConfig: RiscaComponentConfig<RiscaFormInputText>;

  /** Lista di oggetti TipoSoggetto contenente le informazioni per i tipi soggetto. */
  listaTipiSoggetto: TipoSoggettoVo[] = [];
  /** Lista di oggetti TipoNaturaGiuridica contenente le informazioni per le nature giuridiche. */
  listaNatureGiuridiche: TipoNaturaGiuridica[] = [];
  /** Lista di oggetti NazioneVo contenente le informazioni per gli stati. */
  listaStati: NazioneVo[] = [];
  /** Lista di oggetti ProvinciaVo contenente le informazioni per le province. */
  listaProvince: ProvinciaVo[] = [];

  /**
   * Costruttore.
   */
  constructor(
    private _formBuilder: FormBuilder,
    private _gestioneFormsDA: GestioneFormsDatiAnagraficiService,
    private _location: LocationService,
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    private _riscaDatiSoggetto: RiscaDatiSoggettoService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaUtilities: RiscaUtilitiesService,
    soggettoDA: SoggettoDatiAnagraficiService
  ) {
    // Richiamo il super
    super(
      logger,
      navigationHelper,
      riscaFormSubmitHandler,
      riscaUtilities,
      soggettoDA
    );
    // Funzione di setup generico
    this.setupComponente();
  }

  ngOnInit(): void {
    // Lancio il setup delle form
    this.initForms();
    // Setup delle liste per le select del form
    this.initListeSelect();
    // Funzione di init del componente
    this.initComponent(false, false, false);
  }

  ngOnChanges(changes: SimpleChanges) {
    // Verifico se la modalità è stata cambiata
    if (changes.modalita && !changes.modalita.firstChange) {
      // Richiamo i validatori
      this.initValidators();
      this.updateValidatorsTSNG();
      this.updateStatoValidator();
      this.initDisabledFields();
      this.updateDisabledFieldsTSNG();
    }
  }

  /**
   * Funzione di comodo che racchiude le funzioni di setup per il componente.
   */
  private setupComponente() {
    // Attivo il controllo pre-invio dei dati
    this.checkValueValidity = true;

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
    const {
      tipoSoggettoConfig,
      codiceFiscaleConfig,
      naturaGiuridicaConfig,
      nomeConfig,
      cognomeConfig,
      ragioneSocialeConfig,
      partitaIvaConfig,
      comuneDiNascitaConfig,
      provinciaDiNascitaConfig,
      dataDiNascitaConfig,
      statoDiNascitaConfig,
      cittaEsteraNascitaConfig,
    } = this._riscaDatiSoggetto.setupFormInputs();

    // Assegno locamente il setup
    this.tipoSoggettoConfig = tipoSoggettoConfig;
    this.codiceFiscaleConfig = codiceFiscaleConfig;
    this.naturaGiuridicaConfig = naturaGiuridicaConfig;
    this.nomeConfig = nomeConfig;
    this.cognomeConfig = cognomeConfig;
    this.ragioneSocialeConfig = ragioneSocialeConfig;
    this.partitaIvaConfig = partitaIvaConfig;
    this.comuneDiNascitaConfig = comuneDiNascitaConfig;
    this.provinciaDiNascitaConfig = provinciaDiNascitaConfig;
    this.dataDiNascitaConfig = dataDiNascitaConfig;
    this.statoDiNascitaConfig = statoDiNascitaConfig;
    this.cittaEsteraNascitaConfig = cittaEsteraNascitaConfig;
  }

  /**
   * Funzione di setup per le liste per le select del form.
   */
  private initListeSelect() {
    // Definisco un'oggetto per il recupero di tutte le informazioni delle liste
    const resources: IListeDSReq = {
      tipiSoggetto: this._soggettoDA.getTipiSoggetto(),
      stati: this._location.getNazioniAll(),
      comune: this.scaricoComune(false),
      province: this._location.getProvinceAll(),
    };

    // Recupero il comune se configuratio e le province
    forkJoin(resources).subscribe({
      next: (r: IListeDSRes) => {
        // Estraggo gli oggetti
        const { tipiSoggetto, stati, comune, province } = r;
        // Richiamo le funzioni di gestione delle liste
        this.onScaricoTipiSoggetto(tipiSoggetto);
        this.onScaricoStati(stati);
        this.onScaricoComune(comune, province);

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
   * Funzione a se stante per lo scarico delle nature giuridiche e la sua gestione.
   * @param codTS string definisce il codice tipo soggetto per la ricerca delle nature giuridiche.
   */
  private scaricaNatureGiuridiche(codTS: string) {
    // Verifico l'input
    if (!codTS) {
      // Loggo un warning
      const title = 'scaricaNatureGiuridiche';
      const body = 'Codice tipo soggetto non impostato';
      this._logger.warning(title, body);

      // Aggiorno le nature giuridiche con valore vuoto
      this.onScaricoNatureGiuridiche([]);
      // Aggiorno valodatori e campi disabilitati
      this.updateValidatorsTSNG();
      this.updateDisabledFieldsTSNG();
      // Blocco il flusso
      return;
    }

    // Richiamo la funzione di scarico nature giurdiche
    this._soggettoDA.getTipiNaturaGiuridicaByTipoSoggetto(codTS).subscribe({
      next: (ng: TipoNaturaGiuridica[]) => {
        // Richiamo il gestore
        this.onScaricoNatureGiuridiche(ng);
        // Aggiorno valodatori e campi disabilitati
        this.updateValidatorsTSNG();
        this.updateDisabledFieldsTSNG();
      },
      error: (e: any) => {
        // Loggo l'errore
        this._logger.error('scaricaNatureGiuridiche', e);
      },
    });
  }

  /**
   * Funzione invocata allo scarico dei tipi soggetto.
   * @param tipiSoggetto Array di TipoSoggetto scaricati.
   */
  private onScaricoTipiSoggetto(tipiSoggetto: TipoSoggettoVo[]) {
    // Assegno localmente le informazioni dei tipi soggetto
    this.listaTipiSoggetto = tipiSoggetto;
    // Setup iniziale
    this.initTipoSoggetto();

    // Recupero il tipo soggetto
    const tipoSoggetto = this._riscaUtilities.getFormValue(
      this.mainForm,
      this.RDS_C.TIPO_SOGGETTO
    );
    // Aggiorno i dati collegati al tipo soggetto
    this.tipoSoggettoSelected(tipoSoggetto);
  }

  /**
   * Funzione invocata allo scarico delle nature giuridiche.
   * @param natureGiuridiche Array di TipoNaturaGiuridica scaricate.
   */
  private onScaricoNatureGiuridiche(natureGiuridiche: TipoNaturaGiuridica[]) {
    // Assegno le nature giuridiche
    this.listaNatureGiuridiche = natureGiuridiche || [];
    // Definisco un default
    this._soggettoDA.setNaturaGiuridicaDefault(
      this.mainForm,
      this.RDS_C.NATURA_GIURIDICA,
      this.listaNatureGiuridiche,
      this.datiAnagraficiConfig?.tipo_natura_giuridica
    );

    // Gestisco lo stato del campo
    this.abilitazioneNaturaGiuridica();
  }

  /**
   * Funzione di supporto che gestisce le logiche per l'abilitazione del campo: natura giuridica.
   */
  private abilitazioneNaturaGiuridica() {
    // Verifico le condizioni per la disabilitazione dei campi
    const formEnabled = !this.mainForm.disabled;
    // Verifico lo stato del form
    if (formEnabled) {
      // La gestione dei campi non è da verificare
      return;
    }
    // Se la form è disabilitata da configurazione, non effettuo altre funzionalità sulle abilitazioni
    if (this.isFormDisabled) {
      // Disabilito la form
      return;
    }

    // Verifico il singolo stato per la natura giuridica
    const lngExist = this.listaNatureGiuridiche?.length > 0;
    // Se non ci sono nature giuridiche blocco il campo
    if (lngExist) {
      this.mainForm.get(this.RDS_C.NATURA_GIURIDICA).enable();
      // #
    } else {
      this.mainForm.get(this.RDS_C.NATURA_GIURIDICA).disable();
    }
  }

  /**
   * Funzione invocata allo scarico delle nature giuridiche.
   * @param stati Array di NazioneVo scaricati.
   */
  private onScaricoStati(stati: NazioneVo[]) {
    // Assegno localmente la lista
    this.listaStati = stati;
    // Setup iniziale
    this.initStato();
  }

  /**
   * Funzione che aggiorna il dato del comune, partendo dalla configurazione iniziale.
   */
  private aggiornaComune() {
    // Scarico il comune
    this.scaricoComune(true).subscribe({
      next: (comune: ComuneVo) => {
        // Richiamo il setup
        this.onScaricoComune(comune, this.listaProvince);
        // #
      },
      error: (e: RiscaServerError) => {
        // Loggo l'errore
        this._logger.error('onFormRestore', e);
        // #
      },
    });
  }

  /**
   * Funzione che imposta le informazioni per lo scarico del comune.
   * @param attivo boolean che definisce se ricercare solo i comuni attivi (true) o tutti i comuni (false).
   */
  private scaricoComune(attivo: boolean): Observable<ComuneVo> {
    // Tento di recuperare il comune configurato
    const comuneNascita = this.datiAnagraficiConfig?.comune_nascita;
    // Definisco i parametri per le chiamate
    const ciCN = comuneNascita?.cod_istat_comune;

    // Ritorno la chiamata al servizio
    if (attivo) {
      // Richiamo lo scarico comune tra gli attivi
      return this._location.getComuneAttivo(ciCN);
      // #
    } else {
      // Richiamo lo scarico comune tra tutti i comuni
      return this._location.getComuneAll(ciCN);
      // #
    }
  }

  /**
   * Funzione invocata allo scarico del comune e delle province.
   * @param comune Comune scaricato.
   * @param province Array di ProvinciaVo scaricate.
   */
  private onScaricoComune(comune: ComuneVo, province: ProvinciaVo[]) {
    // Setto il valore per la lista delle province
    this.listaProvince = province;

    // Se esiste il comune recupero l'id provincia
    const idProvincia = comune?.id_provincia;
    // Lancio il setup del comune
    this.initComune(comune);
    // Lancio l'init della provincia
    this.initProvincia(idProvincia);
  }

  /**
   * Funzione che effettua il setup per la costruzione e valorizzazione delle form in pagina.
   * ATTENZIONE: questa funzionalità deve essere richiamata esclusivamente all'interno dell'NgOnInit hook di Angular.
   * Richimando questa funzione all'interno del costruttore della classe, si verificheranno errori sulla definizione dei validatori personalizzati.
   */
  private initForms() {
    // Setup del form
    this.initDatiSoggettoForm();
    // Setup dei listiner dei form per la manipolazione dei dati
    this.initFormListener();
    // Setup dei campi form in base alla parametrizzazione degli input
    this.initCampiForm();
    // Lancio il setup per i validatori dei campi
    this.initValidators();
    // Lancio il setup per i campi disabled
    this.initDisabledFields();
  }

  /**
   * Setup del form.
   */
  private initDatiSoggettoForm() {
    // Inizializzo la form
    this.mainForm = this._formBuilder.group(
      {
        tipoSoggetto: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        codiceFiscale: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        naturaGiuridica: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        nome: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        cognome: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        ragioneSociale: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        partitaIva: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        comuneDiNascita: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        dataDiNascita: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        statoDiNascita: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        cittaEsteraNascita: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
        provinciaDiNascita: new FormControl(
          { value: null, disabled: false },
          { validators: [] }
        ),
      },
      {
        validators: [
          codiceFiscalePerTipoSoggetto(
            this.RDS_C.TIPO_SOGGETTO,
            this.RDS_C.CODICE_FISCALE
          ),
        ],
      }
    );

    // Verifico i flag
    if (this.isFormDisabled) {
      // Disabilito il form
      this.mainForm.disable();
    }
  }

  /**
   * Funzione che assegna ai campi del form, le validazioni per i dati anagrafici.
   * E' importante che il FormGroup sia già stato instanziato.
   * @param update boolean che definisce se è necessario ricaricare e ri-validare la form a seguito del cambio dei validatori.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  private initValidators(update?: boolean, uC?: FormUpdatePropagation) {
    // Verifico il flag per la gestione abilitata
    if (this.isMainFormDisabled) {
      // Non faccio partire la gestione dei validatori
      return;
    }

    // Recupero il tipo soggetto
    const idTs = this.idTipoSoggetto;
    // Richiamo la funzione per la gestione dei validatori
    this._gestioneFormsDA.setValidatorsDatiSoggetto(
      this.mainForm,
      idTs,
      this.modalita,
      update,
      uC
    );
  }

  /**
   * Funzione che assegna ai campi legati a tipo soggetto/natura giuridica, le validazioni per tutti i dati anagrafici.
   * E' importante che il FormGroup sia già stato instanziato.
   * @param update boolean che definisce se è necessario ricaricare e ri-validare la form a seguito del cambio dei validatori.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  private updateValidatorsTSNG(
    update = true,
    uC: FormUpdatePropagation = { onlySelf: true, emitEvent: false }
  ) {
    // Verifico il flag per la gestione abilitata
    if (this.isMainFormDisabled) {
      // Non faccio partire la gestione dei validatori
      return;
    }

    // Lancio i validatori a seguito della selezione del tipo soggetto
    this._riscaDatiSoggetto.validatoriTipoSoggettoNaturaGiurdica(
      this.mainForm,
      this.modalita,
      update,
      uC
    );
  }

  /**
   * Funzione che assegna ai campi del form, le validazioni per i validatori dello stato.
   * E' importante che il FormGroup sia già stato instanziato.
   * @param update boolean che definisce se è necessario ricaricare e ri-validare la form a seguito del cambio dei validatori.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  private updateStatoValidator(
    update = true,
    uC: FormUpdatePropagation = { onlySelf: false, emitEvent: false }
  ) {
    // Lancio i validatori a seguito della selezione del tipo soggetto
    this._riscaDatiSoggetto.validatoriStato(
      this.mainForm,
      this.modalita,
      update,
      uC
    );
  }

  /**
   * Funzione che assegna ai campi del form lo stato di disable per i dati anagrafici.
   * E' importante che il FormGroup sia già stato instanziato.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  private initDisabledFields(uC?: FormUpdatePropagation) {
    // Verifico le condizioni per la disabilitazione dei campi
    if (this.checkRDSFormDisabledIsGA) {
      // Non faccio partire la gestione della disabilitazione
      return;
    }

    // Se la form è disabilitata da configurazione, non effettuo altre funzionalità sulle abilitazioni
    if (this.isFormDisabled) {
      // Disabilito la form
      return;
    }

    // Recupero il tipo soggetto
    const idTs = this.idTipoSoggetto;
    // Richiamo la funzione per la gestione dei campi disabilitati
    this._gestioneFormsDA.setDisabledDatiSoggetto(
      this.mainForm,
      idTs,
      this.modalita
    );
  }

  /**
   * Funzione che assegna ai campi del form lo stato di disable per i dati anagrafici.
   * E' importante che il FormGroup sia già stato instanziato.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  private updateDisabledFieldsTSNG(
    uC: FormUpdatePropagation = { onlySelf: true, emitEvent: false }
  ) {
    // Verifico le condizioni per la disabilitazione dei campi
    if (this.checkRDSFormDisabledIsGA) {
      // Non faccio partire la gestione della disabilitazione
      return;
    }

    // Se la form è disabilitata da configurazione, non effettuo altre funzionalità sulle abilitazioni
    if (this.isFormDisabled) {
      // Form disabilitato, blocco il flusso
      return;
    }

    // Definisco variabili di comodo
    const f = this.mainForm;
    const m = this.modalita;
    // Lancio i validatori a seguito della selezione del tipo soggetto
    this._riscaDatiSoggetto.disabledTipoSoggettoNaturaGiurdica(f, m, uC);
  }

  /**
   * Funzione specifica adibita al setup dei listner delle form.
   * La funzione registrerà diversi eventi sulle form, atte a gestire la logica e la manipolazione dei dati.
   */
  private initFormListener() {
    // Registro un ascoltatore per il cambio del tipo soggetto
    this.mainForm.get(this.RDS_C.TIPO_SOGGETTO).valueChanges.subscribe({
      next: (tipoSoggetto: TipoSoggettoVo) => {
        // Aggiorno i dati relativi al tipo soggetto
        this.tipoSoggettoSelected(tipoSoggetto);
      },
    });
    // Registro un ascoltatore per il cambio della natura giuridica
    this.mainForm.get(this.RDS_C.NATURA_GIURIDICA).valueChanges.subscribe({
      next: (naturaGiuridica: TipoNaturaGiuridica) => {
        // Aggiorno i validatori basati su tipo soggetto/natura giuridica
        this.updateValidatorsTSNG(true, { onlySelf: true, emitEvent: false });
        // Aggiorno i campi disabilitati
        this.updateDisabledFieldsTSNG();
      },
    });
    // Registro un ascoltatore per il cambio del comune
    this.mainForm.get(this.RDS_C.COMUNE_NASCITA).valueChanges.subscribe({
      next: (comune: ComuneVo) => {
        // Aggiorno i dati relativi al comune
        this.comuneSelected(comune);
      },
    });
    // Registro un ascoltatore per il cambio dello stato
    this.mainForm.get(this.RDS_C.STATO_NASCITA).valueChanges.subscribe({
      next: (stato: NazioneVo) => {
        // Aggiorno i dati relativi allo stato
        this.statoSelected(stato);
      },
    });
  }

  /**
   * Funzione di setup dei campi del form in base alla parametrizzazione degli input.
   * @param isReset boolean che definisce che è richiesto un reset totale.
   */
  private initCampiForm(isReset?: boolean) {
    // Recupero dal componente i dati anagrafici
    const datiAnagrafici = this.datiAnagraficiConfig;

    // Verifico la configurazione per partita iva
    if (datiAnagrafici?.partita_iva_soggetto && !isReset) {
      // Setto il valore
      this.initCampoForm(
        this.RDS_C.PARTITA_IVA,
        datiAnagrafici.partita_iva_soggetto
      );
    }
    // Verifico la configurazione per ragione sociale
    if (datiAnagrafici?.den_soggetto && !isReset) {
      // Setto il valore
      this.initCampoForm(
        this.RDS_C.RAGIONE_SOCIALE,
        datiAnagrafici.den_soggetto
      );
    }
    // Verifico la configurazione per cognome
    if (datiAnagrafici?.cognome && !isReset) {
      // Setto il valore
      this.initCampoForm(this.RDS_C.COGNOME, datiAnagrafici.cognome);
    }
    // Verifico la configurazione per nome
    if (datiAnagrafici?.nome && !isReset) {
      // Setto il valore
      this.initCampoForm(this.RDS_C.NOME, datiAnagrafici.nome);
    }
    // Verifico la configurazione per data di nascita
    if (datiAnagrafici?.data_nascita_soggetto && !isReset) {
      // Setto il valore
      this.initCampoForm(
        this.RDS_C.DATA_NASCITA,
        this._riscaUtilities.convertServerDateToNgbDateStruct(
          datiAnagrafici.data_nascita_soggetto
        )
      );
    }
    // Verifico la configurazione per città estera
    if (datiAnagrafici?.citta_estera_nascita && !isReset) {
      // Setto il valore
      this.initCampoForm(
        this.RDS_C.CITTA_ESTERA_NASCITA,
        datiAnagrafici.citta_estera_nascita
      );
    }
  }

  /**
   * Funzione di comodo usato per l'init dei campi del form.
   * @param campo string form control name per il set del valore.
   * @param valore any valore da settare.
   */
  private initCampoForm(campo: string, valore: any) {
    // Imposto il campo secondo i parametri
    this._riscaUtilities.setFormValue(this.mainForm, campo, valore);
  }

  /**
   * Funzione di init del componente.
   * @param isReset boolean che specifica se è stato richiamato un reset della form.
   * @param uV boolean che definisce se è necessario aggiornare tutti i validatori. Default: true.
   * @param uDF boolean che definisce se è necessario aggiornare i campi disattivati. Default: true.
   */
  private initComponent(isReset?: boolean, uV = true, uDF = true) {
    // Verifico se esiste il codice fiscale
    if (this.datiAnagraficiConfig?.cf_soggetto && !isReset) {
      // Setto nel form il dato del codice fiscale
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RDS_C.CODICE_FISCALE,
        this.datiAnagraficiConfig?.cf_soggetto
      );
    }

    // Aggiorno i validatori
    if (uV) {
      this.updateValidatorsTSNG();
    }
    // Aggiorno la disabilitazione dei campi
    if (uDF) {
      this.updateDisabledFieldsTSNG();
    }
  }

  /**
   * Funzione di setup per il tipo soggetto.
   */
  private initTipoSoggetto() {
    // Setup per il valore di default del form
    this._soggettoDA.setTipoSoggettoDefault(
      this.mainForm,
      this.RDS_C.TIPO_SOGGETTO,
      this.listaTipiSoggetto,
      this.datiAnagraficiConfig?.tipo_soggetto?.cod_tipo_soggetto
    );
    // Gestisco lo stato del campo
    this.abilitazioneTipoSoggetto();
  }

  /**
   * Funzione di supporto che gestisce le logiche per l'abilitazione del campo: tipo soggetto.
   */
  private abilitazioneTipoSoggetto() {
    // Recupero il codice tipo soggetto
    const codTipoSoggetto =
      this.datiAnagraficiConfig?.tipo_soggetto?.cod_tipo_soggetto || '';

    // Verifico le condizioni per la disabilitazione dei campi
    const isSPF = codTipoSoggetto === CodiceTipoSoggetto.PF;
    const formEnabled = !this.mainForm.disabled;

    // Setup per il campo natura giuridica
    if (isSPF && formEnabled) {
      // Disabilito il campo natura giuridica
      this.mainForm.get(this.RDS_C.NATURA_GIURIDICA).disable();
    }
  }

  /**
   * Funzione di setup per lo stato.
   * @param isReset boolean che definisce che è richiesto un reset totale.
   */
  private initStato(isReset?: boolean) {
    // Recupero l'id dello stato
    let codNazione: string;
    // Verifico se è stato richiesto un reset
    if (isReset) {
      // Imposto la nazione di default
      codNazione = this.C_C.COD_NAZ_ITA;
      // #
    } else {
      // Tento di recuperare lo stato di nascita
      const statoNascita = this.datiAnagraficiConfig?.nazione_nascita;
      // Verifico se esiste una configurazione per il default
      codNazione = statoNascita?.cod_istat_nazione || this.C_C.COD_NAZ_ITA;
    }

    // Setup iniziale
    this._location.setNazioneDefault(
      this.mainForm,
      this.RDS_C.STATO_NASCITA,
      this.listaStati,
      codNazione
    );
  }

  /**
   * Funzione di setup per il comune
   * @param comune Comune contente i dati del comune.
   */
  private initComune(comune: ComuneVo) {
    // Verifico l'input
    if (!comune) {
      return;
    }

    // Imposto il comune nel form
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.RDS_C.COMUNE_NASCITA,
      comune
    );
  }

  /**
   * Funzione di setup per la nazione.
   * @param idProvincia number che definisce l'id della provincia, se non definito il dato verrà resettato.
   */
  private initProvincia(idProvincia?: number) {
    // Verifico se è un reset
    if (!idProvincia) {
      // Aggiorno il valore della provincia
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RDS_C.PROVINCIA_NASCITA,
        null
      );
      // Blocco il flusso
      return;
    }

    // Setup iniziale
    this._location.setProvinciaDefault(
      this.mainForm,
      this.RDS_C.PROVINCIA_NASCITA,
      this.listaProvince,
      idProvincia
    );
  }

  /**
   * #####################
   * METODI DEL COMPONENTE
   * #####################
   */

  /**
   * Funzione che gestisce i dati relativi al tipo soggetto, una volta che questo è selezionato.
   * @param tipoSoggetto TipoSoggetto selezionato.
   */
  private tipoSoggettoSelected(tipoSoggetto: TipoSoggettoVo) {
    // Lancio la gestione della natura giuridica
    this.gestisciNaturaGiuridica(tipoSoggetto);
  }

  /**
   * Funzione che gestisce le logiche per la natura giuridica in base al tipo soggetto.
   * @param tipoSoggetto TipoSoggetto selezionato.
   */
  private gestisciNaturaGiuridica(tipoSoggetto: TipoSoggettoVo) {
    // Resetto i valori per natura giuridica e lista nature giuridiche
    this._riscaUtilities.setFormValue(
      this.mainForm,
      this.RDS_C.NATURA_GIURIDICA,
      null
    );
    this.listaNatureGiuridiche = [];

    // Richiamo la funzione per scaricare i dati della natura giuridica
    this.scaricaNatureGiuridiche(tipoSoggetto?.cod_tipo_soggetto);
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
      this.RDS_C.PROVINCIA_NASCITA,
      this.listaProvince,
      comune
    );
  }

  /**
   * Funzione che gestisce i dati relativi allo stato, una volta che questo è stato selezionato.
   * @param stato NazioneVo selezionato.
   */
  private statoSelected(stato: NazioneVo) {
    // Verifico l'input
    if (!stato) return;

    // Verifico se lo stato selezionato è l'italia
    if (stato.cod_istat_nazione === this.C_C.COD_NAZ_ITA) {
      // Resetto le informazioni
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RDS_C.CITTA_ESTERA_NASCITA,
        null
      );
      // #
    } else {
      // Resetto le informazioni
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RDS_C.COMUNE_NASCITA,
        null
      );
      this._riscaUtilities.setFormValue(
        this.mainForm,
        this.RDS_C.PROVINCIA_NASCITA,
        null
      );
    }

    // Gestisco i validatori per lo stato
    this.updateStatoValidator();
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

    // Rilancio i setup/init del componente
    this.initCampiForm(true);
    this.initTipoSoggetto();
    this.initStato(true);
    this.initComponent(true);
  }

  /**
   * Funzione di restore dei dati manuale richiamabile dal componente padre.
   * La restore permette di resettare completamente tutti i dati, oppure mantenere tipo soggetto, codice fiscale.
   */
  onFormRestore(onlyTSCF: boolean = false) {
    // Resetto il flag del form submitted
    this.mainFormSubmitted = false;

    // Verifico la modalità
    if (onlyTSCF) {
      // Resetto la form
      this.mainForm.reset();
      // Restore del tipo soggetto
      this.initTipoSoggetto();
      // Restore del codice fiscale
      this.initComponent(false, false, false);
      // #
    } else {
      // Rilancio i setup/init del componente
      this.initCampiForm();
      this.initStato();
      this.aggiornaComune();
    }
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter di comodo per verificare se il form è completamente disabilitato da configurazione.
   */
  get isFormDisabled() {
    // Verifico il flag di abilitazione per "isGestioneAbilitata"
    const disabledIGA = !this.isGestioneAbilitata;
    // Verifico i flag di abilitazione per "accesso elementi app"
    const disabledAEA =
      this.insModSoggettoDisabled || this.praticaSoggettoDisabled;

    // Verifico i flag e ritorno la condizione
    return disabledIGA || disabledAEA;
  }

  /**
   * Getter di comodo per verificare se nel form è impostato lo stato: Italia.
   */
  get fStatoItalia() {
    // Verifico lo stato settato se è italia
    return this._location.isNazioneInForm(
      this.mainForm,
      this.RDS_C.STATO_NASCITA,
      this.C_C.COD_NAZ_ITA
    );
  }

  /**
   * Getter per l'id tipo soggetto selezionato.
   */
  get idTipoSoggetto() {
    // Recupero il tipo soggetto
    const tipoSoggetto: TipoSoggettoVo = this._riscaUtilities.getFormValue(
      this.mainForm,
      this.RDS_C.TIPO_SOGGETTO
    );
    // Ritorno l'id tipo soggetto
    return tipoSoggetto?.id_tipo_soggetto;
  }

  /**
   * Getter di comodo per il check sulle configurazioni principali del componente.
   */
  get checkRDSFormDisabledIsGA() {
    // Verifico le condizioni per la disabilitazione dei campi
    const flagGA = !this.isGestioneAbilitata;
    const formDisabled = this.mainForm.disabled;

    // Verifico il flag per la gestione abilitata
    return flagGA || formDisabled;
  }

  /**
   * Getter di comodo per il check sullo stato di disabilitazione del main form.
   */
  get isMainFormDisabled() {
    // Verifico il flag per la gestione abilitata
    return this.mainForm != undefined ? this.mainForm.disabled : true;
  }
}
