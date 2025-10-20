export class ConfigurazioneAmbitoVo {
  chiave: string;
  flg_attivo: string;
  id_ambito: number;
  id_ambito_config: number;
  note: string;
  // ordinamento: number;
  // NOTA BENE: valore può assumere, tramite stringa, diversi tipologie di valori, ma vanno gestite nello specifico durante il recupero del dato.
  // Per esempio: per alcune configurazioni il valore è "S" o "N", che sono l'equivalente di true e false. Mentre in altri casi è un valore generico tornato da DB.
  valore: string;

  constructor(
    chiave?: string,
    flg_attivo?: string,
    id_ambito?: number,
    id_ambito_config?: number,
    note?: string,
    // ordinamento?: number,
    valore?: string
  ) {
    this.chiave = chiave;
    this.flg_attivo = flg_attivo;
    this.id_ambito = id_ambito;
    this.id_ambito_config = id_ambito_config;
    this.note = note;
    // this.ordinamento = ordinamento;
    this.valore = valore;
  }
}
