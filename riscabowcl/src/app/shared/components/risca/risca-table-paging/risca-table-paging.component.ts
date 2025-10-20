import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';
import { RiscaTablePagination } from './../../../utilities/classes/utilities.classes';
import { RTablePagingConsts } from './utilities/risca-table-paging.consts';

@Component({
  selector: 'risca-table-paging',
  templateUrl: './risca-table-paging.component.html',
  styleUrls: ['./risca-table-paging.component.scss'],
})
export class RiscaTablePagingComponent implements OnInit, OnChanges {
  /** RTablePagingConsts contenente le costanti per il componente */
  RTP_C = RTablePagingConsts;

  /**
   * Impostazione immagini pulsanti
   */
  /** string che identifica l'src del pulsante */
  start = 'assets/icon-table-nav-start.svg';
  /** string che identifica l'src del pulsante */
  backward = 'assets/icon-table-nav-backward.svg';
  /** string che identifica l'src del pulsante */
  end = 'assets/icon-table-nav-end.svg';
  /** string che identifica l'src del pulsante */
  forward = 'assets/icon-table-nav-forward.svg';

  /** RiscaTablePagination con la configurazione per la creazione del paginatore. Se non fornite, la paginazione non viene fatta. */
  @Input() paginazione: RiscaTablePagination;
  /** Boolean che gestisce lo spostamento degli elementi sulla destra della pagina. Per default è: true. */
  @Input() right: boolean = true;
  /** Boolean che gestisce la visualizzazione del totale degli elementi del paginatore. */
  @Input() mostraTotale: boolean = false;
  /** Boolean che gestisce la visualizzazione della input con possibilità d'inserire il numero di pagina da visualizzare. */
  @Input() mostraVaiA: boolean = false;

  /** Evento che comunica che è avvenuto un cambio pagina. Il numero della nuova pagina sarà emesso. Le pagine sono enumerate a partire da 1. */
  @Output() onCambioPagina = new EventEmitter<RiscaTablePagination>();

  /** number[] che definisce il numero totale di pagine. */
  pagine: number[] = [];
  /** number che definisce il modello dati per l'input "Vai a". */
  vaiA: number;

  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * ngOnInit.
   */
  ngOnInit(): void {}

  /**
   * ngOnInit.
   */
  ngOnChanges(changes: SimpleChanges): void {
    // Richiamo la funzione di update dei dati del paginatore
    this.update();
  }

  /**
   * Aggiorna la paginazione se cambia la ricerca
   */
  private update() {
    // Verifico se la paginazione da configurazione è visibile
    if (this.paginazioneVisibile) {
      // Calcolo il numero di pagine, arrotondando per eccesso
      if (!this.overflowPagine) {
        // Creo le pagine
        this.pagine = [...Array(this.totalePagineRicerca).keys()];
        // Le enumero correttamente partendo da 1
        this.pagine.forEach((i) => (this.pagine[i] = i + 1));
        // #
      } else {
        let distanzaSX = 0;
        let distanzaDX = 0;

        const metaSxPresunta = Math.floor((this.paginazione.maxPages - 1) / 2);
        const metaDxPresunta = this.paginazione.maxPages - 1 - metaSxPresunta;

        if (this.paginaCorrente <= metaSxPresunta) {
          distanzaSX = this.paginaCorrente - 1;
          distanzaDX = this.paginazione.maxPages - distanzaSX - 1;
        } else if (
          this.paginaCorrente >=
          this.totalePagineRicerca - metaDxPresunta
        ) {
          distanzaDX = this.totalePagineRicerca - this.paginaCorrente;
          distanzaSX = this.paginazione.maxPages - distanzaDX - 1;
        } else {
          distanzaDX = metaDxPresunta;
          distanzaSX = metaSxPresunta;
        }

        const pagineSX = [...Array(distanzaSX).keys()];
        const indiceMinimo = this.paginaCorrente - distanzaSX;
        pagineSX.forEach((i) => (pagineSX[i] = i + indiceMinimo));

        const pagineDX = [...Array(distanzaDX).keys()];
        pagineDX.forEach((i) => (pagineDX[i] = i + this.paginaCorrente + 1));

        this.pagine = [...pagineSX, this.paginaCorrente, ...pagineDX];
      }
    }
  }

