import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PraticaDTVo } from '../../../../../../../core/commons/vo/pratica-vo';
import { RiduzioneAumentoVo } from '../../../../../../../core/commons/vo/riduzione-aumento-vo';
import { IUsoLeggeVo } from '../../../../../../../core/commons/vo/uso-legge-vo';
import { RiscaUtilitiesService } from '../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { RiscaRegExp } from '../../../../../../../shared/utilities';
import { DatiTecniciTributiConsts } from '../../../../../consts/quadri-tecnici/dati-tecnici-tributi/dati-tecnici-tributi.consts';
import { UsoLeggePSDAmbienteInfo } from '../../../utilities/interfaces/dt-ambiente-pratica.interfaces';
import { IDatiTecniciTributi } from '../../../utilities/interfaces/dt-tributi-pratica.interfaces';
import { DatiTecniciVoRicerca } from '../../../utilities/vo/dati-tecnici-ambiente-ricerca-vo';
import {
  DatiTecniciAumentoVo,
  DatiTecniciRiduzioneVo,
} from '../../../utilities/vo/dati-tecnici-ambiente-vo';
import { DatiTecniciTributiRicerca } from '../../../utilities/vo/dati-tecnici-tributi-ricerca-vo';
import {
  DatiTecniciTributiVo,
  DatiTecniciUsiTributiVo,
  DatiTecniciUsoTributoVo,
} from '../../../utilities/vo/dati-tecnici-tributi-vo';
import { DatiTecniciService } from '../../dati-tecnici/dati-tecnici.service';

@Injectable({ providedIn: 'root' })
export class DatiTecniciTributiConverterService {
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTT_C = new DatiTecniciTributiConsts();
  /** Oggetto RiscaRegExp utilizzate come supporto per la validazione dei campi. */
  private riscaRegExp: RiscaRegExp = new RiscaRegExp();

  /**
   * Costruttore
   */
  constructor(
    private _riscaUtilities: RiscaUtilitiesService,
    private _datiTecnici: DatiTecniciService
  ) {}

  /**
   * ###########################################################
   * FUNZIONI DI CONVERSIONE: PraticaDTVo => DatiTecniciTributi
   * ###########################################################
   */

  /**
   * Funzione di conversione da un oggetto PraticaDTVo ad un oggetto DatiTecniciTributi.
   * @param praticaDTVo PraticaDTVo da convertire.
   * @returns DatiTecniciTributi convertito.
   */
  convertPraticaDTVoToDatiTecniciTributi(
    praticaDTVo: PraticaDTVo
  ): Observable<IDatiTecniciTributi> {
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
    return this.convertDatiTecniciCoreToDatiTecniciTributi(dati_tecnici);
  }

  /**
   * Funzione di conversione da una stringa che definisce i dati tecnici ad un oggetto DatiTecniciTributi.
   * @param datiTecnici string o DatiTecniciTributiVo da convertire.
   * @returns DatiTecniciTributi convertito.
   */
  convertDatiTecniciCoreToDatiTecniciTributi(
    datiTecnici: string | DatiTecniciTributiVo
  ): Observable<IDatiTecniciTributi> {
    // Verifico che esistano i dati tecnici
    if (!datiTecnici) {
      // Niente da convertire
      return of(undefined);
    }

    // Controllo di sicurezza e converto il dato
    const isDTString = typeof datiTecnici === 'string';
    const dt: DatiTecniciTributiVo = isDTString
      ? JSON.parse(datiTecnici as string)
      : datiTecnici;
    // Estraggo le informazioni
    const { usi } = dt;
    // Verifico che esistano gli usi
    if (!usi) {
      // Niente da convertire
      return of(undefined);
    }

    // Prendo il codice del tipo uso
    const cod_tipo_uso = Object.keys(usi)[0];
    // Verifico che esista il codice tipo uso
    if (cod_tipo_uso == undefined) {
      // Niente da convertire
      return of(undefined);
    }

    // Prendo l'uso
    const uso = usi[cod_tipo_uso];

    // Definisco un contenitore per i dati tecnici ambiente
    const dtt: IDatiTecniciTributi = {};

    // Recupero dal server tutte le informazioni degli usi di legge.
    return this.convertDatiTecniciUsiVoToUsiDiLeggeInfo(
      uso?.id_tipo_uso_legge
    ).pipe(
      map((uso: IUsoLeggeVo) => {
        // Controllo se è andata a buon fine
        if (uso) {
          // Aggiungo gli usi di legge all'oggetto
          dtt.uso = uso;
          // Aggiungo la popolazione
          dtt.popolazione = usi[uso.cod_tipo_uso]?.popolazione;
        }
        // Ritorno l'oggetto completo
        return dtt;
      })
    );
  }

