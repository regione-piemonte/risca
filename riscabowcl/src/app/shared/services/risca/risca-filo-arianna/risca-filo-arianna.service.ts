import { Injectable } from '@angular/core';
import { clone, compact } from 'lodash';
import { Subject } from 'rxjs';
import { FASegmento } from '../../../components/risca/risca-filo-arianna/utilities/rfa-blocco.class';
import { FALivello } from '../../../components/risca/risca-filo-arianna/utilities/rfa-level.class';
import { IFASegmento } from '../../../components/risca/risca-filo-arianna/utilities/rfa-level.interfaces';
import { RiscaUtilitiesService } from '../risca-utilities/risca-utilities.service';
import { RFALivelli } from './utilities/rfa-levels.classes';

/**
 * Servizio dedicato alla gestione del filo d'arianna applicativo.
 */
@Injectable({ providedIn: 'root' })
export class RiscaFiloAriannaService {
  /** Subject che permette di registrarsi all'evento di avvenuta modifica sugli livello di filoArianna. */
  onFiloAriannaChange$ = new Subject<FASegmento[]>();

  /** RFALivelli con le configurazioni degli oggetti per i livelli. */
  private rfaLivelli: RFALivelli;

  /**
   * Costruttore.
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {
    // Lancio il setup per gli oggetti di configurazioni dei livelli del filo d'arianna
    this.setupFALivelli();
  }

  /**
   * ###########################
   * FUNZIONI E GESTIONE JOURNEY
   * ###########################
   */

  /** FASegmento[] contenente la struttura che gestisce i vari livelli del filo d'arianna. */
  private _filoArianna: FASegmento[] = [];

  /**
   * Getter per _filoArianna.
   * @returns FASegmento[] con la struttura del filo d'arianna.
   */
  get filoArianna(): FASegmento[] {
    // Ritorno l'array creando una copia, non passando il riferimento
    return clone(this._filoArianna);
  }

  /**
   * Funzione di supporto che emette l'evento di cambio del filo d'arianna.
   */
  emitFiloArianna(filoArianna?: FASegmento[]) {
    // Verifico l'input o recupero dal servizio l'oggetto
    const fa = filoArianna ?? this.filoArianna;
    // Emetto l'evento che propaga il dato del filo d'arianna nell'applicazione.
    this.onFiloAriannaChange$.next(fa);
  }

  /**
   * Funzione di reset per filoArianna
   */
  resetFiloArianna() {
    // Imposto ad array vuoto filoArianna
    this._filoArianna = [];
    // Emetto l'evento di cambio di filoArianna
    this.emitFiloArianna(this._filoArianna);
  }

  /**
   * Funzione di supporto che verifica se all'interno di filoArianna è già presente un segmento.
   * @param segmento FASegmento che definisce l'oggetto da ricercare.
   * @returns boolean con l'indicazione di presenza (true) o di assenza (false) del segmento in filoArianna.
   */
  isSegmentoInFiloArianna(segmento: FASegmento): boolean {
    // Verifico l'input
    if (segmento == undefined) {
      // Ritorno il default
      return false;
    }
    // Ricerco all'interno dell'array l'oggetto
    return this.indexSegmentoInFiloArianna(segmento.id) !== -1;
  }

  /**
   * Funzione di supporto che verifica se all'interno di filoArianna è già presente uno livello per id.
   * @param id string che definisce l'id da ricercare.
   * @returns boolean con l'indicazione di presenza (true) o di assenza (false) del segmento in filoArianna.
   */
  isSegmentoInFiloAriannaById(id: string): boolean {
    // Verifico l'input
    if (id == undefined) {
      // Ritorno il default
      return false;
    }
    // Ricerco all'interno dell'array l'oggetto
    return this.indexSegmentoInFiloArianna(id) !== -1;
  }

