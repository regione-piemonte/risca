import { RiscaFormInputTypeahead } from "../../../../utilities";

export interface ITypeAheadDataValidazioneTmpl {
  dataConfig: RiscaFormInputTypeahead;
  data: any;
  typeaheadMap: (v: any) => string;
}