import {
  formatoImportoITA,
  rimuoviDecimaliNonSignificativiIT,
} from '../../../../../../../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { DatiTecniciUsoAmbienteVo } from '../../../../vo/dati-tecnici-ambiente-vo';
import { DTExtrasClass } from '../../../dt-extras/dt-extras.class';
import {
  GRANDE_IDROELETTRICO_20211001,
  GRANDE_IDROELETTRICO_EXTRAS_20211001,
} from './utilities/grande-idroelettrico.consts';
import { I_GRANDE_IDROELETTRICO_EXTRAS_20211001 } from './utilities/grande-idroelettrico.interfaces';

/**
 * Classe specifica che gestisce le informazioni extra per i dati dell'uso: GRANDE_IDROELETTRICO.
 * @version 20211001 versione dato tecnico.
 */
export class DTGrandeIdroelettricoExtras20211001 extends DTExtrasClass<I_GRANDE_IDROELETTRICO_EXTRAS_20211001> {
  // GIT risca-issue #31 => è stato richiesto di gestire i decimali per come sono definiti nel dato, senza limitarli rispetto alla configurazione.
  /** number che definisce il numero di decimali accettati per: PERC_QUOTA_VAR. */
  // [Configurazione pre issue-31: 2]
  private DEC_PERC_QUOTA_VAR: number = 12;
  /** number che definisce il numero di decimali accettati per: PNM_PER_ENERG_GRAT. */
  // [Configurazione pre issue-31: 2]
  private DEC_PNM_PER_ENERG_GRAT: number = 12;
  /** number che definisce il numero di decimali accettati per: QTA_ENERG_GRAT. */
  // [Configurazione pre issue-31: 4]
  private DEC_QTA_ENERG_GRAT: number = 12;
  /** number che definisce il numero di decimali accettati per: TOT_ENERG_PROD. */
  // [Configurazione pre issue-31: 5]
  private DEC_TOT_ENERG_PROD: number = 12;
  /** number che definisce il numero di decimali accettati per: TOT_RICAVI_ANNO. */
  // [Configurazione pre issue-31: 2]
  private DEC_TOT_RICAVI_ANNO: number = 12;
  /** number che definisce il numero di decimali accettati per: PREZZO_MED_ORA_POND. */
  // [Configurazione pre issue-31: 2]
  private DEC_PREZZO_MED_ORA_POND: number = 12;

  /**
   * Costruttore.
   */
  constructor(codiceUso?: string, dtUso?: DatiTecniciUsoAmbienteVo) {
    // Lancio il super
    super();

    // Inizializzo le informazioni per la classe
    this.usoRiferimento = GRANDE_IDROELETTRICO_20211001;
    this.extras = GRANDE_IDROELETTRICO_EXTRAS_20211001;

    // Verifico se è stato passato un dtUso
    if (dtUso) {
      // Tento di generare internamente le informazioni
      this.addExtrasToClass(codiceUso, dtUso);
    }
  }

  /**
   * ####################
   * FUNZIONI DI OVERRIDE
   * ####################
   */

  /**
   * Funzione che genera una stringa HTML per la visualizzazione dei dati all'interno del popover delle tabelle dei dati tecnici.
   * @returns string come template DOM HTML da utilizzare come corpo del popover delle tabelle dei dati tecnici.
   * @override
   */
  popoverPratica(): string {
    // Recupero dall'istanza locale la mappa per i dati extra
    let extras: I_GRANDE_IDROELETTRICO_EXTRAS_20211001;
    extras = this.extras;

    // Creo la stringa per l'utilizzo nel DOM
    let popoverDOM = ``;

    // Recupero le singole label
    const percQuotaVariabile = this.percQuotaVariabile(extras);
    const pnmEnergiaGratuita = this.pnmEnergiaGratuita(extras);
    const quantitaEnergiaGratuitaAnnua =
      this.quantitaEnergiaGratuitaAnnua(extras);
    // Definisco un array con le varie label
    const labels = [
      percQuotaVariabile,
      pnmEnergiaGratuita,
      quantitaEnergiaGratuitaAnnua,
    ];
    // Effettuo una join tra le stringhe, con un break line
    popoverDOM = labels.join('<br>');

    // Ritorno il template generato
    return popoverDOM;
  }

