import { NaturaOggetto } from "./natura-oggetto.model";

// TipologiaOggettoExtendedDTO
export interface TipologiaOggetto {
    id_tipologia_oggetto?: number;
    natura_oggetto?: NaturaOggetto;
    cod_tipologia_oggetto?: string;
    des_tipologia_oggetto?: string;
}
