import { Pipe, PipeTransform } from '@angular/core';
import { Gruppo } from '../../../core/commons/vo/gruppo-vo';
import { IndirizzoSpedizioneVo } from '../../../core/commons/vo/indirizzo-spedizione-vo';
import { RecapitoVo } from '../../../core/commons/vo/recapito-vo';
import {
  indirizzoRecapito,
  isRecapitoAlternativo,
  isRecapitoPrincipale,
} from '../../services/risca/risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../../services/risca/risca-utilities/risca-utilities.service';

/**
 * Pipe che genera il title da visualizzare sopra un indirizzo di spedizione.
 */
@Pipe({ name: 'titleIndirizzoSpedizione' })
export class RiscaIndirizzoSpedizioneTitlePipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * Funzione che costruisce il title per un indirizzo di spedizione per il componente modale.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo contenente le informazioni dell'indirizzo di spedizione.
   * @param recapiti Array di RecapitoVo con le informazioni relative ai recapiti di gestione. Verrà selezionato in base alle informazioni dell'indirizzo di spedizione.
   * @param gruppi Array di Gruppo con le informazioni relative ai gruppi di gestione. Verrà selezionato in base alle informazioni dell'indirizzo di spedizione.
   * @returns string che definisce il titolo per la gestione dell'indirizzo di spedizione.
   */
  transform(
    indirizzoSpedizione: IndirizzoSpedizioneVo,
    recapiti: RecapitoVo[],
    gruppi?: Gruppo[]
  ): string {
    // Definisco un placeholder per il title
    const placeholder = 'Indirizzo di spedizione';

    // Verifico l'input
    if (!indirizzoSpedizione || !recapiti || recapiti.length === 0) {
      // Non si può costruire il title, nemmeno parzialmente
      return placeholder;
    }

    // Recupero le informazioni del recapito collegato all'indirizzo di spedizione
    const recapito = this.estraiRecapito(indirizzoSpedizione, recapiti);
    // Tento di recuperare le informazioni del gruppo (vale solo per certe situazioni, quindi non ci sarà sempre)
    const gruppo = this.estraiGruppo(indirizzoSpedizione, gruppi);

    // Definisco il contenitore per il titolo
    let title = '';
    // Genero la parte di titolo che identifica il recapito
    const titleR = this.generaTitoloRecapito(recapito);
    // Genero la parte di titolo che identifica il gruppo
    const titleG = this.generaTitoloGruppo(gruppo);

    // Verifico se esiste il titolo per il gruppo
    if (titleG !== '') {
      // Definisco il titolo completo
      title = `${titleG}<br>${titleR}`;
      // #
    } else {
      // Il titolo è composto solo dal recapito
      title = titleR;
    }

    // Trimmo gli spazi vuoti
    title = title.trim();

    // Ritorno il titolo per l'indirizzo di spedizione
    return title != '' ? title : placeholder;
  }

  /**
   * ##############################################
   * VERIFICA E RECUPERO DATI PER RECAPITO E GRUPPO
   * ##############################################
   */

  /**
   * Funzione di supporto che definisce le logiche di recupero per l'oggetto recapito collegato all'indirizzo di spedizione.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo contenente le informazioni dell'indirizzo di spedizione.
   * @param recapiti Array di RecapitoVo con le informazioni relative ai recapiti di gestione. Verrà selezionato in base alle informazioni dell'indirizzo di spedizione.
   * @returns RecapitoVo come oggetto di riferimento per l'indirizzo di spedizione.
   */
  private estraiRecapito(
    indirizzoSpedizione: IndirizzoSpedizioneVo,
    recapiti: RecapitoVo[]
  ): RecapitoVo {
    // Richiamo la funzione di utility
    return this._riscaUtilities.estraiRecapitoIS(indirizzoSpedizione, recapiti);
  }

  /**
   * Funzione di supporto che definisce le logiche di recupero per l'oggetto recapito collegato all'indirizzo di spedizione.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo contenente le informazioni dell'indirizzo di spedizione.
   * @param gruppi Array di Gruppo con le informazioni relative ai gruppi di gestione. Verrà selezionato in base alle informazioni dell'indirizzo di spedizione.
   * @returns Gruppo come oggetto di riferimento per l'indirizzo di spedizione.
   */
  private estraiGruppo(
    indirizzoSpedizione: IndirizzoSpedizioneVo,
    gruppi: Gruppo[]
  ): Gruppo {
    // RRichiamo la funzione di utility
    return this._riscaUtilities.estraiGruppoIS(indirizzoSpedizione, gruppi);
  }

  /**
   * ############################################
   * GENERAZIONE DEL TITOLO PER RECAPITO E GRUPPO
   * ############################################
   */

  /**
   * Funzione di supporto che genera il titolo per la sezione dell'indirizzo di spedizione.
   * La funzione prepara il titolo partendo dai dati: recapito.
   * @param recapito RecapitoVo con le informazioni per la costruzione del titolo.
   * @returns string che definisce il titolo creato, partendo dai dati in input.
   */
  private generaTitoloRecapito(recapito: RecapitoVo): string {
    // Verifico l'input
    if (!recapito) {
      // Ritorno stringa vuota
      return '';
    }

    // Definisco il contenitore per il title
    let title = '';
    let titleTR = this.generaTitoloTipoRecapito(recapito);
    let titleI = indirizzoRecapito(recapito);

    // Definisco la decorazione per il title tipo recapito
    titleTR = titleTR != '' ? `${titleTR}:` : '';
    // Definisco il title completo
    title = `${titleTR} <strong>${titleI}</strong>`;

    // Ritorno il title del recapito completo
    return title;
  }

  /**
   * Funzione di supporto che genera il titolo per la sezione dell'indirizzo di spedizione, per la parte del tipo recapito.
   * La funzione prepara il titolo partendo dai dati: tipo recapito.
   * @param recapito RecapitoVo con le informazioni per la costruzione del titolo.
   * @returns string che definisce il titolo creato, partendo dai dati in input.
   */
  private generaTitoloTipoRecapito(recapito: RecapitoVo): string {
    // Verifico l'input
    if (!recapito) {
      // Ritorno stringa vuota
      return '';
    }

    // Definisco il contenitore per il title
    let titleTR = '';

    // Verifico il tipo di recapito
    if (isRecapitoPrincipale(recapito)) {
      // Definisco la parte di title per il tipo recapito
      titleTR = 'Recapito principale';
      // #
    } else if (isRecapitoAlternativo(recapito)) {
      // Definisco la parte di title per il tipo recapito
      titleTR = 'Recapito alternativo';
      // #
    }

    // Ritorno il title per la parte del tipo recapito
    return titleTR;
  }

  /**
   * Funzione di supporto che genera il titolo per la sezione dell'indirizzo di spedizione.
   * La funzione prepara il titolo partendo dai dati: gruppo.
   * @param gruppo Gruppo con le informazioni per la costruzione del titolo.
   * @returns string che definisce il titolo creato, partendo dai dati in input.
   */
  private generaTitoloGruppo(gruppo: Gruppo): string {
    // Definisco le condizioni per la creazione del titolo
    const existG = gruppo != undefined;
    const existGDes = existG && gruppo.des_gruppo_soggetto != undefined;
    const definedGDes = existGDes && gruppo.des_gruppo_soggetto != '';

    // Verifico se esiste l'oggetto
    if (definedGDes) {
      // Esiste, ritorno il titolo formattato
      return `Gruppo: <strong>${gruppo.des_gruppo_soggetto}</strong>`;
      // #
    } else {
      // Non esiste, ritorno stringa vuota
      return '';
    }
  }
}

/**
 * Pipe che recupera, per un indirizzo di spedizione, il recapito da un array di recapiti.
 */
@Pipe({ name: 'recapitoPerIndSped' })
export class RiscaRecapitoPerIndirizzoSpedizionePipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * Funzione che costruisce il title per un indirizzo di spedizione per il componente modale.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo contenente le informazioni dell'indirizzo di spedizione.
   * @param recapiti Array di RecapitoVo con le informazioni relative ai recapiti di gestione. Verrà selezionato in base alle informazioni dell'indirizzo di spedizione.
   * @returns RecapitoVo alla quale l'indirizzo di spedizione è associato.
   */
  transform(
    indirizzoSpedizione: IndirizzoSpedizioneVo,
    recapiti: RecapitoVo[]
  ): RecapitoVo {
    // Verifico l'input
    if (!indirizzoSpedizione || !recapiti || recapiti.length === 0) {
      // Non ci sono le informazioni per recuperare l'oggetto
      return;
    }

    // Richiamo la funzione di utility e ritorno il dato
    return this._riscaUtilities.estraiRecapitoIS(indirizzoSpedizione, recapiti);
  }
}
