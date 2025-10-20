import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { LoggerService } from '../../../../../core/services/logger.service';
import { FormUpdatePropagation } from '../../../../../shared/utilities';
import { DisabledCampiRAPClass } from '../../../class/ricerca-avanzata-pratiche/disabled-campi/disabled-campi.ricerca-avanzata-pratiche.class';
import { DisabledRicercaAvanzataPraticheMRNullClass } from '../../../class/ricerca-avanzata-pratiche/disabled-campi/ricerca-avanzata/disabled-ricerca-avanzata-pratiche.mr.null.class';
import { DisabledRicercaAvanzataPraticheMRPraticaClass } from '../../../class/ricerca-avanzata-pratiche/disabled-campi/ricerca-avanzata/disabled-ricerca-avanzata-pratiche.mr.pratica.class';
import { DisabledRicercaAvanzataPraticheMRStatoDebitorioClass } from '../../../class/ricerca-avanzata-pratiche/disabled-campi/ricerca-avanzata/disabled-ricerca-avanzata-pratiche.mr.stato-debitorio';
import { CodModalitaRicerca } from '../../../components/ricerca-avanzata/form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.enums';

/**
 * Esistono 3 tipi di configurazione per la ricerca avanzata pratiche, che definiscono il campo required.
 * Le configurazioni sono tutte per lo stesso blocco
 * Per il blocco esiste una definizione basata sulla modalità di ricerca pratiche:
 * - Null
 * - Pratica
 * - Stato Debitorio
 * I casi in totale sono 3, attualmente non ci sono configurazioni lato server, per cui il mapping è manuale.
 * In caso di modifiche future, sarà necessario aggiornare il servizio.
 */
@Injectable({
  providedIn: 'root',
})
export class GestioneFormsRicercaPraticheAvanzataService {
  // ##### DATI RICERCA
  /** DisabledDatiSoggettoPFInsClass contenente la mappatura chiave, valore per la ricerca. Modalità Ricerca Null. */
  private dRAPMRN: DisabledRicercaAvanzataPraticheMRNullClass;
  /** DisabledDatiSoggettoPFModClass contenente la mappatura chiave, valore per la ricerca. Modalità Ricerca Pratica. */
  private dRAPMRP: DisabledRicercaAvanzataPraticheMRPraticaClass;
  /** DisabledDatiSoggettoPGInsClass contenente la mappatura chiave, valore per la ricerca. Modalità Ricerca Stato Debitorio. */
  private dRAPMRSD: DisabledRicercaAvanzataPraticheMRStatoDebitorioClass;

  /**
   * Costruttore
   */
  constructor(private _logger: LoggerService) {
    // Lancio il setup delle mappe di require
    this.setupRequired();
  }

  /**
   * ###################################
   * MAPPE DI ACCESSO AI DATI ANAGRAFICI
   * ###################################
   */

  /**
   * Funzione che lancia il setup delle mappe per gli accessi ai campi delle parti dei dati anagrafici.
   */
  private setupRequired() {
    // Setup per la ricerca avanzata pratiche
    this.setupMapDisablerRicercaAvanzataPratiche();
  }

  /**
   * Funzione di setup per la mappa: dati soggetto; per i disabled.
   */
  setupMapDisablerRicercaAvanzataPratiche() {
    // Dati soggetto, PF e inserimento
    this.dRAPMRN = new DisabledRicercaAvanzataPraticheMRNullClass();
    // Dati soggetto, PF e modifica
    this.dRAPMRP = new DisabledRicercaAvanzataPraticheMRPraticaClass();
    // Dati soggetto, PG e inserimento
    this.dRAPMRSD = new DisabledRicercaAvanzataPraticheMRStatoDebitorioClass();
  }

  /**
   * ##########################
   * FUNZIONI RICERCA AVANZATA PRATICHE
   * ##########################
   */

  /**
   * Funzione che gestisce i validatori dei campi in base alla modalità di ricerca.
   * @param f FormGroup da gestire.
   * @param a ModalitaRicerca che definisce il tipo d'azione compiuta sui dati.
   * @param uC FormUpdatePropagation che definisce le configurazioni per la propagazione del disabled.
   */
  setDisabledRicercaAvanzataPratiche(
    f: FormGroup,
    a: CodModalitaRicerca | null | undefined = null,
    uC: FormUpdatePropagation = { onlySelf: true, emitEvent: false }
  ) {
    // Verifico l'input
    if (!f) {
      return;
    }
    // Lancio il set dei disabilitatori
    this.setDisabledByAction(f, a, uC);
  }

