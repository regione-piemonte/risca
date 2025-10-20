import { Injectable } from '@angular/core';
import { FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { GestioneFormsDatiAnagraficiService } from '../../../../features/pratiche/service/dati-anagrafici/gestione-forms-dati-anagrafici.service';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { RiscaContattiConsts } from '../../../consts/risca/risca-contatti.consts';
import { FormUpdatePropagation, AppActions } from '../../../utilities';
import { TipoInvio } from '../../../models/contatti/tipo-invio.model';
import { IdTipoInvio } from '../../../utilities/enums/utilities.enums';
import { RiscaFormBuilderService } from '../risca-form-builder/risca-form-builder.service';
import { RiscaUtilitiesService } from '../risca-utilities/risca-utilities.service';

/**
 * Servizio di utility con funzionalità di comodo per la gestione degli script per il componente risca-contatti.
 */
@Injectable({
  providedIn: 'root',
})
export class RiscaContattiService {
  /** Oggetto RiscaContattiConsts contenente le costanti utilizzate dal componente. */
  RC_C = RiscaContattiConsts;
  /** Oggetto CommonConsts contenente le costanti di uso comune. */
  C_C = new CommonConsts()

  /**
   * Costruttore
   */
  constructor(
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
    const tipoInvioConfig = this._riscaFormBuilder.genInputSelect({
      label: this.RC_C.LABEL_TIPO_INVIO,
      showErrorFC: true,
      defaultLabel: this.C_C.SELEZIONA,
      emptyLabelSelected: true,
    });

    const pecConfig = this._riscaFormBuilder.genInputEmail({
      label: this.RC_C.LABEL_PEC,
      showErrorFC: true,
      extraLabelRight: this.RC_C.LABEL_PEC_DESC,
      maxLength: 150,
    });

    const emailConfig = this._riscaFormBuilder.genInputEmail({
      label: this.RC_C.LABEL_EMAIL,
      showErrorFC: true,
      extraLabelRight: this.RC_C.LABEL_EMAIL_DESC,
      maxLength: 300,
    });

    const telefonoConfig = this._riscaFormBuilder.genInputText({
      label: this.RC_C.LABEL_TELEFONO,
      onlyNumber: false,
      maxLength: 50,
    });

    const cellulareConfig = this._riscaFormBuilder.genInputText({
      label: this.RC_C.LABEL_CELLULARE,
      onlyNumber: false,
      maxLength: 50,
    });

    return {
      tipoInvioConfig,
      pecConfig,
      emailConfig,
      telefonoConfig,
      cellulareConfig,
    };
  }

  /**
   * Funzione che gestisce le logiche per la definizione dei validatori dei campi in base a:
   * - Tipo invio selezionato;
   * La funzione, una volta recuperati i dati di cui sopra, andrà a definire i validatori per i campi.
   * @param f FormGroup contenente i dati del form group.
   * @param a AppActions che definisce la modalità di gestione del form.
   * @param u boolean che definisce se il form control deve essere refreshato dopo l'aggiunta dei validatori. Default è true.
   * @param uC FormUpdatePropagation che definisce le configurazioni per il refresh dati e controlli.
   */
  validatoriTipoInvio(
    f: FormGroup,
    a: AppActions,
    u = false,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !a) {
      return;
    }

    // Recupero i nomi dei form control
    const fcnTI = this.RC_C.TIPO_INVIO;
    const fcnPEC = this.RC_C.PEC;
    const fcnEmail = this.RC_C.EMAIL;

    // Recupero i validatori
    const vPEC = this.getValidatoriCampo(fcnPEC, a);
    const vEmail = this.getValidatoriCampo(fcnEmail, a);

    // Recupero il tipo invio
    const ti: TipoInvio = this._riscaUtilities.getFormValue(f, fcnTI);

    // Verifico che esista il tipo invio
    if (ti) {
      // Definisco le condizioni di require
      const tiPEC = ti.id_tipo_invio === IdTipoInvio.PEC;
      const tiEmail = ti.id_tipo_invio === IdTipoInvio.email;
      // Per comune nascita e città estera verifico lo stato selezionato
      if (tiPEC) {
        // Aggiunto il validatore required
        vPEC.push(Validators.required);
        // #
      } else if (tiEmail) {
        // Aggiunto il validatore required
        vEmail.push(Validators.required);
        // #
      }
    }

    // Imposto i validatori nel form
    this.setValidatoriCampo(f, fcnPEC, vPEC, u, uC);
    this.setValidatoriCampo(f, fcnEmail, vEmail, u, uC);
  }

  /**
   * Funzione di supporto per snellire le righe di codice nei metodi che richiamano le funzionalità.
   * @param fcn string che identifica il nome del form control per il recupero dei validatori.
   * @param a AppActions che definisce la modalità di gestione del form.
   * @returns Array di ValidatorFn contenenti i validatori.
   */
  private getValidatoriCampo(fcn: string, a: AppActions) {
    // Richiamo la funzione per il ritorno dei validatori
    return this._gestioneFormsDA.getValidatorCampoContatti(fcn, a);
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