  /**
   * Funzione che converte un oggetto DatiTecniciUsiVo in una lista di oggetti UsoDiLeggeInfo.
   * @param id_tipo_uso number da convertire.
   * @returns Array di UsoDiLeggeInfo convertita.
   */
  convertDatiTecniciUsiVoToUsiDiLeggeInfo(
    id_tipo_uso: number
  ): Observable<IUsoLeggeVo> {
    // Verifico l'input
    if (id_tipo_uso == undefined) {
      // Nessuna configurazione
      return of(null);
    }

    // Converto l'oggetto in UsoDiLeggeInfo
    const usoDiLegge = this._datiTecnici.getUsoDiLegge(id_tipo_uso);
    // Ritorno la lista di request
    return usoDiLegge ?? of(null);
    // #
  }

  /**
   * Funzione di conversione da una stringa che definisce i dati tecnici ad un oggetto DatiTecniciTributi.
   * @param datiTecnici string o DatiTecniciVo da convertire.
   * @returns DatiTecniciTributi convertito.
   */
  convertDatiTecniciCoreRicercaToDatiTecniciTributiRicerca(
    datiTecnici: string | DatiTecniciVoRicerca
  ): Observable<DatiTecniciTributiRicerca> {
    // Verifico che esistano i dati tecnici
    if (!datiTecnici) {
      // Niente da convertire
      return of(undefined);
    }

    // Controllo di sicurezza e converto il dato
    const isDTString = typeof datiTecnici === 'string';
    const dt: DatiTecniciTributiVo = isDTString
      ? JSON.parse(datiTecnici as string)
      : datiTecnici;
    // Estraggo le informazioni
    const { usi } = dt;

    // Definisco un contenitore per i dati tecnici ambiente
    const dtt: IDatiTecniciTributi = {};
    // Recupero dal server tutte le informazioni degli usi di legge.
    return this.convertDatiTecniciUsiVoToUsiDiLeggeInfo(
      dtt.uso.id_tipo_uso
    ).pipe(
      map((datiUsi: IUsoLeggeVo) => {
        // Controllo se è andata a buon fine
        if (datiUsi) {
          // Aggiungo gli usi di legge all'oggetto
          dtt.uso = datiUsi;
          // Aggiungo la popolazione
          dtt.popolazione = usi[datiUsi.cod_tipo_uso]?.popolazione;
        }
        // Ritorno l'oggetto completo
        return dtt;
      })
    );
  }

  /**
   * ###########################################################
   * FUNZIONI DI CONVERSIONE: DatiTecniciTributi => PraticaDTVo
   * ###########################################################
   */

  /**
   * Funzione che converte un oggetto DatiTecniciTributi in un oggetto PraticaDTVo.
   * @param dta DatiTecniciTributi da convertire.
   * @returns PraticaDTVo convertito.
   */
  convertDatiTecniciTributiToPraticaDTVo(
    pdtVo: PraticaDTVo,
    dta: IDatiTecniciTributi
  ): PraticaDTVo {
    // Verifico l'input
    if (!dta) {
      // Nessuna conversione
      return undefined;
    }

    // Verifico se è stata definita una pratica di partenza
    pdtVo = pdtVo !== undefined ? pdtVo : ({} as any);
    // Lancio la conversione delle informazioni
    const dtVo = this.convertDatiTecniciTributiToDatiTecniciVo(dta);

    // Una volta generati i dati, unisco le informazioni
    const praticaDTVo: PraticaDTVo = {
      riscossione: {
        gest_UID: pdtVo?.riscossione?.gest_UID,
        id_riscossione: pdtVo?.riscossione?.id_riscossione,
        data_modifica: pdtVo?.riscossione?.data_modifica,
        data_inserimento: pdtVo?.riscossione?.data_inserimento,
        dati_tecnici: JSON.stringify(dtVo),
      },
    };

    // Ritorno l'oggetto generato
    return praticaDTVo;
  }

