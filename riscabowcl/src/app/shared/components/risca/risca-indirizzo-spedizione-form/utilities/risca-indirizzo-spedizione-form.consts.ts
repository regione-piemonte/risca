import { Validators } from '@angular/forms';
import { exactLengthValidator } from '../../../../miscellaneous/forms-validators/forms-validators';
import { IIndirizzoSpedizioneValidators } from './risca-indirizzo-spedizione-form.interfaces';

/**
 * Class contenente una serie di costanti per il componente indirizzi spedizione form.
 */
export class IndirizzoSpedizioneFormConsts {
  // Proprietà
  DESTINATARIO = 'destinatario';
  PRESSO = 'presso';
  INDIRIZZO = 'indirizzo';
  COMUNE_CITTA = 'comune_citta';
  PROVINCIA = 'provincia';
  CAP = 'cap';
  FRAZIONE = 'frazione';
  NAZIONE = 'nazione';
  // Label
  LABEL_DESTINATARIO = 'Destinatario';
  LABEL_PRESSO = 'Presso';
  LABEL_INDIRIZZO = 'Indirizzo';
  LABEL_COMUNE_CITTA = 'Comune/Città';
  LABEL_PROVINCIA = 'Provincia';
  LABEL_CAP = 'CAP';
  LABEL_FRAZIONE = 'Località';
  LABEL_NAZIONE = 'Nazione';

  // Configurazioni specifiche per la lunghezza dei campi
  validatoriISItalia: IIndirizzoSpedizioneValidators = {
    destinatario: [Validators.required, Validators.maxLength(100)],
    presso: [Validators.maxLength(100)],
    frazione: [Validators.maxLength(100)],
    indirizzo: [Validators.required, Validators.maxLength(100)],
    cap: [Validators.required, exactLengthValidator(5, true)],
    comune_citta: [Validators.required, Validators.maxLength(90)],
    provincia: [Validators.required, exactLengthValidator(2, true)],
    nazione: [Validators.maxLength(60)],
  };
  validatoriISEstero: IIndirizzoSpedizioneValidators = {
    destinatario: [Validators.required, Validators.maxLength(100)],
    presso: [Validators.maxLength(100)],
    frazione: [Validators.maxLength(100)],
    indirizzo: [Validators.required, Validators.maxLength(100)],
    cap: [Validators.required, Validators.maxLength(10)],
    comune_citta: [Validators.required, Validators.maxLength(90)],
    provincia: [],
    nazione: [Validators.maxLength(60)],
  };

  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }
}
