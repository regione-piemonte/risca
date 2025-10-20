import { RiscaButtonTypes } from '../../../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../../../shared/utilities/enums/risca-notify-codes.enums';
import {
  IActionHandleVerifySD,
  IActionPayloadSD,
} from '../../../../interfaces/dati-contabili/dati-contabili.interfaces';

// Creo e ritorno la configurazione
export const onVerifyModA023: IActionHandleVerifySD = {
  id: RiscaNotifyCodes.A023,
  code: RiscaNotifyCodes.A023,
  onConfirm: (payload: IActionPayloadSD) => {
    // Estraggo le informazioni dal payload
    const { statoDebitorio, queryFlags } = payload;
    // Con la conferma, resetto l'attività dello sd
    statoDebitorio.attivita_stato_deb = undefined;

    // Ritorno il payload aggiornato
    return { statoDebitorio, queryFlags };
  },
  onClose: (payload: IActionPayloadSD) => undefined,
  onCancel: (payload: IActionPayloadSD) => undefined,
  title: 'Verfica stato debitorio',
  confirmBtn: {
    dataConfig: {
      label: 'CONFERMA',
    },
    cssConfig: {
      typeButton: RiscaButtonTypes.primary,
    },
  },
  cancelBtn: {
    dataConfig: {
      label: 'ANNULLA',
    },
    cssConfig: {
      typeButton: RiscaButtonTypes.default,
    },
  },
};
