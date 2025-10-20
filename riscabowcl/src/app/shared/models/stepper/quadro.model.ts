export interface Quadro {
  id_quadro: number;
  id_template_quadro?: number;
  tipo_quadro?: TipoQuadro;
  num_versione?: number;
  flg_tipo_gestione: string;
  json_configura_quadro: any;
  ordinamento_template_quadro: number;
  flg_quadro_obbigatorio?: boolean;
  json_data_quadro?: any;
  // added manually, not in DTO:
  completed?: boolean;
}

interface TipoQuadro {
  id_tipo_quadro?: number;
  cod_tipo_quadro?: string;
  des_tipo_quadro?: string;
}
