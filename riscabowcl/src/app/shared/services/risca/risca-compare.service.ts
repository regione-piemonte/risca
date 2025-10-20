import { Injectable } from '@angular/core';
import { difference, union } from 'lodash';
import { ComponenteGruppo } from '../../../core/commons/vo/componente-gruppo-vo';
import { ComuneVo } from '../../../core/commons/vo/comune-vo';
import { Gruppo } from '../../../core/commons/vo/gruppo-vo';
import { NazioneVo } from '../../../core/commons/vo/nazione-vo';
import { RecapitoVo } from '../../../core/commons/vo/recapito-vo';
import { SoggettoVo } from '../../../core/commons/vo/soggetto-vo';
import { TipoRecapitoVo } from '../../../core/commons/vo/tipo-recapito-vo';
import { TipoSedeVo } from '../../../core/commons/vo/tipo-sede-vo';
import { LoggerService } from '../../../core/services/logger.service';
import {
  TipoNaturaGiuridica,
  TipoSoggettoVo,
} from '../../../features/ambito/models';
import { IDatiTecniciAmbiente } from '../../../features/pratiche/components/quadri-tecnici/utilities/interfaces/dt-ambiente-pratica.interfaces';
import { TipoInvio } from '../../models/contatti/tipo-invio.model';
import {
  CodIstatNazioni,
  IRiscaCheckboxData,
  ParseValueRules,
} from '../../utilities';
import { RiscaUtilitiesService } from './risca-utilities/risca-utilities.service';

@Injectable({
  providedIn: 'root',
})
export class RiscaCompareService {
  /**
   * Costruttore
   */
  constructor(
    private _logger: LoggerService,
    private _riscaUtilities: RiscaUtilitiesService
  ) {}

  /**
   * Controllo su array.
   * Viene verificato se l'array 1 e 2 esistono e se hanno almeno gli stessi elementi.
   * @param a1 Array di any da verificare.
   * @param a2 Array di any da verificare.
   * @returns boolean con il risultato dei controlli.
   */
  sameArrayBasic(a1: any[], a2: any[]): boolean {
    // Verifico l'input
    const sameUnsetted = this._riscaUtilities.sameUnsetted(a1, a2);
    // Verifico se entrambi non sono settati
    if (sameUnsetted) {
      // Considero gli oggetti uguali
      return true;
    }
    // Verifico se un oggetto è settato, l'altro no
    if (!a1 || !a2) {
      // Sono oggetti diversi
      return false;
    }

    // Verifico se la quantità di elementi è diversa
    if (a1.length !== a2.length) {
      // La quantità di recapiti è differente, sono diversi
      return false;
    }

    // Controlli passati
    return true;
  }

  /**
   * Funzione che verifica se due comuni di un soggetto sono gli stessi.
   * @param c1 Comune da comparare.
   * @param c2 Comune da comparare.
   * @returns boolean con il risultato della comparazione.
   */
  sameComune(c1: ComuneVo, c2: ComuneVo): boolean {
    // Richiamo al funzione di verifica
    return this._riscaUtilities.sameObjectByProperty(
      c1,
      c2,
      'cod_istat_comune'
    );
  }

  /**
   * Funzione che verifica se due nazioni di un soggetto sono gli stessi.
   * @param n1 NazioneVo da comparare.
   * @param n2 NazioneVo da comparare.
   * @returns boolean con il risultato della comparazione.
   */
  sameNazione(n1: NazioneVo, n2: NazioneVo): boolean {
    // Richiamo al funzione di verifica
    return this._riscaUtilities.sameObjectByProperty(
      n1,
      n2,
      'cod_istat_nazione'
    );
  }

