import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { MappaErroriFormECodici, RiscaFormInputCss } from '../../../utilities';
import { isEqual } from 'lodash';

@Component({
  selector: 'risca-form-control-error',
  templateUrl: './risca-form-control-error.component.html',
  styleUrls: ['./risca-form-control-error.component.scss'],
})
export class RiscaFormControlErrorComponent implements OnInit {
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();

  /** Input che definisce le configurazioni per gli stili della input. */
  @Input() cssConfig: RiscaFormInputCss;

  /** String che definisce il nome del FormControl a cui è assegnato il componente. */
  @Input('idFormControl') idFC: string;
  /** FormGroup a cui fa riferimento il componente. */
  @Input() formGroup: FormGroup;
  /** Lista di oggetti MappaErroriFormECodici contenente la lista degli errori che devono essere gestiti per il form control. */
  @Input('errMapFormControl') mappaErroriFC: MappaErroriFormECodici[] = [];
  /** Boolean che tiene traccia dello stato di Submit del form padre. */
  @Input() formSubmitted: boolean = false;

  /** Flag booleano che permette d'ignorare gli errori qualora esista un valore di placeholder selezionato. */
  @Input() ignoreDefault: boolean = false;

  /** Boolean che definisce se abilitare la modalità di debug. */
  @Input() debug = false;

  _manualUpdateMsg: any;

  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  ngOnInit() {
    // Verifico che esistano i dati input
    if (!this.idFC) {
      throw new Error('idFormControl not defined');
    }
    if (!this.formGroup) {
      throw new Error('formGroup not defined');
    }
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Funzione che verifica la validità del dato.
   */
  get checkValidationData() {
    // Recupero il valore
    const value = this.formControl.value;
    // Variabili di comodo
    const formSub = this.formSubmitted;
    const vExists = value == null || value.__null;

    // Se il form è stato submittato, ma il valore è null e ignoreDefault è attivo blocco il controllo
    const check = formSub && vExists && !this.ignoreDefault;
    // Ritorno il check
    return check;
  }

  /**
   * Funzione che verifica la validità del dato.
   */
  get checkValidationBlur() {
    // Verifico se il formcontrol non è valido
    const invalid = this.formControl.invalid;
    const isSubmitted = this.formSubmitted;
    const isDirty = this.formControl.dirty;
    const isTouched = this.formControl.touched;

    // Verifico se il form ha subito delle modifiche (tipo blur del campo)
    const actionComplete = isSubmitted || isDirty || isTouched;
    // Se il form è invalido ed è stata completata un'azione attivo il controllo
    const check = invalid && actionComplete;
    // Ritorno il check
    return check;
  }

  /**
   * Getter di comodo che gestisce il cambio di errore e l'aggiornamento dell'interfaccia.
   */
  get manualUpdateMsg() {
    // Recupero le informazioni per la gestione delle casistiche
    const fce = this.formControl?.errors;
    const mum = this._manualUpdateMsg;

    // 1) Non c'è mai stato errore e non ci sono errori sul form control
    if (!fce && !mum) {
      // Niente da modificare
      return undefined;
    }

    // 2) Non c'è mai stato errore e il form ha lanciato errore
    if (fce && !mum) {
      // Imposto l'errore dentro manual update msg
      this._manualUpdateMsg = fce;
      // Ritorno il nuovo errore
      return this._manualUpdateMsg;
    }

    // 3) Il form non ha errori, ma precedentemente è stato generato errore
    if (!fce && mum) {
      // Resetto l'errore manuale
      this._manualUpdateMsg = undefined;
      // Ritorno il nuovo errore
      return this._manualUpdateMsg;
    }

    // 4) Il form ha generato errore ed esiste un errore precedente, ed è un errore differente
    if (fce && mum && !isEqual(fce, mum)) {
      // Aggiorno l'errore manualmente
      this._manualUpdateMsg = fce;
      // Ritorno il nuovo errore
      return this._manualUpdateMsg;
      // #
    } else {
      // L'oggetto d'errore è lo stesso
      return this._manualUpdateMsg;
    }
  }

  /**
   * Getter per il form control.
   */
  get formControl() {
    return this.formGroup.get(this.idFC);
  }
}
