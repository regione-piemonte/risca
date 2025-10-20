import { Component, Input, OnInit } from '@angular/core';
import { RecapitoVo } from '../../../../core/commons/vo/recapito-vo';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { SoggettoDatiAnagraficiService } from '../../../../features/pratiche/service/dati-anagrafici/soggetto-dati-anagrafici/soggetto-dati-anagrafici.service';
import { RiscaFormSubmitHandlerService } from '../../../services/risca/risca-form-submit-handler.service';
import { isRecapitoPrincipale } from '../../../services/risca/risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../../../services/risca/risca-utilities/risca-utilities.service';
import { AppActions } from '../../../utilities';
import { GestioneAnagraficaComponent } from './gestione-anagrafica.component';

/**
 * Classe comune ai componenti che gestiscono le informazioni riguardante i dati anagrafici.
 */
@Component({
  selector: 'gestione-recapito',
  template: ``,
  styleUrls: [],
})
export class GestioneRecapitoComponent<T>
  extends GestioneAnagraficaComponent<T>
  implements OnInit
{
  /** number che definisce l'id_recapito da recuperare. Questa proprietà viene considerata solo in modifica. */
  @Input() idRecapito: number | string;

  /**
   * Costruttore.
   */
  constructor(
    logger: LoggerService,
    navigationHelper: NavigationHelperService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaUtilities: RiscaUtilitiesService,
    soggettoDA: SoggettoDatiAnagraficiService
  ) {
    // Richiamo il super
    super(
      logger,
      navigationHelper,
      riscaFormSubmitHandler,
      riscaUtilities,
      soggettoDA
    );
  }

  ngOnInit() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter per il recapito principale.
   * @returns RecapitoVo.
   */
  get recapito(): RecapitoVo {
    // Recupero dal componente le informazioni per il recupero del recapito
    const datiAnagrafici = this.datiAnagraficiConfig;
    const idRecapito = this.idRecapito;

    // Definisco le condizioni per il recupero
    const existDa = datiAnagrafici?.recapiti?.length > 0;
    const existIdR = this.idRecapito !== undefined;
    const isMod = this.modalita === AppActions.modifica;
    // Controllo le condizioni
    if (!existDa || !existIdR || !isMod) {
      return undefined;
    }

    // Recapiti e configurazioni sono valide, recupero i dati
    return datiAnagrafici.recapiti.find((r: RecapitoVo) => {
      // Verifico l'id_recapito
      const sameIdRecapito = r.id_recapito === idRecapito;
      // Verifico l'id di supporto
      const sameId = r.__id === idRecapito;

      // Ritorno la verifica sull'id recapito
      return sameIdRecapito || sameId;
    });
  }

  /**
   * Getter che permette di sapere se il recapito del componente è il recapito principale.
   * @returns boolean con il risultato del check.
   */
  get isRecapitoPrinciapale(): boolean {
    // Recupero il recapito
    const r: RecapitoVo = this.recapito;
    // Verifico se esiste
    if (!r) {
      // Niente recapito
      return false;
    }

    // Verifico se il recapito è quello principale
    return isRecapitoPrincipale(r);
  }
}
