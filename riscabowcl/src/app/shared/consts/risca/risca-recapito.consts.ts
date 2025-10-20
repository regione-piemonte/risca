/**
 * Interfaccia per l'oggetto RiscaRecapitoConsts.
 */
export interface IRiscaRecapitoConsts {
  TIPO_SEDE: string;
  PRESSO: string;
  STATO: string;
  COMUNE: string;
  PROVINCIA: string;
  CITTA_ESTERA_RECAPITO: string;
  LOCALITA: string;
  INDIRIZZO: string;
  NUMERO_CIVICO: string;
  CAP: string;
  INDIRIZZI_SPEDIZIONE: string;

  LABEL_TIPO_SEDE: string;
  LABEL_PRESSO: string;
  LABEL_STATO: string;
  LABEL_COMUNE: string;
  LABEL_PROVINCIA: string;
  LABEL_CITTA_ESTERA_RECAPITO: string;
  LABEL_LOCALITA: string;
  LABEL_INDIRIZZO: string;
  LABEL_NUMERO_CIVICO: string;
  LABEL_CAP: string;

  PROPERTY_TIPO_SEDE: string;
  PROPERTY_STATO: string;
  PROPERTY_PROVINCIA: string;

  BUTTON_INDIRIZZO_SPEDIZIONE: string;
}

/**
 * Oggetto costante contenente una serie di costanti per il componente risca-recapito.
 */
export const RiscaRecapitoConsts: IRiscaRecapitoConsts = {
  /** Costante che rappresenta il campo del form: tipoSede. */
  TIPO_SEDE: 'tipoSede',
  /** Costante che rappresenta il campo del form: presso. */
  PRESSO: 'presso',
  /** Costante che rappresenta il campo del form: stato. */
  STATO: 'stato',
  /** Costante che rappresenta il campo del form: comune. */
  COMUNE: 'comune',
  /** Costante che rappresenta il campo del form: provincia. */
  PROVINCIA: 'provincia',
  /** Costante che rappresenta il campo del form: cittaEsteraRecapito. */
  CITTA_ESTERA_RECAPITO: 'cittaEsteraRecapito',
  /** Costante che rappresenta il campo del form: localita. */
  LOCALITA: 'localita',
  /** Costante che rappresenta il campo del form: indirizzo. */
  INDIRIZZO: 'indirizzo',
  /** Costante che rappresenta il campo del form: numeroCivico. */
  NUMERO_CIVICO: 'numeroCivico',
  /** Costante che rappresenta il campo del form: cap. */
  CAP: 'cap',
  /** Costante che rappresenta il campo del form: indirizziSpedizione. */
  INDIRIZZI_SPEDIZIONE: 'indirizziSpedizione',

  /** Costante che rappresenta la label per il campo del form: tipoSede. */
  LABEL_TIPO_SEDE: 'Tipo sede',
  /** Costante che rappresenta la label per il campo del form: presso. */
  LABEL_PRESSO: 'Presso',
  /** Costante che rappresenta la label per il campo del form: stato. */
  LABEL_STATO: 'Stato',
  /** Costante che rappresenta la label per il campo del form: comune. */
  LABEL_COMUNE: 'Comune',
  /** Costante che rappresenta la label per il campo del form: provincia. */
  LABEL_PROVINCIA: 'Provincia',
  /** Costante che rappresenta la label per il campo del form: cittaEsteraRecapito. */
  LABEL_CITTA_ESTERA_RECAPITO: 'Citta estera',
  /** Costante che rappresenta la label per il campo del form: localita. */
  LABEL_LOCALITA: 'Località',
  /** Costante che rappresenta la label per il campo del form: indirizzo. */
  LABEL_INDIRIZZO: 'Indirizzo',
  /** Costante che rappresenta la label per il campo del form: numeroCivico. */
  LABEL_NUMERO_CIVICO: 'Numero civico',
  /** Costante che rappresenta la label per il campo del form: cap. */
  LABEL_CAP: 'CAP',

  /** Costante che definisce la proprietà da visualizzare nella select dei tipi sede. */
  PROPERTY_TIPO_SEDE: 'des_tipo_sede',
  /** Costante che definisce la proprietà da visualizzare nella select degli stati. */
  PROPERTY_STATO: 'denom_nazione',
  /** Costante che definisce la proprietà da visualizzare nella select delle province. */
  PROPERTY_PROVINCIA: 'denom_provincia',

  /** Costante che rappresenta la label per il bottone: Indirizzo spedizione. */
  BUTTON_INDIRIZZO_SPEDIZIONE: 'Indirizzo spedizione',
};
