import { LoggerService } from '../../core/services/logger.service';
import { CommonConsts } from '../consts/common-consts.consts';
import { TUserRole } from '../utilities';

/**
 * Interfaccia che definisce la struttura per la gestione di accesso di una sezione/funzione con i ruoli.
 * Le proprietà verranno definite sulla base dell'oggetto costante: CommonConsts.
 */
export interface UserRoleAccess {
  AMMINISTRATORE: boolean;
  GESTORE_BASE: boolean;
  GESTORE_DATI: boolean;
  CONSULTATORE: boolean;
}

/**
 * Classe usata la generazione e gestione degli accessi utente in base al proprio ruolo.
 */
export class UserRoleClass {
  /** Oggetto di costanti comuni all'applicazione. */
  private C_C = new CommonConsts()

  /** UserRoleAccess per la funzionalità: ricercaRiscossione. */
  private _ricercaRiscossione: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: visualizzaRiscossione. */
  private _visualizzaRiscossione: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: modificaRiscossione. */
  private _modificaRiscossione: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: inserisciRiscossione. */
  private _inserisciRiscossione: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: ricercaSoggetto. */
  private _ricercaSoggetto: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: visualizzaSoggetto. */
  private _visualizzaSoggetto: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: modificaSoggetto. */
  private _modificaSoggetto: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: inserisciSoggetto. */
  private _inserisciSoggetto: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: visualizzaGeneraliAmministrativi. */
  private _visualizzaGeneraliAmministrativi: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: modificaGeneraliAmministrativi. */
  private _modificaGeneraliAmministrativi: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: inserisciGeneraliAmministrativi. */
  private _inserisciGeneraliAmministrativi: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: visualizzaDatiTecniciUsi. */
  private _visualizzaDatiTecniciUsi: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: modificaDatiTecniciUsi. */
  private _modificaDatiTecniciUsi: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: inserisciDatiTecniciUsi. */
  private _inserisciDatiTecniciUsi: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: visualizzaDatiAnagrafici. */
  private _visualizzaDatiAnagrafici: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: modificaDASoggetto. */
  private _modificaDatiAnagrafici: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: inserisciDASoggetto. */
  private _inserisciDatiAnagrafici: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: visualizzaPagamenti. */
  private _visualizzaPagamenti: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: visualizzaRimborsi. */
  private _visualizzaRimborsi: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: visualizzaAccertamenti. */
  private _visualizzaAccertamenti: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: visualizzaNAP. */
  private _visualizzaNAP: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: modificaPagamenti. */
  private _modificaPagamenti: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: modificaRimborsi. */
  private _modificaRimborsi: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: modificaAccertamenti. */
  private _modificaAccertamenti: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: modificaNAP. */
  private _modificaNAP: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: inserisciPagamenti. */
  private _inserisciPagamenti: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: inserisciRimborsi. */
  private _inserisciRimborsi: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: inserisciAccertamenti. */
  private _inserisciAccertamenti: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: inserisciNAP. */
  private _inserisciNAP: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: inserisciInteressi. */
  private _inserisciInteressi: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: inserisciDilatazioni. */
  private _inserisciDilatazioni: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: modificaInteressi. */
  private _modificaInteressi: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: modificaDilatazioni. */
  private _modificaDilatazioni: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: inserisciParametriCalcoloCanoneAnnualita. */
  private _inserisciParametriCalcoloCanoneAnnualita: UserRoleAccess;
  /** UserRoleAccess per la funzionalità: emissioneBollettini. */
  private _emissioneBollettini: UserRoleAccess;

  /**
   * Costruttore.
   */
  constructor(private _logger?: LoggerService) {
    // Richiamo il setup della classe
    this.setupClasse();
  }

