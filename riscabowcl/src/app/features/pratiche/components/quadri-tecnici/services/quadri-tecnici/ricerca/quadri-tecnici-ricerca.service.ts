import { Injectable } from '@angular/core';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Subject } from 'rxjs';
import { IUnitaMisuraVo } from 'src/app/core/commons/vo/unita-misura-vo';
import { IUsoLeggeSpecificoVo } from 'src/app/core/commons/vo/uso-legge-specifico-vo';
import { IUsoLeggeVo } from 'src/app/core/commons/vo/uso-legge-vo';
import { PraticaDTVo } from '../../../../../../../core/commons/vo/pratica-vo';
import { RiduzioneAumentoVo } from '../../../../../../../core/commons/vo/riduzione-aumento-vo';
import { RiscaUtilitiesService } from '../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  RiscaRegExp,
  ServerStringAsBoolean,
} from '../../../../../../../shared/utilities';
import { DatiTecniciAmbienteConsts } from '../../../../../consts/quadri-tecnici/dati-tecnici-ambiente/dati-tecnici-ambiente.consts';
import { IRicercaPraticaAvanzataForm } from '../../../../ricerca-avanzata/form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.interfaces';
import { IDatiTecniciAmbiente } from '../../../utilities/interfaces/dt-ambiente-pratica.interfaces';
import { IDatiTecniciTributi } from '../../../utilities/interfaces/dt-tributi-pratica.interfaces';
import {
  DatiTecniciGeneraliAmbienteRicercaVo,
  DatiTecniciTributoVoRicerca,
  DatiTecniciUsiRicercaVo,
  DatiTecniciUsoRicercaVo,
  DatiTecniciVoRicerca,
} from '../../../utilities/vo/dati-tecnici-ambiente-ricerca-vo';
import {
  DatiTecniciAumentoVo,
  DatiTecniciGeneraliAmbienteVo,
  DatiTecniciRiduzioneVo,
  DatiTecniciUsiAmbienteVo,
  DatiTecniciUsoAmbienteVo,
} from '../../../utilities/vo/dati-tecnici-ambiente-vo';
import { DatiTecniciTributiVoRicerca } from '../../../utilities/vo/dati-tecnici-tributi-ricerca-vo';

/**
 * Interfaccia personalizzata che definisce la struttura dei dati uso di legge che utilizzano le form dati tecnici ambiente.
 */
export interface UsoDiLeggeInfoSearch {
  usoDiLegge?: IUsoLeggeVo;
  usiDiLeggeSpecifici?: IUsoLeggeSpecificoVo[];
  unitaDiMisura?: IUnitaMisuraVo;
  quantitaDa?: number;
  quantitaA?: number;
  quantitaFaldaProfonda?: number;
  percFaldaProfonda?: number;
  quantitaFaldaSuperficiale?: number;
  percFaldaSuperficiale?: number;
  percRiduzioni?: RiduzioneAumentoVo[];
  percAumenti?: RiduzioneAumentoVo[];
  dataScadenzaEmasIsoDa?: NgbDateStruct;
  dataScadenzaEmasIsoA?: NgbDateStruct;
}

@Injectable({
  providedIn: 'root',
})
export class QuadriTecniciRicercaService {
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTA_C = new DatiTecniciAmbienteConsts();
  /** Oggetto RiscaRegExp utilizzate come supporto per la validazione dei campi. */
  private riscaRegExp: RiscaRegExp = new RiscaRegExp();

  /** Subject che emette l'evento per il submit della form. */
  onFormSubmit$ = new Subject<boolean>();
  /** Subject che emette l'evento per il reset della form. */
  onFormReset$ = new Subject<boolean>();

  /** Subject che emette il valore del form per la ricerca avanzata. */
  onRicercaAvanzataChange$ = new Subject<IRicercaPraticaAvanzataForm>();

  /**
   * Costruttore
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * #########################################
   * FUNZIONI DI SUPPORTO PER EMISSIONE EVENTI
   * #########################################
   */

