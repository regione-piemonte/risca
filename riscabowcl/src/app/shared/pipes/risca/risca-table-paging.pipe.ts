import { Pipe, PipeTransform } from '@angular/core';
import { RiscaTablePagination } from '../../utilities';

/**
 * Pipe per la visualizzazione dati del totale della paginazione.
 */
@Pipe({ name: 'riscaTPMostraTotale' })
export class RiscaTablePagingMostraTotalePipe implements PipeTransform {
  /** String costante con la label descrittiva: "Pagina" */
  private PAGINA = 'Pagina';
  /** String costante con la label descrittiva: "Di" */
  private DI = 'Di';

  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che compone una stringa informativa per il totale della paginazione.
   * @param pag RiscaTablePagination con la configurazione dati della paginazione.
   * @returns string con le informazioni della paginazione.
   */
  transform(pag: RiscaTablePagination): string {
    // Verifico l'input
    if (!pag) {
      return '';
    }

    // Definisco le costanti localmente per comodità
    const PAGINA = this.PAGINA;
    const DI = this.DI;
    // Esiste la paginazione, estraggo i dati per la descrizione
    const { total, currentPage } = pag;
    // Compongo la descrizione
    const des = `${PAGINA} ${currentPage} ${DI} ${total}`;

    // Ritorno la descrizione
    return des;
  }
}
