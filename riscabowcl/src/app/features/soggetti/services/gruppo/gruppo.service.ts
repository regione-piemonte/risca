import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ComponenteGruppo } from '../../../../core/commons/vo/componente-gruppo-vo';
import { Gruppo } from '../../../../core/commons/vo/gruppo-vo';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { VerificaPraticheStDebitoriVo } from '../../../../core/commons/vo/verifica-pratiche-stati-deboitori-vo';
import { RiscaTableClass } from '../../../../shared/classes/risca-table/risca-table';
import { RiscaTableDataConfig } from '../../../../shared/components/risca/risca-table/utilities/risca-table.classes';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaMessagesService } from '../../../../shared/services/risca/risca-messages.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  IResultVerifyRStDDettaglio,
  RiscaFormBuilderSize,
  RiscaGruppo,
  RiscaSortTypes,
  VerifyIndTipiOggetti,
  VerifyIndTipiOperazioni,
} from '../../../../shared/utilities';
import { RiscaNotifyCodes } from '../../../../shared/utilities/enums/risca-notify-codes.enums';
import { SoggettoDatiAnagraficiService } from '../../../pratiche/service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { PraticheService } from '../../../pratiche/service/pratiche.service';
import { GruppoConsts } from '../../consts/gruppo/gruppo.consts';

@Injectable({
  providedIn: 'root',
})
export class GruppoService {
  /** Oggetto RiscaGruppoConsts contenente le costanti utilizzate dal componente. */
  G_C = GruppoConsts;
  /** Oggetto CommonConsts contenente le costanti di uso comune. */
  C_C = new CommonConsts()

  /**
   * Costruttore
   */
  constructor(
    private _pratiche: PraticheService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    private _riscaMessages: RiscaMessagesService,
    private _riscaUtilities: RiscaUtilitiesService,
    private _soggettoDA: SoggettoDatiAnagraficiService
  ) {}

  /**
   * #######################
   * FUNZIONI DI CONVERSIONI
   * #######################
   */

  /**
   * Funzione che converte, e mergia se definite, le informazioni di un oggetto RiscaGruppo in un oggetto Gruppo.
   * @param rg RiscaGruppo da convertire.
   * @param g Gruppo dalla quale recuperare le informazioni da mergiare.
   * @returns Gruppo convertito.
   */
  convertRiscaGruppoToGruppo(rg: RiscaGruppo, g?: Gruppo): Gruppo {
    // Verifico l'input
    if (!rg) {
      // Nessun dato
      return undefined;
    }

    // Creo un oggetto Gruppo e definisce le informazioni
    let gruppo: Gruppo = {
      des_gruppo_soggetto: rg.nomeGruppo,
      componenti_gruppo: rg.componentiGruppo,
    } as any;
    // Verifico se è stato definito un gruppo in ingresso
    if (g) {
      // Unisco le informazioni tra i due gruppi
      gruppo = { ...g, ...gruppo };
    }

    // Ritorno il gruppo convertito
    return gruppo;
  }

  /**
   * Funzione che converte un array di SoggettoVo in un array di ComponenteGruppo.
   * @param si Array di SoggettoVo da convertire.
   * @param g Gruppo contenente i dati del gruppo che ha configurato il componente.
   * @param capogruppo SoggettoVo definito come componente gruppo capogruppo.
   * @returns Array di ComponenteGruppo convertito.
   */
  convertSoggettiVoToComponentiGruppo(
    si: SoggettoVo[],
    g?: Gruppo,
    capogruppo?: SoggettoVo
  ): ComponenteGruppo[] {
    // Verifico l'input
    if (!si) {
      return [];
    }

    // Rimappo i dati dei componenti definendo un eventuale capogruppo
    return si.map((s: SoggettoVo) => {
      // VVerifico se il soggetto è il capogruppo
      const soggettoCG = s.id_soggetto === capogruppo?.id_soggetto;
      // Richiamo la funzione di conversione
      return this.convertSoggettoVoToComponenteGruppo(s, g, soggettoCG);
      // #
    });
  }