  /**
   * Funzione che permette di emettere l'evento: onFormSubmit$.
   * @param data boolean con il dato da propagare.
   */
  onFormSubmit(data: boolean) {
    // Richiamo il next dell'evento
    this.onFormSubmit$.next(data);
  }

  /**
   * Funzione che permette di emettere l'evento: onFormReset$.
   * @param data boolean con il dato da propagare.
   */
  onFormReset(data: boolean) {
    // Richiamo il next dell'evento
    this.onFormReset$.next(data);
  }

  /**
   * Funzione che permette di emettere l'evento: onRicercaAvanzataChange$.
   * @param data IRicercaPraticaAvanzataReq con il dato da propagare.
   */
  onRicercaAvanzataChange(data: IRicercaPraticaAvanzataForm) {
    // Richiamo il next dell'evento
    this.onRicercaAvanzataChange$.next(data);
  }

  /**
   * #############################################
   * FUNZIONI DI CONVERSIONE DATI TECNICI AMBIENTE
   * #############################################
   */

  /**
   * Funzione di conversione da un oggetto DatiTecniciUsoVo ad un oggetto UsoDiLeggeInfo.
   * @param tipoUso string che definisce il nome del tipo uso.
   * @param dtuVo DatiTecniciUsoVo da convertire.
   * @returns UsoDiLeggeInfo convertito.
   */
  convertDatiTecniciUsoVoToUsoDiLeggeInfo(
    tipoUso: string,
    dtuVo: DatiTecniciUsoAmbienteVo
  ): UsoDiLeggeInfoSearch {
    // Verifico l'input
    if (!dtuVo) {
      // Nessuna configurazione
      return;
    }

    // Definisco un oggetto per la conversione
    const usoDiLegge: UsoDiLeggeInfoSearch = {};

    // Assegno le proprietà
    usoDiLegge.usoDiLegge = {
      id_tipo_uso: dtuVo.id_tipo_uso_legge,
      des_tipo_uso: tipoUso,
    };
    usoDiLegge.usiDiLeggeSpecifici = dtuVo.uso_specifico?.map((up) => {
      // Ritorno la mappatura per gli usi specifici
      return { cod_tipo_uso: up, des_tipo_uso: up };
    });
    usoDiLegge.unitaDiMisura = { des_unita_misura: dtuVo.unita_di_misura };
    usoDiLegge.quantitaDa = dtuVo.qta_acqua;
    usoDiLegge.quantitaA = dtuVo.qta_acqua;
    usoDiLegge.quantitaFaldaProfonda = dtuVo.qta_falda_profonda;
    usoDiLegge.percFaldaProfonda = dtuVo.perc_falda_profonda;
    usoDiLegge.quantitaFaldaSuperficiale = dtuVo.qta_falda_superficie;
    usoDiLegge.percFaldaSuperficiale = dtuVo.perc_falda_superficie;
    usoDiLegge.percRiduzioni = dtuVo.riduzione?.map((r) => {
      // Ritorno la mappatura per gli aumenti
      return {
        id_riduzione_aumento: r?.id_riduzione,
        perc_riduzione_aumento: r?.perc_riduzione_motiv,
        des_riduzione_aumento: r?.motivazione,
      };
    });
    usoDiLegge.percAumenti = dtuVo.aumento?.map((a) => {
      // Ritorno la mappatura per gli aumenti
      return {
        id_riduzione_aumento: a?.id_aumento,
        perc_riduzione_aumento: a?.perc_aumento_motiv,
        des_riduzione_aumento: a?.motivazione,
      };
    });
    // usoDiLegge.dataScadenzaEmasIso =
    //   this._riscaUtilities.convertServerDateToNgbDateStruct(
    //     dtuVo.data_scadenza_emas_iso
    //   );

    // Ritorno l'oggetto
    return usoDiLegge;
  }

