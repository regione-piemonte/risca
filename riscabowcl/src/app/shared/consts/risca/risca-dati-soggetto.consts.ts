/**
 * Interfaccia per l'oggetto RiscaDatiSoggettoConsts.
 */
export interface IRiscaDatiSoggettoConsts {
  TIPO_SOGGETTO: string;
  CODICE_FISCALE: string;
  NATURA_GIURIDICA: string;
  NOME: string;
  COGNOME: string;
  RAGIONE_SOCIALE: string;
  PARTITA_IVA: string;
  COMUNE_NASCITA: string;
  DATA_NASCITA: string;
  STATO_NASCITA: string;
  PROVINCIA_NASCITA: string;
  CITTA_ESTERA_NASCITA: string;

  LABEL_TIPO_SOGGETTO: string;
  LABEL_CODICE_FISCALE: string;
  LABEL_NATURA_GIURIDICA: string;
  LABEL_NOME: string;
  LABEL_COGNOME: string;
  LABEL_RAGIONE_SOCIALE: string;
  LABEL_PARTITA_IVA: string;
  LABEL_COMUNE_NASCITA: string;
  LABEL_DATA_NASCITA: string;
  LABEL_STATO_NASCITA: string;
  LABEL_PROVINCIA_NASCITA: string;
  LABEL_CITTA_ESTERA_NASCITA: string;

  PROPERTY_TIPO_SOGGETTO: string;
  PROPERTY_NATURA_GIURIDICA: string;
  PROPERTY_STATO_NASCITA: string;
  PROPERTY_PROVINCIA_NASCITA: string;
}

/**
 * Oggetto costante contenente una serie di costanti per il componente risca-dati-soggetto.
 */
export const RiscaDatiSoggettoConsts: IRiscaDatiSoggettoConsts = {
  /** Costante che rappresenta il campo del form: tipoSoggetto. */
  TIPO_SOGGETTO: 'tipoSoggetto',
  /** Costante che rappresenta il campo del form: codiceFiscale. */
  CODICE_FISCALE: 'codiceFiscale',
  /** Costante che rappresenta il campo del form: naturaGiuridica. */
  NATURA_GIURIDICA: 'naturaGiuridica',
  /** Costante che rappresenta il campo del form: nome. */
  NOME: 'nome',
  /** Costante che rappresenta il campo del form: cognome. */
  COGNOME: 'cognome',
  /** Costante che rappresenta il campo del form: ragioneSociale. */
  RAGIONE_SOCIALE: 'ragioneSociale',
  /** Costante che rappresenta il campo del form: partitaIva. */
  PARTITA_IVA: 'partitaIva',
  /** Costante che rappresenta il campo del form: comuneDiNascita. */
  COMUNE_NASCITA: 'comuneDiNascita',
  /** Costante che rappresenta il campo del form: dataDiNascita. */
  DATA_NASCITA: 'dataDiNascita',
  /** Costante che rappresenta il campo del form: statoDiNascita. */
  STATO_NASCITA: 'statoDiNascita',
  /** Costante che rappresenta il campo del form: provinciaDiNascita. */
  PROVINCIA_NASCITA: 'provinciaDiNascita',
  /** Costante che rappresenta il campo del form: cittaEsteraNascita. */
  CITTA_ESTERA_NASCITA: 'cittaEsteraNascita',

  /** Costante che rappresenta la label per il campo del form: tipoSoggetto. */
  LABEL_TIPO_SOGGETTO: 'Tipo soggetto',
  /** Costante che rappresenta la label per il campo del form: codiceFiscale. */
  LABEL_CODICE_FISCALE: 'Codice fiscale',
  /** Costante che rappresenta il campo del form: naturaGiuridica. */
  LABEL_NATURA_GIURIDICA: 'Natura giuridica',
  /** Costante che rappresenta la label per il campo del form: nome. */
  LABEL_NOME: 'Nome',
  /** Costante che rappresenta la label per il campo del form: cognome. */
  LABEL_COGNOME: 'Cognome',
  /** Costante che rappresenta la label per il campo del form: ragioneSociale. */
  LABEL_RAGIONE_SOCIALE: 'Ragione sociale',
  /** Costante che rappresenta la label per il campo del form: partitaIva. */
  LABEL_PARTITA_IVA: 'Partita iva',
  /** Costante che rappresenta la label per il campo del form: comuneDiNascita. */
  LABEL_COMUNE_NASCITA: 'Comune di nascita',
  /** Costante che rappresenta la label per il campo del form: dataDiNascita. */
  LABEL_DATA_NASCITA: 'Data di nascita',
  /** Costante che rappresenta la label per il campo del form: statoDiNascita. */
  LABEL_STATO_NASCITA: 'Stato di nascita',
  /** Costante che rappresenta la label per il campo del form: provinciaDiNascita. */
  LABEL_PROVINCIA_NASCITA: 'Provincia di nascita',
  /** Costante che rappresenta la label alternativa per il campo del form: cittaEsteraNascita. */
  LABEL_CITTA_ESTERA_NASCITA: 'Città estera',

  /** Costante che definisce la proprietà da visualizzare nella select dei tipi soggetto. */
  PROPERTY_TIPO_SOGGETTO: 'des_tipo_soggetto',
  /** Costante che definisce la proprietà da visualizzare nella select delle nature giuridiche. */
  PROPERTY_NATURA_GIURIDICA: 'des_tipo_natura_giuridica',
  /** Costante che definisce la proprietà da visualizzare nella select degli stati. */
  PROPERTY_STATO_NASCITA: 'denom_nazione',
  /** Costante che definisce la proprietà da visualizzare nella select delle province. */
  PROPERTY_PROVINCIA_NASCITA: 'denom_provincia',
};
