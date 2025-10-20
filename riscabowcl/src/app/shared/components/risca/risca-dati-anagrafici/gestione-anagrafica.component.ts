import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { SoggettoVo } from '../../../../core/commons/vo/soggetto-vo';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { DatiAnagraficiConsts } from '../../../../features/pratiche/consts/dati-anagrafici/dati-anagrafici.consts';
import { SoggettoDatiAnagraficiService } from '../../../../features/pratiche/service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { CommonConsts } from '../../../consts/common-consts.consts';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import { RiscaErrorsMap } from '../../../utilities/classes/errors-maps';
import { RiscaFormChildComponent } from '../risca-form-child/risca-form-child.component';

/**
 * Classe comune ai componenti che gestiscono le informazioni riguardante i dati anagrafici.
 */
@Component({
  selector: 'gestione-anagrafica',
  template: ``,
  styleUrls: [],
})
export class GestioneAnagraficaComponent<T>
  extends RiscaFormChildComponent<T>
  implements OnInit, OnDestroy
{
  /** Oggetto CommonConsts contenente le costanti di uso comune. */
  C_C = new CommonConsts();
  /** Oggetto con le costanti per la sezione dei dati anagrafici. */
  DA_C = new DatiAnagraficiConsts();
  /** Oggetto contenente una serie di costanti per la gestione delle mappe di errori. */
  EM = new RiscaErrorsMap();

  /** Oggetto SoggettoVo contenente le informazioni di parametrizzazione del componente. */
  @Input('datiAnagrafici') datiAnagraficiConfig: SoggettoVo;
  /** Array di string che definisce le configurazioni delle informazioni modificate alla fonte */
  @Input() fonteConfigs: string[] = [];
  /** Boolean che definisce lo stato di disabilitazione degli elementi del componente, per: inserisci/modifica soggetto. */
  @Input('insModSoggettoDisabled') insModSoggettoDisabled = false;
  /** Boolean che definisce lo stato di disabilitazione degli elementi del componente, per: soggetto in pratica. */
  @Input('praticaSoggettoDisabled') praticaSoggettoDisabled = false;

  /** Boolean che definisce le abilitazioni dei dati anagrafici, scaricate dal servizio. */
  isGestioneAbilitata: boolean;

  /**
   * Costruttore.
   */
  constructor(
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaUtilities: RiscaUtilitiesService,
    protected _soggettoDA: SoggettoDatiAnagraficiService
  ) {
    // Richiamo il super
    super(logger, navigationHelper, riscaFormSubmitHandler, riscaUtilities);
    // Funzione di setup generico
    this.setupAbilitazioniAnagrafica();
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * ngOnDestroy.
   */
  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * Funzione che definisce le abilitazioni per i dati anagrafici, secondo le abilitazioni scaricate.
   */
  private setupAbilitazioniAnagrafica() {
    // Dal servizio, verifico se è abilitata l'inserimento/modifica dei campi
    this.isGestioneAbilitata = this._soggettoDA.isGestioneAbilitata;
  }
}