  /**
   * Funzione di convert da DatiTecniciAmbiente a DatiTecniciGeneraliAmbienteVo.
   * @param datiTencniciAmbiente DatiTecniciAmbiente da convertire.
   * @returns DatiTecniciGeneraliAmbienteVo convertito.
   */
  convertDatiTecniciAmbienteToDatiTecniciGeneraliVo(
    datiTecniciAmbiente: IDatiTecniciAmbiente
  ): DatiTecniciGeneraliAmbienteRicercaVo {
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
    usiDiLeggeRow: UsoDiLeggeInfoSearch[]
  ): DatiTecniciUsiAmbienteVo {
    // Verifico che l'input esista
    if (!usiDiLeggeRow) return {};

    // Definisco i dati di uso
    const datiTecniciUsiVo: DatiTecniciUsiAmbienteVo = {};

    for (let i = 0; i < usiDiLeggeRow.length; i++) {
      // Recupero un uso di legge
      const usoDiLeggeRow = usiDiLeggeRow[i];
      // Definisco il nome dell'oggetto che conterrà i dati
      const nomeUso = this.sanitizeDatiTecniciUsiVoProperty(
        usoDiLeggeRow?.usoDiLegge?.cod_tipo_uso
      );
      if (nomeUso != null) {
        // Compilo l'oggetto richiamando la funzione di convert
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
   * @param usiDiLeggeRow UsoDiLeggeInfo da convertire.
   * @returns DatiTecniciUsoVo convertito.
   */
  convertUsoDiLeggeRowToDatiTecniciUsoVo(
    usoDiLeggeRow: UsoDiLeggeInfoSearch
  ): DatiTecniciUsoAmbienteVo {
    // Verifico che l'input esista
    if (!usoDiLeggeRow) {
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
    let dataScadEMIS: any = null;
    // usoDiLeggeRow.dataScadenzaEmasIso;
    // dataScadEMIS = dataScadEMIS
    //   ? this._riscaUtilities.convertNgbDateStructToDateString(
    //       dataScadEMIS,
    //       RiscaFormatoDate.server
    //     )
    //   : '';

    // Aggiungo ai dati tecnici usi la composizione dei dati
    return new DatiTecniciUsoAmbienteVo(
      usoDiLeggeRow?.usoDiLegge?.id_tipo_uso,
      usoDiLeggeRow?.usoDiLegge?.des_tipo_uso,
      usiSpecificiVo,
      usoDiLeggeRow?.unitaDiMisura?.sigla_unita_misura,
      usoDiLeggeRow?.quantitaDa,
      usoDiLeggeRow?.quantitaFaldaProfonda,
      usoDiLeggeRow?.percFaldaProfonda,
      usoDiLeggeRow?.quantitaFaldaSuperficiale,
      usoDiLeggeRow?.percFaldaSuperficiale,
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
   * @param riduzioniVo RiduzioneAumentoVo da convertire.
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
   * @param riduzioniVo RiduzioneAumentoVo da convertire.
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
   * ############################################
   * FUNZIONI DI CONVERSIONE DATI TECNICI TRIBUTI
   * ############################################
   */

  /**
   * Funzione che converte un oggetto DatiTecniciTributi in un oggetto PraticaDTVo per la ricerca riscossioni avanzata.
   * @param dtaUsi DatiTecniciTributi da convertire.
   * @returns PraticaDTVo convertito.
   */
  convertRicercaDatiTecniciTributiToPraticaDTVoRicerca(
    dtaLocalizzazione: IDatiTecniciTributi
  ): PraticaDTVo {
    // Verifico l'input
    if (!dtaLocalizzazione) {
      // Nessuna conversione
      return undefined;
    }

    // Verifico se è stata definita una pratica di partenza
    dtaLocalizzazione =
      dtaLocalizzazione !== undefined ? dtaLocalizzazione : ({} as any);
    // Lancio la conversione delle informazioni
    const dtVo =
      this.convertDatiTecniciTributiToDatiTecniciVoRicerca(dtaLocalizzazione);

    // Una volta generati i dati, unisco le informazioni
    const praticaDTVo: PraticaDTVo = {
      riscossione: {
        dati_tecnici: JSON.stringify(dtVo),
        data_inserimento: null,
        data_modifica: null,
        gest_UID: null,
        id_riscossione: null,
      },
    };

    // Ritorno l'oggetto generato
    return praticaDTVo;
  }

  /**
   * Funzione di convert da oggetto DatiTecniciTributi a oggetto DatiTecniciVoRicerca per la ricerca avanzata di riscossioni.
   * @param datiTecniciTributi DatiTecniciTributi da convertire.
   * @returns DatiTecniciVo generato.
   */
  convertDatiTecniciTributiToDatiTecniciVoRicerca(
    datiTecniciTributi: IDatiTecniciTributi
  ): DatiTecniciTributiVoRicerca {
    // Verifico che l'oggetto in input esista
    if (!datiTecniciTributi) {
      throw new Error('datiTecniciTributi not defined');
    }

    // Prendo l'uso
    const { popolazione, uso } = datiTecniciTributi;
    // Definisco i dati di uso
    const datiTecniciUsiVo: DatiTecniciTributoVoRicerca = {};
    if (uso) {
      // Prendo i dati dell'uso
      const { id_tipo_uso, cod_tipo_uso } = uso;
      // Popolo i dati di uso
      datiTecniciUsiVo[uso.cod_tipo_uso] = {
        popolazione,
        id_tipo_uso_legge: id_tipo_uso,
        uso_di_legge: cod_tipo_uso,
      };
    }

    // Creo e ritorno l'oggetto DatiTecniciVo
    return new DatiTecniciTributiVoRicerca(undefined, datiTecniciUsiVo);
  }

  /** ######################################
   * Funzioni di ricerca
   * #######################################
   */

  /**
   * Funzione che converte un oggetto DatiTecniciAmbiente in un oggetto PraticaDTVo per la ricerca riscossioni avanzata.
   * @param dtaUsi DatiTecniciAmbiente da convertire.
   * @returns PraticaDTVo convertito.
   */
  convertRicercaDatiTecniciAmbienteToPraticaDTVoRicerca(
    dtaLocalizzazione: IDatiTecniciAmbiente
  ): PraticaDTVo {
    // Verifico l'input
    if (!dtaLocalizzazione) {
      // Nessuna conversione
      return undefined;
    }

    // Verifico se è stata definita una pratica di partenza
    dtaLocalizzazione =
      dtaLocalizzazione !== undefined ? dtaLocalizzazione : ({} as any);
    // Lancio la conversione delle informazioni
    const dtVo =
      this.convertDatiTecniciAmbienteToDatiTecniciVoRicerca(dtaLocalizzazione);

    // Una volta generati i dati, unisco le informazioni
    const praticaDTVo: PraticaDTVo = {
      riscossione: {
        dati_tecnici: JSON.stringify(dtVo),
        data_inserimento: null,
        data_modifica: null,
        gest_UID: null,
        id_riscossione: null,
      },
    };

    // Ritorno l'oggetto generato
    return praticaDTVo;
  }

  /**
   * Funzione di convert da oggetto DatiTecniciAmbiente a oggetto DatiTecniciVoRicerca per la ricerca avanzata di riscossioni.
   * @param datiTecniciAmbiente DatiTecniciAmbiente da convertire.
   * @returns DatiTecniciVo generato.
   */
  convertDatiTecniciAmbienteToDatiTecniciVoRicerca(
    datiTecniciAmbiente: IDatiTecniciAmbiente
  ): DatiTecniciVoRicerca {
    // Verifico che l'oggetto in input esista
    if (!datiTecniciAmbiente) {
      throw new Error('datiTecniciAmbiente not defined');
    }

    // Genero i dati generali
    const datiTecniciGeneraliVo: DatiTecniciGeneraliAmbienteRicercaVo =
      this.convertDatiTecniciAmbienteToDatiTecniciGeneraliVo(
        datiTecniciAmbiente
      );
    // Definisco i dati di uso
    const datiTecniciUsiVo: DatiTecniciUsiRicercaVo =
      this.convertUsiDiLeggeRowToDatiTecniciUsiVoRicerca(
        datiTecniciAmbiente.usiDiLegge
      );

    // Creo e ritorno l'oggetto DatiTecniciVo
    return new DatiTecniciVoRicerca(datiTecniciGeneraliVo, datiTecniciUsiVo);
  }

  /**
   * Funzione di convert da un array di UsoDiLeggeInfo ad un oggetto DatiTecniciUsiVoRicerca per la ricerca avanzata riscossioni.
   * @param usiDiLeggeRow Array di UsoDiLeggeInfo da convertire.
   * @returns DatiTecniciUsiVo convertito.
   */
  convertUsiDiLeggeRowToDatiTecniciUsiVoRicerca(
    usiDiLeggeRow: UsoDiLeggeInfoSearch[]
  ): DatiTecniciUsiRicercaVo {
    // Verifico che l'input esista
    if (!usiDiLeggeRow) {
      return undefined;
    }

    // Definisco i dati di uso
    const datiTecniciUsiVo: DatiTecniciUsiRicercaVo = {};

    // Scorro i dati degli usi dentro i dati tecnici ambiente
    for (let i = 0; i < usiDiLeggeRow.length; i++) {
      // Recupero un uso di legge
      const usoDiLeggeRow = usiDiLeggeRow[i];
      // Definisco il nome dell'oggetto che conterrà i dati
      const nomeUso = this.sanitizeDatiTecniciUsiVoProperty(
        usoDiLeggeRow?.usoDiLegge?.cod_tipo_uso
      );
      // Compilo l'oggetto richiamando la funzione di convert
      datiTecniciUsiVo[nomeUso] =
        this.convertUsoDiLeggeRowToDatiTecniciUsoVoRicerca(usoDiLeggeRow);
    }

    // Ritorno l'oggetto compilato
    return datiTecniciUsiVo;
  }

  /**
   * Funzione di convert da UsoDiLeggeInfo ad un oggetto DatiTecniciUsoVoRicerca per la ricerca avanzata.
   * @param usiDiLeggeRow UsoDiLeggeInfo da convertire.
   * @returns DatiTecniciUsoVo convertito.
   */
  convertUsoDiLeggeRowToDatiTecniciUsoVoRicerca(
    usoDiLeggeRow: UsoDiLeggeInfoSearch
  ): DatiTecniciUsoRicercaVo {
    // Verifico che l'input esista
    if (!usoDiLeggeRow) {
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

    // Aggiungo ai dati tecnici usi la composizione dei dati
    return new DatiTecniciUsoRicercaVo(
      usoDiLeggeRow?.usoDiLegge?.id_tipo_uso,
      usoDiLeggeRow?.usoDiLegge?.des_tipo_uso,
      usiSpecificiVo,
      usoDiLeggeRow?.unitaDiMisura?.des_unita_misura,
      usoDiLeggeRow?.quantitaDa,
      usoDiLeggeRow?.quantitaA,
      usoDiLeggeRow?.quantitaFaldaProfonda,
      usoDiLeggeRow?.percFaldaProfonda,
      usoDiLeggeRow?.quantitaFaldaSuperficiale,
      usoDiLeggeRow?.percFaldaSuperficiale,
      riduzioniVo,
      aumentiVo,
      this._riscaUtilities.convertNgbDateStructToString(
        usoDiLeggeRow.dataScadenzaEmasIsoDa
      ),
      this._riscaUtilities.convertNgbDateStructToString(
        usoDiLeggeRow.dataScadenzaEmasIsoA
      )
    );
  }
}
