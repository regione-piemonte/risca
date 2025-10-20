import { find } from 'lodash';
import { IndirizzoSpedizioneVo } from '../../../../../core/commons/vo/indirizzo-spedizione-vo';
import { RecapitoVo } from '../../../../../core/commons/vo/recapito-vo';
import { RiscaIconsCss } from '../../../../../shared/enums/icons.enums';
import { isSBNTrue } from '../../../../../shared/services/risca/risca-utilities/risca-utilities.functions';

/**
 * Funzione che estrae un indirizzo di spedizione da un oggetto recapito.
 * Se definito in input anche un idGruppo, la condizione di ricerca dell'indirizzo di spedizione verrà effettuata anche su quel valore.
 * @param recapito RecapitoVo dalla quale estrarre i dati dell'indirizzo di spedizione.
 * @param idGruppo number che definisce l'id gruppo per l'estrazione dell'informazione dell'indirizzo di spedzione.
 * @returns IndirizzoSpedizioneVo estratto dal recapito. Se qualcosa è andato storto: undefined.
 */
export const indirizzoSpedizioneFromRecapito = (
  recapito: RecapitoVo,
  idGruppo?: number
): IndirizzoSpedizioneVo => {
  // Verifico se il recapito esiste
  if (!recapito) {
    // Non esiste il recapito richiesto
    return undefined;
  }

  // Recupero gli indirizzi di spedizione
  const indirizzi = recapito.indirizzi_spedizione;
  // Verifico gli indirizzi esistano
  if (!indirizzi || indirizzi.length === 0) {
    // Non ci sono i dati per gli indirizzi
    return undefined;
  }

  // Effettuo la find dell'indirizzo di spedizione del soggetto, o del gruppo se idGruppo è definito
  return find(indirizzi, (i: IndirizzoSpedizioneVo) => {
    // Variabili di comodo
    const idR = recapito.id_recapito;
    const idG = idGruppo;
    // Richiamo la funzione di validazione per i dati dell'indirizzo di spedizione
    return validIndirizzoSpedizione(i, idR, idG);
  });
};

/**
 * Funzione di controllo che verifica la validità di un oggetto IndirizzoSpedizioneVo, sulla base degli idRecapito e possibile idGruppo passati in input.
 * @param indirizzo IndirizzoSpedizioneVo da verificare.
 * @param idRecapito number che definisce l'id recapito da verificare all'interno dell'indirizzo di spedizione.
 * @param idGruppo number che definisce l'id gruppo da verificare all'interno dell'indirizzo di spedizione.
 */
export const validIndirizzoSpedizione = (
  indirizzoSpedizione: IndirizzoSpedizioneVo,
  idRecapito: number,
  idGruppo?: number
): boolean => {
  // Verifico l'input
  if (!indirizzoSpedizione || idRecapito == undefined) {
    // Non si può fare il controllo base
    return undefined;
  }

  // Definisco la condizione di ritorno per il recapito
  const okRecapito = idRecapito === indirizzoSpedizione.id_recapito;
  // Definisco la condizione di ritorno per l'idGruppo
  let okGruppo = true;
  if (idGruppo == undefined) {
    // Definisco la condizione per okGruppo
    okGruppo = indirizzoSpedizione.id_gruppo_soggetto == undefined;
    // #
  } else {
    // Definisco la condizione per okGruppo
    okGruppo = indirizzoSpedizione.id_gruppo_soggetto == idGruppo;
    // #
  }

  // Ritorno le due condizioni
  return okRecapito && okGruppo;
};

/**
 * Determina quale icona mostrare in base alla configurazione dati, per la gestione: enable.
 * @param recapito RecapitoVo per l'estrazione del dato in riferimento all'indirizzo di spedizione.
 * @param idGruppo number che definisce l'id gruppo per l'estrazione dell'indirizzo di spedizione.
 * @returns string con la classe di stile che definisce al suo interno il richiamo all'icona.
 */
export const iconTableISEnable = (
  recapito?: RecapitoVo,
  idGruppo?: number
): string => {
  // Controllo di esistenza
  if (!recapito) {
    return '';
  }
  // Recupero l'indirizzo
  const indP = indirizzoSpedizioneFromRecapito(recapito, idGruppo);
  // Controllo di esistenza
  if (!indP) {
    return '';
  }
  // controllo ind_valido_postel
  if (isSBNTrue(indP.ind_valido_postel)) {
    return RiscaIconsCss.indirizzo_spedizione_valido_enable;
  } else {
    return RiscaIconsCss.indirizzo_spedizione_invalido_enable;
  }
};

/**
 * Determina quale icona mostrare in base alla configurazione dati, per la gestione: disable.
 * @param recapito RecapitoVo per l'estrazione del dato in riferimento all'indirizzo di spedizione.
 * @param idGruppo number che definisce l'id gruppo per l'estrazione dell'indirizzo di spedizione.
 * @returns string con la classe di stile che definisce al suo interno il richiamo all'icona.
 */
export const iconTableISDisable = (
  recapito?: RecapitoVo,
  idGruppo?: number,
  indUndefinedAsDisabled = false
): string => {
  // Controllo di esistenza
  if (!recapito) {
    return '';
  }

  // Definisco le icone per valido e invalido
  const iValid = RiscaIconsCss.indirizzo_spedizione_valido_disable;
  const iInvalid = RiscaIconsCss.indirizzo_spedizione_valido_disable; // RiscaIconsCss.indirizzo_spedizione_invalido_disable;

  // Recupero l'indirizzo
  const indP = indirizzoSpedizioneFromRecapito(recapito, idGruppo);
  // Verifico se la non esistenza dell'indirizzo è da considerare senza icona, o con icona disabilitata
  if (!indP) {
    return indUndefinedAsDisabled ? iInvalid : '';
  }

  // controllo ind_valido_postel
  if (isSBNTrue(indP.ind_valido_postel)) {
    return iValid;
  } else {
    return iInvalid;
  }
};
