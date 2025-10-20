import { JsonRegolaRangeVo } from '../../../../core/commons/vo/json-regola-range-vo';
import { JsonRegolaVo } from '../../../../core/commons/vo/json-regola-vo';
import { RegolaUsoVo } from '../../../../core/commons/vo/regola-uso-vo';
import { formatoImportoITA } from '../../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { LegendaMinimiRegoleUso } from './utilities/configurazioni.enums';

/**
 * Funzione di utility che genera la descrizione per la legenda "minimi" per una regola uso.
 * @param regolaUso RegolaUsoVo dalla quale generare la legenda "minimi".
 * @returns string con la descrizione generata.
 */
export const legendaMinimiRegoleUso = (regolaUso: RegolaUsoVo): string => {
  // Verifico l'input
  if (!regolaUso) {
    // Manca la configurazione
    return LegendaMinimiRegoleUso.errore;
  }

  // Estraggo il json_regola_obj dal dato per gestire meglio le condizioni
  let jsro: JsonRegolaVo;
  jsro = regolaUso.json_regola_obj;
  // Verifico che esista l'oggetto
  if (!jsro) {
    // Gestisco come errore
    return LegendaMinimiRegoleUso.errore;
    // #
  }

  // Caso 1: esiste canone minimo
  if (jsro.canone_minimo != undefined) {
    // Esiste canone minimo, ritorno la descrizione
    return legendaMinimiCanoneMinimo(regolaUso);
  }

  // Caso 2: esiste campo "soglia"
  if (jsro.soglia?.soglia != undefined) {
    // Esiste la soglia, ritorno la descrizione
    return legendaMinimiSoglia(regolaUso);
    // #
  }

  // Caso 3: esiste json_ranges
  if (jsro.ranges?.length >= 2) {
    // Esistono i ranges, ritorno la descrizione
    return legendaMinimiRanges(regolaUso);
  }

  // Casistica finale indefinita
  return LegendaMinimiRegoleUso.vuoto;
};

/**
 * Funzione di utility che genera la descrizione per la legenda "minimi" per una regola uso.
 * @param regolaUso RegolaUsoVo dalla quale generare la legenda "minimi".
 * @returns string con la descrizione generata.
 */
export const legendaMinimiCanoneMinimo = (regolaUso: RegolaUsoVo): string => {
  // Verifico l'input
  if (!regolaUso) {
    // Manca la configurazione
    return LegendaMinimiRegoleUso.errore;
  }

  // Estraggo il json_regola_obj dal dato per gestire meglio le condizioni
  let jsro: JsonRegolaVo;
  jsro = regolaUso.json_regola_obj;
  // Verifico che esista l'oggetto
  if (!jsro) {
    // Gestisco come errore
    return LegendaMinimiRegoleUso.errore;
    // #
  }

  // Caso 1: esiste canone minimo
  if (jsro.canone_minimo != undefined) {
    // Esiste canone minimo, formatto l'importo
    const desCanMinIT: string = formatoImportoITA(jsro.canone_minimo, 2, true);
    // Ritorno la legenda
    return `MINIMO ${desCanMinIT} €`;
  }

  // Casistica finale indefinita
  return LegendaMinimiRegoleUso.vuoto;
};

/**
 * Funzione di utility che genera la descrizione per la legenda "minimi" per una regola uso.
 * @param regolaUso RegolaUsoVo dalla quale generare la legenda "minimi".
 * @returns string con la descrizione generata.
 */
export const legendaMinimiSoglia = (regolaUso: RegolaUsoVo): string => {
  // Verifico l'input
  if (!regolaUso) {
    // Manca la configurazione
    return LegendaMinimiRegoleUso.errore;
  }

  // Estraggo il json_regola_obj dal dato per gestire meglio le condizioni
  let jsro: JsonRegolaVo;
  jsro = regolaUso.json_regola_obj;
  // Verifico che esista l'oggetto
  if (!jsro) {
    // Gestisco come errore
    return LegendaMinimiRegoleUso.errore;
    // #
  }

  // Verifico se esiste il campo "soglia"
  if (jsro.soglia?.soglia != undefined) {
    // Esiste la soglia, estraggo i dati utili
    const sogliaInf: number = jsro.soglia.canone_minimo_soglia_inf;
    const sogliaInfIT: string = formatoImportoITA(sogliaInf, 2, true);
    const sogliaSup: number = jsro.soglia.canone_minimo_soglia_sup;
    const sogliaSupIT: string = formatoImportoITA(sogliaSup, 2, true);
    // Per la soglia deve arrivare a 4 decimali anche se 0
    const soglia: number = jsro.soglia.soglia;
    let desSoglia: string = formatoImportoITA(soglia, 4, true);

    // Compongo le descrizioni per le soglie inferiore e superiore
    let desSogliaInf: string = '';
    desSogliaInf = `<tr> <td>≤ ${desSoglia} </td><td> MINIMO ${sogliaInfIT} € </td>`;
    let desSogliaSup: string = '';
    desSogliaSup = `<tr> <td>> ${desSoglia} </td><td> MINIMO ${sogliaSupIT} € </td>`;

    // Compongo la descrizione effettiva
    const des: string = `<table class="legenda-minimi"> ${desSogliaInf} ${desSogliaSup} </table>`;
    // Ritorno la legenda generata
    return des;
    // #
  }

  // Casistica finale indefinita
  return LegendaMinimiRegoleUso.vuoto;
};

