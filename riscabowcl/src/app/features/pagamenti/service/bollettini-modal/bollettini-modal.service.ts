import { Injectable } from '@angular/core';
import { ElaborazioneVo } from 'src/app/core/commons/vo/elaborazione-vo';
import { StatoElaborazioneVo } from '../../../../core/commons/vo/stato-elaborazione-vo';
import { TipoElaborazioneVo } from '../../../../core/commons/vo/tipo-elaborazione-vo';
import { UserService } from '../../../../core/services/user.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { RiscaModalService } from '../../../../shared/services/risca/risca-modal.service';
import {
  DynamicObjAny,
  ICallbackDataModal,
} from '../../../../shared/utilities';
import { ApriModalConfigs } from '../../../../shared/utilities/classes/risca-modal/risca-modal.classes';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { IApriModalConfigsForClass } from '../../../../shared/utilities/interfaces/risca/risca-modal/risca-modal.interfaces';
import { NPModalitaModale } from '../../component/bollettini/utilities/nuova-prenotazione.enums';
import { BollettiniModalConsts } from '../../consts/bollettini-modal/bollettini-modal.consts';
import { CodiciStatiElaborazione } from '../../enums/gestione-pagamenti/stato-elaborazione.enums';
import { TipoParametroElaborazione } from '../../enums/pagamenti/pagamenti.enums';
import { IBollettiniModalConfigs } from '../../interfaces/bollettini/bollettini.interfaces';
import { IBollettiniModalDataConfigs } from '../../modal/bollettini-modal/utilities/bollettini-modal.interfaces';
import { BollettiniService } from '../bollettini/bollettini.service';
import { PagamentiService } from '../pagamenti/pagamenti.service';
import { BollettiniModalConverterService } from './bollettini-modal-converter.service';

@Injectable({ providedIn: 'root' })
export class BollettiniModalService {
  /** Oggetto contenente le costanti per il componente attuale. */
  BM_C = new BollettiniModalConsts();

  /**
   * Costruttore
   */
  constructor(
    private _bollettini: BollettiniService,
    private _bmConverter: BollettiniModalConverterService,
    private _pagamenti: PagamentiService,
    private _riscaMessages: RiscaMessagesService,
    private _riscaModal: RiscaModalService,
    private _user: UserService
  ) {}

  /**
   * #####################
   * CONFIGURAZIONE MODALI
   * #####################
   */

  /**
   * Funzione che gestisce le configurazioni e apre la modale di gestione per una prenotazione.
   * @param config BollettiniModalConfigs con le configurazioni per l'apertura delle modali.
   */
  openPrenotazioneModal(config: IBollettiniModalConfigs) {
    // Verifico l'input
    if (!config) {
      // Nessuna configurazione, blocco il flusso
      return;
    }

    // Estraggo i parametri dalla configurazione
    const { callbacks, modalMap, dataModal } = config;
    // Definisco una variabile per il componente da passare
    let te = this.tipoElaborazioneModale(dataModal);
    // Recupero il componente dalla mappatura delle modali per le prenotazioni
    const component = this.modalMapComponent(modalMap, te);

    // Verifico se il componente è stato censito per l'apertura della modale
    if (component) {
      // Creo la configurazione per l'apertura della modale
      const mConfig: IApriModalConfigsForClass = {
        callbacks,
        component,
        options: { windowClass: 'r-mdl-nuova-prenotazione' },
        params: { dataModal },
      };

      // Richiamo la funzione di apertura della modale
      this._riscaModal.apriModal(new ApriModalConfigs(mConfig));
    }
  }

  /**
   * Funzione che gestisce le configurazioni e apre una modale per la conferma annullamento prenotazione.
   * @param callbacks ICallbackDataModal con le callback da eseguire al momento della conferma/annullamento della richiesta.
   */
  openAnnullaPrenotazioneModal(callbacks: ICallbackDataModal) {
    // Verifico l'input
    if (!callbacks) {
      // Nessuna configurazione, blocco il flusso
      return;
    }

    // Definisco il codice del messaggio
    const code = RiscaNotifyCodes.A029;
    // Recupero il messaggio da visualizzare per gestire la conferma annullamento della prenotazione
    const message = this._riscaMessages.getMessage(code);
    // Apro la modale di conferma annullamento
    this._riscaModal.apriModalConferma(message, callbacks);
  }

  /**
   * ################################
   * FUNZIONI DI UTILITY DEL SERVIZIO
   * ################################
   */

  /**
   * Funzione di supporto che, data la configurazione dati per la modale, ritorna il tipo elaborazione per definire il componente della modale da aprire.
   * @param dataModal IBollettiniModalDataConfigs con la configurazione dati della modale.
   * @returns TipoElaborazione che identifica il riferimento dati per l'apertura della modale.
   */
  tipoElaborazioneModale(
    dataModal: IBollettiniModalDataConfigs
  ): TipoElaborazioneVo {
    // Recupero il tipo della modale
    const tipoModale = dataModal.modalita;

    // Verifico il tipo di modalità della modale della prenotazione
    if (this.isModaleConferma(tipoModale)) {
      // Recupero il tipo elaborazione, dall'oggetto elaborazione
      return dataModal.elaborazione?.tipo_elabora;
      // #
    } else if (this.isModaleEmissione(tipoModale)) {
      // Recupero il tipo elaborazione direttamente
      return dataModal.tipoElaborazione;
      // #
    } else {
      // Caso non definito, blocco le logiche
      return;
    }
  }

