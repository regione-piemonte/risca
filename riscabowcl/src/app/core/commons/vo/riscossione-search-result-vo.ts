/**
 * Classe che definisce l'oggetto per la costruzione della classe RiscossioneSearchResultVo.
 */
export interface IRiscossioneSearchResultVo {
  id_riscossione?: number;
  codice_utenza?: string;
  numero_pratica?: string;
  procedimento?: string;
  titolare?: string;
  stato?: string;
  comune_opera_di_presa?: string;
  corpo_idrico?: string;
  nome_impianto?: string;
  total?: number;
}

/**
 * Classe che definisce l'oggetto che il server ritorna a seguito di una ricerca.
 */
export class RiscossioneSearchResultVo {
  id_riscossione?: number;
  codice_utenza?: string;
  numero_pratica?: string;
  procedimento?: string;
  titolare?: string;
  stato?: string;
  comune_opera_di_presa?: string;
  corpo_idrico?: string;
  nome_impianto?: string;
  total?: number;

  constructor(c?: IRiscossioneSearchResultVo) {
    // Verifico se esiste la configurazione
    if (!c) {
      // Nessuna configurazione
      return;
    }

    this.id_riscossione = c.id_riscossione;
    this.codice_utenza = c.codice_utenza;
    this.numero_pratica = c.numero_pratica;
    this.procedimento = c.procedimento;
    this.titolare = c.titolare;
    this.stato = c.stato;
    this.comune_opera_di_presa = c.comune_opera_di_presa;
    this.corpo_idrico = c.corpo_idrico;
    this.nome_impianto = c.nome_impianto;
    this.total = c.total;
  }
}
