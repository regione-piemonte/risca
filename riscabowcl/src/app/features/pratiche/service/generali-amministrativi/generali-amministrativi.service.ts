import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { IstanzaProvvedimentoVo } from 'src/app/core/commons/vo/istanza-provvedimento-vo';
import {
  isIstanzaVo,
  isProvvedimentoVo,
} from 'src/app/shared/services/risca/risca-utilities/risca-utilities.functions';
import {
  AppActions,
  NuovaIstanzaFormData,
  NuovoProvvedimentoFormData,
  TNIPFormData,
} from 'src/app/shared/utilities';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { GeneraliAmministrativiConsts } from '../../consts/generali-amministrativi/generali-amministrativi.consts';

@Injectable({
  providedIn: 'root',
})
export class GeneraliAmministrativiService {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts()
  /** Oggetto contenente le costanti per il componente. */
  GA_C = GeneraliAmministrativiConsts;

  /**
   * Costruttore
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * Funzione sul blur del campo progressivoUtenza del form datiGenAmmForm.
   */
  completaProgressivoUtenza(datiGenAmmForm: FormGroup) {
    // Recupero il valore del progressivo utenza
    let progressivoUtenza = this._riscaUtilities.getFormValue(
      datiGenAmmForm,
      this.GA_C.PROGRESSIVO_UTENZA
    );

    // Lancio la funzione di manipolazione della stringa
    progressivoUtenza = this._riscaUtilities.gestisciZeriInizioNumero(
      progressivoUtenza,
      5
    );

    // Aggiorno il dato nel form
    this._riscaUtilities.setFormValue(
      datiGenAmmForm,
      this.GA_C.PROGRESSIVO_UTENZA,
      progressivoUtenza,
      { emitEvent: false }
    );
  }

  /**
   * Disabilita o abilita i campi in base alla modalità della componente
   * @param mainForm la form di generali amministrativi che devo modificare
   * @param modalita la modalità della componente
   */
  setDisabledPerModalita(mainForm: FormGroup, modalita: AppActions) {
    if (mainForm == null || modalita == null) {
      return;
    }

    // Configurazione di comodo
    const config = { onlySelf: true, emitEvent: false };
    // Recupero le chiavi per il blocco/sblocco dei campi
    const tipoPratica = this.GA_C.TIPOLOGIA_PRATICA;
    const codUtenza = this.GA_C.CODICE_UTENZA;
    const progUtenza = this.GA_C.PROGRESSIVO_UTENZA;
    const statoPratica = this.GA_C.STATO_PRATICA;
    const statoDebitorio = this.GA_C.STATO_DEBITORIO_INVIO_SPECIALE;
    // Funzione di comodo per abilitare un campo
    const abilita = (f: string) => {
      mainForm.get(f).enable(config);
    };
    // Funzione di comodo per disabilitare un campo
    const disabilita = (f: string) => {
      mainForm.get(f).disable(config);
    };

    // Verifico la modalità
    if (modalita == AppActions.inserimento) {
      // Abilito i campi
      abilita(tipoPratica);

      // Recupero il valore per il tipo pratica
      const tipoPraticaVal = mainForm.get(tipoPratica).value;
      // Verifico se il valore è impostato
      if (tipoPraticaVal != null) {
        // Abilito i campi
        abilita(codUtenza);
        abilita(progUtenza);
        abilita(statoPratica);
        // #
      } else {
        // I campi per logiche devono essere disabilitati
        disabilita(codUtenza);
        disabilita(progUtenza);
        disabilita(statoPratica);
      }
      // #
    } else if (modalita == AppActions.modifica) {
      // Disabilito i campi
      disabilita(tipoPratica);
      disabilita(codUtenza);
      disabilita(progUtenza);
      disabilita(statoDebitorio);
    }
  }

