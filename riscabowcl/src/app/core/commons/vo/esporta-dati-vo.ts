import * as moment from 'moment';
import { Moment } from 'moment';
import { CodiciTipiElaboraReport } from '../../../features/report/service/esporta-dati/utilities/esporta-dati.enums';
import {
  IReportBilancioParams,
  IReportVincoliCompetenzaParams,
} from '../../../features/report/service/report/utilities/report.interfaces';
import { RiscaNotifyCodes } from '../../../shared/utilities/enums/risca-notify-codes.enums';
import { HelperVo } from './helper-vo';
import {
  ITipoElaborazioneVo,
  TipoElaborazioneVo,
} from './tipo-elaborazione-vo';

export interface IEsportaDatiVo {
  dataDa?: string; // Server date string
  dataA?: string; // Server date string
  tipoElaboraReport?: ITipoElaborazioneVo;
}

/**
 * Classe che gestisce le informazioni per la gestione dell'esporta dati.
 */
export class EsportaDatiVo extends HelperVo {
  dataDa: Moment;
  dataA: Moment;
  tipoElaboraReport: TipoElaborazioneVo;

  constructor(iEDVo?: IEsportaDatiVo) {
    super();

    this.dataDa = this.convertServerDateToMoment(iEDVo?.dataDa);
    this.dataA = this.convertServerDateToMoment(iEDVo?.dataA);

    if (iEDVo?.tipoElaboraReport) {
      this.tipoElaboraReport = new TipoElaborazioneVo(iEDVo?.tipoElaboraReport);
    }
  }

  /**
   * Funzione che converte le informazioni dello stato debitorio in informazioni leggibili dal server.
   * Verranno trasformate tutte le variabili da una struttura FE friendly, in BE like.
   * @override
   */
  toServerFormat(): IEsportaDatiVo {
    // Definisco l'oggetto da ritornare al server
    const be: IEsportaDatiVo = {
      dataDa: this.convertMomentToServerDate(this.dataDa),
      dataA: this.convertMomentToServerDate(this.dataA),
      tipoElaboraReport: this.tipoElaboraReport?.toServerFormat(),
    };

    // Ritorno l'oggetto per il be
    return be;
  }

  /**
   * ##############
   * VERIFICHE DATI
   * ##############
   */

  /**
   * Funzione che verifica delle condizioni specifiche sulle informazioni della classe.
   * Per le verifiche di questa funzione si implementano i seguenti controlli:
   * - Stesso anno per "Data Da" e "Data A"; "Data Da" sia il 1° Gennaio; "Data A" sia il 31° Dicembre; l'anno non sia quello corrente;
   * oppure
   * - Stesso anno per "Data Da" e "Data A"; "Data Da" sia il 1° Gennaio; "Data A" sia il 31° Dicembre o data odierna; l'anno sia quello corrente;
   * Se non si rientra in questo caso specifico, verrà ritornato un codice messaggio: I046.
   * @returns RiscaNotifyCodes se bisogna segnalare qualcosa all'utente. Se i controlli sono passati, verrà ritornato undefined.
   */
  checkDataPerReportBilancio(): RiscaNotifyCodes {
    // Verifico se il tipo elaborazione è per il report bilancio
    if (!this.elaboraReportBilancio) {
      // L'oggetto non è definito come report bilancio
      return undefined;
    }

    // Recupero data DA e data A
    const dataDa: Moment = this.dataDa.clone();
    const dataA: Moment = this.dataA.clone();
    // Per questioni di verifiche, imposto l'orario a mezzanotte
    dataDa.startOf('day');
    dataA.startOf('day');
    // Verifico la validità delle date
    const validDataDa: boolean = dataDa && dataDa.isValid();
    const validDataA: boolean = dataA && dataA.isValid();

    // Recupero le informazioni dalle date
    const dataDaAnno: number = dataDa?.year();
    const dataAAnno: number = dataA?.year();
    const annoCorrente: number = moment().year();
    // Verifico se l'anno è differente
    const annoDifferente: boolean = dataDaAnno !== dataAAnno;

    // Verifico che esistano le date
    if (!validDataDa || !validDataA || annoDifferente) {
      // Non posso controllare
      return undefined;
    }

    // Definisco altre variabili per le verifiche
    const stessoAnnoCorrente = dataDaAnno === annoCorrente;

    // Verifico se sono nell'anno corrente
    if (stessoAnnoCorrente) {
      // Stesso anno, verifico le date
      return this.checkDataRBAnnoCorrente(dataDa, dataA);
      // #
    } else {
      // Anno differente, verifico le date
      return this.checkDataRBAnnoNonCorrente(dataDa, dataA, dataDaAnno);
      // #
    }
  }