  /**
   * Funzione che verifica se due array di RecapitoVO soggetto sono gli stessi.
   * @param r1 Array di RecapitoVo da comparare.
   * @param r2 Array di RecapitoVo da comparare.
   * @returns boolean con il risultato della comparazione.
   */
  sameArrayRecapiti(r1: RecapitoVo[], r2: RecapitoVo[]): boolean {
    // Verifico lo stato base degli array
    const sameArrayBasic = this.sameArrayBasic(r1, r2);
    // Verifico lo stato base
    if (!sameArrayBasic) {
      // Ritorno false
      return false;
    }

    // Funzione di supporto per l'ordinamento dei recapiti
    const sortRecapiti = (a: RecapitoVo, b: RecapitoVo) => {
      // Recupero gli id recapiti
      const idRA = a.id_recapito;
      const idRB = b.id_recapito;
      // Ritorno il sort crescente per numero
      return this._riscaUtilities.sortNumbers(idRA, idRB);
    };

    // Riordino gli array per stesso id_recapito
    const rSort1 = r1.sort(sortRecapiti);
    const rSort2 = r2.sort(sortRecapiti);

    // Verifico se gli oggetti sono gli stessi (SE ARRIVIAMO QUI GLI ARRAY DEVONO ESSERE LUNGHI UGUALI ALMENO!)
    for (let i = 0; i < rSort1.length; i++) {
      // Recupero gli oggetti per la verifica
      const rv1 = rSort1[i];
      const rv2 = rSort2[i];
      // Verifico se non sono gli stessi oggetti
      if (!this._riscaUtilities.sameObjectByProperty(rv1, rv2, 'id_recapito')) {
        // Blocco le logiche, gli array sono diversi
        return false;
      }

      // Effettuo le verifiche sul dettaglio
      if (!this.sameRecapiti(rv1, rv2)) {
        // Blocco le logiche, c'è almeno un oggetto differente
        return false;
      }
    }

    // Controlli superati
    return true;
  }

