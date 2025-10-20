import { ParametroElaborazioneVo } from '../../../../core/commons/vo/parametro-elaborazione-vo';
import { TBollettiniPE } from '../../types/bollettini/bollettini.types';
import { getParametroElaborazione } from '../pagamenti/pagamenti.functions';

/**
 * Funzione che recupera, dalla proprietà "parametri" di un oggetto Elaborazione, un oggetto specifico data una chiave di ricerca.
 * @param parametri Array di ParametroElaborazioneVo contenente le informazioni dei parametri di un oggetto Elaborazione/ElaborazioneVo.
 * @param chiaveParametro TBollettiniPE che definisce la chiave (che alla fine sarà una stringa) per il recupero dello specifica parametro dall'oggetto elaborazione.
 * @returns ParametroElaborazioneVo con i dati estratti. Altrimenti undefined.
 */
export const getParametroElaborazioneBollettini = (
  parametri: ParametroElaborazioneVo[],
  chiaveParametro: TBollettiniPE
): ParametroElaborazioneVo => {
  // Verifico l'input
  if (!parametri || !chiaveParametro) {
    // Ritorno undefined
    return undefined;
  }

  // Variabili di comodo
  const p = parametri;
  const cp = chiaveParametro as string;

  // Richiamo il servizio per estrarre le informazioni dalle proprietà
  return getParametroElaborazione(p, cp);
};
