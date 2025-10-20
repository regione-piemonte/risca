import { Pipe, PipeTransform } from '@angular/core';
import { AccessoElementiAppKeyConsts } from '../../../../core/consts/accesso-elementi-app/accesso-elementi-app.consts';
import { AccessoElementiAppService } from '../../../../core/services/accesso-elementi-app.service';
import { RiscaLockPraticaService } from '../../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import {
  IRiscaNavItem,
  RiscaIstanzePratica,
} from '../../../../shared/utilities';
import { AmbitoService } from '../../../ambito/services';

/**
 * Pipe che verifica la configurazione per la gestione dell'abilitazione delle schede per quanto riguarda la pratica.
 */
@Pipe({ name: 'disabilitaNavPratica' })
export class DisabilitaNavPraticaPipe implements PipeTransform {
  /** Oggetto contenente i valori costanti per le chiavi d'accesso agli elementi dell'app. */
  AEAK_C = AccessoElementiAppKeyConsts;

  /**
   * Costruttore del Pipe.
   */
  constructor(
    private _ambito: AmbitoService,
    private _accessoElementiApp: AccessoElementiAppService
  ) {}

  /**
   * Funzione che gestisce le configurazioni della tab per le informazioni della pratica, e attiva o disattiva le tab.
   * @param nav IRiscaNavItem<RiscaIstanzePratica> con la configurazione dati della nav.
   * @param praticaInModifica boolean che definisce se la pratica è in modalità di modifica.
   * @returns boolean che definisce se la tab è da disabilitare.
   */
  transform(
    nav: IRiscaNavItem<RiscaIstanzePratica>,
    praticaInModifica: boolean
  ): any | undefined {
    // Verifico l'input
    if (!nav) {
      return true;
    }

    // Effettuo uno switch sulle chiavi identificative dell'oggetto
    switch (nav.ngbNavItem) {
      case RiscaIstanzePratica.generaliAmministrativi:
      case RiscaIstanzePratica.datiAnagrafici:
      case RiscaIstanzePratica.datiTecnici:
        // Gestione base, ritorno lo stato di disable
        return this.disabledByNav(nav);
      case RiscaIstanzePratica.datiContabili:
        // Gestione specifica per i dati contabili
        return this.disabledDatiContabili(nav, praticaInModifica);
      case RiscaIstanzePratica.documentiAllegati:
        // Gestione specifica per i documenti allegati
        return this.disabledDocumentiAllegati(nav, praticaInModifica);
      default:
        // Nessuno caso
        return true;
    }
  }

  /**
   * Funzione di gestione per il default dello stato di disabilitazione della tab.
   * @param nav IRiscaNavItem<RiscaIstanzePratica> con la configurazione dati della nav.
   * @returns boolean con lo stato di disable.
   */
  private disabledByNav(nav: IRiscaNavItem<RiscaIstanzePratica>): boolean {
    // Gestione base, ritorno lo stato di disable
    return nav?.disabled != undefined ? nav.disabled : false;
  }

  /**
   * Funzione di gestione per il nav per i dati contabili e il suo stato di disabilitazione.
   * @param nav IRiscaNavItem<RiscaIstanzePratica> con la configurazione dati della nav.
   * @param praticaInModifica boolean che definisce se la pratica è in modalità di modifica.
   * @returns boolean con lo stato di disable.
   */
  private disabledDatiContabili(
    nav: IRiscaNavItem<RiscaIstanzePratica>,
    praticaInModifica: boolean
  ): boolean {
    // Verifico lo stato del disabled e la modalita della pratica
    const disableFE = this.disableByFEModifica(nav, praticaInModifica);
    // Ritorno l'insieme delle condizioni
    return disableFE;
  }

  /**
   * Funzione di gestione per il nav per i documenti allegati e il suo stato di disabilitazione.
   * @param nav IRiscaNavItem<RiscaIstanzePratica> con la configurazione dati della nav.
   * @param praticaInModifica boolean che definisce se la pratica è in modalità di modifica.
   * @returns boolean con lo stato di disable.
   */
  private disabledDocumentiAllegati(
    nav: IRiscaNavItem<RiscaIstanzePratica>,
    praticaInModifica: boolean
  ): boolean {
    // Verifico lo stato del disabled e la modalita della pratica
    const disableFE = this.disableByFEModifica(nav, praticaInModifica);
    // Verifico lo stato della configurazione per l'ambito
    const disableAmbito = !this._ambito.visDocumentiPratica;
    // Verifico se la configurazione di accesso agli elementi è attivo
    let AEA_documentiDisabled: boolean;
    AEA_documentiDisabled = this._accessoElementiApp.isAEADisabled(
      this.AEAK_C.PAGINA_DOC_ALLEGATI
    );

    // Ritorno l'insieme delle condizioni
    return disableFE || disableAmbito || AEA_documentiDisabled;
  }

  /**
   * Funzione di gestione per il nav per il suo stato di disabilitazione.
   * @param nav IRiscaNavItem<RiscaIstanzePratica> con la configurazione dati della nav.
   * @param praticaInModifica boolean che definisce se la pratica è in modalità di modifica.
   * @returns boolean con lo stato di disable.
   */
  private disableByFEModifica(
    nav: IRiscaNavItem<RiscaIstanzePratica>,
    praticaInModifica: boolean
  ): boolean {
    // Gestione base, ritorno lo stato di disable
    const navDisabled = nav?.disabled != undefined ? nav.disabled : false;
    // Verifico lo stato del disabled e la modalita della pratica
    const disableFE = navDisabled || !praticaInModifica;

    // Ritorno l'insieme delle condizioni
    return disableFE;
  }
}

/**
 * Pipe che verifica la tab attualmente attiva. Se la tab è dati contabili o documenti allegati, non visualizza i pulsanti di SALVA e ANNULLA.
 */
@Pipe({ name: 'abilitaPulsantiPratica' })
export class AbilitaPulsantiPraticaPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor(private _riscaLockP: RiscaLockPraticaService) {}

  /**
   * Funzione che verifica la tab attualmente attiva e definisce se i pulsanti sono da visualizzare.
   * @param tab RiscaIstanzePratica che definisce la tab attiva.
   * @returns boolean che definisce se il pulsante è da visualizzare.
   */
  transform(tab: RiscaIstanzePratica): boolean {
    // Verifico la tab esista
    if (!tab) {
      // Niente configurazione
      return false;
    }

    // Verifico se la pratica è lockata da un altro utente
    if (this.isPraticaLocked) {
      // Ritorno false
      return false;
    }

    // Verifico i casi per la tab
    switch (tab) {
      case RiscaIstanzePratica.generaliAmministrativi:
      case RiscaIstanzePratica.datiAnagrafici:
      case RiscaIstanzePratica.datiTecnici:
        return true;
      case RiscaIstanzePratica.datiContabili:
      case RiscaIstanzePratica.documentiAllegati:
      default:
        return false;
    }
  }

  /**
   * Getter di comodo che verifica se la pratica è lockata.
   * @returns boolean con il risultato del check.
   */
  get isPraticaLocked(): boolean {
    // Verifico che esista nel servizio la configurazione del lock pratica
    const existLockStatus = this._riscaLockP.isPraticaInLockStatus();
    // Verifico se un altro utente sta lockando la pratica
    const anotherUserLocker = this._riscaLockP.isAnotherUserLockingPratica();
    // Ritorno le condizioni di disabilitazione
    return existLockStatus && anotherUserLocker;
  }
}
