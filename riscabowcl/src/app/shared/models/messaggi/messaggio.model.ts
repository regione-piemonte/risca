export interface Message {
  id_messaggio: number; // 2
  tipo_messaggio: TipoMessaggio;
  cod_messaggio: string; // 'P002'
  des_testo_messaggio: string; // 'I dati sono stati cancellati correttamente.'
  des_titolo_messaggio?: string;
}

interface TipoMessaggio {
  id_tipo_messaggio: number; // 1
  cod_tipo_messaggio: CodTipoMessEnum; // 'P'
  des_tipo_messaggio: string; // 'Messaggi feedback positivo '
}

export enum CodTipoMessEnum {
  P = 'P',
  E = 'E',
  I = 'I',
  A = 'A'
}
