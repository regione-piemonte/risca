/**
 * Interfaccia per l'oggetto RiscaContattiConsts.
 */
export interface IRiscaContattiConsts {
  TIPO_INVIO: string;
  PEC: string;
  EMAIL: string;
  TELEFONO: string;
  CELLULARE: string;

  LABEL_TIPO_INVIO: string;
  LABEL_PEC: string;
  LABEL_PEC_DESC: string;
  LABEL_EMAIL: string;
  LABEL_EMAIL_DESC: string;
  LABEL_TELEFONO: string;
  LABEL_CELLULARE: string;

  PROPERTY_TIPO_INVIO: string;
}

/**
 * Oggetto costante contenente una serie di costanti per il componente risca-Contatti.
 */
export const RiscaContattiConsts: IRiscaContattiConsts = {
  /** Costante che rappresenta il campo del form: tipoInvio. */
  TIPO_INVIO: 'tipoInvio',
  /** Costante che rappresenta il campo del form: pec. */
  PEC: 'pec',
  /** Costante che rappresenta il campo del form: email. */
  EMAIL: 'email',
  /** Costante che rappresenta il campo del form: telefono. */
  TELEFONO: 'telefono',
  /** Costante che rappresenta il campo del form: cellulare. */
  CELLULARE: 'cellulare',

  /** Costante che rappresenta la label per il campo del form: tipoInvio. */
  LABEL_TIPO_INVIO: 'Tipo invio',
  /** Costante che rappresenta la label per il campo del form: pec. */
  LABEL_PEC: 'PEC',
  /** Costante che rappresenta la label per la descrizione del campo del form: pec. */
  LABEL_PEC_DESC: '(obbbligatorio se <strong>Tipo invio</strong> PEC)',
  /** Costante che rappresenta la label per il campo del form: email. */
  LABEL_EMAIL: 'E-mail',
  /** Costante che rappresenta la label per la descrizione del campo del form: email. */
  LABEL_EMAIL_DESC: '(obbbligatorio se <strong>Tipo invio</strong> E-mail)',
  /** Costante che rappresenta la label per il campo del form: telefono. */
  LABEL_TELEFONO: 'Telefono',
  /** Costante che rappresenta la label per il campo del form: cellulare. */
  LABEL_CELLULARE: 'Cellulare',

  /** Costante che definisce la proprietà da visualizzare nella select dei tipi invio. */
  PROPERTY_TIPO_INVIO: 'des_tipo_invio',
};
