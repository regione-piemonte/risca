import { CodIstatNazioni } from './../../../utilities/enums/utilities.enums';
import { Injectable } from '@angular/core';
import { FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { ComuneVo } from '../../../../core/commons/vo/comune-vo';
import { NazioneVo } from '../../../../core/commons/vo/nazione-vo';
import {
  TipoNaturaGiuridica,
  TipoSoggettoVo,
} from '../../../../features/ambito/models';
import { GestioneFormsDatiAnagraficiService } from '../../../../features/pratiche/service/dati-anagrafici/gestione-forms-dati-anagrafici.service';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { RiscaDatiSoggettoConsts } from '../../../consts/risca/risca-dati-soggetto.consts';
import { FormUpdatePropagation } from '../../../utilities';
import {
  AppActions,
  IdNaturaGiuridica,
  IdNazioni,
  IdTipoSoggetto,
  RiscaFormBuilderSize,
} from '../../../utilities/enums/utilities.enums';
import { LocationService } from '../../location.service';
import { RiscaFormBuilderService } from '../risca-form-builder/risca-form-builder.service';
import { typeaheadComuneFormatter } from '../risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../risca-utilities/risca-utilities.service';

/**
 * Servizio di utility con funzionalità di comodo per la gestione degli script per il componente risca-dati-soggetto.
 */
@Injectable({
  providedIn: 'root',
})
export class RiscaDatiSoggettoService {
  /** Oggetto RiscaDatiSoggettoConsts contenente le costanti utilizzate dal componente. */
  RDS_C = RiscaDatiSoggettoConsts;
  /** Oggetto CommonConsts contenente le costanti di uso comune. */
  C_C = new CommonConsts()

  /**
   * Costruttore
   */
  constructor(
    private _location: LocationService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _gestioneFormsDA: GestioneFormsDatiAnagraficiService
  ) {}

  /**
   * Funzione di setup per le input del form.
   * @returns Oggetto contenente le configurazioni dei campi del form.
   */
  setupFormInputs() {
    // Genero il setup per i campi
    const tipoSoggettoConfig = this._riscaFormBuilder.genInputSelect({
      label: this.RDS_C.LABEL_TIPO_SOGGETTO,
      showErrorFG: true,
      showErrorFC: true,
      defaultLabel: this.C_C.SELEZIONA,
    });

    const codiceFiscaleConfig = this._riscaFormBuilder.genInputText({
      label: this.RDS_C.LABEL_CODICE_FISCALE,
      showErrorFG: true,
      showErrorFC: true,
      maxLength: 16,
    });

    const naturaGiuridicaConfig = this._riscaFormBuilder.genInputSelect({
      label: this.RDS_C.LABEL_NATURA_GIURIDICA,
      showErrorFG: true,
      showErrorFC: true,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
    });

    const nomeConfig = this._riscaFormBuilder.genInputText({
      label: this.RDS_C.LABEL_NOME,
      showErrorFG: true,
      showErrorFC: true,
      maxLength: 100,
    });

    const cognomeConfig = this._riscaFormBuilder.genInputText({
      label: this.RDS_C.LABEL_COGNOME,
      showErrorFG: true,
      showErrorFC: true,
      maxLength: 100,
    });

    const ragioneSocialeConfig = this._riscaFormBuilder.genInputText({
      size: RiscaFormBuilderSize.x2,
      label: this.RDS_C.LABEL_RAGIONE_SOCIALE,
      showErrorFG: true,
      showErrorFC: true,
      maxLength: 250,
    });

    const partitaIvaConfig = this._riscaFormBuilder.genInputText({
      label: this.RDS_C.LABEL_PARTITA_IVA,
      showErrorFG: true,
      showErrorFC: true,
      maxLength: 11,
    });

    // Definisco la funzione di recupero dati alla digitazione
    const typeaheadSearch = (v: string) => {
      // Richiamo la funzione di scarico dei comuni
      return this._location.getComuniQry(v, false);
    };
    // Definisco la funzione di mapping dati a seguito della scelta dai consigli
    const typeaheadMap = (c: ComuneVo) => {
      // Richiamo la funzione di comodo per la formattazione
      return typeaheadComuneFormatter(c);
    };
    const comuneDiNascitaConfig = this._riscaFormBuilder.genInputTypeahead({
      label: this.RDS_C.LABEL_COMUNE_NASCITA,
      showErrorFG: true,
      showErrorFC: true,
      searchOnLength: this.C_C.TYPEAHEAD_COMUNE_SEARCH_ON_LENGHT,
      typeaheadSearch,
      typeaheadMap,
      maxLength: 100,
    });

    const provinciaDiNascitaConfig = this._riscaFormBuilder.genInputSelect({
      label: this.RDS_C.LABEL_PROVINCIA_NASCITA,
      showErrorFG: true,
      showErrorFC: true,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
    });

    // Definisco la data massima ad oggi per la data di nascita
    const dateMaggiorenne: any = this._riscaUtilities.minDateMaggiorenne();
    const limitDateMagg =
      this._riscaUtilities.convertMomentToNgbDateStruct(dateMaggiorenne);
    const dataDiNascitaConfig = this._riscaFormBuilder.genInputDatepicker({
      label: this.RDS_C.LABEL_DATA_NASCITA,
      showErrorFG: true,
      showErrorFC: true,
      maxDate: limitDateMagg,
    });

    const statoDiNascitaConfig = this._riscaFormBuilder.genInputSelect({
      label: this.RDS_C.LABEL_STATO_NASCITA,
      showErrorFG: true,
      showErrorFC: true,
    });

    const cittaEsteraNascitaConfig = this._riscaFormBuilder.genInputText({
      label: this.RDS_C.LABEL_CITTA_ESTERA_NASCITA,
      showErrorFG: true,
      showErrorFC: true,
      maxLength: 100,
    });

    return {
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
    };
  }

  /**
   * Funzione che gestisce le logiche per la definizione del disabled dei campi in base a:
   * - Tipo soggetto selezionato;
   * - Natura giuridica selezionata;
   * La funzione, una volta recuperati i dati di cui sopra, andrà a definire i campi disabled.
   * @param f FormGroup contenente i dati del form group.
   * @param m AppActions che definisce la modalità di gestione del form.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  disabledTipoSoggettoNaturaGiurdica(
    f: FormGroup,
    m: AppActions,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !m) return;

    // Recupero il tipo soggetto e natura giuridica
    const { ts } = this.getTipoSoggettoNaturaGiuridica(f);
    // Recupero l'id del tipo soggetto
    const idTs = ts?.id_tipo_soggetto;
    // Verifico se siamo in modifica o in inserimento
    const isIns = m === AppActions.inserimento;
    // Verifico se tipo soggetto è: PG; e natura giuridica è: impresa individuale.
    const checkTSPGNG = this.verificaTSPGNGImpresaIndividuale(f);

    // Verifico le condizioni
    if (!checkTSPGNG) {
      // Definisco la configurazione di aggionramento
      const c = { onlySelf: false, emitEvent: false };
      // Condizioni non verificate, resetto lo stato dei validatori
      this._gestioneFormsDA.setDisabledDatiSoggetto(f, idTs, m, c);
      // #
    } else {
      // Recupero i nomi dei form control
      const fcnN = this.RDS_C.NOME;
      const fcnC = this.RDS_C.COGNOME;
      const fcnDN = this.RDS_C.DATA_NASCITA;
      const fcnSN = this.RDS_C.STATO_NASCITA;
      const fcnCN = this.RDS_C.COMUNE_NASCITA;
      const fcnPN = this.RDS_C.PROVINCIA_NASCITA;
      const fcnCEN = this.RDS_C.CITTA_ESTERA_NASCITA;

      // Verifico se mi trovo in inserimento
      if (isIns) {
        // Solo per l'inserimento abilito nome e cognome
        f.get(fcnN).enable(uC);
        f.get(fcnC).enable(uC);
      }
      // Abilito gli altri campi
      f.get(fcnDN).enable(uC);
      f.get(fcnSN).enable(uC);
      f.get(fcnCN).enable(uC);
      f.get(fcnPN).enable(uC);
      f.get(fcnCEN).enable(uC);
    }
  }

  /**
   * Funzione che gestisce le logiche per la definizione dei validatori dei campi in base a:
   * - Tipo soggetto selezionato;
   * - Natura giuridica selezionata;
   * La funzione, una volta recuperati i dati di cui sopra, andrà a definire i validatori per i campi.
   * @param f FormGroup contenente i dati del form group.
   * @param a AppActions che definisce la modalità di gestione del form.
   * @param u boolean che definisce se il form control deve essere refreshato dopo l'aggiunta dei validatori.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  validatoriTipoSoggettoNaturaGiurdica(
    f: FormGroup,
    a: AppActions,
    u: boolean,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !a) {
      return;
    }

    // Recupero il tipo soggetto e natura giuridica
    const { ts } = this.getTipoSoggettoNaturaGiuridica(f);
    // Recupero l'id del tipo soggetto persona giuridica
    const idTs = ts?.id_tipo_soggetto;

    // Verifico le condizioni
    if (!this.verificaTSPGNGImpresaIndividuale(f)) {
      // Condizioni non verificate, resetto lo stato dei validatori
      this._gestioneFormsDA.setValidatorsDatiSoggetto(f, idTs, a, true, {
        onlySelf: false,
        emitEvent: false,
      });
      // #
    } else {
      // Recupero i nomi dei form control
      const fcnN = this.RDS_C.NOME;
      const fcnC = this.RDS_C.COGNOME;
      const fcnDN = this.RDS_C.DATA_NASCITA;

      // Recupero i validatori
      const vN = this.getValidatoriCampo(fcnN, idTs, a);
      const vC = this.getValidatoriCampo(fcnC, idTs, a);
      const vDN = this.getValidatoriCampo(fcnDN, idTs, a);

      // Aggiungo ai validatori il required
      vN.push(Validators.required);
      vC.push(Validators.required);
      vDN.push(Validators.required);

      // Imposto i validatori nel form
      this.setValidatoriCampo(f, fcnN, vN, u, uC);
      this.setValidatoriCampo(f, fcnC, vC, u, uC);
      this.setValidatoriCampo(f, fcnDN, vDN, u, uC);
    }

    // Gestisco i validatori per lo stato a prescindere dal tipo soggetto
    this.validatoriStato(f, a, u, uC);
  }

  /**
   * Funzione che gestisce le logiche per la definizione dei validatori dei campi in base a:
   * - Stato selezionato;
   * La funzione, una volta recuperati i dati di cui sopra, andrà a definire i validatori per i campi.
   * @param f FormGroup contenente i dati del form group.
   * @param a AppActions che definisce la modalità di gestione del form.
   * @param u boolean che definisce se il form control deve essere refreshato dopo l'aggiunta dei validatori. Default è false.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  validatoriStato(
    f: FormGroup,
    a: AppActions,
    u = false,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !a) {
      return;
    }

    // Recupero il tipo soggetto e natura giuridica
    const { ts } = this.getTipoSoggettoNaturaGiuridica(f);
    // Recupero l'id del tipo soggetto persona giuridica
    const idTs = ts?.id_tipo_soggetto;

    // Definisco i flag per l'obbligo dello stato, comune o citta estera di nascita
    const tSPGNGDI = this.verificaTSPGNGImpresaIndividuale(f);
    const tSPF = idTs === IdTipoSoggetto.PF;

    // Verifico le condizioni
    if (!tSPGNGDI && !tSPF) {
      // Condizioni non verificate, resetto lo stato dei validatori
      this._gestioneFormsDA.setValidatorsDatiSoggetto(f, idTs, a, true, {
        onlySelf: true,
        emitEvent: false,
      });
      // Interrompo le logiche
      return;
    }

    // Recupero i nomi dei form control
    const fcnSN = this.RDS_C.STATO_NASCITA;
    const fcnCN = this.RDS_C.COMUNE_NASCITA;
    const fcnCE = this.RDS_C.CITTA_ESTERA_NASCITA;

    // Recupero i validatori
    const vSN = this.getValidatoriCampo(fcnSN, idTs, a);
    const vCN = this.getValidatoriCampo(fcnCN, idTs, a);
    const vCE = this.getValidatoriCampo(fcnCE, idTs, a);

    // Aggiungo ai validatori il required
    if (!tSPF) {
      vSN.push(Validators.required);
    }

    // Recupero lo stato di nascita
    const sn: NazioneVo = this._riscaUtilities.getFormValue(f, fcnSN);
    // Verifico esista un valore per la nazione
    if (!sn) {
      return;
    }

    // Verifico se la nazione è l'Italia
    const isITA = sn.cod_istat_nazione === CodIstatNazioni.italia;
    // Per comune nascita e città estera verifico lo stato selezionato
    if (isITA) {
      vCN.push(Validators.required);
    } else {
      vCE.push(Validators.required);
    }

    // Imposto i validatori nel form
    this.setValidatoriCampo(f, fcnSN, vSN, u, uC);
    this.setValidatoriCampo(f, fcnCN, vCN, u, uC);
    this.setValidatoriCampo(f, fcnCE, vCE, u, uC);
  }

  /**
   * Funzione che verifica lo stato del tipo soggetto e della natura giuridica.
   * Se tipo soggetto è: Persona Giuridica; e natura giuridica è: ditta individuale; verrà ritornato true.
   * @param f FormGroup contenente i dati del form group.
   * @returns boolean che definisce se le confizioni sono rispettate.
   */
  verificaTSPGNGImpresaIndividuale(f: FormGroup): boolean {
    // Verifico l'input
    if (!f) {
      return false;
    }

    // Recupero il tipo soggetto e natura giuridica
    const { ts, ng } = this.getTipoSoggettoNaturaGiuridica(f);
    // Verifico che i campi siano definiti
    if (!ts || !ng) {
      return false;
    }

    // Recupero gli id per persona giuridica privata e ditta individuale
    const tsPG = ts.id_tipo_soggetto === IdTipoSoggetto.PG;
    const ngDI =
      ng.id_tipo_natura_giuridica === IdNaturaGiuridica.impresaIndividuale;

    // Verifico le condizioni
    return tsPG && ngDI;
  }

  /**
   * Funzione che recupera tipo soggetto e natura giuridica dal FormGroup.
   * @param f FormGroup contenente i dati del form group.
   * @returns { ts: TipoSoggetto; ng: TipoNaturaGiuridica; } con i dati.
   */
  getTipoSoggettoNaturaGiuridica(f: FormGroup): {
    ts: TipoSoggettoVo;
    ng: TipoNaturaGiuridica;
  } {
    // Definisco l'oggetto conenitore
    const data = { ts: null, ng: null };
    // Verifico l'input
    if (!f) {
      return data;
    }

    // Recupero i nomi dei campi del form
    const fcnTs = this.RDS_C.TIPO_SOGGETTO;
    const fcnNg = this.RDS_C.NATURA_GIURIDICA;
    // Recupero il tipo soggetto
    data.ts = this._riscaUtilities.getFormValue(f, fcnTs);
    // Recupero la natura giuridica
    data.ng = this._riscaUtilities.getFormValue(f, fcnNg);
    // Ritorno i dati
    return data;
  }

  /**
   * Funzione di supporto per snellire le righe di codice nei metodi che richiamano le funzionalità.
   * @param fcn string che identifica il nome del form control per il recupero dei validatori.
   * @param idTs number che definisce l'id del tipo soggetto.
   * @param m AppActions che definisce la modalità di gestione del form.
   * @returns Array di ValidatorFn contenenti i validatori.
   */
  private getValidatoriCampo(
    fcn: string,
    idTs: number,
    m: AppActions
  ): ValidatorFn[] {
    // Richiamo la funzione per il ritorno dei validatori
    return this._gestioneFormsDA.getValidatorCampoDatiSoggetto(fcn, idTs, m);
  }

  /**
   * Funzione di supporto per snellire le righe di codice nei metodi che richiamano le funzionalità.
   * @param f FormGroup per l'aggiornamento dati.
   * @param fcn string che identifica il nome del form control per il recupero dei validatori.
   * @param v Array di ValidatorFn che definisce i validatori da impostare per il campo.
   * @param u boolean che definisce se il form control deve essere refreshato dopo l'aggiunta dei validatori. Default è false.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  private setValidatoriCampo(
    f: FormGroup,
    fcn: string,
    v: ValidatorFn[],
    u = false,
    uC?: FormUpdatePropagation
  ) {
    // Richiamo la funzione per il set dei validatori
    this._riscaUtilities.setFieldValidator(f, fcn, v, u, uC);
  }
}