  /**
   * Funzione che genera una stringa HTML per la visualizzazione dei dati all'interno del popover delle tabelle dei dati tecnici.
   * @returns string come template DOM HTML da utilizzare come corpo del popover delle tabelle dei dati tecnici.
   * @override
   */
  popoverAnnualita(): string {
    // Recupero dall'istanza locale la mappa per i dati extra
    let extras: I_GRANDE_IDROELETTRICO_EXTRAS_20211001;
    extras = this.extras;

    // Creo la stringa per l'utilizzo nel DOM
    let popoverDOM = ``;

    // Recupero le singole label
    const percQuotaVariabile = this.percQuotaVariabile(extras);
    const pnmEnergiaGratuita = this.pnmEnergiaGratuita(extras);
    const quantitaEnergiaGratuitaAnnua =
      this.quantitaEnergiaGratuitaAnnua(extras);
    const totaleEnergiaProdotta = this.totaleEnergiaProdotta(extras);
    const totaleRicaviLordi = this.totaleRicaviLordi(extras);
    const prezzoMedioOrariPonderato = this.prezzoMedioOrariPonderato(extras);
    // Definisco un array con le varie label
    const labels = [
      percQuotaVariabile,
      pnmEnergiaGratuita,
      quantitaEnergiaGratuitaAnnua,
      totaleEnergiaProdotta,
      totaleRicaviLordi,
      prezzoMedioOrariPonderato,
    ];
    // Effettuo una join tra le stringhe, con un break line
    popoverDOM = labels.join('<br>');

    // Ritorno il template generato
    return popoverDOM;
  }

  /**
   * ###################################
   * FUNZIONI DI GENERAZIONE DELLE LABEL
   * ###################################
   */

  /**
   * Funzione che genera label e dati per l'informazione specifica.
   * @param extras E con la mappatura delle chiavi per i dati tecnici extra.
   * @returns string con la label e dati generata.
   */
  private percQuotaVariabile(
    extras: I_GRANDE_IDROELETTRICO_EXTRAS_20211001
  ): string {
    // Definisco la label
    const label = '% Quota variabile';
    // Definisco le informazioni per la gestione del valore
    let dato: string;
    let valore: number = this[extras.perc_quota_var];
    const decimali: number = this.DEC_PERC_QUOTA_VAR;

    // Verifico il dato in maniera specifica
    if (valore >= 0) {
      // Recupero la parte "dati"
      dato = formatoImportoITA(valore, decimali, false);
      // Rimuovo i decimali non necessari
      dato = rimuoviDecimaliNonSignificativiIT(dato);
      // #
    }

    // Combino le informazioni
    const output = `<b>${label}</b>: ${dato ?? ''}`;

    // Ritorno l'output generato
    return output;
  }

  /**
   * Funzione che genera label e dati per l'informazione specifica.
   * @param extras E con la mappatura delle chiavi per i dati tecnici extra.
   * @returns string con la label e dati generata.
   */
  private pnmEnergiaGratuita(
    extras: I_GRANDE_IDROELETTRICO_EXTRAS_20211001
  ): string {
    // Definisco la label
    const label = 'PNM per energia gratuita (Kw)';
    // Definisco le informazioni per la gestione del valore
    const valore: number = this[extras.pnm_per_energ_grat];
    const decimali: number = this.DEC_PNM_PER_ENERG_GRAT;

    // Recupero la parte "dati"
    let dato = formatoImportoITA(valore, decimali, false);
    // Rimuovo i decimali non necessari
    dato = rimuoviDecimaliNonSignificativiIT(dato);

    // Combino le informazioni
    const output = `<b>${label}</b>: ${dato ?? ''}`;

    // Ritorno l'output generato
    return output;
  }

