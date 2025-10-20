import { Injectable } from '@angular/core';
import { FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { IndirizzoSpedizioneVo } from '../../../../core/commons/vo/indirizzo-spedizione-vo';
import { RecapitoVo } from '../../../../core/commons/vo/recapito-vo';
import { IndirizzoSpedizioneFormConsts } from '../../../components/risca/risca-indirizzo-spedizione-form/utilities/risca-indirizzo-spedizione-form.consts';
import {
  IIndirizzoSpedizioneForm,
  IIndirizzoSpedizioneValidators,
  IISErrorMaps,
} from '../../../components/risca/risca-indirizzo-spedizione-form/utilities/risca-indirizzo-spedizione-form.interfaces';
import { CodIstatNazioni, DenominazioneNazioni } from '../../../utilities';
import { RiscaErrorsMap } from '../../../utilities/classes/errors-maps';
import { RiscaUtilitiesService } from '../risca-utilities/risca-utilities.service';

/**
 * Servizio di utility con funzionalità di comodo per la gestione degli script per il componente risca-indirizzo-spedizione-form.
 */
@Injectable()
export class RiscaIndirizzoSpedizioneFormService {
  /** Oggetto contenente le costanti per il componente attuale. */
  private ISF_C = new IndirizzoSpedizioneFormConsts();
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  private EM = new RiscaErrorsMap();

  /**
   * Costruttore
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * ##################################
   * FUNZIONI DI UTILITY DEL COMPONENTE
   * ##################################
   */

  /**
   * Funzione di comodo che va a settare, per una specifica chiave, un array di validatori per un campo del form.
   * @param f FormGroup da gestire.
   * @param fcn string che definisce il nome del FormControl all'interno del FormGroup.
   * @param validators Array di ValidatorFn da definire per il FormControl.
   */
  private setFieldValidator(
    f: FormGroup,
    fcn: string,
    validators: ValidatorFn[]
  ) {
    // Imposto il validatore
    this._riscaUtilities.setFieldValidator(f, fcn, validators);
  }

  /**
   * Funzione di comodo per il disable di un campo di una form.
   * @param form FormGroup per la quale settare il valore.
   * @param key string che definisce il nome del FormControl per il set.
   * @param options Object compatibile con la funzione "setValue", per definire delle opzioni sull'azione.
   */
  private disableFormField(form: FormGroup, key: string, options?: Object) {
    // Richiamo il servizio di utility
    this._riscaUtilities.disableFormField(form, key, options);
  }

  /**
   * Funzione di comodo per l'enable di un campo di una form.
   * @param form FormGroup per la quale settare il valore.
   * @param key string che definisce il nome del FormControl per il set.
   * @param options Object compatibile con la funzione "setValue", per definire delle opzioni sull'azione.
   */
  private enableFormField(form: FormGroup, key: string, options?: Object) {
    // Richiamo il servizio di utility
    this._riscaUtilities.enableFormField(form, key, options);
  }

  /**
   * Funzione di comodo che va a settare, per una specifica chiave, un valore nella form.
   * @param key string chiave del campo form da valorizzare.
   * @param value any con il valore da settare per il campo.
   */
  private setFormValue(f: FormGroup, key: string, value: any) {
    // Setto il valore
    this._riscaUtilities.setFormValue(f, key, value);
  }

  /**
   * Funzione di comodo che verifica se la nazione di un recapito è italia.
   * @param recapito RecapitoVo con le informazioni del recapito da verificare.
   * @boolean boolean che definisce se la nazione del recapito è italia.
   */
  isRecapitoItalia(recapito: RecapitoVo): boolean {
    // Recupero l'id nazione del recapito
    const codNazioneR = recapito?.nazione_recapito?.cod_istat_nazione;
    // Verifico se la nazione del recapito è italiana
    return codNazioneR === CodIstatNazioni.italia;
  }

  /**
   * Funzione di comodo che verifica se la nazione dell'indirizzo di spedizione è Italia.
   * @param indirizzo IndirizzoSpedizioneVo dalla quale estrarre le informazioni per la nazionalità.
   * @returns boolean che indica se l'indirizzo di spedizione è impostato su Italia.
   */
  isIndirizzoItaliano(indirizzo: IndirizzoSpedizioneVo): boolean {
    // Verifico l'input
    if (!indirizzo) {
      // Non può essere italia
      return false;
    }

    // Recupero la nazione dall'indirizzo
    const nazioneIS = indirizzo.nazione_postel?.toUpperCase() ?? '';
    const nazioneCheck = DenominazioneNazioni.italia.toUpperCase();
    // Verifico se la nazione è ITALIA
    return nazioneIS === nazioneCheck;
  }

  /**
   * Funzione di supporto che gestisce le mappe di errore, sulla base della costruzione del recapito.
   * @param indirizzo IndirizzoSpedizioneVo con le informazioni per la decisione degli errori da visualizzare sulla base della nazione.
   * @returns IISErrorMaps con le informazioni per la mappa degli errori.
   */
  generaMappeErroriForm(indirizzo: IndirizzoSpedizioneVo): IISErrorMaps {
    // Recupero le informazioni delle mappe per i vari campi
    const MAP_IS_DESTINATARIO = this.EM.MAP_IS_DESTINATARIO;
    const MAP_IS_PRESSO = this.EM.MAP_IS_PRESSO;
    const MAP_IS_FRAZIONE = this.EM.MAP_IS_FRAZIONE;
    const MAP_IS_INDIRIZZO = this.EM.MAP_IS_INDIRIZZO;
    let MAP_IS_CAP = this.EM.MAP_IS_CAP;
    const MAP_IS_COMUNE_CITTA = this.EM.MAP_IS_COMUNE_CITTA;
    let MAP_IS_PROVINCIA = this.EM.MAP_IS_PROVINCIA;
    const MAP_IS_NAZIONE = this.EM.MAP_IS_NAZIONE;

    // Verifico se il recapito è italiano
    const isItalia = this.isIndirizzoItaliano(indirizzo);
    const PROV_MSG = isItalia ? this.EM.MAP_IS_LENGTH_PROVINCIA_ITA : [];

    MAP_IS_PROVINCIA = [...MAP_IS_PROVINCIA, ...PROV_MSG];

    // Ritorno l'oggetto con la mappa dei dati
    const errorsMap: IISErrorMaps = {
      MAP_IS_DESTINATARIO,
      MAP_IS_PRESSO,
      MAP_IS_FRAZIONE,
      MAP_IS_INDIRIZZO,
      MAP_IS_CAP,
      MAP_IS_COMUNE_CITTA,
      MAP_IS_PROVINCIA,
      MAP_IS_NAZIONE,
    };

    // Ritorno la mapppa degli errori
    return errorsMap;
  }

  /**
   * ###############################
   * FUNZIONI DI CONVERSIONE OGGETTI
   * ###############################
   */

  /**
   * Funzione di conversione di un oggetto IIndirizzoSpedizioneForm, in un oggetto IndirizzoSpedizioneVo.
   * La funzione necessita anche di un oggetto "base" che definisce tutte le informazioni già costruite dal server per l'oggetto dell'indirizzo di spedizione.
   * @param isForm IIndirizzoSpedizioneForm da convertire.
   * @param base IndirizzoSpedizioneVo come base di partenza per le informazioni dell'indirizzo di spedizione.
   * @returns IndirizzoSpedizioneVo convertito.
   */
  convertIISFormToISVoFromBase(
    isForm: IIndirizzoSpedizioneForm,
    base: IndirizzoSpedizioneVo
  ): IndirizzoSpedizioneVo {
    // Verifico l'input
    if (!isForm) {
      // Ritorno undefined
      return undefined;
    }

    // Creo un oggetto IndirizzoSpedizioneVo
    const isVo = new IndirizzoSpedizioneVo();
    // Definisco le informazioni dall'oggetto base
    isVo.id_recapito = base.id_recapito;
    isVo.id_recapito_postel = base.id_recapito_postel;
    isVo.id_gruppo_soggetto = base.id_gruppo_soggetto;
    isVo.ind_valido_postel = base.ind_valido_postel;
    // Definisco le informazioni dell'oggetto della form
    isVo.destinatario_postel = isForm.destinatario;
    isVo.presso_postel = isForm.presso;
    isVo.indirizzo_postel = isForm.indirizzo;
    isVo.citta_postel = isForm.comune_citta;
    isVo.provincia_postel = isForm.provincia;
    isVo.cap_postel = isForm.cap;
    isVo.frazione_postel = isForm.frazione;
    isVo.nazione_postel = isForm.nazione;

    // Ritorno l'oggetto generato
    return isVo;
  }

  /**
   * ############################################################
   * FUNZIONI DI COMODO PER IMPOSTARE I VALORI DEI CAMPI NEL FORM
   * ############################################################
   */

  /**
   * Setta i validatori della form passata in input, in base alla configurazione passata in input per: recapito.
   * La modifica avverrà per referenza.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param indirizzo IndirizzoSpedizioneVo dalla quale estrarre le informazioni per la nazionalità.
   */
  setValidatoriForm(form: FormGroup, indirizzo: IndirizzoSpedizioneVo) {
    // Verifico l'input
    if (!form || !indirizzo) {
      // Nulla da settare, blocco le logiche
      return;
    }

    // Dato che la struttura potrebbe essere differente rispetto a quella che può essere gestita, metto dentro un try catch
    try {
      // Verifico se la nazione del recapito è italiana
      const isItalia = this.isIndirizzoItaliano(indirizzo);

      // A seconda che sia italia o estero, recupero un tipo di configurazione dei validatori
      const validatori: IIndirizzoSpedizioneValidators = isItalia
        ? this.ISF_C.validatoriISItalia
        : this.ISF_C.validatoriISEstero;

      // Lancio il set dei validatori
      this.setValidatoriDestinatario(form, validatori.destinatario);
      this.setValidatoriPresso(form, validatori.presso);
      this.setValidatoriIndirizzo(form, validatori.indirizzo);
      this.setValidatoriComuneCitta(form, validatori.comune_citta);
      this.setValidatoriProvincia(form, validatori.provincia, isItalia);
      this.setValidatoriCap(form, validatori.cap);
      this.setValidatoriFrazione(form, validatori.frazione);
      this.setValidatoriNazione(form, validatori.nazione, isItalia);
      // #
    } catch (e) {
      // Non faccio niente
    }
  }

  /**
   * Setta i disabilitatori della form passata in input, in base alla configurazione passata in input per: recapito.
   * La modifica avverrà per referenza.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param indirizzo IndirizzoSpedizioneVo dalla quale estrarre le informazioni per la nazionalità.
   */
  setDisabilitatoriForm(form: FormGroup, indirizzo: IndirizzoSpedizioneVo) {
    // Verifico l'input
    if (!form || !indirizzo) {
      // Nulla da settare, blocco le logiche
      return;
    }

    // Dato che la struttura potrebbe essere differente rispetto a quella che può essere gestita, metto dentro un try catch
    try {
      // Verifico se la nazione del recapito è italiana
      const isItalia = this.isIndirizzoItaliano(indirizzo);

      // Lancio il set dei validatori
      this.setDisabilitatoriNazione(form, isItalia);
      // #
    } catch (e) {
      // Non faccio niente
    }
  }

  /**
   * Setta i valori della form passata in input, in base alla configurazione passata in input per: indirizzo di spedizione.
   * La modifica avverrà per referenza.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param indirizzoSpedizione IndirizzoSpedizioneVo contenente le informazioni per il setup iniziale dei dati.
   */
  setValoriForm(form: FormGroup, indirizzoSpedizione?: IndirizzoSpedizioneVo) {
    // Verifico l'input
    if (!form || !indirizzoSpedizione) {
      // Nulla da settare, blocco le logiche
      return;
    }

    // Dato che la struttura potrebbe essere differente rispetto a quella che può essere gestita, metto dentro un try catch
    try {
      // Variabili di comodo
      const is = indirizzoSpedizione;

      // Recupero dall'oggetto le informazioni per il set
      this.setDestinatario(form, is.destinatario_postel);
      this.setPresso(form, is.presso_postel);
      this.setIndirizzo(form, is.indirizzo_postel);
      this.setComuneCitta(form, is.citta_postel);
      this.setProvincia(form, is.provincia_postel);
      this.setCap(form, is.cap_postel);
      this.setFrazione(form, is.frazione_postel);
      this.setNazione(form, is.nazione_postel);
      // #
    } catch (e) {
      // Non faccio niente
    }
  }

  /**
   * #################################################
   * FUNZIONI DI SET DEI VALIDATORI DEI CAMPI DEL FORM
   * #################################################
   */

  /**
   * Funzione specifica di set validatori del form per: destinatario.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param validatori Array di ValidatorFn con i validatori da impsotare nel form per lo specifico campo.
   */
  private setValidatoriDestinatario(
    form: FormGroup,
    validatori: ValidatorFn[]
  ) {
    // Definisco la chiave del campo
    const k = this.ISF_C.DESTINATARIO;
    // Setto il campo
    this.setFieldValidator(form, k, validatori);
  }

  /**
   * Funzione specifica di set validatori del form per: presso.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param validatori Array di ValidatorFn con i validatori da impsotare nel form per lo specifico campo.
   */
  private setValidatoriPresso(form: FormGroup, validatori: ValidatorFn[]) {
    // Definisco la chiave del campo
    const k = this.ISF_C.PRESSO;
    // Setto il campo
    this.setFieldValidator(form, k, validatori);
  }

  /**
   * Funzione specifica di set validatori del form per: indirizzo.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param validatori Array di ValidatorFn con i validatori da impsotare nel form per lo specifico campo.
   */
  private setValidatoriIndirizzo(form: FormGroup, validatori: ValidatorFn[]) {
    // Definisco la chiave del campo
    const k = this.ISF_C.INDIRIZZO;
    // Setto il campo
    this.setFieldValidator(form, k, validatori);
  }

  /**
   * Funzione specifica di set validatori del form per: comunecitta.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param validatori Array di ValidatorFn con i validatori da impsotare nel form per lo specifico campo.
   */
  private setValidatoriComuneCitta(form: FormGroup, validatori: ValidatorFn[]) {
    // Definisco la chiave del campo
    const k = this.ISF_C.COMUNE_CITTA;
    // Setto il campo
    this.setFieldValidator(form, k, validatori);
  }

  /**
   * Funzione specifica di set validatori del form per: provincia.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param validatori Array di ValidatorFn con i validatori da impsotare nel form per lo specifico campo.
   * @param isItalia boolean che indica se il recapito di riferimento, è impostato con lo stato italiano.
   */
  private setValidatoriProvincia(
    form: FormGroup,
    validatori: ValidatorFn[],
    isItalia: boolean
  ) {
    // Variabili di comodo
    let v = [];
    // Definisco la chiave del campo
    const k = this.ISF_C.PROVINCIA;

    // Verifico se lo stato del recapito di riferimento rispetto all'indirizzo spedizione è italia
    v = isItalia ? [...validatori, ...[Validators.required]] : validatori;

    // Setto il campo
    this.setFieldValidator(form, k, validatori);
  }

  /**
   * Funzione specifica di set validatori del form per: cap.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param validatori Array di ValidatorFn con i validatori da impsotare nel form per lo specifico campo.
   */
  private setValidatoriCap(form: FormGroup, validatori: ValidatorFn[]) {
    // Definisco la chiave del campo
    const k = this.ISF_C.CAP;
    // Setto il campo
    this.setFieldValidator(form, k, validatori);
  }

  /**
   * Funzione specifica di set validatori del form per: frazione.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param validatori Array di ValidatorFn con i validatori da impsotare nel form per lo specifico campo.
   */
  private setValidatoriFrazione(form: FormGroup, validatori: ValidatorFn[]) {
    // Definisco la chiave del campo
    const k = this.ISF_C.FRAZIONE;
    // Setto il campo
    this.setFieldValidator(form, k, validatori);
  }

  /**
   * Funzione specifica di set validatori del form per: nazione.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param validatori Array di ValidatorFn con i validatori da impsotare nel form per lo specifico campo.
   * @param isItalia boolean che indica se il recapito di riferimento, è impostato con lo stato italiano.
   */
  private setValidatoriNazione(
    form: FormGroup,
    validatori: ValidatorFn[],
    isItalia: boolean
  ) {
    // Variabili di comodo
    let v = [];
    // Definisco la chiave del campo
    const k = this.ISF_C.NAZIONE;

    // Verifico se lo stato del recapito di riferimento rispetto all'indirizzo spedizione è italia
    v = isItalia ? validatori : [...validatori, ...[Validators.required]];

    // Setto il campo
    this.setFieldValidator(form, k, v);
  }

  /**
   * #####################################################
   * FUNZIONI DI SET DEI DISABILITATORI DEI CAMPI DEL FORM
   * #####################################################
   */

  /**
   * Funzione specifica di set disabilitatori del form per: nazione.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param isItalia boolean che indica se il recapito di riferimento, è impostato con lo stato italiano.
   */
  private setDisabilitatoriNazione(form: FormGroup, isItalia: boolean) {
    // Definisco la chiave del campo
    const k = this.ISF_C.NAZIONE;
    // Setto il campo
    isItalia ? this.disableFormField(form, k) : this.enableFormField(form, k);
  }

  /**
   * ##################################
   * FUNZIONI DI SET DEI CAMPI DEL FORM
   * ##################################
   */

  /**
   * Funzione specifica di set del form per: destinatario.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param value string con il valore da impsotare nel form.
   */
  private setDestinatario(form: FormGroup, value: string) {
    // Effettuo la formattazione del valore
    const v = this._riscaUtilities.formatStringToUpperNoAccent(value);
    // Definisco la chiave del campo
    const k = this.ISF_C.DESTINATARIO;
    // Setto il campo
    this.setFormValue(form, k, v);
  }

  /**
   * Funzione specifica di set del form per: presso.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param value string con il valore da impsotare nel form.
   */
  private setPresso(form: FormGroup, value: string) {
    // Definisco la chiave del campo
    const k = this.ISF_C.PRESSO;
    // Setto il campo
    this.setFormValue(form, k, value);
  }

  /**
   * Funzione specifica di set del form per: indirizzo.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param value string con il valore da impsotare nel form.
   */
  private setIndirizzo(form: FormGroup, value: string) {
    // Definisco la chiave del campo
    const k = this.ISF_C.INDIRIZZO;
    // Setto il campo
    this.setFormValue(form, k, value);
  }

  /**
   * Funzione specifica di set del form per: comunecitta.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param value string con il valore da impsotare nel form.
   */
  private setComuneCitta(form: FormGroup, value: string) {
    // Definisco la chiave del campo
    const k = this.ISF_C.COMUNE_CITTA;
    // Setto il campo
    this.setFormValue(form, k, value);
  }

  /**
   * Funzione specifica di set del form per: provincia.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param value string con il valore da impsotare nel form.
   */
  private setProvincia(form: FormGroup, value: string) {
    // Definisco la chiave del campo
    const k = this.ISF_C.PROVINCIA;
    // Setto il campo
    this.setFormValue(form, k, value);
  }

  /**
   * Funzione specifica di set del form per: cap.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param value string con il valore da impsotare nel form.
   */
  private setCap(form: FormGroup, value: string) {
    // Definisco la chiave del campo
    const k = this.ISF_C.CAP;
    // Setto il campo
    this.setFormValue(form, k, value);
  }

  /**
   * Funzione specifica di set del form per: frazione.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param value string con il valore da impsotare nel form.
   */
  private setFrazione(form: FormGroup, value: string) {
    // Definisco la chiave del campo
    const k = this.ISF_C.FRAZIONE;
    // Setto il campo
    this.setFormValue(form, k, value);
  }

  /**
   * Funzione specifica di set del form per: nazione.
   * @param form FormGroup con l'oggetto del form d'aggiornare.
   * @param value string con il valore da impsotare nel form.
   */
  private setNazione(form: FormGroup, value: string) {
    // Definisco la chiave del campo
    const k = this.ISF_C.NAZIONE;
    // Setto il campo
    this.setFormValue(form, k, value);
  }
}
