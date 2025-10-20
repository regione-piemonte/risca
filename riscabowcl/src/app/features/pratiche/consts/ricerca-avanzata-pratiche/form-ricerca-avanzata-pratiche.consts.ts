import {
  CodModalitaRicerca,
  DesModalitaRicerca,
  IdModalitaRicerca,
} from '../../components/ricerca-avanzata/form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.enums';
import { IFRAPModalitaRicerca } from '../../components/ricerca-avanzata/form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.interfaces';

/**
 * Oggetto contenente una serie di costanti per il componente di ricerca Avanzata di pratiche.
 */
export class FormRicercaAvanzataPraticheConsts {
  MODALITA_RICERCA_AVANZATA: IFRAPModalitaRicerca[] = [
    this.ricercaPratiche,
    this.ricercaStatiDebitori,
  ];

  FORM_KEY_PARENT = 'KEY_INS_PRATICA';
  FORM_KEY_CHILD_DGA = 'KEY_DATI_GEN_AMM';
  FORM_KEY_CHILD_DA = 'KEY_DATI_ANAG';
  FORM_KEY_CHILD_DT = 'KEY_DATI_TECNICI_PER_AMBITO';
  FORM_KEY_CHILD_DC = 'KEY_DATI_CONTABILI';
  FORM_KEY_CHILD_DOC = 'KEY_DOCUMENTI_ALLEGATI';

  /** Costante che definisce il nome del campo form = codiceUtenza. */
  // Proprietà da mostrare nella elezione della modalità ricerca
  PROPERTY_MODALITA_RICERCA = 'des_modalita_ricerca';
  PROPERTY_TIPO_SOGGETTO = 'des_tipo_soggetto';
  PROPERTY_NAZIONI = 'denom_nazione';
  PROPERTY_PROVINCE = 'denom_provincia';
  PROPERTY_COMUNE = 'denom_comune';
  PROPERTY_TIPO_TITOLO = 'des_tipo_titolo';
  PROPERTY_TIPO_PROVVEDIMENTO = 'des_tipo_provvedimento';
  PROPERTY_TIPOLOGIA_PRATICA = 'des_tipo_riscossione';
  PROPERTY_STATO_PRATICA = 'des_stato_riscossione';
  PROPERTY_PROVINCIA_COMPETENZA = 'sigla_provincia';
  PROPERTY_USO_DI_LEGGE = 'des_tipo_uso';
  //Sezione di criteri relativi al titolare
  MODALITA_RICERCA = 'modalitaRicerca';
  TIPO_SOGGETTO = 'tipoSoggetto';
  RAGIONE_SOCIALE_COGNOME = 'ragioneSocialeOCognome';
  CODICE_FISCALE = 'codiceFiscale';
  PARTITA_IVA = 'partitaIVA';
  STATO_RESIDENZA = 'stato';
  PROVINCIA_RESIDENZA = 'provincia';
  CITTA_ESTERA_RESIDENZA = 'cittaEsteraResidenza';
  INDIRIZZO = 'indirizzo';
  COMUNE_RESIDENZA = 'comuneResidenza';
  // Sezione di criteri relativi alla localizzazione
  PROVINCIA_COMPETENZA = 'provinciaCompetenza';
  CORPO_IDRICO = 'corpoIdrico';
  COMUNE_COMPETENZA = 'comune';
  NOME_IMPIANTO_IDROELETTRICO = 'nomeImpiantoIdroelettrico';
  // Dati amministrativi
  TIPO_TITOLO = 'tipoTitolo';
  TIPO_PROVVEDIMENTO = 'tipoProvvedimento';
  NUMERO_TITOLO = 'numeroTitolo';
  DATA_TITOLO_DA = 'dataTitoloDa';
  DATA_TITOLO_A = 'dataTitoloA';
  TIPOLOGIA_PRATICA = 'tipologiaPratica';
  STATO_PRATICA = 'statoPratica';
  SCADENZA_CONCESSIONE_DA = 'scadenzaConcessioneDa';
  SCADENZA_CONCESSIONE_A = 'scadenzaConcessioneA';
  DATA_RINUNCIA_REVOCA_DA = 'dataRinunciaRevocaDa';
  DATA_RINUNCIA_REVOCA_A = 'dataRinunciaRevocaA';

