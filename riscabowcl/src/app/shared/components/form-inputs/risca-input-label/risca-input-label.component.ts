import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import { RiscaFormInputCss, RiscaLabelPosition } from '../../../utilities';

@Component({
  selector: 'risca-input-label',
  templateUrl: './risca-input-label.component.html',
  styleUrls: ['./risca-input-label.component.scss'],
})
export class RiscaInputLabelComponent<T> implements OnInit {
  /** Oggetto di costanti comuni all'applicazione. */
  C_C = new CommonConsts()

  /** Input che definisce le configurazioni per gli stili della input. */
  @Input() cssConfig: RiscaFormInputCss;
  /** Input che definisce le configurazioni per i dati della input. */
  @Input() dataConfig: T | any;

  /** Input che definisce la posizione della label. */
  @Input() position = RiscaLabelPosition.top;

  /** String che definisce il nome del FormControl a cui è assegnato il componente. */
  @Input('idFormControl') idFC: string;
  /** FormGroup a cui fa riferimento il componente. */
  @Input() formGroup: FormGroup;
  /** String che definisce l'id del pradre come id del DOM. */
  @Input() idDOM: string;

  /** Boolean che disabilita il controllo sul FormControl required. */
  @Input('disableRequiredCheck') disableRequired = true;

  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

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
   * Getter che verifica se il form control è obbligatorio.
   */
  get require() {
    // Verifico se è abilitato il check di controllo
    if (this.disableRequired) {
      return false;
    }

    // Variabili di comodo
    const fg = this.formGroup;
    const idFC = this.idFC;
    const req = 'required';

    // Verifico se esistono dei validatori ed esiste il required
    return this._riscaUtilities.hasValidator(fg, idFC, req);
  }

  /**
   * Getter per lo status della proprietà: label; unito con la direzione.
   */
  get checkLabel() {
    // Definisco le condizioni per label
    const checkLT = this.dataConfig?.label !== undefined;
    const checkLL = this.dataConfig?.labelLeft !== undefined;
    const checkLR = this.dataConfig?.labelRight !== undefined;
    const checkLB = this.dataConfig?.labelBottom !== undefined;

    // Verifico la posizione
    switch (this.position) {
      case RiscaLabelPosition.top:
        return checkLT;
      case RiscaLabelPosition.left:
        return checkLL;
      case RiscaLabelPosition.right:
        return checkLR;
      case RiscaLabelPosition.bottom:
        return checkLB;
      default:
        return false;
    }
  }

  /**
   * Getter per il dato della proprietà: label; a seconda della posizione.
   */
  get dataLabel() {
    // Verifico la posizione
    switch (this.position) {
      case RiscaLabelPosition.top:
        return this.dataConfig.label;
      case RiscaLabelPosition.left:
        return this.dataConfig.labelLeft;
      case RiscaLabelPosition.right:
        return this.dataConfig.labelRight;
      case RiscaLabelPosition.bottom:
        return this.dataConfig.labelBottom;
      default:
        return false;
    }
  }

  /**
   * Getter per il dato della proprietà: hide; a seconda della posizione.
   */
  get hideLabel() {
    // Verifico la posizione
    switch (this.position) {
      case RiscaLabelPosition.top:
        return this.dataConfig.hideLabel;
      case RiscaLabelPosition.left:
        return this.dataConfig.hideLabelLeft;
      case RiscaLabelPosition.right:
        return this.dataConfig.hideLabelRight;
      case RiscaLabelPosition.bottom:
        return this.dataConfig.hideLabelBottom;
      default:
        return false;
    }
  }

  /**
   * Getter per lo status della proprietà: extraLabelRight.
   */
  get checkExtraLabelRight() {
    return this.dataConfig?.extraLabelRight !== undefined;
  }

  /**
   * Getter per lo status della proprietà: extraLabelSub.
   */
  get checkExtraLabelSub() {
    return this.dataConfig?.extraLabelSub !== undefined;
  }

  /**
   * Getter per la posizione della label: top.
   */
  get isLabelTop() {
    return this.position === RiscaLabelPosition.top;
  }

  /**
   * Getter per la posizione della label: bottom.
   */
  get isLabelBottom() {
    return this.position === RiscaLabelPosition.bottom;
  }

  /**
   * Getter per la posizione della label: left.
   */
  get isLabelLeft() {
    return this.position === RiscaLabelPosition.left;
  }

  /**
   * Getter per la posizione della label: right.
   */
  get isLabelRight() {
    return this.position === RiscaLabelPosition.right;
  }
}