  /**
   * Funzione di setup che inizializza tutte le proprietà della classe.
   */
  setupClasse() {
    // Definisco due costanti di comodo
    const SI = true;
    const NO = false;

    // Lancio, per ogni proprietà il proprio setup
    this._ricercaRiscossione =                        this.configUserRoleAccess(SI, SI, SI, SI);
    this._visualizzaRiscossione =                     this.configUserRoleAccess(SI, SI, SI, SI);
    this._modificaRiscossione =                       this.configUserRoleAccess(SI, SI, SI, NO);
    this._inserisciRiscossione =                      this.configUserRoleAccess(SI, SI, SI, NO);
    this._ricercaSoggetto =                           this.configUserRoleAccess(SI, SI, SI, SI);
    this._visualizzaSoggetto =                        this.configUserRoleAccess(SI, SI, SI, SI);
    this._modificaSoggetto =                          this.configUserRoleAccess(SI, SI, SI, NO);
    this._inserisciSoggetto =                         this.configUserRoleAccess(SI, SI, SI, NO);
    this._visualizzaGeneraliAmministrativi =          this.configUserRoleAccess(SI, SI, SI, SI);
    this._modificaGeneraliAmministrativi =            this.configUserRoleAccess(SI, SI, SI, NO);
    this._inserisciGeneraliAmministrativi =           this.configUserRoleAccess(SI, SI, SI, NO);
    this._visualizzaDatiTecniciUsi =                  this.configUserRoleAccess(SI, SI, SI, SI);
    this._modificaDatiTecniciUsi =                    this.configUserRoleAccess(SI, SI, SI, NO);
    this._inserisciDatiTecniciUsi =                   this.configUserRoleAccess(SI, SI, SI, NO);
    this._visualizzaDatiAnagrafici =                  this.configUserRoleAccess(SI, SI, SI, SI);
    this._modificaDatiAnagrafici =                    this.configUserRoleAccess(SI, SI, SI, NO);
    this._inserisciDatiAnagrafici =                   this.configUserRoleAccess(SI, SI, SI, NO);
    this._visualizzaPagamenti =                       this.configUserRoleAccess(SI, SI, SI, SI);
    this._visualizzaRimborsi =                        this.configUserRoleAccess(SI, SI, SI, SI);
    this._visualizzaAccertamenti =                    this.configUserRoleAccess(SI, SI, SI, SI);
    this._visualizzaNAP =                             this.configUserRoleAccess(SI, SI, SI, SI);
    this._modificaPagamenti =                         this.configUserRoleAccess(SI, SI, SI, NO);
    this._modificaRimborsi =                          this.configUserRoleAccess(SI, SI, SI, NO);
    this._modificaAccertamenti =                      this.configUserRoleAccess(SI, SI, SI, NO);
    this._modificaNAP =                               this.configUserRoleAccess(SI, SI, SI, NO);
    this._inserisciPagamenti =                        this.configUserRoleAccess(SI, SI, SI, NO);
    this._inserisciRimborsi =                         this.configUserRoleAccess(SI, SI, SI, NO);
    this._inserisciAccertamenti =                     this.configUserRoleAccess(SI, SI, SI, NO);
    this._inserisciNAP =                              this.configUserRoleAccess(SI, SI, SI, NO);
    this._inserisciInteressi =                        this.configUserRoleAccess(SI, SI, NO, NO);
    this._inserisciDilatazioni =                      this.configUserRoleAccess(SI, SI, NO, NO);
    this._modificaInteressi =                         this.configUserRoleAccess(SI, SI, NO, NO);
    this._modificaDilatazioni =                       this.configUserRoleAccess(SI, SI, NO, NO);
    this._inserisciParametriCalcoloCanoneAnnualita =  this.configUserRoleAccess(SI, SI, NO, NO);
    this._emissioneBollettini =                       this.configUserRoleAccess(SI, SI, NO, NO);
  }

  /**
   * Funzione di setup per oggetti di tipo UserRoleAccess.
   * @param amministratore boolean che definisce l'accesso per il ruolo: amministratore.
   * @param gestoreBase boolean che definisce l'accesso per il ruolo: gestoreBase.
   * @param gestoreDati boolean che definisce l'accesso per il ruolo: gestoreDati.
   * @param consultatore boolean che definisce l'accesso per il ruolo: consultatore.
   * @returns UserRoleAccess configurato.
   */
  configUserRoleAccess(
    amministratore: boolean,
    gestoreBase: boolean,
    gestoreDati: boolean,
    consultatore: boolean
  ): UserRoleAccess {
    // Definisco il contenitore del configuratore
    const config = {};
    // Definisco le proprietà/configurazioni
    config[this.C_C.AMMINISTRATORE] = amministratore;
    config[this.C_C.GESTORE_BASE] = gestoreBase;
    config[this.C_C.GESTORE_DATI] = gestoreDati;
    config[this.C_C.CONSULTATORE] = consultatore;
    // Si ritorna un oggetto configurato con i parametri
    return config as UserRoleAccess;
  }

