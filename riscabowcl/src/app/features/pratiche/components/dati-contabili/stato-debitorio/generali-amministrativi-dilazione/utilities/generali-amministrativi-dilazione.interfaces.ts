import { NgbDateStruct, NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { IRiscaCheckboxData } from '../../../../../../../shared/utilities';
import { TipoTitoloVo } from '../../../../../../../core/commons/vo/tipo-titolo-vo';
import { TipoIstanzaProvvedimentoVo } from '../../../../../../../core/commons/vo/tipo-istanza-provvidemento-vo';

/**
 * Interfaccia che definisce la struttura dell'oggetto generato dalla form del componente generali-amministrativi-dilazione.component.ts
 */
export interface GeneraliAmministrativiDilazione {
  codiceUtenza: string;
  dataUltimaModifica: string;
  numRichiestaProtocollo: number;
  dataRichiestaProtocollo: NgbDateStruct;
  restituitoMittente: IRiscaCheckboxData;
  periodoPagamento: string;
  scadenzaPagamento: NgbDateStruct;
  codiceAvviso: string;
  stato: string;
  invioSpecialePostel: IRiscaCheckboxData;
  annullato: IRiscaCheckboxData;
  motivazione: string;
  canoneAnnualitaCorrente: number;
  annualitaPrecedente: number;
  canoneDovuto: number;
  addebitoAnnoSuccessivo: IRiscaCheckboxData;
  interessiMaturati: number;
  speseNotifica: number;
  importoCompensazione: number;
  dilazione: IRiscaCheckboxData;
  tipoTitolo: string;
  numeroTitolo: number;
  dataTitolo: NgbDateStruct;
  inizioConcessione: NgbDateStruct;
  scadenzaConcessione: NgbDate;
  istanzaRinnovo: TipoIstanzaProvvedimentoVo;
  noteSD: string;
  utenzeCompensazione: string;
}
