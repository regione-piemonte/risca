import { ValidatorFn } from '@angular/forms';
import { MappaErroriFormECodici } from '../../../../utilities';

/**
 * Interfaccia che definisce l'oggetto generato dal form dell'indirizzo spedizione.
 */
export interface IIndirizzoSpedizioneForm {
  destinatario: string;
  presso: string;
  indirizzo: string;
  comune_citta: string;
  provincia: string;
  cap: string;
  frazione: string;
  nazione: string;
}

/**
 * Interfaccia di comodo che definisce le proprietà per i validatori dei campi dell'oggetto indirizzo di spedizione.
 * Dalla documentazione: WP2-2.2-USR-V07-US009_GestioneDatiAnagrafici.docx
 *  DESTINATARIO_POSTEL <= 100 caratteri  	DESTINATARIO_POSTEL <= 100 caratteri  	        E056
 *  PRESSO_POSTEL <= 100 caratteri	        PRESSO_POSTEL <= 100 caratteri	                E057
 *  FRAZIONE_POSTEL <=100 caratteri	        FRAZIONE_POSTEL <=100 caratteri	                E058
 *  INDIRIZZO_POSTEL <= 100 caratteri       INDIRIZZO_POSTEL <= 100 caratteri               E059
 *  CAP_POSTEL <= 5 caratteri               CAP_POSTEL <= 10 caratteri                      E060 (ITALIA) E061 (ESTERO)
 *  CITTA_POSTEL	<=  90 caratteri	        CITTA_POSTEL	<= 90 caratteri                   E062
 *  PROVINCIA_POSTEL <= 3 caratteri 	      Nessun controllo	                              E063 (SOLO ITALIA)
 *  NAZIONE_POSTEL <= 60	                  NAZIONE_POSTEL <= 60                            E064
 */
export interface IIndirizzoSpedizioneValidators {
  destinatario: ValidatorFn[];
  presso: ValidatorFn[];
  frazione: ValidatorFn[];
  indirizzo: ValidatorFn[];
  cap: ValidatorFn[];
  comune_citta: ValidatorFn[];
  provincia: ValidatorFn[];
  nazione: ValidatorFn[];
}

/**
 * Intefaccia che definisce la struttura dati riguardante le mappe d'errore per la gestione dei campi del form.
 */
export interface IISErrorMaps {
  MAP_IS_DESTINATARIO: MappaErroriFormECodici[];
  MAP_IS_PRESSO: MappaErroriFormECodici[];
  MAP_IS_FRAZIONE: MappaErroriFormECodici[];
  MAP_IS_INDIRIZZO: MappaErroriFormECodici[];
  MAP_IS_CAP: MappaErroriFormECodici[];
  MAP_IS_COMUNE_CITTA: MappaErroriFormECodici[];
  MAP_IS_PROVINCIA: MappaErroriFormECodici[];
  MAP_IS_NAZIONE: MappaErroriFormECodici[];
}
