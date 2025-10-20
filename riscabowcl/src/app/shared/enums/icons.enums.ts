/**
 * #############################
 * ENUMS ICONE TRAMITE COMPONENT
 * #############################
 */

/**
 * Enums che raccoglie il path per le icone dell'applicazione.
 */
export enum RiscaIcons {
  logout = 'assets/icon-logout.svg',
  download_file = 'assets/icon-download-file.svg',
  trash = 'assets/icon-cestino.svg',
}

/**
 * Const che definisce, come variabile, le icone dell'applicazione.
 */
export const RISCA_ICONS = RiscaIcons;

/**
 * Enums che raccoglie il path per le icone dell'applicazione.
 */
export enum RiscaIconsDisabled {
  logout = 'assets/icon-logout-disabled.svg',
  download_file = 'assets/icon-download-file-disabled.svg',
  trash = 'assets/icon-cestino-disabled.svg',
}

/**
 * Const che definisce, come variabile, le icone dell'applicazione.
 */
export const RISCA_ICONS_DISABLED = RiscaIconsDisabled;

/**
 * #######################
 * ENUMS ICONE TRAMITE CSS
 * #######################
 */

/**
 * Enums che raccoglie il path per le icone dell'applicazione.
 * La mappatura definisce la classe di stile CSS per il caricamento dell'icona.
 */
export enum RiscaIconsCss {
  logout = 'ri-enable-logout',
  confirm_elements = 'ri-enable-confirm-elements',
  download_file = 'ri-enable-download-file',
  download_allegato = 'ri-enable-download-allegato',
  reload = 'ri-enable-reload',
  indirizzo_spedizione_invalido_enable = 'rta-enabled-postel-error',
  indirizzo_spedizione_invalido_disable = 'rta-disabled-postel-error',
  indirizzo_spedizione_valido_enable = 'rta-enable-postel-success',
  indirizzo_spedizione_valido_disable = 'rta-disabled-postel-success',
  stati_debitori_collegati = 'stati-debitori-collegati-enable',
  pagamento = 'icon-pagamento-enable',
  stampa = 'icon-stampa',
  pdf = 'icon-pdf',
  info = 'icon-info'
}

/**
 * Const che definisce, come variabile, le icone dell'applicazione.
 */
export const RISCA_ICONS_CSS = RiscaIconsCss;

/**
 * Enums che raccoglie il path per le icone dell'applicazione.
 * La mappatura definisce la classe di stile CSS per il caricamento dell'icona.
 */
export enum RiscaIconsCssDisabled {
  logout = 'ri-disable-logout',
  confirm_elements = 'ri-disable-confirm-elements',
  download_file = 'ri-disable-download-file',
  download_allegato = 'ri-disable-download-allegato',
  reload = 'ri-disable-reload',
  indirizzo_spedizione_error = 'rta-disabled-postel-error',
  indirizzo_spedizione_success = 'rta-disabled-postel-success',
  stati_debitori_collegati = 'stati-debitori-collegati-disable',
  pagamento = 'icon-pagamento-disable',
  stampa = 'icon-stampa-disabled',
  pdf = 'icon-pdf-disabled',
}

/**
 * Const che definisce, come variabile, le icone dell'applicazione.
 */
export const RISCA_ICONS_CSS_DISABLED = RiscaIconsCssDisabled;
