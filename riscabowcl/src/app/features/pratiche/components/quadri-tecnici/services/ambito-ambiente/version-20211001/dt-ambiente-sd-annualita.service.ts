import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AnnualitaSDVo } from '../../../../../../../core/commons/vo/annualita-sd-vo';
import { StatoDebitorioVo } from '../../../../../../../core/commons/vo/stato-debitorio-vo';
import { ConfigService } from '../../../../../../../core/services/config.service';
import { HttpUtilitiesService } from '../../../../../../../shared/services/http-utilities/http-utilities.service';
import { RiscaUtilitiesService } from '../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { IRiscaAnnoSelect } from '../../../../../../../shared/utilities';
import { DatiTecniciAmbienteSDConsts } from '../../../../../consts/quadri-tecnici/dati-tecnici-ambiente-sd/dati-tecnici-ambiente-sd.consts';

@Injectable({ providedIn: 'root' })
export class DTAmbienteSDAnnualitaService extends HttpUtilitiesService {
  /** Oggetto contenente le costanti utilizzate nel componente. */
  private DTA_SD_C = new DatiTecniciAmbienteSDConsts();

  /** string con il path relativo alla chiamata: /tipi-usi-regole/anni. */
  private PATH_USI_LEGGE_ANNI: string = '/tipi-usi-regole/anni';

  /**
   * Costruttore
   */
  constructor(
    config: ConfigService,
    private _http: HttpClient,
    riscaUtilities: RiscaUtilitiesService
  ) {
    // Richiamo il super
    super(config, riscaUtilities);
  }

  /**
   * Funzione che genera una lista di anni definita per le annualità per inserimento/modifica.
   * A seconda della configurazione in input vengono definite logiche di gestione differenti.
   * @param idAmbito number con l'id ambito di riferimento per lo scarico della lista.
   * @param statoDebitorio StatoDebitorioVo per disattivare gli anni che risultano già definiti nello stato debitorio.
   * @param annualitaAbilitate AnnualitaSDVo che definisce l'annualità da escludere dal blocco degli anni.
   * @param annoAnnualita number che definisce l'anno dell'annualità. L'anno sarà abilitato e selezionato per default.
   * @returns IRiscaAnnoSelect[] che definisce l'array di anni che l'utente può selezionare.
   */
  generaListaAnnualita(
    idAmbito: number,
    statoDebitorio?: StatoDebitorioVo,
    annualitaAbilitate?: AnnualitaSDVo[],
    annoAnnualita?: number
  ): Observable<IRiscaAnnoSelect[]> {
    // Definisco l'array di anni da disabilitare
    let disabilitaAnnualita: number[] = [];
    // Definisco l'array di anni da esclidere dalla disabilitazione
    let permettiAnnualita: number[] = [];

    // Controllo di esistenza per lo stato debitorio
    if (statoDebitorio != undefined) {
      // Prendo la lista di annualità da escludere poiché già presenti nell'oggetto stato debitorio
      const annualitaEscluse = statoDebitorio.annualita_sd as AnnualitaSDVo[];
      // Prendo dalle annualità il loro anno di riferimento
      disabilitaAnnualita = annualitaEscluse.map((a: AnnualitaSDVo) => a.anno);
    }
    // Controllo di esistenza per le annualità d'abilitare
    if (annualitaAbilitate != undefined) {
      // Assegno le annualità da permettere, poiché richiesta specifica (potrebbe essereci l'annulità già inserita, ma richiesta in modifica)
      permettiAnnualita = annualitaAbilitate.map(
        (aS: AnnualitaSDVo) => aS.anno
      );
    }
    // Controllo di esistenza per l'anno annualita
    if (annoAnnualita != undefined) {
      // Aggiungo alla lista di permesso questo anno
      permettiAnnualita.push(annoAnnualita);
    }

    // EFfettuo la chiamata all'annualità usi, per ottenere la lista dal server e ritorno la risposta che genererò
    return this.annualitaUsi(idAmbito).pipe(
      map((anniGenrati: number[]) => {
        // Verifico il risultato ottenuo dalla risposta
        anniGenrati = anniGenrati ?? [];

        // Effettuo un map della lista degli anni, definendo proprietà per la gestione della select
        return anniGenrati?.map((anno: number) => {
          // Definisco l'oggetto di base da ritornare
          const annualita: IRiscaAnnoSelect = { anno };
          // Per la gestione di informazioni solo FE, assegno l'annulita con un parse ad any
          const annualitany: any = annualita;

          // Verifico se l'annualità attualmente iterata risulta presenete nella lista di annualità da disabilitare
          let annoDaDisabilitare: boolean;
          annoDaDisabilitare = disabilitaAnnualita.some((aSD: number) => {
            // Verifico e ritorno il controllo tra l'anno nella lista di quelli disabilitati e l'annualità iterata
            return aSD === anno;
          });
          // Verifico se l'annualità ciclata è comunque abilitata perché definita dal chiamante
          let annoAbilitatoSpeciale: boolean;
          annoAbilitatoSpeciale = permettiAnnualita.some((pa: number) => {
            // Verifico e ritorno il controllo tra l'anno nella lista di quelli abilitati speciali e l'annualità iterata
            return pa === anno;
          });

          // Verifico se l'annualità è da disabilitare o è abilitata
          annualitany.__disabled = annoDaDisabilitare && !annoAbilitatoSpeciale;

          // Ritorno l'oggetto generato
          return annualitany;
        });
      })
    );
  }

  /**
   * Funzione che effettua lo scarico degli anni da presentare all'interno della tendina di scelta delle annualità.
   * @param idAmbito number con l'id ambito di riferimento per lo scarico della lista.
   */
  annualitaUsi(idAmbito: number): Observable<number[]> {
    // Definisco l'url
    const url = this._config.appUrl(this.PATH_USI_LEGGE_ANNI, idAmbito);
    // Effettuo la chiamata
    return this._http.get<number[]>(url).pipe(
      map((result: number[]) => {
        // Converto la risposta del server
        return result;
        // #
      })
    );
  }
}
