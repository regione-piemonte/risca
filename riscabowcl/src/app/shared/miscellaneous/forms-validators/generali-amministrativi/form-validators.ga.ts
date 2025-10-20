import {
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
} from '@angular/forms';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import * as moment from 'moment';
import { StatoRiscossioneVo } from 'src/app/core/commons/vo/stati-riscossione-vo';
import { TipoAutorizzazioneVo } from 'src/app/core/commons/vo/tipo-autorizzazioni-vo';
import { IstanzeProvvedimentiTable } from 'src/app/shared/classes/risca-table/generali-amministrativi/generali-amministrativi.istanze-provvedimenti.table';
import {
  CodStatiRiscossione,
  CodTipiAutorizzazione,
  FormUpdatePropagation,
  NuovaIstanzaFormData,
  NuovoProvvedimentoFormData,
  RiscaAmbiti,
  AppActions,
} from 'src/app/shared/utilities';
import { RiscaErrorKeys } from 'src/app/shared/utilities/classes/errors-keys';
import { convertNgbDateStructToMoment } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import {
  checkValidationFormControl,
  checkValidationFormControlByFormGroup,
  generaObjErrore,
} from '../forms-validators';
import { validatorStatoRiscossioneAttiva } from './form-validators-stato-pratica-attiva.ga';
import { validatorStatoRiscossioneScaduta } from './form-validators-stato-pratica-scaduta.ga';

/**
 * Form validator personalizzato.
 * Il validatore verificherà che il form control contenga all'interno dell'array che lo valorizza almeno un elemento.
 * @param quantity number che definisce la quantità minima da verificare. Per default è 1.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const arrayContainsAtLeastProvvedimentoValidator = (
  quantity: number = 1
) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formControl: FormControl): ValidationErrors | null => {
    // Verifico che il formControl esista
    if (!formControl) {
      return;
    }
    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Controllo i form control tramite funzione
    const istanzeProvvedimenti: boolean | any[] =
      checkValidationFormControl(formControl);

    // Verifico che siano ritornate i valori
    if (!istanzeProvvedimenti) {
      return null;
    }

    // Preparo l'oggetto di errore
    const key = ERR_KEY.CONTAIN_AT_LEAST;
    // Genero e ritorno l'errore
    const customFormError = generaObjErrore(key);

    // Riassegno la variabile tipizzandola
    const istProv = istanzeProvvedimenti as (
      | NuovaIstanzaFormData
      | NuovoProvvedimentoFormData
    )[];

    // Verifico l'input
    if (istProv.length === 0) {
      // Non ci sono elementi, è un errore
      return customFormError;
    }

    // Recupero dall'array tutti gli oggetti NuovoProvvedimentoFormData
    const prov = istProv.filter(
      (iP: NuovaIstanzaFormData | NuovoProvvedimentoFormData) => {
        // Verifico se è un'istanza o un provvedimento
        return iP.hasOwnProperty('tipologiaProvvedimento');
      }
    );

    // Verifico che il valore esista, sia un array e contenga un elmeneto
    if (!prov || !Array.isArray(prov) || prov.length < quantity) {
      // Genero e ritorno l'errore
      return customFormError;
    }

    // Rimuovo eventuali errori
    return null;
  };
};

/**
 * Form validator personalizzato.
 * Il validatore verificherà che il form control progressivo utenza abbia un valore congruo rispetto alla tipologia pratica scelta.
 * @param idAmbito number con l'id ambito per la gestione del controllo.
 * @param tPFormControlName string nome del form control per pratica.
 * @param pUFormControlName string nome del form control per progressivo utenza.
 * @param modalita AppActions che definisce la modalità di gestione dei dati.
 * @returns ValidationErrors contenente le informazioni sull'errore.
 */