  /**
   * Funzione che verifica se un recapito ha le stesse informazioni di un altro recapito.
   * @param r1 RecapitoVo da verificare.
   * @param r2 RecapitoVo da verificare.
   * @returns boolean che definisce se i recapiti in input hanno le stesse informazioni.
   */
  sameRecapiti(r1: RecapitoVo, r2: RecapitoVo): boolean {
    // Verifico l'input
    if (!r1 && !r2) {
      // Tutti e due undefined li considero uguali
      return true;
    }
    if (!r1 || !r2) {
      // Uno undefined e uno valorizzato, son diversi
      return false;
    }

    // Estraggo le informazioni da verificare dal primo recapito
    const ind1 = r1.indirizzo;
    const numCiv1 = r1.num_civico;
    const email1 = r1.email;
    const pec1 = r1.pec;
    const presso1 = r1.presso;
    const cER1 = r1.citta_estera_recapito;
    const capR1 = r1.cap_recapito;
    const desLoc1 = r1.des_localita;
    const tS1 = r1.tipo_sede;
    const tI1 = r1.tipo_invio;
    const tR1 = r1.tipo_recapito;
    const cR1 = r1.comune_recapito;
    const nR1 = r1.nazione_recapito;
    const tel1 = r1.telefono;
    const cel1 = r1.cellulare;
    // Estraggo le informazioni da verificare dal secondo recapito
    const ind2 = r2.indirizzo;
    const numCiv2 = r2.num_civico;
    const email2 = r2.email;
    const pec2 = r2.pec;
    const presso2 = r2.presso;
    const cER2 = r2.citta_estera_recapito;
    const capR2 = r2.cap_recapito;
    const desLoc2 = r2.des_localita;
    const tS2 = r2.tipo_sede;
    const tI2 = r2.tipo_invio;
    const tR2 = r2.tipo_recapito;
    const cR2 = r2.comune_recapito;
    const nR2 = r2.nazione_recapito;
    const tel2 = r2.telefono;
    const cel2 = r2.cellulare;

    // Definisco dei flag di check di comodo
    let check_ind = true;
    let check_numCiv = true;
    let check_email = true;
    let check_pec = true;
    let check_presso = true;
    let check_cER = true;
    let check_capR = true;
    let check_desLoc = true;
    let check_tS = true;
    let check_tI = true;
    let check_tR = true;
    let check_cR = true;
    let check_nR = true;
    let check_tel = true;
    let check_cel = true;
    // Variabili di comodo
    let m = 'compareRecapiti';

    // Verifico i campi (NOT_RELATED: https://www.youtube.com/watch?v=VUcWyt0nmHk)
    if (ind1 != ind2) {
      // Variabile di comodo
      let w = `${m} | indirizzo`;
      // Loggo un warning
      this._logger.warning(w, { ind1, ind2 });
      // Aggiorno il flag per il dato specifico
      check_ind = false;
    }
    if (numCiv1 != numCiv2) {
      // Variabile di comodo
      let w = `${m} | num_civico`;
      // Loggo un warning
      this._logger.warning(w, { numCiv1, numCiv2 });
      // Aggiorno il flag per il dato specifico
      check_numCiv = false;
    }
    if (email1 != email2) {
      // Variabile di comodo
      let w = `${m} | email`;
      // Loggo un warning
      this._logger.warning(w, { email1, email2 });
      // Aggiorno il flag per il dato specifico
      check_email = false;
    }
    if (pec1 != pec2) {
      // Variabile di comodo
      let w = `${m} | pec`;
      // Loggo un warning
      this._logger.warning(w, { pec1, pec2 });
      // Aggiorno il flag per il dato specifico
      check_pec = false;
    }
    if (presso1 != presso2) {
      // Variabile di comodo
      let w = `${m} | presso`;
      // Loggo un warning
      this._logger.warning(w, { presso1, presso2 });
      // Aggiorno il flag per il dato specifico
      check_presso = false;
    }
    if (capR1 != capR2) {
      // Variabile di comodo
      let w = `${m} | cap_recapito`;
      // Loggo un warning
      this._logger.warning(w, { capR1, capR2 });
      // Aggiorno il flag per il dato specifico
      check_capR = false;
    }
    if (desLoc1 != desLoc2) {
      // Variabile di comodo
      let w = `${m} | des_localita`;
      // Loggo un warning
      this._logger.warning(w, { desLoc1, desLoc2 });
      // Aggiorno il flag per il dato specifico
      check_desLoc = false;
    }
    if (!this.sameTipoSede(tS1, tS2)) {
      // Variabile di comodo
      let w = `${m} | tipo_sede`;
      // Loggo un warning
      this._logger.warning(w, { tS1, tS2 });
      // Aggiorno il flag per il dato specifico
      check_tS = false;
    }
    if (!this.sameTipoInvio(tI1, tI2)) {
      // Variabile di comodo
      let w = `${m} | tipo_invio`;
      // Loggo un warning
      this._logger.warning(w, { tI1, tI2 });
      // Aggiorno il flag per il dato specifico
      check_tI = false;
    }
    if (!this.sameTipoRecapito(tR1, tR2)) {
      // Variabile di comodo
      let w = `${m} | tipo_recapito`;
      // Loggo un warning
      this._logger.warning(w, { tR1, tR2 });
      // Aggiorno il flag per il dato specifico
      check_tR = false;
    }

    // LA NAZIONE HA UNA GESTIONE PARTICOLARE
    if (!this.sameNazione(nR1, nR2)) {
      // Variabile di comodo
      let w = `${m} | nazione_recapito`;
      // Loggo un warning
      this._logger.warning(w, { nR1, nR2 });
      // Aggiorno il flag per il dato specifico
      check_nR = false;
    } else {
      // Stessa nazione, verifico la tipologia
      const codNaz = nR1.cod_istat_nazione;
      // Verifico se la nazione è l'italia
      if (codNaz === CodIstatNazioni.italia) {
        // Controllo il comune
        if (!this.sameComune(cR1, cR2)) {
          // Variabile di comodo
          let w = `${m} | comune_recapito`;
          // Loggo un warning
          this._logger.warning(w, { cR1, cR2 });
          // Aggiorno il flag per il dato specifico
          check_cR = false;
        }
      } else {
        // Controllo la città estera
        if (cER1 != cER2) {
          // Variabile di comodo
          let w = `${m} | citta_estera_recapito`;
          // Loggo un warning
          this._logger.warning(w, { cER1, cER2 });
          // Aggiorno il flag per il dato specifico
          check_presso = false;
        }
      }
    }

    if (tel1 != tel2) {
      // Variabile di comodo
      let w = `${m} | telefono`;
      // Loggo un warning
      this._logger.warning(w, { tel1, tel2 });
      // Aggiorno il flag per il dato specifico
      check_tel = false;
    }
    if (cel1 != cel2) {
      // Variabile di comodo
      let w = `${m} | cellulare`;
      // Loggo un warning
      this._logger.warning(w, { cel1, cel2 });
      // Aggiorno il flag per il dato specifico
      check_cel = false;
    }

    // Tutto verificato correttamente
    return (
      check_ind &&
      check_numCiv &&
      check_email &&
      check_pec &&
      check_presso &&
      check_cER &&
      check_capR &&
      check_desLoc &&
      check_tS &&
      check_tI &&
      check_tR &&
      check_cR &&
      check_nR &&
      check_tel &&
      check_cel
    );
  }

