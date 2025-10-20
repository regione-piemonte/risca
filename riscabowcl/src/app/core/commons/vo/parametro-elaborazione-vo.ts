import { HelperVo } from "./helper-vo";

export class IParametroElaborazioneVo {
  id_parametro_elabora?: number;
  id_elabora?: number;
  raggruppamento?: string;
  chiave?: string;
  valore?: string;
}

export class ParametroElaborazioneVo extends HelperVo {
  id_parametro_elabora: number;
  id_elabora: number;
  raggruppamento: string;
  chiave: string; // Per la bollettazione si possono usare le chiavi di: TipoParametroElaborazione
  valore: string;

  constructor(iPEVo?: IParametroElaborazioneVo) {
    // Richiamo il super
    super();

    // Verifico l'input
    if (!iPEVo) {
      // Nessuna configurazione
      return;
    }

    this.id_parametro_elabora = iPEVo?.id_parametro_elabora;
    this.id_elabora = iPEVo?.id_elabora;
    this.raggruppamento = iPEVo?.raggruppamento;
    this.chiave = iPEVo?.chiave;
    this.valore = iPEVo?.valore;
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IParametroElaborazioneVo {
    // Creo l'oggetto per il server
    const be: IParametroElaborazioneVo = {
      id_parametro_elabora: this.id_parametro_elabora,
      id_elabora: this.id_elabora,
      raggruppamento: this.raggruppamento,
      chiave: this.chiave,
      valore: this.valore,
    };

    // Ritorno l'oggetto server like
    return be;
  }
}
