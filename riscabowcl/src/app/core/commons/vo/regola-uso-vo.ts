import { cloneDeep } from 'lodash';
import { Moment } from 'moment';
import { HelperVo } from './helper-vo';
import { IJsonRegolaVo, JsonRegolaVo } from './json-regola-vo';
import { IUsoLeggeVo, UsoLeggeVo } from './uso-legge-vo';

/**
 * Enum dedicato alla gestione del campo: ; questo campo serve per poter gestire su quale valore bisogna poi riportare le informazioni nell'oggetto figlio: json_regola_obj;
 */
export enum TipoCanoneRegolaUso {
  unitario = 'canone_unitario',
  percentuale = 'canone_percentuale',
}

/**
 * Per l'oggetto RegolaUsoVo, in lettura si ha un oggetto con proprietà differenti rispetto a quelle in scrittura.
 */
export interface IRegolaUsoReadVo {
  id_tipo_uso_regola?: number;
  id_tipo_uso?: number;
  data_inizio?: string; // Data in modalità server format
  data_fine?: string; // Data in modalità server format
  json_regola?: string; // Oggetto json stringhizzato, da parsare; PUO' ESSERE NULL!
  id_algoritmo?: number;
  tipo_uso?: IUsoLeggeVo;
}

/**
 * Per l'oggetto RegolaUsoVo, in lettura si ha un oggetto con proprietà differenti rispetto a quelle in scrittura.
 */
export interface IRegolaUsoWriteVo {
  id_tipo_uso_regola?: number;
  id_tipo_uso?: number;
  data_inizio?: string; // Data in modalità server format
  data_fine?: string; // Data in modalità server format
  json_regola_obj?: IJsonRegolaVo;
  id_algoritmo?: number;
  tipo_uso?: IUsoLeggeVo;
}

export class RegolaUsoVo extends HelperVo {
  id_tipo_uso_regola?: number;
  id_tipo_uso?: number;
  data_inizio?: Moment; // Data in modalità server format
  data_fine?: Moment; // Data in modalità server format
  json_regola?: string; // Oggetto json stringhizzato, da parsare;
  json_regola_obj?: JsonRegolaVo;
  id_algoritmo?: number;
  tipo_uso?: UsoLeggeVo;

  /**
   * number prorietà specifica di FE. Ci sono delle logiche complesse per la gestione del canone per json_regola_obj.
   * Questa proprietà è utilizzata nella tabella che gestisce la lista di oggetti RegolaUsoVo, che tiene traccia del canone_unitario o canone_percentuale per gestirla nella tabella.
   */
  __json_regola_canone?: number;
  /**
   * TipoCanoneRegolaUso prorietà specifica di FE. Ci sono delle logiche complesse per la gestione del canone per json_regola_obj.
   * Questa proprietà è utilizzata per capire come rimappare le informazioni gestite dalla tabella che gestisce questo oggetto. Il flag andrà ad aggiornare: o canone_unitario o canone_percentuale.
   */
  __json_regola_tipo_canone?: TipoCanoneRegolaUso;

  constructor(iRUVo?: IRegolaUsoReadVo | IRegolaUsoWriteVo) {
    super();

    this.id_tipo_uso_regola = iRUVo?.id_tipo_uso_regola;
    this.id_tipo_uso = iRUVo?.id_tipo_uso;
    this.data_inizio = this.convertServerDateToMoment(iRUVo?.data_inizio);
    this.data_fine = this.convertServerDateToMoment(iRUVo?.data_fine);
    this.id_algoritmo = iRUVo?.id_algoritmo;
    this.tipo_uso = new UsoLeggeVo(iRUVo?.tipo_uso);

    // Per le proprietà json regola, essendo proprietà esclusive della tipologia in input, tratto quindi l'input come any per evitare errori di accesso alle proprietà
    const iRUAny = iRUVo as any;
    // Definisco le variabili per l'assegnazione delle informazioni
    let json_regola: string;
    json_regola = iRUAny?.json_regola;
    let json_regola_obj: IJsonRegolaVo;

    // Verifico se esiste un oggetto in input per json_regola_obj
    if (iRUAny?.json_regola_obj != undefined) {
      // Esiste un oggetto in input lo assegno
      json_regola_obj = iRUAny?.json_regola_obj;
      // #
    } else if (iRUAny?.json_regola != undefined) {
      // Dovrebbe esserci una regola come stringa, tento di effettuare il parse come jsonObject
      try { 
        // Conversione della stringa ad oggetto
        json_regola_obj = JSON.parse(iRUAny?.json_regola);
        // #
      } catch (e) {};
      // #
    }
    
    // Ignoro le casistiche e tento l'assegnazione direttamente
    this.json_regola = json_regola;
    this.json_regola_obj = new JsonRegolaVo(json_regola_obj);
  }