  /**
   * Funzione che verifica se due comuni di un soggetto sono gli stessi.
   * @param tng1 Comune da comparare.
   * @param tng2 Comune da comparare.
   * @returns boolean con il risultato della comparazione.
   */
  sameNaturaGiuridica(
    tng1: TipoNaturaGiuridica,
    tng2: TipoNaturaGiuridica
  ): boolean {
    // Richiamo al funzione di verifica
    return this._riscaUtilities.sameObjectByProperty(
      tng1,
      tng2,
      'id_tipo_natura_giuridica'
    );
  }

  /**
   * Funzione che verifica se due tipi soggetto di un soggetto sono gli stessi.
   * @param ts1 TipoSoggetto da comparare.
   * @param ts2 TipoSoggetto da comparare.
   * @returns boolean con il risultato della comparazione.
   */
  sameTipoSoggetto(ts1: TipoSoggettoVo, ts2: TipoSoggettoVo): boolean {
    // Richiamo al funzione di verifica
    return this._riscaUtilities.sameObjectByProperty(
      ts1,
      ts2,
      'id_tipo_soggetto'
    );
  }

  /**
   * Funzione che verifica se due tipi sede di un recapito sono gli stessi.
   * @param tS1 TipoSedeVo da comparare.
   * @param tS2 TipoSedeVo da comparare.
   * @returns boolean con il risultato della comparazione.
   */
  sameTipoSede(tS1: TipoSedeVo, tS2: TipoSedeVo): boolean {
    // Richiamo al funzione di verifica
    return this._riscaUtilities.sameObjectByProperty(tS1, tS2, 'id_tipo_sede');
  }

  /**
   * Funzione che verifica se due tipi invio di un recapito sono gli stessi.
   * @param tI1 TipoInvio da comparare.
   * @param tI2 TipoInvio da comparare.
   * @returns boolean con il risultato della comparazione.
   */
  sameTipoInvio(tI1: TipoInvio, tI2: TipoInvio): boolean {
    // Richiamo al funzione di verifica
    return this._riscaUtilities.sameObjectByProperty(tI1, tI2, 'id_tipo_invio');
  }

  /**
   * Funzione che verifica se due tipi recapito di un recapito sono gli stessi.
   * @param tR1 TipoRecapitoVo da comparare.
   * @param tR2 TipoRecapitoVo da comparare.
   * @returns boolean con il risultato della comparazione.
   */
  sameTipoRecapito(tR1: TipoRecapitoVo, tR2: TipoRecapitoVo): boolean {
    // Richiamo al funzione di verifica
    return this._riscaUtilities.sameObjectByProperty(
      tR1,
      tR2,
      'id_tipo_recapito'
    );
  }

