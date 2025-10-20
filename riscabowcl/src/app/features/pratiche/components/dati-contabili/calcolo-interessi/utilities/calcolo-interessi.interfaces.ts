import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

export interface ICalcoloInteressi {
  canoneMancante: number;
  dataScadenza: NgbDateStruct;
  dataVersamento: NgbDateStruct;
  interessiDovuti?: number;
  totaleDaVersare?: number;
}