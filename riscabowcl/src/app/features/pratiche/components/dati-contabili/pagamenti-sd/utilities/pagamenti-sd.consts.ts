import { IJourneySnapshot, IJStepConfig } from "../../../../../../core/interfaces/navigation-helper/navigation-helper.interfaces";
import { AppCallers, AppClaimants, AppRoutes } from "../../../../../../shared/utilities";


/**
 * Interfaccia strettamente collegata alla costante: PagamentiSDConsts.
 */
export interface IPagamentiSDConsts {
  DATI_UTENZA: string;
  ACCORDION_NUOVO_VERSAMENTO: string;

  LABEL_SOGGETTO_VERSAMENTO: string;
  LABEL_NOTE: string;
  LABEL_IMPORTO_VERSAMENTO: string;
  LABEL_MODALITA_PAGAMENTO: string;
  LABEL_DATA_VERSAMENTO: string;
  LABEL_RIMBORSATO: string;
  LABEL_CODICE_UTENZA: string;
  LABEL_NAP: string;

  LABEL_NAP_ESTESO: string;
  LABEL_NON_IDENTIFICATO: string;
  LABEL_NON_DI_COMPETENZA: string;
  LABEL_DA_RIMBORSARE: string;
  LABEL_IMP_DA_ASSEGNARE: string;

  DATA_VERSAMENTO: string;
  IMPORTO_VERSAMENTO: string;
  TIPO_MODALITA_PAGAMENTO: string;

  CODICE_UTENZA: string;
  NAP: string;
  SOGGETTO_VERSAMENTO: string;
  NOTE: string;
  NON_IDENTIFICATO_IMP_VERSATO: string;
  NON_DI_COMPETENZA_IMP_VERSATO: string;
  DA_RIMBORSARE_IMP_VERSATO: string;
  IMP_DA_ASSEGNARE: string;
  FLAG_RIMBORSATO: string;

  FORM_KEY_PARENT_SD_VERSAMENTI: string;
  FORM_KEY_CHILD_SD_VERSAMENTI: string;

  DETTAGLIO_PAGAMENTO: string;
  RISCA_TABLE_ACTION: string;

  MODALITA_PAGAMENTO_MANUALE: string;
  MODALITA_PAGAMENTO_MANUALE_COD: string;
  // Tipi di Imp_Non_Propri per i flag
  NON_IDENTIFICATO_COD: string;
  NON_DI_COMPETENZA_COD: string;
  DA_RIMBORSARE_COD: string;
  // FORM PAGAMENTI
  PAGAMENTI: string;
}

/**
 * Costante che rappresenta le funzionalità presenti su db per la parte dei pagamenti.
 */
export const PagamentiSDConsts: IPagamentiSDConsts = {
  DATI_UTENZA: 'Dati utenza',
  ACCORDION_NUOVO_VERSAMENTO: 'Nuovo Pagamento',

  LABEL_SOGGETTO_VERSAMENTO: 'Soggetto versamento',
  LABEL_DATA_VERSAMENTO: 'Data pagamento',
  LABEL_IMPORTO_VERSAMENTO: 'Importo versato',
  LABEL_MODALITA_PAGAMENTO: 'Modalità di pagamento',
  LABEL_NOTE: 'Note',
  LABEL_RIMBORSATO: 'Rimborsato',
  LABEL_CODICE_UTENZA: 'Codice utenza',
  LABEL_NAP: 'NAP',

  LABEL_NAP_ESTESO: 'NAP - Numero Avviso Pagamento',
  LABEL_NON_IDENTIFICATO: 'Non Identificato',
  LABEL_NON_DI_COMPETENZA: 'Non di competenza',
  LABEL_DA_RIMBORSARE: 'Da rimborsare',
  LABEL_IMP_DA_ASSEGNARE: 'Importo da assegnare',

  CODICE_UTENZA: 'codice_utenza',
  NAP: 'nap',
  SOGGETTO_VERSAMENTO: 'soggetto_versamento',
  DATA_VERSAMENTO: 'data_op_val',
  IMPORTO_VERSAMENTO: 'importo_versato',
  TIPO_MODALITA_PAGAMENTO: 'tipo_modalita_pag',
  NOTE: 'note',
  NON_IDENTIFICATO_IMP_VERSATO: 'non_identificato_imp_versato',
  NON_DI_COMPETENZA_IMP_VERSATO: 'non_di_competenza_imp_versato',
  DA_RIMBORSARE_IMP_VERSATO: 'da_rimborsare_imp_versato',
  IMP_DA_ASSEGNARE: 'imp_da_assegnare',
  FLAG_RIMBORSATO: 'flg_rimborsato',

  FORM_KEY_PARENT_SD_VERSAMENTI: 'FORM_KEY_PARENT_SD_VERSAMENTI',
  FORM_KEY_CHILD_SD_VERSAMENTI: 'FORM_KEY_CHILD_SD_VERSAMENTI',

  DETTAGLIO_PAGAMENTO: 'Dettaglio pagamento',
  // Classe di stile utilizzata per la gestione delle icone nel componente risca-table
  RISCA_TABLE_ACTION: 'risca-table-action',

  MODALITA_PAGAMENTO_MANUALE: 'Manuale',
  MODALITA_PAGAMENTO_MANUALE_COD: 'M',

  // Tipi di Imp_Non_Propri per i flag
  NON_IDENTIFICATO_COD: 'NI',
  NON_DI_COMPETENZA_COD: 'NC',
  DA_RIMBORSARE_COD: 'DR',
  // FORM PAGAMENTI
  PAGAMENTI: 'pagamenti',
};
