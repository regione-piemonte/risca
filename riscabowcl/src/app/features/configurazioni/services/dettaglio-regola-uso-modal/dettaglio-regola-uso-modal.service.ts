import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UsoLeggeVo } from '../../../../core/commons/vo/uso-legge-vo';
import { ConfigurazioniService } from '../configurazioni/configurazioni.service';

@Injectable({ providedIn: 'root' })
export class DettaglioRegolaUsoModalService {
  /**
   * Costruttore.
   */
  constructor(private _configurazioni: ConfigurazioniService) {}

  /**
   * ###############################
   * FUNZIONI CHE RICHIAMO I SERVIZI
   * ###############################
   */

  /**
   * Funzione di GET che recupera la lista degli usi effettivi dato un uso di legge di riferimento.
   * La funzione modifica e manipola l'output della get base e la gestisce per la logica FE.
   * @param idUsoPadre number con l'id uso per il riferimento dello scarico dati.
   * @returns Observable<UsoLeggeVo[]> con le informazioni scaricate.
   */
  getUsiEffettivi(idUsoPadre: number): Observable<UsoLeggeVo[]> {
    // Richiamo la funzione del servizio
    return this._configurazioni.getUsiEffettivi(idUsoPadre);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo, che restituisce il path dell'API dal servizio di dipendenza.
   * @returns string con l'API.
   */
  get PATH_TIPI_USO_EFFETTIVI(): string {
    // Recupero il path dal servizio
    return this._configurazioni.PATH_TIPI_USO_EFFETTIVI;
  }
}
