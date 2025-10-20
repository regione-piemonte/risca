import { RiscaButtonTypes } from '../../../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../../../shared/utilities/enums/risca-notify-codes.enums';
import {
  IActionHandleVerifySD,
  IActionPayloadSD,
  IActionRejectSD,
} from '../../../../interfaces/dati-contabili/dati-contabili.interfaces';
import { FlgsUpdateDataScadenza } from './verifica-sd-mod.enums';

// Creo e ritorno la configurazione
export const onVerifyModA027: IActionHandleVerifySD = {
  id: RiscaNotifyCodes.A027,
  code: RiscaNotifyCodes.A027,
  onConfirm: (payload: IActionPayloadSD) => {
    // Estraggo le informazioni dal payload
    const { statoDebitorio, queryFlags } = payload;
    // Aggiungo un query param per A027
    queryFlags.flgUpDataScadenza = FlgsUpdateDataScadenza.conferma;
    // Ritorno il payload aggiornato
    return { statoDebitorio, queryFlags };
  },
  onClose: (payload: IActionPayloadSD) => undefined,
  onCancel: (payload: IActionPayloadSD) => undefined,
  onReject: (payload?: IActionPayloadSD) => {
    // Definisco le informazioni di notifica di ritorno
    let reject: IActionRejectSD;
    reject = { code: RiscaNotifyCodes.I015 };
    // Ritorno le informazioni di reject della modale
    return reject;
  },
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
