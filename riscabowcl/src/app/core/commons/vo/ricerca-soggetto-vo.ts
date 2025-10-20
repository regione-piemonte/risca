import { convertServerBooleanStringToBoolean } from '../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { TRiscaServerBoolean } from '../../../shared/utilities';
import { SoggettoVo } from './soggetto-vo';

/**
 * interface che definisce l'oggetto restituito dalla ricerca di un soggetto: /soggetti?idAmbito=<number>&idTipoSoggetto=<number>&cfSoggetto=<string>
 */
export interface IRicercaSoggettoVo {
  soggetto?: SoggettoVo;
  fonti_ambito?: string;
  fonte_ricerca?: string;
  is_abilitato?: string;
  is_gestione_abilitata?: string;
  is_fonte_abilitata_in_lettura?: string;
  is_fonte_abilitata_in_scrittura?: string;
  codice_messaggio?: string;
  lista_campi_aggiornati?: string;


  /* #### ESEMPIO DATO ####
  {
    "soggetto": {
        "fonte": {},
        "id_soggetto": 104,
        "id_ambito": 1,
        "cf_soggetto": "HNVZNQ29S07G134G",
        "id_fonte_origine": 0,
        "nome": "Zinquo",
        "cognome": "Honv",
        "data_nascita_soggetto": "1987-02-12",
        "gest_data_ins": "2022-02-10T00:00:00.000+0000",
        "gest_attore_ins": "BVORCR93E08A182G",
        "gest_data_upd": "2022-02-10T00:00:00.000+0000",
        "gest_attore_upd": "BVORCR93E08A182G",
        "gest_uid": "Risorse idriche",
        "tipo_soggetto": {
            "id_tipo_soggetto": 1,
            "cod_tipo_soggetto": "PF",
            "des_tipo_soggetto": "Persona Fisica",
            "ordina_tipo_soggetto": 1
        },
        "tipo_natura_giuridica": {},
        "comune_nascita": {},
        "nazione_nascita": {}
    },
    "fonti_ambito": "RISCA",
    "is_abilitato": "N",
    "is_gestione_abilitata": "S",
    "is_fonte_abilitata_in_lettura": "S",
    "is_fonte_abilitata_in_scrittura": "S"
    "codice_messaggio": "...",
  	"lista_campi_aggiornati": "nome,cognome,cf_soggetto,comune_nascita,nazione_nascita"
  }
  */
}

/**
 * Classe che definisce l'oggetto restituito dalla ricerca di un soggetto: /soggetti?idAmbito=<number>&idTipoSoggetto=<number>&cfSoggetto=<string>
 */
export class RicercaSoggetto {
  soggetto: SoggettoVo;
  fonti_ambito: string;
  fonte_ricerca: string;
  is_abilitato: boolean;
  is_gestione_abilitata: boolean;
  is_fonte_abilitata_in_lettura: boolean;
  is_fonte_abilitata_in_scrittura: boolean;
  codice_messaggio: string;
  lista_campi_aggiornati: string[];

  constructor(rsVo: IRicercaSoggettoVo) {
    this.soggetto = rsVo?.soggetto;
    this.fonti_ambito = rsVo?.fonti_ambito;
    this.fonte_ricerca = rsVo?.fonte_ricerca;
    this.is_abilitato = this.converSBtoB(rsVo?.is_abilitato);
    this.is_gestione_abilitata = this.converSBtoB(rsVo?.is_gestione_abilitata);
    this.is_fonte_abilitata_in_lettura = this.converSBtoB(
      rsVo?.is_fonte_abilitata_in_lettura
    );
    this.is_fonte_abilitata_in_scrittura = this.converSBtoB(
      rsVo?.is_fonte_abilitata_in_scrittura
    );
    this.codice_messaggio = rsVo?.codice_messaggio;
    this.lista_campi_aggiornati = this.convertLCA(rsVo?.lista_campi_aggiornati);
  }

  /**
   * Funzione di comodo per la conversione del boolean server ad un boolean standard.
   * @param v TRiscaServerBoolean con il valore da convertire.
   * @returns boolean convertito.
   */
  private converSBtoB(v: TRiscaServerBoolean): boolean {
    // Richiamo la funzione di conversione
    return convertServerBooleanStringToBoolean(v);
  }

  /**
   * Funzione di comodo per la conversione della lista campi aggiornati, da stringa a array di stringhe.
   * @param lca string da convertire.
   * @returns Array di string convertite.
   */
  private convertLCA(lca: string, splitter = ','): string[] {
    // Verifico l'input
    if (!lca) {
      // Ritorno il default
      return [];
    }

    // Effettuo la conversione del dato
    return lca.split(splitter);
  }
}
