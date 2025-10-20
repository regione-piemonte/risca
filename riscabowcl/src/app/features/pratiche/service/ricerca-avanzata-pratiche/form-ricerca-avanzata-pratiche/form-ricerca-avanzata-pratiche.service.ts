import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormRicercaAvanzataPraticheConsts } from 'src/app/features/pratiche/consts/ricerca-avanzata-pratiche/form-ricerca-avanzata-pratiche.consts';
import { NazioneVo } from '../../../../../core/commons/vo/nazione-vo';
import { ProvinciaVo } from '../../../../../core/commons/vo/provincia-vo';
import { IRicercaIstanzaVo } from '../../../../../core/commons/vo/riscossione-search-vo';
import { StatoRiscossioneVo } from '../../../../../core/commons/vo/stati-riscossione-vo';
import { TipoIstanzaProvvedimentoVo } from '../../../../../core/commons/vo/tipo-istanza-provvidemento-vo';
import { TipoTitoloVo } from '../../../../../core/commons/vo/tipo-titolo-vo';
import { RiscaUtilitiesService } from '../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { TipoSoggettoVo } from '../../../../ambito/models';
import { IFRAPInitFormFields } from '../../../class/form-ricerca-avanzata-pratiche/form-ricerca-avanzata-pratiche.classes';
import {
  IFRAIstanza,
  IFRAPModalitaRicerca,
} from '../../../components/ricerca-avanzata/form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.interfaces';
import { ProvinciaCompetenzaVo } from '../../../../../core/commons/vo/provincia-competenza-vo';

/**
 * Servizio di utility con funzionalità di gestione per gli alert dell'applicazione.
 */
@Injectable()
export class FormRicercaAvanzataPraticheService {
  /** Oggetto contenente i valori costanti per il componente. */
  private RRAP_C = new FormRicercaAvanzataPraticheConsts();

  /**
   * Costruttore.
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * Funzione che gestisce la pre-valorizzazione delle input del form della ricerca.
   * La pre-valorizzazione delle select avviene mediante modifica dell'array dato in pasto al componente risca-select.
   * Sulla base del contenuto verrà definità la proprietà custom __selected, che valorizzerà appunto la select.
   * @param configs IRRAPInitFormFields contenente i dati per la pre-valorizzazione.
   */
  initFormFields(configs: IFRAPInitFormFields) {
    // Verifico l'input
    if (!configs) {
      // Niente pre-valorizzazione
      return;
    }

    // Estraggo tutte le informazioni di configurazione dall'oggetto
    const {
      form,
      source,
      listaTipiModalitaRicerca,
      listaTipiUtente,
      listaNazioni,
      listaProvince,
      listaProvinceCompetenza,
      listaTipiTitoli,
      listaTipiProvvedimenti,
      listaStatiPratiche,
      listaTipiIstanza,
    } = configs;

    // Richiamo la funzione di utility per la valorizzazione dei campi standard della form
    this._riscaUtilities.initFormFields(form, source?.pratica);

    // Estraggo i dati per la valorizzazione delle liste
    let { modalitaRicerca } = source || {};
    modalitaRicerca =
      modalitaRicerca || this.defaultModalitaRicerca(listaTipiModalitaRicerca);
    const {
      tipoSoggetto,
      stato,
      provincia,
      provinciaCompetenza,
      tipoTitolo,
      tipoProvvedimento,
      statoPratica,
    } = source?.pratica || {};
    // Richiamo la funzione per la gestione delle liste
    this.initTipiModalitaRicerca(
      form,
      listaTipiModalitaRicerca,
      modalitaRicerca
    );
    this.initTipiUtente(form, listaTipiUtente, tipoSoggetto);
    this.initNazioni(form, listaNazioni, stato);
    this.initProvince(form, listaProvince, provincia);
    this.initProvinceCompetenza(
      form,
      listaProvinceCompetenza,
      provinciaCompetenza
    );
    this.initTipiTitoli(form, listaTipiTitoli, tipoTitolo);
    this.initTipiProvvedimenti(form, listaTipiProvvedimenti, tipoProvvedimento);
    this.initStatiPratiche(form, listaStatiPratiche, statoPratica);
    this.initTipiIstanza(form, listaTipiIstanza, undefined);
  }