  /**
   * Funzione che verifica e ritorna il possibile accesso ad una sezione, in base al ruolo.
   * @param sezione string che definisce la sezione da verificare.
   * @param ruolo TUserRole che definisce il ruolo da verificare.
   * @returns boolean che definisce se è possibile accedere.
   */
  checkSezione(sezione: string, ruolo: TUserRole): boolean {
    // Verifico l'input
    if (!sezione || !ruolo) return false;

    // Per assicurarmi non ci siano violazioni uso un try catch
    try {
      // Check su sezione/ruolo
      const check = this[sezione][ruolo];
      // Ritorno il controllo
      return check;
      // #
    } catch (e) {
      // Loggo l'errore
      if (this._logger) this._logger.error('checkSectionRole', sezione + ' | Verificare definizione dei getter.');
      if (this._logger) this._logger.error('checkSectionRole', ruolo + ' | Verificare definizione dei getter.');
      // Ritorno false
      return false;
    }
  }

  /**
   * ######
   * GETTER
   * ######
   */

  /**
   * Getter per _ricercaRiscossione.
   * @returns UserRoleAccess.
   */
  get ricercaRiscossione(): UserRoleAccess {
    return this._ricercaRiscossione;
  }

  /**
   * Getter per _visualizzaRiscossione.
   * @returns UserRoleAccess.
   */
  get visualizzaRiscossione(): UserRoleAccess {
    return this._visualizzaRiscossione;
  }

  /**
   * Getter per _modificaRiscossione.
   * @returns UserRoleAccess.
   */
  get modificaRiscossione(): UserRoleAccess {
    return this._modificaRiscossione;
  }

  /**
   * Getter per _inserisciRiscossione.
   * @returns UserRoleAccess.
   */
  get inserisciRiscossione(): UserRoleAccess {
    return this._inserisciRiscossione;
  }

  /**
   * Getter per _ricercaSoggetto.
   * @returns UserRoleAccess.
   */
  get ricercaSoggetto(): UserRoleAccess {
    return this._ricercaSoggetto;
  }

  /**
   * Getter per _visualizzaSoggetto.
   * @returns UserRoleAccess.
   */
  get visualizzaSoggetto(): UserRoleAccess {
    return this._visualizzaSoggetto;
  }

  /**
   * Getter per _modificaSoggetto.
   * @returns UserRoleAccess.
   */
  get modificaSoggetto(): UserRoleAccess {
    return this._modificaSoggetto;
  }

  /**
   * Getter per _inserisciSoggetto.
   * @returns UserRoleAccess.
   */
  get inserisciSoggetto(): UserRoleAccess {
    return this._inserisciSoggetto;
  }

  /**
   * Getter per _visualizzaGeneraliAmministrativi.
   * @returns UserRoleAccess.
   */
  get visualizzaGeneraliAmministrativi(): UserRoleAccess {
    return this._visualizzaGeneraliAmministrativi;
  }

  /**
   * Getter per _modificaGeneraliAmministrativi.
   * @returns UserRoleAccess.
   */
  get modificaGeneraliAmministrativi(): UserRoleAccess {
    return this._modificaGeneraliAmministrativi;
  }

  /**
   * Getter per _inserisciGeneraliAmministrativi.
   * @returns UserRoleAccess.
   */
  get inserisciGeneraliAmministrativi(): UserRoleAccess {
    return this._inserisciGeneraliAmministrativi;
  }
  
  /**
   * Getter per _visualizzaDatiAnagrafici.
   * @returns UserRoleAccess.
   */
  get visualizzaDatiAnagrafici(): UserRoleAccess {
    return this._visualizzaDatiAnagrafici;
  }
  
  /**
   * Getter per _modificaDatiAnagrafici.
   * @returns UserRoleAccess.
   */
  get modificaDASoggetto(): UserRoleAccess {
    return this._modificaDatiAnagrafici;
  }
  
  /**
   * Getter per _inserisciDatiAnagrafici.
   * @returns UserRoleAccess.
   */
  get inserisciDASoggetto(): UserRoleAccess {
    return this._inserisciDatiAnagrafici;
  }

