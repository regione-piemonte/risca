import { FALivello } from '../../../../components/risca/risca-filo-arianna/utilities/rfa-level.class';

export class RFALivelli {
  /** FALivello come oggetto di configurazione per: home. */
  home: FALivello;
  /** FALivello come oggetto di configurazione per: pratiche. */
  pratiche: FALivello;
  /** FALivello come oggetto di configurazione per: pagamenti. */
  pagamenti: FALivello;
  /** FALivello come oggetto di configurazione per: verifiche. */
  verifiche: FALivello;
  /** FALivello come oggetto di configurazione per: spedizioni. */
  spedizioni: FALivello;
  /** FALivello come oggetto di configurazione per: canone. */
  canone: FALivello;
  /** FALivello come oggetto di configurazione per: report. */
  report: FALivello;

  /** FALivello come oggetto di configurazione per: nuovaPratica. */
  nuovaPratica: FALivello;
  /** FALivello come oggetto di configurazione per: ricercaSemplice. */
  ricercaSemplice: FALivello;
  /** FALivello come oggetto di configurazione per: ricercaAvanzata. */
  ricercaAvanzata: FALivello;
  /** FALivello come oggetto di configurazione per: ricercaSoggetto. */
  ricercaSoggetto: FALivello;
  /** FALivello come oggetto di configurazione per: ricercaGruppo. */
  ricercaGruppo: FALivello;
  /** FALivello come oggetto di configurazione per: nuovoGruppo. */
  nuovoGruppo: FALivello;
  /** FALivello come oggetto di configurazione per: elencoElaborazioni. */
  elencoElaborazioni: FALivello;
  /** FALivello come oggetto di configurazione per: ricerchePagamenti. */
  ricerchePagamenti: FALivello;
  /** FALivello come oggetto di configurazione per: pagamentiDaVisionare. */
  pagamentiDaVisionare: FALivello;
  /** FALivello come oggetto di configurazione per: bollettini. */
  bollettini: FALivello;

  /** FALivello come oggetto di configurazione per: dettaglioSoggetto. */
  dettaglioGenerico: FALivello;
  /** FALivello come oggetto di configurazione per: pratica. */
  pratica: FALivello;
  /** FALivello come oggetto di configurazione per: dettaglioSoggetto. */
  dettaglioSoggetto: FALivello;
  /** FALivello come oggetto di configurazione per: dettaglioGruppo. */
  dettaglioGruppo: FALivello;
  /** FALivello come oggetto di configurazione per: praticheCollegate. */
  praticheCollegate: FALivello;
  /** FALivello come oggetto di configurazione per: ricercaPagamenti. */
  ricercaPagamenti: FALivello;
  /** FALivello come oggetto di configurazione per: ricercaMorosita. */
  ricercaMorosita: FALivello;
  /** FALivello come oggetto di configurazione per: ricercaRimborsi. */
  ricercaRimborsi: FALivello;

  /** FALivello come oggetto di configurazione per: ricercaAvanzataPratiche. */
  ricercaAvanzataPratiche: FALivello;
  /** FALivello come oggetto di configurazione per: ricercaAvanzataStatiDebitori. */
  ricercaAvanzataStatiDebitori: FALivello;

  /** FALivello come oggetto di configurazione per: datiContabili. */
  datiContabili: FALivello;
  /** FALivello come oggetto di configurazione per: dettaglioPratica. */
  dettaglioPratica: FALivello;
  /** FALivello come oggetto di configurazione per: cercaTitolare. */
  cercaTitolare: FALivello;
  /** FALivello come oggetto di configurazione per: nuovoSoggetto. */
  nuovoSoggetto: FALivello;
  /** FALivello come oggetto di configurazione per: documentiAllegati. */
  documentiAllegati: FALivello;

  /** FALivello come oggetto di configurazione per: nuovoStatoDeb. */
  nuovoStatoDeb: FALivello;
  /** FALivello come oggetto di configurazione per: dettaglioStatoDeb. */
  dettaglioStatoDeb: FALivello;
  /** FALivello come oggetto di configurazione per: dettaglioAccertamenti. */
  dettaglioAccertamenti: FALivello;
  /** FALivello come oggetto di configurazione per: dettaglioPagamenti. */
  dettaglioPagamenti: FALivello;
  /** FALivello come oggetto di configurazione per: dettaglioMorosita. */
  dettaglioMorosita: FALivello;
  /** FALivello come oggetto di configurazione per: dettaglioRimborsi. */
  dettaglioRimborsi: FALivello;

  /** FALivello come oggetto di configurazione per: configurazioneCanoni. */
  configurazioni: FALivello;
  /** FALivello come oggetto di configurazione per: configurazioneCanoni. */
  configurazioneCanoni: FALivello;
  /** FALivello come oggetto di configurazione per: tassiDiInteresse. */
  tassiDiInteresse: FALivello;
  /** FALivello come oggetto di configurazione per: parametriDellaDilazione. */
  parametriDellaDilazione: FALivello;
  /** FALivello come oggetto di configurazione per: altriParametri. */
  altriParametri: FALivello;

