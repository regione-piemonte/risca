import { Moment } from 'moment';
import {
  PagamentoNonProprioVo,
  IPagamentoNonProprioVo,
} from './pagamento-non-proprio-vo';
import {
  DettaglioPagamentoIdVo,
  IDettaglioPagamentoIdVo,
} from './dettaglio-pagamento-id-vo';
import { HelperVo } from './helper-vo';
import { TipoModalitaPagamentoVo } from './tipo-modalita-pagamento-vo';
import { AmbitoVo } from './ambito-vo';

export interface IPagamentoVo {
  ambito?: AmbitoVo;
  id_pagamento?: number;
  id_tipologia_pag?: number;
  id_tipo_fonte_pag?: number;
  id_tipo_modalita_pag?: number;
  tipo_modalita_pag?: TipoModalitaPagamentoVo;
  id_file_poste?: number;
  id_immagine?: number;
  causale?: string; // maxlength?: 1000
  data_op_val?: string; // String che definisce la data in formato server
  importo_versato?: number;
  data_download?: string; // String che definisce la data in formato server
  quinto_campo?: string; // maxlength?: 16
  cro?: string; // maxlength?: 16
  note?: string; // maxlength?: 500
  numero_pagamento?: number;
  soggetto_versamento?: string; // maxlength?: 200
  indirizzo_versamento?: string; // maxlength?: 80
  civico_versamento?: string; // maxlength?: 10
  frazione_versamento?: string; // maxlength?: 50
  comune_versamento?: string; // maxlength?: 40
  cap_versamento?: string; // maxlength?: 5
  prov_versamento?: string; // maxlength?: 20
  flg_rimborsato?: number;
  imp_da_assegnare?: number;
  codice_avviso?: string; // maxlength: 35
  dettaglio_pag?: IDettaglioPagamentoIdVo[];
  pag_non_propri?: IPagamentoNonProprioVo[];
}

export class PagamentoVo extends HelperVo {
  __id: string;

  ambito?: AmbitoVo;
  id_pagamento: number;
  id_tipologia_pag: number;
  id_tipo_fonte_pag: number;
  id_tipo_modalita_pag?: number;
  tipo_modalita_pag: TipoModalitaPagamentoVo;
  id_file_poste: number;
  id_immagine: number;
  causale: string; // maxlength: 1000
  data_op_val: Moment; // String che definisce la data in formato server
  importo_versato: number;
  data_download: Moment; // String che definisce la data in formato server
  quinto_campo: string; // maxlength: 16
  cro: string; // maxlength: 16
  note: string; // maxlength: 500
  numero_pagamento: number;
  soggetto_versamento: string; // maxlength: 200
  indirizzo_versamento: string; // maxlength: 80
  civico_versamento: string; // maxlength: 10
  frazione_versamento: string; // maxlength: 50
  comune_versamento: string; // maxlength: 40
  cap_versamento: string; // maxlength: 5
  prov_versamento: string; // maxlength: 20
  flg_rimborsato: boolean;
  imp_da_assegnare: number;
  codice_avviso: string; // maxlength: 35
  dettaglio_pag: DettaglioPagamentoIdVo[];
  pag_non_propri: PagamentoNonProprioVo[];

  constructor(iPVo?: IPagamentoVo) {
    super();

    this.ambito = iPVo?.ambito;
    this.id_pagamento = iPVo?.id_pagamento;
    this.id_tipologia_pag = iPVo?.id_tipologia_pag;
    this.id_tipo_fonte_pag = iPVo?.id_tipo_fonte_pag;
    this.id_tipo_modalita_pag = iPVo?.id_tipo_modalita_pag;
    this.tipo_modalita_pag = iPVo?.tipo_modalita_pag;
    this.id_file_poste = iPVo?.id_file_poste;
    this.id_immagine = iPVo?.id_immagine;
    this.causale = iPVo?.causale;
    this.data_op_val = this.convertServerDateToMoment(iPVo?.data_op_val);
    this.importo_versato = iPVo?.importo_versato;
    this.data_download = this.convertServerDateToMoment(iPVo?.data_download);
    this.quinto_campo = iPVo?.quinto_campo;
    this.cro = iPVo?.cro;
    this.note = iPVo?.note;
    this.numero_pagamento = iPVo?.numero_pagamento;
    this.soggetto_versamento = iPVo?.soggetto_versamento;
    this.indirizzo_versamento = iPVo?.indirizzo_versamento;
    this.civico_versamento = iPVo?.civico_versamento;
    this.frazione_versamento = iPVo?.frazione_versamento;
    this.comune_versamento = iPVo?.comune_versamento;
    this.cap_versamento = iPVo?.cap_versamento;
    this.prov_versamento = iPVo?.prov_versamento;
    this.flg_rimborsato = this.convertServerBoolNumToBoolean(
      iPVo?.flg_rimborsato
    );
    this.imp_da_assegnare = iPVo?.imp_da_assegnare;
    this.codice_avviso = iPVo?.codice_avviso;
    this.dettaglio_pag = iPVo?.dettaglio_pag?.map(
      (dp: IDettaglioPagamentoIdVo) => {
        // Conversione dell'oggetto
        return new DettaglioPagamentoIdVo(dp);
      }
    );
    this.pag_non_propri = iPVo?.pag_non_propri?.map(
      (dpnp: IPagamentoNonProprioVo) => {
        // Conversione dell'oggetto
        return new PagamentoNonProprioVo(dpnp);
      }
    );
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IPagamentoVo {
    // Conversione delle date
    const data_op_val = this.convertMomentToServerDate(
      this.data_op_val as Moment
    );
    const data_download = this.convertMomentToServerDate(
      this.data_download as Moment
    );

    // Effettuo un parse per le liste
    const dp = this.dettaglio_pag as DettaglioPagamentoIdVo[];
    const dettaglio_pag = this.listToServerFormat(dp);

    const pnp = this.pag_non_propri as PagamentoNonProprioVo[];
    const pag_non_propri = this.listToServerFormat(pnp);

    const be: IPagamentoVo = {
      ambito: this.ambito,
      id_pagamento: this.id_pagamento,
      id_tipo_fonte_pag: this.id_tipo_fonte_pag,
      id_tipologia_pag: this.id_tipologia_pag,
      id_tipo_modalita_pag: this.id_tipo_modalita_pag,
      tipo_modalita_pag: this.tipo_modalita_pag,
      id_file_poste: this.id_file_poste,
      id_immagine: this.id_immagine,
      causale: this.causale,
      data_op_val,
      importo_versato: this.importo_versato,
      data_download,
      quinto_campo: this.quinto_campo,
      cro: this.cro,
      note: this.note,
      numero_pagamento: this.numero_pagamento,
      soggetto_versamento: this.soggetto_versamento,
      indirizzo_versamento: this.indirizzo_versamento,
      civico_versamento: this.civico_versamento,
      frazione_versamento: this.frazione_versamento,
      comune_versamento: this.comune_versamento,
      cap_versamento: this.cap_versamento,
      prov_versamento: this.prov_versamento,
      flg_rimborsato: this.convertBooleanToServerBoolNum(this.flg_rimborsato),
      imp_da_assegnare: this.imp_da_assegnare,
      codice_avviso: this.codice_avviso,
      dettaglio_pag: dettaglio_pag as IDettaglioPagamentoIdVo[],
      pag_non_propri: pag_non_propri as IPagamentoNonProprioVo[],
    };

    return be;
  }
}