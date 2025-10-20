/**
 * Enum che definisce i valori per le sezioni delle configurazioni, per la gestione del menu.
 */
export enum AzioniConfigurazione {
  canoni = 'canoni',
  tassiDiInteresse = 'tassi-di-interesse',
  parametriDellaDilazione = 'parametri-della-dilazione',
  altriParametri = 'altri-parametri',
}

/**
 * Enum che definisce le chiavi per il get/set delle informazioni tramite routing applicativo.
 */
export enum ConfigurazioneRouteKeys {
  canoni = 'CANONI',
  tassiDiInteresse = 'TASSI_DI_INTERESSE',
  parametriDellaDilazione = 'PARAMETRI_DELLA_DILAZIONE',
  altriParametri = 'ALTRI_PARAMETRI',
}