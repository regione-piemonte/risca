import { Moment } from 'moment';
import {
  arrotondaEccesso,
  isEmptyDeep,
} from '../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { AccertamentoVo, IAccertamentoVo } from './accertamento-vo';
import { AnnualitaSDVo, IAnnualitaSDVo } from './annualita-sd-vo';
import { AttivitaSDVo, IAttivitaSDVo } from './attivita-sd-vo';
import { HelperVo } from './helper-vo';
import { IRataVo, RataVo } from './rata-vo';
import { IRimborsoVo, RimborsoVo } from './rimborso-vo';
import {
  IStatoContribuzioneSDVo,
  StatoContribuzioneSDVo,
} from './stato-contribuzione-vo';

export interface IStatoDebitorioVo {
  id_stato_debitorio?: number;
  id_riscossione?: number;
  id_soggetto?: number;
  id_gruppo_soggetto?: number;
  id_recapito_stato_debitorio?: number;
  attivita_stato_deb?: IAttivitaSDVo;
  stato_contribuzione?: IStatoContribuzioneSDVo;
  id_avviso_pagamento?: number;
  num_titolo?: number;
  data_pagamento?: string; // Date server format
  data_provvedimento?: string; // Date server format
  num_richiesta_protocollo?: number;
  data_richiesta_protocollo?: string; // Date server format
  data_ultima_modifica?: string; // Date server format
  des_usi?: string;
  note?: string;
  desc_periodo_pagamento?: string;
  desc_motivo_annullo?: string;
  annualita_sd?: IAnnualitaSDVo[];
  num_annualita?: number;
  anno?: number;
  nap?: string;
  imp_recupero_canone?: number;
  imp_recupero_interessi?: number;
  imp_spese_notifica?: number;
  imp_compensazione_canone?: number;
  imp_mancante_imp_eccedente?: number;
  interessi_maturati_spese_di_notifica?: number;
  importo_dovuto?: number;
  flg_annullato?: number;
  flg_restituito_mittente?: number;
  flg_invio_speciale?: number;
  flg_dilazione?: number;
  flg_addebito_anno_successivo?: number;
  multi_nap?: boolean;
  canone_dovuto?: number;
  importo_versato?: number;
  importo_eccedente?: number;
  rimborsi?: IRimborsoVo[];
  tipo_titolo?: string; //TipoTitoloVo;
  rate?: IRataVo[];
  id_tipo_dilazione?: number;
  cod_utenza?: string;
  accertamenti?: IAccertamentoVo[];
  attivita?: string;
  stato_riscossione?: string;
  acc_importo_dovuto?: number;
  acc_data_scadenza_pag?: string;
  acc_importo_versato?: number;
  acc_importo_di_canone_mancante?: number;
  acc_interessi_mancanti?: number;
  acc_interessi_versati?: number;
  acc_importo_rimborsato?: number;
  calcolo_interessi?: number;
  num_pratica?: string;
  titolare?: string;
}

/**
 * Oggetto IStatoDebitorioVo ridotto con le informazioni minime necessarie per il BE per il salvataggio dei dati degli accertamenti.
 */
export interface IStatoDebitorioAccertamentiVo {
  id_stato_debitorio?: number;
  id_riscossione?: number;
  id_provvedimento?: number;
  id_soggetto?: number;
  id_gruppo_soggetto?: number;
  id_recapito_stato_debitorio?: number;
  flg_annullato?: number;
  flg_restituito_mittente?: number;
  flg_invio_speciale?: number;
  flg_dilazione?: number;
  flg_addebito_anno_successivo?: number;
  accertamenti?: IAccertamentoVo[];
  attivita_stato_deb?: IAttivitaSDVo;
}