  /**
   * Funzione che converte un SoggettoVo in ComponenteGruppo.
   * @param s SoggettoVo da convertire.
   * @param g Gruppo contenente i dati del gruppo che ha configurato il componente.
   * @param capogruppo boolean che definisce se questo è il capogruppo.
   * @returns ComponenteGruppo convertito.
   */
  convertSoggettoVoToComponenteGruppo(
    s: SoggettoVo,
    g?: Gruppo,
    capogruppo: boolean = false
  ): ComponenteGruppo {
    // Verifico l'input
    if (!s) {
      return undefined;
    }

    // Definisco un oggetto ComponenteGruppo
    const cg: ComponenteGruppo = {
      id_soggetto: s.id_soggetto,
      id_gruppo_soggetto: g?.id_gruppo_soggetto || undefined,
      flg_capo_gruppo: capogruppo,
    };

    // Ritorno il nuovo oggetto
    return cg;
  }

  /**
   * #############################
   * CONTROLLI PRATICHE PER GRUPPO
   * #############################
   */

  /**
   * Funzione che effettua tutti i controlli sulle pratiche di un gruppo, con modalità: modifica.
   * Dalla documentazione i controlli saranno:
   * - Controllo presenza PRATICHE associate al gruppo o associate ad uno o più STATI DEBITORI;
   * @param g Gruppo da verificare per l'operazione di: modifica.
   * @param idTipoOper VerifyIndTipiOperazioni che definisce la configurazione per il tipo di verifica da effettuare.
   * @param capogruppoUpdated boolean che definsice se il gruppo ha il proprio capogruppo differente rispetto a quello salvato su DB.
   * @param excludeIdRiscossione number che definisce l'id di una riscossione da escludere dai controlli di verify.
   * @returns Observable<IResultVerifyRStDDettaglio> con i risultati dei controlli.
   */
  controllaPStDGruppoModifica(
    g: Gruppo,
    idTipoOper: VerifyIndTipiOperazioni,
    capogruppoUpdated: boolean,
    excludeIdRiscossione?: number
  ): Observable<IResultVerifyRStDDettaglio> {
    // Variabili di comodo
    const indTOgg = capogruppoUpdated
      ? VerifyIndTipiOggetti.gruppoReferenteUpd
      : VerifyIndTipiOggetti.gruppoReferenteNotUpd;
    const idTOper = idTipoOper;
    const exIdR = excludeIdRiscossione;
    // Recupero l'id del gruppo
    const idG = g?.id_gruppo_soggetto;

    // Lancio la chiamata per il controllo delle informazioni
    return this._pratiche.verifyPStDGruppo(idG, indTOgg, idTOper, exIdR).pipe(
      map((verifica: VerificaPraticheStDebitoriVo) => {
        // Verifico e formatto l'informazione di ritorno
        return this.dettaglioFEVerifyPStD(verifica);
      })
    );
  }

  /**
   * Funzione che effettua tutti i controlli sulle pratiche di un gruppo, con modalità: elimina.
   * Dalla documentazione i controlli saranno:
   * - Controllo presenza PRATICHE associate al gruppo o associate ad uno o più STATI DEBITORI;
   * @param g Gruppo da verificare per l'operazione di: modifica.
   * @param idTipoOper VerifyIndTipiOperazioni che definisce la configurazione per il tipo di verifica da effettuare.
   * @param excludeIdRiscossione number che definisce l'id di una riscossione da escludere dai controlli di verify.
   * @returns Observable<IResultVerifyRStDDettaglio> con i risultati dei controlli.
   */
  controllaPStDGruppoElimina(
    g: Gruppo,
    idTipoOper: VerifyIndTipiOperazioni,
    excludeIdRiscossione?: number
  ): Observable<IResultVerifyRStDDettaglio> {
    // Variabili di comodo
    const indTOgg = VerifyIndTipiOggetti.gruppoReferenteNotUpd;
    const idTOper = idTipoOper;
    const exIdR = excludeIdRiscossione;
    // Recupero l'id del gruppo
    const idG = g?.id_gruppo_soggetto;

    // Lancio la chiamata per il controllo delle informazioni
    return this._pratiche.verifyPStDGruppo(idG, indTOgg, idTOper, exIdR).pipe(
      map((verifica: VerificaPraticheStDebitoriVo) => {
        // Verifico e formatto l'informazione di ritorno
        return this.dettaglioFEVerifyPStD(verifica);
      })
    );
  }

  /**
   * ################################
   * FUNZIONI DI UTILITY DEL SERVIZIO
   * ################################
   */

