import {
  ChangeDetectionStrategy,
  Component,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  SimpleChanges
} from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ProvinciaCompetenzaVo } from '../../../../core/commons/vo/provincia-competenza-vo';
import {
  PraticaEDatiTecnici,
  PraticaVo
} from '../../../../core/commons/vo/pratica-vo';
import { TipoRiscossioneVo } from '../../../../core/commons/vo/tipo-riscossione-vo';
import { UserService } from '../../../../core/services/user.service';
import { PraticheService } from '../../service/pratiche.service';

@Component({
  selector: 'dati-sintesi',
  templateUrl: './dati-sintesi.component.html',
  styleUrls: ['./dati-sintesi.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DatiSintesiComponent implements OnInit, OnChanges, OnDestroy {
  /** Input che in base alla modalità pilota la visibilità della componente. */
  @Input('pratica') praticaEDatiTecnici: PraticaEDatiTecnici;

  /** Observable<string> che definisce la descrizione per: codiceUtenza. */
  codiceUtenza: Observable<string>
  /** Observable<string> che definisce la descrizione per: tipologiaPratica. */
  tipologiaPratica: Observable<string>

  /**
   * Costruttore
   */
  constructor(private _pratiche: PraticheService, private _user: UserService) {}

  ngOnInit() {
    // Lancio l'init del componente
    this.initComponente();
  }

  ngOnChanges(changes: SimpleChanges) {
    // Variabili di comodo
    const changesPEDT = changes?.praticaEDatiTecnici;

    // Verifico se ci sono da aggiornare i dati per la pratica
    if (changesPEDT && !changesPEDT.firstChange) {
      // Aggiorno le informazioni del componente
      this.initObservables();
    }
  }

  ngOnDestroy() {
    // Cancello la referenza degli observables
    this.codiceUtenza = undefined;
    this.tipologiaPratica = undefined;
  }

  /**
   * Funzione che definisce le logiche di init del componente.
   */  
   initComponente() {
    // Lancio l'init degli observable
    this.initObservables();
  }

  /**
   * Funzione di supporto che inizializza gli osbervables.
   */  
  initObservables() {
    // Assegno gli observables
    this.codiceUtenza = this.codiceUtenzaAsync;
    this.tipologiaPratica = this.tipologiaPraticaAsync;
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Mostra o nasconde il riquadro
   */
  get visibile(): boolean {
    return this.praticaEDatiTecnici != null;
  }

  /**
   * Getter per i dati della pratica, estratti dall'input.
   */
  get pratica(): PraticaVo {
    // Ritorno 'pratica'
    return this.praticaEDatiTecnici?.pratica;
  }

  /**
   * Getter per il codice utenza.
   */
  get codiceUtenzaAsync(): Observable<string> {
    // Recupero dalla pratica le informazioni
    const codRiscossioneProvincia = this.pratica?.cod_riscossione_prov;
    const progressivo = this.pratica?.cod_riscossione_prog ?? '';

    // Richiamo il servizio delle pratiche per la generazione dei dati
    return this._pratiche
      .getCodiceUtenzaByCodiceRiscossioneProvincia(codRiscossioneProvincia)
      .pipe(
        map((codiceUtenza: ProvinciaCompetenzaVo) => {
          // Recupero la sigla
          const sigla = codiceUtenza.sigla_provincia ?? '';
          // Unisco e ritorno i dati
          return `${sigla}${progressivo}`;
        })
      );
  }

  /**
   * Getter per il numero pratica.
   */
  get numeroPratica(): string {
    // Recupero e ritorno il numero pratica
    return this.pratica?.num_pratica ?? '';
  }

  /**
   * Getter per tipologia pratica.
   */
  get tipologiaPraticaAsync(): Observable<string> {
    // Recupero l'id ambito
    const idAmbito = this._user.idAmbito;
    // Recupero l'id tipo riscossione
    const idTipoRiscossione = this.pratica?.id_tipo_riscossione;

    // Recupero e ritorno la descrizione del tipo pratica
    return this._pratiche
      .getTipiRiscossioneByIdTipoRiscossione(idAmbito, idTipoRiscossione)
      .pipe(
        map((tipoRiscossione: TipoRiscossioneVo) => {
          // Recupero la descrizione
          return tipoRiscossione?.des_tipo_riscossione;
        })
      );
  }

  /**
   * Getter per lo stato riscossione.
   */
  get stato(): string {
    // Recupero e ritorno lo stato della riscossione
    return this.pratica?.stato_riscossione?.des_stato_riscossione ?? '';
  }

  /**
   * Getter per il tipo autorizzazione.
   */
  get procedimento(): string {
    // Recupero e ritorno il procedimento (tipo autorizza)
    return this.pratica?.tipo_autorizza?.des_tipo_autorizza ?? '';
  }
}
