import { Injectable } from '@angular/core';
import { clone, remove } from 'lodash';
import { forkJoin, Observable } from 'rxjs';
import { RiduzioneAumentoVo } from '../../../../../../../core/commons/vo/riduzione-aumento-vo';
import { RiscaUtilitiesService } from '../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { DTAmbienteFlagManuale } from '../../../../../../../shared/utilities';
import { DatiTecniciAmbienteConsts } from '../../../../../consts/quadri-tecnici/dati-tecnici-ambiente/dati-tecnici-ambiente.consts';
import { UsoLeggePSDAmbienteInfo } from '../../../utilities/interfaces/dt-ambiente-pratica.interfaces';
import { DatiTecniciService } from '../../dati-tecnici/dati-tecnici.service';

/**
 * Interfaccia di comodo per la request di riduzioni e aumenti per l'automatismo.
 */
interface IRiduzioniAumentiAutoReq {
  riduzioni: Observable<RiduzioneAumentoVo[]>;
  aumenti: Observable<RiduzioneAumentoVo[]>;
}

/**
 * Interfaccia di comodo per la response di riduzioni e aumenti per l'automatismo.
 */
interface IRiduzioniAumentiAutoRes {
  riduzioni: RiduzioneAumentoVo[];
  aumenti: RiduzioneAumentoVo[];
}

@Injectable()
export class AutoRiduzioniAumentiDTAmbienteService {
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTA_C = new DatiTecniciAmbienteConsts();

  /** Array di RiduzioneAumentoVo che contiene le informazioni delle riduzioni automatiche. */
  private _ridAuto: RiduzioneAumentoVo[];
  /** Array di RiduzioneAumentoVo che contiene le informazioni degli aumenti automatici. */
  private _aumAuto: RiduzioneAumentoVo[];

