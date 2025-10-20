/**
 * Interfaccia che definisce la struttura dell'oggetto che contiene tutte le informazioni per i messaggi dell'applicazione.
 */
 export class MessaggioUtenteVo {
  constructor(
    public cod_messaggio: string,
    public des_testo_messaggio: string,
    public id_messaggio: number,
    public tipo_messaggio: TipoMessaggioUtenteVo
  ) {}
}

export class TipoMessaggioUtenteVo {
  constructor(
    public cod_tipo_messaggio: string,
    public des_tipo_messaggio: string,
    public id_tipo_messaggio: number,
  ) {}
}