  /**
   * Funzione che recupera dal map delle modali, il componente della modale per stesso codice tipo elaborazione.
   * @param modalMap DynamicObjAny contenente la mappatura dei dati tra codice tipo elaborazione e referenza del componente della modale.
   * @param tipoElaborazione TipoElaborazione per la ricerca del modale.
   * @return any che definisce la referenza del componente per la modale d'aprire. Se non è valido o non trova niente, ritorna undefined.
   */
  private modalMapComponent(
    modalMap: DynamicObjAny,
    tipoElaborazione: TipoElaborazioneVo
  ): any {
    // Verifico l'input
    if (!modalMap || !tipoElaborazione) {
      // Niente da ritornare
      return undefined;
    }

    // Recupero il codice tipo elaborazione per il recupero della modale
    const CTE = tipoElaborazione.cod_tipo_elabora;
    // Accedo per prorietà e ritorno il valore
    return modalMap[CTE];
  }

  /**
   * Funzione di comodo per verificare se il tipo della modale é: emissione.
   * @param tipo NPModalitaModale che definisce la tipologia di modale.
   * @returns boolean che definisce se la modalità è: emssione.
   */
  isModaleEmissione(tipo: NPModalitaModale): boolean {
    // Verifico se il tipo è emissione
    return tipo === NPModalitaModale.emissione_prenotazione;
  }

  /**
   * Funzione di comodo per verificare se il tipo della modale é: conferma.
   * @param tipo NPModalitaModale che definisce la tipologia di modale.
   * @returns boolean che definisce se la modalità è: conferma.
   */
  isModaleConferma(tipo: NPModalitaModale): boolean {
    // Verifico se il tipo è conferma
    return tipo === NPModalitaModale.conferma_prenotazione;
  }

  /**
   * Funzione che recupera il codice del raggruppamento dato un ambito.
   * @param idAmbito number che definisce il tipo raggruppamento riferito ad un ambito.
   */
  raggruppamentoByAmbito(idAmbito: number): string {
    // Richiamo la funzione della bollettazione
    return this._pagamenti.raggruppamentoByAmbito(idAmbito);
  }

  /**
   * Funzione che aggiunge ai parametri dell'oggetto Elaborazione in input, un ParametroElaborazione che definisce le informazioni per un dirigente pro tempore.
   * @param elabora Elaborazione da modificare.
   * @param dirigenteProTempore string che definisce il valore d'assegnare per il dirigente pro tempore.
   */
  addDPTToElaborazione(elabora: ElaborazioneVo, dirigenteProTempore: string) {
    // Verifico l'input
    if (!elabora) {
      // Ninete dati d'aggiornare
      return;
    }

    // Variabili di comodo
    const k = TipoParametroElaborazione.DIRIGENTE_PRO_TEMPORE;
    const v = dirigenteProTempore;
    // Definisco le informazioni per un nuovo ParametroElaborazione
    const idA = this._user.idAmbito;
    const r = this.raggruppamentoByAmbito(idA);
    const dptPE = this._bmConverter.createParametroElaborazioneVo(k, v, r);

    // Verifico se esitono parametri
    if (!elabora.parametri) {
      // Creo fisicamente un array vuoto
      elabora.parametri = [];
    }

    // Aggiungo l'oggetto del dirigente pro tempore
    elabora.parametri.push(dptPE);
  }

  /**
   * Funzione che ritorna uno stato per la bollettazione, a seconda dell'input richiesto.
   * @param codiceStato StatiElaborazione con il codice dello stato da cercare.
   * @return StatoElaborazioneVo come oggetto trovato.
   */
  getStatoBollettazione(
    codiceStato: CodiciStatiElaborazione
  ): StatoElaborazioneVo {
    if (!codiceStato) {
      // Niente da ritornare
      return;
    }
    // Recupero gli stati elaborazione per la bollettazione
    const statiBollettazione = this._bollettini.statiBollettazione;
    // Estraggo dalla lista l'oggetto per stesso codice
    const statoBollettazione = statiBollettazione?.find(
      (sb: StatoElaborazioneVo) => {
        // Effettuo un confronto tra codici
        return sb.cod_stato_elabora === codiceStato;
      }
    );

    // Ritorno lo stato richiesto
    return statoBollettazione;
  }

  /**
   * Funzione che definisce per l'oggetto in input Elaborazione lo stato: EMISSIONE_RICHIESTA.
   * @param elabora Elaborazione per la quale definire lo stato.
   */
  addEmissioneRichiestaToElaborazione(elabora: ElaborazioneVo) {
    if (!elabora) {
      // Niente d'aggiornare
      return;
    }
    // Recupero il codice per lo stato: EMISSIONE_RICHIESTA
    const codiceStato = CodiciStatiElaborazione.EMISSIONE_RICHIESTA;
    // Recupero lo stato per la bollettazione
    const emissioneRichiesta = this.getStatoBollettazione(codiceStato);
    // Modifico il riferimento dell'oggetto, definito i dati per un nuovo stato elaborazione
    elabora.stato_elabora = emissioneRichiesta;
  }

  /**
   * Funzione che definisce per l'oggetto in input Elaborazione lo stato: CONFERMA_RICHIESTA.
   * @param elabora Elaborazione per la quale definire lo stato.
   */
  addConfermaRichiestaToElaborazione(elabora: ElaborazioneVo) {
    if (!elabora) {
      // Niente d'aggiornare
      return;
    }
    // Recupero il codice per lo stato: CONFERMA_RICHIESTA
    const codiceStato = CodiciStatiElaborazione.CONFERMA_RICHIESTA;
    // Recupero lo stato per la bollettazione
    const emissioneRichiesta = this.getStatoBollettazione(codiceStato);
    // Modifico il riferimento dell'oggetto, definito i dati per un nuovo stato elaborazione
    elabora.stato_elabora = emissioneRichiesta;
  }
}
