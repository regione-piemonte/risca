import { IRiscaCheckboxData, IRiscaRadioData } from 'src/app/shared/utilities';
import { DettaglioPagSearchResultVo } from '../../../../../core/commons/vo/dettaglio-pag-search-result-vo';
import { PagamentoNonProprioVo } from '../../../../../core/commons/vo/pagamento-non-proprio-vo';
import { PagamentoVo } from '../../../../../core/commons/vo/pagamento-vo';
import { StatoDebitorioVo } from '../../../../../core/commons/vo/stato-debitorio-vo';
import { ITipoImpNonProprioVo } from '../../../../../core/commons/vo/tipo-importo-non-proprio-vo';
import { RiscaNotifyCodes } from '../../../../utilities/enums/risca-notify-codes.enums';

/**
 * Interfaccia di comodo che definisce i dati della form per il pagamento bollettazione.
 */
export interface IPagamentoBollettazioneForm {
  soggettoVersamento: string;
  nap: string;
  codiceUtenza: string;
  importoNonIdentificato?: number;
  importoNonDiCompetenza?: number;
  importoDaRimborsare?: number;
  rimborsato: IRiscaCheckboxData;
  importoDaAssegnare?: number;
  note: string;
}

/**
 * Interfaccia che rappresenta l'oggetto ritornato del form di gestione del pagamento bollettazione.
 */
export interface IPagamentoBollettazioneData {
  pagamento: PagamentoVo;
  dettagliPag?: DettaglioPagSearchResultVo[];
  dettagliPagCancellati?: DettaglioPagSearchResultVo[];
  statiDebitoriSelezionati?: StatoDebitorioVo[];
}

/**
 * Interfaccia che rappresenta le informazioni per la verifica degli importi per il componente.
 */
export interface IVerificaImporti {
  pagamentoDaVisionare: boolean;
  importoPagamento?: number;
  dettagliPagamento?: DettaglioPagSearchResultVo[];
  statiDebitoriSel?: StatoDebitorioVo[];
  importoNonIdentificato?: number;
  importoNonDiCompetenza?: number;
  importoDaRimborsare?: number;
}

/**
 * Interfaccia che rappresenta il risultato della verifica degli importi per il componente.
 */
export interface IVerificaImportiResult {
  /** Boolean con il risultato della verifica. */
  validated: boolean;
  /** RiscaNotifyCodes con il codice di notifica da comunicare all'utente. */
  notifica?: RiscaNotifyCodes;
  /** Number che definisce il valore di scarto tra gli importi stati debitori/importi "non propri" ed il valore dell'importo del pagamento. */
  residuo?: number;
}

/**
 * Interfaccia che rappresenta il risultato della verifica sull'automatizzazione dati per stato debitorio singolo.
 * Maggiori dettagli nelle funzioni di gestione.
 */
export interface IAutoImportoVersatoSD {
  verified: boolean;
  statoDebitorioUpd?: StatoDebitorioVo;
}

/**
 * Interfaccia che rappresenta il risultato della verifica sull'automatizzazione dati per almeno 2 stati debitori e i propri importi mancanti/eccedenti.
 * Maggiori dettagli nelle funzioni di gestione.
 */
export interface IImportiMancantiEccedentiSDAsImportoPagamento {
  verified: boolean;
  statiDebitoriUpd?: StatoDebitorioVo[];
}

/**
 * Interfaccia che definisce l'oggetto utilizzato per l'aggiornamento dati per i pagamenti non propri.
 */
export interface IAggiornaPagNonPropri {
  importo: number;
  tipo: ITipoImpNonProprioVo;
}

/**
 * Interfaccia che definisce i paramtri per la generazione di un oggetto PagamentoNonPropriVo.
 */
export interface IGeneraPagamentoNonProprio {
  importo: number;
  tipo: ITipoImpNonProprioVo;
  idPagamento: number;
  pagNonPropri?: PagamentoNonProprioVo[];
}

/**
 * Interfaccia che definisce le informazioni emesse al momento del verifica importi.
 */
export interface IVerificaImportiSuccess {
  notifica: RiscaNotifyCodes;
  salvaDopoVerifica?: boolean;
}

/**
 * Interfaccia che definisce le informazioni di ricerca per gli stati debitori, rispetto ai dati estratti dalla form.
 */
export interface IRicercaSDPagamentoForm {
  targetRicercaStatiDebitori?: IRiscaRadioData;
  ricercaPuntuale?: string;
  targetRicercaTitolare?: IRiscaRadioData;
  ricercaTitolare?: string;
  importoDa?: number;
  importoA?: number;
  sdDaEscludere?: number[];
}