  /**
   * Funzione che continua il flusso della funzione "checkDataPerReportBilancio".
   * Alcuni controlli vengono ignorati poiché ereditati dalla funzione principale.
   * @param dataDa Moment con la data da di riferimento.
   * @param dataA Moment con la data a di riferimento.
   * @returns RiscaNotifyCodes se bisogna segnalare qualcosa all'utente. Se i controlli sono passati, verrà ritornato undefined.
   */
  private checkDataRBAnnoCorrente(dataDa: Moment, dataA: Moment) {
    // Definisco altre variabili per le verifiche
    const primoGiornoAnno = moment().startOf('year').startOf('day');
    const ultimoGiornoAnno = moment().endOf('year').startOf('day');
    const oggi = moment().startOf('day');
    // Definisco le logiche per la verifica sul primo giorno dell'anno
    const isFirstDayYear = dataDa.isSame(primoGiornoAnno);
    // Definisco le logiche per la verifica sull'ultimo giorno dell'anno
    const isLastDayYear = dataA.isSame(ultimoGiornoAnno);
    // Definisco le logiche per la verifica sul giorno corrente
    const isToday = dataA.isSame(oggi);

    // Verifico le condizioni per la segnalazione
    if (!isFirstDayYear || !(isLastDayYear || isToday)) {
      // Le date non sono valide
      return RiscaNotifyCodes.I046;
      // #
    }

    // Date valide
    return undefined;
  }

  /**
   * Funzione che continua il flusso della funzione "checkDataPerReportBilancio".
   * Alcuni controlli vengono ignorati poiché ereditati dalla funzione principale.
   * @param dataDa Moment con la data da di riferimento.
   * @param dataA Moment con la data a di riferimento.
   * @param annoRiferimento number con l'anno di riferimento.
   * @returns RiscaNotifyCodes se bisogna segnalare qualcosa all'utente. Se i controlli sono passati, verrà ritornato undefined.
   */
  private checkDataRBAnnoNonCorrente(
    dataDa: Moment,
    dataA: Moment,
    annoRiferimento: number
  ) {
    // Definisco altre variabili per le verifiche
    const primoGiornoAnno = moment()
      .set('year', annoRiferimento)
      .startOf('year')
      .startOf('day');
    const ultimoGiornoAnno = moment()
      .set('year', annoRiferimento)
      .endOf('year')
      .startOf('day');
    // Definisco le logiche per la verifica sul primo giorno dell'anno
    const isFirstDayYear = dataDa.isSame(primoGiornoAnno);
    // Definisco le logiche per la verifica sull'ultimo giorno dell'anno
    const isLastDayYear = dataA.isSame(ultimoGiornoAnno);

    // Verifico le condizioni per la segnalazione
    if (!isFirstDayYear || !isLastDayYear) {
      // Le date non sono valide
      return RiscaNotifyCodes.I046;
      // #
    }

    // Date valide
    return undefined;
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che genera e ritorna l'oggetto parziale con le informazioni per la gestione dei query param di ricerca per la generazione report bilancio.
   * @notes L'anno di inizio e l'anno di fine dentro le date della classe dovranno essere gli stessi. Questo controllo però non viene eseguito e si prende l'anno di inizio come riferimento, se non esiste, l'anno di fine.
   * @returns Partial<IReportBilancioParams> con le informazioni generate.
   */
  get reportBilancioParams(): Partial<IReportBilancioParams> {
    // Recupero la data inizio e la uso inizialmente come riferimento
    let dataRef: Moment = this.dataDa;
    // Verifico se esiste la data
    if (!dataRef || !dataRef.isValid()) {
      // La data inizio non è valida, provo con quella di fine
      dataRef = this.dataA;
      // Verifico di nuovo
      if (!dataRef || dataRef.isValid()) {
        // Anche la data fine non è valida, ritorno oggetto vuoto
        return { anno: null };
        // #
      }
    }

    // La data di riferimento è valorizzata, recupero l'anno
    const anno: number = dataRef.year();
    // Genero e ritorno l'oggetto
    return { anno };
  }

  /**
   * Getter che genera e ritorna l'oggetto parziale con le informazioni per la gestione dei query param di ricerca per la generazione report variazioni competenza.
   * @returns Partial<IReportVincoliCompetenzaParams> con le informazioni generate.
   */
  get reportVariazioniCompetenzaParams(): Partial<IReportVincoliCompetenzaParams> {
    // Recupero la data d'inizio
    const dataInizioM: Moment = this.dataDa;
    // Recupero la data di fine
    const dataFineM: Moment = this.dataA;

    // Converto le date in string
    const dataInizio: string = this.convertMomentToServerDate(dataInizioM);
    const dataFine: string = this.convertMomentToServerDate(dataFineM);

    // Genero e ritorno l'oggetto
    return { dataInizio, dataFine };
  }

  /**
   * Getter che verifica se la configurazione dell'elaborazione report è di un tipo specifico.
   * @returns boolean con il risultato del check.
   */
  get elaboraReportBilancio(): boolean {
    // Verifico se il tipo elaborazione è per il report bilancio
    const codTER: string = this.tipoElaboraReport?.cod_tipo_elabora;
    // Verifico il codice
    return codTER === CodiciTipiElaboraReport.reportBilancio;
  }

  /**
   * Getter che verifica se la configurazione dell'elaborazione report è di un tipo specifico.
   * @returns boolean con il risultato del check.
   */
  get elaboraReportVariazioniCompetenza(): boolean {
    // Verifico se il tipo elaborazione è per il report bilancio
    const codTER: string = this.tipoElaboraReport?.cod_tipo_elabora;
    // Verifico il codice
    return codTER === CodiciTipiElaboraReport.reportVariazioniCompetenza;
  }
}
