/**
 * Classe che identifica le costanti del componente associato.
 */
export class RiscaRicercaRimborsiConsts {
  /** Costante che definisce il nome del campo form: tipoRicercaRimborsi. */
  TIPO_RICERCA_RIMBORSI: string = 'tipoRicercaRimborsi';
  /** Costante che definisce il nome del campo form: anno. */
  ANNO: string = 'anno';

  /** Costante che definisce la label del campo form: tipoRicercaRimborsi. */
  LABEL_TIPO_RICERCA_RIMBORSI: string = 'Stato rimborso';
  /** Costante che definisce la label del campo form: anno. */
  LABEL_ANNO: string = 'Anno';

  /** Costante che definisce il nome della proprietà per la select del campo form: tipoRicercaRimborsi. */
  PROPERTY_TIPO_RICERCA_RIMBORSI: string = 'des_tipo_ricerca_rimborso';
  /** Costante che definisce il nome della proprietà per la select del campo form: anno. */
  PROPERTY_ANNO: string = 'anno';

  /** Costante per l'anno minimo della select per gli anni. Da documentazione: 2000. */
  MIN_ANNO: number = 2000;
}