  /**
   * Funzione di setup per le input del form.
   * @returns Oggetto contenente le configurazioni dei campi del form.
   */
  setupFormInputs() {
    // Configurazioni delle form inputs
    const nomeGruppoConfig = this._riscaFormBuilder.genInputText({
      label: this.G_C.LABEL_NOME_GRUPPO,
      showErrorFC: true,
      showErrorFG: true,
      size: RiscaFormBuilderSize.x2,
      maxLength: 100,
    });

    return { nomeGruppoConfig };
  }

  /**
   * Funzione che recupera il capogruppo da un Gruppo.
   * @param g Gruppo contenente i dati del gruppo.
   * @returns ComponenteGruppo che identifica il capogruppo.
   */
  getCapogruppo(g: Gruppo): ComponenteGruppo {
    // Verifico l'input
    if (!g) {
      return;
    }

    // Ricerco il componente gruppo con flg_capogruppo
    return g.componenti_gruppo?.find((cg) => cg.flg_capo_gruppo);
  }

  /**
   * Funzione che cicla i componenti del gruppo e scarica le informazioni dei soggetti.
   * @param componentiGruppo Array di ComponenteGruppo per l'ottenimento dei dati.
   * @returns Observable<SoggettoVo[]> contenente i dettagli dei componenti scaricati.
   */
  getDettaglioComponentiGruppo(
    componentiGruppo: ComponenteGruppo[],
    onlyCapogruppo: boolean = false
  ): Observable<SoggettoVo[]> {
    // Verifico l'input
    if (!componentiGruppo || componentiGruppo.length === 0) {
      // Nessun dato da scaricare, ritorno un of con array vuoto
      return of([]);
    }

    // Richiamo la funzione del servizio
    return this._soggettoDA.getDettaglioComponenti(
      componentiGruppo,
      onlyCapogruppo
    );
  }

  /**
   * Funzione che ordina i dati di una tabella per flag capogruppo.
   * L'ordinamento dati avverrà tramite riferimento.
   * @param tableGruppo RiscaTableClass<T> contenente i dati per i gruppi.
   */
  sortTableByCapogruppo<T>(tableGruppo: RiscaTableClass<T>) {
    // Definisco le logiche di sort
    const sort = (
      rowA: RiscaTableDataConfig<any>,
      rowB: RiscaTableDataConfig<any>
    ) => {
      // Verifico se la riga A o B è selezionata
      const aSel = rowA.selected;
      const bSel = rowB.selected;
      // Verifico quale riga è selezionata
      if (aSel) {
        // Ritorno 1 come a > b
        return 1;
      } else if (bSel) {
        // Ritorno -1 come a < b
        return -1;
      } else {
        // Entrambe le righe non sono selezionate, nessun ordinamento
        return 0;
      }
    };

    // Ordino la tabella per capogruppo
    tableGruppo.sortElements(sort, RiscaSortTypes.decrescente);
  }

  /**
   * Funzione di comodo che gestisce tutte le logiche di comunicazione all'utente per quanto riguarda le pratiche/stati debitori collegate al gruppo.
   * La gestione avverrà per l'operazione di: elimina.
   * @param verifica IResultVerifyRStDDettaglio con l'oggetto di controllo generato dalla funzione. Contiene il risultato dei controlli.
   * @returns string contenente il messaggio da visualizzare all'utente.
   */
  messaggioControlRStDGElimina(verifica: IResultVerifyRStDDettaglio): string {
    // Recupero le informazioni per la pratica/st debitori
    const { dettaglio } = verifica || {};

    // Definisco le informazioni da andare a sostituire nel messaggio
    const info = this._riscaUtilities.verifyPStDInfo(dettaglio);

    // Definisco le configurazioni per la generazione dell'errore
    const phs = [info.quantita, info.label];
    const code = RiscaNotifyCodes.E040;
    // Recupero il messaggio con placeholder
    return this._riscaMessages.getMessageWithPlacholderByCode(code, phs);
  }

  /**
   * Funzione di comodo che converte il risultato di controllo su un gruppo in un oggetto di comodo per il chiamante.
   * @param verificaPStDVo VerificaPraticheStDebitoriVo con il risultato del controllo.
   * @returns IResultVerifyRStDDettaglio con le informazioni di comodo.
   */
  private dettaglioFEVerifyPStD(
    verificaPStDVo: VerificaPraticheStDebitoriVo
  ): IResultVerifyRStDDettaglio {
    // Richiamo il servizio di utility
    return this._riscaUtilities.dettaglioFEVerifyPStD(verificaPStDVo);
  }
}
