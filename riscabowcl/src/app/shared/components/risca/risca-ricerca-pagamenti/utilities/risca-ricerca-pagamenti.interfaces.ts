import { NgbDateStruct } from "@ng-bootstrap/ng-bootstrap";
import { TipoRicercaPagamentoVo } from "../../../../../core/commons/vo/tipo-ricerca-pagamento-vo";
import { TipoModalitaPagamentoVo } from "../../../../../core/commons/vo/tipo-modalita-pagamento-vo";

/**
 * Interfaccia che rappresenta i filtri di ricerca generati dal form.
 */
export interface IFiltriRicercaPagamenti {
  statoPagamento: TipoRicercaPagamentoVo;
  modalitaPagamento: TipoModalitaPagamentoVo;
  dataOperazioneValutaDa: NgbDateStruct;
  dataOperazioneValutaA: NgbDateStruct;
  quintoCampo: string;
  codiceAvviso: string;
  codiceRiferimentoOperazione: string;
  importoDa: number;
  importoA: number;
  soggetto: string;
}