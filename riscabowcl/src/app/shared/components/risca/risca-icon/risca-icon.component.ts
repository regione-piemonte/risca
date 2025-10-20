import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';

@Component({
  selector: 'risca-icon',
  templateUrl: './risca-icon.component.html',
  styleUrls: ['./risca-icon.component.scss'],
})
export class RiscaIconComponent implements OnInit, OnChanges {
  /** Costante che rappresenta la classe per l'icona come default. */
  private ICON_DEFAULT = 'risca-icon';
  /** Costante che rappresenta la classe per la disabilitazione dell'icona. */
  private ICON_DISABLED = 'grey-filter';

  /** Array di string che definisce le classi da utilizzare. */
  @Input() classes: string[] = [];
  /** String che definisce l'url della risorsa dell'icona. */
  @Input('icon') iconPath: string = '';
  /** Any che definisce gli css per l'icona, deve essere compatibile con NgStyle. */
  @Input() iconStyles: any = {};
  /** String che definisce il title dell'icona. */
  @Input() iconTitle: string = '';
  /** Boolean che definisce se l'icona è disabilitata. */
  @Input() disabled: boolean = false;
  /** Boolean che definisce se utilizzare la classe di default dell'icona. */
  @Input() useDefault: boolean = true;
  /** Boolean che definisce se attivare il click dell'icona. */
  @Input() enableClick: boolean = false;

  /** Output per comunicare al padre il cambio di stato dell'accordion. */
  @Output() onIconClick: EventEmitter<any> = new EventEmitter<any>();

  /** Array di string che definisce le classi utilizzate effettivamente per l'icona. */
  iconClasses: string[] = [];

  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  ngOnInit() {
    // Lancio l'init della classi di stile
    this.initIconClasses();
  }

  ngOnChanges(changes: SimpleChanges) {
    // Verifico se ci sono state modifiche
    if (this.inputChanged(changes)) {
      // Aggiorno le class di stile
      this.initIconClasses();
    }
  }

  /**
   * Funzione che, a seconda delle configurazioni, genera le classi di stile per le icone.
   */
  initIconClasses() {
    // Resetto l'array contenente le classi per l'icona
    this.iconClasses = [];

    // Aggiungo le classi extra definite
    this.iconClasses = [...this.iconClasses, ...this.classes];
    // Verifico se la classe di default è da ignorare
    if (this.useDefault) {
      // Aggiungo la classe di default
      this.iconClasses.push(this.ICON_DEFAULT);
    }
    // Verifico se l'icona deve risultare disabilitata
    if (this.disabled) {
      // Aggiungo la classe di disabilitazione
      this.iconClasses.push(this.ICON_DISABLED);
    }
  }

  /**
   * #######################
   * FUNZIONI DEL COMPONENTE
   * #######################
   */

  /**
   * Funzione di supporto che verifica se c'è stato un cambiamento nell'input del componente.
   * @param changes SimpleChanges con le modifiche generate dall'ngOnChanges.
   * @returns boolean che definisce se anche solo un input è stato modificato.
   */
  private inputChanged(changes: SimpleChanges): boolean {
    // Verifico l'input
    if (!changes) {
      // Ritorno false
      return false;
    }

    // Verifico, per ogni input, se c'è stata una modifica
    const classesC = changes.classes && !changes.classes.firstChange;
    const iconStylesC = changes.iconStyles && !changes.iconStyles.firstChange;
    const disabledC = changes.disabled && !changes.disabled.firstChange;

    return classesC || iconStylesC || disabledC;
  }

  /**
   * Funzione collegata al click dell'icona.
   */
  iconClick() {
    // Verifico che il click sia abilitato
    if (this.enableClick) {
      // Emetto l'evento di change dell'accordion
      this.onIconClick.emit();
    }
  }
}
