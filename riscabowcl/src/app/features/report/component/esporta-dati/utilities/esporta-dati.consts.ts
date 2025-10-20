import { AccessoElementiAppKeyConsts } from '../../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import {
  IRiscaButtonAllConfig,
  RiscaButtonTypes,
} from '../../../../../shared/utilities';
import { RiscaIcons } from '../../../../../shared/enums/icons.enums';

/**
 * Classe contenente le costanti per il componente associato.
 */
export class EsportaDatiConsts {
  /** Configurazione Stato debitorio */
  BTN_CONFIG_CREA_REPORT: IRiscaButtonAllConfig = {
    cssConfig: { typeButton: RiscaButtonTypes.default },
    dataConfig: {
      label: 'Crea report',
      codeAEA: AccessoElementiAppKeyConsts.ESPORTA_DATI,
    },
  };

  ICON_DOWNLOAD = RiscaIcons.download_file;
}