  /**
   * Funzione che pre-valorizza la lista: listaTipiModalitaRicerca;
   * @param f FormGroup con la referenza alla form della ricerca avanzata.
   * @param l Array di IRRAPModalitaRicerca contenenti i dati della lista.
   * @param e IRRAPModalitaRicerca contente il dato da pre-valorizzare.
   */
  initTipiModalitaRicerca(
    f: FormGroup,
    l: IFRAPModalitaRicerca[],
    e: IFRAPModalitaRicerca
  ) {
    // Verifico input tramite funzione di utility
    if (!this.checkSelectRAP(f, l, e)) {
      // Non ci sono le configurazioni
      return;
    }

    // Definisco il nome del form control da aggiornare
    const fcn = this.RRAP_C.MODALITA_RICERCA;
    // Definisco la funzione di match dati
    const find = (a: IFRAPModalitaRicerca, b: IFRAPModalitaRicerca) => {
      // Effettuo la verifica
      return a.id_modalita_ricerca === b.id_modalita_ricerca;
    };

    // Richiamo la funzione di valirizzazione tramite utility
    this._riscaUtilities.setFormValueAndSelect(f, fcn, l, e, find);
  }

  /**
   * Funzione che pre-valorizza la lista: listaTipiUtente;
   * @param f FormGroup con la referenza alla form della ricerca avanzata.
   * @param l Array di TipoSoggetto contenenti i dati della lista.
   * @param e TipoSoggetto contente il dato da pre-valorizzare.
   */
  initTipiUtente(f: FormGroup, l: TipoSoggettoVo[], e: TipoSoggettoVo) {
    // Verifico input tramite funzione di utility
    if (!this.checkSelectRAP(f, l, e)) {
      // Non ci sono le configurazioni
      return;
    }

    // Definisco il nome del form control da aggiornare
    const fcn = this.RRAP_C.TIPO_SOGGETTO;
    // Definisco la funzione di match dati
    const find = (a: TipoSoggettoVo, b: TipoSoggettoVo) => {
      // Effettuo la verifica
      return a.cod_tipo_soggetto === b.cod_tipo_soggetto;
    };

    // Richiamo la funzione di valirizzazione tramite utility
    this._riscaUtilities.setFormValueAndSelect(f, fcn, l, e, find);
  }

  /**
   * Funzione che pre-valorizza la lista: listaNazioni;
   * @param f FormGroup con la referenza alla form della ricerca avanzata.
   * @param l Array di NazioneVo contenenti i dati della lista.
   * @param e NazioneVo contente il dato da pre-valorizzare.
   */
  initNazioni(f: FormGroup, l: NazioneVo[], e: NazioneVo) {
    // Verifico input tramite funzione di utility
    if (!this.checkSelectRAP(f, l, e)) {
      // Non ci sono le configurazioni
      return;
    }

    // Definisco il nome del form control da aggiornare
    const fcn = this.RRAP_C.STATO_RESIDENZA;
    // Definisco la funzione di match dati
    const find = (a: NazioneVo, b: NazioneVo) => {
      // Effettuo la verifica
      return a.cod_istat_nazione === b.cod_istat_nazione;
    };

    // Richiamo la funzione di valirizzazione tramite utility
    this._riscaUtilities.setFormValueAndSelect(f, fcn, l, e, find);
  }

  /**
   * Funzione che pre-valorizza la lista: listaProvince;
   * @param f FormGroup con la referenza alla form della ricerca avanzata.
   * @param l Array di ProvinciaVo contenenti i dati della lista.
   * @param e ProvinciaVo contente il dato da pre-valorizzare.
   */
  initProvince(f: FormGroup, l: ProvinciaVo[], e: ProvinciaVo) {
    // Verifico input tramite funzione di utility
    if (!this.checkSelectRAP(f, l, e)) {
      // Non ci sono le configurazioni
      return;
    }

    // Definisco il nome del form control da aggiornare
    const fcn = this.RRAP_C.PROVINCIA_RESIDENZA;
    // Definisco la funzione di match dati
    const find = (a: ProvinciaVo, b: ProvinciaVo) => {
      // Effettuo la verifica
      return a.cod_provincia === b.cod_provincia;
    };

    // Richiamo la funzione di valirizzazione tramite utility
    this._riscaUtilities.setFormValueAndSelect(f, fcn, l, e, find);
  }

