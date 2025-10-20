import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { IUsoLeggeVo } from '../../../../../../../core/commons/vo/uso-legge-vo';
import { UserService } from '../../../../../../../core/services/user.service';
import { RiscaCompareService } from '../../../../../../../shared/services/risca/risca-compare.service';
import { RiscaUtilitiesService } from '../../../../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  DTAmbienteFlagManuale,
  ParseValueRules,
} from '../../../../../../../shared/utilities';
import { DatiTecniciAmbienteConsts } from '../../../../../consts/quadri-tecnici/dati-tecnici-ambiente/dati-tecnici-ambiente.consts';
import {
  IDatiTecniciAmbiente,
  IDatiUsoLeggeReq,
  UsoLeggePSDAmbienteInfo,
} from '../../../utilities/interfaces/dt-ambiente-pratica.interfaces';
import { DatiTecniciService } from '../../dati-tecnici/dati-tecnici.service';

@Injectable({ providedIn: 'root' })
export class DatiTecniciAmbienteService {
  /** Oggetto contenente le costanti utilizzate nel componente. */
  DTA_C = new DatiTecniciAmbienteConsts();

  /**
   * Costruttore
   */
  constructor(
    private _datiTecnici: DatiTecniciService,
    private _riscaCompare: RiscaCompareService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {}

  /**
   * Funzione di comodo, utilizzata per la verifica formale di quantità e quantità falda profonda.
   * @param quantita number quantita da verificare.
   * @param quantitaFalda number quantita falda da verificare.
   * @param skipCtrlQuantita boolean che permette di saltare il controllo di dimensione tra quantità. Per default è false.
   * @returns boolean che definisce se quantita e quantita falda sono valori validi
   */
  checkQuantitaFalda(
    quantita: number,
    quantitaFalda: number,
    skipCtrlQuantita: boolean = false
  ): boolean {
    // Verifico che le quantita esistano
    const existQuantita = quantita !== undefined && quantita !== null;
    const existQuantitaFalda =
      quantitaFalda !== undefined && quantitaFalda !== null;
    const existsQuantita = existQuantita && existQuantitaFalda;

    // Definisco il controllo sulle quantita
    let verificaQuantita = true;
    // Verifico se il controllo sulle quantita è da saltare
    if (!skipCtrlQuantita) {
      verificaQuantita = quantita >= quantitaFalda;
    }

    // Controllo e ritorno che gli input esistano e che quantita sia maggiore uguale a quantita falda
    return existsQuantita && verificaQuantita;
  }

  /**
   * Funzione che calcola il valore della % falda profonda da assegnare poi al form usiForm.
   * @param quantita number quantita per il calcolo.
   * @param quantitaFaldaProfonda number quantita falda profonda per il calcolo.
   * @param decimalPrecision number che definisce la precisione decimale del risultato.
   * @returns number della percentuale falda profonda calcolata.
   */
  calcolaPercentualeFaldaProfonda(
    quantita: number,
    quantitaFaldaProfonda: number,
    decimalPrecision: number
  ): number {
    // Verifico se entrambi i numeri sono a 0
    if (quantita === 0 && quantitaFaldaProfonda === 0) {
      // Dato che non si può dividere per 0
      return 0;
    }

    // Effettuo il calcolo puro per la percentuale
    const n: number = (quantitaFaldaProfonda / quantita) * 100;
    // Gestisco e ritorno il valore effettivo
    return this._riscaUtilities.arrotondaEccesso(n, decimalPrecision);
  }

  /**
   * Funzione che calcola il valore della quantita falda superficiale da assegnare poi al form usiForm.
   * @param quantita number quantita per il calcolo.
   * @param quantitaFaldaProfonda number quantita falda profonda per il calcolo.
   * @param decimalPrecision number che definisce la precisione decimale del risultato.
   * @returns number della quantita falda superficiale.
   */
  calcolaQuantitaFaldaSuperficiale(
    quantita: number,
    quantitaFaldaProfonda: number,
    decimalPrecision: number
  ): number {
    // Effettuo il calcolo puro per la percentuale
    const n: number = quantita - quantitaFaldaProfonda;
    // Calcolo la percentuale di falda profonda
    return this._riscaUtilities.arrotondaEccesso(n, decimalPrecision);
  }

  /**
   * Funzione che calcola il valore della % falda superficiale da assegnare poi al form usiForm.
   * @param quantita number quantita per il calcolo.
   * @param quantitaFaldaSuperficiale number quantita falda superficiale per il calcolo.
   * @param decimalPrecision number che definisce la precisione decimale del risultato.
   * @returns number della percentuale falda superficiale
   */
  calcolaPercentualeFaldaSuperficiale(
    quantita: number,
    quantitaFaldaSuperficiale: number,
    decimalPrecision: number
  ): number {
    // Verifico se entrambi i numeri sono a 0
    if (quantita === 0 && quantitaFaldaSuperficiale === 0) {
      // Dato che non si può dividere per 0
      return 0;
    }

    // Effettuo il calcolo puro per la percentuale
    const n: number = (quantitaFaldaSuperficiale / quantita) * 100;
    // Calcolo la percentuale di falda profonda
    return this._riscaUtilities.arrotondaEccesso(n, decimalPrecision);
  }

  /**
   * Funzione che disabilita gli usi di legge, nell'array [listaUsiLegge] se presenti all'interno dell'array [usiLeggeIns].
   * @param listaUsiLegge Array di UsoLeggeVo da aggiornare con la disabilitazione dei singoli oggetti.
   * @param usiLeggeIns Array di UsoDiLeggeInfo che definisce quali oggetti disabilitare.
   * @returns Array di UsoLeggeVo aggiornato.
   */
  disabilitaUsiDiLegge(
    listaUsiLegge: IUsoLeggeVo[],
    usiLeggeIns: UsoLeggePSDAmbienteInfo[]
  ): IUsoLeggeVo[] {
    // Verifico che le liste esistano
    if (!listaUsiLegge || !usiLeggeIns) {
      return listaUsiLegge;
    }

    // Creo una copia dell'array
    let listaUsiLeggeUpd = [...listaUsiLegge];
    // Setto a false tutti i disabled della lista
    listaUsiLeggeUpd = listaUsiLeggeUpd.map((u: any) => {
      // Resetto il disabled
      u.__disabled = false;
      // Ritorno l'oggetto mappato
      return u;
    });

    // Ciclo gli usi inseriti
    usiLeggeIns.forEach((usoIns: UsoLeggePSDAmbienteInfo) => {
      // Cerco all'interno della lista degli usi di legge, se è presente l'uso di legge iterato
      const idUso = listaUsiLeggeUpd.findIndex((u: IUsoLeggeVo) => {
        // Devo ignorare la select
        const selectIdTipoUso = u != undefined && u?.id_tipo_uso != undefined;
        // Verifico se c'è uguaglianza d'id
        return (
          selectIdTipoUso && u?.id_tipo_uso === usoIns?.usoDiLegge?.id_tipo_uso
        );
      });

      // Se è stata trovata referenza aggiorno l'oggetto in listaUsiLeggeUpd tramite indice
      if (idUso !== -1) {
        // Aggiorno l'oggetto
        listaUsiLeggeUpd[idUso]['__disabled'] = true;
      }
    });

    // Ritorno la lista aggiornata
    return listaUsiLeggeUpd;
  }

  /**
   * Funzione che recupera le informazioni dal form usiForm per il calcolo automatico delle informazioni collegate alle falde.
   * @param usiForm FormGroup per l'aggiornamento dati.
   */
  calcolaQuantitaPerUsi(usiForm: FormGroup) {
    // Verifico che i campi siano validi
    const validQ = usiForm.get(this.DTA_C.QUANTITA).valid;
    const validQFP = usiForm.get(this.DTA_C.QUANTITA_FALDA_PROFONDA).valid;

    // Se uno dei due valori non è valido, blocco il flusso
    if (!validQ || !validQFP) {
      return;
    }

    // Recupero l'informazione dal form per la quantità
    const quantita = this._riscaUtilities.getFormValue(
      usiForm,
      this.DTA_C.QUANTITA
    );
    // Recupero l'informazione dal form per la quantità falda profonda
    const quantitaFaldaProfonda = this._riscaUtilities.getFormValue(
      usiForm,
      this.DTA_C.QUANTITA_FALDA_PROFONDA
    );
    // Controllo che le informazioni esistano e che siano valide
    const checkQuantitaFalda = this.checkQuantitaFalda(
      quantita,
      quantitaFaldaProfonda
    );

    // Se invalide resetto i campi
    if (!checkQuantitaFalda) {
      // Reset dei campi associati
      this._riscaUtilities.setFormValue(
        usiForm,
        this.DTA_C.PERC_FALDA_PROFONDA,
        null
      );
      this._riscaUtilities.setFormValue(
        usiForm,
        this.DTA_C.QUANTITA_FALDA_SUPERFICIALE,
        null
      );
      this._riscaUtilities.setFormValue(
        usiForm,
        this.DTA_C.PERC_FALDA_SUPERFICIALE,
        null
      );
      // Blocco le logiche
      return;
    }

    // Calcolo il valore per % falda profonda
    this.calcolaPercFaldaProfonda(usiForm, quantita, quantitaFaldaProfonda);
    // Calcolo il valore per quantita falda superficiale
    this.calcolaQntFaldaSuperficiale(usiForm, quantita, quantitaFaldaProfonda);

    // Recupero l'informazione dal form per la quantità falda superficiale
    const quantitaFaldaSuperficiale = this._riscaUtilities.getFormValue(
      usiForm,
      this.DTA_C.QUANTITA_FALDA_SUPERFICIALE
    );
    // Calcolo il valore per % falda superficiale
    this.calcolaPercFaldaSuperficiale(
      usiForm,
      quantita,
      quantitaFaldaSuperficiale
    );
  }

  /**
   * Funzione che calcola il valore della % falda profonda da assegnare poi al form usiForm.
   * @param usiForm FormGroup per l'aggiornamento dati.
   * @param quantita number quantita per il calcolo.
   * @param quantitaFaldaProfonda number quantita falda profonda per il calcolo.
   */
  private calcolaPercFaldaProfonda(
    usiForm: FormGroup,
    quantita: number,
    quantitaFaldaProfonda: number
  ) {
    // Calcolo la percentuale di falda profonda
    let percFaldaProfonda: number;
    percFaldaProfonda = this.calcolaPercentualeFaldaProfonda(
      quantita,
      quantitaFaldaProfonda,
      2 // this.DTA_C.DECIMAL_PRECISION
    );

    // Assegno il valore alla form
    this._riscaUtilities.setFormValue(
      usiForm,
      this.DTA_C.PERC_FALDA_PROFONDA,
      percFaldaProfonda
    );
  }

  /**
   * Funzione che calcola il valore della quantita falda superficiale da assegnare poi al form usiForm.
   * @param usiForm FormGroup per l'aggiornamento dati.
   * @param quantita number quantita per il calcolo.
   * @param quantitaFaldaProfonda number quantita falda profonda per il calcolo.
   */
  private calcolaQntFaldaSuperficiale(
    usiForm: FormGroup,
    quantita: number,
    quantitaFaldaProfonda: number
  ) {
    // Calcolo la percentuale di falda profonda
    let quantitaFaldaSuperficiale: number;
    quantitaFaldaSuperficiale = this.calcolaQuantitaFaldaSuperficiale(
      quantita,
      quantitaFaldaProfonda,
      4 // this.DTA_C.DECIMAL_PRECISION
    );

    // Assegno il valore alla form
    this._riscaUtilities.setFormValue(
      usiForm,
      this.DTA_C.QUANTITA_FALDA_SUPERFICIALE,
      quantitaFaldaSuperficiale
    );
  }

  /**
   * Funzione che calcola il valore della % falda superficiale da assegnare poi al form usiForm.
   * NOTA: il valore del form control quantitaFaldaSuperficiale deve essere già stato calcolato.
   * @param usiForm FormGroup per l'aggiornamento dati.
   * @param quantita number quantita per il calcolo.
   * @param quantitaFaldaSuperficiale number quantita falda superficiale per il calcolo.
   */
  private calcolaPercFaldaSuperficiale(
    usiForm: FormGroup,
    quantita: number,
    quantitaFaldaSuperficiale: number
  ) {
    // Calcolo la percentuale di falda superficiale
    const percFaldaSuperficiale = this.calcolaPercentualeFaldaSuperficiale(
      quantita,
      quantitaFaldaSuperficiale,
      2 // this.DTA_C.DECIMAL_PRECISION
    );

    // Assegno il valore alla form
    this._riscaUtilities.setFormValue(
      usiForm,
      this.DTA_C.PERC_FALDA_SUPERFICIALE,
      percFaldaSuperficiale
    );
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
    // Richiamo la funzione di conversione
    return this._riscaCompare.compareDatiTecniciAmbiente(
      dta1,
      dta2,
      parseRules
    );
  }

  /**
   * Funzione che genera la richiesta dati per le informazioni relative ad un uso di legge.
   * @param usoDiLegge UsoLeggeVo per il recupero delle informazioni collegate.
   * @param isGestioneManuale boolean con il flag per la gestione manuale dei dati.
   * @returns IDatiUsoLeggeReq con l'insieme di richieste per i dati relativi all'uso di legge.
   */
  datiCorrelatiUsoLeggeReq(
    usoDiLegge: IUsoLeggeVo,
    isGestioneManuale: boolean
  ): IDatiUsoLeggeReq {
    // Recupero il valore della gestione manuale
    const gestioneManuale = isGestioneManuale;
    const flgManuale = gestioneManuale
      ? DTAmbienteFlagManuale.tutti
      : DTAmbienteFlagManuale.liberi;

    // Informazione per lo scarico dati
    const idUso = usoDiLegge.id_tipo_uso;
    const idAmbito = this._user.idAmbito;
    const idUnitaMisuraUso = usoDiLegge.id_unita_misura;

    // Definisco l'insieme delle chiamate dei dati collegati all'uso di legge
    const usoLeggeReq: IDatiUsoLeggeReq = {
      usiSpecifici: this._datiTecnici.getUsiDiLegge(idAmbito, idUso),
      unitaMisura: this._datiTecnici.getUnitaDiMisura(idUnitaMisuraUso),
      riduzioni: this._datiTecnici.getRiduzioniFlagManuale(idUso, flgManuale),
      aumenti: this._datiTecnici.getAumentiFlagManuale(idUso, flgManuale),
    };

    // Ritorno l'oggetto con la richiesta
    return usoLeggeReq;
  }
}
