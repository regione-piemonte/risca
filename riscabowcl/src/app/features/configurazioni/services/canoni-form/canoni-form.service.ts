import { Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import {
  IRiscaAnnoSelect,
  IRiscaServerError,
  RiscaServerError,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { ConfigurazioniService } from '../configurazioni/configurazioni.service';

@Injectable({ providedIn: 'root' })
export class CanoniFormService {
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
   * Funzione di GET che recupera la lista degli anni disponibili per i tipi usi regole, dato un id ambito di riferimento.
   * La funzione modifica e manipola l'output della get base e la gestisce per la logica FE.
   * @param idAmbito number con l'id ambito da utilizzare per la ricerca dati.
   * @returns Observable<number[]> con le informazioni scaricate.
   */
  getAnniUsiRegole(idAmbito: number): Observable<IRiscaAnnoSelect[]> {
    // Richiamo la funzione del servizio
    return this._configurazioni.getAnniUsiRegole(idAmbito).pipe(
      switchMap((anni: number[]) => {
        // Verifico se l'array restituito è vuoto
        if (!anni || anni.length == 0) {
          // La lista vuota definisco le informazioni per la gestione dell'errore
          const code = RiscaNotifyCodes.I040;
          const title = `[RISCA_FE] ${this.PATH_ANNI_REGOLA} returned an empty response`;
          const error: IRiscaServerError = { code, title };
          // Genero e ritorno un errore
          const e = new RiscaServerError(error);
          return throwError(e);
          // #
        } else {
          // Lista con valori, la rigiro
          return of(anni);
          // #
        }
      }),
      map((anni: number[]) => {
        // Converto la lista in oggetti IRiscaAnnoSelect
        return anni.map((anno: number, i: number) => {
          // Definisco un oggetto IRiscaAnnoSelect
          let ras: IRiscaAnnoSelect;
          ras = { anno };

          // Il servizio, da documentazione, riporta che il primo elemento della lista deve essere quello selezionato di default
          if (i === 0) {
            // "sovrascrivo" l'oggetto definendo la proprietà __selected
            ras = { ...ras, ...{ __selected: true } };
            // #
          }

          // Ritorno l'oggetto
          return ras;
        });
      })
    );
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
  get PATH_ANNI_REGOLA(): string {
    // Recupero il path dal servizio
    return this._configurazioni.PATH_ANNI_REGOLA;
  }
}