  /**
   * ############################
   * FUNZIONI COMUNI DI SETTAGGIO
   * ############################
   */

  /**
   * VALIDO PER TUTTI I CAMPI DELL'OGGETTO DISABLED.
   * Funzione che gestisce il set dei disabled per il form group, dato il tipo di azione e un oggetto composto dalle configurazioni dei campi.
   * L'oggetto di configurazione deve avere delle proprietà con lo stesso nome del tipo d'azione.
   * @param f FormGroup da gestire.
   * @param a ModalitaRicerca azione da gestire.
   * @param s IDisabledConfigs oggetto con i source per le configurazioni.
   * @param uC FormUpdatePropagation che definisce le configurazioni per la propagazione del disabled.
   */
  setDisabledByAction(
    f: FormGroup,
    a: CodModalitaRicerca | null | undefined = null,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f) {
      return;
    }

    // Definisco un contanitore per i disabilitatori
    let disabler: DisabledCampiRAPClass;
    // Verifico qual è la modalita
    switch (a) {
      case CodModalitaRicerca.pratica: {
        disabler = this.dRAPMRP;
        break;
      }
      case CodModalitaRicerca.statoDebitorio: {
        disabler = this.dRAPMRSD;
        break;
      }
      default: {
        disabler = this.dRAPMRN;
        break;
      }
    }

    // Lancio il set dei validatori
    this.setDisabledForFields(f, disabler, uC);
  }

  /**
   * VALIDO PER TUTTI I CAMPI DELL'OGGETTO DISABLED.
   * Funzione che gestisce il disable dei campi per il form group, data una configurazione.
   * @param f FormGroup da gestire.
   * @param disabler DisabledCampiDAClass con la configurazione dei campi per il form.
   * @param uC FormUpdatePropagation che definisce le configurazioni per la propagazione del disabled.
   */
  setDisabledForFields(
    f: FormGroup,
    disabler: DisabledCampiRAPClass,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !disabler) {
      return;
    }

    // Ciclo le proprietà nell'oggetto
    for (let [field] of Object.entries(disabler.config)) {
      // Lancio la funzione di setup dei disabled
      this.setDisableForField(f, field, disabler, uC);
    }
  }

  /**
   * VALIDO PER UN SINGOLO CAMPO.
   * Funzione che gestisce il set dei disabled per il form group, data una configurazione.
   * @param f FormGroup da gestire.
   * @param field string che identifica il FormControl da settare.
   * @param disabler ValidatorCampiDAClass con la configurazione dei campi per il form.
   * @param uC FormUpdatePropagation che definisce le configurazioni per la propagazione del disabled.
   */
  setDisableForField(
    f: FormGroup,
    field: string,
    disabler: DisabledCampiRAPClass,
    uC?: FormUpdatePropagation
  ) {
    // Verifico l'input
    if (!f || !field || !disabler) {
      return;
    }

    // Recupero la configurazione per il disabled
    const disabled = this.getDisabledForField(field, disabler);
    // Verifico se il campo è da disabilitare
    if (disabled) {
      // Disabilito il campo
      f.get(field).disable(uC);
      // #
    } else {
      // Abilito il campo
      f.get(field).enable(uC);
    }
  }

  /**
   * VALIDO PER UN SINGOLO CAMPO.
   * Funzione che ritorna se il campo è da disabilitare.
   * @param field string che identifica il FormControl da settare.
   * @param disabler DisabledCampiDAClass con la configurazione dei campi per il form.
   * @returns boolean per il disable definito per un campo.
   */
  getDisabledForField(field: string, disabler: DisabledCampiRAPClass): boolean {
    // Verifico l'input
    if (!field || !disabler) return false;

    // Per evitare errori bloccanti di programmazione, gestisco le logiche in un try catch
    try {
      // Dalla configurazione dei validatori, recupero quello richiesto
      const fieldData = disabler.config[field];
      // Verifico se esiste la configurazione

      if (fieldData === undefined) return false;

      // Ritorno lo stato disabled
      return fieldData.disabled || false;
      // #
    } catch (e) {
      // Loggo un warning
      this._logger.warning('getDisabledForField failed', { e, disabler });
      // Ritorno false
      return false;
    }
  }
}
