import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Subscription } from 'rxjs';
import { RiscaStampaPraticaService } from '../../../services/risca/risca-stampa/risca-stampa-pratica.service';
import { RiscaServerError } from '../../../utilities';
import { RiscaStampaPraticaBTNConst } from './utilities/risca-stampa-pratica-btn.consts';

@Component({
  selector: 'risca-stampa-pratica-btn',
  templateUrl: './risca-stampa-pratica-btn.component.html',
  styleUrls: ['./risca-stampa-pratica-btn.component.scss'],
})
export class RiscaStampaPraticaBtnComponent implements OnInit {
  /** Oggetto contenente una serie di costanti comuni per la sezione dei dati contabili. */
  RSP_C = RiscaStampaPraticaBTNConst;

  /** Input boolean che definisce se il pulsante deve essere disattivato. */
  @Input() disabled: boolean = false;
  /** Input setter per l'id pratica specifico per la stampa. */
  @Input() set idPratica(idPratica: number) {
    // Assegno il disabled
    this._idPratica = idPratica;
  }

  /** EventEmitter che comunica al componente padre che è stata lanciata la stampa della pratica. */
  @Output() onStampaAvviata = new EventEmitter<any>();
  /** EventEmitter che comunica al componente padre che la stampa della pratica è andata in errore. */
  @Output() onStampaError = new EventEmitter<RiscaServerError>();

  /** Subscription che permette di collegarsi all'evento di errore generato dalla stampa pratica pdf. */
  private onStampaError$: Subscription;

  /** number che definisce l'id pratica SPECIFICO per la stmapa. */
  private _idPratica: number;

  /**
   * Costruttore.
   */
  constructor(private _riscaStampaP: RiscaStampaPraticaService) {
    // Lancio il setup del componente
    this.setupComponente();
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * ################################
   * FUNZIONI DI SETUP DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione di setup del componente.
   */
  private setupComponente() {
    // Lancio la funzione di setup dei listener
    this.setupListeners();
  }

  /**
   * Funzione di setup che permette al componente di agganciarsi agli eventi asincroni.
   */
  private setupListeners() {
    // Mi aggancio all'evento di cambio dei livelli del filo d'arianna
    this.onStampaError$ = this._riscaStampaP.onStampaError$.subscribe({
      next: (e: RiscaServerError) => {
        // Gestisco l'errore generato
        this.onStampaError.emit(e);
      },
    });
  }
  /**
   * #######################
   * FUNZIONI DEL COMPONENTE
   * #######################
   */

  /**
   * Funzione che effettua la stampa di una pratica.
   * Utilizzando il servizio, se l'id pratica non è definito a livello di componente, verrà usato quello nel servizio.
   */
  stampaPratica() {
    // Emetto l'evento di stampa pratica avviata
    this.onStampaAvviata.emit();
    // Richiamo il servizio di stampa
    this._riscaStampaP.stampaEApriPDFPratica(this._idPratica);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter di comodo che verifica se esiste una pratica in sessione per la stampa.
   * @returns boolean con il risultato del check.
   */
  get praticaInSessione(): boolean {
    // Recupero dal servizio se esiste un id pratica
    const idP = this._riscaStampaP.getIdPraticaStampa();
    // Verifico e ritorno se esiste una pratica in sessione
    return idP != undefined;
  }
}
