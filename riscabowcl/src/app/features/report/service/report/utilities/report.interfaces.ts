import { IReportLocationVo } from '../../../../../core/commons/vo/report-location-vo';
import { ReportResultVo } from '../../../../../core/commons/vo/report-result-vo';
import { RiscaFruitori } from '../../../../../shared/utilities';
import { IRicercaMorosita } from '../../../../pagamenti/service/ricerca-morosita/utilities/ricerca-morosita.interfaces';

/**
 * Interfaccia che definisce le informazioni gestite a seguito dei risultati ottenuti dal polling sugli stati di generazione dei report.
 */
export interface IPollReportsStatus {
  /** IReportLocationVo[] con la lista di tutti gli oggetti per richiesti per la verifica sullo stato generazione dei report. */
  request: IReportLocationVo[];
  /** IReportLocationVo[] con la lista dei report che devono ancora essere completati. */
  actual: IReportLocationVo[];
  /** ReportResultVo[] con la lista di tutti report completati. */
  completed: ReportResultVo[];
  /** ReportResultVo[] con la lista di tutti report che risultano essere andati in errore. */
  errors: ReportResultVo[];
  /** ReportResultVo[] con la lista di tutti report ancora in corso di generazione. */
  inProgress: ReportResultVo[];
  /** ReportResultVo[] con la lista di tutti i report che hanno generato uno stato sconosciuto. */
  unknown?: ReportResultVo[];
}

/**
 * Interfaccia che definisce le informazioni per la generazione di un documento ruolo. 
 */
export interface ICreaRuoloMorosita extends IRicercaMorosita {
  // fruitore: RiscaFruitori.RISCA; // "RISCA" come valore specifico per l'applicazione 
  // tipoRicercaMorosita: 'RICMOR08' - Valore fisso specifico per la "creazione ruolo" x ricerca morosità con il valore per il tipo ricerca morosita CodiciTipoRicercaMorosita.DA_INVIARE_A_RISCOSSIONE_COATTIVA
}

/**
 * Interfaccia che definisce le informazioni per i query params per la generazione di un report sulla ricerca pagamenti.
 */
export interface IRicercaPagamentiParams {
  fruitore?: RiscaFruitori.RISCA; // "RISCA" come valore specifico per l'applicazione 
}

/*
 * Interfaccia che definisce le informazioni per i query params di generazione report bilancio.
 */
export interface IReportBilancioParams {
  fruitore?: RiscaFruitori.RISCA; // "RISCA" come valore specifico per l'applicazione 
  asynch?: boolean;
  anno: number;
}

/**
 * Interfaccia che definisce le informazioni per i query params di generazione report vincoli competenza.
 */
export interface IReportVincoliCompetenzaParams {
  fruitore?: RiscaFruitori.RISCA; // "RISCA" come valore specifico per l'applicazione 
  asynch?: boolean;
  dataInizio: string;
  dataFine: string;
}