  constructor() {
    // Lancio il setup degli oggetti per il filo d'arianna
    this.home = new FALivello({
      id: 'home',
      label: 'Home',
    });
    this.pratiche = new FALivello({
      id: 'pratiche',
      label: 'Pratiche',
    });
    this.pagamenti = new FALivello({
      id: 'pagamenti',
      label: 'Pagamenti',
    });
    this.verifiche = new FALivello({
      id: 'verifiche',
      label: 'Verifiche',
    });
    this.spedizioni = new FALivello({
      id: 'spedizioni',
      label: 'Spedizioni',
    });
    this.canone = new FALivello({
      id: 'canone',
      label: 'Canone',
    });
    this.report = new FALivello({
      id: 'report',
      label: 'Report',
    });
    this.nuovaPratica = new FALivello({
      id: 'nuovaPratica',
      label: 'Nuova pratica',
    });
    this.ricercaSemplice = new FALivello({
      id: 'ricercaSemplice',
      label: 'Ricerca semplice',
    });
    this.ricercaAvanzata = new FALivello({
      id: 'ricercaAvanzata',
      label: 'Ricerca avanzata',
    });
    this.ricercaSoggetto = new FALivello({
      id: 'ricercaSoggetto',
      label: 'Ricerca soggetto',
    });
    this.ricercaGruppo = new FALivello({
      id: 'ricercaGruppo',
      label: 'Ricerca gruppo',
    });
    this.nuovoGruppo = new FALivello({
      id: 'nuovoGruppo',
      label: 'Nuovo gruppo',
    });
    this.elencoElaborazioni = new FALivello({
      id: 'elencoElaborazioni',
      label: 'Elenco elaborazioni',
    });
    this.ricerchePagamenti = new FALivello({
      id: 'ricerchePagamenti',
      label: 'Ricerche',
    });
    this.pagamentiDaVisionare = new FALivello({
      id: 'pagamentiDaVisionare',
      label: 'Pagamenti da visionare',
    });
    this.bollettini = new FALivello({
      id: 'bollettini',
      label: 'Bollettini',
    });
    this.dettaglioGenerico = new FALivello({
      id: 'dettaglioGenerico',
      label: 'Dettaglio',
    });
    this.pratica = new FALivello({
      id: 'pratica',
      label: 'Pratica',
    });
    this.dettaglioSoggetto = new FALivello({
      id: 'dettaglioSoggetto',
      label: 'Dettaglio soggetto',
    });
    this.dettaglioGruppo = new FALivello({
      id: 'dettaglioGruppo',
      label: 'Dettaglio gruppo',
    });
    this.praticheCollegate = new FALivello({
      id: 'praticheCollegate',
      label: 'Pratiche collegate',
    });
    this.ricercaPagamenti = new FALivello({
      id: 'ricercaPagamenti',
      label: 'Pagamenti',
    });
    this.ricercaMorosita = new FALivello({
      id: 'ricercaMorosita',
      label: 'Morosità',
    });
    this.ricercaRimborsi = new FALivello({
      id: 'ricercaRimborsi',
      label: 'Rimborsi',
    });
    this.ricercaAvanzataPratiche = new FALivello({
      id: 'ricercaAvanzataPratiche',
      label: 'Per pratica',
    });
    this.ricercaAvanzataStatiDebitori = new FALivello({
      id: 'ricercaAvanzataStatiDebitori',
      label: 'Per stato debitorio',
    });
    this.datiContabili = new FALivello({
      id: 'datiContabili',
      label: 'Dati contabili',
    });
    this.dettaglioPratica = new FALivello({
      id: 'dettaglioPratica',
      label: 'Dettaglio pratica',
    });
    this.cercaTitolare = new FALivello({
      id: 'cercaTitolare',
      label: 'Cerca titolare',
    });
    this.nuovoSoggetto = new FALivello({
      id: 'nuovoSoggetto',
      label: 'Nuovo soggetto',
    });
    this.documentiAllegati = new FALivello({
      id: 'documentiAllegati',
      label: 'Documenti allegati',
    });
    this.nuovoStatoDeb = new FALivello({
      id: 'nuovoStatoDeb',
      label: 'Nuovo stato debitorio',
    });
    this.dettaglioStatoDeb = new FALivello({
      id: 'dettaglioStatoDeb',
      label: 'Dettaglio stato debitorio',
    });
    this.dettaglioAccertamenti = new FALivello({
      id: 'dettaglioAccertamenti',
      label: 'Dettaglio accertamenti',
    });
    this.dettaglioPagamenti = new FALivello({
      id: 'dettaglioPagamenti',
      label: 'Dettaglio pagamenti',
    });
    this.dettaglioMorosita = new FALivello({
      id: 'dettaglioMorosita',
      label: 'Dettaglio morosità',
    });
    this.dettaglioRimborsi = new FALivello({
      id: 'dettaglioRimborsi',
      label: 'Dettaglio rimborsi',
    });
    this.configurazioni = new FALivello({
      id: 'configurazioni',
      label: 'Configurazioni',
    });
    this.configurazioneCanoni = new FALivello({
      id: 'configurazioneCanoni',
      label: 'Canoni',
    });
    this.tassiDiInteresse = new FALivello({
      id: 'tassiDiInteresse',
      label: `Tassi d'interesse`,
    });
    this.parametriDellaDilazione = new FALivello({
      id: 'parametriDellaDilazione',
      label: 'Parametri della dilazione',
    });
    this.altriParametri = new FALivello({
      id: 'altriParametri',
      label: 'Altri parametri',
    });
  }
}
