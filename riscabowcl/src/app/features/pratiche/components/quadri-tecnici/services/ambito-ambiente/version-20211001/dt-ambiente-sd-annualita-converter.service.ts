import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AnnualitaSDVo } from '../../../../../../../core/commons/vo/annualita-sd-vo';
import { AnnualitaUsoSDVo } from '../../../../../../../core/commons/vo/annualita-uso-sd-vo';
import { PraticaDTVo } from '../../../../../../../core/commons/vo/pratica-vo';
import { RiduzioneAumentoVo } from '../../../../../../../core/commons/vo/riduzione-aumento-vo';
import { UsoRiduzioneAumentoVo } from '../../../../../../../core/commons/vo/uso-riduzione-aumento-vo';
import { LoggerService } from '../../../../../../../core/services/logger.service';
import { RiscaUtilitiesService } from '../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { ServerStringAsBoolean } from '../../../../../../../shared/utilities';
import { DatiTecniciAmbienteSDConsts } from '../../../../../consts/quadri-tecnici/dati-tecnici-ambiente-sd/dati-tecnici-ambiente-sd.consts';
import {
  IDatiTecniciAmbiente,
  IDTAmbienteASD,
  UsoLeggePSDAmbienteInfo,
} from '../../../utilities/interfaces/dt-ambiente-pratica.interfaces';
import {
  DatiTecniciAmbienteVo,
  DatiTecniciGeneraliAmbienteVo,
  DatiTecniciUsiAmbienteVo,
} from '../../../utilities/vo/dati-tecnici-ambiente-vo';
import { DatiTecniciAmbienteConverterService } from './dati-tecnici-ambiente-converter.service';

@Injectable({ providedIn: 'root' })
export class DTAmbienteSDAnnualitaConverterService {
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTA_SD_C = new DatiTecniciAmbienteSDConsts();

