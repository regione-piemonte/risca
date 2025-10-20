import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';
import { Observable } from 'rxjs';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';

@Component({
  selector: 'risca-image-box',
  templateUrl: './risca-image-box.component.html',
  styleUrls: ['./risca-image-box.component.scss'],
})
export class RiscaImageBox implements OnInit, OnChanges {
  /** Array di string che definisce le classi da utilizzare. */
  @Input() configs: any;

  /** Output per comunicare al padre il click sull'immagine. */
  @Output() onImgClick: EventEmitter<any> = new EventEmitter<any>();

  /** Observable che contiene il risultato dello scarico dati per l'immagine. */
  imageLoader: Observable<any>;

  /**
   * Costruttore.
   */
  constructor(private _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * ngOnInit.
   */
  ngOnInit() {
    // Lancio l'init della classi di stile
    this.initComponente();
  }

  /**
   * NgOnChanges.
   */
  ngOnChanges(changes: SimpleChanges) {
    // Variabile di comodo
    const changesConf = changes.configs;
    // Verifico se è modificato la configurazione
    if (changesConf && !changesConf.firstChange) {
      // Aggiorno le class di stile
      this.initComponente();
    }
  }

  /**
   * Funzione di init del componente.
   */
  initComponente() {
    // TEST
    const img =
      'https://www.tutorialspoint.com/assets/questions/media/426142-1668760765.png';
    this.imageLoader = this._riscaUtilities.responseWithDelay(img);
  }

  /**
   * #######################
   * FUNZIONI DEL COMPONENTE
   * #######################
   */

  /**
   * Funzione collegata al click dell'immagine.
   */
  imageClick() {
    // Emetto l'evento di change dell'accordion
    this.onImgClick.emit();
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo per il recupero di: imageTitle.
   */
  get imageTitle(): string {
    return 'immagine';
  }

  /**
   * Getter di comodo per il recupero di: imageClasses.
   */
  get imageClasses(): string[] {
    return [];
  }

  /**
   * Getter di comodo per il recupero di: imageStyles.
   */
  get imageStyles(): any {
    return {};
  }
}
