import * as moment from 'moment';
import { Moment } from 'moment';
import { AmbitoVo } from './ambito-vo';
import { HelperVo } from './helper-vo';
import {
  IParametroElaborazioneVo,
  ParametroElaborazioneVo,
} from './parametro-elaborazione-vo';
import { RegistroElaboraVo } from './registro-elabora-vo';
import { StatoElaborazioneVo } from './stato-elaborazione-vo';
import {
  ITipoElaborazioneVo,
  TipoElaborazioneVo,
} from './tipo-elaborazione-vo';
import { clearFileName } from '../../../shared/services/risca/risca-utilities/risca-utilities.functions';

export interface IElaborazioneVo {
  ambito: AmbitoVo;
  data_richiesta: string; // String con formato convertibile a Date javascript ISOString
  id_elabora: number;
  parametri: IParametroElaborazioneVo[];
  stato_elabora: StatoElaborazioneVo;
  tipo_elabora: ITipoElaborazioneVo;
  registro_elabora?: RegistroElaboraVo;
  nome_file_generato?: string;
}

/**
 * ###############################
 * Oggetto Elaborazione Bollettini
 * ###############################
 */

export class ElaborazioneVo extends HelperVo {
  ambito: AmbitoVo;
  data_richiesta: Moment; // String con formato convertibile a Date javascript ISOString
  id_elabora: number;
  parametri: ParametroElaborazioneVo[];
  stato_elabora: StatoElaborazioneVo;
  tipo_elabora: TipoElaborazioneVo;
  registro_elabora?: RegistroElaboraVo;
  nome_file_generato?: string;
  nomeFile?: string;

  constructor(iEVo?: IElaborazioneVo) {
    // Richiamo il super
    super();

    // Verifico l'inpuf
    if (!iEVo) {
      // Nessuna configurazione, setto gli array vuoti
      this.parametri = [];
      // Blocco le logiche
      return;
    }

    this.ambito = iEVo.ambito;
    this.data_richiesta = moment(iEVo.data_richiesta);
    this.id_elabora = iEVo.id_elabora;
    this.parametri =
      iEVo.parametri?.map((p: IParametroElaborazioneVo) => {
        return new ParametroElaborazioneVo(p);
      }) ?? [];
    this.stato_elabora = iEVo.stato_elabora;
    this.tipo_elabora = new TipoElaborazioneVo(iEVo.tipo_elabora);
    this.registro_elabora = iEVo.registro_elabora;
    this.nome_file_generato = iEVo.nome_file_generato;
    this.nomeFile = clearFileName(clearFileName(iEVo.nome_file_generato, '\\'));
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IElaborazioneVo {
    // Creo l'oggetto per il server
    const be: IElaborazioneVo = {
      ambito: this.ambito,
      data_richiesta: this.data_richiesta?.toDate()?.toISOString(),
      id_elabora: this.id_elabora,
      parametri: this.parametri,
      stato_elabora: this.stato_elabora,
      tipo_elabora: this.tipo_elabora?.toServerFormat(),
      registro_elabora: this.registro_elabora,
    };

    // Ritorno l'oggetto server like
    return be;
  }
}
