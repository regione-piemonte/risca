import { PagamentoVo } from '../../../../../core/commons/vo/pagamento-vo';
import { TipoModalitaPagamentoVo } from '../../../../../core/commons/vo/tipo-modalita-pagamento-vo';
import { PagamentiSDConsts } from '../../../components/dati-contabili/pagamenti-sd/utilities/pagamenti-sd.consts';

/**
 * Funzione di comodo che verifica se un pagamento è di tipo manuale.
 * @param pagamento PagamentoVo con l'oggetto da verificare.
 * @returns boolean che indica se il pagamento è manuale.
 */
export const isPagamentoManuale = (pagamento: PagamentoVo): boolean => {
  // Dal pagamento recupero il tipo di pagamento
  const tipoPagamento: TipoModalitaPagamentoVo = pagamento?.tipo_modalita_pag;
  // Infine recupero il codice del pagamento dal tipo pagamento
  const codTipoPag: string = tipoPagamento?.cod_modalita_pag ?? '';

  // Recupero il codice censito per il pagamento manuale
  const PAG_MANUALE = PagamentiSDConsts.MODALITA_PAGAMENTO_MANUALE_COD;
  // Se il pagamento non è manuale, la cancellazione è disabilitata
  const isManuale = codTipoPag == PAG_MANUALE;

  // Ritorno il check
  return isManuale;
};