/**
 * Funzione di utility che genera la descrizione per la legenda "minimi" per una regola uso.
 * Questa funzione è dedicata specificatamente per la legenda che gestisce i ranges.
 * @param regolaUso RegolaUsoVo dalla quale generare la legenda "minimi".
 * @returns string con la descrizione generata.
 */
export const legendaMinimiRanges = (regolaUso: RegolaUsoVo): string => {
  // Verifico l'input
  if (!regolaUso) {
    // Manca la configurazione
    return LegendaMinimiRegoleUso.errore;
  }

  // Estraggo il json_regola_obj dal dato per gestire meglio le condizioni
  let jsro: JsonRegolaVo;
  jsro = regolaUso.json_regola_obj;
  // Verifico che esista l'oggetto
  if (!jsro) {
    // Gestisco come errore
    return LegendaMinimiRegoleUso.errore;
    // #
  }

  // Verifico se esiste il campo json_ranges e ha almeno dei valori
  if (jsro.ranges?.length >= 2) {
    // Definisco la descrizione per la legenda
    let desLeg: string = '';
    // Estraggo le informazioni utili
    const minPrinc: number = jsro.minimoPrincipale;
    const rangesFE: JsonRegolaRangeVo[] = jsro.rangesFE;

    // Dall'array di ranges vado a generare un array di descrizioni, gestendo le specifiche casistiche
    let desRanges: string[] = rangesFE.map((r: JsonRegolaRangeVo) => {
      // Estraggo dall'oggetto range le informazioni
      const sogliaMin: number = r.soglia_min;
      const sogliaMax: number = r.soglia_max;
      // Definisco dei booleani di comodo che gestiscono l'esistenza effettiva dei dati
      const existSMin: boolean = sogliaMin != undefined;
      const existSMax: boolean = sogliaMax != undefined;
      // Definisco le variabili che conterranno le descrizioni, se manca il dato le descrizioni saranno dei blank per tenere allineata la lettura
      let desSogliaMin: string;
      desSogliaMin = existSMin
        ? `≥ ${formatoImportoITA(sogliaMin, 4, true)}`
        : ``;
      let desSogliaMax: string;
      desSogliaMax = existSMax
        ? `≤ ${formatoImportoITA(sogliaMax, 4, true)}`
        : ``;

      // Definisco la descrizione per il canone minimo
      const canoneMinimo: number = r.canone_minimo;
      let desCanMinIT: string = formatoImportoITA(canoneMinimo, 2);
      let desCanMin: string = `${desCanMinIT} €`;

      // Definisco la descrizione completa di canone minimo
      let des: string;
      des = `<tr>
        <td> ${desSogliaMin}</td>
        <td> ${desSogliaMax}</td>
        <td> MINIMO ${desCanMin}</td>
      </tr>`;

      // Ritorno la descrizione della legenda
      return des;
      // #
    });

    // Verifico se esiste il minimo principale
    if (minPrinc != undefined) {
      // Definisco la descrizione per l'importo formattato
      const desMinPrincITNum: string = formatoImportoITA(minPrinc, 2);
      
      // Definisco le descrizioni per la composizione della legenda
      let desMinPrinc: string;
      // Definisco la descrizione per la legenda dell'importo minimo principale
      desMinPrinc = `<tr> <td colspan="3">MINIMO PRINCIPALE ${desMinPrincITNum} €</td> </tr>`;
      
      // Aggiungo in testata all'array dei ranges, la descrizione del minimo principale
      desRanges.unshift(desMinPrinc);
      // #
    }

    // Concateno le descrizioni generate, separandole da uno <br> per la visualizzazione
    desLeg = desRanges.join('');
    // Ritorno la legenda generata
    return `<table class="legenda-minimi"> ${desLeg} </table>`;
  }

  // Casistica finale indefinita
  return LegendaMinimiRegoleUso.vuoto;
};
