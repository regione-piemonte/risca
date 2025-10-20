import { Gruppo } from '../../../../core/commons/vo/gruppo-vo';
import { IndirizzoSpedizioneVo } from '../../../../core/commons/vo/indirizzo-spedizione-vo';
import { RecapitoVo } from '../../../../core/commons/vo/recapito-vo';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import {
  ICallbackDataModal,
  RiscaServerError,
} from '../../../../shared/utilities';

/**
 * Interfaccia di comodo che definisce le informazioni ritornate dalla funzione di supporto di scissione indirizzi spedizione a seguito della verify.
 */
export interface IRisultatiVerifyIS {
  indirizziValidi: IndirizzoSpedizioneVo[];
  indirizziInvalidi: IRVISInvalidi[];
}

/**
 * INterfaccia di comodo che definisce il dettaglio per gli errori definiti dal servizio di verifica indirizzi spedizione.
 */
export interface IRVISInvalidi {
  error: RiscaServerError;
  indirizzoSpedizione: IndirizzoSpedizioneVo;
}

/**
 * Interfaccia di comodo che definisce la struttura delle informazioni sul controllo degli errori degli indirizzi di spedizione a seguito dell'inserimento/modifica soggetto.
 */
export interface IInvalidISDataFromSoggetto {
  indirizziSpedizione: IndirizzoSpedizioneVo[];
  recapiti: RecapitoVo[];
  gruppi?: Gruppo[];
}

/**
 * Interfaccia che definisce le informazioni di un indirizzo di spedizione e le informazioni del recapito ed eventuale gruppo connessi ad esso.
 */
export interface IIndSpedRecapitoGruppo {
  indirizzoSpedizione: IndirizzoSpedizioneVo;
  recapito: RecapitoVo;
  gruppo?: Gruppo;
}

/**
 * Interfaccia che definisce le informazioni per la gestione di uno e più indirizzi di spedizione, tramite apposita modale.
 */
export interface IGestisciIndirizzoSpedizione {
  soggetto: SoggettoVo;
  callbacks: ICallbackDataModal;
}

/**
 * Interfaccia che definisce le informazioni per poter gestire la correzione degli indirizzi di spedizione di un soggetto, tramite apposita modale.
 */
export interface ICorreggiIndirizziSpedizione
  extends IGestisciIndirizzoSpedizione {}

/**
 * Interfaccia che definisce le informazioni per poter gestire la modifica di un indirizzo di spedizione, tramite apposita modale.
 */
export interface IAggiornaIndirizzoSpedizione extends IGestisciIndirizzoSpedizione {
  idRecapito: number;
  idGruppo?: number;
  isFormDisabilitato?: boolean;
}
