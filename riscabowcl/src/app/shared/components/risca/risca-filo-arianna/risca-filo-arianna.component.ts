import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { flatten, uniq } from 'lodash';
import { Subscription } from 'rxjs/index';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { RiscaFiloAriannaService } from '../../../services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { FASegmento } from './utilities/rfa-blocco.class';
import { FALivello } from './utilities/rfa-level.class';

@Component({
  selector: 'risca-filo-arianna',
  templateUrl: './risca-filo-arianna.component.html',
  styleUrls: ['./risca-filo-arianna.component.scss'],
})
export class RiscaFiloAriannaComponent implements OnInit, OnDestroy {
  /** Oggetto di costanti contenente le informazioni comuni all'applicazione. */
  C_C = new CommonConsts();

  /** Input String che definisce una classe di stile (compatibile con NgClass) o un oggetto any che definisce proprietà css (compatibile con NgStyle): per il conteiner. */
  @Input() cssContainer: string | any;

  /** Subscription che permette di collegarsi all'evento di cambio per il filo d'arianna. */
  private filoAriannaChange$: Subscription;

  /** FASegmento[] che definisce gli step presenti all'interno della navigazione filo arianna. */
  filoArianna: FASegmento[];

  /**
   * Costruttore.
   */
  constructor(private _riscaFA: RiscaFiloAriannaService) {
    // Lancio il setup del componente
    this.setupComponente();
  }

  /**
   * OnInit.
   */
  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * OnDestroy.
   */
  ngOnDestroy() {
    // Tento di fare l'unsubscribe degli events
    try {
      // Verifico che esista l'event emitter
      if (this.filoAriannaChange$) {
        this.filoAriannaChange$.unsubscribe();
      }
    } catch (e) {}
  }

  /**
   * #################
   * FUNZIONI DI SETUP
   * #################
   */

  /**
   * Funzione di setup del componente.
   */
  private setupComponente() {
    // Lancio la funzione di setup dei listener
    this.setupListeners();
    // Lancio la funzione di setup per l'array filo arianna
    this.setupFiloArianna();
  }

  /**
   * Funzione di setup che permette al componente di agganciarsi agli eventi asincroni.
   */
  private setupListeners() {
    // Mi aggancio all'evento di cambio dei livelli del filo d'arianna
    this.filoAriannaChange$ = this._riscaFA.onFiloAriannaChange$.subscribe({
      next: (fa: FASegmento[]) => {
        // Richiamo la funzione di gestione del cambio del filo d'arianna
        this.onFiloAriannaChange(fa);
      },
    });
  }

  /**
   * Funzione di setup per il set iniziale dei dati per l'array filoArianna.
   */
  private setupFiloArianna() {
    // Recupero dal servizio l'array filoArianna
    this.filoArianna = this._riscaFA.filoArianna;
  }

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  /**
   * #######################
   * FUNZIONI DEL COMPONENTE
   * #######################
   */

  /**
   * Funzione invocata al cambio d'informazioni relative all'array filoArianna.
   * @param filoArianna FASegmento[] che definisce la nuova struttura del filoArianna.
   */
  private onFiloAriannaChange(filoArianna: FASegmento[]) {
    // Aggiorno la variabile locale
    this.filoArianna = filoArianna;
  }

  /**
   * ###############
   * GETTER & SETTER
   * ###############
   */

  /**
   * Getter di comodo che combina le informazioni dei blocchi e dei livelli del filo d'arianna.
   * @returns FALivello[] con la lista di tutti gli elementi del filo d'arianna.
   */
  get livelliFA(): FALivello[] {
    // Estrapolo dalla configurazione tutti i livelli per il filo
    let livelliSegmenti: FALivello[][];
    livelliSegmenti = this.filoArianna?.map((s: FASegmento) => {
      // Ritorno solo i livelli del filo
      return s.livelli;
    });
    livelliSegmenti = livelliSegmenti ?? [];

    // Ottenuto i livelli effettuo un flatten per ottenere solo una lista di array
    let livelliFA: FALivello[];
    livelliFA = uniq(flatten(livelliSegmenti));

    // Ritorno la lista di livelli del filo d'arianna
    return livelliFA;
  }

  /**
   * Getter di comodo per il check di esistenza dati per filo d'arianna.
   * @returns boolean con il risultato del check.
   */
  get checkFA(): boolean {
    // Verifico che esista l'array filo arianna e abbia almeno un elemento
    return this.filoArianna?.length > 0;
  }
}
