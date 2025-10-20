import { TipoParametroElaborazione } from '../../enums/pagamenti/pagamenti.enums';

/**
 * Types specifico che identifica le proprietà utilizzate dalla pagina bollettini, per la proprietà "parametri" dell'oggetto Elaborazione/ElaborazioneVo
 */
export type TBollettiniPE =
  | TipoParametroElaborazione.DT_PROTOCOLLO
  | TipoParametroElaborazione.NUM_PROTOCOLLO
  | TipoParametroElaborazione.DT_SCAD_PAG
  | TipoParametroElaborazione.ANNO;
