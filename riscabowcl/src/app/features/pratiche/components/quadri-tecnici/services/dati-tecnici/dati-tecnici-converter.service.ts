import { Injectable } from '@angular/core';
import {
  ComponenteDt,
  ComponenteDtVo,
} from '../../../../../../core/commons/vo/componente-dt-vo';

@Injectable({
  providedIn: 'root',
})
export class DatiTecniciConverterService {
  /**
   * Costruttore
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione di conversione da un array di oggetti ComponenteDtVo ad un array ComponenteDt.
   * @param componentiDtVo Array di ComponenteDtVo da convertire.
   * @returns Array di ComponenteDt convertito.
   */
  convertComponentiDtVoToComponentiDt(
    componentiDtVo: ComponenteDtVo[]
  ): ComponenteDt[] {
    // Verifico l'input
    if (componentiDtVo?.length <= 0) {
      // Nessun dato, ritorno array vuoto
      return [];
    }

    // Lancio il map degli oggetti convertendoli singolarmente
    return componentiDtVo.map((c: ComponenteDtVo) => {
      // Lancio la conversione dell'oggetto
      return this.convertComponenteDtVoToComponenteDt(c);
    });
  }

  /**
   * Funzione di conversione da un oggetto ComponenteDtVo a ComponenteDt.
   * @param componenteDtVo Array di ComponenteDtVo da convertire.
   * @returns Array di ComponenteDt convertito.
   */
  convertComponenteDtVoToComponenteDt(
    componenteDtVo: ComponenteDtVo
  ): ComponenteDt {
    // Verifico l'input
    if (!componenteDtVo) {
      // Nessun dato, ritorno undefined
      return undefined;
    }

    // Creo un nuovo oggetto ComponenteDt
    const componenteDt = new ComponenteDt();
    // Converto e setto i dati da un oggetto ComponenteDtVo
    componenteDt.setComponenteDtVoAsComponenteDt(componenteDtVo);

    // Ritorno l'oggetto convertito
    return componenteDt;
  }
}