  /**
   * Funzione che verifica se due oggetti IRiscaCheckboxData.
   * @param rC1 IRiscaCheckboxData da comparare.
   * @param rC2 IRiscaCheckboxData da comparare.
   * @returns boolean con il risultato della comparazione.
   */
  sameIRiscaCheckboxData(
    rC1: IRiscaCheckboxData,
    rC2: IRiscaCheckboxData
  ): boolean {
    // Richiamo al funzione di verifica
    return this._riscaUtilities.sameObjectByProperty(rC1, rC2, 'value');
  }

  /**
   * Funzione che verifica se due array di componente gruppi sono gli stessi.
   * @param cg1 Array di ComponenteGruppo da comparare.
   * @param cg2 Array di ComponenteGruppo da comparare.
   * @returns boolean con il risultato della comparazione.
   */
  sameComponentiGruppo(
    cg1: ComponenteGruppo[],
    cg2: ComponenteGruppo[]
  ): boolean {
    // Verifico lo stato base degli array
    const sameArrayBasic = this.sameArrayBasic(cg1, cg2);
    // Verifico lo stato base
    if (!sameArrayBasic) {
      // Ritorno false
      return false;
    }

    // Recupero dagli array la lista degli id
    const idCG1 = cg1.map((cg) => cg.id_soggetto);
    const idCG2 = cg2.map((cg) => cg.id_soggetto);
    // Verifico le intersezioni tra gli id degli array
    const diff = union(difference(idCG1, idCG2), difference(idCG2, idCG1));
    // Verifico le differenze tra i due array
    if (diff.length > 0) {
      // Esistono delle differenze, la comparazione è false
      return false;
    }

    // Non ci sono differenze, sono gli stessi valori
    return true;
  }

  /**
   * Funzione che verifica se due array di componente gruppi sono gli stessi.
   * @param cg1 Array di ComponenteGruppo da comparare.
   * @param cg2 Array di ComponenteGruppo da comparare.
   * @returns boolean con il risultato della comparazione.
   */
  sameCapigruppo(cg1: ComponenteGruppo[], cg2: ComponenteGruppo[]): boolean {
    // Verifico l'input
    if (!cg1 && !cg2) {
      // Non ci sono elementi, ma li considero come same capogruppo
      return true;
    }
    if (!cg1 || !cg2) {
      // Un array esiste, l'altro no. Sicuramente non ci sarà un capogruppo uguale
      return false;
    }

    // Estraggo i capogruppi
    const capoG1 = cg1.find((cg) => cg.flg_capo_gruppo);
    const capoG2 = cg2.find((cg) => cg.flg_capo_gruppo);

    // Richiamo al funzione di verifica
    return this._riscaUtilities.sameObjectByProperty(
      capoG1,
      capoG2,
      'id_soggetto'
    );
  }

