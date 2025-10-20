import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AnnualitaSDVo } from 'src/app/core/commons/vo/annualita-sd-vo';
import { AnnualitaUsoSDVo } from 'src/app/core/commons/vo/annualita-uso-sd-vo';
import { RiduzioneAumentoVo } from 'src/app/core/commons/vo/riduzione-aumento-vo';
import { UsoRiduzioneAumentoVo } from 'src/app/core/commons/vo/uso-riduzione-aumento-vo';
import { DatiTecniciTributiConsts } from 'src/app/features/pratiche/consts/quadri-tecnici/dati-tecnici-tributi/dati-tecnici-tributi.consts';
import { RiscaUtilitiesService } from 'src/app/shared/services/risca/risca-utilities/risca-utilities.service';
import { PraticaDTVo } from '../../../../../../../core/commons/vo/pratica-vo';
import { ServerStringAsBoolean } from '../../../../../../../shared/utilities';
import {
  IDTAmbienteASD,
  UsoLeggePSDAmbienteInfo,
} from '../../../utilities/interfaces/dt-ambiente-pratica.interfaces';
import { IDatiTecniciTributi } from '../../../utilities/interfaces/dt-tributi-pratica.interfaces';
import { DatiTecniciGeneraliAmbienteVo } from '../../../utilities/vo/dati-tecnici-ambiente-vo';
import {
  DatiTecniciTributiVo,
  DatiTecniciUsiTributiVo,
} from '../../../utilities/vo/dati-tecnici-tributi-vo';
import { DatiTecniciTributiConverterService } from './dati-tecnici-tributi-converter.service';

@Injectable({ providedIn: 'root' })
export class DTTributiSDAnnualitaConverterService {
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTT_SD_C = new DatiTecniciTributiConsts();

  /**
   * Costruttore
   */
  constructor(
    private _dttConverter: DatiTecniciTributiConverterService,
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
  convertPraticaDTVoToDatiTecniciTributiSD(
    praticaDTVo: PraticaDTVo
  ): Observable<IDTAmbienteASD> {
    // Verifico l'input
    if (!praticaDTVo) {
      // Niente da convertire
      return of(undefined);
    }

    // Richiamo la funzione del servizio di conversione pratica
    return this._dttConverter.convertPraticaDTVoToDatiTecniciTributi(
      praticaDTVo
    );
  }

  /**
   * #####################################################
   * FUNZIONI DI CONVERSIONE: PraticaDTVo => DTAmbienteASD
   * #####################################################
   */

  /**
   * Funzione di convert da oggetto DatiTecniciTributi a oggetto DatiTecniciTributiVo.
   * @param dtaASD DatiTecniciTributi da convertire.
   * @returns DatiTecniciTributiVo generato.
   */
  convertDTAmbienteASDToDatiTecniciVo(
    dtaASD: IDatiTecniciTributi
  ): DatiTecniciTributiVo {
    // Verifico che l'oggetto in input esista
    if (!dtaASD) {
      throw new Error('DTAmbienteASD not defined');
    }

    // Definisco i dati di uso
    const datiTecniciUsiVo: DatiTecniciUsiTributiVo =
      this._dttConverter.convertUsoDiLeggeRowToDatiTecniciUsiVo(dtaASD);

    // Creo e ritorno l'oggetto DatiTecniciTributiVo
    return new DatiTecniciTributiVo(null, datiTecniciUsiVo);
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
      '', // dtaASD.corpoIdricoCaptazione, // => Per l'annualità queste informazioni sono unset
      '', // dtaASD.comune, // => Per l'annualità queste informazioni sono unset
      '', // dtaASD.nomeImpiantoIdroElettrico, // => Per l'annualità queste informazioni sono unset
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
    const { anno, json_dt_annualita_sd, id_componente_dt } = annualitaSDVo;

    // Richiamo la funzione per rigenerare i dati tecnici
    return this._dttConverter
      .convertDatiTecniciCoreToDatiTecniciTributi(json_dt_annualita_sd)
      .pipe(
        map((dta: IDatiTecniciTributi) => {
          // Genero un oggetto DTAmbienteASD
          const dtaASD: IDTAmbienteASD = {};
          // Definisco le proprietà dai dati tecnici
          dtaASD.usiDiLegge = [{ usoDiLegge: dta.uso }];
          dtaASD.idComponenteDt = id_componente_dt;
          dtaASD.annualita = { anno };

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
  convertDTTributiASDToAnnualitaSDVo(
    dtaASD: IDatiTecniciTributi
  ): AnnualitaSDVo {
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

    // Dato fittizio: non esiste l'annualitaUso in questo ambito
    const annualitaUso: AnnualitaUsoSDVo = new AnnualitaUsoSDVo({
      id_annualita_uso_sd: null,
      id_annualita_sd: null,
      tipo_uso: dtaASD.uso,
      canone_uso: 0,
      canone_unitario: 0,
      uso_ridaum: [],
    });

    // Aggiungo le informazioni in mio possesso
    annualita.anno = dtaASD.annualita?.anno;
    annualita.json_dt_annualita_sd = jsonDTString;
    annualita.annualita_uso_sd = [annualitaUso];
    annualita.canone_annuo = 0;

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
    annualitaUso.canone_uso = usoInfo.canoneUso ?? 0;
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