export const progressivoUtenzaPerTipologiaPraticaValidator = (
  idAmbito: number,
  tPFormControlName: string,
  pUFormControlName: string,
  modalita: AppActions
) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formGroup: FormGroup): ValidationErrors | null => {
    // Verifico che l'oggetto formGroup esista
    if (!formGroup) {
      // Passo il controllo come valido
      return null;
    }

    // In modifica, non devono essere applicati i controlli
    if (modalita === AppActions.modifica) {
      // Non sono necessari controlli
      return null;
    }

    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;

    // Controllo i form control tramite funzione
    const tipologiaPratica = checkValidationFormControlByFormGroup(
      formGroup,
      tPFormControlName
    );
    let progressivoUtenza = checkValidationFormControlByFormGroup(
      formGroup,
      pUFormControlName
    );

    // Verifico che siano ritornate i valori
    if (!tipologiaPratica || !progressivoUtenza) {
      return null;
    }

    // Converto il progressivo utenza in number e verifico sia effettivamente un numero
    progressivoUtenza = Number(progressivoUtenza);

    // Controllo con isNaN
    if (isNaN(progressivoUtenza)) {
      return null;
    }

    if (idAmbito == RiscaAmbiti.ambiente) {
      // Creo i casi di gestione - PRATICA
      const praticaOrdinaria =
        tipologiaPratica.cod_tipo_riscossione === 'ORDINARIA';
      const praticaPreferenziale =
        tipologiaPratica.cod_tipo_riscossione === 'PREFERENZIALE';
      const praticaAttingimentoOPlus =
        tipologiaPratica.cod_tipo_riscossione === 'ATTINGIMENTO' ||
        tipologiaPratica.cod_tipo_riscossione === 'ATTINGIMENTO_PLURI';
      // Creo i casi di gestione - PROGRESSIVO UTENZA
      const progUtenzaThreshold1 =
        progressivoUtenza >= 1 && progressivoUtenza <= 9999;
      const progUtenzaThreshold2 =
        progressivoUtenza >= 10000 && progressivoUtenza <= 49999;
      const progUtenzaThreshold3 = progressivoUtenza >= 50000;

      // Verifico le casistiche di errore
      // #1) PRATICA ORDINARIA
      if (praticaOrdinaria && !progUtenzaThreshold1) {
        // Recupero la chiave d'errore
        const key = ERR_KEY.PRATICA_ORDINARIA_PROGRESSIVO;
        // Genero e ritorno l'errore
        return generaObjErrore(key);
      }
      // #2) PRATICA PREFERENZIALE
      if (praticaPreferenziale && !progUtenzaThreshold2) {
        // Recupero la chiave d'errore
        const key = ERR_KEY.PRATICA_PREFERENZIALE_PROGRESSIVO;
        // Genero e ritorno l'errore
        return generaObjErrore(key);
      }
      // #3) PRATICA ATTINGIMENTO O ATTINGIMENTO_PLURI
      if (praticaAttingimentoOPlus && !progUtenzaThreshold3) {
        // Recupero la chiave d'errore
        const key = ERR_KEY.PRATICA_ATTINGIMENTO_O_PLUS_PROGRESSIVO;
        // Genero e ritorno l'errore
        return generaObjErrore(key);
      }
    }

    // Non ci sono errori
    return null;
  };
};

/**
 * Form validator personalizzato.
 * Questo form validator controlla che la data provvedimento risulti minore della data di oggi.
 * @param idAmbito number che definisce l'ambito di gestione dell'utente.
 * @param dataProvvedimentoFormControlName string che definisce il form control name del form, per il recupero delle informazioni.
 * @returns ValidationErrors | null che definisce, in ordine, se è stato generato un errore dal controllo, o se le informazioni sono corrette.
 */
export const dataProvvedimentoValidator = (
  idAmbito: number,
  dataProvvedimentoFormControlName: string
) => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formGroup: FormGroup): ValidationErrors | null => {
    // Verifico che l'oggetto formGroup esista
    if (!formGroup) {
      // Niente formgroup, niente informazioni
      return;
    }

    // Definisco la costante contenente le chiavi d'errore per i forms validators
    const ERR_KEY = RiscaErrorKeys;
    // Variabili di comodo
    const f = formGroup;
    const dpFCN = dataProvvedimentoFormControlName;

    // Controllo e recupero le informazioni dal form control name, se esitono
    let dataProvvedimento: NgbDateStruct;
    dataProvvedimento = checkValidationFormControlByFormGroup(f, dpFCN);

    // Verifico che siano ritornate i valori
    if (!dataProvvedimento) {
      // Niente da controllare
      return null;
    }

    // Verifico l'ambito
    if (idAmbito == RiscaAmbiti.ambiente) {
      // Converto la data provvedimento in moment
      const dpM = convertNgbDateStructToMoment(dataProvvedimento);
      // Definisco la data di oggi
      const oggi = moment();

      // Verifico le date
      if (dpM.isSameOrAfter(oggi)) {
        // Recupero la chiave d'errore
        const key = ERR_KEY.PROCEDIMENTO_SCADENZA_SOSPENSIONE;
        // Genero e ritorno l'errore
        return generaObjErrore(key);
      }
    }

    // Non ci sono errori
    return null;
  };
};

/**
 * Funzione che gestisce la validazione per il campo: "Data scadenza concessione".
 * @param idAmbito number che definisce l'ambito attualmente attivo per l'utente.
 * @param dataFineConcessioneFormControlName string che definisce il nome del campo: DATA_FINE_CONCESSIONE.
 * @param procedimentoFormControlName string che definisce il nome del campo: PROCEDIMENTO.
 * @param statoPraticaFormControlName string che definisce il nome del campo: STATO_PRATICA.
 * @returns ValidationErrors | null con la gestione dell'errore.
 */