  /**
   * Converte una lista di IstanzaProvvedimentoVo in una lista di NuovaIstanzaFormData o NuovoProvvedimentoFormData
   * @param istanzeProvvedimenti IstanzaProvvedimentoVo[] da convertire.
   * @returns TNIPFormData[] come risultato della conversione.
   */
  convertIstanzeProvvedimentiVoToTNIPFormDatas(
    istanzeProvvedimenti: IstanzaProvvedimentoVo[]
  ): TNIPFormData[] {
    // Verifico l'input
    if (!istanzeProvvedimenti) {
      // Ritorno array vuoto, i dati sono parziali o assenti
      return [];
    }

    // Distunguo i due tipi di dati tramite il check sulla proprietà tipo titolo
    const istanze = istanzeProvvedimenti.filter(
      (ip: IstanzaProvvedimentoVo) => {
        // Verifico se l'oggetto è un'istanza
        return isIstanzaVo(ip);
        // #
      }
    );
    const provvedimenti = istanzeProvvedimenti.filter(
      (ip: IstanzaProvvedimentoVo) => {
        // Verifico se l'oggetto è un provvedimento
        return isProvvedimentoVo(ip);
        // #
      }
    );

    // Ciclo le istanze e costruisco gli oggetti
    const iNIP: TNIPFormData[] = istanze.map((i: IstanzaProvvedimentoVo) => {
      // Lancio la conversione
      return this.createNuovaIstanzaFormData(i);
    });
    // Ciclo i provvedimenti e costruisco gli oggetti
    const pNIP: TNIPFormData[] = provvedimenti.map(
      (p: IstanzaProvvedimentoVo) => {
        // Lancio la conversione
        return this.createNuovoProvvedimentoFormData(p);
      }
    );

    // Unisco gli array di dati
    const listaTNIPFD: TNIPFormData[] = iNIP.concat(pNIP);
    // Ritorno la lista
    return listaTNIPFD;
  }

  /**
   * Funzione di conversione da un oggetto IstanzaProvvedimentoVo ad un oggetto NuovaIstanzaFormData.
   * @param istanza IstanzaProvvedimentoVo da convertire.
   * @returns NuovaIstanzaFormData convertita.
   */
  createNuovaIstanzaFormData(
    istanza: IstanzaProvvedimentoVo
  ): NuovaIstanzaFormData {
    // Creo il contenitore per un'istanza
    let newI: NuovaIstanzaFormData = {
      original: undefined,
      tipologiaIstanza: undefined,
      dataIstanza: undefined,
    };

    // Effettuo il parse della data
    const dataIstanza = this._riscaUtilities.convertServerDateToNgbDateStruct(
      istanza?.data_provvedimento
    );

    // Aggiungo le informazioni
    newI.original = istanza;
    newI.dataIstanza = dataIstanza;
    newI.noteIstanza = istanza?.note;
    newI.numeroIstanza = istanza?.num_titolo;
    newI.tipologiaIstanza = istanza?.id_tipo_provvedimento;

    // Aggiungo l'istanza all'array dati
    return newI;
  }

  /**
   * Funzione di conversione da un oggetto IstanzaProvvedimentoVo ad un oggetto NuovoProvvedimentoFormData.
   * @param provvedimento IstanzaProvvedimentoVo da convertire.
   * @returns NuovoProvvedimentoFormData convertita.
   */
  createNuovoProvvedimentoFormData(
    provvedimento: IstanzaProvvedimentoVo
  ): NuovoProvvedimentoFormData {
    // Creo il contenitore per un'istanza
    let newP: NuovoProvvedimentoFormData = {
      original: undefined,
      dataProvvedimento: undefined,
      numeroProvvedimento: undefined,
      tipoTitolo: undefined,
      tipologiaProvvedimento: undefined,
    };

    // Effettuo il parse della data
    const dataProvvedimento =
      this._riscaUtilities.convertServerDateToNgbDateStruct(
        provvedimento?.data_provvedimento
      );

    // Aggiungo le informazioni
    newP.original = provvedimento;
    newP.dataProvvedimento = dataProvvedimento;
    newP.noteProvvedimento = provvedimento?.note;
    newP.numeroProvvedimento = provvedimento?.num_titolo;
    newP.tipoTitolo = provvedimento?.id_tipo_titolo;
    newP.tipologiaProvvedimento = provvedimento?.id_tipo_provvedimento;

    // Aggiungo l'istanza all'array dati
    return newP;
  }
}
