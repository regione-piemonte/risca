import { RiscaSortTypes } from "../../../../shared/utilities";

/** 
 * Interfaccia che definisce la struttura del json warning che può essere definito all'interno dell'header.
 */
export interface IJsonWarning {
  code: string;
  message: string;
}

/**
 * Interfaccia che definisce le informazioni del sort generato dalla risposta dei servizi.
 */
export interface ISortInfoRes {
  sortBy?: string;
  sortDirection?: RiscaSortTypes;
}