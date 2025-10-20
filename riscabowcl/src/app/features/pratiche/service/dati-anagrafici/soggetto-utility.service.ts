import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { ComponenteGruppo } from '../../../../core/commons/vo/componente-gruppo-vo';
import { Gruppo, GruppoVo } from '../../../../core/commons/vo/gruppo-vo';
import { RecapitoVo } from '../../../../core/commons/vo/recapito-vo';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { UserService } from '../../../../core/services/user.service';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaCompareService } from '../../../../shared/services/risca/risca-compare.service';
import { RiscaModalService } from '../../../../shared/services/risca/risca-modal.service';
import { isRecapitoPrincipale } from '../../../../shared/services/risca/risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  GruppoEComponenti,
  IAlertCAFConfigs,
  ICallbackDataModal,
  ParseValueRules,
  RiscaAlertConfigs,
  RiscaContatto,
  RiscaDatiSoggetto,
  RiscaRecapito,
} from '../../../../shared/utilities';
import { ApriModalConfigs } from '../../../../shared/utilities/classes/risca-modal/risca-modal.classes';
import {
  AbilitaDASezioni,
  AbilitaDASoggetti,
  CodIstatNazioni,
  RiscaInfoLevels,
} from '../../../../shared/utilities/enums/utilities.enums';
import { IApriModalConfigsForClass } from '../../../../shared/utilities/interfaces/risca/risca-modal/risca-modal.interfaces';
import { TipoNaturaGiuridica, TipoSoggettoVo } from '../../../ambito/models';
import { AmbitoService } from '../../../ambito/services';
import { DatiAnagraficiConsts } from '../../consts/dati-anagrafici/dati-anagrafici.consts';
import { CercaTitolareModalComponent } from '../../modal/cerca-titolare-modal/cerca-titolare-modal.component';
import { ICercaTitolareModalConfigs } from '../../modal/cerca-titolare-modal/utilities/cerca-titolare-modal.interfaces';
import { GruppoSoggettoService } from './gruppo-soggetto.service';
import { RecapitiService } from './recapiti.service';

@Injectable({
  providedIn: 'root',
})
export class SoggettoDatiAnagraficiUtilityService {
  /** Oggetto di costanti comuni all'applicazione. */
  private C_C = new CommonConsts()
  /** Oggetto con le costanti per la sezione dei dati anagrafici. */
  private DA_C = new DatiAnagraficiConsts();

