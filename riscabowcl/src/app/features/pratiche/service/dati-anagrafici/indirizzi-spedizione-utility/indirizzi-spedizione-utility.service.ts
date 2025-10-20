import { Injectable } from '@angular/core';
import { NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { cloneDeep, find, findIndex, forEach, uniq } from 'lodash';
import { Gruppo } from '../../../../../core/commons/vo/gruppo-vo';
import { IndirizzoSpedizioneVo } from '../../../../../core/commons/vo/indirizzo-spedizione-vo';
import { RecapitoVo } from '../../../../../core/commons/vo/recapito-vo';
import { SoggettoVo } from '../../../../../core/commons/vo/soggetto-vo';
import { LoggerService } from '../../../../../core/services/logger.service';
import { RiscaIndirizziSpedizioneModalComponent } from '../../../../../shared/modals/risca-indirizzi-spedizione-modal/risca-indirizzi-spedizione-modal.component';
import { IIndirizziSpedizioneModalConfig } from '../../../../../shared/modals/risca-indirizzi-spedizione-modal/utilities/risca-indirizzi-spedizione-modal.interfaces';
import { RiscaModalService } from '../../../../../shared/services/risca/risca-modal.service';
import { isSBNTrue } from '../../../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { RiscaGestioneISModal } from '../../../../../shared/utilities';
import { ApriModalConfigs } from '../../../../../shared/utilities/classes/risca-modal/risca-modal.classes';
import { IApriModalConfigs } from '../../../../../shared/utilities/interfaces/risca/risca-modal/risca-modal.interfaces';
import {
  IAggiornaIndirizzoSpedizione as IModificaIndirizzoSpedizione,
  ICorreggiIndirizziSpedizione,
  IIndSpedRecapitoGruppo,
  IInvalidISDataFromSoggetto,
} from '../../../interfaces/indirizzi-spedizione/indirizzi-spedizione.interfaces';
import {
  indirizzoSpedizioneFromRecapito,
  validIndirizzoSpedizione,
} from './indirizzi-spedizione-utility.functions';

@Injectable({
  providedIn: 'root',
})
export class IndirizziSpedizioneUtilityService {
  /**
   * Costruttore
   */
  constructor(
    private _logger: LoggerService,
    private _riscaModal: RiscaModalService
  ) {}

  /**
   * ####################################################################
   * FUNZIONE DI ESTRAZIONE DATI PER GLI INDIRIZZI DI SPEDIZIONE INVALIDI
   * ####################################################################
   */

  /**
   * Funzione di supporto che estrae le informazioni d'errore per quanto riguarda gli indirizzi di spedizione errati a seguito di un inserimento/modifica soggetto.
   * Le informazioni coinvolgono le informazioni di: recapiti, indirizzi spedizione ed eventuali gruppi.
   * @param soggetto SoggettoVo generato dal server come errore. Conterrà gli indirizzi di spedizione da correggere.
   */
  invalidIndirizziSpedizioneFromSoggetto(
    soggetto: SoggettoVo
  ): IInvalidISDataFromSoggetto {
    // Verifico l'input
    if (!soggetto || !soggetto.recapiti) {
      // Non ci sono informazioni per la correzione
      return;
    }

    // Dal soggetto vado ad estrarre la lista dei vari recapiti
    let recapitiErrati: RecapitoVo[];
    recapitiErrati = this.invalidRecapitiByIndirizziSpedizione(
      soggetto.recapiti
    );

    // Dalla lista dei recapiti errati estraggo la lista degli indirizzi invalidi
    let indirizziErrati: IndirizzoSpedizioneVo[];
    indirizziErrati =
      this.invalidIndirizziSpedizioneFromRecapiti(recapitiErrati);

    // Recupero i gruppi dal soggetto
    const gruppiSoggetto = soggetto.gruppo_soggetto as Gruppo[];
    // Dalla lista degli indirizzi invalidi, provo ad estrarre i gruppi
    let gruppiErrati: Gruppo[];
    gruppiErrati = this.invalidGruppiByIndirizziSpedizione(
      gruppiSoggetto,
      indirizziErrati
    );
    // Per la gestione del gruppo, in questo caso, se non ci sono elementi lo imposto ad undefined
    gruppiErrati = gruppiErrati?.length > 0 ? gruppiErrati : undefined;

    // Ritorno le informazioni estratte dalle varie funzioni
    return {
      indirizziSpedizione: indirizziErrati,
      recapiti: recapitiErrati,
      gruppi: gruppiErrati,
    };
  }

  /**
   * Funzione che estrae tutti i recapiti, dalla lista in input, che risultano avere almeno 1 indirizzo di spedizione errato.
   * @param recapiti Array di RecapitoVo che definisce i dati da verificare e filtrare.
   * @returns Array di RecapitoVo con tutti i recapiti che hanno almeno un indirizzo di spedizione invalido.
   */
  private invalidRecapitiByIndirizziSpedizione(
    recapiti: RecapitoVo[]
  ): RecapitoVo[] {
    // Verifico l'input
    if (!recapiti) {
      // Non ci sono informazioni
      return [];
    }

    // Dal soggetto vado ad estrarre la lista dei vari recapiti
    return recapiti.filter((r: RecapitoVo) => {
      // Verifico se il recapito ha degli indirizzi in errore
      return r.indirizzi_spedizione?.some((is: IndirizzoSpedizioneVo) => {
        // Ritorno la proprietà di validità
        return !isSBNTrue(is.ind_valido_postel);
      });
    });
  }

  /**
   * Funzione che estrae tutti gli indirizzi di spedizione, dalla lista in input dei recapiti, che risultano essere invalidi.
   * @param recapiti Array di RecapitoVo che definisce i dati per il recupero degli indirizzi di spedizione errati.
   * @returns Array di IndirizzoSpedizioneVo con tutti gli indirizzi errati.
   */
  private invalidIndirizziSpedizioneFromRecapiti(
    recapiti: RecapitoVo[]
  ): RecapitoVo[] {
    // Verifico l'input
    if (!recapiti) {
      // Non ci sono informazioni
      return [];
    }

    // Dalla lista dei recapiti errati estraggo la lista degli indirizzi invalidi
    let indirizziErrati: IndirizzoSpedizioneVo[] = [];
    // Ciclo per i recapiti errati ed estraggo gli indirizzi invalidi
    recapiti.forEach((re: RecapitoVo) => {
      // Recupero gli indirizzi di spedizione
      const indSped = re.indirizzi_spedizione;
      // Filtro gli indirizzi per gli oggetti errati
      const isErr = indSped.filter((is: IndirizzoSpedizioneVo) => {
        // Ritorno gli oggetti con valido postel a 0
        return !isSBNTrue(is.ind_valido_postel);
      });

      // Unisco le informazioni degli indirizzi errati, con gli indirizzi errati di questo specifico recapito
      indirizziErrati = [...indirizziErrati, ...isErr];
    });

    // Ritorno la lista degli indirizzi
    return indirizziErrati;
  }

  /**
   * Funzione che estrae tutti i gruppi che risultano essere collegati agli indirizzi di spedizione in input..
   * @param gruppi Array di Gruppo con le informazioni da estrarre a seconda degli indirizzi errati.
   * @param indirizziSpedizione Array di IndirizzoSpedizioneVo con le informazioni degli indirizzi per estrarre i gruppi.
   * @returns Array di Gruppo contenente le informazioni dei gruppi che sono associati ai recapiti.
   */
  private invalidGruppiByIndirizziSpedizione(
    gruppi: Gruppo[],
    indirizziSpedizione: IndirizzoSpedizioneVo[]
  ): Gruppo[] {
    // Verifico l'input
    if (!gruppi || !indirizziSpedizione) {
      // Non ci sono informazioni
      return [];
    }
    // Verifico se ci sono gruppi e indirizzi da ciclare
    if (gruppi.length === 0 || indirizziSpedizione.length === 0) {
      // Non ci sono informazioni
      return [];
    }

    // Recupero dagli indirizzi tutti i possibili id_gruppo_soggetto definiti
    const idGruppiErr = indirizziSpedizione
      .map((ie: IndirizzoSpedizioneVo) => {
        // Ritorno gli id del gruppo
        return ie.id_gruppo_soggetto;
      })
      // Applico un filtro per rimuovere tutti i valori undefined
      .filter((idGruppo: number) => idGruppo != undefined);

    // Verifico se ci sono informazioni per gli id gruppi
    if (idGruppiErr?.length > 0) {
      // Ci sono informazioni, pulisco i valori duplicati
      const idGruppiErrUniq = uniq(idGruppiErr);

      // Vado a recuperare, per ogni id gruppo, l'oggetto del gruppo
      const gruppiErrati: Gruppo[] = idGruppiErrUniq
        .map((idGruppo: number) => {
          // Effettuo una find per stesso id gruppo
          return find(gruppi, (g: Gruppo) => g.id_gruppo_soggetto === idGruppo);
        })
        // Vado a pulire l'array di oggetti undefined
        .filter((gruppo: Gruppo) => gruppo != undefined);

      // Ritorno la lista dei gruppi che risultano in errore
      return gruppiErrati;
      // #
    } else {
      // Non ci sono informazioni per i gruppi
      return [];
    }
  }

  /**
   * ##########################################################################################
   * FUNZIONI CHE ESTRAGGONO LE INFORMAZIONI DI UN INDIRIZZO DI SPEDIZIONE A SECONDA DELL'INPUT
   * ##########################################################################################
   */

  /**
   * Funzione di comodo che estrae le informazioni di un indirizzo di spedizione e tutti i dati a lui connessi, partendo dalla configurazione in input.
   * La funzione partirà a gestire le logiche da un oggetto soggetto.
   * @param soggetto SoggettoVo che definisce la base delle informazioni per l'estrazione dell'indirizzo di spedizione
   * @param idRecapito number che definisce l'id recapito come riferimento per l'estrazione dell'indirizzo di spedizione.
   * @param idGruppo number che definisce l'id gruppo come riferimento per l'estrazione dell'indirizzo di spedizione.
   * @returns IIndirizzoSpedizioneDatiCollegati con le informazioni estratte dal soggetto, se qualcosa è andato storto: undefined.
   */
  indirizzoSpedizioneFromSoggetto(
    soggetto: SoggettoVo,
    idRecapito: number,
    idGruppo?: number
  ): IIndSpedRecapitoGruppo {
    // Verifico l'input
    if (!soggetto || !idRecapito) {
      // Non ci sono i dati minimi per il controllo
      return undefined;
    }

    // Estraggo dal soggetto le informazioni dei recapiti
    const recapiti = soggetto.recapiti;
    // Verifico i recapiti esistano
    if (!recapiti || recapiti.length === 0) {
      // Non ci sono i dati per gli indirizzi
      return undefined;
    }

    // Recupero il recapito richiesto dall'input
    const recapito = find(recapiti, (r: RecapitoVo) => {
      // Verifico per stesso id
      return r.id_recapito === idRecapito;
    });
    // Verifico se il recapito esiste
    if (!recapito) {
      // Non esiste il recapito richiesto
      return undefined;
    }

    // Recupero le informazioni relative all'indirizzo di spedizione
    const indirizzoSpedizione = this.indirizzoSpedizioneFromRecapito(
      recapito,
      idGruppo
    );
    // Verifico se l'indirizzo esiste
    if (!recapito) {
      // Non esiste l'indirizzo richiesto
      return undefined;
    }

    // Definisco un contenitore per il possibile gruppo
    let gruppo = undefined;
    // Se esiste l'id gruppo, tento di recuperarlo
    if (idGruppo != undefined) {
      // Recupero i gruppi
      const gruppi = soggetto.gruppo_soggetto;
      // Tento di recuperare il gruppo dal soggetto
      gruppo = find(gruppi, (g: Gruppo) => g.id_gruppo_soggetto === idGruppo);
      // Verifico se l'indirizzo esiste
      if (!gruppo) {
        // Il gruppo deve esistere, altrimenti l'indirizzo di spedizione non sarà coerente con i dati
        return undefined;
      }
    }

    // Ritorno le informazioni estratte
    return { indirizzoSpedizione, recapito, gruppo };
  }

  /**
   * Funzione che estrae un indirizzo di spedizione da un oggetto recapito.
   * Se definito in input anche un idGruppo, la condizione di ricerca dell'indirizzo di spedizione verrà effettuata anche su quel valore.
   * @param recapito RecapitoVo dalla quale estrarre i dati dell'indirizzo di spedizione.
   * @param idGruppo number che definisce l'id gruppo per l'estrazione dell'informazione dell'indirizzo di spedzione.
   * @returns IndirizzoSpedizioneVo estratto dal recapito. Se qualcosa è andato storto: undefined.
   */
  indirizzoSpedizioneFromRecapito(
    recapito: RecapitoVo,
    idGruppo?: number
  ): IndirizzoSpedizioneVo {
    // Richiamo la funzione di recupero
    return indirizzoSpedizioneFromRecapito(recapito, idGruppo);
  }

  /**
   * Funzione di controllo che verifica la validità di un oggetto IndirizzoSpedizioneVo, sulla base degli idRecapito e possibile idGruppo passati in input.
   * @param indirizzo IndirizzoSpedizioneVo da verificare.
   * @param idRecapito number che definisce l'id recapito da verificare all'interno dell'indirizzo di spedizione.
   * @param idGruppo number che definisce l'id gruppo da verificare all'interno dell'indirizzo di spedizione.
   */
  validIndirizzoSpedizione(
    indirizzoSpedizione: IndirizzoSpedizioneVo,
    idRecapito: number,
    idGruppo?: number
  ): boolean {
    // Richiamo la funzione di check
    return validIndirizzoSpedizione(indirizzoSpedizione, idRecapito, idGruppo);
  }

  /**
   * ###############################################################################################
   * FUNZIONE CHE AGGIORNA I DATI DI UN SOGGETO FORMALMENTE INVALIDO PER GLI INDIRIZZI DI SPEDIZIONE
   * ###############################################################################################
   */

  /**
   * Funzione di supporto che aggiorna le informazioni del soggetto coinvolte dalla struttura dati degli indirizzi di spedizione passati in input.
   * @param soggetto SoggettoVo d'aggiornare con le informazioni degli indirizzi di spedizione corretti.
   * @param indirizziSpedizione Array di IndirizzoSpedizioneVo con la lista d'informazioni per l'aggiornamento.
   * @returns SoggettoVo come copia dell'oggetto in input e con le informazioni aggiornate.
   */
  updateSoggettoWithValidIndirizziSpedizione(
    soggetto: SoggettoVo,
    indirizziSpedizione: IndirizzoSpedizioneVo[]
  ): SoggettoVo {
    // Verifico l'input
    if (!soggetto || !indirizziSpedizione) {
      // Niente d'aggiornare
      return soggetto;
    }

    // Creo una copia del soggetto sulla quale lavorare
    const s = cloneDeep(soggetto);

    // Ciclo la lista degli indirizzi di spedizione
    forEach(indirizziSpedizione, (indirizzo: IndirizzoSpedizioneVo) => {
      // Verifico per sicurezza lo stato del flag
      if (isSBNTrue(indirizzo.ind_valido_postel)) {
        // Indirizzo valido recupero i recapiti del soggetto
        const recapiti = s.recapiti;
        // Recupero l'indice posizionale del recapito per aggiornare l'indirizzo
        const iRecapito = this.indexRecapitoByIS(recapiti, indirizzo);

        // Verifico l'indice posizionale del recapito che esista
        if (iRecapito >= 0) {
          // Esiste, recupero l'oggetto
          const recapito = recapiti[iRecapito];
          // Recupero l'indice posizionale per l'indirizzo ciclato
          const iIndirizzo = this.indexIndirizzoSpedizioneByRecIndSped(
            recapito,
            indirizzo
          );

          // Verifico l'indice posizionale dell'indirizzo di spedizione
          if (iIndirizzo >= 0) {
            // Ho le informazioni per aggiornare le informazioni
            s.recapiti[iRecapito].indirizzi_spedizione[iIndirizzo] = indirizzo;
          }
        }
        // #
      } else {
        // Loggo un warning
        this._logger.warning('Update soggetto is', JSON.stringify(indirizzo));
      }
    });

    // Ritorno il soggetto aggiornato
    return s;
  }

  /**
   * Funzione di comodo che, dato un array di recapiti, estrae il suo indice posizionale sulla base dell'id recapit dentro l'oggetto indirizzo passato in input.
   * @param recapiti Array di RecapitoVo per la quale definire l'indice posizionale.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo con l'oggetto che definirà l'id del recapito da recuperare.
   * @returns number che indica la posizone dell'elemento nell'array. Se torna -1, l'elemento non è presente nell'array. Se torna -2, vuol dire che l'input è undefined.
   */
  indexRecapitoByIS(
    recapiti: RecapitoVo[],
    indirizzoSpedizione: IndirizzoSpedizioneVo
  ): number {
    // Verifico l'input
    if (!recapiti || !indirizzoSpedizione) {
      // Ritorno un indice negativo
      return -2;
    }

    // Lancio la find index per l'array di recapiti
    return findIndex(recapiti, (recapito: RecapitoVo) => {
      // Recupero l'id del recapito dall'indirizzo di spedizione
      const idRecapito = indirizzoSpedizione.id_recapito;
      // Ritorno il check sull'id
      return recapito.id_recapito === idRecapito;
    });
  }

  /**
   * Funzione di comodo che, dato un array di recapiti, estrae il suo indice posizionale sulla base dell'id recapit dentro l'oggetto indirizzo passato in input.
   * @param recapito RecapitoVo per la quale definire l'indice posizionale dell'indirizzo di spedizione.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo con l'oggetto che definirà l'id dell'indirizzo di spedizione da ritornare.
   * @returns number che indica la posizone dell'elemento nell'array. Se torna -1, l'elemento non è presente nell'array. Se torna -2, vuol dire che l'input è undefined.
   */
  indexIndirizzoSpedizioneByRecIndSped(
    recapito: RecapitoVo,
    indirizzoSpedizione: IndirizzoSpedizioneVo
  ): number {
    // Definisco le variabili di controllo
    const invalidRecapito =
      !recapito ||
      !recapito.indirizzi_spedizione ||
      recapito.indirizzi_spedizione.length === 0;

    // Verifico l'input
    if (invalidRecapito || !indirizzoSpedizione) {
      // Ritorno un indice negativo
      return -2;
    }

    // Recupero gli indirizzi di spedizione del recapito
    const indirizziRecapito = recapito.indirizzi_spedizione;

    // Lancio la find index per l'array di recapiti
    return findIndex(indirizziRecapito, (indirizzoR: IndirizzoSpedizioneVo) => {
      // Recupero l'id del recapito dall'indirizzo di spedizione
      const idIndirizzoR = indirizzoR.id_recapito_postel;
      // Recupero l'id dell'oggetto recapito in input
      const idIndirizzo = indirizzoSpedizione.id_recapito_postel;

      // Definisco le condizioni di verifica
      const okIdInd = idIndirizzoR === idIndirizzo;
      // Ritorno il check sull'id
      return okIdInd;
    });
  }

  /**
   * Funzione di comodo che, dato un array di recapiti, estrae il suo indice posizionale sulla base dell'id recapit dentro l'oggetto indirizzo passato in input.
   * @param recapito RecapitoVo per la quale definire l'indice posizionale dell'indirizzo di spedizione.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo con l'oggetto che definirà l'id dell'indirizzo di spedizione da ritornare.
   * @param idGruppo number che definisce l'id gruppo per l'individuazione dell'indirizzo di spedizione.
   * @returns number che indica la posizone dell'elemento nell'array. Se torna -1, l'elemento non è presente nell'array. Se torna -2, vuol dire che l'input è undefined.
   */
  indexIndirizzoSpedizioneByRecGrupIndSped(
    recapito: RecapitoVo,
    indirizzoSpedizione: IndirizzoSpedizioneVo,
    idGruppo: number
  ): number {
    // Definisco le variabili di controllo
    const invalidRecapito =
      !recapito ||
      !recapito.indirizzi_spedizione ||
      recapito.indirizzi_spedizione.length === 0;

    // Verifico l'input
    if (invalidRecapito || !indirizzoSpedizione) {
      // Ritorno un indice negativo
      return -2;
    }

    // Recupero gli indirizzi di spedizione del recapito
    const indirizziRecapito = recapito.indirizzi_spedizione;

    // Lancio la find index per l'array di recapiti
    return findIndex(indirizziRecapito, (indirizzoR: IndirizzoSpedizioneVo) => {
      // Recupero l'id del recapito dall'indirizzo di spedizione
      const idIndirizzoR = indirizzoR.id_recapito_postel;
      // Recupero l'id dell'oggetto recapito in input
      const idIndirizzo = indirizzoSpedizione.id_recapito_postel;
      // Recupero l'id_gruppo_soggetto dell'indirizzo di spedizione
      const idGIS = indirizzoSpedizione.id_gruppo_soggetto;

      // Definisco le condizioni di verifica
      const okIdInd = idIndirizzoR === idIndirizzo;
      const okIdGru =
        idGruppo != undefined ? idGruppo == idGIS : idGIS == undefined;

      // Ritorno il check sull'id
      return okIdInd && okIdGru;
    });
  }

  /**
   * ###################################################################################################
   * FUNZIONE DI COMODO CHE PERMETTE DI AGGIORNARE UN INDIRIZZO DI SPEDIZIONE ALL'INTERNO DI UN SOGGETTO
   * ###################################################################################################
   */

  /**
   * Funzione di supporto che modifica un indirizzo di spedizione all'interno di un oggetto soggetto.
   * La modifica avverrà per referenza.
   * @param soggetto SoggettoVo come oggetto da modificare con il nuovo indirizzo di spedizione.
   * @param idRecapito number che definisce per quale recapito bisogna aggiornare l'indirizzo di spedizione.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo cone le informazioni d'aggiornare.
   * @param idGruppo number che definisce l'id gruppo per l'individuazione dell'indirizzo di spedizione.
   */
  aggiornaIndirizzoSpedizioneInSoggetto(
    soggetto: SoggettoVo,
    idRecapito: number,
    indirizzoSpedizione: IndirizzoSpedizioneVo,
    idGruppo?: number
  ) {
    // Verifico l'oggeto in input
    if (!soggetto || !idRecapito || !indirizzoSpedizione) {
      // Blocco le logiche
      return;
    }

    // Recupero la lista dei recapiti del soggetto
    const recapiti = soggetto.recapiti;
    // Verifico che esistano i recapiti
    if (!recapiti) {
      // Non ci sono dati
      return;
    }

    // Vado a cercare ilr recapito per l'aggiornamento
    const recapito = find(recapiti, (r: RecapitoVo) => {
      // Effettuo il match tra id
      return r.id_recapito === idRecapito;
    });
    // Verifico che esista il recapito
    if (!recapito) {
      // Non c'è il dato d'aggiornare
      return;
    }

    // Richiamo la funzione di aggiornamento dati partendo dal recapito
    this.aggiornaIndirizzoSpedizioneInRecapito(
      recapito,
      indirizzoSpedizione,
      idGruppo
    );
  }

  /**
   * ###################################################################################################
   * FUNZIONE DI COMODO CHE PERMETTE DI AGGIORNARE UN INDIRIZZO DI SPEDIZIONE ALL'INTERNO DI UN RECAPITO
   * ###################################################################################################
   */

  /**
   * Funzione di supporto che modifica un indirizzo di spedizione all'interno di un oggetto recapito.
   * La modifica avverrà per referenza.
   * @param recapito RecapitoVo come oggetto da modificare con il nuovo indirizzo di spedizione.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo cone le informazioni d'aggiornare.
   * @param idGruppo number che definisce l'id gruppo per l'individuazione dell'indirizzo di spedizione.
   */
  aggiornaIndirizzoSpedizioneInRecapito(
    recapito: RecapitoVo,
    indirizzoSpedizione: IndirizzoSpedizioneVo,
    idGruppo?: number
  ) {
    // Verifico l'oggeto in input
    if (!recapito || !indirizzoSpedizione) {
      // Blocco le logiche
      return;
    }

    // Recupero la lista degli indirizzi del recapito
    const indirizzi = recapito.indirizzi_spedizione;
    // Verifico esistano gli indirizzi
    if (!indirizzi) {
      // Nesson dato d'aggiornare
      return;
    }

    // Cerco all'interno dell'array l'indice dell'indirizzo di spedizione
    const iIS = this.indexIndirizzoSpedizioneByRecGrupIndSped(
      recapito,
      indirizzoSpedizione,
      idGruppo
    );
    // Verifico se è stata trovata referenza
    if (iIS < 0) {
      // Si è verificato un errore
      return;
    }

    // Indice posizionale trovato, aggiorno l'indirizzo
    recapito.indirizzi_spedizione[iIS] = indirizzoSpedizione;
  }

  /**
   * ########################################################################################################
   * FUNZIONE PER LA CORREZIONE DEGLI INDIRIZZI DI SPEDZIONE ERRATI ALL'INTERNO DI UN SOGGETTO TRAMITE MODALE
   * ########################################################################################################
   */

  /**
   * Funzione che gestisce la correzione degli indirizzi di spedizione che risultano presenti all'interno di un soggetto.
   * @param config ICorreggiIndirizziSpedizione come oggetto contenente le informazioni per la gestione della correzione degli indirizzi di spedizione di un soggetto.
   */
  correggiIndirizziSpedizioneSoggetto(config: ICorreggiIndirizziSpedizione) {
    // Verifico l'input
    if (!config) {
      // Non ci sono informazioni per la correzione
      return;
    }

    config.callbacks = undefined;

    // Estraggo le informazioni dall'oggetto di configurazione
    const { soggetto, callbacks } = config;
    // Verifico l'input
    if (!soggetto || !soggetto.recapiti) {
      // Non ci sono informazioni per la correzione
      return;
    }

    // Richiamo la funzione di comodo del servizio
    const errData: IInvalidISDataFromSoggetto =
      this.invalidIndirizziSpedizioneFromSoggetto(soggetto);
    // Definisco la gestione come correzione
    const correzione = RiscaGestioneISModal.correzione;

    // Creo la configurazione di parametri per correggere gli indirizzi di spedizione
    const dataModal: IIndirizziSpedizioneModalConfig = {
      gestione: correzione,
      recapiti: errData.recapiti,
      indirizziSpedizione: errData.indirizziSpedizione,
      gruppi: errData.gruppi,
    };
    // Preparo le configurazioni per l'apertura della modale
    const modalConfig: IApriModalConfigs = { params: { dataModal }, callbacks };
    // Richiamo l'apertura della modale
    this.apriModaleIS(modalConfig);
  }

  /**
   * ######################################################################################################
   * FUNZIONE PER LA MODIFICA DEGLI INDIRIZZI DI SPEDZIONE ERRATI ALL'INTERNO DI UN SOGGETTO TRAMITE MODALE
   * ######################################################################################################
   */

  /**
   * Funzione che gestisce la modifica di un indirizzo di spedizione di un soggetto.
   * @param config IAggiornaIndirizzoSpedizione come oggetto contenente le informazioni per la gestione della modifica di un indirizzo di spedizione di un soggetto.
   */
  modificaIndirizziSpedizioneSoggetto(config: IModificaIndirizzoSpedizione) {
    // Verifico l'input
    if (!config) {
      // Non ci sono informazioni per la correzione
      return;
    }

    // Estraggo le informazioni dall'oggetto di configurazione
    const { soggetto, callbacks, idRecapito, idGruppo, isFormDisabilitato } =
      config;
    // Definisco le confizioni di controllo
    const okS = soggetto != undefined;
    const okR = okS && soggetto.recapiti != undefined;
    const okIdR = idRecapito != undefined;
    // Verifico l'input
    if (!okR || !okIdR) {
      // Non ci sono informazioni per la correzione
      return;
    }

    // Richiamo la funzione di comodo del servizio
    let datiIS: IIndSpedRecapitoGruppo;
    datiIS = this.indirizzoSpedizioneFromSoggetto(
      soggetto,
      idRecapito,
      idGruppo
    );
    // Definisco la gestione come aggiornamento singolo
    const aggiornamentoSingolo = RiscaGestioneISModal.aggiornamentoSingolo;

    // Creo la configurazione di parametri per correggere gli indirizzi di spedizione
    const dataModal: IIndirizziSpedizioneModalConfig = {
      gestione: aggiornamentoSingolo,
      recapiti: [datiIS.recapito],
      indirizziSpedizione: [datiIS.indirizzoSpedizione],
      gruppi: [datiIS.gruppo],
      isFormDisabilitato: isFormDisabilitato ?? false,
    };
    // Preparo le configurazioni per l'apertura della modale
    const modalConfig: IApriModalConfigs = { params: { dataModal }, callbacks };
    // Richiamo l'apertura della modale
    this.apriModaleIS(modalConfig);
  }

  /**
   * ####################################
   * GESTIONE DELLA MODALE PER L'APERTURA
   * ####################################
   */

  /**
   * Funzione che apre un modale e visualizza i dati di un indirizzo di spedizione.
   * @param config ApriModalConfigs contenente le informazioni di configurazione per l'apertura della modale.
   */
  apriModaleIS(config: IApriModalConfigs) {
    // Variabili di configurazione fisse
    const component = RiscaIndirizziSpedizioneModalComponent;
    const options: NgbModalOptions = {
      windowClass: 'r-mdl-indirizzi-spedizione',
      backdrop: 'static',
    };

    // Richiamo l'apertura del modale passando le configurazioni
    this._riscaModal.apriModal(
      new ApriModalConfigs({
        component,
        options: { ...options, ...config?.options },
        callbacks: config?.callbacks,
        params: config?.params,
      })
    );
  }
}