  /**
   * Funzione di convert da oggetto DatiTecniciTributi a oggetto DatiTecniciVo.
   * @param datiTecniciTributi DatiTecniciTributi da convertire.
   * @returns DatiTecniciVo generato.
   */
  convertDatiTecniciTributiToDatiTecniciVo(
    datiTecniciTributi: IDatiTecniciTributi
  ): DatiTecniciTributiVo {
    // Verifico che l'oggetto in input esista
    if (!datiTecniciTributi) {
      throw new Error('datiTecniciTributi not defined');
    }

    // Definisco i dati di uso
    const datiTecniciUsiVo: DatiTecniciUsiTributiVo =
      this.convertUsiDiLeggeRowToUsiTributiVo(datiTecniciTributi);

    // Creo e ritorno l'oggetto DatiTecniciVo
    return new DatiTecniciTributiVo(undefined, datiTecniciUsiVo);
  }

  /**
   * Funzione di convert da un DatiTecniciTributi ad un oggetto UsiTributiVo.
   * @param datiTecniciTributi Array di DatiTecniciTributi da convertire.
   * @returns UsiTributiVo convertito.
   */
  convertUsiDiLeggeRowToUsiTributiVo(
    datiTecniciTributi: IDatiTecniciTributi
  ): DatiTecniciUsiTributiVo {
    // Verifico che l'input esista
    if (!datiTecniciTributi || !datiTecniciTributi?.uso) {
      return {};
    }

    // Definisco i dati di uso
    const datiTecniciUsiVo: DatiTecniciUsiTributiVo = {};
    // Definisco il nome dell'oggetto che conterrà i dati
    const nomeUso = this.sanitizeDatiTecniciUsiVoProperty(
      datiTecniciTributi.uso.cod_tipo_uso
    );
    // Prendo la popolazione
    const popolazione = datiTecniciTributi.popolazione;
    // Prendo i dati dell'uso
    const { id_tipo_uso, cod_tipo_uso } = datiTecniciTributi.uso;
    // Compilo l'oggetto richiamando la funzione di convert
    if (nomeUso != null && popolazione != null) {
      // Inserisco il nomeuso e la popolazione
      datiTecniciUsiVo[nomeUso] = {
        popolazione,
        id_tipo_uso_legge: id_tipo_uso,
        uso_di_legge: cod_tipo_uso,
      };
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
   * Funzione di convert da UsoDiLeggeInfo ad un oggetto DatiTecniciUsoTributoVo.
   * @param usoDiLeggeRow UsoDiLeggeInfo da convertire.
   * @returns DatiTecniciUsoTributoVo convertito.
   */
  convertUsoDiLeggeRowToDatiTecniciUsoTributiVo(
    usoDiLeggeRow: UsoLeggePSDAmbienteInfo,
    popolazione: number
  ): DatiTecniciUsoTributoVo {
    // Verifico che l'input esista
    if (!usoDiLeggeRow) {
      // Non si può convertire, ma è un errore!
      throw new Error('usoDiLeggeRow not defined.');
    }

    // Aggiungo ai dati tecnici usi la composizione dei dati
    return new DatiTecniciUsoTributoVo(
      usoDiLeggeRow.usoDiLegge.id_tipo_uso,
      usoDiLeggeRow.usoDiLegge.des_tipo_uso,
      popolazione
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
   * Funzione di convert da un array di UsoDiLeggeInfo ad un oggetto DatiTecniciUsiTributiVo.
   * @param usiDiLeggeRow Array di UsoDiLeggeInfo da convertire.
   * @returns DatiTecniciUsiTributiVo convertito.
   */
  convertUsoDiLeggeRowToDatiTecniciUsiVo(
    dtt: IDatiTecniciTributi
  ): DatiTecniciUsiTributiVo {
    // Verifico che l'input esista
    if (!dtt?.uso) {
      return {};
    }

    // Definisco i dati di uso
    const datiTecniciUsiVo: DatiTecniciUsiTributiVo = {};

    // Recupero il codice tipo uso
    const codTUso = dtt?.uso?.cod_tipo_uso;
    // Definisco il nome dell'oggetto che conterrà i dati
    const nomeUso = this.sanitizeDatiTecniciUsiVoProperty(codTUso);

    // Compilo l'oggetto richiamando la funzione di convert
    if (nomeUso != null) {
      // Converto l'oggetto
      const usoTVo = this.convertUsoDiLeggeRowToDatiTecniciUsoTributiVo(
        { usoDiLegge: dtt.uso },
        dtt.popolazione
      );
      // Assegno l'oggetto con il nome dell'uso
      datiTecniciUsiVo[nomeUso] = usoTVo;
    }

    // Ritorno l'oggetto compilato
    return datiTecniciUsiVo;
  }
}
