/**
 * Interfaccia strettamente collegata alla costante di configurazione.
 */
export interface IGestisciRimborsoModalConsts {
  MODAL_TITLE: string;
  LABEL_ANNULLA: string;
  LABEL_CONFERMA: string;
  LABEL_ANNULLA_RICERCA: string;
  LABEL_CAMBIA_CREDITORE: string;
}

/**
 * Costante per la pagina collegata.
 */
export const GestisciRimborsoModalConsts: IGestisciRimborsoModalConsts = {
  MODAL_TITLE: 'Modifica Rimborso',
  LABEL_ANNULLA: 'ANNULLA',
  LABEL_CONFERMA: 'CONFERMA',
  LABEL_ANNULLA_RICERCA: 'ANNULLA RICERCA',
  LABEL_CAMBIA_CREDITORE: 'CAMBIA CREDITORE',
};
