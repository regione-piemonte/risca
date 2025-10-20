import { Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { JsonRegolaVo } from '../../../../core/commons/vo/json-regola-vo';
import {
  RegolaUsoVo,
  TipoCanoneRegolaUso,
} from '../../../../core/commons/vo/regola-uso-vo';
import { RiscaTablePagination } from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { ConfigurazioniService } from '../configurazioni/configurazioni.service';
import { IRicercaUsiRegola } from '../configurazioni/utilities/configurazioni.interfaces';
import { IPutListaRegoleParams } from './utilities/canoni.interfaces';

@Injectable({ providedIn: 'root' })
export class CanoniService {
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
   * Funzione di GET che recupera la lista dei tipi usi regola disponibili, dato un id ambito ed un anno di riferimento.
   * La funzione modifica e manipola l'output della get base e la gestisce per la logica FE.
   * @param pathParams IRicercaUsiRegola con le informazioni di ricerca in path params.
   * @param paginazione RiscaTablePagination con l'oggetto di ricerca per la paginazione.
   * @returns Observable<RicercaPaginataResponse<RegolaUsoVo[]>> con le informazioni scaricate.
   */
  getListaRegolePaginated(
    pathParams: IRicercaUsiRegola,
    paginazione?: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<RegolaUsoVo[]>> {
    // Richiamo la funzione del servizio
    return this._configurazioni
      .getListaRegolePaginated(pathParams, paginazione)
      .pipe(
        switchMap((ricerca: RicercaPaginataResponse<RegolaUsoVo[]>) => {
          // Verifico se l'array restituito è vuoto
          if (!ricerca?.sources || ricerca.sources.length == 0) {
            // La lista vuota definisco le informazioni per la gestione dell'errore
            const code = RiscaNotifyCodes.E005;
            const path = this.PATH_LISTA_REGOLE;
            // Genero e ritorno un errore
            const e = this._configurazioni.feError(code, path);
            return throwError(e);
            // #
          } else {
            // Lista con valori, la rigiro
            return of(ricerca);
            // #
          }
        })
      );
  }

  /**
   * Funzione di PUT che effettua l'aggiornamento della lista di regole uso.
   * La funzione modifica e manipola l'output della put base e la gestisce per la logica FE.
   * @param params IPutListaRegoleParamscon le informazioni per eseguire la PUT dei dati e l'aggiornamento completo di FE.
   * @returns Observable<RicercaPaginataResponse<RegolaUsoVo[]>> con le informazioni aggiornate e con lo scarico paginato delle informazioni.
   */
  putListaRegoleWithPaginatedResponse(
    params: IPutListaRegoleParams
  ): Observable<RicercaPaginataResponse<RegolaUsoVo[]>> {
    // Estraggo dall'input i parametri
    const { regoleModificate, pathParams, paginazione } = params ?? {};

    // Invoco la chiamata di aggiornamento del servizio
    return this._configurazioni.putListaRegole(regoleModificate).pipe(
      switchMap((res: RegolaUsoVo[]) => {
        // Ignoro la risposta ottenuta e rilancio la ricerca paginata
        return this.getListaRegolePaginated(pathParams, paginazione);
        // #
      })
    );
  }

  /**
   * ###################
   * FUNZIONI DI UTILITY
   * ###################
   */

  /**
   * Funzione di controllo che verifica le informazioni definite per le regole degli usi mediante proprietà di fe:
   * - __json_regola_canone;
   * - __json_regola_tipo_canone;
   * Con le rispettive informazioni di canone della regola uso.
   * Se i valori differiscono, vuol dire che la regola è stata aggiornata.
   * Tutti gli usi/regola aggiornati verranno restituiti dalla funzione.
   * @param regole RegolaUsoVo[] con la lista da analizzare.
   * @returns RegolaUsoVo[] con la lista di tutti gli oggetti che risultano aggiornati.
   */
  canoniRegoleModificati(regole: RegolaUsoVo[]): RegolaUsoVo[] {
    // Verifico l'input
    if (!regole || regole.length == 0) {
      // Non ci sono dati
      return [];
    }

    // Definisco il contenitore per le regole modificate
    const regoleMod: RegolaUsoVo[] = [];

    // Itero le regole in input
    for (let i = 0; i < regole.length; i++) {
      // Recupero la regola dall'array per indice
      let r: RegolaUsoVo;
      r = regole[i];

      // Estraggo le informazioni di FE per le verifiche
      const __jsrc = r.__json_regola_canone;
      const __jsrcTipo = r.__json_regola_tipo_canone;

      // Estraggo le informazioni della regola
      const jsonRegola: JsonRegolaVo = r.json_regola_obj;
      // Verifico esista effettivamente una regola
      if (!jsonRegola) {
        // Salto le verifiche e passo al prossimo elemento
        continue;
      }

      // Estraggo le informazioni dell'oggetto della regola
      const canoneUni: number = jsonRegola.canone_unitario;
      const canonePerc: number = jsonRegola.canone_percentuale;

      // Definisco il flag che definisce se è stata fatta una modifica
      let isRegolaMod: boolean = false;

      // Verifico qual è il valore da prendere come riferimento
      switch (__jsrcTipo) {
        case TipoCanoneRegolaUso.unitario:
          // Il check è da fare sul canone unitario
          isRegolaMod = __jsrc != canoneUni;
          break;
        // #
        case TipoCanoneRegolaUso.percentuale:
          // Il check è da fare sul canone percentuale
          isRegolaMod = __jsrc != canonePerc;
          break;
        // #
      }

      // Verifico il risultato del check
      if (isRegolaMod) {
        // Il canone definito FE è diverso dal canone della regola, aggiungo la regola come modificata
        regoleMod.push(r);
        // #
      }
    }

    // Ritorno la lista di regole aggiornate
    return regoleMod;
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
  get PATH_LISTA_REGOLE(): string {
    // Recupero il path dal servizio
    return this._configurazioni.PATH_LISTA_REGOLE;
  }
}
