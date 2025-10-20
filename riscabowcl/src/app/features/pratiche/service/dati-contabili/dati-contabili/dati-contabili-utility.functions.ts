import { find, reverse, sortBy } from 'lodash';
import { Moment } from 'moment';
import { AnnualitaSDVo } from '../../../../../core/commons/vo/annualita-sd-vo';
import { RataVo } from '../../../../../core/commons/vo/rata-vo';

/**
 * Funzione che definisce l'informazione da visualizzare a video per la gestione delle annualità.
 * L'annualità di riferimento si basa su una specifica logica richiesta dagli analisti.
 * @param annualita AnnualitaSDVo[] per la definizione del riferimento.
 * @returns string con l'indicazione dell'annualità di riferimento.
 */
export const annualitaRiferimento = (annualita: AnnualitaSDVo[]): string => {
  // Verifico l'input
  if (!annualita || annualita.length === 0) {
    // Nessuna annualità
    return '';
  }

  // Verifico se ci sono più annualità
  const multiA = annualita?.length > 1;
  // Recupero l'annualità più recente
  const recentA = annualitaPiuRecente(annualita);
  // Definisco la stringa con il riferimento dell'annualità
  const annualitaRef = `${recentA?.anno ?? ''}${multiA ? '+' : ''}`;

  // Ritorno la stringa identificativa
  return annualitaRef;
};

/**
 * Funzione che recupera da una lista di annualità, l'annualità più recente.
 * @param annualita AnnualitaSDVo[] dalla quale estrarre l'annualità più recente.
 * @returns AnnualitaSDVo con l'annualità più recente. Se non ci sono dati: undefined.
 */
export const annualitaPiuRecente = (
  annualita: AnnualitaSDVo[]
): AnnualitaSDVo => {
  // Verifico l'input
  if (!annualita || annualita.length === 0) {
    // Nessuna annualità
    return undefined;
  }

  // Ordino le annualita in maniera crescente, effettuo una reverse ed estraggo la prima annualità (quindi la piu recente)
  return reverse(sortBy(annualita, (a: AnnualitaSDVo) => a.anno))[0];
};

/**
 * Funzione che recupera da una lista di rate, la rata che risulta senza rata padre (id_rata_sd_padre == null).
 * @param rate RataVo[] dalla quale estrarre la rata senza padre.
 * @returns RataVo con la rata senza padre. Se non esiste: undefined.
 */
export const getRataPadre = (rate: RataVo[]): RataVo => {
  // Verifico l'input
  if (!rate || rate.length === 0) {
    // Nessuna annualità
    return undefined;
  }

  // Ritorno la rata senza proprietà: id_rata_sd_padre
  return find(rate, (rata: RataVo) => {
    // Ritorno la rata senza id_rata_sd_padre
    return rata.id_rata_sd_padre == undefined;
  });
};

/**
 * Funzione che recupera da una lista di rate, la rata che risulta senza rata padre (id_rata_sd_padre == null).
 * @param rate RataVo[] dalla quale estrarre la rata senza padre.
 * @returns RataVo con la rata senza padre. Se non esiste: undefined.
 */
export const dataScadenzaRataPadre = (rate: RataVo[]): Moment => {
  // Richiamo la funzionalità di recupero della rata
  const rata = getRataPadre(rate);

  // Verifico esista una rata senza padre
  if (!rata) {
    // Ritorno undefined
    return undefined;
  }

  // La rata esiste, cerco di ritornare la data scadenza
  return rata?.data_scadenza_pagamento as Moment;
};

/**
 * Funzione di comodo che definisce le logiche di match tra due oggetti annualità, prendendo in considerazione sia l'id di BE che l'id di FE.
 * @param annualitaLista AnnualitaSDVo che definisce l'oggetto della lista da comparare.
 * @param annualitaCompare AnnualitaSDVo che definisce l'oggetto d'interesse da comparare.
 * @returns boolean che definisce se gli oggetti sono gli stessi.
 */
export const findAnnaulitaBEFE = (
  annualitaLista: AnnualitaSDVo,
  annualitaCompare: AnnualitaSDVo
): boolean => {
  // Definisco i flag di controllo
  const idASDExist = annualitaLista.id_annualita_sd != undefined;
  const idAFEExist = annualitaLista.__id != undefined;
  // Definisco le condizioni di find
  const checkIdASD =
    idASDExist &&
    annualitaLista.id_annualita_sd == annualitaCompare.id_annualita_sd;
  const checkIdAFE = idAFEExist && annualitaLista.__id == annualitaCompare.__id;

  // Ritorno le condizioni
  return checkIdASD || checkIdAFE;
};