  /**
   * Funzione che pre-valorizza la lista: listaProvince;
   * @param f FormGroup con la referenza alla form della ricerca avanzata.
   * @param l Array di ProvinciaVo contenenti i dati della lista.
   * @param e ProvinciaCompetenzaVo contente il dato da pre-valorizzare.
   */
  initProvinceCompetenza(
    f: FormGroup,
    l: ProvinciaCompetenzaVo[],
    e: ProvinciaCompetenzaVo
  ) {
    // Verifico input tramite funzione di utility
    if (!this.checkSelectRAP(f, l, e)) {
      // Non ci sono le configurazioni
      return;
    }

    // Definisco il nome del form control da aggiornare
    const fcn = this.RRAP_C.PROVINCIA_COMPETENZA;
    // Definisco la funzione di match dati
    const find = (a: ProvinciaCompetenzaVo, b: ProvinciaCompetenzaVo) => {
      // Effettuo la verifica
      return a.sigla_provincia === b.sigla_provincia;
    };

    // Richiamo la funzione di valirizzazione tramite utility
    this._riscaUtilities.setFormValueAndSelect(f, fcn, l, e, find);
  }

  /**
   * Funzione che pre-valorizza la lista: listaTipiTitoli;
   * @param f FormGroup con la referenza alla form della ricerca avanzata.
   * @param l Array di TipoTitoloVo contenenti i dati della lista.
   * @param e TipoTitoloVo contente il dato da pre-valorizzare.
   */
  initTipiTitoli(f: FormGroup, l: TipoTitoloVo[], e: TipoTitoloVo) {
    // Verifico input tramite funzione di utility
    if (!this.checkSelectRAP(f, l, e)) {
      // Non ci sono le configurazioni
      return;
    }

    // Definisco il nome del form control da aggiornare
    const fcn = this.RRAP_C.TIPO_TITOLO;
    // Definisco la funzione di match dati
    const find = (a: TipoTitoloVo, b: TipoTitoloVo) => {
      // Effettuo la verifica
      return a.cod_tipo_titolo === b.cod_tipo_titolo;
    };

    // Richiamo la funzione di valirizzazione tramite utility
    this._riscaUtilities.setFormValueAndSelect(f, fcn, l, e, find);
  }

  /**
   * Funzione che pre-valorizza la lista: listaTipiProvvedimenti;
   * @param f FormGroup con la referenza alla form della ricerca avanzata.
   * @param l Array di TipoProvvedimentoVo contenenti i dati della lista.
   * @param e TipoProvvedimentoVo contente il dato da pre-valorizzare.
   */
  initTipiProvvedimenti(
    f: FormGroup,
    l: TipoIstanzaProvvedimentoVo[],
    e: TipoIstanzaProvvedimentoVo
  ) {
    // Verifico input tramite funzione di utility
    if (!this.checkSelectRAP(f, l, e)) {
      // Non ci sono le configurazioni
      return;
    }

    // Definisco il nome del form control da aggiornare
    const fcn = this.RRAP_C.TIPO_PROVVEDIMENTO;
    // Definisco la funzione di match dati
    const find = (a: TipoIstanzaProvvedimentoVo, b: TipoIstanzaProvvedimentoVo) => {
      // Effettuo la verifica
      return a.cod_tipo_provvedimento === b.cod_tipo_provvedimento;
    };

    // Richiamo la funzione di valirizzazione tramite utility
    this._riscaUtilities.setFormValueAndSelect(f, fcn, l, e, find);
  }

  /**
   * Funzione che pre-valorizza la lista: listaStatiPratiche;
   * @param f FormGroup con la referenza alla form della ricerca avanzata.
   * @param l Array di StatoRiscossioneVo contenenti i dati della lista.
   * @param e StatoRiscossioneVo contente il dato da pre-valorizzare.
   */
  initStatiPratiche(
    f: FormGroup,
    l: StatoRiscossioneVo[],
    e: StatoRiscossioneVo
  ) {
    // Verifico input tramite funzione di utility
    if (!this.checkSelectRAP(f, l, e)) {
      // Non ci sono le configurazioni
      return;
    }

    // Definisco il nome del form control da aggiornare
    const fcn = this.RRAP_C.STATO_PRATICA;
    // Definisco la funzione di match dati
    const find = (a: StatoRiscossioneVo, b: StatoRiscossioneVo) => {
      // Effettuo la verifica
      return a.cod_stato_riscossione === b.cod_stato_riscossione;
    };

    // Richiamo la funzione di valirizzazione tramite utility
    this._riscaUtilities.setFormValueAndSelect(f, fcn, l, e, find);
  }