  /**
   * #############
   * CAMBIO PAGINA
   * #############
   */

  /**
   * Istruzione per andare avanti o indietro sulle pagine
   * @param pagina pagina su cui andare
   */
  vaiAPagina(pagina: number) {
    // Verifico di essere negli estremi delle pagine
    const moreOrEqThanMin = pagina >= 1;
    const lessOrEqThanMax = pagina <= this.totalePagineRicerca;
    const inRange = moreOrEqThanMin && lessOrEqThanMax;
    // Verifico di non essere già sullo stesso numero di pagina
    const anotherPage = this.paginaCorrente != pagina;

    // Verifico le condizioni minime per spostarmi su una nuova pagina
    if (inRange && anotherPage) {
      // Creo una copia dell'oggetto della paginazione da emettere
      const paginazione = new RiscaTablePagination(this.paginazione);
      // Aggiorno la paginazione
      paginazione.currentPage = pagina;
      // Emetto l'oggetto della paginazione
      this.onCambioPagina.emit(paginazione);
    }
  }

  /**
   * ####################
   * VERIFICA INPUT VAI A
   * ####################
   */

  /**
   * Funzione di verifica sul valore dell'input: Vai a.
   * La funzione correggerà valori che si trovano al di fuori del range: 1 ==> totale pagine.
   */
  checkVaiA() {
    // Recupero dal componente il valore dell'input
    const vaiA = this.vaiA;
    // Definisco i valori per il range delle pagine
    const min = 1;
    const max = this.paginazione?.total ?? 1;

    // Verifico se il valore è minimo del minimo
    if (vaiA < min) {
      // Imposto il valore al minimo
      this.vaiA = min;
      // #
    } else if (vaiA > max) {
      // Imposto il valore al massimo
      this.vaiA = max;
      // #
    }
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che definisce se le parti del paginatore sono da visualizzare.
   * @returns boolean che definisce la visibilità del paginatore.
   */
  get paginazioneVisibile(): boolean {
    // Verifico l'esistenza della configurazione e che abbia effettivamente elementi per le pagine
    const existElem4Page = this.paginazione?.elementsForPage > 0;
    // Verifico se il totale degli elementi è maggiore degli elementi per pagina
    const totalMoreThanElem4Page =
      this.paginazione?.total > this.paginazione?.elementsForPage;

    // Ritorno l'insieme dei check
    return existElem4Page && totalMoreThanElem4Page;
  }

  /**
   * Getter per il recupero della pagina corrente.
   * @return number con il numero attualmente attivo della paginazione.
   */
  get paginaCorrente() {
    // Recupero la proprietà per la pagina corrente
    return this.paginazione?.currentPage;
  }

  /**
   * Getter per il controllo sulla pagina attualmente selezionata è anche la prima pagina disponibile.
   * @returns boolean con il risultato del controllo.
   */
  get isPrimaPagina(): boolean {
    // Verifico se la pagina corrente è la prima pagina
    return this.paginaCorrente === 1;
  }

  /**
   * Getter per il controllo sulla pagina attualmente selezionata è anche l'ultima pagina disponibile.
   * @returns boolean con il risultato del controllo.
   */
  get isUltimaPagina(): boolean {
    // Verifico se la pagina corrente è la prima pagina
    return this.paginaCorrente === this.totalePagineRicerca;
  }

  /**
   * Getter per il calcolo di numero delle pagine per l'attuale ricerca.
   * @returns number con il numero di pagine per l'attuale ricerca.
   */
  get totalePagineRicerca(): number {
    // Effettuo il calcolo del numero di pagine sulla base del totale degli elementi diviso gli elementi per pagina
    const totalPages =
      this.paginazione?.total / this.paginazione?.elementsForPage;
    // Ritorno il valore arrotondato per eccesso
    return Math.ceil(totalPages);
  }

  /**
   * Getter che verifica se il numero di pagine è superiore al numero di pagine massime da mostrare.
   * @returns boolean che definisce se le pagine calcolate per gli elementi supera il numero massimo di pagine da visualizzare.
   */
  get overflowPagine(): boolean {
    // Effettuo la verifica
    return this.totalePagineRicerca > this.paginazione?.maxPages;
  }

  /**
   * Getter che verifica se a destra della pagina corrente ci sono più pagine di quante ne vengano mostrate, come eccesso verso il totale di pagine generate dalla ricerca.
   * Il getter è utilizzato per visualizzare i ... a destra del paginatore.
   * @returns boolean con il risultato del controllo.
   */
  get showDotsRight(): boolean {
    // Verifico se non c'è un overflow di pagine
    if (!this.overflowPagine) {
      // Niente overflow, non devo mostrare i puntini a destra
      return false;
    }

    // Recupero la pagina corrente
    const currentPage = this.paginaCorrente;
    // Definisco la metà delle pagine visibili definite dalla configurazione (-1)
    const halfMaxPages = Math.floor((this.paginazione.maxPages - 1) / 2);
    // Recupero il numero massimo di pagine di ricerca
    const maxPages = this.totalePagineRicerca;

    // Sommo il numero della pagina corrente con la metà del numero di pagine visualizzabili
    const rangeCheck = currentPage + halfMaxPages;
    // Il check per i puntini è deciso dal fatto che se il range calcolato è minore del numero massimo di pagine della ricerca
    const check = rangeCheck < maxPages;

    // Ritorno il check
    return check;
  }

  /**
   * Getter che verifica se a sinistra della pagina corrente ci sono più pagine di quante ne vengano mostrate, come eccesso verso il minimo di pagine presenti.
   * Il getter è utilizzato per visualizzare i ... a sinistra del paginatore.
   * @returns boolean con il risultato del controllo.
   */
  get showDotsLeft(): boolean {
    // Verifico se non c'è un overflow di pagine
    if (!this.overflowPagine) {
      // Niente overflow, non devo mostrare i puntini a sinistra
      return false;
    }

    // Recupero la pagina corrente
    const currentPage = this.paginaCorrente;
    // Definisco la metà delle pagine visibili definite dalla configurazione (-1)
    const halfMaxPages = Math.floor((this.paginazione.maxPages - 1) / 2);
    // Definisco il numero minimo di pagine di ricerca
    const minPages = 1;

    // Sottraggo al numero della pagina corrente il valore della metà del numero di pagine visualizzabili
    const rangeCheck = currentPage - halfMaxPages;
    // Il check per i puntini è deciso dal fatto che se il range calcolato è maggiore del numero minimo di pagine della ricerca
    const check = rangeCheck > minPages;

    // Ritorno il check
    return check;
  }

  /**
   * Getter che verifica se il valore di cambio pagina è un valore valido.
   * @returns boolean con il risultato del check.
   */
  get checkVaiAInRange(): boolean {
    // Verifico il valore vai a
    if (this.vaiA == undefined) {
      // Ritorno false
      return false;
    }

    // Recupero dal componente il valore dell'input
    const vaiA = this.vaiA;
    // Definisco i valori per il range delle pagine
    const min = 1;
    const max = this.paginazione?.total ?? 1;
    // Definisco i flag di controllo
    const moreThenMin = vaiA >= min;
    const lessThenMax = vaiA <= max;
    const inRange = moreThenMin && lessThenMax;

    // Verifico se il valore è minimo del minimo
    return inRange;
  }
}
