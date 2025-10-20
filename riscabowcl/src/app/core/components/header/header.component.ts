import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RISCA_ICONS } from '../../../shared/enums/icons.enums';
import { RiscaLockPraticaService } from '../../../shared/services/risca/risca-lock-pratica/risca-lock-pratica.service';
import { IRichiestaLockPraticaRes } from '../../../shared/services/risca/risca-lock-pratica/utilities/risca-lock-pratica.interfaces';
import { AppRoutes, RiscaServerError } from '../../../shared/utilities';
import {
  NgBoostrapContainersConsts,
  NgBoostrapDisplaysConsts,
  NgBoostrapPlacementsConsts,
} from '../../../shared/utilities/enums/ng-bootstrap.enums';
import { AmbitoVo } from '../../commons/vo/ambito-vo';
import { UserInfoVo } from '../../commons/vo/user-info-vo';
import { HttpHelperService } from '../../services/http-helper/http-helper.service';
import { UserService } from '../../services/user.service';
import { HeaderConsts } from './utilities/header.consts';

/**
 * @version SONARQUBE_22_04_24 Rimosso OnInit vuoto. 
 */
@Component({
  selector: 'risca-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class RiscaHeaderComponent implements OnDestroy {
  /** Classe contenente le costanti del componente. */
  H_C = new HeaderConsts();

  /** Recupero la costante per le direttive di ng boostrap. */
  NGBPlacements = NgBoostrapPlacementsConsts;
  NGBDisplays = NgBoostrapDisplaysConsts;
  NGBContainers = NgBoostrapContainersConsts;
  /** Recupero la costante per le icone applicative. */
  R_I = RISCA_ICONS;
  /** Costante per la fix grafica agli elementi ngbDropdownItem della libreria di bootstrap. */
  NGB_DDI_FIX = { 'margin-bottom': '2px' };

  /** UserInfoVO con i dati dell'utente. */
  userInfo: UserInfoVo;
  /** AmbitoVo con le informazioni dell'ambito utente. */
  ambito: AmbitoVo;

  /** Any con il sottotitolo dell'applicazione generato dall'ambito utente. */
  pageInfo: { subTitle?: string };

  /**
   * Costruttore.
   */
  constructor(
    private _httHelper: HttpHelperService,
    private _riscaLockP: RiscaLockPraticaService,
    private _router: Router,
    private _user: UserService
  ) {
    /**
     * NOTA DELLO SVILUPPATORE: ATTENZIONE!
     * Questo componente viene caricato dinamicamente tramite template.
     * Quindi il caricamento delle informazioni avviene solo tramite costruttore.
     * Se bisogna gestire qualche ascoltatore, bisogna usare un BehaviorSubject, in maniera tale che venga comunicato a questo componente l'ultimo valore emesso.
     * Altrimenti non carica niente :D
     */

    // Lancio il setup del componente
    this.setupComponente();
  }

  /**
   * ngOnDestroy.
   */
  ngOnDestroy() {
    // Tento di fare l'unsubscribe degli events
    try {
    } catch (e) {}
  }

  /**
   * ######################
   * FUNZIONALITA' DI SETUP
   * ######################
   */

  /**
   * Funzione per il setup del componente.
   */
  private setupComponente() {
    // Imposto i dati per l'utente
    this.setupUserInfo();
  }

  /**
   * Funzione di setup per le informazioni utente.
   */
  private setupUserInfo() {
    // Recupero i dati dell'user
    this.userInfo = this._user.user;
    // Recupero dall'user l'ambito
    this.ambito = this._user.ambito;
    // Lancio il set del sotto titolo
    this.setSubTitleAmbito();
  }

  /**
   * ############################
   * FUNZIONALITA' DEL COMPONENTE
   * ############################
   */

  /**
   * Funzione che imposta il sottotitolo per la pagina.
   */
  private setSubTitleAmbito() {
    // Verifico l'oggetto
    if (!this.pageInfo) {
      // Lo inizializzo
      this.pageInfo = {};
    }

    // Recupero l'id ambito
    const idA = this.ambito?.id_ambito;
    // Verifico se esiste
    if (idA == undefined) {
      // Id ambito non definito, ritorno stringa vuota
      this.pageInfo.subTitle = '';
    }

    // Esiste l'ambito, recupero la descrizione tramite chiave dalle costanti
    const subTitle = this.H_C.SUB_TITLE_AMBITO[idA] ?? '';
    // Ritorno il sottotitolo
    this.pageInfo.subTitle = subTitle;
  }

  /**
   * Funzione che permette la navigazione verso la home page.
   * Se i dati minimi non sono stati scaricati, non viene eseguita alcuna azione.
   */
  goToHome() {
    // Verifico i dati
    if (!this.userInfo) {
      return;
    }
    // Navigo verso la home page
    this._router.navigateByUrl(AppRoutes.home);
  }

  /**
   * Funzione che richiama il logout applicativo.
   */
  callLogout() {
    // Verifico che non sia un consultatore
    const notCONSULTATORE = !this._user.isCONSULTATORE;

    // Verifico se l'utente ha aperto una riscossione lockata
    if (this.isPraticaLockedByUser && notCONSULTATORE) {
      // La riscossione è effettivamente lockata dall'utente, la sblocco
      this._riscaLockP.sbloccaPratica().subscribe({
        next: (res: IRichiestaLockPraticaRes) => {
          // Richiamo la funzione di logout
          this._user.logOut();
          // #
        },
        error: (e: RiscaServerError) => {
          // Richiamo la funzione di logout
          this._user.logOut();
          // #
        },
      });
      // #
    } else {
      // Richiamo la funzione di logout
      this._user.logOut();
      // #
    }
  }

  /**
   * Funzione per il download del manuale utente.
   * RISCA-780: il download avviene scaricando il file dalla cartella assets/download
   */
  downloadManualeUtente() {
    // Dichiaro il nome del file per il download
    const fileName: string = 'RISCA - Manuale Utente.pdf';
    // Richiamo la funzione di download
    this._httHelper.downloadLocalResource(fileName);
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter che verifica l'esistenza dell'utente nell'app.
   */
  get userExist() {
    // Verifico l'oggetto esista
    return this.userInfo !== undefined;
  }

  get inizialiUser() {
    // Recupero le iniziali
    const iN = this.userInfo?.nome.charAt(0) ?? '';
    const iC = this.userInfo?.cognome.charAt(0) ?? '';
    // Ritorno iniziale nome/cognome
    return `${iN}${iC}`;
  }

  get titleDettaglioUser() {
    // Recupero le informazioni
    const nome = this.userInfo?.nome ?? '';
    const cognome = this.userInfo?.cognome ?? '';
    const ruolo = this.userInfo?.ruolo ? `- ${this.userInfo.ruolo}` : '';
    // Compongo il title
    return `${nome} ${cognome} ${ruolo}`;
  }

  /**
   * Getter di comodo che verifica se la pratica è lockata.
   * @returns boolean con il risultato del check.
   */
  get isPraticaLockedByUser(): boolean {
    // Verifico che esista nel servizio la configurazione del lock pratica
    const existLockStatus = this._riscaLockP.isPraticaInLockStatus();
    // Verifico se un altro utente sta lockando la pratica
    const currentUserLocker = this._riscaLockP.isCurrentUserLockingPratica();
    // Ritorno le condizioni di disabilitazione
    return existLockStatus && currentUserLocker;
  }
}
