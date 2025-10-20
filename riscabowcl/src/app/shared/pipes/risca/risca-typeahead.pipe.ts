import { Pipe, PipeTransform } from '@angular/core';
import { RiscaUtilitiesService } from '../../services/risca/risca-utilities/risca-utilities.service';

/**
 * Pipe dedicata alla manipolazione dei dati per ottenere un output leggibile per il componente risca-typeahead.
 */
@Pipe({ name: 'riscaTypeaheadMap' })
export class RiscaTypeaheadMapPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che manipola l'output da visualizzare nella input del componente risca-typeahead.
   * @param popupTip any contenente l'oggetto selezionato dall'utente nel popup dei suggerimenti.
   * @param mapping Funzione che definisce le logiche per il mapping dell'oggetto 'popupTip'.
   * @returns string come valore da visualizzare nella input.
   */
  transform(popupTip: any, mapping: (v: any) => string): string {
    // Verifico che gli input siano definiti
    if (!popupTip || !mapping) {
      return '';
    }

    // Eseguo la funzione passando come parametro l'oggetto popupTip
    return mapping(popupTip);
  }
}

/**
 * Pipe dedicata alla verifica dei dati per la gestione della colorazione degli elementi della lista secondo la validazione tramite data_fine_validita.
 */
@Pipe({ name: 'riscaTypeaheadDataValidita' })
export class RiscaTypeaheadDataValiditaPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * Funzione che restituisce una classe CSS per la gestione degli elementi con data fine validità scaduta.
   * @param e any che definisce l'oggetto selezionato della lista.
   * @returns string come classe css d'assegnare all'elemento della lista.
   */
  transform(e: any): string {
    // Definisco una costante con il nome dalla classe di stile
    const cssClass = 'dropdown-element';

    // Verifico che gli input siano definiti
    if (!e) {
      return '';
    }

    // Verifico la data fine validità mediante funzione di utility
    if (!this._riscaUtilities.isDataValiditaAttiva(e)) {
      // Ritorno la classe di stile
      return cssClass;
      // #
    } else {
      // Nessuna classe di stile
      return '';
    }
  }
}
