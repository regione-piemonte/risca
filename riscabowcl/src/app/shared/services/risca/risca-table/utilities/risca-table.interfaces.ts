/**
 * Interfaccia che definisce l'oggetto di gestione per l'aggiornamento dell'abilitazione delle input della tabella.
 */
export interface ITableEnableChanges {
  /** string che definisce l'id della tabella per cui gestire l'abilitazione. */
  tableId: string;
  /** boolean che definisce se le input sono d'abilitare o da disabilitare. */
  enable: boolean;
}