  /**
   * Funzione di supporto che verifica se all'interno di filoArianna è già presente un segmento per id.
   * Verrà tornato l'oggetto nell'array, altrimenti undefined.
   * @param id string che definisce l'id da ricercare.
   * @returns FASegmento con l'oggetto all'interno dell'array, oppure undefined.
   */
  segmentoInFiloAriannaById(id: string): FASegmento {
    // Verifico l'input
    if (id == undefined) {
      // Ritorno il default
      return undefined;
    }

    // Tento di recuperare da filoArianna lo segmento
    return this._filoArianna.find((l: FASegmento) => {
      // Ritorno il check sugli id
      return l.id === id;
    });
  }

  /**
   * Funzione di supporto che ritorna un segmento all'interno del filo d'arianna, cercando all'interno dei livelli quello richiesto in input.
   * @param livello string che definisce il livello da ricercare.
   * @returns FASegmento con l'oggetto all'interno dell'array, oppure undefined.
   */
  segmentoInFAByLivello(livello: FALivello): FASegmento {
    // Verifico l'input
    if (livello == undefined) {
      // Ritorno il default
      return undefined;
    }

    // Tento di recuperare da filoArianna lo segmento
    const iSegmento = this.indexSegmentoInFAByIdLivello(livello.id);
    // Verifico se è stato trovato un indice
    if (iSegmento != -1) {
      // E' stato trovato il segmento
      return this._filoArianna[iSegmento];
      // #
    }

    // Non esiste il segmento, ritorno undefined
    return undefined;
  }

  /**
   * Funzione di supporto che verifica se all'interno di filoArianna è già presente un segmento per id.
   * Verrà tornato l'indice posizionale nell'array, altrimenti -1.
   * @param id string che definisce l'id da ricercare.
   * @returns number con l'indice posizionale all'interno dell'array.
   */
  indexSegmentoInFiloArianna(id: string): number {
    // Verifico l'input
    if (id == undefined) {
      // Ritorno il default
      return -1;
    }

    // Tento di recuperare da filoArianna lo segmento
    return this._filoArianna.findIndex((l: FASegmento) => {
      // Ritorno il check sugli id
      return l.id === id;
    });
  }

  /**
   * Funzione di supporto che verifica se all'interno dei segmenti dentro il filo d'arianna è presente uno specifico livello.
   * @param livello FALivello che definisce l'oggetto da ricercare.
   * @returns boolean con l'indicazione di presenza (true) o di assenza (false) dello livello in filoArianna.
   */
  isLivelloInFiloArianna(livello: FALivello): boolean {
    // Verifico l'input
    if (livello == undefined) {
      // Ritorno il default
      return false;
    }
    // Ricerco all'interno dell'array l'oggetto
    return this.indexSegmentoInFAByIdLivello(livello.id) !== -1;
  }

  /**
   * Funzione di supporto che verifica se all'interno dei segmenti dentro il filo d'arianna è presente uno specifico livello.
   * @param id string che definisce l'id da ricercare.
   * @returns boolean con l'indicazione di presenza (true) o di assenza (false) dello livello in filoArianna.
   */
  isLivelloInFiloAriannaById(id: string): boolean {
    // Verifico l'input
    if (id == undefined) {
      // Ritorno il default
      return false;
    }
    // Ricerco all'interno dell'array l'oggetto
    return this.indexSegmentoInFAByIdLivello(id) !== -1;
  }

  /**
   * Funzione di supporto che verifica se all'interno di filoArianna è già presente un livello dentro un segmento, dato il suo id.
   * Verrà tornato l'indice posizionale nell'array, altrimenti -1.
   * @param id string che definisce l'id da ricercare.
   * @returns number con l'indice posizionale all'interno dell'array.
   */
  indexSegmentoInFAByIdLivello(id: string): number {
    // Verifico l'input
    if (id == undefined) {
      // Ritorno il default
      return -1;
    }

    // Definisco la variabile per la posizione
    let iSegmento = -1;
    // Ciclo tutti i segmenti per poter accedere ai livelli
    for (let i = 0; i < this._filoArianna.length; i++) {
      // Estraggo il segmento dall'array
      const s: FASegmento = this._filoArianna[i];
      // Effettuo una find e cerco se all'interno dei livelli del segmento è presente l'id cercato
      const searchLvl: FALivello = s.livelli.find((l: FALivello) => {
        // Effettuo una comparazione tra id dei livelli
        return l.id === id;
      });
      // Verifico se è stato trovato il livello
      if (searchLvl != undefined) {
        // Livello trovato, imposto l'indice del segmento
        iSegmento = i;
        // Blocco il ciclo
        break;
      }
    }

    // Ritorno l'indice posizionale
    return iSegmento;
  }

