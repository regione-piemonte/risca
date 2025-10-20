import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { clone } from 'lodash';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { RecapitoVo } from '../../../../core/commons/vo/recapito-vo';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import {
  CodiciTipiRecapito,
  TipoRecapitoVo,
} from '../../../../core/commons/vo/tipo-recapito-vo';
import { ConfigService } from '../../../../core/services/config.service';
import { LoggerService } from '../../../../core/services/logger.service';
import { UserService } from '../../../../core/services/user.service';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaCompareService } from '../../../../shared/services/risca/risca-compare.service';
import { RiscaModalService } from '../../../../shared/services/risca/risca-modal.service';
import {
  isRecapitoAlternativo,
  isRecapitoPrincipale,
} from '../../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  ParseValueRules,
  RiscaContatto,
  RiscaRecapito,
} from '../../../../shared/utilities';
import { ApriModalConfigs } from '../../../../shared/utilities/classes/risca-modal/risca-modal.classes';
import { AppActions } from '../../../../shared/utilities/enums/utilities.enums';
import { IRecapitoAlternativoModal } from '../../interfaces/recapiti/recapito-alternativo-modal.interfaces';

/**
 * Interfaccia dedicata all'oggetto di configurazione per il parsing da dati recapito form a RecapitoVo
 */
export interface IDatiRecapitoFormConfigs {
  mapData: Map<string, any>;
  keyRecapito: string;
  keyContatti: string;
  tipoRecapito?: TipoRecapitoVo;
}

/**
 * Classe utilizzata per la generazione di un oggetto RecapitoVo dai dati dei form recapito e contatti.
 */
export class DatiRecapitoFormConfigs {
  /** Map con i dati del form recpito e contatti. */
  mapData: Map<string, any>;
  /** string che identifica la chiave di recupero dell'oggetto recapito. */
  keyRecapito: string;
  /** string che identifica la chiave di recupero dell'oggetto contatti. */
  keyContatti: string;
  /** Per default il valore è impostato a CodiciTipoRecapito.alternativo. */
  tipoRecapito: TipoRecapitoVo;

  constructor(c: IDatiRecapitoFormConfigs) {
    this.mapData = c?.mapData;
    this.keyRecapito = c?.keyRecapito;
    this.keyContatti = c?.keyContatti;
    this.tipoRecapito = c?.tipoRecapito;
  }
}

@Injectable({
  providedIn: 'root',
})
export class RecapitiService {
  /** Oggetto di costanti comuni all'applicazione. */
  private C_C = new CommonConsts();

  /** Costante per il path: /soggetti */
  private PATH_SOGGETTI = '/soggetti';
  /** Costante per il path: /tipi-recapito */
  private PATH_TIPI_RECAPITO = '/tipi-recapito';

  /** String contenente il path url del be. */
  private beUrl: string;

  /** TipoRecapitoVo che definisce per il tipo recapito: principale. */
  private _tipoRecapitoPrincipale: TipoRecapitoVo;
  /** TipoRecapitoVo che definisce per il tipo recapito: alternativo. */
  private _tipoRecapitoAlternativo: TipoRecapitoVo;

