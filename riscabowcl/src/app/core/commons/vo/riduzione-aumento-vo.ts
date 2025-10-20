export class RiduzioneAumentoVo {
  constructor(
    public id_riduzione_aumento?: number,
    public sigla_riduzione_aumento?: string,
    public des_riduzione_aumento?: string,
    public perc_riduzione_aumento?: number,
    public flg_riduzione_aumento?: number,
    public flg_manuale?: number,
    public flg_da_applicare?: number,
    public ordina_riduzione_aumento?: number,
    public data_inizio_validita?: string,
    public data_fine_validita?: string
  ) {}
}
