/**
 * Interfaccia che definisce le proprietà per la ricerca degli usi regola.
 */
export interface IRicercaUsiRegola {
  idAmbito: number;
  anno: number;
}

/**
 * Interfaccia che definisce le proprietà per la creazione di una annualità, mediante POST.
 */
export interface IPostCreaAnnualita {
  idAmbito: number;
  anno: number;
  percentuale?: number;
}