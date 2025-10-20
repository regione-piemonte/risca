import {
  Directive,
  ElementRef,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import { AccessoElementiAppService } from '../../../core/services/accesso-elementi-app.service';

@Directive({
  selector: '[accessoElementoApp]',
})
export class RiscaAccessoElementiAppDirective implements OnInit, OnChanges {
  /** String che definisce l'id dell'elemento per il check di abilitazione/disabilitazione dell'elemento. */
  @Input('codiceUtilizzo') code: string;
  /** Boolean che definisce una condizione alternativa per il disable dell'elemento. Ha meno priorità rispetto al flag recuperato da code. */
  @Input('otherDisabled') disabled: boolean;

  /** ElementRef che definisce l'oggetto rappresentativo del DOM in pagina. */
  private _element: ElementRef;
  /** Boolean che definisce se l'elemento è enable o disable a seconda della configurazione della profilazione. */
  private _accesso: boolean;

  /**
   * Costruttore.
   */
  constructor(
    element: ElementRef,
    private _accessoEA: AccessoElementiAppService
  ) {
    // Assegno localmente la referenza dell'oggetto.
    this.element = element;
  }

  ngOnInit() {
    // Richiamo il check per l'elemento
    this.initAccesso();
  }

  ngOnChanges(changes: SimpleChanges) {
    // Verifico se è stata modificato l'input
    if (changes.disabled && !changes.disabled.firstChange) {
      // Verifico lo stato attuale dell'accesso
      if (this.utilizzoToEnabled) {
        // Verifico le altre condizioni
        this.setStatoElemento(!this.disabled);
      }
    }
  }

  /**
   * ##############################################
   * FUNZIONI DI CONTROLLO PER IL DISABLE DEL CAMPO
   * ##############################################
   */

  /**
   * Funzione che verifica e imposta lo stato di abilitazione e disabilitazione dell'elemento.
   */
  private initAccesso() {
    // Lancio il recupero + check disable sull'input
    this._accesso = this.accessoElementoApp(this.code);
    // Lancio il check effettivo dell'accesso della risorsa
    this.definisciStatoAccesso();
  }

  /**
   * Funzione di comodo che recupera la configurazione per l'utilizzo dell'elemento, dalla configurazione della profilazione utilizzo.
   * @param code string che definisce il codice per il recupero dell'elemento all'interno delle configurazioni della profilazione utilizzo.
   */
  private accessoElementoApp(code: string): boolean {
    // Verifico l'input
    if (code === undefined) {
      // Se non è definito, l'utilizzo è a true
      return true;
    }

    // Recupero dal servizio il flag per sapere lo stato di attivazione della componentistica
    return this._accessoEA.checkAccessoElementoApp(code);
  }

  /**
   * Funzione che gestisce lo stato di accesso (abilitato/disabilitato) della risorsa del DOM.
   */
  definisciStatoAccesso() {
    // Verifico se il flag sull'utilizzo è già stato valorizzato
    if (this.utilizzoToDisabled) {
      // Disabilito l'elemento
      this.setStatoElemento(false);
      // #
    } else {
      // Controllo le altre condizioni
      this.definisciStatoElemento(this.disabled);
    }
  }

  /**
   * Funzione di supporto per gestire lo stato dell'elemento per gli altri disabled.
   * @param disabled boolean con la configurazione per gli altri disabled.
   */
  definisciStatoElemento(disabled: boolean) {
    // Controllo le altre condizioni
    if (this.disabled) {
      // Disabilito l'elemento
      this.setStatoElemento(false);
      // #
    } else {
      // Abilito l'elemento
      this.setStatoElemento(true);
    }
  }

  /**
   * Funzione che abilita/disabilita l'elemento.
   * @param abilita boolean che definisce se l'elemento è d'abilitare.
   */
  setStatoElemento(abilita: boolean) {
    // Verifico che l'elemento sia correttamente collegato
    if (this.nativeElement == undefined) {
      // Interrompo le logiche
      return;
    }

    // Verifico il flag booleano
    if (abilita) {
      // Abilito l'elemento
      this.nativeElement.disabled = false;
      // #
    } else {
      // Disabilito l'elemento
      this.nativeElement.disabled = true;
    }
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Setter per element.
   */
  set element(e: ElementRef) {
    this._element = e;
  }

  /**
   * Getter per la proprietà native element.
   */
  get nativeElement() {
    return this._element?.nativeElement;
  }

  /**
   * Getter per la gestione del "disabled" per l'elemento.
   */
  get utilizzoToDisabled() {
    return this._accesso !== undefined && this._accesso === false;
  }

  /**
   * Getter per la gestione del "disabled" per l'elemento.
   */
  get utilizzoToEnabled() {
    return this._accesso !== undefined && this._accesso === true;
  }
}
