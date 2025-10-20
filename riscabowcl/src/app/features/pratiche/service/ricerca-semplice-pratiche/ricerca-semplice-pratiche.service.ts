import { Injectable } from '@angular/core';
import { forkJoin, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { RiscossioneSearchV2Vo } from 'src/app/core/commons/vo/riscossione-search-vo';
import { PraticheService } from 'src/app/features/pratiche/service/pratiche.service';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import { RiscossioneSearchResultVo } from '../../../../core/commons/vo/riscossione-search-result-vo';
import { StatoRiscossioneVo } from '../../../../core/commons/vo/stati-riscossione-vo';
import { UserService } from '../../../../core/services/user.service';
import {
  IRicercaPraticaSempliceRes,
  RicercaPraticaSemplice,
  RiscaTablePagination,
} from '../../../../shared/utilities';

@Injectable({
  providedIn: 'root',
})
export class RicercaSemplicePraticheService {
  /**
   * Costruttore.
   */
  constructor(
    private _praticheService: PraticheService,
    private _user: UserService
  ) {}

  /**
   * Chiama il BE per ottenere la lista di pratiche per la ricerca semplice
   * @param filtri RicercaPraticaSemplice
   * @param paginazione RiscaTablePagination
   * @returns Observable<RicercaPaginataResponse>
   */
  public getRicercaPraticaSemplice(
    filtri: RicercaPraticaSemplice,
    paginazione?: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<IRicercaPraticaSempliceRes[]>> {
    // Creo l'oggetto della richiesta
    const riscossioneSearch: RiscossioneSearchV2Vo =
      this.convertDatiRicercaPraticaSempliceReqToRiscossioneSearch(filtri);

    // Creo la richiesta e chiedo gli stati per il mapping
    const requests = [
      this._praticheService.searchPratiche(riscossioneSearch, paginazione),
      this._praticheService.getStatiRiscossione(this._user.idAmbito),
    ];
    // Eseguo la chiamata
    return forkJoin(requests).pipe(
      map((res: any[]) => {
        // Estraggo dalla response i dati per la paginazione, le pratiche e gli stati.
        const response = res[0] as RicercaPaginataResponse<RiscossioneSearchResultVo[]>;
        const stati = res[1] as StatoRiscossioneVo[];
        const pratiche = response.sources as RiscossioneSearchResultVo[];
        // Ottengo le pratiche con dentro gli stati
        const praticheComplete =
          this.convertRiscossioneSearchResultVoToDatiRicercaPraticaSempliceRes(
            pratiche,
            stati
          );
        // Restituisco l'oggetto con dentro le pratiche e la paginazione
        return new RicercaPaginataResponse<IRicercaPraticaSempliceRes[]>({
          sources: praticheComplete,
          paging: response.paging,
        });
      })
    );
  }

  /**
   * Converte l'oggetto ottenuto dalla form di ricerca semplice nell'oggetto contenente i filtri della ricerca.
   * @param search Oggetto con i parametri di ricerca.
   * @returns Oggetto per la ricerca riscossione. Deve avere i campi vuoti a null e non stringa vuota secondo le specifiche del BE.
   */
  private convertDatiRicercaPraticaSempliceReqToRiscossioneSearch(
    search: RicercaPraticaSemplice
  ): RiscossioneSearchV2Vo {
    return new RiscossioneSearchV2Vo({
      numero_pratica:
        search?.numeroPratica == '' ? null : search?.numeroPratica,
      codice_fiscale:
        search?.codiceFiscale == '' ? null : search?.codiceFiscale,
      ragione_sociale:
        search?.ragioneSocialeOCognome == ''
          ? null
          : search?.ragioneSocialeOCognome,
      cod_riscossione: search?.codiceUtenza == '' ? null : search?.codiceUtenza,
      nap:
        search?.numeroAvvisoPagamento == ''
          ? null
          : search?.numeroAvvisoPagamento,
      codice_avviso: search?.codiceAvviso == '' ? null : search?.codiceAvviso,
    });
  }

  /**
   * Converte il risultato della ricerca semplice nell'oggetto contenente i dati da mostrare in tabella.
   * @param riscossioni le riscossioni ottenute dalla ricerca.
   * @returns le riscossioni ottenute dalla ricerca ma del tipo usato dalle tabelle.
   */
  private convertRiscossioneSearchResultVoToDatiRicercaPraticaSempliceRes(
    riscossioni: RiscossioneSearchResultVo[] = [],
    statiRiscossione: StatoRiscossioneVo[] = []
  ): IRicercaPraticaSempliceRes[] {
    const datiRicercaResponse: IRicercaPraticaSempliceRes[] = [];
    // per ogni riscossione creo un oggetto convertito inserendo lo stato
    riscossioni.forEach((r) => {
      const datiRicerca: IRicercaPraticaSempliceRes = {
        id_riscossione: r.id_riscossione,
        codiceUtenza: r.codice_utenza,
        titolare: r.titolare,
        numero: r.numero_pratica,
        procedimento: r.procedimento,
        stato: statiRiscossione.find((x) => x.cod_stato_riscossione == r.stato),
      };
      // Inserisco la riscossione convertita
      datiRicercaResponse.push(datiRicerca);
    });

    return datiRicercaResponse;
  }
}
