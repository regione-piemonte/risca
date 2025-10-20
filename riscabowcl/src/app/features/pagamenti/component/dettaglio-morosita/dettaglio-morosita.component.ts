import { Component, OnInit } from '@angular/core';
import { StatoDebitorioVo } from '../../../../core/commons/vo/stato-debitorio-vo';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { FASegmento } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-blocco.class';
import { RiscaFiloAriannaService } from '../../../../shared/services/risca/risca-filo-arianna/risca-filo-arianna.service';
import { AppCallers } from '../../../../shared/utilities';
import { DatiContabiliUtilityService } from '../../../pratiche/service/dati-contabili/dati-contabili/dati-contabili-utility.service';
import { DettaglioMorositaService } from '../../service/dettaglio-accertamenti/dettaglio-morosita.service';
import { TipiGestioneVerifiche } from '../gestione-verifiche/utilities/gestione-verifiche.enums';
import { IGestioneVerificheRouteParams } from '../gestione-verifiche/utilities/gestione-verifiche.interfaces';

@Component({
  selector: 'dettaglio-morosita',
  templateUrl: './dettaglio-morosita.component.html',
  styleUrls: ['./dettaglio-morosita.component.scss'],
})
export class DettaglioMorositaComponent {
  /** StatoDebitorioVo con le informazioni dello stato debitorio e i dati degli accertamenti. */
  statoDebitorio: StatoDebitorioVo;

  /**
   * Costruttore.
   */
  constructor(
    private _datiContabiliUtility: DatiContabiliUtilityService,
    private _dettaglioMorosita: DettaglioMorositaService,
    private _navigationHelper: NavigationHelperService,
    private _riscaFA: RiscaFiloAriannaService
  ) {
    // Richiamo il setup per il componente
    this.setupComponente();
  }

  // /**
  //  * ngOnInit.
  //  */
  // ngOnInit() {
  //   // Lancio l'init del componente
  //   this.initComponent();
  // }

  /**
   * ngOnDestroy.
   */
  ngOnDestroy() {
    // Rimuovo dal servizio le informazioni per lo stato debitorio
    this._dettaglioMorosita.resetStatoDebitorio();
    // Rimuovo la funzionalità del back del browser
    this._navigationHelper.deleteBrowerBack();
  }

  /**
   * ################################
   * FUNZIONI DI SETUP DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione di setup per il componente.
   */
  private setupComponente() {
    // Lancio il setup per il filo d'arianna
    this.setupFiloArianna();
    // Lancio il setup dei dati per lo stato debitorio
    this.setupStatoDebitorio();
    // Setup funzionale per sovrascrivere il comportamento del back del browser
    this.setupBrowserRouting();
  }

  /**
   * Funzione di setup che genera i livelli per il filo d'arianna.
   */
  private setupFiloArianna() {
    // Definisco un oggetto per l'aggiornamento del filo d'arianna
    const rFA = this._riscaFA.verifiche;
    const rmFA = this._riscaFA.ricercaMorosita;
    const dmFA = this._riscaFA.dettaglioMorosita;
    // Creo un segmento con i livelli
    let segmento: FASegmento;
    segmento = this._riscaFA.creaSegmentoByLivelli(rFA, rmFA, dmFA);
    // E' stato generato un segmento, lo aggiungo al filo
    this._riscaFA.aggiungiSegmento(segmento);
  }

  /**
   * Funzione che gestisce le logiche per il recupero/set del dato per lo stato debitorio.
   */
  private setupStatoDebitorio() {
    // Dal servizio specifico recupero lo stato debitorio
    this.statoDebitorio = this._dettaglioMorosita.getStatoDebitorio();
    // Definisco lo stato debitorio all'interno del servizio
    this._datiContabiliUtility.statoDebitorio = this.statoDebitorio;
  }

  /**
   * Setta la funzione di override del back del browser
   */
  private setupBrowserRouting() {
    // Definisco una funzione custom per la gestione del back del browser
    const back = () => {
      // Richiamo la funzione locale
      this.onTornaIndietro();
    };
    // Registro una funzione custom per il back del browser
    this._navigationHelper.setBrowserBack(back);
    // Sovrascrivo la funzione di default del back con la funzione sopra definita
    this._navigationHelper.overrideBrowserBack();
  }

  /**
   * ###############################
   * FUNZIONI DI INIT DEL COMPONENTE
   * ###############################
   */

  // /**
  //  * Funzione di init del componente.
  //  */
  // private initComponent() {}

  /**
   * #############################################
   * FUNZIONI COLLEGATE AL COMPONENTE ACCERTAMENTI
   * #############################################
   */

  /**
   * Funzione collegata all'evento onTornaIndietro del componente.
   */
  onTornaIndietro() {
    // Cancello il filo d'arianna
    this.rimuoviSegmentoDettaglioMorosita();

    // Effettuo l'indietro tramite servizio
    if (this.callerRicercaMorosita) {
      // Torno alla ricerca
      this.tornaRicercaMorosita();
    }
  }

  /**
   * Funzione che effettua un reindirizzamento verso la pagina della ricerca morosita.
   */
  private tornaRicercaMorosita() {
    // Definisco i parametri per la pagina della pratica
    const state: IGestioneVerificheRouteParams = {
      navTarget: TipiGestioneVerifiche.morosita,
    };

    // Richiamo la funzione di back del servizio di navigazione
    this._navigationHelper.stepBack({ stateTarget: state });
  }

  /**
   * #####################
   * GESTIONE FILO ARIANNA
   * #####################
   */

  /**
   * Funzione di comodo che rimuove i segmenti partendo dalla tab ricerche.
   */
  private rimuoviSegmentoDettaglioMorosita() {
    // Recupero i 3 livelli che possono essere presenti nel filo d'arianna
    const rmFA = this._riscaFA.ricercaMorosita;
    const dmFA = this._riscaFA.dettaglioMorosita;
    // Effettuo 3 ricerche all'interno dei segmenti per il filo d'arianna
    const segmentoRM: FASegmento = this._riscaFA.segmentoInFAByLivello(rmFA);
    const segmentoRR: FASegmento = this._riscaFA.segmentoInFAByLivello(dmFA);
    // Definisco un'unica variabile che contiene uno dei possibili valori ritornati
    const segmento = segmentoRM ?? segmentoRR ?? undefined;

    // Verifico se è stato trovato un segmento
    if (segmento) {
      // Segmento di gestione trovato, elimino lui e i segmenti successivi
      this._riscaFA.rimuoviSegmento(segmento);
    }
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che verifica se il caller è definito per 'ricerca morosita'.
   */
  get callerRicercaMorosita(): boolean {
    // Verifico il caller
    return this._navigationHelper.isLastCaller(AppCallers.ricercaMorosita);
  }
}
