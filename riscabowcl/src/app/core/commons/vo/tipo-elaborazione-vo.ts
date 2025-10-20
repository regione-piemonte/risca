import { Moment } from 'moment';
import * as moment from 'moment';
import { AmbitoVo } from './ambito-vo';
import { FunzionalitaVo } from './funzionalita-vo';
import { HelperVo } from './helper-vo';

export interface ITipoElaborazioneVo {
  ambito?: AmbitoVo;
  cod_tipo_elabora?: string;
  data_inizio_validita?: string; // Javascript Date format
  des_tipo_elabora?: string;
  flg_default?: number;
  flg_visibile?: number;
  funzionalita: FunzionalitaVo;
  id_ambito?: number;
  id_funzionalita?: number;
  id_tipo_elabora?: number;
  ordina_tipo_elabora?: number;
}

export class TipoElaborazioneVo extends HelperVo {
  ambito?: AmbitoVo;
  cod_tipo_elabora?: string;
  data_inizio_validita?: Moment;
  des_tipo_elabora?: string;
  flg_default?: boolean;
  flg_visibile?: boolean;
  funzionalita: FunzionalitaVo;
  id_ambito?: number;
  id_funzionalita?: number;
  id_tipo_elabora?: number;
  ordina_tipo_elabora?: number;

  constructor(iTEvo?: ITipoElaborazioneVo) {
    // Richiamo il super
    super();

    // Verifico se esiste un input
    if (!iTEvo) {
      // Niente configurazione
      return;
    }

    this.ambito = iTEvo.ambito;
    this.cod_tipo_elabora = iTEvo.cod_tipo_elabora;
    this.data_inizio_validita = moment(iTEvo.data_inizio_validita);
    this.des_tipo_elabora = iTEvo.des_tipo_elabora;
    this.flg_default = this.convertServerBoolNumToBoolean(iTEvo.flg_default);
    this.flg_visibile = this.convertServerBoolNumToBoolean(iTEvo.flg_visibile);
    this.funzionalita = iTEvo.funzionalita;
    this.id_ambito = iTEvo.id_ambito;
    this.id_funzionalita = iTEvo.id_funzionalita;
    this.id_tipo_elabora = iTEvo.id_tipo_elabora;
    this.ordina_tipo_elabora = iTEvo.ordina_tipo_elabora;
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): ITipoElaborazioneVo {
    // Creo l'oggetto per il server
    const be: ITipoElaborazioneVo = {
      ambito: this.ambito,
      cod_tipo_elabora: this.cod_tipo_elabora,
      data_inizio_validita: this.data_inizio_validita?.toDate()?.toISOString(),
      des_tipo_elabora: this.des_tipo_elabora,
      flg_default: this.convertBooleanToServerBoolNum(this.flg_default),
      flg_visibile: this.convertBooleanToServerBoolNum(this.flg_visibile),
      funzionalita: this.funzionalita,
      id_ambito: this.id_ambito,
      id_funzionalita: this.id_funzionalita,
      id_tipo_elabora: this.id_tipo_elabora,
      ordina_tipo_elabora: this.ordina_tipo_elabora,
    };

    // Ritorno l'oggetto server like
    return be;
  }
}
