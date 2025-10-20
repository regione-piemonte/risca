/**
 * Classe che identifica le costanti del componente associato.
 */
export class RiscaRicercaMorositaConsts {
  /** Costante che definisce il nome del campo form: tipoRicercaMorosita. */
  TIPO_RICERCA_MOROSITA: string = 'tipoRicercaMorosita';
  /** Costante che definisce il nome del campo form: anno. */
  ANNO: string = 'anno';
  /** Costante che definisce il nome del campo form: restituitoAlMittente. */
  RESTITUITO_MITTENTE: string = 'restituitoAlMittente';
  /** Costante che definisce il nome del campo form: annullato. */
  ANNULLATO: string = 'annullato';
  /** Costante che definisce il nome del campo form: limiteInvioAccertamento. */
  LIMITE_INVIO_ACCERTAMENTO: string = 'limiteInvioAccertamento';

  /** Costante che definisce la label del campo form: tipoRicercaMorosita. */
  LABEL_TIPO_RICERCA_MOROSITA: string = 'Stato morosità';
  /** Costante che definisce la label del campo form: anno. */
  LABEL_ANNO: string = 'Anno';
  /** Costante che definisce la label dei campi form: restituitoAlMittente. */
  LABEL_RESTITUITO_MITTENTE: string = 'Restituito al mittente';
  /** Costante che definisce la label del campo form: annullato. */
  LABEL_ANNULLATO: string = 'Annullato';
  /** Costante che definisce la label del campo form: limiteInvioAccertamento. */
  LABEL_LIMITE_INVIO_ACCERTAMENTO: string = 'Limite per invio accertamento';
  /** Costante che definisce la label del campo form: limiteInvioAccertamento con valore minore uguale. */
  LABEL_LIMITE_INVIO_ACCERTAMENTO_MIN_EQ: string = '<=';
  /** Costante che definisce la label del campo form: limiteInvioAccertamento con valore maggiore. */
  LABEL_LIMITE_INVIO_ACCERTAMENTO_MAG: string = '>';

  /** Costante che definisce il nome della proprietà per la select del campo form: tipoRicercaMorosita. */
  PROPERTY_TIPO_RICERCA_MOROSITA: string = 'des_tipo_ricerca_morosita';
  /** Costante che definisce il nome della proprietà per la select del campo form: anno. */
  PROPERTY_ANNO: string = 'anno';

  /** Costante per l'anno minimo della select per gli anni. Da documentazione: 2000. */
  MIN_ANNO: number = 2000;
}