  /**
   * Funzione che verifica se un soggetto ha le stesse informazioni di un altro soggetto.
   * @param s1 SoggettoVo da verificare.
   * @param s2 SoggettoVo da verificare.
   * @param parseRules ParseValueRules che definisce le logiche di sanitizzazione degli oggetti.
   * @returns boolean che definisce se i soggetti in input hanno le stesse informazioni.
   */
  sameSoggetti(
    s1: SoggettoVo,
    s2: SoggettoVo,
    parseRules?: ParseValueRules
  ): boolean {
    // Definisco le regole d'applicare sugli oggetti
    const rules = parseRules || new ParseValueRules();
    // Effettuo la sanitificazione degli oggetti
    s1 = this._riscaUtilities.valueRegulated(s1, rules);
    s2 = this._riscaUtilities.valueRegulated(s2, rules);

    // Verifico l'input
    if (!s1 && !s2) {
      // Tutti e due undefined li considero uguali
      return true;
    }
    if (!s1 || !s2) {
      // Uno undefined e uno valorizzato, son diversi
      return false;
    }

    // Estraggo le informazioni da verificare dal primo soggetto
    const cfs1 = s1.cf_soggetto;
    const cen1 = s1.citta_estera_nascita;
    const cogn1 = s1.cognome;
    const cn1 = s1.comune_nascita;
    const dns1 = s1.data_nascita_soggetto;
    const ds1 = s1.den_soggetto;
    const idS1 = s1.id_soggetto;
    const nn1 = s1.nazione_nascita;
    const nome1 = s1.nome;
    const pIvaS1 = s1.partita_iva_soggetto;
    const recapiti1 = s1.recapiti;
    const tng1 = s1.tipo_natura_giuridica;
    const ts1 = s1.tipo_soggetto;
    // Estraggo le informazioni da verificare dal secondo soggetto
    const cfs2 = s2.cf_soggetto;
    const cen2 = s2.citta_estera_nascita;
    const cogn2 = s2.cognome;
    const cn2 = s2.comune_nascita;
    const dns2 = s2.data_nascita_soggetto;
    const ds2 = s2.den_soggetto;
    const idS2 = s2.id_soggetto;
    const nn2 = s2.nazione_nascita;
    const nome2 = s2.nome;
    const pIvaS2 = s2.partita_iva_soggetto;
    const recapiti2 = s2.recapiti;
    const tng2 = s2.tipo_natura_giuridica;
    const ts2 = s2.tipo_soggetto;

    // Definisco dei flag di check di comodo
    let check_cfs = true;
    let check_cen = true;
    let check_cogn = true;
    let check_cn = true;
    let check_dns = true;
    let check_ds = true;
    let check_idS = true;
    let check_nn = true;
    let check_nome = true;
    let check_pIvaS = true;
    let check_recapiti = true;
    let check_tng = true;
    let check_ts = true;
    // Variabili di comodo
    let m = 'sameSoggetti';

    // Verifico i campi (NOT_RELATED: https://www.youtube.com/watch?v=VUcWyt0nmHk)
    if (cfs1 != cfs2) {
      // Variabile di comodo
      let w = `${m} | cf_soggetto`;
      // Loggo un warning
      this._logger.warning(w, { cfs1, cfs2 });
      // Aggiorno il flag per il dato specifico
      check_cfs = false;
    }
    if (cogn1 != cogn2) {
      // Variabile di comodo
      let w = `${m} | cognome`;
      // Loggo un warning
      this._logger.warning(w, { cogn1, cogn2 });
      // Aggiorno il flag per il dato specifico
      check_cogn = false;
    }
    if (dns1 != dns2) {
      // Variabile di comodo
      let w = `${m} | data_nascita_soggetto`;
      // Loggo un warning
      this._logger.warning(w, { dns1, dns2 });
      // Aggiorno il flag per il dato specifico
      check_dns = false;
    }
    if (ds1 != ds2) {
      // Variabile di comodo
      let w = `${m} | den_soggetto`;
      // Loggo un warning
      this._logger.warning(w, { ds1, ds2 });
      // Aggiorno il flag per il dato specifico
      check_ds = false;
    }
    if (idS1 != idS2) {
      // Variabile di comodo
      let w = `${m} | id_soggetto`;
      // Loggo un warning
      this._logger.warning(w, { idS1, idS2 });
      // Aggiorno il flag per il dato specifico
      check_idS = false;
    }

    // LA NAZIONE HA UNA GESTIONE PARTICOLARE
    if (!this.sameNazione(nn1, nn2)) {
      // Variabile di comodo
      let w = `${m} | nazione_nascita`;
      // Loggo un warning
      this._logger.warning(w, { nn1, nn2 });
      // Aggiorno il flag per il dato specifico
      check_nn = false;
    } else {
      // Stessa nazione, verifico la tipologia
      const codNaz = nn1.cod_istat_nazione;
      // Verifico se la nazione è l'italia
      if (codNaz === CodIstatNazioni.italia) {
        // Controllo il comune
        if (!this.sameComune(cn1, cn2)) {
          // Variabile di comodo
          let w = `${m} | comune_nascita`;
          // Loggo un warning
          this._logger.warning(w, { cn1, cn2 });
          // Aggiorno il flag per il dato specifico
          check_cn = false;
        }
      } else {
        // Controllo la città estera
        if (cen1 != cen2) {
          // Variabile di comodo
          let w = `${m} | citta_estera_nascita`;
          // Loggo un warning
          this._logger.warning(w, { cen1, cen2 });
          // Aggiorno il flag per il dato specifico
          check_cen = false;
        }
      }
    }

    if (nome1 != nome2) {
      // Variabile di comodo
      let w = `${m} | nome`;
      // Loggo un warning
      this._logger.warning(w, { nome1, nome2 });
      // Aggiorno il flag per il dato specifico
      check_nome = false;
    }
    if (pIvaS1 != pIvaS2) {
      // Variabile di comodo
      let w = `${m} | partita_iva_soggetto`;
      // Loggo un warning
      this._logger.warning(w, { pIvaS1, pIvaS2 });
      // Aggiorno il flag per il dato specifico
      check_pIvaS = false;
    }
    if (!this.sameArrayRecapiti(recapiti1, recapiti2)) {
      // Variabile di comodo
      let w = `${m} | recapiti`;
      // Loggo un warning
      this._logger.warning(w, { recapiti1, recapiti2 });
      // Aggiorno il flag per il dato specifico
      check_recapiti = false;
    }
    if (!this.sameNaturaGiuridica(tng1, tng2)) {
      // Variabile di comodo
      let w = `${m} | tipo_natura_giuridica`;
      // Loggo un warning
      this._logger.warning(w, { tng1, tng2 });
      // Aggiorno il flag per il dato specifico
      check_tng = false;
    }
    if (!this.sameTipoSoggetto(ts1, ts2)) {
      // Variabile di comodo
      let w = `${m} | tipo_soggetto`;
      // Loggo un warning
      this._logger.warning(w, { ts1, ts2 });
      // Aggiorno il flag per il dato specifico
      check_ts = false;
    }

    // Tutto verificato correttamente
    return (
      check_cfs &&
      check_cen &&
      check_cogn &&
      check_cn &&
      check_dns &&
      check_ds &&
      check_idS &&
      check_nn &&
      check_nome &&
      check_pIvaS &&
      check_recapiti &&
      check_tng &&
      check_ts
    );
  }