  /**
   * Funzione che genera label e dati per l'informazione specifica.
   * @param extras E con la mappatura delle chiavi per i dati tecnici extra.
   * @returns string con la label e dati generata.
   */
  private quantitaEnergiaGratuitaAnnua(
    extras: I_GRANDE_IDROELETTRICO_EXTRAS_20211001
  ): string {
    // Definisco la label
    const label = 'Quantità energia gratuita annua (MWh)';

    // Recupero la parte "dati"
    const decimali: number = this.DEC_QTA_ENERG_GRAT;
    const coeffEnergGrat = this[extras.coeff_energ_grat];
    const pnmEnergGrat = this[extras.pnm_per_energ_grat];
    // Verifico che entrambe le informazioni esistano
    if (coeffEnergGrat == undefined || pnmEnergGrat == undefined) {
      // Non esistono le informazioni di calcolo
      return '';
    }

    // Definisco il valore per la conversione
    const valore: number = (coeffEnergGrat * pnmEnergGrat) / 1000;
    // Effettuo il calcolo per la visualizzazione
    let dato = formatoImportoITA(valore, decimali, false);
    // Rimuovo i decimali non necessari
    dato = rimuoviDecimaliNonSignificativiIT(dato);

    // Combino le informazioni
    const output = `<b>${label}</b>: ${dato ?? ''}`;

    // Ritorno l'output generato
    return output;
  }

  /**
   * Funzione che genera label e dati per l'informazione specifica.
   * @param extras E con la mappatura delle chiavi per i dati tecnici extra.
   * @returns string con la label e dati generata.
   */
  private totaleEnergiaProdotta(
    extras: I_GRANDE_IDROELETTRICO_EXTRAS_20211001
  ): string {
    // Definisco la label
    const label = 'Totale Energia Prodotta (MWh)';
    // Definisco le informazioni per la gestione del valore
    const valore: number = this[extras.tot_energ_prod];
    const decimali: number = this.DEC_TOT_ENERG_PROD;

    // Recupero la parte "dati"
    let dato = formatoImportoITA(valore, decimali, false);
    // Rimuovo i decimali non necessari
    dato = rimuoviDecimaliNonSignificativiIT(dato);

    // Combino le informazioni
    const output = `<b>${label}</b>: ${dato ?? ''}`;

    // Ritorno l'output generato
    return output;
  }

  /**
   * Funzione che genera label e dati per l'informazione specifica.
   * @param extras E con la mappatura delle chiavi per i dati tecnici extra.
   * @returns string con la label e dati generata.
   */
  private totaleRicaviLordi(
    extras: I_GRANDE_IDROELETTRICO_EXTRAS_20211001
  ): string {
    // Definisco la label
    const label = 'Totale Ricavi Lordi (euro)';
    // Definisco le informazioni per la gestione del valore
    const valore: number = this[extras.tot_ricavi_anno];
    const decimali: number = this.DEC_TOT_RICAVI_ANNO;

    // Recupero la parte "dati"
    let dato = formatoImportoITA(valore, decimali, false);
    // Rimuovo i decimali non necessari
    dato = rimuoviDecimaliNonSignificativiIT(dato);

    // Combino le informazioni
    const output = `<b>${label}</b>: ${dato ?? ''}`;

    // Ritorno l'output generato
    return output;
  }

  /**
   * Funzione che genera label e dati per l'informazione specifica.
   * @param extras E con la mappatura delle chiavi per i dati tecnici extra.
   * @returns string con la label e dati generata.
   */
  private prezzoMedioOrariPonderato(
    extras: I_GRANDE_IDROELETTRICO_EXTRAS_20211001
  ): string {
    // Definisco la label
    const label = 'Prezzo medio orari ponderato (euro/MWh)';
    // Definisco le informazioni per la gestione del valore
    const valore: number = this[extras.prezzo_med_ora_pond];
    const decimali: number = this.DEC_PREZZO_MED_ORA_POND;

    // Recupero la parte "dati"
    let dato = formatoImportoITA(valore, decimali, false);
    // Rimuovo i decimali non necessari
    dato = rimuoviDecimaliNonSignificativiIT(dato);

    // Combino le informazioni
    const output = `<b>${label}</b>: ${dato ?? ''}`;

    // Ritorno l'output generato
    return output;
  }
}
