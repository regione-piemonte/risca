import { ParametroElaborazioneVo } from '../../../../core/commons/vo/parametro-elaborazione-vo';

/**
 * Funzione che recupera, dalla proprietà "parametri" di un oggetto Elaborazione, un oggetto specifico data una chiave di ricerca.
 * @param parametri Array di ParametroElaborazioneVo contenente le informazioni dei parametri di un oggetto Elaborazione/ElaborazioneVo.
 * @param chiaveParametro string che definisce la chiave per il recupero dello specifica parametro dall'oggetto elaborazione.
 * @returns ParametroElaborazioneVo con i dati estratti. Altrimenti undefined.
 */
export const getParametroElaborazione = (
  parametri: ParametroElaborazioneVo[],
  chiaveParametro: string
): ParametroElaborazioneVo => {
  // Verifico l'input
  if (!parametri || !chiaveParametro) {
    // Ritorno undefined
    return undefined;
  }

  // Ricerco all'interno dell'array l'oggetto che abbia la chiave richiesta
  return parametri.find((p: ParametroElaborazioneVo) => {
    // Verifico per stessa chiave
    return p.chiave === chiaveParametro;
  });
};
