import * as moment from 'moment';
import { Moment } from 'moment';
import { convertViewDateToMoment } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import { RiscaFormatoDate } from '../../../utilities';
import { DataStandardSalvataDaDatabase } from '../tassi-di-interesse/utilities/tassi-di-interessi.const';

/**
 * Funzione per la tabella dei tassi di interesse che gestisce la logica per la visualizzazione della data fine.
 * Se la data fine risulta nel futuro rispetto ad oggi, allora bisogna visualizzare "IN CORSO".
 * Altrimenti si visualizza la data.
 * @param dataFine RiscaFormatoDate.view con la data fine da gestire in formato "view date".
 * @returns string con il risultato della gestione da visualizzare.
 */
export const gestisciDataFine = (dataFine: RiscaFormatoDate.view): string => {
  // Verifico esista l'input
  if (!dataFine) {
    // Manca l'input
    return '';
    // #
  }

 const TDI_C = new DataStandardSalvataDaDatabase();
  // Definisco la data di oggi come moment per la verifica
  const oggi: Moment = moment();
  // Converto la data fine in moment
  const dataFineMoment: Moment = convertViewDateToMoment(dataFine);
  // Verifico se la data di fine è nel futuro rispetto ad oggi
  //const dataSalvataDaDB: string = DataStandardSalvataDaDatabase;
  const dataSalvataDaDB = TDI_C.DATA_SALVATA;
  //formatto la data fine in modo da poterla confrontare con la data di oggi
  const dataFineMomentFormat = dataFineMoment.format('YYYY-MM-DD');
  
  /** confronto che la data fine sia dopo oggi && dev'essere diversa dalla stringa che viene salvata
   * nel db per indicare che la data non è stata ancora impostata => "2100-12-31"
   */
  if (dataFineMoment.isAfter(oggi) && dataFineMomentFormat == dataSalvataDaDB ) {
    // La data di fine non è ancora giunta
    return ``;
    // #
  } else {
    // La data fine è stata passata
    return dataFine;
    // #
  }
};