  /**
   * Funzione di comodo che ritorna un oggetto segmento dato il suo indice posizionale.
   * @param index number con l'indice posizionale del segmento.
   * @returns FASegmento con il segmento trovato. Altrimenti undefined.
   */
  segmentoByIndex(index: number): FASegmento {
    // Verifico l'input
    if (index == undefined || index < 0) {
      // Ritorno il default
      return undefined;
    }

    // Ritorno l'oggetto per indice
    return this.filoArianna[index];
  }

  /**
   * Funzione che crea un segmento composto da indefiniti livelli.
   * Se mancano i livelli, verrà ritornato undefined.
   * @param livelli ...FALivello che definisce le configurazioni per l'aggiunta di indefiniti livelli al filoArianna.
   * @returns FASegmento con l'oggetto generato.
   */
  creaSegmentoByLivelli(...livelli: FALivello[]): FASegmento {
    // Verifico l'input
    if (!livelli) {
      // Blocco il flusso
      return;
    }

    // Creo un nuovo oggetto segmento, con informazioni minime
    return new FASegmento({ livelli });
  }

  /**
   * Funzione che aggiunge a filoArianna un nuovo segmento.
   * @param segmento FASegmento che definisce le configurazioni per l'aggiunta di un segmento al filoArianna.
   * @param emitOnAdd boolean che permette di gestire l'emissione dell'evento di cambio filo arianna. Per default è: true.
   */
  aggiungiSegmento(segmento: FASegmento, emitOnAdd = true) {
    // Verifico l'input
    if (!segmento) {
      // Blocco il flusso
      return;
    }

    // Definisco una variabile che conterrà il segmento dell'oggetto, calcolato rispetto all'ultimo elemento presente nella lista
    let ordineSeg: number = 0;
    // Verifico se esistono oggetti all'interno del filo d'arianna
    if (this._filoArianna.length > 0) {
      // Esistono livelli, recupero l'ultimo oggetto
      const lastSegmento = this.getLastSegmento();
      // Recupero il livello dell'ultimo segmento e lo incremento di 1
      ordineSeg = lastSegmento.segmento + 1;
    }

    // Definisco il livello indicativo dell'oggetto da inserire nel filo d'arianna
    segmento.segmento = ordineSeg;
    segmento.id = segmento.id ?? ordineSeg.toString();
    // Aggiungo al filo d'arianna il nuovo livello
    this._filoArianna.push(segmento);

    // Verifico la gestione di emsissione evento
    if (emitOnAdd) {
      // Emetto l'evento di aggiornamento del filo
      this.emitFiloArianna(this.filoArianna);
    }
  }

  /**
   * Funzione che aggiunge un segmento al filo d'arianna, composto da indefiniti livelli.
   * @param livelli ...FALivello che definisce le configurazioni per l'aggiunta di indefiniti livelli al filoArianna.
   */
  aggiungiSegmentoByLivelli(...livelli: FALivello[]) {
    // Verifico l'input
    if (!livelli) {
      // Blocco il flusso
      return;
    }

    // Rimuovo possibili valori null
    const faLivelli: FALivello[] = compact(livelli);
    // Creo un nuovo oggetto segmento, con informazioni minime
    const segmento: FASegmento = this.creaSegmentoByLivelli(...faLivelli);
    // Aggiungo il segmento al filo d'arianna
    this.aggiungiSegmento(segmento);
  }

