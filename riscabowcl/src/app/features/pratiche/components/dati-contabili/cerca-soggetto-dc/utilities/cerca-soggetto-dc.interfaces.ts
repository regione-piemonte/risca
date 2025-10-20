/**
 * Interfaccia che definisce i parametri da passare alla modale.
 */
export interface ICercaSoggettoDCConfigs {
  /** boolean che definisce se i gruppi sono attivi per questo operatore. */
  gruppiAbilitati: boolean;
  /** boolean che definisce se i soggetti sono attivi per questo operatore. */
  soggettiAbilitati: boolean;
  /** string che definisce il testo descrittivo della form da visualizzare. */
  description?: string;
}
