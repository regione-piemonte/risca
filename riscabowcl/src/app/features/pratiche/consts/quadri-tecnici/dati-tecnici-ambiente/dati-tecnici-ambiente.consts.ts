/**
 * Oggetto costante contenente una serie di costanti per il componente dati-tecnici-ambiente.
 */
export class DatiTecniciAmbienteConsts {
  /** Costante che identifica il numero massimo di cifre decimali visualizzare. */
  DECIMAL_PRECISION = 4;

  /** Costante che identifica il nome del form group: datiTecniciAmbienteForm */
  DATI_TECNICI_AMBIENTE_FORM = 'datiTecniciAmbienteForm';
  /** Costante che identifica il nome del form group: usiForm */
  USI_FORM = 'usiForm';
  /** Costante che identifica il nome del form control: corpoIdricoCaptazione. */
  CORPO_IDRICO_CAPTAZIONE = 'corpoIdricoCaptazione';
  /** Costante che identifica il nome del form control: comune. */
  COMUNE = 'comune';
  /** Costante che identifica il nome del form control: nomeImpiantoIdroElettrico. */
  NOME_IMPIANTO_IDROELETTRICO = 'nomeImpiantoIdroElettrico';
  /** Costante che identifica il nome del form control: portataDaAssegnare. */
  PORTATA_DA_ASSEGNARE = 'portataDaAssegnare';
  /** Costante che identifica il nome del form control: gestioneManuale. */
  GESTIONE_MANUALE = 'gestioneManuale';
  /** Costante che identifica il nome del form control: usoDiLegge. */
  USO_DI_LEGGE = 'usoDiLegge';
  /** Costante che identifica il nome del form control: usiDiLegge. */
  USI_DI_LEGGE = 'usiDiLegge';
  /** Costante che identifica il nome del form control: usiDiLeggeSpecifici. */
  USI_DI_LEGGE_SPECIFICI = 'usiDiLeggeSpecifici';
  /** Costante che identifica la label del campo: usiRiepilogo. */
  USI_RIEPILOGO = 'usiRiepilogo';
  /** Costante che identifica il nome del form control: unitaDiMisura. */
  UNITA_DI_MISURA = 'unitaDiMisura';
  /** Costante che identifica il nome del form control: unitaDiMisuraDesc. */
  UNITA_DI_MISURA_DESC = 'unitaDiMisuraDesc';
  /** Costante che identifica il nome del form control: quantita. */
  QUANTITA = 'quantita';
  /** Costante che identifica il nome del form control: quantitaFaldaProfonda. */
  QUANTITA_FALDA_PROFONDA = 'quantitaFaldaProfonda';
  /** Costante che identifica il nome del form control: percFaldaProfonda. */
  PERC_FALDA_PROFONDA = 'percFaldaProfonda';
  /** Costante che identifica il nome del form control: quantitaFaldaSuperficiale. */
  QUANTITA_FALDA_SUPERFICIALE = 'quantitaFaldaSuperficiale';
  /** Costante che identifica il nome del form control: percFaldaSuperficiale. */
  PERC_FALDA_SUPERFICIALE = 'percFaldaSuperficiale';
  /** Costante che identifica il nome del form control: percRiduzioni. */
  PERC_DI_RIDUZIONE_MOTIVAZIONE = 'percRiduzioni';
  /** Costante che identifica il nome del form control: percAumenti. */
  PERC_DI_AUMENTO_MOTIVAZIONE = 'percAumenti';
  /** Costante che identifica il nome del form control: dataScadenzaEmasIso. */
  DATA_SCADENZA_EMAS_ISO = 'dataScadenzaEmasIso';

  /** Costante che identifica la proprietà per la visualizzazione delle select associata a: usoDiLegge. */
  PROPERTY_USO_DI_LEGGE = 'des_tipo_uso';
  /** Costante che identifica la proprietà per la visualizzazione delle select associata a: usiDiLeggeSpecifici. */
  PROPERTY_USI_DI_LEGGE_SPECIFICI = 'des_tipo_uso';

  /** Costante che identifica la label del campo: corpoIdricoCaptazione. */
  LABEL_CORPO_IDRICO_CAPTAZIONE = '*Corpo idrico captazione';
  /** Costante che identifica la label del campo: comune. */
  LABEL_COMUNE = '*Comune';
  /** Costante che identifica la label del campo: nomeImpiantoIdroElettrico. */
  LABEL_NOME_IMPIANTO_IDROELETTRICO = 'Nome impianto idroelettrico';
  /** Costante che identifica la label del campo: portataDaAssegnare. */
  LABEL_PORTATA_DA_ASSEGNARE = 'Portata complessiva utenza';
  /** Costante che identifica la label del campo: gestioneManuale. */
  LABEL_GESTIONE_MANUALE = 'Gestione manuale';
  /** Costante che identifica la label del campo: usoDiLegge. */
  LABEL_USO_DI_LEGGE = '*Uso di legge';
  /** Costante che identifica la label del campo: usiDiLeggeSpecifici. */
  LABEL_USI_SPECIFICI = 'Uso specifico (possibile selezione multipla)';
  /** Costante che identifica la label del campo: usiRiepilogo. */
  LABEL_USI_RIEPILOGO = 'Uso/i';
  /** Costante che identifica la label del campo: unitaDiMisuraDesc. */
  LABEL_UNITA_DI_MISURA_DESC = 'Unità di misura';
  /** Costante che identifica la label del campo: quantita. */
  LABEL_QUANTITA = 'Quantità';
  /** Costante che identifica la label del campo: quantitaFaldaProfonda. */
  LABEL_QUANTITA_FALDA_PROFONDA = 'Quantità falda profonda';
  /** Costante che identifica la label del campo: percFaldaProfonda. */
  LABEL_PERC_FALDA_PROFONDA = '% falda profonda';
  /** Costante che identifica la label del campo: quantitaFaldaSuperficiale. */
  LABEL_QUANTITA_FALDA_SUPERFICIALE = 'Quantità falda superficiale';
  /** Costante che identifica la label del campo: percFaldaSuperficiale. */
  LABEL_PERC_FALDA_SUPERFICIALE = '% falda superficiale';
  /** Costante che identifica la label del campo: percRiduzioni. */
  LABEL_PERC_RIDUZIONE =
    '% di riduzione motivazione (possibile selezione multipla)';
  /** Costante che identifica la label del campo: percAumenti. */
  LABEL_PERC_AUMENTO =
    '% di aumento motivazione (possibile selezione multipla)';
  /** Costante che identifica la label del campo: dataScadenzaEmasIso. */
  LABEL_DATA_SCADENZA_EMAS_ISO = 'Data scadenza EMAS o ISO';

  /** Costante che identifica la label per l'accordion degli usi. */
  ACCORDION_USI_DI_LEGGE = 'Usi di legge';

  /** string che definisce il title per la segnalazione di mancanza dei dati tecnici. */
  NO_DT_TITLE = '[RISCA]';
  /** string che definisce il body per la segnalazione di mancanza dei dati tecnici. */
  NO_DT_DES =
    'Nessun dato tecnico associato alla pratica. Impossibile caricare la pagina con le informazioni.';

  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }
}