  /**
   * Funzione che rimuove dal filo di arianna un segmento specifico.
   * @param livello FASegmento che definisce le configurazioni per la rimozione dal filo d'Arianna.
   */
  rimuoviSegmento(segmento: FASegmento) {
    // Verifico l'input
    if (!segmento) {
      // Blocco il flusso
      return;
    }

    // Ricerco all'interno dell'array l'oggetto
    const idLiv = this.indexSegmentoInFiloArianna(segmento.id);
    // Verifico se esiste l'elemento nella lista
    if (idLiv != -1) {
      // Calcolo il numero di elementi da rimuovere fino alla fine dell'array
      const tillEnd = this._filoArianna.length - idLiv;
      // Rimuovo dalla lista da quell'elemento in poi tutti i livelli successivi
      this._filoArianna.splice(idLiv, tillEnd);
      // Emetto l'evento di aggiornamento del filo
      this.emitFiloArianna();
    }
  }

  /**
   * Funzione che rimuove dal filo di arianna l'ultimo segmento presente nella lista.
   */
  rimuoviUltimoSegmento() {
    // Recupero l'ultimo elemento della lista
    const lastS: FASegmento = this.getLastSegmento();
    // Richiamo la funzione di rimozione del segmento
    this.rimuoviSegmento(lastS);
  }

  /**
   * Funzione che recupera i dati di un segmento salvato in filoArianna.
   * @param id string che definisce l'id da recuperare.
   * @returns FASegmento con i dati del livello salvato in filoArianna.
   */
  getSegmento(id: string): FASegmento {
    // Recupero lo livello
    const iStep = this.indexSegmentoInFiloArianna(id);
    // Verifico che l'elemento esista nel filoArianna
    if (iStep === -1) {
      // Niente da ritornare
      return;
    }
    // Step trovato, ritorno le informazioni
    return this._filoArianna[iStep];
  }

  /**
   * Funzione che permette di creare, dall'interfaccia di configurazione e di aggiungere un segmento alla navigazione filoArianna.
   * @param segmentoConfig IFASegmento con i dati del segmento da creare e aggiungere.
   */
  creaEAggiungiSegmento(segmentoConfig: IFASegmento) {
    // Verifico l'input
    if (!segmentoConfig) {
      // Blocco le logiche
      return;
    }

    // Genero l'oggetto per il segmento
    const segmento = new FASegmento(segmentoConfig);
    // Aggiungo a filoArianna uno livello
    this.aggiungiSegmento(segmento);
  }

  /**
   * Funzione che recupera l'indice dell'ultimo segmento nella lista.
   * @returns number che definisce l'indice dell'ultimo segmento, se esiste.
   */
  getLastSegmentoIndex(): number {
    // Recupero l'indice dell'ultimo elemento
    const iLastSeg = this._filoArianna.length - 1;
    // Recupero l'ultimo indice
    return iLastSeg;
  }

  /**
   * Funzione che recupera l'ultimo segmento nella lista.
   * @returns FASegmento se esiste l'ultimo segmento.
   */
  getLastSegmento(): FASegmento {
    // Recupero l'indice dell'ultimo elemento
    const iLastSeg = this.getLastSegmentoIndex();
    // Recupero l'ultimo elemento
    const lastSeg = this._filoArianna[iLastSeg];
    // Ritorno l'oggetto
    return lastSeg;
  }

  /**
   * ############################
   * CONFIGURAZIONE DEGLI OGGETTI
   * ############################
   */

  /**
   * Funzione che richiama l'istanza di tutti gli oggetti per la gestione del filo d'arianna.
   */
  setupFALivelli() {
    // Lancio l'istanza dell'oggetto per le configurazioni dei livelli
    this.rfaLivelli = new RFALivelli();
  }

  /**
   * Getter per la configurazione del livello: home.
   * @returns FALivello con le configurazioni del livello.
   */
  get home(): FALivello {
    // Ritorno l'oggetto per: home
    return this.rfaLivelli.home;
  }

  /**
   * Getter per la configurazione del livello: pratiche.
   * @returns FALivello con le configurazioni del livello.
   */
  get pratiche(): FALivello {
    // Ritorno l'oggetto per: pratiche
    return this.rfaLivelli.pratiche;
  }

  /**
   * Getter per la configurazione del livello: pagamenti.
   * @returns FALivello con le configurazioni del livello.
   */
  get pagamenti(): FALivello {
    // Ritorno l'oggetto per: pagamenti
    return this.rfaLivelli.pagamenti;
  }