  /**
   * Costruttore
   */
  constructor(
    private _dtaConverter: DatiTecniciAmbienteConverterService,
    private _logger: LoggerService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {}

  /**
   * #####################################################
   * FUNZIONI DI CONVERSIONE: PraticaDTVo => DTAmbienteASD
   * #####################################################
   */

  /**
   * Funzione di conversione da un oggetto PraticaDTVo ad un oggetto DTAmbienteASD.
   * @param praticaDTVo PraticaDTVo da convertire.
   * @returns DTAmbienteASD convertito.
   */
  convertPraticaDTVoToDatiTecniciAmbienteSD(
    praticaDTVo: PraticaDTVo
  ): Observable<IDTAmbienteASD> {
    // Verifico l'input
    if (!praticaDTVo) {
      // Niente da convertire
      return of(undefined);
    }

    // Richiamo la funzione del servizio di conversione pratica
    return this._dtaConverter
      .convertPraticaDTVoToDatiTecniciAmbiente(praticaDTVo)
      .pipe(
        map((dta: IDatiTecniciAmbiente) => {
          // Verifico il ritorno
          if (!dta) {
            // La configurazione non è stato effettuato
            return undefined;
          }

          // Converto l'oggetto DatiTecniciAmbiente in DTAmbienteASD
          const dtaASD: IDTAmbienteASD = {
            comune: dta.comune ?? '',
            corpoIdricoCaptazione: dta.corpoIdricoCaptazione ?? '',
            nomeImpiantoIdroElettrico: dta.nomeImpiantoIdroElettrico ?? '',
            portataDaAssegnare: dta.portataDaAssegnare,
            gestioneManuale: dta.gestioneManuale,
            usiDiLegge: dta.usiDiLegge,
            // rateoPrimaAnnualita: undefined, // Queste informazioni in inserimento non sono definite.
            // dataInizio: undefined, // Queste informazioni in inserimento non sono definite.
            // numeroMesi: undefined, // Queste informazioni in inserimento non sono definite.
            // canoneAnnualita: undefined, // Queste informazioni in inserimento non sono definite.
          };

          // Ritorno l'oggetto DTAmbienteASD
          return dtaASD;
          // #
        })
      );
  }

  /**
   * #####################################################
   * FUNZIONI DI CONVERSIONE: PraticaDTVo => DTAmbienteASD
   * #####################################################
   */

  /**
   * Funzione di convert da oggetto DTAmbienteASD a oggetto DatiTecniciVo.
   * @param dtaASD DTAmbienteASD da convertire.
   * @returns DatiTecniciVo generato.
   */
  convertDTAmbienteASDToDatiTecniciVo(
    dtaASD: IDTAmbienteASD
  ): DatiTecniciAmbienteVo {
    // Verifico che l'oggetto in input esista
    if (!dtaASD) {
      throw new Error('DTAmbienteASD not defined');
    }

    // Genero i dati generili
    const datiTecniciGeneraliVo: DatiTecniciGeneraliAmbienteVo =
      this.convertDTAmbienteASDToDatiTecniciGeneraliVo(dtaASD);
    // Definisco i dati di uso
    const datiTecniciUsiVo: DatiTecniciUsiAmbienteVo =
      this._dtaConverter.convertUsiDiLeggeRowToDatiTecniciUsiVo(
        dtaASD.usiDiLegge
      );

    // Creo e ritorno l'oggetto DatiTecniciVo
    return new DatiTecniciAmbienteVo(datiTecniciGeneraliVo, datiTecniciUsiVo);
  }

  /**
   * Funzione di convert da DatiTecniciAmbiente a DatiTecniciGeneraliAmbienteVo.
   * @param dtaASD DatiTecniciAmbiente da convertire.
   * @returns DatiTecniciGeneraliAmbienteVo convertito.
   */
  convertDTAmbienteASDToDatiTecniciGeneraliVo(
    dtaASD: IDTAmbienteASD
  ): DatiTecniciGeneraliAmbienteVo {
    // Verifico che l'input esista
    if (!dtaASD) {
      return undefined;
    }

    // Variabile di comodo
    const check = dtaASD.gestioneManuale?.check;

    // Creo un nuovo oggetto e lo ritorno
    return new DatiTecniciGeneraliAmbienteVo(
      dtaASD.corpoIdricoCaptazione,
      dtaASD.comune,
      dtaASD.nomeImpiantoIdroElettrico,
      dtaASD.portataDaAssegnare,
      check ? ServerStringAsBoolean.true : ServerStringAsBoolean.false
    );
  }

  /**
   * #######################################################
   * FUNZIONI DI CONVERSIONE: AnnualitaSDVo => DTAmbienteASD
   * #######################################################
   */

  /**
   * Funzione di conversione da un oggetto AnnualitaSDVo ad un oggetto DTAmbienteASD.
   * @param annualitaSDVo AnnualitaSDVo da convertire.
   * @returns DTAmbienteASD convertito.
   */
  convertAnnualitaSDVoToDTAmbienteASD(
    annualitaSDVo: AnnualitaSDVo
  ): Observable<IDTAmbienteASD> {
    // Verifico l'input
    if (!annualitaSDVo) {
      // Niente da convertire
      return of(undefined);
    }

    // Estraggo dall'annualità i dati principali
    const {
      anno,
      json_dt_annualita_sd,
      id_componente_dt,
      flg_rateo_prima_annualita,
      data_inizio,
      numero_mesi,
      canone_annuo,
      annualita_uso_sd,
    } = annualitaSDVo;

    // Loggo i dati tecnici
    const t = 'GESTIONE ANNUALITA';
    this._logger.datiTecnici(t, '', true);

    // Richiamo la funzione per rigenerare i dati tecnici
    return this._dtaConverter
      .convertDatiTecniciCoreToDatiTecniciAmbiente(
        json_dt_annualita_sd,
        annualita_uso_sd
      )
      .pipe(
        map((dta: IDatiTecniciAmbiente) => {
          // Genero un oggetto DTAmbienteASD
          const dtaASD: IDTAmbienteASD = {};
          // Definisco le proprietà dai dati tecnici
          dtaASD.comune = dta.comune;
          dtaASD.corpoIdricoCaptazione = dta.corpoIdricoCaptazione;
          dtaASD.nomeImpiantoIdroElettrico = dta.nomeImpiantoIdroElettrico;
          dtaASD.portataDaAssegnare = dta.portataDaAssegnare;
          dtaASD.usiDiLegge = dta.usiDiLegge;
          dtaASD.gestioneManuale = dta.gestioneManuale;
          dtaASD.idComponenteDt = id_componente_dt;
          dtaASD.annualita = { anno };
          dtaASD.rateoPrimaAnnualita = {
            id: this.DTA_SD_C.LABEL_FLAG_RATEO_PRIMA_ANNUALITA,
            label: this.DTA_SD_C.LABEL_FLAG_RATEO_PRIMA_ANNUALITA,
            value: flg_rateo_prima_annualita,
            check: flg_rateo_prima_annualita,
          };
          dtaASD.dataInizio =
            this._riscaUtilities.convertMomentToNgbDateStruct(data_inizio);
          dtaASD.numeroMesi = numero_mesi;
          dtaASD.canoneAnnualita = canone_annuo;

          // Ritorno l'oggetto convertito
          return dtaASD;
        })
      );
  }

  /**
   * #######################################################
   * FUNZIONI DI CONVERSIONE: DTAmbienteASD => AnnualitaSDVo
   * #######################################################
   */

  /**
   * Funzione che converte un oggetto DTAmbienteASD in un oggetto AnnualitaSDVo.
   * @param dtaASD DTAmbienteASD da convertire.
   * @returns PraticaDTVo convertito.
   */
  convertDTAmbienteASDToAnnualitaSDVo(dtaASD: IDTAmbienteASD): AnnualitaSDVo {
    // Verifico l'input
    if (!dtaASD) {
      // Nessuna conversione
      return undefined;
    }

    // Creo un oggetto vuoto per l'annualità e definisco le proprietà
    const annualita = new AnnualitaSDVo();

    // Conversione delle informazioni
    const jsonDT = this.convertDTAmbienteASDToDatiTecniciVo(dtaASD);
    const jsonDTString = JSON.stringify(jsonDT);
    const dI = this._riscaUtilities.convertNgbDateStructToMoment(
      dtaASD.dataInizio
    );

    // Aggiungo le informazioni in mio possesso
    annualita.anno = dtaASD.annualita?.anno;
    annualita.json_dt_annualita_sd = jsonDTString;
    annualita.canone_annuo = dtaASD.canoneAnnualita;
    annualita.flg_rateo_prima_annualita = dtaASD.rateoPrimaAnnualita?.check;
    annualita.numero_mesi = dtaASD.numeroMesi;
    annualita.data_inizio = dI;
    annualita.id_componente_dt = dtaASD.idComponenteDt;
    annualita.annualita_uso_sd =
      this.convertDTAmbienteASDToAnnualitaUsiSDVo(dtaASD);

    // Ritorno le informazioni generate
    return annualita;
  }

  /**
   * ############################################################
   * FUNZIONI DI CONVERSIONE: DTAmbienteASD => AnnualitaUsoSDVo[]
   * ############################################################
   */

  /**
   * Funzione che converte un oggetto DTAmbienteASD in un array di oggetti AnnualitaUsoSDVo.
   * @param dtaASD DTAmbienteASD da convertire.
   * @returns AnnualitaUsoSDVo[] convertito.
   */
  convertDTAmbienteASDToAnnualitaUsiSDVo(
    dtaASD: IDTAmbienteASD
  ): AnnualitaUsoSDVo[] {
    // Verifico l'input
    if (!dtaASD) {
      // Nessuna conversione
      return undefined;
    }

    // Recupero dall'oggetto la lista degli usi
    const usiDiLegge = dtaASD.usiDiLegge || [];

    // Rimappo gli usi di legge in un array di annualità uso
    return usiDiLegge.map((u: UsoLeggePSDAmbienteInfo) => {
      // Rimappo le informazioni dell'oggetto
      return this.convertUsoLeggeSDInfoToAnnualitaUsoSDVo(u);
    });
  }

  /**
   * Funzione che converte un oggetto UsoLeggePSDInfo in un oggetto AnnualitaUsoSDVo.
   * @param usoInfo UsoLeggePSDInfo da convertire.
   * @returns AnnualitaUsoSDVo convertito.
   */
  convertUsoLeggeSDInfoToAnnualitaUsoSDVo(
    usoInfo: UsoLeggePSDAmbienteInfo
  ): AnnualitaUsoSDVo {
    // Verifico l'input
    if (!usoInfo) {
      // Nessuna conversione
      return undefined;
    }

    // Creo un oggetto vuoto per l'annualità e definisco le proprietà
    const annualitaUso = new AnnualitaUsoSDVo();

    // Conversione delle informazioni
    const usoRid = usoInfo.percRiduzioni ?? [];
    const usoAum = usoInfo.percAumenti ?? [];
    let usoRidAum = usoRid.concat(usoAum);
    if (usoRidAum !== null && usoRidAum?.length == 0) {
      usoRidAum = null;
    }

    // Aggiungo le informazioni in mio possesso
    annualitaUso.tipo_uso = usoInfo.usoDiLegge;
    annualitaUso.canone_uso = usoInfo.canoneUso;
    annualitaUso.canone_unitario = usoInfo.canoneUnitarioUso;
    annualitaUso.uso_ridaum =
      this.convertRiduzioniAumentiVoToUsiRiduzioneAumentoVo(usoRidAum);

    // Ritorno le informazioni generate
    return annualitaUso;
  }

  /**
   * Funzione di conversione da una lista di oggetti RiduzioneAumentoVo ad una lista di oggetti UsoRiduzioneAumentoVo.
   * @param ridAum RiduzioneAumentoVo[] da convertire.
   * @returns UsoRiduzioneAumentoVo[] convertito.
   */
  convertRiduzioniAumentiVoToUsiRiduzioneAumentoVo(
    ridAum: RiduzioneAumentoVo[]
  ): UsoRiduzioneAumentoVo[] {
    // Verifico l'input
    if (!ridAum || ridAum.length == 0) {
      // Niente da convertire
      return [];
    }

    // Mappo la lista e converto gli oggetti
    return ridAum.map((ra: RiduzioneAumentoVo) => {
      // Converto e ritorno l'oggetto
      return this.convertRiduzioneAumentoVoToUsoRiduzioneAumentoVo(ra);
    });
  }

  /**
   * Funzione di conversione da un oggetto RiduzioneAumentoVo ad un oggetto UsoRiduzioneAumentoVo.
   * @param ridAum RiduzioneAumentoVo da convertire.
   * @returns UsoRiduzioneAumentoVo convertito.
   */
  convertRiduzioneAumentoVoToUsoRiduzioneAumentoVo(
    ridAum: RiduzioneAumentoVo
  ): UsoRiduzioneAumentoVo {
    // Verifico l'input
    if (!ridAum) {
      // Niente da convertire
      return undefined;
    }

    // Creo un oggetto UsoRiduzioneAumentoVo
    const usoRA = new UsoRiduzioneAumentoVo();
    // Assegno le informazioni all'oggetto
    usoRA.id_riduzione_aumento = ridAum.id_riduzione_aumento;
    usoRA.riduzione_aumento = ridAum;

    return usoRA;
  }
}
