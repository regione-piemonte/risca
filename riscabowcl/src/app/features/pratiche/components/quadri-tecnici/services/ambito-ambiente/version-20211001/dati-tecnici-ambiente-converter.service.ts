import { Injectable } from '@angular/core';
import { flatten, uniqWith } from 'lodash';
import { forkJoin, Observable, of } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { AnnualitaSDVo } from '../../../../../../../core/commons/vo/annualita-sd-vo';
import { AnnualitaUsoSDVo } from '../../../../../../../core/commons/vo/annualita-uso-sd-vo';
import { PraticaDTVo } from '../../../../../../../core/commons/vo/pratica-vo';
import { RiduzioneAumentoVo } from '../../../../../../../core/commons/vo/riduzione-aumento-vo';
import { StatoDebitorioVo } from '../../../../../../../core/commons/vo/stato-debitorio-vo';
import { IUsoLeggeVo } from '../../../../../../../core/commons/vo/uso-legge-vo';
import { LoggerService } from '../../../../../../../core/services/logger.service';
import { RiscaUtilitiesService } from '../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  RiscaFormatoDate,
  RiscaRegExp,
  ServerStringAsBoolean,
} from '../../../../../../../shared/utilities';
import { DatiTecniciAmbienteConsts } from '../../../../../consts/quadri-tecnici/dati-tecnici-ambiente/dati-tecnici-ambiente.consts';
import { DTGrandeIdroelettricoExtras20211001 } from '../../../utilities/classes/ambito-ambiente/version-20211001/grande-idroelettrico/grande-idroelettrico.class';
import {
  GRANDE_IDROELETTRICO_20211001,
  GRANDE_IDROEL_VARIABILE_20211001,
  GRANDE_IDROEL_ENERGIA_GRATUITA_20211001,
} from '../../../utilities/classes/ambito-ambiente/version-20211001/grande-idroelettrico/utilities/grande-idroelettrico.consts';
import { DTExtrasClass } from '../../../utilities/classes/dt-extras/dt-extras.class';
import {
  IDatiTecniciAmbiente,
  UsoLeggePSDAmbienteInfo,
} from '../../../utilities/interfaces/dt-ambiente-pratica.interfaces';
import {
  DatiTecniciAmbienteRicerca,
  DatiTecniciUsiRicercaVo,
  DatiTecniciUsoRicercaVo,
  DatiTecniciVoRicerca,
  UsoDiLeggeInfoRicerca,
} from '../../../utilities/vo/dati-tecnici-ambiente-ricerca-vo';
import {
  DatiTecniciAmbienteVo,
  DatiTecniciAumentoVo,
  DatiTecniciGeneraliAmbienteVo,
  DatiTecniciRiduzioneVo,
  DatiTecniciUsiAmbienteVo,
  DatiTecniciUsoAmbienteVo,
} from '../../../utilities/vo/dati-tecnici-ambiente-vo';
import { DatiTecniciService } from '../../dati-tecnici/dati-tecnici.service';

/**
 * Interfaccia che definisce la request per lo scarico dei dati per gli usi in ambito ambiente.
 */
interface IUsiAmbienteReq {
  usoLegge: Observable<IUsoLeggeVo>;
  usiSpecifici: Observable<IUsoLeggeVo[]>;
  riduzioni: Observable<RiduzioneAumentoVo[]>;
  aumenti: Observable<RiduzioneAumentoVo[]>;
}

/**
 * Interfaccia che definisce la response per lo scarico dei dati per gli usi in ambito ambiente.
 */
interface IUsiAmbienteRes {
  usoLegge: IUsoLeggeVo;
  usiSpecifici: IUsoLeggeVo[];
  riduzioni: RiduzioneAumentoVo[];
  aumenti: RiduzioneAumentoVo[];
}

/**
 * Interfaccia che definisce la request per lo scarico dati per i dati tecnici ambiente di pratica e stato debitorio.
 */
interface ISDRiepilogoReq {
  dtPratica: Observable<IDatiTecniciAmbiente>;
  dtAnnualita: Observable<IDatiTecniciAmbiente[]>;
}

/**
 * Interfaccia che definisce la response per lo scarico dati per i dati tecnici ambiente di pratica e stato debitorio.
 */
interface ISDRiepilogoRes {
  dtPratica: IDatiTecniciAmbiente;
  dtAnnualita: IDatiTecniciAmbiente[];
}

@Injectable({ providedIn: 'root' })
export class DatiTecniciAmbienteConverterService {
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTA_C = new DatiTecniciAmbienteConsts();
  /** Oggetto RiscaRegExp utilizzate come supporto per la validazione dei campi. */
  private riscaRegExp: RiscaRegExp = new RiscaRegExp();