  /**
   * Funzione che pre-valorizza la lista: listaTipiIstanza;
   * @param f FormGroup con la referenza alla form della ricerca avanzata.
   * @param l Array di TipoProvvedimentoVo contenenti i dati della lista.
   * @param e TipoProvvedimentoVo contente il dato da pre-valorizzare.
   */
  initTipiIstanza(
    f: FormGroup,
    l: TipoIstanzaProvvedimentoVo[],
    e: TipoIstanzaProvvedimentoVo
  ) {
    // Verifico input tramite funzione di utility
    if (!this.checkSelectRAP(f, l, e)) {
      // Non ci sono le configurazioni
      return;
    }

    // Definisco il nome del form control da aggiornare
    const fcn = this.RRAP_C.TIPO_ISTANZA;
    // Definisco la funzione di match dati
    const find = (a: TipoIstanzaProvvedimentoVo, b: TipoIstanzaProvvedimentoVo) => {
      // Effettuo la verifica
      return a.cod_tipo_provvedimento === b.cod_tipo_provvedimento;
    };

    // Richiamo la funzione di valirizzazione tramite utility
    this._riscaUtilities.setFormValueAndSelect(f, fcn, l, e, find);
  }

  /**
   * Funzione che verifica i dati per le funzioni di pre-valirizzazione campi form e liste.
   * @param f FormGroup con la referenza alla form della ricerca avanzata.
   * @param l Array di any contenenti i dati della lista.
   * @param e any contente il dato da pre-valorizzare.
   * @returns boolean che definisce se tutte le variabili in input esistono.
   */
  checkSelectRAP(f: FormGroup, l: any[], e: any): boolean {
    // Verifico che l'input esista
    return f != undefined && l != undefined && e != undefined;
  }

  /**
   * Funzione che definisce quale sia la modalità di ricerca di default da impostare all'interno della select.
   * @param modalitaRicerca Array di IRRAPModalitaRicerca che rappresenta la modalità ricerca disponibili.
   * @returns IRRAPModalitaRicerca che definisce il valore di default per la modalità ricerca.
   */
  private defaultModalitaRicerca(
    modalitaRicerca: IFRAPModalitaRicerca[]
  ): IFRAPModalitaRicerca {
    // Verifico l'input
    if (!modalitaRicerca || modalitaRicerca.length === 0) {
      // Ritorno undefined
      return undefined;
    }
    // Recupero l'elemento di default dalla lista
    return modalitaRicerca[0];
  }

  /**
   * Funzione che genera un array compatibile per la pre-valorizzazione della tabella in pagina.
   * @param istanze Array di RicercaIstanza con i dati per la generazione dati della pagina.
   * @param listaTipiIstanza Array di TipoProvvedimentoVo con i dati per la generazione dati della pagina.
   * @returns Array di IFRAPIstanza per popolare la tabella delle istanze.
   */
  generateIstanzeTable(
    istanze: IRicercaIstanzaVo[],
    listaTipiIstanza: TipoIstanzaProvvedimentoVo[]
  ): IFRAIstanza[] {
    // Check sull'input
    const validI = istanze != undefined && istanze.length > 0;
    const validLTI =
      listaTipiIstanza != undefined && listaTipiIstanza.length > 0;

    // Verifico l'input
    if (!validI || !validLTI) {
      // Niente da generare
      return [];
    }

    // Genero l'array di oggetti per la tabella, mappando la lista d'istanze di configurazione
    return istanze
      .map((cIstanza: IRicercaIstanzaVo) => {
        // Cerco l'oggetto di configurazione dalla lista d'istanze possibili
        const fIstanza = listaTipiIstanza.find((tI: TipoIstanzaProvvedimentoVo) => {
          // Effettuo un confronto tra ricerca istanza e tipo istanza
          return cIstanza.id_istanza === tI.id_tipo_provvedimento;
        });

        // Verifico se c'è l'istanza ricercata
        if (fIstanza !== undefined) {
          // Recupero i dati da ritornare
          const tipoIstanza = fIstanza;
          const didR = cIstanza.data_istanza_da;
          const dataDa =
            this._riscaUtilities.convertServerDateToNgbDateStruct(didR);
          const diaR = cIstanza.data_istanza_a;
          const dataA =
            this._riscaUtilities.convertServerDateToNgbDateStruct(diaR);

          // Oggetto trovato, genero e ritorno il dato per la tabella
          let istanzaTab: IFRAIstanza;
          istanzaTab = { tipoIstanza, dataDa, dataA };

          // Ritorno l'oggetto
          return istanzaTab;
        }
      })
      .filter((iTab: IFRAIstanza) => {
        // Rimuovo i dati undefined
        return iTab !== undefined;
      });
  }
}
