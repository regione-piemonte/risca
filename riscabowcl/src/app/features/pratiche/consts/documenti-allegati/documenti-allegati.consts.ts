/**
 * Interfaccia che definisce le proprietà dell'oggetto di costanti per il componente documenti-allegati.
 */
interface IDocumentiAllegatiConsts {
  ACTION_DOCUMENTO: string;
  ACTION_ALLEGATI: string;
}

/**
 * Oggetto contenente una serie di costanti per il componente documenti-allegati.
 */
export const DocumentiAllegatiConsts: IDocumentiAllegatiConsts = {
  /** Costante che indica l'azione del bottone Documento. */
  ACTION_DOCUMENTO: 'documento',
  /** Costante che indica l'azione del bottone Allegati. */
  ACTION_ALLEGATI: 'allegati',
};
