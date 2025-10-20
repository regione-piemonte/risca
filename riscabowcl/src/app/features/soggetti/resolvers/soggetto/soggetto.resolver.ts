import { Injectable } from '@angular/core';
import { Resolve, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { RicercaSoggetto } from '../../../../core/commons/vo/ricerca-soggetto-vo';
import { UserService } from '../../../../core/services/user.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { AppActions } from '../../../../shared/utilities';
import { AmbitoService } from '../../../ambito/services';
import { SoggettoDatiAnagraficiService } from '../../../pratiche/service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { ISoggettoRouteParams } from '../../components/soggetto/utilities/soggetto.interfaces';

/**
 * Type di comodo per la tipizzazione del della funzione resolve.
 */
type TSoggettoResolve =
  | Observable<ISoggettoRouteParams>
  | Promise<ISoggettoRouteParams>
  | ISoggettoRouteParams;

@Injectable()
export class SoggettoResolver implements Resolve<TSoggettoResolve> {
  /**
   * Costruttore
   */
  constructor(
    private _ambito: AmbitoService,
    private _router: Router,
    private _riscaUtilities: RiscaUtilitiesService,
    private _soggettoDA: SoggettoDatiAnagraficiService,
    private _user: UserService
  ) {}

  /**
   * Resolve che recupera i dati di una soggetto, anche per l'eventuale stato della fonte, ma solo se la configurazione lo permette.
   * @returns TSoggettoResolve con l'oggetto da passare al componente.
   */
  resolve(): TSoggettoResolve {
    // Effettuo il recupero delle informazioni
    return this.verificaScaricaDatiSoggettoFonte();
  }

  /**
   * Funzione che verifica e gestisce lo scarico dei dati per la pagina del soggetto.
   * @returns Observable<ISoggettoRouteParams> con l'oggetto da passare al componente.
   */
  private verificaScaricaDatiSoggettoFonte(): Observable<ISoggettoRouteParams> {
    // Verifico la configurazione per lo scarico dati dalla fonte in lettura e del soggetto
    const isFAL = this.verificaLetturaDaFonte();
    const checkModalita = this.verificaModalitaPagina();

    // Verifico le configurazioni
    if (isFAL && checkModalita) {
      // Scarico i dati del soggetto per il recupero delle informazioni della fonte
      return this.scaricaDatiSoggettoFonte();
      // #
    } else {
      // Ritorno l'attuale configurazione
      return this.inoltraStateSoggetto();
    }
  }

  /**
   * Funzione che verifica la configurazione per ambito, e se questo è abilitato alla lettura dati dalla fonte.
   * @returns boolean con lo stato di check per la lettura dalla fonte.
   */
  private verificaLetturaDaFonte(): boolean {
    // Richiamo il servizio per sapere lo stato della lettura fonte
    return this._ambito.isFonteAbilitataInLettura;
  }

  /**
   * Funzione che verifica se la modalità della pagina è "modifica".
   * @returns boolean con lo stato di check definito dalla struttura del soggetto.
   */
  private verificaModalitaPagina() {
    // Recupero lo state dal servizio
    const stateSoggetto = this.getSoggettoState();
    // Verifico la modalita della pagina
    const { modalita } = stateSoggetto || {};

    // Ritorno il check tra i flag
    return modalita === AppActions.modifica;
  }

  /**
   * Funzione che scarica le informazioni del soggetto, con l'aggiunta dei dati della fonte.
   * @returns Observable<ISoggettoRouteParams> con i dati passati nello state del routing e i dati della fonte.
   */
  private scaricaDatiSoggettoFonte(): Observable<ISoggettoRouteParams> {
    // Recupero lo state dal servizio
    const stateSoggetto = this.getSoggettoState();
    // Estraggo i dati per effettuare la ricerca, così da ottenere i dati della fonte
    const { idTS, cf } = this.getSoggettoTSAndCF();
    const idA = this._user.idAmbito;

    // Lancio la ricerca di un soggetto
    return this._soggettoDA.cercaSoggetto(idA, idTS, cf).pipe(
      map((ricercaSoggetto: RicercaSoggetto) => {
        // Recupero la lista dei campi aggiornati dalla fonte
        const caf = ricercaSoggetto.lista_campi_aggiornati || [];
        // Aggiorno l'oggetto state con la lista dei campi aggiornati
        stateSoggetto.campiAggiornatiFonte = caf;
        // Aggiorno l'oggetto soggetto (se esiste) perché potrebbe avere campi diversi se aggiornati dalla fonte
        if (ricercaSoggetto.soggetto) {
          // Modifico l'informazione
          stateSoggetto.soggetto = ricercaSoggetto.soggetto;
        }

        // Ritorno l'oggetto dello state come response finale
        return stateSoggetto;
        // #
      })
    );
  }

  /**
   * Funzione che inoltra i dati passati tramite state al componente del soggetto.
   * @returns Observable<ISoggettoRouteParams> con i dati passati nello state del routing.
   */
  private inoltraStateSoggetto(): Observable<ISoggettoRouteParams> {
    // Recupero lo state dal servizio
    const state = this.getSoggettoState();
    // Ritorno un observable con i dati dello state
    return of(state);
  }

  /**
   * Funzione di comodo che ritorna l'oggetto state dal servizio di routing.
   * @returns ISoggettoRouteParams contenente i dati dello state.
   */
  private getSoggettoState(): ISoggettoRouteParams {
    // Recupero i parametri dallo state della route
    return this._riscaUtilities.getRouterState(this._router);
  }

  /**
   * Funzione di supporto che recupera dallo state il soggetto, successivamente recupera codice fisca e id tipo soggetto.
   * @returns Oggetto { idTS: number; cf: string } con le informazioni estratte.
   */
  private getSoggettoTSAndCF(): { idTS: number; cf: string } {
    // Recupero lo state dal servizio
    const stateSoggetto = this.getSoggettoState();
    // Estraggo i dati per effettuare la verifica
    const { soggetto } = stateSoggetto || {};
    // Recupero i dati per il check
    const idTS = soggetto?.tipo_soggetto?.id_tipo_soggetto;
    const cf = soggetto?.cf_soggetto;

    // Ritorno le informazioni
    return { idTS, cf };
  }
}
