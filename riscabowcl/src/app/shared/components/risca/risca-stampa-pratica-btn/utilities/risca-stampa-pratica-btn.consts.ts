import { RiscaButtonConfig } from '../../../../utilities';

/**
 * Interfaccia strettamente collegata alla costante: RiscaStampaPraticaBTNConst.
 */
export interface IRiscaStampaPraticaBTNConst {
  STAMPA: RiscaButtonConfig;
}

/**
 * Costante che rappresenta le funzionalità presenti su db per la parte dei pagamenti.
 */
export const RiscaStampaPraticaBTNConst: IRiscaStampaPraticaBTNConst = {
  STAMPA: { label: 'STAMPA' },
};