  /**
   * Getter per _visualizzaDatiTecniciUsi.
   * @returns UserRoleAccess.
   */
  get visualizzaDatiTecniciUsi(): UserRoleAccess {
    return this._visualizzaDatiTecniciUsi;
  }

  /**
   * Getter per _modificaDatiTecniciUsi.
   * @returns UserRoleAccess.
   */
  get modificaDatiTecniciUsi(): UserRoleAccess {
    return this._modificaDatiTecniciUsi;
  }

  /**
   * Getter per _inserisciDatiTecniciUsi.
   * @returns UserRoleAccess.
   */
  get inserisciDatiTecniciUsi(): UserRoleAccess {
    return this._inserisciDatiTecniciUsi;
  }

  /**
   * Getter per _visualizzaPagamenti.
   * @returns UserRoleAccess.
   */
  get visualizzaPagamenti(): UserRoleAccess {
    return this._visualizzaPagamenti;
  }

  /**
   * Getter per _visualizzaRimborsi.
   * @returns UserRoleAccess.
   */
  get visualizzaRimborsi(): UserRoleAccess {
    return this._visualizzaRimborsi;
  }

  /**
   * Getter per _visualizzaAccertamenti.
   * @returns UserRoleAccess.
   */
  get visualizzaAccertamenti(): UserRoleAccess {
    return this._visualizzaAccertamenti;
  }

  /**
   * Getter per _visualizzaNAP.
   * @returns UserRoleAccess.
   */
  get visualizzaNAP(): UserRoleAccess {
    return this._visualizzaNAP;
  }

  /**
   * Getter per _modificaPagamenti.
   * @returns UserRoleAccess.
   */
  get modificaPagamenti(): UserRoleAccess {
    return this._modificaPagamenti;
  }

  /**
   * Getter per _modificaRimborsi.
   * @returns UserRoleAccess.
   */
  get modificaRimborsi(): UserRoleAccess {
    return this._modificaRimborsi;
  }

  /**
   * Getter per _modificaAccertamenti.
   * @returns UserRoleAccess.
   */
  get modificaAccertamenti(): UserRoleAccess {
    return this._modificaAccertamenti;
  }

  /**
   * Getter per _modificaNAP.
   * @returns UserRoleAccess.
   */
  get modificaNAP(): UserRoleAccess {
    return this._modificaNAP;
  }

  /**
   * Getter per _inserisciPagamenti.
   * @returns UserRoleAccess.
   */
  get inserisciPagamenti(): UserRoleAccess {
    return this._inserisciPagamenti;
  }

  /**
   * Getter per _inserisciRimborsi.
   * @returns UserRoleAccess.
   */
  get inserisciRimborsi(): UserRoleAccess {
    return this._inserisciRimborsi;
  }

  /**
   * Getter per _inserisciAccertamenti.
   * @returns UserRoleAccess.
   */
  get inserisciAccertamenti(): UserRoleAccess {
    return this._inserisciAccertamenti;
  }

  /**
   * Getter per _inserisciNAP.
   * @returns UserRoleAccess.
   */
  get inserisciNAP(): UserRoleAccess {
    return this._inserisciNAP;
  }

  /**
   * Getter per _inserisciInteressi.
   * @returns UserRoleAccess.
   */
  get inserisciInteressi(): UserRoleAccess {
    return this._inserisciInteressi;
  }

  /**
   * Getter per _inserisciDilatazioni.
   * @returns UserRoleAccess.
   */
  get inserisciDilatazioni(): UserRoleAccess {
    return this._inserisciDilatazioni;
  }

  /**
   * Getter per _modificaInteressi.
   * @returns UserRoleAccess.
   */
  get modificaInteressi(): UserRoleAccess {
    return this._modificaInteressi;
  }

  /**
   * Getter per _modificaDilatazioni.
   * @returns UserRoleAccess.
   */
  get modificaDilatazioni(): UserRoleAccess {
    return this._modificaDilatazioni;
  }

  /**
   * Getter per _inserisciParametriCalcoloCanoneAnnualita.
   * @returns UserRoleAccess.
   */
  get inserisciParametriCalcoloCanoneAnnualita(): UserRoleAccess {
    return this._inserisciParametriCalcoloCanoneAnnualita;
  }

  /**
   * Getter per _emissioneBollettini.
   * @returns UserRoleAccess.
   */
  get emissioneBollettini(): UserRoleAccess {
    return this._emissioneBollettini;
  }
}
