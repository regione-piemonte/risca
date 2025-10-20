import { NgbDateStruct } from "@ng-bootstrap/ng-bootstrap";

/**
 * Interfaccia che definisce l'oggetto restituito dal form.
 */
export interface IInteressiDiMoraFormData {
  percentuale: number;
  dataInizio: NgbDateStruct;
  dataFine: NgbDateStruct;
}