  /**
   * Getter per la configurazione del livello: verifiche.
   * @returns FALivello con le configurazioni del livello.
   */
  get verifiche(): FALivello {
    // Ritorno l'oggetto per: verifiche
    return this.rfaLivelli.verifiche;
  }

  /**
   * Getter per la configurazione del livello: spedizioni.
   * @returns FALivello con le configurazioni del livello.
   */
  get spedizioni(): FALivello {
    // Ritorno l'oggetto per: spedizioni
    return this.rfaLivelli.spedizioni;
  }

  /**
   * Getter per la configurazione del livello: canone.
   * @returns FALivello con le configurazioni del livello.
   */
  get canone(): FALivello {
    // Ritorno l'oggetto per: canone
    return this.rfaLivelli.canone;
  }

  /**
   * Getter per la configurazione del livello: report.
   * @returns FALivello con le configurazioni del livello.
   */
  get report(): FALivello {
    // Ritorno l'oggetto per: report
    return this.rfaLivelli.report;
  }

  /**
   * Getter per la configurazione del livello: nuovaPratica.
   * @returns FALivello con le configurazioni del livello.
   */
  get nuovaPratica(): FALivello {
    // Ritorno l'oggetto per: nuovaPratica
    return this.rfaLivelli.nuovaPratica;
  }

  /**
   * Getter per la configurazione del livello: ricercaSemplice.
   * @returns FALivello con le configurazioni del livello.
   */
  get ricercaSemplice(): FALivello {
    // Ritorno l'oggetto per: ricercaSemplice
    return this.rfaLivelli.ricercaSemplice;
  }

  /**
   * Getter per la configurazione del livello: ricercaAvanzata.
   * @returns FALivello con le configurazioni del livello.
   */
  get ricercaAvanzata(): FALivello {
    // Ritorno l'oggetto per: ricercaAvanzata
    return this.rfaLivelli.ricercaAvanzata;
  }

  /**
   * Getter per la configurazione del livello: ricercaSoggetto.
   * @returns FALivello con le configurazioni del livello.
   */
  get ricercaSoggetto(): FALivello {
    // Ritorno l'oggetto per: ricercaSoggetto
    return this.rfaLivelli.ricercaSoggetto;
  }

  /**
   * Getter per la configurazione del livello: ricercaGruppo.
   * @returns FALivello con le configurazioni del livello.
   */
  get ricercaGruppo(): FALivello {
    // Ritorno l'oggetto per: ricercaGruppo
    return this.rfaLivelli.ricercaGruppo;
  }

  /**
   * Getter per la configurazione del livello: nuovoGruppo.
   * @returns FALivello con le configurazioni del livello.
   */
  get nuovoGruppo(): FALivello {
    // Ritorno l'oggetto per: nuovoGruppo
    return this.rfaLivelli.nuovoGruppo;
  }

  /**
   * Getter per la configurazione del livello: elencoElaborazioni.
   * @returns FALivello con le configurazioni del livello.
   */
  get elencoElaborazioni(): FALivello {
    // Ritorno l'oggetto per: elencoElaborazioni
    return this.rfaLivelli.elencoElaborazioni;
  }

  /**
   * Getter per la configurazione del livello: ricercaPagamenti.
   * @returns FALivello con le configurazioni del livello.
   */
  get ricerchePagamenti(): FALivello {
    // Ritorno l'oggetto per: ricercaPagamenti
    return this.rfaLivelli.ricerchePagamenti;
  }

  /**
   * Getter per la configurazione del livello: pagamentiDaVisionare.
   * @returns FALivello con le configurazioni del livello.
   */
  get pagamentiDaVisionare(): FALivello {
    // Ritorno l'oggetto per: pagamentiDaVisionare
    return this.rfaLivelli.pagamentiDaVisionare;
  }

  /**
   * Getter per la configurazione del livello: bollettini.
   * @returns FALivello con le configurazioni del livello.
   */
  get bollettini(): FALivello {
    // Ritorno l'oggetto per: bollettini
    return this.rfaLivelli.bollettini;
  }