  /**
   * Costruttore
   */
  constructor(
    private _config: ConfigService,
    private _http: HttpClient,
    private _logger: LoggerService,
    private _riscaCompare: RiscaCompareService,
    private _riscaModal: RiscaModalService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {
    // Setup del be url
    this.beUrl = this._config.getBEUrl();
  }

  /**
   * ###########################
   * FUNZIONI DI PARSING OGGETTI
   * ###########################
   */

  /**
   * Funzione di convert dei dati front-end per un recapito in un oggetto compatibile con SoggettoVo.
   * @param recapito RiscaRecapito da convertire.
   * @param contatti RiscaContatto da convertire.
   * @returns Oggetto parziale SoggettoVo convertito.
   */
  convertRiscaRecapitoToSoggettoVo(
    recapito: RiscaRecapito,
    contatti: RiscaContatto
  ): SoggettoVo {
    // Verifico l'input
    if (!recapito) {
      // Ritorno un oggetto vuoto
      return {};
    }

    // Creo un nuovo oggetto di tipo SoggettoVo
    const datiAnagrafici = new SoggettoVo();
    // Definisco un array che andrà a contenere i dati del recapito
    const recapiti: RecapitoVo[] = [];

    // Effettuo la mappatura dei dati
    const recapitoVo = this.convertRiscaRecapitoToRecapitoVo(
      recapito,
      contatti
    );
    // Una volta generato l'oggetto lo inserisco nell'array
    recapiti.push(recapitoVo);
    // Aggiungo ai dati anagrafici l'array
    datiAnagrafici.recapiti = recapiti;

    // Ritorno l'oggetto
    return datiAnagrafici;
  }

  /**
   * Funzione che effettua il convert di un oggetto RiscaRecapito ad un oggetto RecapitoVo.
   * @param recapito RiscaRecapito da convertire.
   * @param contatti RiscaContatto da convertire.
   * @returns RecapitoVo convertito.
   */
  convertRiscaRecapitoToRecapitoVo(
    recapito: RiscaRecapito,
    contatti: RiscaContatto
  ): RecapitoVo {
    // Verifico l'input
    if (!recapito) return undefined;

    // Effettuo la mappatura dei dati
    const recapitoVo: RecapitoVo = {
      // Obbligatori
      tipo_recapito: recapito.tipoRecapito,
      tipo_invio: contatti.tipoInvio,
    };

    // Facoltativi recapito
    recapitoVo.tipo_sede = this._riscaUtilities.valueOrDefault(
      recapito.tipoSede,
      null
    );
    recapitoVo.presso = this._riscaUtilities.valueOrDefault(
      recapito.presso,
      null
    );
    recapitoVo.nazione_recapito = this._riscaUtilities.valueOrDefault(
      recapito.stato,
      null
    );
    recapitoVo.comune_recapito = this._riscaUtilities.valueOrDefault(
      recapito.comune,
      null
    );
    recapitoVo.citta_estera_recapito = this._riscaUtilities.valueOrDefault(
      recapito.cittaEsteraRecapito,
      null
    );
    recapitoVo.des_localita = this._riscaUtilities.valueOrDefault(
      recapito.localita,
      null
    );
    recapitoVo.indirizzo = this._riscaUtilities.valueOrDefault(
      recapito.indirizzo,
      null
    );
    recapitoVo.num_civico = this._riscaUtilities.valueOrDefault(
      recapito.numeroCivico,
      null
    );
    recapitoVo.cap_recapito = this._riscaUtilities.valueOrDefault(
      recapito.cap,
      null
    );

    // Facoltativo contatti
    recapitoVo.cellulare = this._riscaUtilities.valueOrDefault(
      contatti.cellulare,
      null
    );
    recapitoVo.email = this._riscaUtilities.valueOrDefault(
      contatti.email,
      null
    );
    recapitoVo.telefono = this._riscaUtilities.valueOrDefault(
      contatti.telefono,
      null
    );
    recapitoVo.pec = this._riscaUtilities.valueOrDefault(contatti.pec, null);

    // Indirizzi di spedizione
    recapitoVo.indirizzi_spedizione = recapito.indirizziSpedizione ?? [];

    // Ritorno l'oggetto
    return recapitoVo;
  }

  /**
   * #################
   * CHIAMATE ALLE API
   * #################
   */

  /**
   * Funzione che recupera i tipi recapito dal server.
   */
  getTipiRecapito(): Observable<TipoRecapitoVo[]> {
    // Variabili di comodo
    const method = 'getTipiRecapito';
    // Loggo l'azione
    // this.logger.log(`${method}`, '');

    // Definisco l'url
    const url = `${this.beUrl}${this.PATH_TIPI_RECAPITO}`;

    // Lancio la chiamata per il recupero dati
    return this._http.get<TipoRecapitoVo[]>(url).pipe(
      tap((tipiRecapito: TipoRecapitoVo[]) => {
        // Estraggo le due informazioni per: recapito principale e recapito alternativo
        this._tipoRecapitoPrincipale = tipiRecapito?.find((tr) => {
          // Recupero il codice per recapito principale
          const trP = CodiciTipiRecapito.principale;
          // Verifico se il recapito è principale
          return tr.cod_tipo_recapito === trP;
        });
        this._tipoRecapitoAlternativo = tipiRecapito?.find((tr) => {
          // Recupero il codice per recapito principale
          const trA = CodiciTipiRecapito.alternativo;
          // Verifico se il recapito è principale
          return tr.cod_tipo_recapito === trA;
        });
      })
    );
  }

  /**
   * Funzione che rimuove dal DB un oggetto recapito alternativo, riferito ad un soggetto.
   * @param idSoggetto number che definisce l'id del soggetto per la cancellazione.
   * @param idRecapitoA number che definisce l'id del recapito alternativo per la cancellazione.
   * @returns Observable<RecapitoVo[]> con la lista dei recapiti alternativi del soggetto aggiornati.
   */
  removeRecapitoAlternativo(
    idSoggetto: number,
    idRecapitoA: number
  ): Observable<RecapitoVo[]> {
    // Definisco l'url
    const url = `${this.beUrl}${this.PATH_SOGGETTI}/${idSoggetto}`;
    // Definisco i query params per la chiamata
    const params = this._riscaUtilities.createQueryParams({
      idRecapito: idRecapitoA,
    });

    // Lancio la chiamata per il recupero dati
    return this._http.delete<SoggettoVo>(url, { params }).pipe(
      map((soggettoAggiornato: SoggettoVo) => {
        // Ritorno la lista dei recapiti alternativi
        return soggettoAggiornato?.recapiti?.filter((r: RecapitoVo) => {
          // Filtro per recapito alternativo
          return isRecapitoAlternativo(r);
          // #
        });
      })
    );
  }

  /**
   * ##################
   * FUNZIONI DI COMODO
   * ##################
   */

  /**
   * Funzione che estrae l'oggetto del recapito principale da un oggetto SoggettoVo.
   * @param datiAnagrafici SoggettoVo contenente le informazioni dei dati anagrafici.
   * @returns RecapitoVo contenente i dati del recapito principale.
   */
  getRecapitoPrincipaleFromSoggettoVo(datiAnagrafici: SoggettoVo): RecapitoVo {
    // Verifico l'input
    if (!datiAnagrafici) {
      return;
    }

    // Recupero i recapiti
    const recapiti = datiAnagrafici.recapiti;
    // Verifico esistano i recapiti
    if (!recapiti) {
      return;
    }

    // Ricerco e ritorno il recapito principale
    return this.getRecapitoPrincipaleFromRecapiti(recapiti);
  }

  /**
   * Funzione che estrae la lista di oggetti recapiti alternativi da un oggetto SoggettoVo.
   * @param datiAnagrafici SoggettoVo contenente le informazioni dei dati anagrafici.
   * @returns Array di RecapitoVo contenente i dati dei recapiti alternativi.
   */
  getRecapitiAlternativiFromSoggettoVo(
    datiAnagrafici: SoggettoVo
  ): RecapitoVo[] {
    // Verifico l'input
    if (!datiAnagrafici) {
      return [];
    }

    // Recupero i recapiti
    const recapiti = datiAnagrafici.recapiti;
    // Verifico esistano i recapiti
    if (!recapiti) {
      return [];
    }

    // Ricerco e ritorno il recapito principale
    return this.getRecapitiAlternativiFromRecapiti(recapiti);
  }

  /**
   * Funzione che estrae l'oggetto del recapito principale da una lista di oggetti RecapitoVo.
   * @param recapiti Array di RecapitoVo contenente le informazioni dei recapiti.
   * @returns RecapitoVo contenente i dati del recapito principale.
   */
  getRecapitoPrincipaleFromRecapiti(recapiti: RecapitoVo[]): RecapitoVo {
    // Verifico se esistono i recapiti
    if (!recapiti) {
      return;
    }

    // Ricerco il recapito principale
    return recapiti.find((r) => {
      // Verifico se il recapito è principale
      return isRecapitoPrincipale(r);
      // #
    });
  }

  /**
   * Funzione che estrae la lista di oggetti recapiti alternativi da una lista di oggetti RecapitoVo.
   * @param recapiti Array di RecapitoVo contenente le informazioni dei recapiti.
   * @returns Array di RecapitoVo contenente i dati dei recapiti alternativi.
   */
  getRecapitiAlternativiFromRecapiti(recapiti: RecapitoVo[]): RecapitoVo[] {
    // Verifico se esistono i recapiti
    if (!recapiti) {
      return;
    }

    // Filtro gli oggetti dei recapiti e estraggo solo gli alternativi
    return recapiti.filter((r) => {
      // Verifico il codice recapito se alternativo
      return isRecapitoAlternativo(r);
      // #
    });
  }

  /**
   * Funzione che genera le informazioni del recapito principale, partendo dai dati delle form.
   * @param recapito RiscaRecapito che definisce i dati dalla form.
   * @param contatti RiscaContatto che definisce i dati dalla form.
   * @returns RecapitoVo per il recapito principale.
   */
  generaRecapitoVoPrincipale(
    recapito: RiscaRecapito,
    contatti: RiscaContatto
  ): RecapitoVo {
    // Verifico l'input
    if (!recapito || !contatti) {
      return;
    }

    // Per il recapito definisco che è il principale
    recapito.tipoRecapito = this.tipoRecapitoPrincipale;
    // Richiamo la funzione di parsing per la gestione degli oggetti dei recapiti
    const recapitoP: RecapitoVo = this.convertRiscaRecapitoToRecapitoVo(
      recapito,
      contatti
    );

    // Ritorno l'oggetto
    return recapitoP;
  }

  /**
   * Funzione di gestione per la composizione dei recapiti di un soggetto.
   * @param recapito RiscaRecapito con le informazioni per: recapito; da salvare.
   * @param contatti RiscaContatto con le informazioni per: contatti; da salvare.
   * @param datiAnagrafici SoggettoVo contenente l'origine dei dati per l'aggiornamento dei dati del recapito principale.
   * @returns Array di RecapitoVo contenente tutti i recapiti del soggetto.
   */
  gestisciRecapiti(
    recapito: RiscaRecapito,
    contatti: RiscaContatto,
    recapitiA: RecapitoVo[],
    datiAnagrafici?: SoggettoVo
  ): RecapitoVo[] {
    // Verifico l'input
    if (!recapito || !contatti) {
      return [];
    }

    // Genero l'oggetto del recapito principale
    let recapitoP = this.generaRecapitoVoPrincipale(recapito, contatti);

    // Verifico se esistono dei dati anagrafici
    if (datiAnagrafici) {
      // Verifico se esiste un recapito principale
      const recapitoPDA = datiAnagrafici.recapiti?.find((r: RecapitoVo) => {
        // Verifico il codice recapito
        return isRecapitoPrincipale(r);
        // #
      });
      // Se esiste effettuo un merge delle informazioni
      if (recapitoPDA) {
        // Aggiorno il recapito principale
        recapitoP = { ...recapitoPDA, ...recapitoP };
      }
    }

    // Ritorno la composizione dei recapiti
    return this.componiRecapiti(recapitoP, recapitiA);
  }

  /**
   * Funzione che gestisce i recapiti del soggetto.
   * La funzione unirà il recapito principale con i possibili recapiti alternativi.
   * @param recapitoP RecapitoVo con i dati del recapito principale.
   * @param recapitiA Array di RecapitoVo con i dati dei recapiti alternativi.
   * @returns Array di RecapitoVo contenente tutti i recapiti del soggetto.
   */
  componiRecapiti(
    recapitoP: RecapitoVo,
    recapitiA: RecapitoVo[]
  ): RecapitoVo[] {
    // Verifico l'input
    if (!recapitoP) {
      return [];
    }

    // Definisco un contenitore con il recapito principale
    let recapiti = [recapitoP];
    // Concateno gli array
    return recapiti.concat(recapitiA);
  }

  /**
   * Funzione di comodo che estra i recapiti alternativi da un array di recapiti.
   * @param recapiti Array di RecapitoVo contenente i recapiti da filtrare.
   */
  recapitiAlternativiFromRecapiti(recapiti: RecapitoVo[]): RecapitoVo[] {
    // Verifico l'input
    if (!recapiti) {
      // Ritorno array vuoto
      return [];
    }

    // Applico un filter sui recapiti ed estraggo gli alternativi
    return recapiti?.filter((r: RecapitoVo) => {
      // Verifico se è un recapito alternativo
      return isRecapitoAlternativo(r);
      // #
    });
  }

  /**
   * #######################################################
   * FUNZIONALITA' DI GESTIONE DATI DEI RECAPITI ALTERNATIVI
   * #######################################################
   */

  /**
   * Funzione di convert che tramite i dati generati dal form risca-recapito e risca-contatti genera un oggetto RecapitoVo.
   * @param c DatiRecapitoFormConfigs contenente i dati di configurazioni. Se mancano parametri obbligatori, il risultato della funzione sarà: undefined.
   * @returns RecapitoVo con l'oggetto generato o undefined in caso manchino le configurazioni/dati.
   */
  generateRecapitoVoFromDatiRecapitoForm(
    c: DatiRecapitoFormConfigs
  ): RecapitoVo {
    // Verifico esista la configurazione
    if (!c) {
      return;
    }

    // Verifico la mapData
    if (!c.mapData || !c.keyRecapito || !c.keyContatti) {
      return;
    }
    // Recupero i dati tramite chiavi
    const recapitoAlt: RiscaRecapito = c.mapData.get(c.keyRecapito);
    const contattiAlt: RiscaContatto = c.mapData.get(c.keyContatti);

    // Per il recapito definisco che è un alternativo
    recapitoAlt.tipoRecapito = c.tipoRecapito;

    // Richiamo la funzione di parsing per la gestione degli oggetti dei recapiti
    const daRA = this.convertRiscaRecapitoToRecapitoVo(
      recapitoAlt,
      contattiAlt
    );

    // Creo un id di supporto per la gestione del dato
    daRA.__id = this._riscaUtilities.generateRandomId();
    // Recupero l'oggetto del recapito
    const recapitoReady: RecapitoVo = daRA;

    // Ritorno il recapito formattato
    return recapitoReady;
  }

  /**
   * Funzione che apre un modale e visualizza i dati di un recapito.
   * L'utente potrà decidere di modificare i dati del modale e premendo salva verrà lanciata la callback passata come parametro.
   * @param datiAnagrafici SoggettoVo contenenti i dati anagrafici dell'utente.
   * @param ra RecapitoVo contenente i dati del recapito alternativo d'aggiornare.
   * @param onConfirmModal (r: RecapitoVo) => void che definisce le logiche a seguito del salvataggio delle modifiche del recapito alternativo.
   */
  updateRecapitoAlternativoModal(raM: IRecapitoAlternativoModal) {
    // Definisco localmente la funzione passata per parametro
    const onCompleteUpdate = raM.onConfirmModal;

    // Definisco la funzione di aggiornamento dato
    const onConfirm = (raU: RecapitoVo) => {
      // Definisco che il raMdoficato è alternativo
      raU.tipo_recapito = this.tipoRecapitoAlternativo;
      // Essendo un aggiornamento, mergio le proprietà con l'oggetto originale
      const nuovoRA = { ...raM.ra, ...raU };

      // Lancio, se definita, la funzione di onConfirmModal
      if (onCompleteUpdate) {
        onCompleteUpdate(nuovoRA);
      }
    };

    const { component } = raM;

    // Richiamo l'apertura del modale passando le configurazioni
    this._riscaModal.apriModal(
      new ApriModalConfigs({
        component,
        options: { windowClass: 'r-mdl-modifica-contatto-alternativo' },
        callbacks: { onConfirm },
        params: {
          recapito: raM.ra,
          modalita: AppActions.modifica,
          tipoSoggetto: raM.datiAnagrafici?.tipo_soggetto,
        },
      })
    );
  }

  /**
   * ################################
   * FUNZIONE DI COMPARE TRA RECAPITI
   * ################################
   */

  /**
   * Funzione di supporto per il compare tra recapiti.
   * Verranno effettuate operazioni di sanitifizzazione sugli oggetti per renderli formalmente coerenti.
   * @param r1 RecapitoVo da comparare.
   * @param r2 RecapitoVo da comparare.
   * @returns boolean con il risultato del compare.
   */
  sanitizeAndCompareRecapiti(r1: RecapitoVo, r2: RecapitoVo): boolean {
    // Variabili di comodo
    let R1 = clone(r1);
    let R2 = clone(r2);
    // Definisco le regole d'applicare sugli oggetti
    const rules = new ParseValueRules();
    // Effettuo la sanitificazione degli oggetti
    R1 = this._riscaUtilities.valueRegulated(R1, rules);
    R2 = this._riscaUtilities.valueRegulated(R2, rules);
    // Loggo i due oggetti per debug
    this._logger.debug('R1', R1);
    this._logger.debug('R2', R2);

    // Verifico se i due recapiti sono gli stessi per stesse informazioni
    const sameRA = this.compareRecapiti(R1, R2);
    // Ritorno il risultato
    return sameRA;
  }

  /**
   * Funzione che verifica se un recapito ha le stesse informazioni di un altro recapito.
   * @param r1 RecapitoVo da verificare.
   * @param r2 RecapitoVo da verificare.
   * @returns boolean che definisce se i recapiti in input hanno le stesse informazioni.
   */
  compareRecapiti(r1: RecapitoVo, r2: RecapitoVo): boolean {
    // Richiamo la funzione di same del servizio di utility
    return this._riscaCompare.sameRecapiti(r1, r2);
  }

  /**
   * ##############################################
   * GETTER DI COMODO PER LE VARIABILI DEL SERVIZIO
   * ##############################################
   */

  /**
   * Getter per _tipoRecapitoPrincipale.
   */
  get tipoRecapitoPrincipale(): TipoRecapitoVo {
    // Ritorno l'oggetto
    return this._tipoRecapitoPrincipale;
  }

  /**
   * Getter per _tipoRecapitoAlternativo.
   */
  get tipoRecapitoAlternativo(): TipoRecapitoVo {
    // Ritorno l'oggetto
    return this._tipoRecapitoAlternativo;
  }
}
