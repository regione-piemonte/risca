import {
  RiscaTableBodyTabMethodData,
  RiscaTableBodyTabMethodType,
} from '../../utilities';
import { RiscaCssHandlerTypes, RiscaTableBodyTabMethods } from '../../utilities/enums/utilities.enums';

/**
 * Oggetto costante contenente una serie di costanti per il componente risca-recapito.
 */
interface IRiscaTableConsts {
  RADIO_GN_PREFIX: string;

  CHECK: RiscaTableBodyTabMethodType;
  DELETE: RiscaTableBodyTabMethodType;
  CLOSE: RiscaTableBodyTabMethodType;
  DETAIL: RiscaTableBodyTabMethodType;
  MODIFY: RiscaTableBodyTabMethodType;
  RADIO: RiscaTableBodyTabMethodType;

  CHECKS_ACTION: RiscaTableBodyTabMethodData;

  CHECK_ACTION: RiscaTableBodyTabMethodData;
  DELETE_ACTION: RiscaTableBodyTabMethodData;
  CLOSE_ACTION: RiscaTableBodyTabMethodData;
  DETAIL_ACTION: RiscaTableBodyTabMethodData;
  MODIFY_ACTION: RiscaTableBodyTabMethodData;
}

/**
 * Oggetto costante contenente una serie di costanti per il componente risca-recapito.
 */
export const RiscaTableConsts: IRiscaTableConsts = {
  RADIO_GN_PREFIX: 'risca_radio',

  CHECK: RiscaTableBodyTabMethods.check,
  DELETE: RiscaTableBodyTabMethods.delete,
  CLOSE: RiscaTableBodyTabMethods.close,
  DETAIL: RiscaTableBodyTabMethods.detail,
  MODIFY: RiscaTableBodyTabMethods.modify,
  RADIO: RiscaTableBodyTabMethods.radio,

  CHECKS_ACTION: {
    action: RiscaTableBodyTabMethods.check,
    custom: false,
    disable: () => false,
  },
  CHECK_ACTION: {
    action: RiscaTableBodyTabMethods.check,
    custom: false,
    disable: () => false,
  },
  DELETE_ACTION: {
    action: RiscaTableBodyTabMethods.delete,
    custom: false,
    title: 'Cancella',
    class: 'risca-table-action',
    iconEnabled: 'rta-enable-delete',
    iconDisabled: 'rta-disable-delete',
    disable: () => false,
  },
  CLOSE_ACTION: {
    action: RiscaTableBodyTabMethods.close,
    custom: false,
    title: 'Chiudi',
    class: 'risca-table-action',
    iconEnabled: 'risca-blue-i risca-btn-x close',
    iconDisabled: 'risca-btn-x close',
    disable: () => false,
  },
  DETAIL_ACTION: {
    action: RiscaTableBodyTabMethods.detail,
    custom: false,
    title: 'Dettaglio',
    class: 'risca-table-action',
    iconEnabled: 'rta-enable-detail',
    iconDisabled: 'rta-disable-detail',
    disable: () => false,
  },
  MODIFY_ACTION: {
    action: RiscaTableBodyTabMethods.modify,
    custom: false,
    title: 'Modifica',
    class: 'risca-table-action',
    iconEnabled: 'rta-enable-modify',
    iconDisabled: 'rta-disable-modify',
    disable: () => false,
  },
};