  /**
   * Ovverride della funzione toServerFormat.
   * @param toWrite boolean che definisce se il toServerFormat deve generare un oggetto IRegolaUsoReadVo o un oggetto IRegolaUsoWriteVo. Per default: true.
   * @returns IRegolaUsoReadVo | IRegolaUsoWriteVo con il risultato della conversione.
   */
  toServerFormat(
    toWrite: boolean = true
  ): IRegolaUsoReadVo | IRegolaUsoWriteVo {
    // Definisco l'oggetto per il server
    let be: IRegolaUsoReadVo | IRegolaUsoWriteVo;

    // Verifico quale tipo di oggetto si vuole gestire come output per il server
    if (toWrite) {
      // Oggetto target: IRegolaUsoWriteVo
      const beWrite: IRegolaUsoWriteVo = {
        id_tipo_uso_regola: this.id_tipo_uso_regola,
        id_tipo_uso: this.id_tipo_uso,
        data_inizio: this.convertMomentToServerDate(this.data_inizio),
        data_fine: this.convertMomentToServerDate(this.data_fine),
        json_regola_obj: this.json_regola_obj?.toServerFormat(),
        id_algoritmo: this.id_algoritmo,
        tipo_uso: this.tipo_uso?.toServerFormat(),
      };
      // Assegno l'oggetto a quello da restituire
      be = beWrite;
      // #
    } else {
      // Oggetto target: IRegolaUsoReadVo
      const beRead: IRegolaUsoReadVo = {
        id_tipo_uso_regola: this.id_tipo_uso_regola,
        id_tipo_uso: this.id_tipo_uso,
        data_inizio: this.convertMomentToServerDate(this.data_inizio),
        data_fine: this.convertMomentToServerDate(this.data_fine),
        json_regola: this.json_regola,
        id_algoritmo: this.id_algoritmo,
        tipo_uso: this.tipo_uso?.toServerFormat(),
      };
      // Assegno l'oggetto a quello da restituire
      be = beRead;
      // #
    }

    // Ritorno l'oggetto formattato per il be
    return be;
  }

  /**
   * Funzione specifica di utilizzo della classe.
   * Questa funzione va ad analizzare la proprietà: json_regola_obj; andando a riportare le proprietà: canone_unitario o canone_percentuale; per la gestione della tabella.
   * A seconda delle combinazioni di valori, le proprietà: __json_regola_canone e __json_regola_tipo_canone.
   * Le regole sono:
   * - La priorità dati per le proprietà è per: json_regola_obj.canone_unitario;
   * - Se la condizione sopra è false, si deve utilizzare: json_regola_obj.canone_percentuale;
   */
  prepareRegolaUsoVoToTable() {
    // Deve esistere PER FORZA la proprietà json_regola_obj, altrimenti tutta la struttura non sta in piedi
    if (this.json_regola_obj == undefined) {
      // Lancio errore, questo è bloccante
      const e: string = `prepareRegolaUsoVoToTable => json_regola_obj cannot be undefined!`;
      throw new Error(e);
    }

    // Definisco localmente l'oggetto per comodità
    const jsro = this.json_regola_obj;
    // Definisco la condizione principale di gestione per capire come gestire i dati
    let takePercentuale: boolean = false;

    // Definisco le specifiche condizioni per capire se prendere canone_percentuale
    // 1) La proprietà deve esistere
    const case1: boolean = jsro.canone_percentuale != undefined;
    // 2) La proprietà canone_unitario non esiste
    const case2: boolean = jsro.canone_unitario == undefined;

    // Unisco le condizioni nel boolean unico
    takePercentuale = case1 && case2;

    // Verifico la condizione di gestione
    if (takePercentuale) {
      // Devo definire le informazioni sul canone percentuale
      this.__json_regola_canone = jsro.canone_percentuale;
      this.__json_regola_tipo_canone = TipoCanoneRegolaUso.percentuale;
      // #
    } else {
      // Definisco i dati dal canone unitario
      this.__json_regola_canone = jsro.canone_unitario;
      this.__json_regola_tipo_canone = TipoCanoneRegolaUso.unitario;
    }
  }

  /**
   * Funzione specifica di utilizzo della classe.
   * Questa funzione va ad aggiornare le informazioni per: canone_unitario o canone_percentuale; in base alle informazioni presenti nei campi specifici: __json_regola_canone e __json_regola_tipo_canone.
   * Queste informazioni servono per tenere allineate le informazioni degli oggetti innestati della classe.
   */
  updateRegolaUsoVoFromTable() {
    // Deve esistere PER FORZA la proprietà json_regola_obj, altrimenti tutta la struttura non sta in piedi
    if (this.json_regola_obj == undefined) {
      // Lancio errore, questo è bloccante
      const e: string = `updateRegolaUsoVoFromTable => json_regola_obj cannot be undefined!`;
      throw new Error(e);
    }

    // Recupero il valore di supporto del FE
    const canoneFE: number = this.__json_regola_canone;

    // Verifico su quale proprietà è stata mappato il contenitore di FE
    switch (this.__json_regola_tipo_canone) {
      // Unitario
      case TipoCanoneRegolaUso.unitario:
        this.json_regola_obj.canone_unitario = canoneFE;
        break;
      // Percentuale
      case TipoCanoneRegolaUso.percentuale:
        this.json_regola_obj.canone_percentuale = canoneFE;
        break;
    }
  }

  /**
   * Funzione che effettua una "clone" di se stesso.
   * @returns RegolaUsoVo con il risultato della conversione.
   */
  clone(): RegolaUsoVo {
    // Effettuo la toServerFormat
    const be: IRegolaUsoWriteVo = this.toServerFormat();
    // Effettuo un cloneDeep delle informazioni
    const beWClone: IRegolaUsoWriteVo = cloneDeep(be);

    // Rigenero un oggetto regola uso e lo ritorno
    return new RegolaUsoVo(beWClone);
  }
}
