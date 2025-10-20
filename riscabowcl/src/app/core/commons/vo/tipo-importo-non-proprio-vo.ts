export interface ITipoImpNonProprioVo {
  id_tipo_imp_non_propri: number;
  cod_tipo_imp_non_propri: string;
  des_tipo_imp_non_propri: string;
}

/**
 * Per la gestione del dato, è stata richiesta la definizione statica degli oggetti di questa tipologia.
 */
export class TipiImpNonProprioVo {
  /** ITipoImpNonPropriVo per le informazioni: "non identificato". */
  private _nonIdentificato: ITipoImpNonProprioVo = {
    id_tipo_imp_non_propri: 1,
    cod_tipo_imp_non_propri: 'NI',
    des_tipo_imp_non_propri: 'Non identificato',
  };
  /** ITipoImpNonPropriVo per le informazioni: "non di competenza". */
  private _nonDiCompetenza: ITipoImpNonProprioVo = {
    id_tipo_imp_non_propri: 2,
    cod_tipo_imp_non_propri: 'NC',
    des_tipo_imp_non_propri: 'Non di competenza',
  };
  /** ITipoImpNonPropriVo per le informazioni: "da rimborsare". */
  private _daRimborsare: ITipoImpNonProprioVo = {
    id_tipo_imp_non_propri: 3,
    cod_tipo_imp_non_propri: 'DR',
    des_tipo_imp_non_propri: 'Da rimborsare',
  };

  /**
   * ######
   * GETTER
   * ######
   */

  /**
   * Getter per il tipo: non identificato.
   * @returns ITipoImpNonPropriVo.
   */
  get nonIdentificato(): ITipoImpNonProprioVo {
    // Ritorno l'oggetto
    return this._nonIdentificato;
  }

  /**
   * Getter per il tipo: non di competenza.
   * @returns ITipoImpNonPropriVo.
   */
  get nonDiCompetenza(): ITipoImpNonProprioVo {
    // Ritorno l'oggetto
    return this._nonDiCompetenza;
  }

  /**
   * Getter per il tipo: da rimborsare.
   * @returns ITipoImpNonPropriVo.
   */
  get daRimborsare(): ITipoImpNonProprioVo {
    // Ritorno l'oggetto
    return this._daRimborsare;
  }

  // GETTER CODICI

  /**
   * Getter per il codice dell'oggetto: non identificato.
   * @returns string con il codice associato all'oggetto.
   */
  get codNonIdentificato(): string {
    // Ritorno il codice dell'oggetto
    return this._nonIdentificato.cod_tipo_imp_non_propri;
  }

  /**
   * Getter per il codice dell'oggetto: non di competenza.
   * @returns string con il codice associato all'oggetto.
   */
  get codNonDiCompetenza(): string {
    // Ritorno il codice dell'oggetto
    return this._nonDiCompetenza.cod_tipo_imp_non_propri;
  }

  /**
   * Getter per il codice dell'oggetto: da rimborsare.
   * @returns string con il codice associato all'oggetto.
   */
  get codDaRimborsare(): string {
    // Ritorno il codice dell'oggetto
    return this._daRimborsare.cod_tipo_imp_non_propri;
  }
}