export class StatoDebitorioVo extends HelperVo {
  id_stato_debitorio?: number;
  id_riscossione?: number;
  id_soggetto?: number;
  id_gruppo_soggetto?: number;
  id_recapito_stato_debitorio?: number;
  attivita_stato_deb?: AttivitaSDVo;
  stato_contribuzione?: StatoContribuzioneSDVo;
  id_avviso_pagamento?: number;
  num_titolo?: number;
  data_pagamento?: Moment; // Date server format
  data_provvedimento?: Moment; // Date server format
  num_richiesta_protocollo?: number;
  data_richiesta_protocollo?: Moment; // Date server format
  data_ultima_modifica?: Moment; // Date server format
  des_usi?: string;
  note?: string;
  desc_periodo_pagamento?: string;
  desc_motivo_annullo?: string;
  annualita_sd?: AnnualitaSDVo[];
  num_annualita?: number;
  anno?: number;
  nap?: string;
  imp_recupero_canone?: number;
  imp_recupero_interessi?: number;
  imp_spese_notifica?: number;
  imp_compensazione_canone?: number;
  imp_mancante_imp_eccedente?: number;
  importo_dovuto?: number;
  interessi_maturati_spese_di_notifica?: number;
  flg_annullato?: boolean;
  flg_restituito_mittente?: boolean;
  flg_invio_speciale?: boolean;
  flg_dilazione?: boolean;
  flg_addebito_anno_successivo?: boolean;
  multi_nap?: boolean;
  canone_dovuto?: number;
  importo_versato?: number;
  rimborsi?: RimborsoVo[];
  rate?: RataVo[];
  tipo_titolo?: string; //TipoTitoloVo;
  id_tipo_dilazione?: number;
  importo_eccedente?: number;
  cod_utenza?: string;
  accertamenti?: AccertamentoVo[];
  attivita?: string;
  stato_riscossione?: string;
  acc_importo_dovuto?: number;
  acc_data_scadenza_pag?: Moment;
  acc_importo_versato?: number;
  acc_importo_di_canone_mancante?: number;
  acc_interessi_mancanti?: number;
  acc_interessi_versati?: number;
  acc_importo_rimborsato?: number;
  calcolo_interessi?: number;
  num_pratica?: string;
  titolare?: string;

