/**
 * Oggetto contenente una serie di costanti per il componente omonimo.
 */
export class DettaglioRegolaUsoModalConsts {
  MODAL_TITLE: string = 'Dettagli usi per canone';

  USO_PRINCIPALE: string = 'Uso principale selezionato';

  ACCORDION_USI_EFFETTIVI: string = 'Usi effettivi';
  ACCORDION_LEGENDA: string = 'Legenda';

  LABEL_CAMPI_OBBLIGATORI_CANONE_MINIMO: string = `*Campi obbligatori. Numero con 2 decimali`;
  LABEL_CAMPI_OBBLIGATORI_SOGLIA: string = `*Campi obbligatori`;
  LABEL_CAMPI_OBBLIGATORI_RANGES: string = `*Campi obbligatori. Almeno una combinazione condizione + valore è obbligatoria`;

  /** any compatibile con la direttiva NgStyle per la gestione grafica. */
  STYLE_ROW_MINIMO_PRINCIPALE: any = {
    'margin-top': '-33px',
    'margin-left': '578px',
  };
}
