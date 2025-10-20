export class CalcoloCanoneVo {
  constructor(public calcolo_canone: number) {}
}

export class CalcoloCanoneV2Vo {
  constructor(
    public importo_canone: number,
    public canone_usi: CanoneUso[],
    public json_regola_mancante: boolean,
    public num_mesi: number
  ) {}
}

export class CanoneUso {
  constructor(
    public anno_uso: string,
    public cod_tipo_uso: string,
    public json_regola_mancante: boolean,
    public canone_uso: number,
    public canone_unitario: number,
    public canone_uso_frazionato: number,
  ) {}
}
