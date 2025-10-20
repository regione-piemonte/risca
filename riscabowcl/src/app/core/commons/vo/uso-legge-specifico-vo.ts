export class IUsoLeggeSpecificoVo {
  id_tipo_uso?: number;
  id_ambito?: number;
  id_unita_misura?: number;
  id_accerta_bilancio?: number;
  cod_tipo_uso?: string;
  des_tipo_uso?: string;
  flg_uso_principale?: string;
  ordina_tipo_uso?: number;
  data_fine_validita?: string; // string formattata in iso date
  data_inizio_validita?: string; // string formattata in iso date
  flg_default?: number;
  id_tipo_uso_padre?: number;
}
