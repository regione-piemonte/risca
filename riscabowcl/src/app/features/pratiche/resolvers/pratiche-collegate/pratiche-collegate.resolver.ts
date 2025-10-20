import { Injectable } from '@angular/core';
import { Resolve, Router } from '@angular/router';
import { forkJoin, Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { RiscossioneSearchResultVo } from '../../../../core/commons/vo/riscossione-search-result-vo';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { RiscaSpinnerService } from '../../../../shared/services/risca-spinner.service';
import {
  GruppoEComponenti,
  RiscaServerError,
} from '../../../../shared/utilities';
import { SoggettoDatiAnagraficiService } from '../../service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { PraticheCollegateService } from '../../service/pratiche-collegate/pratiche-collegate.service';
import { PraticheService } from '../../service/pratiche.service';

/**
 * Oggetto contenente le informazioni di request per i dati del resolver.
 */
export interface IPraticheCollegateReq {
  soggetto?: Observable<SoggettoVo>;
  gruppo?: Observable<GruppoEComponenti>;
  pratiche?: Observable<RiscossioneSearchResultVo[]>;
  error?: Observable<RiscaServerError>;
}

/**
 * Oggetto contenente le informazioni di response per il resolver.
 */
export interface IPraticheCollegateRes {
  soggetto?: SoggettoVo;
  gruppo?: GruppoEComponenti;
  pratiche?: RiscossioneSearchResultVo[];
  error?: RiscaServerError;
}

/**
 * Type di comodo per la tipizzazione del della funzione resolve.
 */
type TPraticheCollegateResolve =
  | Observable<IPraticheCollegateRes>
  | Promise<IPraticheCollegateRes>
  | IPraticheCollegateRes;

@Injectable()
export class PraticheCollegateResolver
  implements Resolve<IPraticheCollegateRes>
{
  /**
   * Costruttore
   */
  constructor(
    private _pratiche: PraticheService,
    private _praticheCollegate: PraticheCollegateService,
    private _router: Router,
    private _soggettoDA: SoggettoDatiAnagraficiService,
    private _spinner: RiscaSpinnerService
  ) {}

  /**
   * Resolve che recupera i dati di una pratica per il caricamento del componente.
   * @returns Observable<IPraticheCollegateRes> | Promise<IPraticheCollegateRes> | IPraticheCollegateRes con l'oggetto della pratica.
   */
  resolve(): TPraticheCollegateResolve {
    // Effettuo il recupero delle informazioni
    return this.scaricaDatiPraticheCollegate();
  }

  /**
   * Funzione che gestisce lo scarico dei dati per la pagina delle pratiche collegate.
   * @returns Observable<IPraticheCollegateRes>
   */
  private scaricaDatiPraticheCollegate(): Observable<IPraticheCollegateRes> {
    // Tento di recuperare id_soggetto o id_gruppo dalle fonti dati: state o snapshot
    const { idSoggetto, idGruppo } =
      this._praticheCollegate.getPraticheCollegateForLoad(this._router);

    // Richiamo la funzione di get dei dati pratiche
    return this.getPraticheCollegateInformations(idSoggetto, idGruppo);
  }

  /**
   * Funzione che verifica e gestisce il recupero dei dati per la pagina delle pratiche collegate.
   * @param idSoggetto number che definisce il possibile id soggetto per il recupero dati.
   * @param idGruppo number che definisce il possibile id gruppo per il recupero dati.
   * @returns Observable<IPraticheCollegateRes> con le informazioni scaricate per le pratiche collegate.
   */
  private getPraticheCollegateInformations(
    idSoggetto?: number,
    idGruppo?: number
  ): Observable<IPraticheCollegateRes> {
    // Creo un oggetto di default per il return dei dati
    const defaultRes = { pratiche: [] };

    // Verifico l'input
    if (idSoggetto == undefined && idGruppo == undefined) {
      // Ritorno un oggetto di fallback
      return of(defaultRes);
    }

    // Definisco un oggetto per lo scarico delle informazioni
    const praticheReq: IPraticheCollegateReq = {
      soggetto: of(undefined),
      gruppo: of(undefined),
      pratiche: of([]),
    };

    // Verifico quale id esiste
    if (idSoggetto != undefined) {
      // Scarico i dati del soggetto
      praticheReq.soggetto = this._soggettoDA.getSoggetto(idSoggetto);
      // Scarico i dati delle pratiche collegate
      praticheReq.pratiche = this._pratiche.getPraticheSoggetto(idSoggetto);
      // #
    } else {
      // Modifico l'oggetto di richiesta
      praticheReq.gruppo =
        this._soggettoDA.getGruppoEDettaglioComponenti(idGruppo);
      // Scarico i dati delle pratiche collegate
      praticheReq.pratiche = this._pratiche.getPraticheGruppo(idGruppo);
      // #
    }

    // Lancio le richieste mediante forkJoin
    return forkJoin(praticheReq).pipe(
      catchError((e: RiscaServerError) => {
        // Manipolo l'errore ed effettuo una conversione in success
        const eHanlder: IPraticheCollegateReq = {
          soggetto: of(undefined),
          gruppo: of(undefined),
          pratiche: of([]),
          error: of(e),
        };

        // Ritorno la forkJoin con la gestione dati
        return forkJoin(eHanlder);
      }),
      tap((res: any) => {
        // Chiudo lo spinner per sicurezza
        this._spinner.hideAll();
      })
    );
  }
}
