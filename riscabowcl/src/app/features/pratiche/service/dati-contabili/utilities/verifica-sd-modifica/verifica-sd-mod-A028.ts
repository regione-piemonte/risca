import { RiscaButtonTypes } from '../../../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../../../shared/utilities/enums/risca-notify-codes.enums';
import {
  IActionHandleVerifySD,
  IActionPayloadSD,
} from '../../../../interfaces/dati-contabili/dati-contabili.interfaces';
import { FlgsUpdateDataScadenza } from './verifica-sd-mod.enums';

// Creo e ritorno la configurazione
export const onVerifyModA028: IActionHandleVerifySD = {
  id: RiscaNotifyCodes.A028,
  code: RiscaNotifyCodes.A028,
  onConfirm: (payload: IActionPayloadSD) => {
    // Estraggo le informazioni dal payload
    const { statoDebitorio, queryFlags } = payload;
    // Aggiungo un query param per A028
    queryFlags.flgUpDataScadenza = FlgsUpdateDataScadenza.conferma;
    // Ritorno il payload aggiornato
    return { statoDebitorio, queryFlags };
  },
  onCancel: (payload: IActionPayloadSD) => {
    // Estraggo le informazioni dal payload
    const { statoDebitorio, queryFlags } = payload;
    // Aggiungo un query param per A028
    queryFlags.flgUpDataScadenza = FlgsUpdateDataScadenza.annulla;
    // Ritorno il payload aggiornato
    return { statoDebitorio, queryFlags };
  },
  onClose: (payload: IActionPayloadSD) => payload,
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