  /**
   * Costruttore
   */
  constructor(
    private _ambito: AmbitoService,
    private _gruppoSoggetto: GruppoSoggettoService,
    private _recapiti: RecapitiService,
    private _riscaAlert: RiscaAlertService,
    private _riscaCompare: RiscaCompareService,
    private _riscaModal: RiscaModalService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {}

  /**
   * #####################################
   * FUNZIONI PER LA GESTIONE DELLE MODALI
   * #####################################
   */

  /**
   * Funzione che apre il modale per la ricerca di un titolare.
   * @param callbacks CallbackDataModal contenente le funzioni di callback del modale.
   */
  apriCercaTitolareModal(callbacks?: ICallbackDataModal) {
    // Definisco l'oggetto di configurazione per la modal di bootstrap
    const options: NgbModalOptions = { windowClass: 'cerca-titolare-modal' };
    // Definisco l'istanza del componente della modale
    const component = CercaTitolareModalComponent;

    // Definisco i parametri da passare alla modal
    const dataModal: ICercaTitolareModalConfigs = {
      gruppiAbilitati: this._gruppoSoggetto.isAbilitato,
      soggettiAbilitati: this.isGestioneAbilitata,
    };
    const params = { dataModal };

    // Definisco l'oggetto di configurazioni per il servizio
    const configs: IApriModalConfigsForClass = {
      component,
      options,
      callbacks,
      params,
    };

    // Richiamo il servizio per l'apertura del modale
    this._riscaModal.apriModal(new ApriModalConfigs(configs));
  }

  /**
   * ###########################
   * FUNZIONI DI CONVERT OGGETTI
   * ###########################
   */

  /**
   * Funzione che genera un oggetto SoggettoVo dati in input le parti per la composizione dell'oggetto.
   * @param datiSoggetto RiscaDatiSoggetto con i dati soggetto.
   * @param recapiti Array di RecapitoVo con i recapiti del soggetto.
   * @returns SoggettoVo con le informazioni dei dati anagrafici.
   */
  convertDatiSoggettoFormToSoggettoVo(
    datiSoggetto: RiscaDatiSoggetto,
    recapiti: RecapitoVo[]
  ): SoggettoVo {
    // Definisco variabili che conterranno parti per la composizione dell'oggetto SoggettoVo
    let soggettoVo: SoggettoVo;

    // Verifico se esistono le strutture dati e le parso
    if (datiSoggetto) {
      // Effettuo la conversione dei dati FE ai dati SoggettoVo
      soggettoVo = this.convertRiscaDatiSoggettoToSoggettoVo(datiSoggetto);
    }

    // Verifico che esistano oggetti parsati
    if (!soggettoVo && !recapiti) return undefined;

    // Definisco un oggetto con dati generici per i dati anagrafici
    const datiAnagraficiGenerici = {
      den_soggetto: datiSoggetto.ragioneSociale,
    };

    // Unisco tutti i dati anagrafici
    const datiAnagraficiVo = {
      ...datiAnagraficiGenerici,
      ...soggettoVo,
    };
    // Aggiungo i recapiti
    datiAnagraficiVo.recapiti = recapiti;

    // Ritorno l'oggetto composto dalle parti dei dati anagrafici
    return datiAnagraficiVo;
  }

  /**
   * Funzione di parsing da un oggetto RiscaDatiSoggetto ad un oggetto compatibile con SoggettoVo.
   * @param datiSoggetto RiscaDatiSoggetto da convertire.
   * @returns SoggettoVo contenente i dati del soggetto.
   */
  convertRiscaDatiSoggettoToSoggettoVo(
    datiSoggetto: RiscaDatiSoggetto
  ): SoggettoVo {
    // Verifico l'input
    if (!datiSoggetto) {
      // Ritorno un oggetto vuoto
      return {};
    }

    // Effettuo un parsing per la data
    const data_nascita_soggetto =
      this._riscaUtilities.convertNgbDateStructToDateString(
        datiSoggetto.dataDiNascita,
        this.C_C.DATE_FORMAT_SERVER
      );

    // Da datiSoggetto estraggo le informazioni per i dati anagrafici
    const datiSoggettoAnagrafici: SoggettoVo = {
      id_ambito: this._user.idAmbito,
      // ambito: this._ambito.ambito,
      tipo_soggetto: datiSoggetto.tipoSoggetto,
      cf_soggetto: datiSoggetto.codiceFiscale,
      tipo_natura_giuridica: datiSoggetto.naturaGiuridica || null,
      nome: datiSoggetto.nome,
      cognome: datiSoggetto.cognome,
      partita_iva_soggetto: datiSoggetto.partitaIva,
      data_nascita_soggetto,
      comune_nascita: datiSoggetto.comuneDiNascita,
      nazione_nascita: datiSoggetto.statoDiNascita,
      citta_estera_nascita: datiSoggetto.cittaEsteraNascita,
    } as any;

    // Sanitizzo possibili proprietà undefined e ritorno l'oggetto
    return this._riscaUtilities.sanitizeServerObject(datiSoggettoAnagrafici);
  }

  /**
   * Funzione di convert dei dati front-end per un recapito in un oggetto compatibile con SoggettoVo.
   * @param recapito RiscaRecapito da convertire.
   * @param contatti RiscaContatto da convertire.
   * @returns Oggetto parziale SoggettoVo convertito.
   */
  convertRiscaRecapitoToSoggettoVo(
    recapito: RiscaRecapito,
    contatti: RiscaContatto
  ): SoggettoVo {
    // Verifico l'input
    if (!recapito) {
      // Ritorno un oggetto vuoto
      return {} as any;
    }

    // Creo un nuovo oggetto di tipo SoggettoVo
    const soggetto = new SoggettoVo();
    // Definisco un array che andrà a contenere i dati del recapito
    const recapiti: RecapitoVo[] = [];

    // Effettuo la mappatura dei dati
    const recapitoVo = this._recapiti.convertRiscaRecapitoToRecapitoVo(
      recapito,
      contatti
    );
    // Una volta generato l'oggetto lo inserisco nell'array
    recapiti.push(recapitoVo);
    // Aggiungo ai dati anagrafici l'array
    soggetto.recapiti = recapiti;

    // Ritorno l'oggetto
    return soggetto;
  }

  /**
   * ##########################################
   * FUNZIONI DI CONVERSIONE SOGGETTO => GRUPPI
   * ##########################################
   */

  /**
   * Funzione di convert che gestisce la struttura dati del SoggettoVo in input, convertendo l'eventuale array gruppo_soggetto, da GruppoVo[] a Gruppo[].
   * @param soggettoVo SoggettoVo per la conversione.
   * @returns SoggettoVo aggiornato.
   */
  convertSoggettoGruppiVoToGruppi(soggettoVo: SoggettoVo): SoggettoVo {
    // Variabili di comodo
    const gExist = soggettoVo?.gruppo_soggetto?.length > 0;

    // Verifico l'input
    if (!gExist) {
      // Blocco il flusso
      return soggettoVo;
    }

    // Converto la lista dei gruppi del soggetto
    soggettoVo.gruppo_soggetto = this._gruppoSoggetto.convertGruppiVoToGruppi(
      soggettoVo.gruppo_soggetto as GruppoVo[]
    );

    // Ritorno il soggetto
    return soggettoVo;
  }

  /**
   * Funzione di convert che gestisce la struttura dati del SoggettoVo in input, convertendo l'eventuale array gruppo_soggetto, da Gruppo[] a GruppoVo[].
   * @param soggettoVo SoggettoVo per la conversione.
   * @returns SoggettoVo aggiornato.
   */
  convertSoggettoGruppiToGruppiVo(soggettoVo: SoggettoVo): SoggettoVo {
    // Variabili di comodo
    const gExist = soggettoVo?.gruppo_soggetto?.length > 0;

    // Verifico l'input
    if (!gExist) {
      // Blocco il flusso
      return soggettoVo;
    }

    // Converto la lista dei gruppi del soggetto
    soggettoVo.gruppo_soggetto = this._gruppoSoggetto.convertGruppiToGruppiVo(
      soggettoVo.gruppo_soggetto as Gruppo[]
    );

    // Ritorno il soggetto
    return soggettoVo;
  }

  /**
   * ################################
   * FUNZIONI DI UTILITY DEL SERVIZIO
   * ################################
   */

  /**
   * Funzione che compone i messaggi per l'alert di gestione dei campi aggiornati alla fonte.
   * @param labels Array di string che contiene tutti i dati per i messaggi.
   */
  messaggiAlertCampiAggiornatiFonte(labels: string[]): string[] {
    // Verifico l'input
    if (!labels) {
      // Ritorno un array vuoto
      return [];
    }

    // Definisco il contenitore dei messaggi
    let m = [];
    // Definisco testata, sottotitolo e uno spazio vuoto (visualizzazione più simile a prototipo)
    const testata = [this.DA_C.TITOLO_INFO, this.DA_C.SOTTOTITOLO_INFO, ''];

    // Variabile di comodo con degli spacing HTML
    const space = '&emsp;';
    // Effettuo un remapping della label aggiungendo un minimo di grafica
    const labelsOut = labels.map((l: string) => {
      // Ritorno le label con formattazione
      return `${space}${space}- <strong>${l}</strong>`;
    });

    // Compongo l'alert
    m = [...testata, ...labelsOut];

    // Ritorno la messaggistica
    return m;
  }

  /**
   * Funzione che verifica la response per i dati di un soggetto.
   * @param response Array di SoggettoVo da verificare.
   * @returns boolean che definisce se tutta la lista è composta da oggetti vuoti.
   */
  checkSoggetti(response: SoggettoVo[]): boolean {
    // Verifico l'input
    if (!response) {
      return false;
    }

    // Itero l'array
    let checkArray = response.map((r) => {
      // Verifico se l'oggeto è vuoto
      if (this._riscaUtilities.isEmptyObject(r)) {
        // Oggetto vuoto
        return undefined;
      }
      // L'oggetto esiste, lo ritorno
      return r;
    });
    // Rimuovo tutti i valori undefined
    checkArray = checkArray.filter((c) => c !== undefined);

    // Richiamo il controllo del servizio
    return checkArray.length > 0;
  }

  /**
   * Funzione di utility che recupera il soggetto referente o capogruppo, da un oggetto GruppoEComponenti.
   * @param gec GruppoEComponenti dalla quale estrarre i dati.
   * @returns SoggettoVo con i dati del referente/capogruppo.
   */
  getCapogruppoFromGruppoEComponenti(gec: GruppoEComponenti): SoggettoVo {
    // Verifico l'input
    if (!gec) {
      // Ritorno undefined
      return undefined;
    }

    // Recupero dall'oggetto il gruppo e i componenit
    const { gruppo, componenti } = gec;
    // Dal gruppo recupero il componente che ha il flag di capogruppo
    const idSCapogruppo = gruppo?.componenti_gruppo?.find(
      (c: ComponenteGruppo) => c.flg_capo_gruppo
    )?.id_soggetto;
    // Dai componenti ricerco per stesso id
    const referente = componenti?.find((c) => c.id_soggetto === idSCapogruppo);

    // Ritorno l'oggetto trovato
    return referente;
  }

  /**
   * Funzione che recupera le informazioni dei recapiti alternativi fusi tra un soggetto 1 e un soggetto 2.
   * I recapiti del soggetto 2, definiranno il valore delle proprietà comuni.
   * @param s1 SoggettoVo dalla quale estrarre i dati dei recapiti.
   * @param s2 SoggettoVo dalla quale estrarre i dati dei recapiti.
   * @returns Array di RecapitoVo con le informazioni mergiate.
   */
  mergeRecapitiSoggetti(s1: SoggettoVo, s2: SoggettoVo): RecapitoVo[] {
    // Verifico l'input
    if (!s1 && !s2) {
      // Ritorno un array vuoto
      return [];
    }
    // Verifico se uno dei due oggetti non è definito
    if (!s1 || !s2) {
      // Se manca s2, ritornerò un array vuoto
      if (!s2) {
        // Array vuoto
        return [];
        // #
      } else {
        // Ritorno l'array di s2
        return s2.recapiti;
      }
    }

    // Recupero i recapiti dei soggetti
    const r1 = s1.recapiti;
    const r2 = s2.recapiti;

    // Ritorno i recapiti mergiati
    return this.mergeArrayRecapiti(r1, r2);
  }

  /**
   * Funzione che mergia i dati comuni agli array in input.
   * I dati del secondo array, definiranno il valore delle proprietà comuni.
   * @param r1 Array di RecapitoVo per il merge.
   * @param r2 Array di RecapitoVo per il merge.
   * @returns Array di RecapitoVo con le informazioni mergiate.
   */
  mergeArrayRecapiti(r1: RecapitoVo[], r2: RecapitoVo[]): RecapitoVo[] {
    // Verifico l'input r2
    if (!r2) {
      // Ritorno un array vuoto
      return [];
      // #
    } else if (!r1) {
      // Ritorno direttamente r2
      return r2;
    }

    // Creo un array contenitore
    const rf: RecapitoVo[] = [];

    // Ciclo gli elementi dell'array 2
    r2.forEach((rc2: RecapitoVo) => {
      // Cerco lo stesso oggetto nell'array 1
      const rt1 = r1.find((rc1) => rc1.id_recapito === rc2.id_recapito);
      // Verifico se è stata trovata corrispondenza
      if (rt1) {
        // Creo un oggetto mergiando le informazioni
        const rm = this.mergeRecapiti(rt1, rc2);
        // Inserisco l'oggetto nell'array
        rf.push(rm);
        // #
      } else {
        // Non esiste, inserisco solo rc2
        rf.push(rc2);
      }
    });

    // Ritorno l'array compilato
    return rf;
  }

  /**
   * Funzione che mergia i dati dei recapiti principali dagli array in input.
   * I dati del secondo array, definiranno il valore delle proprietà comuni.
   * @param r1 Array di RecapitoVo per il merge.
   * @param r2 Array di RecapitoVo per il merge.
   * @returns RecapitoVo con le informazioni mergiate.
   */
  mergeRecapitiPrincipaliDaRecapiti(
    r1: RecapitoVo[],
    r2: RecapitoVo[]
  ): RecapitoVo[] {
    // Verifico l'input r2
    if (!r2) {
      // Ritorno un array vuoto
      return [];
      // #
    } else if (!r1) {
      // Ritorno direttamente r2
      return r2;
    }

    // Vado a recuperare i dati dei recapiti principali, tramite codice
    const iRP1 = r1.findIndex((r: RecapitoVo) => {
      // Recupero il recapito principale
      return isRecapitoPrincipale(r);
      // #
    });
    const iRP2 = r2.findIndex((r: RecapitoVo) => {
      // Recupero il recapito principale
      return isRecapitoPrincipale(r);
      // #
    });

    // Verifico che il recapito princiapale 2 esista
    if (iRP2 === -1) {
      // Ritorno l'array senza fare modifiche
      return r2;
    }

    // Dichiaro due contenitori per i recapiti
    const rp1 = iRP1 !== -1 ? r1[iRP1] : {};
    const rp2 = r2[iRP2];
    // Definisco un oggetto contenitore per il merge
    const rpf = this.mergeRecapiti(rp1, rp2);

    // Modifico l'oggetto nell'array 2
    r2[iRP2] = rpf;

    // Ritorno l'array modificato
    return r2;
  }

  /**
   * Funzione che fa un merge dei dati dei recapiti.
   * Verrà gestita la particolare gestione dello stato, dato che crea delle differenze di valorizzazione dei parametri.
   * ATTENZIONE: se ci sono proprietà comuni agli oggetti, saranno mantenute quelle dell'oggetto r2.
   * @param r1 RecapitoVo da mergiare.
   * @param r2 RecapitoVo da mergiare.
   * @returns RecapitoVo mergiato.
   */
  mergeRecapiti(r1: RecapitoVo, r2: RecapitoVo): RecapitoVo {
    // Verifico l'input
    if (!r1) {
      // Assegno un oggetto vuoto
      r1 = {} as any;
    }
    if (!r2) {
      // Assegno un oggetto vuoto
      r2 = {} as any;
    }

    // Definisco un oggetto contenitore per il merge
    const rm = { ...r1, ...r2 };

    // Verifico la nazione del secondo recapito
    const nr2 = r2.nazione_recapito?.cod_istat_nazione;
    // Verifico se la nazione è Italia
    if (nr2 === CodIstatNazioni.italia) {
      // Cancello le proprietà che non dovrebbero presentarsi
      delete rm.citta_estera_recapito;
      // #
    } else {
      // Cancello le proprietà che non fanno parte dello stato estero
      delete rm.comune_recapito;
      // #
    }

    // Ritorno l'oggetto recapito mergiato
    return rm;
  }

  /**
   * Funzione che mergia i dati tra due soggetti.
   * ATTENZIONE: se i soggetti hanno proprietà comuni, verranno mantenute quelle dell'oggetto s2.
   * @param s1 SoggettoVo da mergiare.
   * @param s2 SoggettoVo da mergiare.
   * @returns SoggettoVo con i dati mergiati.
   */
  mergeSoggetti(s1: SoggettoVo, s2: SoggettoVo): SoggettoVo {
    // Verifico l'input
    if (!s1) {
      // Assegno un oggetto vuoto
      s1 = {} as any;
    }
    if (!s2) {
      // Assegno un oggetto vuoto
      s2 = {} as any;
    }

    // Definisco un oggetto contenitore per il merge
    const sm = { ...s1, ...s2 };

    // Verifico la nazione del secondo soggetto
    const ns2 = s2.nazione_nascita?.cod_istat_nazione;
    // Verifico se la nazione è Italia
    if (ns2 === CodIstatNazioni.italia) {
      // Cancello le proprietà che non dovrebbero presentarsi
      delete sm.citta_estera_nascita;
      // #
    } else {
      // Cancello le proprietà che non fanno parte dello stato estero
      delete sm.comune_nascita;
      // #
    }

    // Recupero i recapiti comuni mergiando le informazioni
    const recapitiMerge = this.mergeRecapitiSoggetti(s1, s2);
    // Aggiorno i recapiti del soggetto finale
    sm.recapiti = recapitiMerge;

    // Ritorno l'oggetto recapito mergiato
    return sm;
  }

  /**
   * ###################################
   * FUNZIONI DI SET AUTOMATICI PER FORM
   * ###################################
   */

  /**
   * Funzione che estrae e imposta come valore di default un tipo soggetto all'interno di un form.
   * @param f FormGroup da valorizzare.
   * @param fcn FormControlName che definisce il campo del form da settare.
   * @param listaTipiSoggetto Array di TipoSoggetto dalla quale cercare il default.
   * @param codTipoSoggetto string che definisce il codice tipo soggetto da ricercare.
   */
  setTipoSoggettoDefault(
    f: FormGroup,
    fcn: string,
    listaTipiSoggetto: TipoSoggettoVo[],
    codTipoSoggetto: string
  ) {
    // Verifico l'input
    if (!f || !fcn || !listaTipiSoggetto || !codTipoSoggetto) return;

    // Lancio una sanitizzazione degli oggetti qualora ci siano già dei dafeult
    this._riscaUtilities.sanitizeRiscaSelectedFlag(listaTipiSoggetto);

    // Vado a recuperare l'oggetto ricercato
    const i = listaTipiSoggetto.findIndex(
      (s: TipoSoggettoVo) => s.cod_tipo_soggetto === codTipoSoggetto
    );
    // Verifico se esiste l'indice
    if (i !== -1) {
      // Recupero l'oggetto
      const tipoSoggetto = listaTipiSoggetto[i] as any;
      // Aggiorno l'oggetto referenziato per risca-select
      tipoSoggetto.__selected = true;
      // Imposto l'oggetto come valore
      this._riscaUtilities.setFormValue(f, fcn, tipoSoggetto);
    }
  }

  /**
   * Funzione che genera un RiscaAlertConfigs con i dati per i campi aggiornati dalla fonte.
   * @param configs IAlertCAFConfigs con le configurazioni dell'oggetto.
   * @returns RiscaAlertConfigs con le informazioni dell'alert per la visualizzazione dei campi aggiornati dalla fonte.
   */
  alertCampiAggiornatiFonte(configs: IAlertCAFConfigs): RiscaAlertConfigs {
    // Verifico l'input
    if (!configs) {
      // Niente configurazione
      return undefined;
    }

    // Estraggo i campi dalla configurazione
    const { alertConfigs, listaCampiAggiornati } = configs;

    // Ottengo i campi aggiornati dalla fonte dall'oggetto del BE
    const caf = listaCampiAggiornati || [];
    // Verifico che ci siano elementi
    if (caf.length === 0) {
      // Blocco il flusso
      return;
    }

    // Recupero la mappa per generare le label del campi aggiornati
    const map = this.DA_C.MAP_CAMPI_FONTE;
    // Genero le label per i campi aggiornati
    const labelCampi = this._riscaUtilities.getFieldsFonteMessages(caf, map);
    // Richiamo il servizio per generare i messaggi dell'alert
    const m = this.messaggiAlertCampiAggiornatiFonte(labelCampi);

    // Variabili di comodo
    const a = alertConfigs;
    const i = RiscaInfoLevels.info;

    // Aggiorno l'alert con le comunicazioni
    return this._riscaAlert.aggiornaAlertConfigs(a, m, i);
  }

  /**
   * Funzione che estrae e imposta come valore di default un tipo soggetto all'interno di un form.
   * @param f FormGroup da valorizzare.
   * @param fcn FormControlName che definisce il campo del form da settare.
   * @param listaNatureGiuridiche Array di TipoNaturaGiuridica dalla quale cercare il default.
   * @param naturaGiuridica TipoNaturaGiuridica che definisce la natura giuridica da ricercare.
   */
  setNaturaGiuridicaDefault(
    f: FormGroup,
    fcn: string,
    listaNatureGiuridiche: TipoNaturaGiuridica[],
    naturaGiuridica: TipoNaturaGiuridica
  ) {
    // Verifico l'input
    if (
      !f ||
      !fcn ||
      !listaNatureGiuridiche ||
      !naturaGiuridica?.id_tipo_natura_giuridica
    )
      return;

    // Lancio una sanitizzazione degli oggetti qualora ci siano già dei dafeult
    this._riscaUtilities.sanitizeRiscaSelectedFlag(listaNatureGiuridiche);

    // Vado a recuperare l'oggetto ricercato
    const tipoSoggetto = listaNatureGiuridiche.find(
      (n: TipoNaturaGiuridica) =>
        n.id_tipo_natura_giuridica === naturaGiuridica.id_tipo_natura_giuridica
    ) as any; // Tipizzo ad any per poter aggiugnere le proprietà custom della risca-select;
    // Verifico sia stato trovato l'oggetto
    if (tipoSoggetto) {
      // Aggiorno l'oggetto referenziato per risca-select
      tipoSoggetto.__selected = true;
      // Imposto l'oggetto come valore
      this._riscaUtilities.setFormValue(f, fcn, tipoSoggetto);
    }
  }

  /**
   * ################################
   * FUNZIONE DI COMPARE TRA SOGGETTI
   * ################################
   */

  /**
   * Funzione che verifica se un soggetto ha le stesse informazioni di un altro soggetto.
   * @param s1 SoggettoVo da verificare.
   * @param s2 SoggettoVo da verificare.
   * @param parseRules ParseValueRules che definisce le logiche di sanitizzazione degli oggetti.
   * @returns boolean che definisce se i soggetti in input hanno le stesse informazioni.
   */
  compareSoggetti(
    s1: SoggettoVo,
    s2: SoggettoVo,
    parseRules?: ParseValueRules
  ): boolean {
    // Ritorno il same dal servizio di utility
    return this._riscaCompare.sameSoggetti(s1, s2, parseRules);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che recupera l'abilitazione 'isGestioneAbilitata' per i soggetti.
   */
  get isGestioneAbilitata(): boolean {
    // Recupero dal servizio l'abilitazione per sezione/tipo abilitazione
    return this._ambito.isGestioneAbilitata;
  }

  /**
   * Getter che recupera l'abilitazione 'isFonteAbilitataInLettura' per i soggetti.
   */
  get isFonteAbilitataInLettura(): boolean {
    // Recupero dal servizio l'abilitazione per sezione/tipo abilitazione
    return this._ambito.isFonteAbilitataInLettura;
  }

  /**
   * Getter che recupera l'abilitazione 'isFonteAbilitataInScrittura' per i soggetti.
   */
  get isFonteAbilitataInScrittura(): boolean {
    // Recupero dal servizio l'abilitazione per sezione/tipo abilitazione
    return this._ambito.isFonteAbilitataInScrittura;
  }
}
