import { RiscaServerError } from '../../../utilities';
import { RiscaNotifyCodes } from '../../../utilities/enums/risca-notify-codes.enums';

/**
 * Questo errore è definito per gestire la seguente casistica:
 * - POST /soggetti quando la generazione degli indirizzi di spedizione va in errore per almeno un indirizzo di spedizione.
 */
export const RSEPostSoggettiIS = new RiscaServerError({
  error: {
    code: RiscaNotifyCodes.E065,
    status: '400',
  },
});