  /**
   * Costruttore
   */
  constructor(
    private _datiTecnici: DatiTecniciService,
    private _logger: LoggerService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {}

  /**
   * ###########################################################
   * FUNZIONI DI CONVERSIONE: PraticaDTVo => DatiTecniciAmbiente
   * ###########################################################
   */

  /**
   * Funzione di conversione da un oggetto PraticaDTVo ad un oggetto DatiTecniciAmbiente.
   * @param praticaDTVo PraticaDTVo da convertire.
   * @param usiAnnualita AnnualitaUsoSDVo[] con la lista di valori per completare le informazioni dei dati tecnici.
   * @returns DatiTecniciAmbiente convertito.
   */
  convertPraticaDTVoToDatiTecniciAmbiente(
    praticaDTVo: PraticaDTVo,
    usiAnnualita?: AnnualitaUsoSDVo[]
  ): Observable<IDatiTecniciAmbiente> {
    // Verifico l'input
    if (!praticaDTVo) {
      // Niente da convertire
      return of(undefined);
    }

    // Estraggo le informazioni
    const { riscossione } = praticaDTVo;
    // Estraggo le proprietà della riscossione
    const { dati_tecnici } = riscossione || {};

    // Verifico che esistano i dati tecnici
    if (!dati_tecnici) {
      // Niente da convertire
      return of(undefined);
    }

    // Lancio la funzione di conversione
    return this.convertDatiTecniciCoreToDatiTecniciAmbiente(
      dati_tecnici,
      usiAnnualita
    );
  }

  /**
   * Funzione di conversione da una stringa che definisce i dati tecnici ad un oggetto DatiTecniciAmbiente.
   * @param datiTecnici string o DatiTecniciVo da convertire.
   * @param usiAnnualita AnnualitaUsoSDVo[] con la lista di valori per completare le informazioni dei dati tecnici.
   * @returns DatiTecniciAmbiente convertito.
   */
  convertDatiTecniciCoreToDatiTecniciAmbiente(
    datiTecnici: string | DatiTecniciAmbienteVo,
    usiAnnualita?: AnnualitaUsoSDVo[]
  ): Observable<IDatiTecniciAmbiente> {
    // Verifico che esistano i dati tecnici
    if (!datiTecnici) {
      // Niente da convertire
      return of(undefined);
    }

    // Controllo di sicurezza e converto il dato
    const isDTString = typeof datiTecnici === 'string';
    const dt: DatiTecniciAmbienteVo = isDTString
      ? JSON.parse(datiTecnici as string)
      : datiTecnici;
    // Faccio un log dei dati tecnici come check preventivo
    const d = 'DatiTecniciCore => DatiTecniciAmbiente';
    this.logDatiTecnici(dt, d);

    // Estraggo le informazioni
    const { dati_generali, usi } = dt;
    // Definisco un contenitore per i dati tecnici ambiente
    const dta: IDatiTecniciAmbiente = {};

    // Recupero la portata
    let portata: number = null;
    if (dati_generali && dati_generali.portata_da_assegnare !== -1) {
      portata = dati_generali.portata_da_assegnare;
    }
    // Definisco i dati generali
    dta.comune = dati_generali?.comune;
    dta.corpoIdricoCaptazione = dati_generali?.corpo_idrico_captazione;
    dta.nomeImpiantoIdroElettrico = dati_generali?.nome_impianto_idrico ?? '';
    dta.portataDaAssegnare = portata;

    // Verifico il valore della gestione manuale
    const gm = dati_generali?.gestione_manuale;
    // Genero l'oggetto checkbox per la gestione del valore
    dta.gestioneManuale = {
      id: this.DTA_C.LABEL_GESTIONE_MANUALE,
      label: this.DTA_C.LABEL_GESTIONE_MANUALE,
      value: gm === ServerStringAsBoolean.true,
      check: gm === ServerStringAsBoolean.true,
    };
    // Converto i DatiTecniciUsiVo in UsoDiLeggeInfo
    dta.usiDiLegge = [];

    // Recupero dal server tutte le informazioni degli usi di legge.
    return this.convertDatiTecniciUsiVoToUsiDiLeggeInfo(usi, usiAnnualita).pipe(
      map((datiUsi: UsoLeggePSDAmbienteInfo[]) => {
        // Aggiungo gli usi di legge all'oggetto
        dta.usiDiLegge = datiUsi || [];
        // Ritorno l'oggetto completo
        return dta;
      })
    );
  }

  /**
   * Funzione che converte un oggetto DatiTecniciUsiVo in una lista di oggetti UsoDiLeggeInfo.
   * @param dtuVo DatiTecniciUsiVo da convertire.
   * @param usiAnnualita AnnualitaUsoSDVo[] con la lista di valori per completare le informazioni dei dati tecnici.
   * @returns Array di UsoDiLeggeInfo convertita.
   */
  convertDatiTecniciUsiVoToUsiDiLeggeInfo(
    dtuVo: DatiTecniciUsiAmbienteVo,
    usiAnnualita?: AnnualitaUsoSDVo[]
  ): Observable<UsoLeggePSDAmbienteInfo[]> {
    // Verifico l'input
    if (!dtuVo) {
      // Nessuna configurazione
      return of([]);
    }

    // Variabili di comodo
    const u = usiAnnualita;
    // Definisco un array che conterrà le informazioni da ritornare
    const usiDiLeggeInfo: Observable<UsoLeggePSDAmbienteInfo>[] = [];

    // Ciclo le proprietà dell'oggetto, e converto le informazioni
    for (let v of Object.values(dtuVo)) {
      // Converto l'oggetto in UsoDiLeggeInfo
      const usoDiLegge: Observable<UsoLeggePSDAmbienteInfo> =
        this.convertDatiTecniciUsoVoToUsoDiLeggeInfo(v, u);
      // Aggiungo la chiamata
      usiDiLeggeInfo.push(usoDiLegge);
    }

    // Verifico se ci sono richieste da fare
    if (usiDiLeggeInfo.length > 0) {
      // Ritorno la lista di request
      return forkJoin(usiDiLeggeInfo);
      // #
    } else {
      // Ritorno una lista vuota
      return of([]);
    }
  }

  /**
   * Funzione di conversione da un oggetto DatiTecniciUsoVo ad un oggetto UsoDiLeggeInfo.
   * @param dtuVo DatiTecniciUsoVo da convertire.
   * @param usiAnnualita AnnualitaUsoSDVo[] con la lista di valori per completare le informazioni dei dati tecnici.
   * @returns UsoDiLeggeInfo convertito.
   */
  convertDatiTecniciUsoVoToUsoDiLeggeInfo(
    dtuVo: DatiTecniciUsoAmbienteVo,
    usiAnnualita?: AnnualitaUsoSDVo[]
  ): Observable<UsoLeggePSDAmbienteInfo> {
    // Verifico l'input
    if (!dtuVo) {
      // Nessuna configurazione
      return;
    }

    // Definisco un oggetto per la conversione
    const usoDiLegge: UsoLeggePSDAmbienteInfo = {};

    // Estraggo l'id tipo uso legge dall'oggetto del dato tecnico
    const idTipoUso = dtuVo.id_tipo_uso_legge;
    // Verifico se esiste l'id effettivamente
    if (idTipoUso == undefined) {
      // Segnalo in console un errore specifico
      this.reportDTUsoIdError(dtuVo);
      // #
    }

    // Recupero l'oggetto uso di legge principale
    return this._datiTecnici.getUsoDiLegge(idTipoUso).pipe(
      switchMap((usoLegge: IUsoLeggeVo) => {
        // Creo un oggetto che conterrà le informazioni per gli usi di questo uso di legge
        const usiReq: IUsiAmbienteReq = {
          usoLegge: of(usoLegge),
          usiSpecifici: of([]),
          riduzioni: of([]),
          aumenti: of([]),
        };

        // Ottengo gli observable per avere tutti gli usi di legge specifici
        usiReq.usiSpecifici = this.getUsiDiLeggeSpecifici(dtuVo.uso_specifico);
        // Ottengo gli observable per avere tutte le riduzioni
        usiReq.riduzioni = this.getRiduzioni(dtuVo.riduzione);
        // Ottengo gli observable per avere tutti gli aumenti
        usiReq.aumenti = this.getAumenti(dtuVo.aumento);
        // Ritorno la fork di tutte le request per gli usi di legge specifici
        return forkJoin(usiReq);
        // #
      }),
      map((usiRes: IUsiAmbienteRes) => {
        // Estraggo le informazioni
        const dettaglioUsoLegge = usiRes.usoLegge;
        const dettaglioUsiSpec = usiRes.usiSpecifici;

        // Assegno le proprietà
        usoDiLegge.usoDiLegge = dettaglioUsoLegge;
        usoDiLegge.usiDiLeggeSpecifici = dettaglioUsiSpec;

        usoDiLegge.unitaDiMisura = {
          des_unita_misura: dtuVo.unita_di_misura,
          sigla_unita_misura: dtuVo.unita_di_misura,
        };
        usoDiLegge.quantita = this.notMenoUno(dtuVo.qta_acqua);
        usoDiLegge.quantitaFaldaProfonda = this.notMenoUno(
          dtuVo.qta_falda_profonda
        );
        usoDiLegge.percFaldaProfonda = this.notMenoUno(
          dtuVo.perc_falda_profonda
        );
        usoDiLegge.quantitaFaldaSuperficiale = this.notMenoUno(
          dtuVo.qta_falda_superficie
        );
        usoDiLegge.percFaldaSuperficiale = this.notMenoUno(
          dtuVo.perc_falda_superficie
        );
        usoDiLegge.percRiduzioni = usiRes.riduzioni;
        usoDiLegge.percAumenti = usiRes.aumenti;
        usoDiLegge.dataScadenzaEmasIso =
          this._riscaUtilities.convertServerDateToNgbDateStruct(
            dtuVo.data_scadenza_emas_iso
          );

        // Verifico se sono stati passati degli usi dell'annualità
        if (usiAnnualita) {
          // Recupero il codice uso per la ricerca
          const usoAnnualita = usiAnnualita.find((uA: AnnualitaUsoSDVo) => {
            // Effettuo una comparazione per id
            return uA.tipo_uso?.id_tipo_uso == idTipoUso;
          });

          // Verifico se è stata trovata corrispondenza
          if (usoAnnualita) {
            // Definisco canone e canone unitario
            usoDiLegge.canoneUso = usoAnnualita.canone_uso;
            usoDiLegge.canoneUnitarioUso = usoAnnualita.canone_unitario;
          }
        }

        // Definisco le informazioni per l'ultima funzionalità
        const codiceUso = usiRes.usoLegge.cod_tipo_uso;
        const dtUso = dtuVo;
        // Verifico se l'uso di legge ha dei parametri extra specifici
        usoDiLegge.extras = this.usoDiLeggeExtras(codiceUso, dtUso);

        // Ritorno l'oggetto
        return usoDiLegge;
      })
    );
  }

  /**
   * Funzione che verifica se l'uso di legge passato in input possiede, per configurazione, dei dati extra.
   * Se l'uso risulta tra quelli con possibili dati extra, verrà generata una classe specifica per gestire queste informazioni.
   * @param codiceUso string con il codice dell'uso di legge.
   * @param dtUso any contenente le informazioni dei dati tecnici per un uso. Dovrebbe essere passato sempre un oggetto.
   * @returns DTExtrasClass<any> con la configurazione per i datri extra specifici per l'uso.
   */
  usoDiLeggeExtras(codiceUso: string, dtUso: any): DTExtrasClass<any> {
    // Verifico l'input
    if (!codiceUso || !dtUso) {
      // Mancano le configurazioni minime
      return undefined;
    }

    // Effettuo uno switch e verifico il codice dell'uso in input
    switch (codiceUso) {
      case GRANDE_IDROELETTRICO_20211001:
      case GRANDE_IDROEL_VARIABILE_20211001:
      case GRANDE_IDROEL_ENERGIA_GRATUITA_20211001:
        // dtUso.PERC_QUOTA_VAR = 2210.1234;
        // dtUso.PNM_PER_ENERG_GRAT = 123000000.1234;
        // dtUso.COEFF_ENERG_GRAT = 4031233.1231;
        // dtUso.PNM_PER_ENERG_GRAT = 13012232.13123;
        // dtUso.TOT_ENERG_PROD = 20132.123123;
        // dtUso.TOT_RICAVI_ANNO = 1200.1233;
        // dtUso.PREZZO_MED_ORA_POND = 1000.3213;
        // Uso mappato, genero la sua classe specifica
        return new DTGrandeIdroelettricoExtras20211001(codiceUso, dtUso);
      // #
      default:
        // Nessuna configurazione trovata
        return undefined;
    }
  }

  /**
   * ###########################################################################
   * FUNZIONI DI CONVERSIONE: DatiTecniciVoRicerca => DatiTecniciAmbienteRicerca
   * ###########################################################################
   */

  /**
   * Funzione di conversione da una stringa che definisce i dati tecnici ad un oggetto DatiTecniciAmbiente.
   * @param datiTecnici string o DatiTecniciVo da convertire.
   * @returns DatiTecniciAmbiente convertito.
   */
  convertDatiTecniciCoreRicercaToDatiTecniciAmbienteRicerca(
    datiTecnici: string | DatiTecniciVoRicerca
  ): Observable<DatiTecniciAmbienteRicerca> {
    // Verifico che esistano i dati tecnici
    if (!datiTecnici) {
      // Niente da convertire
      return of(undefined);
    }

    // Controllo di sicurezza e converto il dato
    const isDTString = typeof datiTecnici === 'string';
    const dt: DatiTecniciVoRicerca = isDTString
      ? JSON.parse(datiTecnici as string)
      : datiTecnici;
    // Faccio un log dei dati tecnici come check preventivo
    const d = 'DatiTecniciCoreRicerca => DatiTecniciAmbienteRicerca';
    this.logDatiTecnici(dt, d);

    // Estraggo le informazioni
    const { dati_generali, usi } = dt;

    // Definisco un contenitore per i dati tecnici ambiente
    const dta: DatiTecniciAmbienteRicerca = {};

    // Recupero la portata
    let portata: number = null;
    if (dati_generali && dati_generali.portata_da_assegnare !== -1) {
      portata = dati_generali.portata_da_assegnare;
    }
    // Definisco i dati generali
    dta.comune = dati_generali?.comune;
    dta.corpoIdricoCaptazione = dati_generali?.corpo_idrico_captazione;
    dta.nomeImpiantoIdroElettrico = dati_generali?.nome_impianto_idrico;
    dta.portataDaAssegnare = portata;

    // Verifico il valore della gestione manuale
    const gm = dati_generali?.gestione_manuale;
    // Genero l'oggetto checkbox per la gestione del valore
    dta.gestioneManuale = {
      id: this.DTA_C.LABEL_GESTIONE_MANUALE,
      label: this.DTA_C.LABEL_GESTIONE_MANUALE,
      value: gm === ServerStringAsBoolean.true,
      check: gm === ServerStringAsBoolean.true,
    };
    // Converto i DatiTecniciUsiVo in UsoDiLeggeInfo
    dta.usiDiLegge = [];

    // Recupero dal server tutte le informazioni degli usi di legge.
    return this.convertDatiTecniciUsiRicercaToUsiDiLeggeInfoRicerca(usi).pipe(
      map((datiUsi: UsoDiLeggeInfoRicerca[]) => {
        // Aggiungo gli usi di legge all'oggetto
        dta.usiDiLegge = datiUsi || [];
        // Ritorno l'oggetto completo
        return dta;
      })
    );
  }

  /**
   * Funzione che converte un oggetto DatiTecniciUsiVo in una lista di oggetti UsoDiLeggeInfo.
   * @param dtuVo DatiTecniciUsiVo da convertire.
   * @returns Array di UsoDiLeggeInfo convertita.
   */
  convertDatiTecniciUsiRicercaToUsiDiLeggeInfoRicerca(
    dtuVo: DatiTecniciUsiRicercaVo
  ): Observable<UsoDiLeggeInfoRicerca[]> {
    // Verifico l'input
    if (!dtuVo) {
      // Nessuna configurazione
      return of([]);
    }

    // Definisco un array che conterrà le informazioni da ritornare
    const usiDiLeggeInfo: Observable<UsoDiLeggeInfoRicerca>[] = [];

    // Ciclo le proprietà dell'oggetto, e converto le informazioni
    for (let v of Object.values(dtuVo)) {
      // Converto l'oggetto in UsoDiLeggeInfo
      const usoDiLegge =
        this.convertDatiTecniciUsoRicercaToUsoDiLeggeInfoRicerca(v);
      // Aggiungo la chiamata
      usiDiLeggeInfo.push(usoDiLegge);
    }

    // Verifico se ci sono richieste da fare
    if (usiDiLeggeInfo.length > 0) {
      // Ritorno la lista di request
      return forkJoin(usiDiLeggeInfo);
      // #
    } else {
      // Ritorno una lista vuota
      return of([]);
    }
  }

  /**
   * Funzione di conversione da un oggetto DatiTecniciUsoVo ad un oggetto UsoDiLeggeInfo.
   * @param dtuVo DatiTecniciUsoVo da convertire.
   * @returns UsoDiLeggeInfo convertito.
   */
  convertDatiTecniciUsoRicercaToUsoDiLeggeInfoRicerca(
    dtuVo: DatiTecniciUsoRicercaVo
  ): Observable<UsoDiLeggeInfoRicerca> {
    // Verifico l'input
    if (!dtuVo) {
      // Nessuna configurazione
      return;
    }

    // Definisco un oggetto per la conversione
    const usoDiLegge: UsoDiLeggeInfoRicerca = {};

    // Estraggo l'id tipo uso legge dall'oggetto del dato tecnico
    const idTipoUso = dtuVo.id_tipo_uso_legge;
    // Verifico se esiste l'id effettivamente
    if (idTipoUso == undefined) {
      // Segnalo in console un errore specifico
      this.reportDTUsoIdError(dtuVo);
      // #
    }

    // Recupero l'oggetto uso di legge principale
    return this._datiTecnici.getUsoDiLegge(idTipoUso).pipe(
      switchMap((usoLegge: IUsoLeggeVo) => {
        // Creo un oggetto che conterrà le informazioni per gli usi di questo uso di legge
        const usiReq: IUsiAmbienteReq = {
          usoLegge: of(usoLegge),
          usiSpecifici: of([]),
          riduzioni: of([]),
          aumenti: of([]),
        };

        // Ottengo gli observable per avere tutti gli usi di legge specifici
        usiReq.usiSpecifici = this.getUsiDiLeggeSpecifici(dtuVo.uso_specifico);
        // Ottengo gli observable per avere tutte le riduzioni
        usiReq.riduzioni = this.getRiduzioni(dtuVo.riduzione);
        // Ottengo gli observable per avere tutti gli aumenti
        usiReq.aumenti = this.getAumenti(dtuVo.aumento);
        // Ritorno la fork di tutte le request per gli usi di legge specifici
        return forkJoin(usiReq);
        // #
      }),
      map((usiRes: IUsiAmbienteRes) => {
        // Estraggo le informazioni
        const dettaglioUsoLegge = usiRes.usoLegge;
        const dettaglioUsiSpec = usiRes.usiSpecifici;

        // Assegno le proprietà
        usoDiLegge.usoDiLegge = dettaglioUsoLegge;
        usoDiLegge.usiDiLeggeSpecifici = dettaglioUsiSpec;

        usoDiLegge.unitaDiMisura = {
          des_unita_misura: dtuVo.unita_di_misura,
        };
        usoDiLegge.quantitaDa = this.notMenoUno(dtuVo.qta_acqua_da);
        usoDiLegge.quantitaA = this.notMenoUno(dtuVo.qta_acqua_a);
        usoDiLegge.dataScadenzaEmasIsoDa =
          this._riscaUtilities.convertServerDateToNgbDateStruct(
            dtuVo.data_scadenza_emas_iso_da
          );
        usoDiLegge.dataScadenzaEmasIsoA =
          this._riscaUtilities.convertServerDateToNgbDateStruct(
            dtuVo.data_scadenza_emas_iso_a
          );
        // Ritorno l'oggetto
        return usoDiLegge;
      })
    );
  }

  /**
   * ##########################
   * SCARICO DATI SPECIFICI USI
   * ##########################
   */

  /**
   * Ottiene gli usi di legge specifici in base ai loro codici
   * @param usiSpecifici array di codici degli usi di legge specifici
   * @returns observable con gli usi di legge
   */
  private getUsiDiLeggeSpecifici(
    usiSpecifici: string[] = []
  ): Observable<IUsoLeggeVo[]> {
    // Verifico che esistano le informazioni (almeno 1 elemento)
    if (usiSpecifici.length > 0) {
      // Creo un array di request
      const ulsReq: Observable<IUsoLeggeVo>[] = [];
      // Scarico ogni istanza del dato
      usiSpecifici.forEach((uls) => {
        // Aggiungo la chiamata
        ulsReq.push(this._datiTecnici.getUsoDiLegge(uls));
        // #
      });
      // Aggiungo l'array di request all'oggetto per gli usi
      return forkJoin(ulsReq);
      // #
    } else {
      // Ritorno una lista vuota per gli usi di legge specifici
      return of([]);
    }
  }

  /**
   * Ottiene gli aumenti in base ai loro codici
   * @param aumenti array di codici degli aumenti
   * @returns observable con gli aumenti
   */
  private getAumenti(
    aumenti: DatiTecniciAumentoVo[] = []
  ): Observable<RiduzioneAumentoVo[]> {
    // Verifico che esistano le informazioni (almeno 1 elemento)
    if (aumenti.length > 0) {
      // Creo un array di request
      const uaReq: Observable<any>[] = [];
      // Aggiungo la chiamata
      aumenti.forEach((ua) => {
        // Aggiungo la chiamata
        uaReq.push(this._datiTecnici.getPercentualeAumento(ua.id_aumento));
        // #
      });
      // Aggiungo l'array di request all'oggetto per gli aumenti
      return forkJoin(uaReq);
    } else {
      return of([]);
    }
  }

  /**
   * Ottiene le riduzioni in base ai loro codici
   * @param riduzioni array di codici delle riduzioni
   * @returns observable con le riduzioni
   */
  private getRiduzioni(
    riduzioni: DatiTecniciRiduzioneVo[] = []
  ): Observable<RiduzioneAumentoVo[]> {
    // Verifico che esistano le informazioni (almeno 1 elemento)
    if (riduzioni.length > 0) {
      // Creo un array di request
      const uaReq: Observable<any>[] = [];
      // Aggiungo la chiamata
      riduzioni.forEach((ua) => {
        // Aggiungo la chiamata
        uaReq.push(this._datiTecnici.getPercentualeRiduzione(ua.id_riduzione));
        // #
      });
      // Aggiungo l'array di request all'oggetto per le riduzioni
      return forkJoin(uaReq);
    } else {
      return of([]);
    }
  }

  /**
   * ##############################################################
   * FUNZIONI DI CONVERSIONE: StatoDebitorio => DatiTecniciAmbiente
   * ##############################################################
   */

  /**
   * Funzione di conversione da un oggetto praticaDTVo/statoDebitorio ad un oggetto DatiTecniciAmbiente.
   * La funzione verificherà se esistono i dati per le annualità e ogni singolo loro uso. Se esistono verranno uniti insieme per preparare i dati di riepilogo.
   * @param praticaDTVo PraticaDTVo da convertire.
   * @param statoDebitorio StatoDebitorioVo con i dati delle annualità e gli usi da convertire.
   * @returns DatiTecniciAmbiente convertito.
   */
  convertStatoDebitorioRiepilogoToDatiTecniciAmbiente(
    praticaDTVo: PraticaDTVo,
    statoDebitorio: StatoDebitorioVo
  ): Observable<IDatiTecniciAmbiente> {
    // Loggo i dati tecnici
    const t = 'STATO DEBITORIO - RIEPILOGO';
    this._logger.datiTecnici(t, '', true);

    // Compongo una doppia request per conversione/scarico dati per dati tecnici e stato debitorio
    const req: ISDRiepilogoReq = {
      dtPratica: this.convertPraticaDTVoToDatiTecniciAmbiente(praticaDTVo),
      dtAnnualita: this.datiTecniciAmbienteAnnualitaSD(statoDebitorio),
    };

    // Richiamo per convertire le informazioni del dato tecnico di base
    return forkJoin(req).pipe(
      map((res: ISDRiepilogoRes) => {
        // Estraggo dalla response le informazioni
        const dtPratica = res.dtPratica;
        const dtAnn = res.dtAnnualita;
        // Richiamo al funzione di controllo e gestione dei dati tecnici
        return this.onConvertSDRiepilogoToDTAmbiente(dtPratica, dtAnn);
        // #
      })
    );
  }

  /**
   * Funzione di comodo che recupera i dati tecnici ambiente all'interno di uno stato debitorio, innestati nei vari array delle annualità.
   * Se non esiste qualche livello d'oggetto verrà ritornato array vuoto.
   * @param statoDebitorio StatoDebitorioVo per la conversione dei dati tecnici ambiente delle annualità.
   * @returns Observable<DatiTecniciAmbiente[]> con la lista dei dati tecnici scaricati per le annualità.
   */
  private datiTecniciAmbienteAnnualitaSD(
    statoDebitorio: StatoDebitorioVo
  ): Observable<IDatiTecniciAmbiente[]> {
    // Verifico l'input
    if (!statoDebitorio) {
      // Niente da convertire
      return of([]);
    }

    // Esiste lo stato debitorio, recupero tutte le annualita
    const annualita = statoDebitorio.annualita_sd ?? [];
    // Ritorno la forkjoin delle chiamate
    return this.datiTecniciAmbienteAnnualita(annualita);
  }

  /**
   * Funzione di comodo che recupera i dati tecnici ambiente delle annualità.
   * Se non esiste qualche livello d'oggetto verrà ritornato array vuoto.
   * @param annualita AnnualitaSDVo[] per la conversione dei dati tecnici ambiente delle annualità.
   * @returns Observable<DatiTecniciAmbiente[]> con la lista dei dati tecnici scaricati per le annualità.
   */
  private datiTecniciAmbienteAnnualita(
    annualita: AnnualitaSDVo[]
  ): Observable<IDatiTecniciAmbiente[]> {
    // Verifico l'input
    if (!annualita) {
      // Niente da convertire
      return of([]);
    }

    // Per ogni annualità recupero tutti il dato tecnico
    const jsDTAnnualita: string[] = annualita.map((a: AnnualitaSDVo) => {
      // Estraggo gli i dati annualità/usi
      return a.json_dt_annualita_sd;
    });

    // Loggo i dati tecnici
    const t = 'GESTIONE ANNUALITA';
    this._logger.datiTecnici(t, '', true);

    // Rimappo i dati tecnici come un array di datiAmbiente da convertire
    const dtAnnualita: Observable<IDatiTecniciAmbiente>[] = [];
    // Ciclo le mappatura delle annualità
    jsDTAnnualita.forEach((jsDT: string) => {
      // Richiamo la funzione di conversione per i dati tecnici
      dtAnnualita.push(this.convertDatiTecniciCoreToDatiTecniciAmbiente(jsDT));
    });

    // Verifico se esistono richieste
    if (dtAnnualita.length === 0) {
      // Non esistono dati
      return of([]);
    }

    // Ritorno la forkjoin delle chiamate
    return forkJoin(dtAnnualita);
  }

  /**
   * Funzione che gestisce le informazioni per i dati tecnici ambiente per il riepilogo dello stato debitorio.
   * I dati generali verranno estratti dai dati tecnici della pratica, mentre gli usi di legge verranno recuperati da tutte le annualità.
   * Per una gestione più chiara, gli usi poi verranno filtrati per ottenere solo valori unici.
   * @param dtPratica DatiTecniciAmbiente con le informazioni dei dati tecnici per la pratica.
   * @param dtAnnualita DatiTecniciAmbiente[] con le informazioni dei dati tecnici per lo stato debitorio/annualità.
   * @returns DatiTecniciAmbiente con le informazioni finali di riepilogo.
   */
  private onConvertSDRiepilogoToDTAmbiente(
    dtPratica: IDatiTecniciAmbiente,
    dtAnnualita: IDatiTecniciAmbiente[]
  ): IDatiTecniciAmbiente {
    // Verifico l'input
    if (!dtAnnualita || dtAnnualita.length == 0) {
      // Ritorno il dati tecnico della pratica
      return dtPratica;
    }

    // Creo un oggetto DatiTecniciAmbiente vuoto, da riempire con le informazioni
    const dtA: IDatiTecniciAmbiente = {};
    // Definisco le informazioni generali prendendoli dalla pratica
    dtA.comune = dtPratica.comune;
    dtA.corpoIdricoCaptazione = dtPratica.corpoIdricoCaptazione;
    dtA.gestioneManuale = dtPratica.gestioneManuale;
    dtA.nomeImpiantoIdroElettrico = dtPratica.nomeImpiantoIdroElettrico;
    dtA.portataDaAssegnare = dtPratica.portataDaAssegnare;

    // Per gli usi di legge gli usi di legge delle annualità hanno priorità
    dtA.usiDiLegge = this.usiLeggePraticaAnnualita(dtAnnualita, dtPratica);

    // Ritorno l'oggetto del dato tecnico
    return dtA;
  }

  /**
   * Funzione che gestisce le informazioni per gli usi di legge per il riepilogo stato debitorio.
   * Gli usi di legge verranno recuperati da tutte le annualità.
   * Per una gestione più chiara, gli usi poi verranno filtrati per ottenere solo valori unici.
   * @param dtAnnualita DatiTecniciAmbiente[] con le informazioni dei dati tecnici per lo stato debitorio/annualità.
   * @param dtPratica DatiTecniciAmbiente con le informazioni dei dati tecnici per la pratica.
   * @returns DatiTecniciAmbiente con le informazioni finali di riepilogo.
   */
  private usiLeggePraticaAnnualita(
    dtAnnualita: IDatiTecniciAmbiente[],
    dtPratica?: IDatiTecniciAmbiente
  ): UsoLeggePSDAmbienteInfo[] {
    // Verifico l'input
    if (!dtAnnualita) {
      // Ritorno gli usi della pratica
      return dtPratica?.usiDiLegge ?? [];
    }

    // Estraggo da tutti i dati tecnici gli usi ed effuetto un flatten dei dati, così da ottenere una matrice di usi
    let matrixUsiAnn: UsoLeggePSDAmbienteInfo[][];
    matrixUsiAnn = dtAnnualita.map((dtA: IDatiTecniciAmbiente) => {
      // Ritorno l'array degli usi di legge
      return dtA.usiDiLegge;
    });
    // Effettuo un "flatten", per trasformare la matrice in un array di dati
    const usiAnn: UsoLeggePSDAmbienteInfo[] = flatten(matrixUsiAnn);

    // Effettuo un filtro di unicità sui dati, in maniera tale da togliere tutti i doppioni
    const usiAnnUniq: UsoLeggePSDAmbienteInfo[] = uniqWith(
      usiAnn,
      (usoA: UsoLeggePSDAmbienteInfo, usoB: UsoLeggePSDAmbienteInfo) => {
        // Recupero le informazioni per la comparazione sui codici uso
        const codA = usoA?.usoDiLegge?.cod_tipo_uso;
        const codB = usoB?.usoDiLegge?.cod_tipo_uso;
        // Effettuo un compare tra i codici uso
        return codA === codB;
      }
    );

    // Ritorno la lista di usi unici
    return usiAnnUniq;
  }

  /**
   * ###########################################################
   * FUNZIONI DI CONVERSIONE: DatiTecniciAmbiente => PraticaDTVo
   * ###########################################################
   */

  /**
   * Funzione che converte un oggetto DatiTecniciAmbiente in un oggetto PraticaDTVo.
   * @param dta DatiTecniciAmbiente da convertire.
   * @returns PraticaDTVo convertito.
   */
  convertDatiTecniciAmbienteToPraticaDTVo(
    pdtVo: PraticaDTVo,
    dta: IDatiTecniciAmbiente
  ): PraticaDTVo {
    // Verifico l'input
    if (!dta) {
      // Nessuna conversione
      return undefined;
    }

    // Verifico se è stata definita una pratica di partenza
    pdtVo = pdtVo !== undefined ? pdtVo : ({} as any);
    // Lancio la conversione delle informazioni
    const dtVo = this.convertDatiTecniciAmbienteToDatiTecniciVo(dta);
    // Stringhizzo i dati tecnici
    const dt = JSON.stringify(dtVo);

    // Faccio un log dei dati tecnici come check preventivo
    const d = 'DatiTecniciAmbiente => PraticaDTVo';
    this.logDatiTecnici(dt, d);

    // Una volta generati i dati, unisco le informazioni
    const praticaDTVo: PraticaDTVo = {
      riscossione: {
        gest_UID: pdtVo?.riscossione?.gest_UID,
        id_riscossione: pdtVo?.riscossione?.id_riscossione,
        data_modifica: pdtVo?.riscossione?.data_modifica,
        data_inserimento: pdtVo?.riscossione?.data_inserimento,
        dati_tecnici: dt,
      },
    };

    // Ritorno l'oggetto generato
    return praticaDTVo;
  }

  /**
   * Funzione di convert da oggetto DatiTecniciAmbiente a oggetto DatiTecniciVo.
   * @param datiTecniciAmbiente DatiTecniciAmbiente da convertire.
   * @returns DatiTecniciVo generato.
   */
  convertDatiTecniciAmbienteToDatiTecniciVo(
    datiTecniciAmbiente: IDatiTecniciAmbiente
  ): DatiTecniciAmbienteVo {
    // Verifico che l'oggetto in input esista
    if (!datiTecniciAmbiente) {
      throw new Error('datiTecniciAmbiente not defined');
    }

    // Genero i dati generili
    const datiTecniciGeneraliVo: DatiTecniciGeneraliAmbienteVo =
      this.convertDatiTecniciAmbienteToDatiTecniciGeneraliVo(
        datiTecniciAmbiente
      );
    // Definisco i dati di uso
    const datiTecniciUsiVo: DatiTecniciUsiAmbienteVo =
      this.convertUsiDiLeggeRowToDatiTecniciUsiVo(
        datiTecniciAmbiente.usiDiLegge
      );

    // Creo e ritorno l'oggetto DatiTecniciVo
    return new DatiTecniciAmbienteVo(datiTecniciGeneraliVo, datiTecniciUsiVo);
  }

  /**
   * Funzione di convert da DatiTecniciAmbiente a DatiTecniciGeneraliAmbienteVo.
   * @param datiTecniciAmbiente DatiTecniciAmbiente da convertire.
   * @returns DatiTecniciGeneraliAmbienteVo convertito.
   */
  convertDatiTecniciAmbienteToDatiTecniciGeneraliVo(
    datiTecniciAmbiente: IDatiTecniciAmbiente
  ): DatiTecniciGeneraliAmbienteVo {
    // Verifico che l'input esista
    if (!datiTecniciAmbiente) {
      return undefined;
    }

    // Variabile di comodo
    const check = datiTecniciAmbiente.gestioneManuale?.check;

    // Creo un nuovo oggetto e lo ritorno
    return new DatiTecniciGeneraliAmbienteVo(
      datiTecniciAmbiente.corpoIdricoCaptazione,
      datiTecniciAmbiente.comune,
      datiTecniciAmbiente.nomeImpiantoIdroElettrico,
      datiTecniciAmbiente.portataDaAssegnare,
      check ? ServerStringAsBoolean.true : ServerStringAsBoolean.false
    );
  }

  /**
   * Funzione di convert da un array di UsoDiLeggeInfo ad un oggetto DatiTecniciUsiVo.
   * @param usiDiLeggeRow Array di UsoDiLeggeInfo da convertire.
   * @returns DatiTecniciUsiVo convertito.
   */
  convertUsiDiLeggeRowToDatiTecniciUsiVo(
    usiDiLeggeRow: UsoLeggePSDAmbienteInfo[]
  ): DatiTecniciUsiAmbienteVo {
    // Verifico che l'input esista
    if (!usiDiLeggeRow) {
      return {};
    }

    // Definisco i dati di uso
    const datiTecniciUsiVo: DatiTecniciUsiAmbienteVo = {};

    // Scorro i dati degli usi dentro i dati tecnici ambiente
    for (let i = 0; i < usiDiLeggeRow.length; i++) {
      // Recupero un uso di legge
      const usoDiLeggeRow = usiDiLeggeRow[i];
      // Definisco il nome dell'oggetto che conterrà i dati
      const nomeUso = this.sanitizeDatiTecniciUsiVoProperty(
        usoDiLeggeRow?.usoDiLegge?.cod_tipo_uso
      );
      // Compilo l'oggetto richiamando la funzione di convert
      if (nomeUso != null) {
        datiTecniciUsiVo[nomeUso] =
          this.convertUsoDiLeggeRowToDatiTecniciUsoVo(usoDiLeggeRow);
      }
    }

    // Ritorno l'oggetto compilato
    return datiTecniciUsiVo;
  }

  /**
   * Funzione di supporto che sanitizza le proprietà per DatiTecniciUsiVo.
   * Gli spazi bianchi e gli "-" verranno sostituiti con "_".
   * @param property string nome della property da sanitizzare.
   * @returns string property sanitizzata.
   */
  sanitizeDatiTecniciUsiVoProperty(property: string): string {
    // Verifico che la property sia definita
    if (!property) {
      return property;
    }

    // Dichiaro una variabile contenitore
    let propertySanitize = '';
    // Effettuo la replace degli spazi vuoti
    propertySanitize = property.replace(' ', '_');
    // Effettuo la replace di caratteri speciali
    propertySanitize = propertySanitize.replace(
      this.riscaRegExp.usoLeggeProperty,
      '_'
    );

    // Ritorno la proprietà sanitizzata
    return propertySanitize;
  }

  /**
   * Funzione di convert da UsoDiLeggeInfo ad un oggetto DatiTecniciUsoVo.
   * @param usoDiLeggeRow UsoDiLeggeInfo da convertire.
   * @returns DatiTecniciUsoVo convertito.
   */
  convertUsoDiLeggeRowToDatiTecniciUsoVo(
    usoDiLeggeRow: UsoLeggePSDAmbienteInfo
  ): DatiTecniciUsoAmbienteVo {
    // Verifico che l'input esista
    if (!usoDiLeggeRow) {
      // Non si può convertire, ma è un errore!
      throw new Error('usoDiLeggeRow not defined.');
    }

    // Variabili di comodo
    const usiSpec = usoDiLeggeRow.usiDiLeggeSpecifici || [];
    // Recupero la lista degli usi specifici
    const usiSpecificiVo = usiSpec.map((us) => us.cod_tipo_uso);

    // Recupero e formatto le riduzioni
    const riduzioniVo = this.convertRiduzioniVoToDatiTecniciRiduzioniVo(
      usoDiLeggeRow.percRiduzioni
    );
    // Recupero e formatto gli aumenti
    const aumentiVo = this.convertAumentiVoToDatiTecniciAumentiVo(
      usoDiLeggeRow.percAumenti
    );
    // Definisco la data scadenza
    let dataScadEMIS: any = usoDiLeggeRow.dataScadenzaEmasIso ?? '';
    // Verifico che esista la data scadenza emas iso
    if (dataScadEMIS) {
      // Definisco il formato server
      const SERVER = RiscaFormatoDate.server;
      // Converto la data
      dataScadEMIS = this._riscaUtilities.convertNgbDateStructToDateString(
        dataScadEMIS,
        SERVER
      );
    }

    // Aggiungo ai dati tecnici usi la composizione dei dati
    return new DatiTecniciUsoAmbienteVo(
      usoDiLeggeRow.usoDiLegge.id_tipo_uso,
      usoDiLeggeRow.usoDiLegge.des_tipo_uso,
      usiSpecificiVo,
      usoDiLeggeRow.unitaDiMisura.sigla_unita_misura,
      usoDiLeggeRow.quantita,
      usoDiLeggeRow.quantitaFaldaProfonda,
      usoDiLeggeRow.percFaldaProfonda,
      usoDiLeggeRow.quantitaFaldaSuperficiale,
      usoDiLeggeRow.percFaldaSuperficiale,
      riduzioniVo,
      aumentiVo,
      dataScadEMIS
    );
  }

  /**
   * Funzione di convert da array di RiduzioneAumentoVo ad array di DatiTecniciRiduzioneVo.
   * @param riduzioniVo Array RiduzioneAumentoVo da convertire.
   * @returns Array DatiTecniciRiduzioneVo convertito.
   */
  convertRiduzioniVoToDatiTecniciRiduzioniVo(
    riduzioniVo: RiduzioneAumentoVo[]
  ): DatiTecniciRiduzioneVo[] {
    // Verifico che l'input esista
    if (!riduzioniVo) {
      return [];
    }
    // Ritorno la mappatura degli oggetti parsati
    return riduzioniVo.map((riduzioneVo: RiduzioneAumentoVo) =>
      this.convertRiduzioneVoToDatiTecniciRiduzioneVo(riduzioneVo)
    );
  }

  /**
   * Funzione di convert da RiduzioneAumentoVo a DatiTecniciRiduzioneVo.
   * @param riduzioneVo RiduzioneAumentoVo da convertire.
   * @returns DatiTecniciRiduzioneVo convertito.
   */
  convertRiduzioneVoToDatiTecniciRiduzioneVo(
    riduzioneVo: RiduzioneAumentoVo
  ): DatiTecniciRiduzioneVo {
    // Verifico che l'input esista
    if (!riduzioneVo) {
      return undefined;
    }
    // Converto l'oggetto e lo ritorno
    return new DatiTecniciRiduzioneVo(
      riduzioneVo.id_riduzione_aumento,
      riduzioneVo.des_riduzione_aumento,
      Number(riduzioneVo.perc_riduzione_aumento)
    );
  }

  /**
   * Funzione di convert da array di RiduzioneAumentoVo ad array di DatiTecniciAumentoVo.
   * @param aumentiVo Array RiduzioneAumentoVo da convertire.
   * @returns Array DatiTecniciAumentoVo convertito.
   */
  convertAumentiVoToDatiTecniciAumentiVo(
    aumentiVo: RiduzioneAumentoVo[]
  ): DatiTecniciAumentoVo[] {
    // Verifico che l'input esista
    if (!aumentiVo) {
      return [];
    }
    // Ritorno la mappatura degli oggetti parsati
    return aumentiVo.map((aumentoVo: RiduzioneAumentoVo) =>
      this.convertAumentoVoToDatiTecniciAumentoVo(aumentoVo)
    );
  }

  /**
   * Funzione di convert da RiduzioneAumentoVo a DatiTecniciAumentoVo.
   * @param aumentoVo RiduzioneAumentoVo da convertire.
   * @returns DatiTecniciRiduzioneVo convertito.
   */
  convertAumentoVoToDatiTecniciAumentoVo(
    aumentoVo: RiduzioneAumentoVo
  ): DatiTecniciAumentoVo {
    // Verifico che l'input esista
    if (!aumentoVo) {
      return undefined;
    }
    // Converto l'oggetto e lo ritorno
    return new DatiTecniciAumentoVo(
      aumentoVo.id_riduzione_aumento,
      aumentoVo.des_riduzione_aumento,
      Number(aumentoVo.perc_riduzione_aumento)
    );
  }

  /**
   * ################################
   * FUNZIONI DI UTILITY DEL SERVIZIO
   * ################################
   */

  /**
   * Funzione di comodo di verifica per la gestione del valore -1.
   * Se il valore passato è -1, verrà convertito in null.
   * @param v any da verificare.
   */
  private notMenoUno(v: any) {
    // Verifico che il valore esista
    const existV = v !== undefined && v !== null;
    if (!existV || v === -1) {
      // Ritorno null
      return null;
    }
    // Ritorno il valore
    return v;
  }

  /**
   * Funzione di supporto per il log specifico dell'errore dovuto al dato tecnico uso che risulta senza id tipo uso legge.
   * @param dtuVo DatiTecniciUsoAmbienteVo | DatiTecniciUsoRicercaVo con il dettaglio informativo del dato tecnico.
   */
  private reportDTUsoIdError(
    dtuVo: DatiTecniciUsoAmbienteVo | DatiTecniciUsoRicercaVo
  ) {
    // Definisco le informazioni per il primo log dell'errore
    const t1 = `[RISCA] DATI TECNICI AMBIENTE`;
    const b1 = `Non esiste la proprietà: 'id_tipo_uso_legge' per il caricamento dei dati`;
    // Definisco le informazioni per il primo log dell'errore
    const t2 = `[RISCA] DETTAGLIO: DatiTecniciUsoAmbienteVo`;
    const b2 = dtuVo;
    // Loggo gli errori
    this._logger.error(t1, b1);
    this._logger.error(t2, b2);
  }

  /**
   * Funzione di comodo per il log dei dati tecnici.
   * @param dt any con le informazioni del dato tecnico da loggare.
   * @param title string che definisce un titolo descrittivo della funzione chiamante.
   */
  private logDatiTecnici(dt: any, title?: string) {
    // Verifico il titolo
    title = title ? title : 'logDatiTecnici';
    // Loggo i dati tecnici
    this._logger.datiTecnici(title, dt);
  }
}
