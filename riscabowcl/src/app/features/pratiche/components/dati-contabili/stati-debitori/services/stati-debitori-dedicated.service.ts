import { Injectable } from '@angular/core';
import { Moment } from 'moment';
import { RicercaPaginataResponse } from '../../../../../../core/classes/http-helper/http-helper.classes';
import { StatoDebitorioVo } from '../../../../../../core/commons/vo/stato-debitorio-vo';
import { RiscaUtilitiesService } from '../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  RiscaSortTypes,
  RiscaTablePagination,
} from '../../../../../../shared/utilities';

@Injectable()
export class StatiDebitoriDedicatedService {
  /**
   * Costruttore.
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * Funzione che gestisce una richiesta specifica da parte degli analisti.
   * Se la paginazione ha come parametro di ordinamento "data scadenza" allora va applicato anche un ordinamento per l'annualità.
   * @reference RISCA-ISSUES-48
   * @param response RicercaPaginataResponse<StatoDebitorioVo[]> con la risposta dal servizio che scarica gli stati debitori.
   * @param pagination RiscaTablePagination con l'oggetto contenente i dati di paginazione.
   * @returns RicercaPaginataResponse<StatoDebitorioVo[]> con gli stati debitori ordinati per data scadenza e annualità.
   */
  sortDataScandenzaEAnnualita(
    response: RicercaPaginataResponse<StatoDebitorioVo[]>,
    pagination: RiscaTablePagination
  ): RicercaPaginataResponse<StatoDebitorioVo[]> {
    // Verifico l'input
    if (!response || !pagination) {
      // Mancano le configurazioni
      return response;
    }

    // Estraggo la tipologia di ordinamento
    const ordinamento: string = pagination.sortBy;
    // Verifico se l'ordinamento è la data scadenza (deriva dalla tabella: \shared\classes\risca-table\dati-contabili\stati-debitori.table.ts)
    if (ordinamento !== 'dataScadenza') {
      // Non devo fare niente
      return response;
    }

    // Estraggo i dati scaricati
    const sd: StatoDebitorioVo[] = [...response?.sources];
    // Definisco le informazioni per il sort dati
    const sortFn = (a: StatoDebitorioVo, b: StatoDebitorioVo) => {
      // Ritorno la logica di sort definita
      return this.sortSdByDataScadenzaEAnnualita(a, b);
      // #
    };
    const sortType = RiscaSortTypes.decrescente;

    // Lancio la funzione di sort per gli stati debitori
    const sdSorted: StatoDebitorioVo[] = sd.sort(
      (a: StatoDebitorioVo, b: StatoDebitorioVo) => {
        // Ritorno la logica di sort definita
        return this._riscaUtilities.sortObjects(a, b, sortType, sortFn);
      }
    );

    // Ritorno l'oggetto come response paginata
    return { paging: response.paging, sources: sdSorted };
  }

  /**
   * Funzione che definisce le logiche "strette" di ordinamento per gli stati debitori per data scadenza e annualità.
   * @param sdA StatoDebitorioVo da ordinare.
   * @param sdB StatoDebitorioVo da ordinare.
   * @returns number con il risultato di sort.
   */
  private sortSdByDataScadenzaEAnnualita(
    sdA: StatoDebitorioVo,
    sdB: StatoDebitorioVo
  ): number {
    // Verifico l'input
    if (!sdA || !sdA.acc_data_scadenza_pag) {
      // Ritorno l'ordinamento per sdB
      return 1;
    }
    // Verifico l'input
    if (!sdB || !sdB.acc_data_scadenza_pag) {
      // Ritorno l'ordinamento per sdB
      return 1;
    }

    // Estraggo le informazioni per la verifica
    const dtScadSDA: Moment = sdA.acc_data_scadenza_pag;
    const dtScadSDB: Moment = sdB.acc_data_scadenza_pag;
    const annSDA: number = sdA.anno ?? 0;
    const annSDB: number = sdB.anno ?? 0;
    // Verifico se la data scadenza è la stessa
    const sameDtScad: boolean = dtScadSDA.isSame(dtScadSDB);

    // Controllo la condizione
    if (sameDtScad) {
      // La data scadenza è la stessa, verifico l'annualità e ritorno la differenza
      return annSDA - annSDB;
      // #
    } else if (dtScadSDA.isBefore(dtScadSDB)) {
      // Scadenza A prima della scadenza B
      return -1;
      // #
    } else {
      // Scadenza B prima della scadenza A
      return 1;
    }
  }
}