export const dataScadenzaConcessioneValidator = (
  idAmbito: number,
  dataFineConcessioneFormControlName: string,
  procedimentoFormControlName: string,
  statoPraticaFormControlName: string
): ValidationErrors | null => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formGroup: FormGroup): ValidationErrors | null => {
    // Verifico che l'oggetto formGroup esista
    if (!formGroup) {
      // Niente configurazione
      return;
    }

    // Verifico e gestisco l'ambito, indirizzando verso il controllo specifico
    switch (idAmbito) {
      case RiscaAmbiti.ambiente:
        // Definisco l'oggetto di configurazione da passare alla funzione
        const configs: IDtScadenzaConcessioneAA = {
          formGroup,
          dataFineConcessioneFormControlName,
          procedimentoFormControlName,
          statoPraticaFormControlName,
        };
        // Richiamo il controllo per AMBIENTE
        return dataScadenzaConcessioneAmbienteValidator(configs);
      // #
      default:
        // Ritorno null come "nessun errore"
        return null;
    }
  };
};

/**
 * Interfaccia di comodo che definisce le informazioni da passare per la gestione del campo "data scadenza concessione" per ambito: AMBIENTE.
 */
interface IDtScadenzaConcessioneAA {
  formGroup: FormGroup;
  dataFineConcessioneFormControlName: string;
  procedimentoFormControlName: string;
  statoPraticaFormControlName: string;
}

/**
 * Funzione che gestisce la validazione per il campo: "Data scadenza concessione". L'ambito gestito è: AMBIENTE.
 * RISCA-638 => La “Data Scadenza Concessione” è obbligatoria quando "Procedimento" ≠ “Autorizzazione provvisoria” AND lo stato pratica è ATTIVA.
 * @param configs IDtScadenzaConcessioneAA con le configurazioni per la gestione del campo.
 * @returns ValidationErrors | null con la gestione dell'errore.
 */
export const dataScadenzaConcessioneAmbienteValidator = (
  configs: IDtScadenzaConcessioneAA
): ValidationErrors | null => {
  // Estraggo dall'input di configurazione le informazioni
  const {
    formGroup,
    dataFineConcessioneFormControlName,
    procedimentoFormControlName,
    statoPraticaFormControlName,
  } = configs || {};
  // Verifico che l'oggetto formGroup esista
  if (!formGroup) {
    // Niente configurazione
    return;
  }

  // Definisco la costante contenente le chiavi d'errore per i forms validators.
  const ERR_KEY = RiscaErrorKeys;
  // Variabili di comodo
  const fg = formGroup;
  const dtFC_FCN = dataFineConcessioneFormControlName;
  const proc_FCN = procedimentoFormControlName;
  const stP_FCN = statoPraticaFormControlName;
  // Definisco le variabili per la modifica dei validatori sul campo del form
  const v: ValidatorFn[] = [];
  const uC: FormUpdatePropagation = { onlySelf: true, emitEvent: false };

  // Controllo e recupero i valori per i campi del form
  let dtScadConc: NgbDateStruct;
  dtScadConc = formGroup.get(dtFC_FCN)?.value;
  let proc: TipoAutorizzazioneVo;
  proc = checkValidationFormControlByFormGroup(fg, proc_FCN);
  let statoR: StatoRiscossioneVo;
  statoR = checkValidationFormControlByFormGroup(fg, stP_FCN);

  // ### 1) Recupero le infomazioni per la gestione della data scadenza concessione ###
  const isDtSCDefined = dtScadConc != undefined;
  // ### 2) Recupero le infomazioni per la gestione del procedimento ###
  const codProc = proc?.cod_tipo_autorizza;
  const AUT_PROV = CodTipiAutorizzazione.AUTORIZZAZIONE_PROVV;
  // Definisco le condizioni per la gestione specifica del procedimento
  const isProcAutProv = codProc == AUT_PROV;
  // ### 3) Recupero le informazioni per la gestione dello stato pratica ###
  const codPratica = statoR?.cod_stato_riscossione;
  const STATO_ATTIVA = CodStatiRiscossione.ATTIVA;
  // Definisco le condizioni per la gestione specifica dello stato pratica
  const isPraticaAttiva = codPratica == STATO_ATTIVA;

  // Definisco lo scenario d'errore
  const errorScenario = !isDtSCDefined && !isProcAutProv && isPraticaAttiva;

  // Verifico il caso di errore
  if (errorScenario) {
    // Data non definita, ma il procedimento non è autorizzazione provvisoria e la pratica è attiva
    const key = ERR_KEY.DATA_SCADENZA_CONCESSIONE_REQUIRED;
    // Genero e ritorno l'errore
    return generaObjErrore(key);
    // #
  } else {
    // Non si verifica il caso, ritorno null
    return null;
  }
};

