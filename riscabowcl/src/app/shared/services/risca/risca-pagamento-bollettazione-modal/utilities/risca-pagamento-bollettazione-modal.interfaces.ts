import { IRiscaSDPerPagamentoModalConfig } from '../../../../modals/risca-sd-nap-codice-utenza-modal/utilities/risca-sd-per-pagamento-modal.interfaces';
import { ICallbackDataModal } from '../../../../utilities';

/**
 * Interfaccia che definisce le proprietà di configurazione per le modali dei veramenti non manuali.
 */
export interface IRiscaPBModalConfigs {
  callbacks: ICallbackDataModal;
  dataModal: IRiscaSDPerPagamentoModalConfig;
}