  /**
   * Funzione che verifica se un gruppo ha le stesse informazioni di un altro gruppo.
   * @param g1 Gruppo da verificare.
   * @param g2 Gruppo da verificare.
   * @param parseRules ParseValueRules che definisce le logiche di sanitizzazione degli oggetti.
   * @returns boolean che definisce se i soggetti in input hanno le stesse informazioni.
   */
  sameGruppi(g1: Gruppo, g2: Gruppo, parseRules?: ParseValueRules): boolean {
    // Definisco le regole d'applicare sugli oggetti
    const rules = parseRules || new ParseValueRules();
    // Effettuo la sanitificazione degli oggetti
    g1 = this._riscaUtilities.valueRegulated(g1, rules);
    g2 = this._riscaUtilities.valueRegulated(g2, rules);

    // Verifico l'input
    if (!g1 && !g2) {
      // Tutti e due undefined li considero uguali
      return true;
    }
    if (!g1 || !g2) {
      // Uno undefined e uno valorizzato, son diversi
      return false;
    }

    // Estraggo le informazioni da verificare dal primo soggetto
    const des1 = g1.des_gruppo_soggetto;
    const comp1 = g1.componenti_gruppo;
    // Estraggo le informazioni da verificare dal secondo soggetto
    const des2 = g2.des_gruppo_soggetto;
    const comp2 = g2.componenti_gruppo;

    // Definisco dei flag di check di comodo
    let check_des = true;
    let check_comp = true;
    let check_capogruppo = true;
    // Variabili di comodo
    let m = 'compareGruppi';

    // Verifico i campi (NOT_RELATED: https://www.youtube.com/watch?v=VUcWyt0nmHk)
    if (des1 != des2) {
      // Variabile di comodo
      let w = `${m} | des_gruppo_soggetto`;
      // Loggo un warning
      this._logger.warning(w, { des1, des2 });
      // Aggiorno il flag per il dato specifico
      check_des = false;
    }
    if (!this.sameComponentiGruppo(comp1, comp2)) {
      // Variabile di comodo
      let w = `${m} | componenti_gruppo`;
      // Loggo un warning
      this._logger.warning(w, { comp1, comp2 });
      // Aggiorno il flag per il dato specifico
      check_comp = false;
    }
    if (!this.sameCapigruppo(comp1, comp2)) {
      // Variabile di comodo
      let w = `${m} | capigruppo`;
      // Loggo un warning
      this._logger.warning(w, { comp1, comp2 });
      // Aggiorno il flag per il dato specifico
      check_capogruppo = false;
    }

    // Tutto verificato correttamente
    return check_des && check_comp && check_capogruppo;
  }

