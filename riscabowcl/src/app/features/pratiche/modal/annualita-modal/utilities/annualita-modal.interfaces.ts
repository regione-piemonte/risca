import { AppActions } from 'src/app/shared/utilities';
import { AnnualitaSDVo } from '../../../../../core/commons/vo/annualita-sd-vo';
import { PraticaEDatiTecnici } from '../../../../../core/commons/vo/pratica-vo';
import { StatoDebitorioVo } from '../../../../../core/commons/vo/stato-debitorio-vo';

/**
 * Interfaccia che definisce i parametri da passare alla modale.
 */
export interface IAnnualitaModalConfigs {
  /** StatoDebitorioVo con le informazioni dello stato debitorio di riferimento. */
  statoDebitorio: StatoDebitorioVo;
  /** Modalità con la modalità scelta per la modale. Inserimento o Modifica */
  modalita: AppActions;
  /** PraticaEDatiTecnici con le informazioni di pratica e dati tecnici di riferimento. */
  praticaEDT: PraticaEDatiTecnici;
  /** AnnualitaSDVo con le informazioni dell'annualità di riferimento per la modifica. */
  annualita?: AnnualitaSDVo;
  /** Boolean che specifica se, per le annualità in lavorazione da parte dall'utente, ne esiste già una con il flag rateo prima annualità. */
  rateoPrimaAnnualita?: boolean;
}