  /**
   * Costruttore
   */
  constructor(
    private _datiTecnici: DatiTecniciService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {
    // Scarico le informazioni di riduzioni aumenti automatici
    this.setupService();
  }

  /**
   * Funzione di setup che scarica le informazioni per il setup componente.
   */
  private setupService() {
    // Lancio lo scarico delle informazioni per le riduzioni/aumenti automatici
    this.scaricaRiduzioniAumentiAuto();
  }

  /**
   * Funzione che scarica riduzioni e aumenti per la gestione dell'automatismo per i dati tecnici.
   */
  private scaricaRiduzioniAumentiAuto() {
    // Creo una combinazione delle due chiamate
    const raReq: IRiduzioniAumentiAutoReq = {
      riduzioni: this.getRiduzioniAuto(),
      aumenti: this.getAumentiAuto(),
    };

    // Lancio le chiamate di recupero dei dati
    forkJoin(raReq).subscribe({
      next: (raRes: IRiduzioniAumentiAutoRes) => {
        // Recupero le informazioni
        const { riduzioni, aumenti } = raRes;
        // Assegno localmente le informazioni
        this._ridAuto = riduzioni;
        this._aumAuto = aumenti;
      },
    });
  }

  /**
   * Funzione che scarica le informazioni delle riduzioni da gestire automaticamente.
   * @returns Observable<RiduzioneAumentoVo[]> con le informazioni scaricate.
   */
  getRiduzioniAuto(): Observable<RiduzioneAumentoVo[]> {
    // Definisco il flag auto
    const auto = DTAmbienteFlagManuale.automatico;
    // Richiamo il servizio per il recupero delle riduzioni automatiche
    return this._datiTecnici.getPercentualiRiduzioni(auto);
  }

  /**
   * Funzione che scarica le informazioni degli aumenti da gestire automaticamente.
   * @returns Observable<RiduzioneAumentoVo[]> con le informazioni scaricate.
   */
  getAumentiAuto(): Observable<RiduzioneAumentoVo[]> {
    // Definisco il flag auto
    const auto = DTAmbienteFlagManuale.automatico;
    // Richiamo il servizio per il recupero degli aumenti automatiche
    return this._datiTecnici.getPercentualiAumenti(auto);
  }

  /**
   * ###################################
   * GESTIONE DEGLI ALGORITMI AUTOMATICI
   * ###################################
   */

  /**
   * Funzione che rimuove in automatico, riduzioni e aumenti automatici dagli usi di legge (se definiti).
   * @param usi Array di UsoDiLeggeInfo da manipolare per la rimozione dei dati automatici.
   * @returns Array di UsoDiLeggeInfo con le informazioni aggiornate.
   */
  rimuoviRiduzioniAumentiAutomaticiUsi(
    usi: UsoLeggePSDAmbienteInfo[]
  ): UsoLeggePSDAmbienteInfo[] {
    // Verifico l'input
    if (!usi || usi.length === 0) {
      // Ritorno array vuoto
      return [];
    }

    // Rimappo la lista di usi, rimuovendo riduzioni e aumenti
    const usiUpd = usi.map((uso: UsoLeggePSDAmbienteInfo) => {
      // Lancio la funzione di aggiornato
      return this.rimuoviRiduzioniAumentiAutomaticiUso(uso);
    });

    // ritorno la lista di usi aggiornata
    return usiUpd;
  }

  /**
   * Funzione che rimuove in automatico, riduzioni e aumenti automatici dall'uso di legge (se definiti).
   * @param uso UsoDiLeggeInfo da manipolare per la rimozione dei dati automatici.
   * @returns UsoDiLeggeInfo con le informazioni aggiornate.
   */
  rimuoviRiduzioniAumentiAutomaticiUso(
    uso: UsoLeggePSDAmbienteInfo
  ): UsoLeggePSDAmbienteInfo {
    // Verifico l'input
    if (!uso) {
      // Ritorno se stesso
      return uso;
    }

    // Rimuovo riduzioni e aumenti
    uso = this.rimuoviRiduzioniAutomatiche(uso);
    uso = this.rimuoviAumentiAutomatici(uso);

    // Ritorno la lista di usi aggiornata
    return uso;
  }

  /**
   * Funzione che rimuove le riduzioni automatiche da un uso.
   * @param uso UsoDiLeggeInfo per l'aggiornamento delle riduzioni.
   * @returns UsoDiLeggeInfo aggiornato.
   */
  rimuoviRiduzioniAutomatiche(
    uso: UsoLeggePSDAmbienteInfo
  ): UsoLeggePSDAmbienteInfo {
    // Verifico l'input
    if (!uso) {
      // Ritorno l'oggetto
      return uso;
    }

    // Recupero riduzioni uso e riduzioni automatiche
    const riduzioniUso = clone(uso.percRiduzioni);
    const riduzioniAuto = clone(this._ridAuto);
    // Manipolo le riduzioni uso e le aggiorno
    const riduzioni = this.rimuoviROAAutomaticiUso(riduzioniUso, riduzioniAuto);

    // Aggiorno la lista delle riduzioni nell'oggetto
    uso.percRiduzioni = riduzioni;

    // Ritorno l'oggetto aggiornato
    return uso;
  }

  /**
   * Funzione che rimuove gli aumenti automatici da un uso.
   * @param uso UsoDiLeggeInfo per l'aggiornamento degli aumenti.
   * @returns UsoDiLeggeInfo aggiornato.
   */
  rimuoviAumentiAutomatici(
    uso: UsoLeggePSDAmbienteInfo
  ): UsoLeggePSDAmbienteInfo {
    // Verifico l'input
    if (!uso) {
      // Ritorno l'oggetto
      return uso;
    }

    // Recupero riduzioni uso e riduzioni automatiche
    const aumentiUso = clone(uso.percAumenti);
    const aumentiAuto = clone(this._aumAuto);
    // Manipolo le riduzioni uso e le aggiorno
    const aumenti = this.rimuoviROAAutomaticiUso(aumentiUso, aumentiAuto);

    // Aggiorno la lista delle riduzioni nell'oggetto
    uso.percAumenti = aumenti;

    // Ritorno l'oggetto aggiornato
    return uso;
  }

  /**
   * Funzione comune che rimuove gli elementi delle riduzioni/aumenti di un uso, se contenuti nella lista riduzioni/aumenti automatici.
   * @param ridAumUso Array di RiduzioneAumentoVo da manipolare.
   * @param ridAumAuto Array di RiduzioneAumentoVo che definisce quali elementi andranno rimossi.
   * @returns Array di RiduzioneAumentoVo aggiornati.
   */
  rimuoviROAAutomaticiUso(
    ridAumUso: RiduzioneAumentoVo[],
    ridAumAuto: RiduzioneAumentoVo[]
  ): RiduzioneAumentoVo[] {
    // Verifico l'input
    if (!ridAumUso || ridAumUso.length === 0) {
      // Ritorno array vuoto
      return [];
    }

    // Funzione di comodo che definisce se data una riduzione, questa è automatica
    const isAuto = (r: RiduzioneAumentoVo) => {
      // Recupero la sigla della riduzione/aumento
      const sRA = r.sigla_riduzione_aumento;
      // Effettuo una find all'interno della lista delle riduzioni automatiche
      return ridAumAuto.some((rA: RiduzioneAumentoVo) => {
        // Recupero la sigla della riduzione/aumento auto
        const sARA = rA.sigla_riduzione_aumento;
        // Ritorno il confronto tra sigle input e auto
        return sARA === sRA;
        // #
      });
    };

    // Rimuovo dalle riduzioni, tutte quelle automatiche
    remove(ridAumUso, (r: RiduzioneAumentoVo) => {
      // Verifico se la riduzione è automatica
      return isAuto(r);
    });

    // Ritorno la lista aggiorntata
    return ridAumUso;
  }
}