  constructor(sdVo?: IStatoDebitorioVo) {
    super();

    this.id_stato_debitorio = sdVo?.id_stato_debitorio;
    this.id_riscossione = sdVo?.id_riscossione;
    this.id_soggetto = sdVo?.id_soggetto;
    this.id_gruppo_soggetto = sdVo?.id_gruppo_soggetto;
    this.id_recapito_stato_debitorio = sdVo?.id_recapito_stato_debitorio;
    this.attivita_stato_deb = new AttivitaSDVo(sdVo?.attivita_stato_deb);
    this.stato_contribuzione = new StatoContribuzioneSDVo(
      sdVo?.stato_contribuzione
    );
    this.id_avviso_pagamento = sdVo?.id_avviso_pagamento;
    this.num_titolo = sdVo?.num_titolo;
    this.data_pagamento = this.convertServerDateToMoment(sdVo?.data_pagamento);
    this.data_provvedimento = this.convertServerDateToMoment(
      sdVo?.data_provvedimento
    );
    this.num_richiesta_protocollo = sdVo?.num_richiesta_protocollo;
    this.data_richiesta_protocollo = this.convertServerDateToMoment(
      sdVo?.data_richiesta_protocollo
    );
    this.data_ultima_modifica = this.convertServerDateToMoment(
      sdVo?.data_ultima_modifica
    );
    this.des_usi = sdVo?.des_usi;
    this.note = sdVo?.note;
    this.desc_periodo_pagamento = sdVo?.desc_periodo_pagamento;
    this.desc_motivo_annullo = sdVo?.desc_motivo_annullo;
    this.annualita_sd = this.convertIAnnualitaSDiVo(sdVo?.annualita_sd);
    this.num_annualita = sdVo?.num_annualita;
    this.anno = sdVo?.anno;
    this.nap = sdVo?.nap;
    this.imp_recupero_canone = sdVo?.imp_recupero_canone;
    this.imp_recupero_interessi = sdVo?.imp_recupero_interessi;
    this.imp_spese_notifica = sdVo?.imp_spese_notifica;
    this.imp_compensazione_canone = sdVo?.imp_compensazione_canone;
    this.imp_mancante_imp_eccedente = sdVo?.imp_mancante_imp_eccedente;
    this.importo_dovuto = sdVo?.importo_dovuto;
    this.flg_annullato = this.convertServerBoolNumToBoolean(
      sdVo?.flg_annullato
    );
    this.interessi_maturati_spese_di_notifica =
      sdVo?.interessi_maturati_spese_di_notifica;
    this.flg_restituito_mittente = this.convertServerBoolNumToBoolean(
      sdVo?.flg_restituito_mittente
    );
    this.flg_invio_speciale = this.convertServerBoolNumToBoolean(
      sdVo?.flg_invio_speciale
    );
    this.flg_dilazione = this.convertServerBoolNumToBoolean(
      sdVo?.flg_dilazione
    );
    this.flg_addebito_anno_successivo = this.convertServerBoolNumToBoolean(
      sdVo?.flg_addebito_anno_successivo
    );
    this.multi_nap = sdVo?.multi_nap;
    this.canone_dovuto = sdVo?.canone_dovuto;
    this.importo_versato = sdVo?.importo_versato;
    this.rimborsi = this.convertRimborsiVo(sdVo?.rimborsi);
    this.rate = this.convertRateVo(sdVo?.rate);
    this.id_tipo_dilazione = sdVo?.id_tipo_dilazione;
    this.importo_eccedente = sdVo?.importo_eccedente;
    this.cod_utenza = sdVo?.cod_utenza;
    this.accertamenti = this.convertAccertamentiVo(sdVo?.accertamenti);
    this.attivita = sdVo?.attivita ?? '';
    this.stato_riscossione = sdVo?.stato_riscossione ?? '';

    this.acc_importo_dovuto = sdVo?.acc_importo_dovuto;
    this.acc_data_scadenza_pag = this.convertServerDateToMoment(
      sdVo?.acc_data_scadenza_pag
    );
    this.acc_importo_versato = sdVo?.acc_importo_versato;
    this.acc_importo_di_canone_mancante = sdVo?.acc_importo_di_canone_mancante;
    this.acc_interessi_mancanti = sdVo?.acc_interessi_mancanti;
    this.acc_interessi_versati = sdVo?.acc_interessi_versati;
    this.acc_importo_rimborsato = sdVo?.acc_importo_rimborsato;
    this.calcolo_interessi = sdVo?.calcolo_interessi;
    this.num_pratica = sdVo?.num_pratica;
    this.titolare = sdVo?.titolare;
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IStatoDebitorioVo {
    // Conversione della data
    const data_op_val = this.convertMomentToServerDate(this.data_pagamento);
    const data_provvedimento = this.convertMomentToServerDate(
      this.data_provvedimento
    );
    const data_richiesta_protocollo = this.convertMomentToServerDate(
      this.data_richiesta_protocollo
    );
    const data_ultima_modifica = this.convertMomentToServerDate(
      this.data_ultima_modifica
    );
    const acc_data_scadenza_pag = this.convertMomentToServerDate(
      this.acc_data_scadenza_pag
    );

    // Conversione dei boolean
    const flg_annullato = this.convertBooleanToServerBoolNum(
      this.flg_annullato as boolean
    );
    const flg_restituito_mittente = this.convertBooleanToServerBoolNum(
      this.flg_restituito_mittente as boolean
    );
    const flg_invio_speciale = this.convertBooleanToServerBoolNum(
      this.flg_invio_speciale as boolean
    );
    const flg_dilazione = this.convertBooleanToServerBoolNum(
      this.flg_dilazione as boolean
    );
    const flg_addebito_anno_successivo = this.convertBooleanToServerBoolNum(
      this.flg_addebito_anno_successivo as boolean
    );
    const multi_nap = this.multi_nap;

    // Effettuo il parse degli oggetti
    const asd = this.attivita_stato_deb;
    const iASD = this.objToServerFormat(asd);
    const attivita_stato_deb = isEmptyDeep(iASD) ? undefined : iASD;

    const sc = this.stato_contribuzione;
    const stato_contribuzione = this.objToServerFormat(sc);

    // Effettuo un parse per le liste
    const aSD = this.annualita_sd as AnnualitaSDVo[];
    const annualita_sd = this.listToServerFormat(aSD);
    // ###
    const rSD = this.rimborsi as RimborsoVo[];
    const rimborsi = this.listToServerFormat(rSD);
    // ###
    const raSD = this.rate as RataVo[];
    const rate = this.listToServerFormat(raSD);
    // ###
    const accSD = this.accertamenti as AccertamentoVo[];
    const accertamenti = this.listToServerFormat(accSD);

    const be: IStatoDebitorioVo = {
      id_stato_debitorio: this.id_stato_debitorio,
      id_riscossione: this.id_riscossione,
      id_soggetto: this.id_soggetto,
      id_gruppo_soggetto: this.id_gruppo_soggetto,
      id_recapito_stato_debitorio: this.id_recapito_stato_debitorio,
      attivita_stato_deb: (attivita_stato_deb as IAttivitaSDVo) ?? null,
      stato_contribuzione: stato_contribuzione as IStatoContribuzioneSDVo,
      id_avviso_pagamento: this.id_avviso_pagamento,
      num_titolo: this.num_titolo,
      data_pagamento: data_op_val,
      data_provvedimento,
      num_richiesta_protocollo: this.num_richiesta_protocollo,
      data_richiesta_protocollo,
      data_ultima_modifica,
      des_usi: this.des_usi,
      note: this.note,
      desc_periodo_pagamento: this.desc_periodo_pagamento,
      desc_motivo_annullo: this.desc_motivo_annullo,
      annualita_sd: annualita_sd as IAnnualitaSDVo[],
      num_annualita: this.num_annualita,
      anno: this.anno,
      nap: this.nap,
      imp_recupero_canone: this.imp_recupero_canone,
      imp_recupero_interessi: this.imp_recupero_interessi,
      imp_spese_notifica: this.imp_spese_notifica,
      imp_compensazione_canone: this.imp_compensazione_canone,
      imp_mancante_imp_eccedente: this.imp_mancante_imp_eccedente,
      importo_dovuto: this.importo_dovuto,
      flg_annullato,
      flg_restituito_mittente,
      flg_invio_speciale,
      flg_dilazione,
      flg_addebito_anno_successivo,
      multi_nap,
      canone_dovuto: this.canone_dovuto,
      importo_versato: this.importo_versato,
      rimborsi,
      rate,
      tipo_titolo: this.tipo_titolo,
      id_tipo_dilazione: this.id_tipo_dilazione,
      importo_eccedente: this.importo_eccedente,
      cod_utenza: this.cod_utenza,
      accertamenti,
      attivita: this.attivita,
      stato_riscossione: this.stato_riscossione,

      acc_importo_dovuto: this.acc_importo_dovuto,
      acc_data_scadenza_pag,
      acc_importo_versato: this.acc_importo_versato,
      acc_importo_di_canone_mancante: this.acc_importo_di_canone_mancante,
      acc_interessi_mancanti: this.acc_interessi_mancanti,
      acc_interessi_versati: this.acc_interessi_versati,
      acc_importo_rimborsato: this.acc_importo_rimborsato,
    };

    // Ritorno l'oggetto per il be
    return be;
  }

  /**
   * Funzione di comodo per preprare un oggetto da mandare al server per la specifica gestione dei dati degli accertamenti.
   */
  toServerFormatAccertamenti(): IStatoDebitorioAccertamentiVo {
    // Richiamo la funzione del formato server
    const sdVo: IStatoDebitorioVo = this.toServerFormat();

    // Andiamo a rimappare l'oggetto dello stato debitorio in modalità compatibile con il salvataggio degli accertamenti
    const be: IStatoDebitorioAccertamentiVo = {};
    // Rimappo le proprietà dall'oggetto stato debitorio
    be.id_stato_debitorio = sdVo.id_stato_debitorio;
    be.id_riscossione = sdVo.id_riscossione;
    be.id_soggetto = sdVo.id_soggetto;
    be.id_gruppo_soggetto = sdVo.id_gruppo_soggetto;
    be.id_recapito_stato_debitorio = sdVo.id_recapito_stato_debitorio;
    be.flg_annullato = sdVo.flg_annullato;
    be.flg_restituito_mittente = sdVo.flg_restituito_mittente;
    be.flg_invio_speciale = sdVo.flg_invio_speciale;
    be.flg_dilazione = sdVo.flg_dilazione;
    be.flg_addebito_anno_successivo = sdVo.flg_addebito_anno_successivo;
    be.accertamenti = sdVo.accertamenti;
    be.attivita_stato_deb = sdVo.attivita_stato_deb ?? null;

    // Ritorno l'oggetto aggiornato
    return be;
  }

  /**
   * Funzione di conversione dati da IAnnualitaSDVo a AnnualitaSDVo.
   * @param iASDiVo IAnnualitaSDVo[] da convertire.
   * @returns AnnualitaSDVo[] convertito.
   */
  convertIAnnualitaSDiVo(iASDiVo: IAnnualitaSDVo[]): AnnualitaSDVo[] {
    // Verifico l'input
    if (!iASDiVo) {
      // Niente da convertire
      return [];
    }

    // Converto tutti gli oggetti
    return iASDiVo.map((iAUsoSD: IAnnualitaSDVo) => {
      // Converto l'oggetto
      return new AnnualitaSDVo(iAUsoSD);
    });
  }

  /**
   * Funzione di conversione dati da IRimborsoVo a RimborsoVo.
   * @param iRiVo IRimborsoVo[] da convertire.
   * @returns RimborsoVo[] convertito.
   */
  convertRimborsiVo(iRiVo: IRimborsoVo[]): RimborsoVo[] {
    // Verifico l'input
    if (!iRiVo) {
      // Niente da convertire
      return [];
    }

    // Converto tutti gli oggetti
    return iRiVo.map((iRVo: IRimborsoVo) => {
      // Converto l'oggetto
      return new RimborsoVo(iRVo);
    });
  }

  /**
   * Funzione di conversione dati da IRataVo a RataVo.
   * @param iReVo IRataVo[] da convertire.
   * @returns RataVo[] convertito.
   */
  convertRateVo(iReVo: IRataVo[]): RataVo[] {
    // Verifico l'input
    if (!iReVo) {
      // Niente da convertire
      return [];
    }

    // Converto tutti gli oggetti
    return iReVo.map((iRaVo: IRataVo) => {
      // Converto l'oggetto
      return new RataVo(iRaVo);
    });
  }

  /**
   * Funzione di conversione dati da IAccertamentoVo a AccertamentoVo.
   * @param iAccVo IAccertamentoVo[] da convertire.
   * @returns AccertamentoVo[] convertito.
   */
  convertAccertamentiVo(iAccVo: IAccertamentoVo[]): AccertamentoVo[] {
    // Verifico l'input
    if (!iAccVo) {
      // Niente da convertire
      return [];
    }

    // Converto tutti gli oggetti
    return iAccVo.map((iAccVo: IAccertamentoVo) => {
      // Converto l'oggetto
      return new AccertamentoVo(iAccVo);
    });
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che calcola l'informazione di importo mancante con interessi.
   * @returns number con il risultato del calcolo.
   */
  get importoMancanteConInteressi(): number {
    // Recupero le informazioni per il calcolo
    let importoMancate: number;
    importoMancate = this.acc_importo_di_canone_mancante ?? 0;
    let interessi: number;
    interessi = this.calcolo_interessi ?? 0;
    let interessiMaturatiSpeseNotifica: number;
    interessiMaturatiSpeseNotifica =
      this.interessi_maturati_spese_di_notifica ?? 0;

    // Effettuo la somma delle informazioni
    let importoMancanteConInteressi: number;
    importoMancanteConInteressi =
      importoMancate + interessi + interessiMaturatiSpeseNotifica;

    // Ritorno il calcolo
    return arrotondaEccesso(importoMancanteConInteressi, 2);
  }
}
