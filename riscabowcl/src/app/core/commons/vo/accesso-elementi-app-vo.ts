/**
 * ###########################################
 * DEFINIZIONE OGGETTO: ProfilazioneUtilizzoVo
 * ###########################################
 */

export class AccessoElementiAppVo {
  /**
   * Costruttore.
   */
  constructor(
    public profilo_pa: ProfiloPAVo,
    public profilo_oggetto_app: ProfiloOggettoAppVo[]
  ) {}
}

export class ProfiloPAVo {
  /**
   * Costruttore.
   */
  constructor(
    public id_profilo_pa: number,
    public cod_profilo_pa: string,
    public des_profilo_pa: string
  ) {}
}

export class ProfiloOggettoAppVo {
  /**
   * Costruttore.
   */
  constructor(
    public id_profilo_pa: number,
    public oggetto_app: OggettoAppVo,
    public flg_attivo: boolean
  ) {}
}

export class OggettoAppVo {
  /**
   * Costruttore.
   */
  constructor(
    public id_oggetto_app: number,
    public tipo_oggetto_app: TipoOggettoAppVo,
    public cod_oggetto_app: string,
    public des_oggetto_app: string
  ) {}
}

export class TipoOggettoAppVo {
  /**
   * Costruttore.
   */
  constructor(
    public id_tipo_tipo_oggetto_app: number,
    public cod_tipo_oggetto_app: string,
    public des_tipo_oggetto_app: string
  ) {}
}

/**
 * #########################################
 * DEFINIZIONE OGGETTO: ProfilazioneUtilizzo
 * NOTA BENE: PER ORA NON E' NECESSARIA QUESTA STRUTTURA, LA MANTENIAMO IN CASO DI MODIFICHE
 * #########################################
 */

// export class AccessoElementiApp {
//   /**
//    * Costruttore.
//    */
//   constructor(
//     public profiloPA: ProfiloPAVo,
//     public oggettiApp: ProfiloOggettoApp[]
//   ) {}
// }

// export class ProfiloOggettoApp {
//   /**
//    * Costruttore.
//    */
//   constructor(
//     public id_profilo_pa: number,
//     public oggetto_app: OggettoAppVo,
//     public flag_attivo: boolean
//   ) {}
// }

/*
###### ESEMPIO DI OGGETTO ######

{
  "profilo_pa": {
    "id_profilo_pa": 1,
    "cod_profilo_pa": "AMMINISTRATORE",
    "des_profilo_pa": "Amministratore"
  },
  "profilo_oggetto_app": [
    {
      "id_profilo_pa": 1,
      "oggetto_app": {
        "id_oggetto_app": 9,
        "tipo_oggetto_app": {
          "id_tipo_tipo_oggetto_app": 3,
          "cod_tipo_oggetto_app": "PULSANTE",
          "des_tipo_oggetto_app": "Pulsante Applicativo"
        },
        "cod_oggetto_app": "NUOVA_PRATICA_HOME",
        "des_oggetto_app": "Pulsante vai a nuova Pratica"
      },
      "flg_attivo": 1
    },
    {
      "id_profilo_pa": 1,
      "oggetto_app": {
        "id_oggetto_app": 9,
        "tipo_oggetto_app": {
          "id_tipo_tipo_oggetto_app": 4,
          "cod_tipo_oggetto_app": "PULSANTE",
          "des_tipo_oggetto_app": "Pulsante Applicativo"
        },
        "cod_oggetto_app": "PULSANTE",
        "des_oggetto_app": "Pulsante vai a nuova Pratica"
      },
      "flg_attivo": 1
    }
  ]
}
*/