  /**
   * Getter per la configurazione del livello: dettaglioGenerico.
   * @returns FALivello con le configurazioni del livello.
   */
  get dettaglioGenerico(): FALivello {
    // Ritorno l'oggetto per: dettaglioGenerico
    return this.rfaLivelli.dettaglioGenerico;
  }

  /**
   * Getter per la configurazione del livello: pratica.
   * @returns FALivello con le configurazioni del livello.
   */
  get pratica(): FALivello {
    // Ritorno l'oggetto per: pratica
    return this.rfaLivelli.pratica;
  }

  /**
   * Getter per la configurazione del livello: dettaglioSoggetto.
   * @returns FALivello con le configurazioni del livello.
   */
  get dettaglioSoggetto(): FALivello {
    // Ritorno l'oggetto per: dettaglioSoggetto
    return this.rfaLivelli.dettaglioSoggetto;
  }

  /**
   * Getter per la configurazione del livello: dettaglioGruppo.
   * @returns FALivello con le configurazioni del livello.
   */
  get dettaglioGruppo(): FALivello {
    // Ritorno l'oggetto per: dettaglioGruppo
    return this.rfaLivelli.dettaglioGruppo;
  }

  /**
   * Getter per la configurazione del livello: praticheCollegate.
   * @returns FALivello con le configurazioni del livello.
   */
  get praticheCollegate(): FALivello {
    // Ritorno l'oggetto per: praticheCollegate
    return this.rfaLivelli.praticheCollegate;
  }

  /**
   * Getter per la configurazione del livello: ricercaPagamenti.
   * @returns FALivello con le configurazioni del livello.
   */
  get ricercaPagamenti(): FALivello {
    // Ritorno l'oggetto per: ricercaPagamenti
    return this.rfaLivelli.ricercaPagamenti;
  }

  /**
   * Getter per la configurazione del livello: ricercaMorosita.
   * @returns FALivello con le configurazioni del livello.
   */
  get ricercaMorosita(): FALivello {
    // Ritorno l'oggetto per: ricercaMorosita
    return this.rfaLivelli.ricercaMorosita;
  }

  /**
   * Getter per la configurazione del livello: ricercaRimborsi.
   * @returns FALivello con le configurazioni del livello.
   */
  get ricercaRimborsi(): FALivello {
    // Ritorno l'oggetto per: ricercaRimborsi
    return this.rfaLivelli.ricercaRimborsi;
  }

  /**
   * Getter per la configurazione del livello: ricercaAvanzataPratiche.
   * @returns FALivello con le configurazioni del livello.
   */
  get ricercaAvanzataPratiche(): FALivello {
    // Ritorno l'oggetto per: ricercaAvanzataPratiche
    return this.rfaLivelli.ricercaAvanzataPratiche;
  }

  /**
   * Getter per la configurazione del livello: ricercaAvanzataStatiDebitori.
   * @returns FALivello con le configurazioni del livello.
   */
  get ricercaAvanzataStatiDebitori(): FALivello {
    // Ritorno l'oggetto per: ricercaAvanzataStatiDebitori
    return this.rfaLivelli.ricercaAvanzataStatiDebitori;
  }

  /**
   * Getter per la configurazione del livello: datiContabili.
   * @returns FALivello con le configurazioni del livello.
   */
  get datiContabili(): FALivello {
    // Ritorno l'oggetto per: datiContabili
    return this.rfaLivelli.datiContabili;
  }

  /**
   * Getter per la configurazione del livello: dettaglioPratica.
   * @returns FALivello con le configurazioni del livello.
   */
  get dettaglioPratica(): FALivello {
    // Ritorno l'oggetto per: dettaglioPratica
    return this.rfaLivelli.dettaglioPratica;
  }

  /**
   * Getter per la configurazione del livello: cercaTitolare.
   * @returns FALivello con le configurazioni del livello.
   */
  get cercaTitolare(): FALivello {
    // Ritorno l'oggetto per: cercaTitolare
    return this.rfaLivelli.cercaTitolare;
  }

