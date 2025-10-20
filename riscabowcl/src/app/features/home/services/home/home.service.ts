import { Injectable } from '@angular/core';
import { forkJoin, Observable, of } from 'rxjs';
import { RicercaPaginataResponse } from 'src/app/core/classes/http-helper/http-helper.classes';
import { SoggettoVo } from 'src/app/core/commons/vo/soggetto-vo';
import {
  GruppoEComponenti,
  RiscaTablePagination,
} from 'src/app/shared/utilities';
import { LoggerService } from '../../../../core/services/logger.service';
import { FlagRicercaCL } from '../../../../shared/utilities';
import { GruppoSoggettoService } from '../../../pratiche/service/dati-anagrafici/gruppo-soggetto.service';
import { SoggettoDatiAnagraficiService } from '../../../pratiche/service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { HomeRicercaSoggettiGruppi } from '../../components/home/home.component';
import { AmbitoService } from '../../../ambito/services';

/**
 * Interfaccia che definisce la request per lo scarico di soggetti e gruppi per la ricerca tramite campo libero.
 */
interface CercaSGReq {
  soggetti: Observable<RicercaPaginataResponse<SoggettoVo[]>>;
  gruppiEComponenti: Observable<RicercaPaginataResponse<GruppoEComponenti[]>>;
}

/**
 * Interfaccia che definisce la response per lo scarico di soggetti e gruppi per la ricerca tramite campo libero.
 */
export interface CercaSGRes {
  soggetti: RicercaPaginataResponse<SoggettoVo[]>;
  gruppiEComponenti: RicercaPaginataResponse<GruppoEComponenti[]>;
}

@Injectable({
  providedIn: 'root',
})
export class HomeService {
  /**
   * Costruttore
   */
  constructor(
    private _ambito: AmbitoService,
    private _soggettoDA: SoggettoDatiAnagraficiService
  ) { }

  /**
   * ###############################
   * FUNZIONI DI COMODO DEL SERVIZIO
   * ###############################
   */

  /**
   * Funzione che genera una stringa di testo da visualizzare per la descrizione della sezione ricerca soggetti/gruppi.
   * @returns Oggetto { testata: string, descrizione: string; placeholder: string } con la testata e descrizione in base alle abilitazioni dei dati anagrafici.
   */
  testiRicercaSoggettiGruppi(): {
    testata: string;
    descrizione: string;
    placeholder: string;
  } {
    // Definisco la descrizione
    let head = '';
    let desc = '';
    let plac = '';
    const dSOnly = 'Puoi ricercare il soggetto di interesse indicando';
    const dSAndG = 'Puoi ricercare il soggetto e/o il gruppo indicando';
    const d2 = '"<strong>Nome Gruppo</strong>",';
    const d3 =
      '"<strong>Codice fiscale</strong>", "<strong>Partita IVA</strong>", "<strong>Ragione Sociale</strong>", "<strong>PEC</strong>" o "<strong>Cognome</strong>".';
    const h1 = 'Ricerca Soggetto';
    const h2 = 'e/o Gruppo';
    const p1 = 'Cerca per';
    const p2 = 'Nome Gruppo,';
    const p3 = 'Codice fiscale, Partita IVA, Ragione Sociale, PEC o Cognome';

    // Recupero dal servizio l'oggetto delle abilitazioni
    const abilitato = this._ambito.isGruppoAbilitato;

    // Verifico se l'utente è abilitato ai gruppi
    if (abilitato) {
      // Compongo il messaggio
      head = `${h1} ${h2}`;
      desc = `${dSAndG} ${d2} ${d3}`;
      plac = `${p1} ${p2} ${p3}`;
      // #
    } else {
      // Compongo il messaggio
      head = `${h1}`;
      desc = `${dSOnly} ${d3}`;
      plac = `${p1} ${p3}`;
      // #
    }

    // Ritorno la descrizione
    return { testata: head, descrizione: desc, placeholder: plac };
  }

  /**
   * ##############################
   * FUNZIONI CON CHIAMATE ALLE API
   * ##############################
   */

