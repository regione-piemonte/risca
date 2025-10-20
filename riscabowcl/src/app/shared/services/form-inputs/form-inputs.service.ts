import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MappaErroriFormECodici } from '../../utilities';

/**
 * Servizio di utility con funzionalità di comodo per i componenti di form-inputs.
 */
@Injectable({
  providedIn: 'root',
})
export class FormInputsService {
  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che verifica se esistono errori sul FormGroup.
   * @param formGroup FormGruop da verificare.
   * @param mappaErroriFG Array di MappaErroriFormECodici contenente la lista dei possibili errori.
   * @returns boolean che definsce se all'interno del FormGroup esistono gli errori presenti nel mapping.
   */
  getFormErrors(formGroup: FormGroup, mappaErroriFG: MappaErroriFormECodici[]) {
    // Verifico l'input
    if (!formGroup || !mappaErroriFG || mappaErroriFG.length === 0) {
      // Ritorno false
      return false;
    }

    // Verifico se il form ha generato un errore
    for (let i = 0; i < mappaErroriFG.length; i++) {
      // Recupero l'oggetto all'indice
      const errore = mappaErroriFG[i];
      // Verifico se nel form esiste l'errore estratto
      if (formGroup.errors && formGroup.errors[errore.erroreForm]) {
        // Errore trovato, blocco il ciclo e ritorno true
        return true;
      }
    }

    // Non ho trovato niente, ritorno false
    return false;
  }
}