/**
 * Form validator personalizzato.
 * Questo validatore verifica la correttezza delel stato della riscossione.
 * Dal documento di analisi (19/07/2022 - WP2-2.2-USR-V07-US005_GestioneRiscossione-Dati Generali e Amministrativi.docx).
 * @param idAmbito number con l'id ambito con cui sta lavorando l'utente.
 * @param statoPraticaFormControlName string con il form control name per lo stato pratica.
 * @param dataInizioSospensioneFormControlName string con il form control name per la data inizio sospensione.
 * @param dataFineSospensioneFormControlName string con il form control name per la data fine sospensione.
 * @param dataFineConcessioneFormControlName string con il form control name per la data fine concessione.
 * @param istanzeProvvedimentiTable IstanzeProvvedimentiTable con le informzioni relative alla tabella delle istanze e provvedimenti.
 * @returns ValidationErrors | null che definisce se è stato generato un errore oppure se la validazione è andata a buon fine (null).
 */
export const statoRiscossionePraticaValidator = (
  idAmbito: number,
  statoPraticaFormControlName: string,
  dataInizioSospensioneFormControlName: string,
  dataFineSospensioneFormControlName: string,
  dataFineConcessioneFormControlName: string,
  istanzeProvvedimentiTable: IstanzeProvvedimentiTable
): ValidationErrors | null => {
  // Come struttura di un validatore personalizzato, viene ritornata una funzione che gestisce le logiche di controllo
  return (formGroup: FormGroup): ValidationErrors | null => {
    // Verifico che l'oggetto formGroup esista
    if (!formGroup) {
      return;
    }
    // Variabili di comodo
    const f = formGroup;
    const spFCN = statoPraticaFormControlName;
    const disFCN = dataInizioSospensioneFormControlName;
    const dfsFCN = dataFineSospensioneFormControlName;
    const dfcFCN = dataFineConcessioneFormControlName;
    const isTable = istanzeProvvedimentiTable;

    // Recupero lo stato d'abilitazione del campo per lo stato della pratica
    let spDisabled: boolean;
    spDisabled = f.get(spFCN).disabled;
    // Recupero il dato relativo allo stato della pratica
    let statoPratica: StatoRiscossioneVo;
    statoPratica = checkValidationFormControlByFormGroup(f, spFCN);
    // Definisco le condizioni di verifica per il valore
    const stValue = !statoPratica || statoPratica.cod_stato_riscossione == null;

    // Verifico lo stato della pratica, per abilitazione e valore
    if (spDisabled || stValue) {
      // Non è valorizzato, nessun errore
      return null;
    }

    // Definisco le condizioni per l'ambito
    const isAmbiente = idAmbito === RiscaAmbiti.ambiente;
    // Recupero lo stato della pratica come codice
    const statoRiscossione = statoPratica.cod_stato_riscossione.trim();

    // Verifico la tipologia dell'ambito
    if (isAmbiente) {
      // Inizio i controlli in base allo stato
      switch (statoRiscossione) {
        // RISCA-592 => Richiesta rimozione delle logiche di controllo per lo stato: SOSPESA
        // case CodStatoRiscossioneVoEnum.SOSPESA:
        //   // Verifico le condizioni per lo stato sospesa
        //   return validatorStatoRiscossioneSospesa(f, disFCN, dfsFCN);
        // #
        case CodStatiRiscossione.SCADUTA:
          // Verifico le condizioni per lo stato scaduta
          return validatorStatoRiscossioneScaduta(f, dfcFCN, isTable);
        // #
        // RISCA-632 => RIchiesta rimozione delle logiche di controllo per lo stato: RINUNCIATA
        // case CodStatoRiscossioneVoEnum.RINUNCIATA:
        //   // Verifico le condizioni per lo stato rinuncia
        //   return validatorStatoRiscossioneRinunciata(isTable);
        // // #
        case CodStatiRiscossione.ATTIVA:
          // Variabile di comodo
          const validatorAttiva = validatorStatoRiscossioneAttiva;
          // Verifico le condizioni per lo stato attiva
          return validatorAttiva(f, disFCN, dfsFCN, dfcFCN, isTable);
        // #
        default:
          // In questi casi va tutto bene, non ho errori.
          return null;
        // #
      }
    }
  };
};
