import { RiscaButtonConfig } from '../../../../../utilities';

/**
 * Interfaccia strettamente collegata alla costante.
 */
export interface IRiscaUtilityModalConsts {
  BTN_ANNULLA_CONFIG: RiscaButtonConfig;
  BTN_CONFERMA_CONFIG: RiscaButtonConfig;
  BTN_CHIUDI_CONFIG: RiscaButtonConfig;
}

/**
 * Oggetto contenente le costanti del componente associato.
 */
export const RiscaUtilityModalConsts: IRiscaUtilityModalConsts = {
  BTN_ANNULLA_CONFIG: { label: 'ANNULLA' },
  BTN_CONFERMA_CONFIG: { label: 'CONFERMA' },
  BTN_CHIUDI_CONFIG: { label: 'CHIUDI' },
};