  /**
   * Funzione di compare tra due oggetti di tipo: DatiTecniciAmbiente.
   * @param dta1 DatiTecniciAmbiente da comparare.
   * @param dta2 DatiTecniciAmbiente da comparare.
   * @param parseRules ParseValueRules che definisce le logiche di sanitizzazione degli oggetti.
   * @returns boolean che definisce il risultato della comparazione.
   */
  compareDatiTecniciAmbiente(
    dta1: IDatiTecniciAmbiente,
    dta2: IDatiTecniciAmbiente,
    parseRules?: ParseValueRules
  ): boolean {
    // Definisco le regole d'applicare sugli oggetti
    const rules = parseRules || new ParseValueRules();
    // Effettuo la sanitificazione degli oggetti
    dta1 = this._riscaUtilities.valueRegulated(dta1, rules);
    dta2 = this._riscaUtilities.valueRegulated(dta2, rules);

    // Verifico l'input
    if (!dta1 && !dta2) {
      // Tutti e due undefined li considero uguali
      return true;
    }
    if (!dta1 || !dta2) {
      // Uno undefined e uno valorizzato, son diversi
      return false;
    }

    // Estraggo le informazioni da verificare dal primo soggetto
    const comune1 = dta1.comune;
    const cic1 = dta1.corpoIdricoCaptazione;
    const nie1 = dta1.nomeImpiantoIdroElettrico;
    const pda1 = dta1.portataDaAssegnare;
    // Estraggo le informazioni da verificare dal secondo soggetto
    const comune2 = dta2.comune;
    const cic2 = dta2.corpoIdricoCaptazione;
    const nie2 = dta2.nomeImpiantoIdroElettrico;
    const pda2 = dta2.portataDaAssegnare;

    // Definisco dei flag di check di comodo
    let check_comune = true;
    let check_cic = true;
    let check_nie = true;
    let check_pda = true;
    // Variabili di comodo
    let m = 'compareDatiTecniciAmbiente';

    // Verifico i campi (NOT_RELATED: https://www.youtube.com/watch?v=VUcWyt0nmHk)
    if (comune1 != comune2) {
      // Variabile di comodo
      let w = `${m} | comune`;
      // Loggo un warning
      this._logger.warning(w, { comune1, comune2 });
      // Aggiorno il flag per il dato specifico
      check_comune = false;
    }
    if (cic1 != cic2) {
      // Variabile di comodo
      let w = `${m} | corpoIdricoCaptazione`;
      // Loggo un warning
      this._logger.warning(w, { cic1, cic2 });
      // Aggiorno il flag per il dato specifico
      check_cic = false;
    }
    if (nie1 != nie2) {
      // Variabile di comodo
      let w = `${m} | nomeImpiantoIdroElettrico`;
      // Loggo un warning
      this._logger.warning(w, { nie1, nie2 });
      // Aggiorno il flag per il dato specifico
      check_nie = false;
    }
    if (pda1 != pda2) {
      // Variabile di comodo
      let w = `${m} | portataDaAssegnare`;
      // Loggo un warning
      this._logger.warning(w, { pda1, pda2 });
      // Aggiorno il flag per il dato specifico
      check_pda = false;
    }

    // Tutto verificato correttamente
    return check_comune && check_cic && check_nie && check_pda;
  }
}