  ANNO_CANONE = 'annoCanone';
  CANONE = 'canone';

  RESTITUITO_AL_MITTENTE = 'restituitoAlMittente';
  // Box grigio 1
  ISTANZE = 'istanze';
  TIPO_ISTANZA = 'tipoIstanza';
  DATA_ISTANZA_DA = 'dataIstanzaDa';
  DATA_ISTANZA_A = 'dataIstanzaA';
  //
  SELEZIONA = 'Seleziona';

  /** Labels */
  //Sezione di criteri relativi al titolare
  LABEL_MODALITA_RICERCA = 'Modalità di ricerca';
  LABEL_TIPO_SOGGETTO = 'Tipo soggetto';
  LABEL_RAGIONE_SOCIALE_COGNOME = 'Ragione Sociale/Cognome Nome';
  LABEL_CODICE_FISCALE = 'Codice Fiscale';
  LABEL_PARTITA_IVA = 'Partita IVA';
  LABEL_STATO = 'Stato';
  LABEL_PROVINCIA = 'Provincia';
  LABEL_INDIRIZZO = 'Indirizzo';
  LABEL_COMUNE_RESIDENZA = 'Comune';
  LABEL_CITTA_ESTERA = 'Città estera ';
  // Sezione di criteri relativi alla localizzazione
  LABEL_PROVINCIA_COMPETENZA = 'Provincia di competenza';
  LABEL_CORPO_IDRICO = 'Corpo idrico';
  LABEL_COMUNE = 'Comune';
  LABEL_NOME_IMPIANTO_IDROELETTRICO = 'Nome impianto idroelettrico';
  // Dati amministrativi
  LABEL_ALTRI_DATI = 'Altri dati';
  LABEL_TIPO_TITOLO = 'Tipo titolo';
  LABEL_TIPO_PROVVEDIMENTO = 'Tipo provvedimento';
  LABEL_NUMERO_TITOLO = 'Numero titolo';
  LABEL_DATA_TITOLO = 'Data titolo';
  LABEL_DA = 'da';
  LABEL_A = 'a';
  LABEL_TIPOLOGIA_PRATICA = 'Tipologia pratica';
  LABEL_STATO_PRATICA = 'Stato pratica';
  LABEL_SCADENZA_CONCESSIONE = 'Scadenza concessione';
  LABEL_DATA_RINUNCIA_REVOCA = 'Data rinuncia/revoca';

  LABEL_ANNO_CANONE = 'Anno canone';
  LABEL_CANONE = 'Canone';

  LABEL_RESTITUITO_AL_MITTENTE = 'Restituito al mittente';
  LABEL_ISTANZA = 'Istanza'; // Box grigio 1
  LABEL_ISTANZE = 'Istanze';
  LABEL_TIPO_ISTANZA = 'Tipologia Istanza';
  LABEL_DATA_ISTANZA = 'Data';

  // Label per accordion istanza/provvedimento
  ACCORDION_ISTANZA_PROVVEDIMENTO = 'Istanza/Provvedimento';

  /**
   * Getter di comodo per l'oggetto di ricerca pratiche.
   * @returns IFRAPModalitaRicerca con l'oggetto di configurazione.
   */
  get ricercaPratiche(): IFRAPModalitaRicerca {
    // Creo un oggetto IFRAPModalitaRicerca
    const ricercaPratiche = {
      id_modalita_ricerca: IdModalitaRicerca.pratica,
      cod_modalita_ricerca: CodModalitaRicerca.pratica,
      des_modalita_ricerca: DesModalitaRicerca.pratica,
    };
    // Ritorno l'oggetto
    return ricercaPratiche;
  }

  /**
   * Getter di comodo per l'oggetto di ricerca stati debitori.
   * @returns IFRAPModalitaRicerca con l'oggetto di configurazione.
   */
  get ricercaStatiDebitori(): IFRAPModalitaRicerca {
    // Creo un oggetto IFRAPModalitaRicerca
    const ricercaStatiDebitori = {
      id_modalita_ricerca: IdModalitaRicerca.statoDebitorio,
      cod_modalita_ricerca: CodModalitaRicerca.statoDebitorio,
      des_modalita_ricerca: DesModalitaRicerca.statoDebitorio,
    };
    // Ritorno l'oggetto
    return ricercaStatiDebitori;
  }
}
