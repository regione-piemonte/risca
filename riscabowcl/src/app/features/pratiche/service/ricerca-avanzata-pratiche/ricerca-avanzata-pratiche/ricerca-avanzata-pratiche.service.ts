import { Injectable } from '@angular/core';
import { forkJoin, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { RicercaPaginataResponse } from 'src/app/core/classes/http-helper/http-helper.classes';
import { RiscossioneSearchResultVo } from 'src/app/core/commons/vo/riscossione-search-result-vo';
import { RiscossioneSearchV2Vo } from 'src/app/core/commons/vo/riscossione-search-vo';
import { PraticheService } from 'src/app/features/pratiche/service/pratiche.service';
import { StatoRiscossioneVo } from '../../../../../core/commons/vo/stati-riscossione-vo';
import { IRicercaPaginataResponse } from '../../../../../core/interfaces/http-helper/http-helper.interfaces';
import { UserService } from '../../../../../core/services/user.service';
import {
  IRicercaPraticaAvanzataReduced,
  RiscaTablePagination,
} from '../../../../../shared/utilities';
import {
  IRicercaPraticaAvanzataReq,
  IRicercaPraticaAvanzataRes,
} from './utilities/ricerca-avanzata-pratiche.interfaces';

@Injectable({
  providedIn: 'root',
})
export class RicercaAvanzataPraticheService {
  constructor(
    private _praticheService: PraticheService,
    private _user: UserService
  ) {}

  /**
   * Funzione di ricerca per le pratiche, con configurazione per la versione avanzata.
   * @param riscossioneSearch RiscossioneSearchV2Vo contenente le informazioni per i filtri di ricerca.
   * @param paginazione RiscaTablePagination con l'oggetto per la gestione della paginazione.
   * @returns Observable<IRicercaPraticaAvanzataRes[]> con le configurazioni dei dati generali amministrativi.
   */
  public getRicercaPraticaAvanzata(
    riscossioneSearch: RiscossioneSearchV2Vo,
    paginazione?: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<IRicercaPraticaAvanzataReduced[]>> {
    // Variabili di comodo
    const search = riscossioneSearch;
    const pag = paginazione;
    const idA = this._user.idAmbito;

    // Creo la richiesta e chiedo gli stati per il mapping
    const req: IRicercaPraticaAvanzataReq = {
      ricercaPratiche: this._praticheService.searchPratiche(search, pag),
      statiRiscossione:
        this._praticheService.getStatiRiscossione(idA),
    };

    // Eseguo la chiamata
    return forkJoin(req).pipe(
      map((res: IRicercaPraticaAvanzataRes) => {
        // Estraggo le informazioni dalla response
        const { ricercaPratiche, statiRiscossione } = res;

        // Estraggo dalla ricerca paginata le informazioni delle pratiche
        const pratiche: RiscossioneSearchResultVo[] = ricercaPratiche.sources;
        // Ottengo la pratica convertita e assieme passo i dati di paginazione provenienti dal BE
        const sources = this.searchRiscossioniToReduced(
          pratiche,
          statiRiscossione
        );
        // Recupero la paginazione generata per la ricerca delle pratiche
        const paging = ricercaPratiche.paging;

        // Creo l'oggetto di configurazione per ottenere una ricerca paginata response
        let c: IRicercaPaginataResponse<IRicercaPraticaAvanzataReduced[]>;
        c = { sources, paging };

        // Genero e ritorno un nuovo oggetto RicercaPaginataResponse
        return new RicercaPaginataResponse<IRicercaPraticaAvanzataReduced[]>(c);
        // #
      })
    );
  }

  /**
   * Funzione di supporto per la conversione delle informazioni.
   * Partendo da: RiscossioneSearchResultVo[] & StatoRiscossioneVo[];
   * Ottenendo: IRicercaPraticaAvanzataReduced[];
   * @param p RiscossioneSearchResultVo[] con le informazioni da convertire.
   * @param sr StatoRiscossioneVo[] con le informazioni da convertire.
   * @returns IRicercaPraticaAvanzataReduced[] con le informazioni convertite.
   */
  private searchRiscossioniToReduced(
    p: RiscossioneSearchResultVo[],
    sr: StatoRiscossioneVo[]
  ): IRicercaPraticaAvanzataReduced[] {
    // Converto le informazioni
    const r =
      this.convertRiscossioneSearchResultVoToDatiRicercaPraticaAvanzataRes(
        p,
        sr
      );
    // Ritorno le informazioni
    return r;
  }

  /**
   * Converte il risultato della ricerca avanzata nell'oggetto contenente i dati da mostrare in tabella.
   * @param riscossioni le riscossioni ottenute dalla ricerca.
   * @returns le riscossioni ottenute dalla ricerca ma del tipo usato dalle tabelle.
   */
  convertRiscossioneSearchResultVoToDatiRicercaPraticaAvanzataRes(
    riscossioni: RiscossioneSearchResultVo[],
    statiRiscossione: StatoRiscossioneVo[]
  ): IRicercaPraticaAvanzataReduced[] {
    const datiRicercaResponse: IRicercaPraticaAvanzataReduced[] = [];
    // Per ogni riscossione, creo il risultato convertito inserendo lo stato
    riscossioni.forEach((r: RiscossioneSearchResultVo) => {
      const datiRicerca: IRicercaPraticaAvanzataReduced = {
        id_riscossione: r.id_riscossione,
        codiceUtenza: r.codice_utenza,
        titolare: r.titolare,
        numero: r.numero_pratica,
        procedimento: r.procedimento,
        stato: statiRiscossione.find((x) => x.cod_stato_riscossione == r.stato),
        comuneOperaDiPresa: r.comune_opera_di_presa,
        corpoIdrico: r.corpo_idrico,
        nomeImpianto: r.nome_impianto,
        total: r.total,
      };
      // Inserisco la riscossione convertita
      datiRicercaResponse.push(datiRicerca);
    });

    return datiRicercaResponse;
  }
}
