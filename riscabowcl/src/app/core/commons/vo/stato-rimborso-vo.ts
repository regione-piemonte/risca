/**
 * Classe che definisce la struttura di una riga dei rimborsi provenienti dal BE.
 */
export class StatoRimborsoVo {
  constructor(
    public id_stato_rimborso: number,
    public cod_stato_rimborso: string,
    public des_stato_rimborso: string
  ) {}
}