  /**
   * Funzione predisposta alla ricerca di soggetti e gruppi, usando un campo libero e un target per il tipo di ricerca.
   * @param idAmbito number che definisce l'id dell'ambito per la ricerca.
   * @param ricercaSGData HomeRicercaSoggettiGruppi contenente i dati per la ricerca.
   * @returns CercaSGReq con i dati scaricati a seconda della ricerca.
   */
  cercaSGCampoLibero(idAmbito: number, ricercaSGData: HomeRicercaSoggettiGruppi,
    paginazioneSoggetti: RiscaTablePagination, paginazioneGruppi: RiscaTablePagination): Observable<CercaSGRes> {
    // Verifico l'input
    if (idAmbito === undefined || !ricercaSGData) {
      const sources = [];

      // Ritorno la forkJoin
      return forkJoin({
        soggetti: of(
          new RicercaPaginataResponse<SoggettoVo[]>({
            sources,
            paging: paginazioneSoggetti,
          })
        ),
        gruppiEComponenti: of(
          new RicercaPaginataResponse<GruppoEComponenti[]>({
            sources,
            paging: paginazioneGruppi,
          })
        ),
      });
    }

    // Variabili di comodo
    const c = ricercaSGData.campoLibero;
    const tv = ricercaSGData.target.value;
    const T = FlagRicercaCL.soggettiEGruppi;
    const S = FlagRicercaCL.soggetti;
    const G = FlagRicercaCL.gruppi;

    // Definisco l'oggetto per la chiamata
    const req: CercaSGReq = {
      soggetti: of(
        new RicercaPaginataResponse({
          sources: [],
          paging: paginazioneSoggetti,
        })
      ),
      gruppiEComponenti: of(
        new RicercaPaginataResponse({ sources: [], paging: paginazioneGruppi })
      ),
    };

    // Verifico il target value
    if (tv === T) {
      req.soggetti = this.cercaSoggetti(idAmbito, c, paginazioneSoggetti);
      req.gruppiEComponenti = this.cercaGruppiEComponenti(idAmbito, c, T, paginazioneGruppi);
    } else if (tv === S) {
      // Imposto i flag per soggetti con S
      req.soggetti = this.cercaSoggetti(idAmbito, c, paginazioneSoggetti);
      // #
    } else if (tv === G) {
      // Imposto i flag per gruppi con G
      //req.gruppiEComponenti = this.cercaGruppiEComponenti(idAmbito,c,G,paginazioneGruppi);

      //emmanuel => anzichè fare la chiamata passando G passo il T che è la combinazione di S e G
      req.gruppiEComponenti = this.cercaGruppiEComponenti(idAmbito, c, T, paginazioneGruppi);
      // #
    }

    // Metto in forkJoin le chiamate
    return forkJoin(req);
  }

  /**
   * Funzione di comodo per la ricerca dei soggetti.
   * @param idA number che definisce l'id ambito per la ricerca.
   * @param c string che definisce il campo libero per la ricerca.
   * @returns Observable<RicercaPaginataResponse> con i dati dei soggetti trovati e i dati della paginazioje.
   */
  private cercaSoggetti(
    idA: number,
    c: string,
    paginazione: RiscaTablePagination
  ): Observable<RicercaPaginataResponse<SoggettoVo[]>> {
    // Eseguo la ricerca dei dati
    return this._soggettoDA.cercaSoggettiCampoLibero(idA, c, paginazione);
  }

  /**
   * Funzione di comodo per la ricerca dei gruppi.
   * @param idA number che definisce l'id ambito per la ricerca.
   * @param c string che definisce il campo libero per la ricerca.
   * @returns Observable<GruppoEComponenti[]> con i dati dei gruppi e dei componenti trovati.
   */
  private cercaGruppiEComponenti(
    idA: number,
    c: string,
    hFlag: FlagRicercaCL,
    paginazioneGruppi: RiscaTablePagination = undefined
  ): Observable<RicercaPaginataResponse<GruppoEComponenti[]>> {
    // Eseguo la ricerca dei dati
    return this._soggettoDA.cercaGruppiEComponentiCampoLibero(
      idA,
      c,
      hFlag,
      true,
      paginazioneGruppi
    );
  }
}
