import { CodIstatNazioni } from './../../../utilities/enums/utilities.enums';
import { Injectable } from '@angular/core';
import { FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { ComuneVo } from '../../../../core/commons/vo/comune-vo';
import { NazioneVo } from '../../../../core/commons/vo/nazione-vo';
import { GestioneFormsDatiAnagraficiService } from '../../../../features/pratiche/service/dati-anagrafici/gestione-forms-dati-anagrafici.service';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { RiscaRecapitoConsts } from '../../../consts/risca/risca-recapito.consts';
import { exactLengthValidator } from '../../../miscellaneous/forms-validators/forms-validators';
import { AppActions, FormUpdatePropagation } from '../../../utilities';
import {
  IdNazioni,
  RiscaFormBuilderSize,
} from '../../../utilities/enums/utilities.enums';
import { LocationService } from '../../location.service';
import { RiscaFormBuilderService } from '../risca-form-builder/risca-form-builder.service';
import { typeaheadComuneFormatter } from '../risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../risca-utilities/risca-utilities.service';

/**
 * Servizio di utility con funzionalità di comodo per la gestione degli script per il componente risca-recapito.
 */
@Injectable({
  providedIn: 'root',
})
export class RiscaRimborsoService {
  /** Oggetto RiscaRecapitoConsts contenente le costanti utilizzate dal componente. */
  RR_C = RiscaRecapitoConsts;
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
    const tipoSedeConfig = this._riscaFormBuilder.genInputSelect({
      label: this.RR_C.LABEL_TIPO_SEDE,
      showErrorFC: true,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
    });

    const pressoConfig = this._riscaFormBuilder.genInputText({
      size: RiscaFormBuilderSize.x2,
      label: this.RR_C.LABEL_PRESSO,
      maxLength: 150,
    });

    const statoConfig = this._riscaFormBuilder.genInputSelect({
      label: this.RR_C.LABEL_STATO,
      showErrorFC: true,
    });

    // Definisco la funzione di recupero dati alla digitazione
    const typeaheadSearch = (v: string) => {
      // Richiamo la funzione di scarico dei comuni
      return this._location.getComuniQry(v, true);
    };
    // Definisco la funzione di mapping dati a seguito della scelta dai consigli
    const typeaheadMap = (c: ComuneVo) => {
      // Richiamo la funzione di comodo per la formattazione
      return typeaheadComuneFormatter(c);
    };
    const comuneConfig = this._riscaFormBuilder.genInputTypeahead({
      label: this.RR_C.LABEL_COMUNE,
      showErrorFC: true,
      typeaheadSearch,
      typeaheadMap,
      maxLength: 100,
    });

    const provinciaConfig = this._riscaFormBuilder.genInputSelect({
      label: this.RR_C.LABEL_PROVINCIA,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
    });

    const cittaEsteraRecapitoConfig = this._riscaFormBuilder.genInputText({
      label: this.RR_C.LABEL_CITTA_ESTERA_RECAPITO,
      showErrorFC: true,
      maxLength: 100,
    });

    const localitaConfig = this._riscaFormBuilder.genInputText({
      label: this.RR_C.LABEL_LOCALITA,
      maxLength: 250,
    });

    const indirizzoConfig = this._riscaFormBuilder.genInputText({
      label: this.RR_C.LABEL_INDIRIZZO,
      showErrorFC: true,
      maxLength: 100,
    });

    const numeroCivicoConfig = this._riscaFormBuilder.genInputText({
      size: RiscaFormBuilderSize.small,
      label: this.RR_C.LABEL_NUMERO_CIVICO,
      showErrorFC: true,
      maxLength: 30,
    });

    const capConfig = this._riscaFormBuilder.genInputText({
      size: RiscaFormBuilderSize.small,
      label: this.RR_C.LABEL_CAP,
      showErrorFC: true,
      maxLength: 10,
      onlyNumber: true,
    });

    return {
      tipoSedeConfig,
      pressoConfig,
      statoConfig,
      comuneConfig,
      provinciaConfig,
      cittaEsteraRecapitoConfig,
      localitaConfig,
      indirizzoConfig,
      numeroCivicoConfig,
      capConfig,
    };
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

    // Variabili di comodo
    const capMaxLIT = this.C_C.CAP_MAX_LENGTH_ITA;
    const capMaxLEst = this.C_C.CAP_MAX_LENGTH_EST;

    // Recupero i nomi dei form control
    const fcnSN = this.RR_C.STATO;
    const fcnCN = this.RR_C.COMUNE;
    const fcnCE = this.RR_C.CITTA_ESTERA_RECAPITO;
    const fcnCAP = this.RR_C.CAP;

    // Recupero i validatori
    const vCN = this.getValidatoriCampo(fcnCN, a);
    const vCE = this.getValidatoriCampo(fcnCE, a);
    const vCAP = this.getValidatoriCampo(fcnCAP, a);

    // Recupero lo stato di nascita
    const sn: NazioneVo = this._riscaUtilities.getFormValue(f, fcnSN);
    // Verifico esista un valore per la nazione
    if (!sn) {
      return;
    }

    // Verifico se la nazione è l'Italia
    const isITA = sn.cod_istat_nazione === CodIstatNazioni.italia;

    // Verifico lo stato e gestisco i validatori
    if (isITA) {
      // Definisco i validatori dipendenti rispetto allo stato
      vCN.push(Validators.required);
      vCAP.push(exactLengthValidator(capMaxLIT, true));
      // #
    } else {
      // Definisco i validatori dipendenti rispetto allo stato
      vCE.push(Validators.required);
      vCAP.push(Validators.maxLength(capMaxLEst));
      // #
    }

    // Imposto i validatori nel form
    this.setValidatoriCampo(f, fcnCN, vCN, u, uC); // NazioneVo
    this.setValidatoriCampo(f, fcnCE, vCE, u, uC); // Città estera
    this.setValidatoriCampo(f, fcnCAP, vCAP, true, uC); // CAP
  }

  /**
   * Funzione di supporto per snellire le righe di codice nei metodi che richiamano le funzionalità.
   * @param fcn string che identifica il nome del form control per il recupero dei validatori.
   * @param a AppActions che definisce la modalità di gestione del form.
   * @returns Array di ValidatorFn contenenti i validatori.
   */
  private getValidatoriCampo(fcn: string, a: AppActions) {
    // Richiamo la funzione per il ritorno dei validatori
    return this._gestioneFormsDA.getValidatorCampoRecapiti(fcn, a);
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
