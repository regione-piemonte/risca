/**
 * Oggetto costante contenente una serie di costanti per il componente dati-tecnici-ambiente.
 */
export class RicercaDatiTecniciTributiConsts {
  /** Costante che identifica il nome del form control: popolazione. */
  POPOLAZIONE = 'popolazione';
  /** Costante che identifica il nome del form control: uso. */
  USO = 'uso';

  /** Costante che identifica la proprietà per la visualizzazione delle select associata a: usoDiLegge. */
  PROPERTY_USO_DI_LEGGE = 'des_tipo_uso';

  /** Costante che identifica la label del campo: popolazione. */
  LABEL_POPOLAZIONE = 'Popolazione';
  /** Costante che identifica la label del campo: comune. */
  LABEL_USO = 'Uso';

  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }
}