  /**
   * Getter per la configurazione del livello: nuovoSoggetto.
   * @returns FALivello con le configurazioni del livello.
   */
  get nuovoSoggetto(): FALivello {
    // Ritorno l'oggetto per: nuovoSoggetto
    return this.rfaLivelli.nuovoSoggetto;
  }

  /**
   * Getter per la configurazione del livello: documentiAllegati.
   * @returns FALivello con le configurazioni del livello.
   */
  get documentiAllegati(): FALivello {
    // Ritorno l'oggetto per: documentiAllegati
    return this.rfaLivelli.documentiAllegati;
  }

  /**
   * Getter per la configurazione del livello: nuovoStatoDeb.
   * @returns FALivello con le configurazioni del livello.
   */
  get nuovoStatoDeb(): FALivello {
    // Ritorno l'oggetto per: nuovoStatoDeb
    return this.rfaLivelli.nuovoStatoDeb;
  }

  /**
   * Getter per la configurazione del livello: dettaglioStatoDeb.
   * @returns FALivello con le configurazioni del livello.
   */
  get dettaglioStatoDeb(): FALivello {
    // Ritorno l'oggetto per: dettaglioStatoDeb
    return this.rfaLivelli.dettaglioStatoDeb;
  }

  /**
   * Getter per la configurazione del livello: dettaglioAccertamenti.
   * @returns FALivello con le configurazioni del livello.
   */
  get dettaglioAccertamenti(): FALivello {
    // Ritorno l'oggetto per: dettaglioAccertamenti
    return this.rfaLivelli.dettaglioAccertamenti;
  }

  /**
   * Getter per la configurazione del livello: dettaglioPagamenti.
   * @returns FALivello con le configurazioni del livello.
   */
  get dettaglioPagamenti(): FALivello {
    // Ritorno l'oggetto per: dettaglioPagamenti
    return this.rfaLivelli.dettaglioPagamenti;
  }

  /**
   * Getter per la configurazione del livello: dettaglioMorosita.
   * @returns FALivello con le configurazioni del livello.
   */
  get dettaglioMorosita(): FALivello {
    // Ritorno l'oggetto per: dettaglioMorosita
    return this.rfaLivelli.dettaglioMorosita;
  }

  /**
   * Getter per la configurazione del livello: dettaglioRimborsi.
   * @returns FALivello con le configurazioni del livello.
   */
  get dettaglioRimborsi(): FALivello {
    // Ritorno l'oggetto per: dettaglioRimborsi
    return this.rfaLivelli.dettaglioRimborsi;
  }

  /**
   * Getter per la configurazione del livello: configurazioni.
   * @returns FALivello con le configurazioni del livello.
   */
  get configurazioni(): FALivello {
    // Ritorno l'oggetto per: configurazioni
    return this.rfaLivelli.configurazioni;
  }

  /**
   * Getter per la configurazione del livello: configurazioneCanoni.
   * @returns FALivello con le configurazioni del livello.
   */
  get configurazioneCanoni(): FALivello {
    // Ritorno l'oggetto per: configurazioneCanoni
    return this.rfaLivelli.configurazioneCanoni;
  }

  /**
   * Getter per la configurazione del livello: tassiDiInteresse.
   * @returns FALivello con le configurazioni del livello.
   */
  get tassiDiInteresse(): FALivello {
    // Ritorno l'oggetto per: tassiDiInteresse
    return this.rfaLivelli.tassiDiInteresse;
  }

  /**
   * Getter per la configurazione del livello: parametriDellaDilazione.
   * @returns FALivello con le configurazioni del livello.
   */
  get parametriDellaDilazione(): FALivello {
    // Ritorno l'oggetto per: parametriDellaDilazione
    return this.rfaLivelli.parametriDellaDilazione;
  }

  /**
   * Getter per la configurazione del livello: altriParametri.
   * @returns FALivello con le configurazioni del livello.
   */
  get altriParametri(): FALivello {
    // Ritorno l'oggetto per: altriParametri
    return this.rfaLivelli.altriParametri;
  }
}
