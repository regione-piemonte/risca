import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { RicercaPaginataResponse } from '../../../../core/classes/http-helper/http-helper.classes';
import {
  IPagamentoVo,
  PagamentoVo,
} from '../../../../core/commons/vo/pagamento-vo';
import { IRicercaPagamentiVo } from '../../../../core/commons/vo/ricerca-pagamenti-vo';
import { TipoModalitaPagamentoVo } from '../../../../core/commons/vo/tipo-modalita-pagamento-vo';
import { TipoRicercaPagamentoVo } from '../../../../core/commons/vo/tipo-ricerca-pagamento-vo';
import { ConfigService } from '../../../../core/services/config.service';
import { HttpHelperService } from '../../../../core/services/http-helper/http-helper.service';
import { IFiltriRicercaPagamenti } from '../../../../shared/components/risca/risca-ricerca-pagamenti/utilities/risca-ricerca-pagamenti.interfaces';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { HttpUtilitiesService } from '../../../../shared/services/http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaTablePagination } from '../../../../shared/utilities';

@Injectable({ providedIn: 'root' })
export class RicercaPagamentiService extends HttpUtilitiesService {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts();

  /** Costante per il path: /ricerca_pagamenti */
  private PATH_SEARCH_PAGAMENTI = '/ricerca_pagamenti';

  /**
   * Costruttore.
   */
  constructor(
    config: ConfigService,
    private _httpHelper: HttpHelperService,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(config, riscaUtilities);
  }

  /**
   * ###############################
   * FUNZIONI CHE RICHIAMO I SERVIZI
   * ###############################
   */

  /**
   * Funzione che richiama la ricerca pagamenti.
   * @param searchFilters IRicercaPagamentiVo con i query param per la ricerca.
   * @returns Observable<RicercaPaginataResponse<PagamentoVo[]>> con i risultati di ricerca.
   */
  searchPagamenti(
    searchFilters: IRicercaPagamentiVo,
    paginazione: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<PagamentoVo[]>> {
    // Definisco l'url per il collegamento al servizio del server
    const url: string = this.appUrl(this.PATH_SEARCH_PAGAMENTI);
    // Definisco i query params per la chiamata
    const params = this.createPagingParams(paginazione);
    // Definisco le options
    const options = { params };

    // Lancio la chiamata per il recupero dati
    return this._httpHelper.searchWithPost(url, searchFilters, options).pipe(
      map((res: RicercaPaginataResponse<IPagamentoVo[]>) => {
        // Ritorno la response convertita
        return this.paginatePagamenti(res);
      })
    );
  }

  /**
   * ################################
   * FUNZIONI DI UTILITY DEL SERVIZIO
   * ################################
   */

  /**
   * Funzione che lancia le logiche di conversione dati e gestione paginazione per i pagamenti.
   * @param res RicercaPaginataResponse<IPagamentoVo[]> con la risposta ottenuta dal server.
   * @returns RicercaPaginataResponse<PagamentoVo[]> come risposta convertita.
   */
  private paginatePagamenti(
    res: RicercaPaginataResponse<IPagamentoVo[]>
  ): RicercaPaginataResponse<PagamentoVo[]> {
    // Recupero la lista di oggetti ritornati dal server
    const { sources } = res;
    // Effettuo un parse dei dati in una lista FE friendly
    const pagamenti: PagamentoVo[] = sources?.map((iSD: IPagamentoVo) => {
      // Creo l'oggetto di FE
      return new PagamentoVo(iSD);
    });
    // Creo una nuova response da ritornare
    const newPag: RicercaPaginataResponse<PagamentoVo[]> = {
      paging: res.paging,
      sources: pagamenti,
    };

    // Ritorno la response modificata
    return newPag;
  }

  /**
   * #######################
   * FUNZIONI DI CONVERSIONI
   * #######################
   */

  /**
   * Funzione di conversione che converte le informazioni generate dal form di ricerca RiscaRicercaPagamenti in un oggetto compatibile con i parametri di ricerca per i query param del servizio.
   * @param f IFiltriRicercaPagamenti da convertire.
   * @returns RicercaPagamenti convertito.
   */
  convertIFiltriRicercaPagamentiToIRicercaPagamenti(
    f: IFiltriRicercaPagamenti
  ): IRicercaPagamentiVo {
    // Verifico l'input
    if (!f) {
      // Nessuna configurazione
      return;
    }

    // Estraggo dall'oggetto del form i dati per la ricerca
    const stato_pagamento: TipoRicercaPagamentoVo = f.statoPagamento;
    this._riscaUtilities.sanitizeFEProperties(stato_pagamento);
    const modalita_pagamento: TipoModalitaPagamentoVo = f.modalitaPagamento;
    this._riscaUtilities.sanitizeFEProperties(modalita_pagamento);
    
    let data_op_da: string;
    data_op_da = this._riscaUtilities.convertNgbDateStructToServerDate(
      f.dataOperazioneValutaDa
    );
    let data_op_a: string;
    data_op_a = this._riscaUtilities.convertNgbDateStructToServerDate(
      f.dataOperazioneValutaA
    );
    const quinto_campo: string = f.quintoCampo;
    const codice_avviso: string = f.codiceAvviso;
    const cro: string = f.codiceRiferimentoOperazione;
    const importo_da: number = f.importoDa;
    const importo_a: number = f.importoA;
    const soggetto_versamento: string = f.soggetto;

    // Creo un oggetto per i query param
    const queryFiltrers: IRicercaPagamentiVo = {
      stato_pagamento,
      modalita_pagamento,
      data_op_da,
      data_op_a,
      quinto_campo,
      codice_avviso,
      cro,
      importo_da,
      importo_a,
      soggetto_versamento,
    };

    // Ritorno l'oggetto con i dati per la query
    return queryFiltrers;
  }
}
