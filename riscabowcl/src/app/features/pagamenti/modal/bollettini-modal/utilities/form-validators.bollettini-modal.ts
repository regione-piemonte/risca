import { FormGroup, ValidationErrors } from '@angular/forms';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Moment } from 'moment';
import { checkValidationFormControlByFormGroup, generaObjErrore } from 'src/app/shared/miscellaneous/forms-validators/forms-validators';
import { RiscaErrorKeys } from 'src/app/shared/utilities/classes/errors-keys';
import { convertNgbDateStructToMoment } from '../../../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { IRiscaAnnoSelect } from '../../../../../shared/utilities';

/**
 * Verifica che dataScadenzaPagamento >= anno.
 * @param dataScadenzaPagamentoFormControlName string che definisce il nome del formControl di dataScadenzaPagamento.
 * @param annoFormControlName IRiscaAnnoSelect che definisce il nome del formControl di anno.
 * @returns ValidationErrors con la mappatura dell'errore se il validator non soddisfa le condizioni.
 */
export const dataScadenzaPagamentoEAnnoValidator = (
  dataScadenzaPagamentoFormControlName: string,
  annoFormControlName: string
): ValidationErrors => {
  return (formGroup: FormGroup): ValidationErrors | null => {
    // Verifico che il formControl esista
    if (!formGroup) {
      return;
    }
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Variabili di comodo
    const dspFCN = dataScadenzaPagamentoFormControlName;
    const aFCN = annoFormControlName;

    // Recupero i valori dei form control dal FormGroup
    const dsp: NgbDateStruct = checkValidationFormControlByFormGroup(
      formGroup,
      dspFCN
    );
    const anno: IRiscaAnnoSelect = checkValidationFormControlByFormGroup(formGroup, aFCN);

    // Verifico che i campi esistano
    if (!dsp || !anno) {
      // Non si può fare il check
      return null;
    }

    // Recupero il valore dell'anno dalla data
    const annoDSP = dsp.year;
    const annoSel = anno.anno;

    // Faccio una compare degli anni
    if (annoDSP < annoSel) {
      // Preparo l'oggetto di errore
      const keyErrDataScadPagOverAnno = ERR_KEY.ANNO_PRIMA_ANNO_SCAD_PAG;
      // Ritorno l'errore generato
      return generaObjErrore(keyErrDataScadPagOverAnno);
    }

    // Rimuovo eventuali errori
    return null;
  };
};

/**
 * Verifica che dataProtocollo < dataScadenzaPagamento
 * @param dataProtocolloFormControlName string che definisce il nome del formControl di dataProtocollo
 * @param dataScadenzaPagamentoFormControlName string che definisce il nome del formControl di dataScadenzaPagamento.
 * @returns ValidationErrors con la mappatura dell'errore se il validator non soddisfa le condizioni.
 */
export const dataProtocolloEDataScadenzaPagamanetoValidator = (
  dataProtocolloFormControlName: string,
  dataScadenzaPagamentoFormControlName: string
) => {
  return (formGroup: FormGroup): ValidationErrors | null => {
    // Verifico che il formControl esista
    if (!formGroup) {
      return;
    }
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Variabili di comodo
    const dspFCN = dataScadenzaPagamentoFormControlName;
    const dpFCN = dataProtocolloFormControlName;

    // Recupero i valori dei form control dal FormGroup
    const dsp: NgbDateStruct = checkValidationFormControlByFormGroup(
      formGroup,
      dspFCN
    );
    const dp: NgbDateStruct = checkValidationFormControlByFormGroup(
      formGroup,
      dpFCN
    );

    // Verifico che i campi esistano
    if (!dsp || !dp) {
      // Non si può fare il check
      return null;
    }

    // Converto le date in moment
    const dpM: Moment = convertNgbDateStructToMoment(dp);
    const dspM: Moment = convertNgbDateStructToMoment(dsp);
    // Definisco le condizioni di verifica per il check errori
    const existDTM = dpM != undefined && dspM != undefined;
    const okDTM = existDTM && dpM.isValid() && dspM.isValid();

    // Verifico che le date risultino valide
    if (!okDTM) {
      // C'è un errore sulle date, ignoro il controllo
      return null;
    }

    // La data di protocollo deve essere antecedente alla data scadenza pagamento
    if (dpM.isSameOrAfter(dspM)) {
      // Preparo l'oggetto di errore
      const keyErrDSPAndDP = ERR_KEY.DATA_PROT_DOPO_DATA_SCAD_PAG;
      // Ritorno l'errore generato
      return generaObjErrore(keyErrDSPAndDP);
    }

    // Rimuovo eventuali errori
    return null;
  };
};
