import { Injectable } from '@angular/core';
import { ComponenteDt } from '../../../../../../../core/commons/vo/componente-dt-vo';
import { DatiTecniciService } from '../../dati-tecnici/dati-tecnici.service';

@Injectable({
  providedIn: 'root',
})
export class RicercaDatiTecniciService {
  /**
   * Costruttore
   */
  constructor(private _datiTecnici: DatiTecniciService) {}

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  public get ricercaDt(): ComponenteDt {
    return this._datiTecnici.ricercaDt;
  }
}
