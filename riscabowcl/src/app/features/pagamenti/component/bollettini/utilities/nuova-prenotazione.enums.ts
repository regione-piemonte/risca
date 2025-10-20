/**
 * Enum di supporto che definisce il cod_tipo_elabora per una specifica gestione per una nuova prenotazione.
 */
export enum TipiPrenotazione {
  bollettazione_ordinaria = 'BO',
  bollettazione_speciale = 'BS',
  avviso_bonario = 'AB',
  sollecito_pagamento = 'SP',
  bollettazione_grande_idroelettrico = 'BG',
}

/**
 * Enum di supporto che definisce le modalità di gestione delle modali per la nuova prenotazione.
 */
export enum NPModalitaModale {
  emissione_prenotazione = 'emissionePrenotazione',
  conferma_prenotazione = 'confermaPrenotazione',
}
