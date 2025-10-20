import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoggerService } from '../../../../core/services/logger.service';
import { NavigationHelperService } from '../../../../core/services/navigation-helper/navigation-helper.service';
import { UserService } from '../../../../core/services/user.service';
import { RiscaFormChildComponent } from '../../../../shared/components/risca/risca-form-child/risca-form-child.component';
import { CommonConsts } from '../../../../shared/consts/common-consts.consts';
import { RiscaFormBuilderService } from '../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaFormSubmitHandlerService } from '../../../../shared/services/risca/risca-form-submit-handler.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import {
  IRiscaAnnoSelect,
  RiscaServerError,
} from '../../../../shared/utilities';
import { CanoniFormService } from '../../services/canoni-form/canoni-form.service';
import { CanoniFormConsts } from './utilities/canoni-form.consts';
import { CanoniFormFieldsConfig } from './utilities/canoni-form.fields-configs';
import { CanoniFormFormConfigs } from './utilities/canoni-form.form-configs';
import { ICanoniFormData } from './utilities/canoni-form.interfaces';

@Component({
  selector: 'canoni-form',
  templateUrl: './canoni-form.component.html',
  styleUrls: ['./canoni-form.component.scss'],
})
export class CanoniFormComponent
  extends RiscaFormChildComponent<ICanoniFormData>
  implements OnInit, OnDestroy
{
  /** Oggetto contenente le costanti comuni all'applicazione. */
  C_C = new CommonConsts();
  /** Oggetto con le costanti del componente di riferimento. */
  CF_C = new CanoniFormConsts();

  /** CanoniFormFieldsConfig come classe che definisce la struttura dei campi di ricerca del form. */
  formInputs: CanoniFormFieldsConfig;
  /** CanoniFormFormConfigs come classe che definisce la struttura di configurazione del form. */
  formConfigs: CanoniFormFormConfigs;

  /** IRiscaAnnoSelect[] con la lista degli anni disponibili per la selezione. */
  listaCanoniUfficiali: IRiscaAnnoSelect[] = [];

  /**
   * Costruttore.
   */
  constructor(
    private _canoniForm: CanoniFormService,
    logger: LoggerService,
    private _formBuilder: FormBuilder,
    navigationHelper: NavigationHelperService,
    private _riscaFormBuilder: RiscaFormBuilderService,
    riscaFormSubmitHandler: RiscaFormSubmitHandlerService,
    riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {
    super(logger, navigationHelper, riscaFormSubmitHandler, riscaUtilities);

    // Lancio il setup del componente
    this.setupComponente();
  }

  ngOnInit() {
    // Lancio l'init del componente
    this.initComponente();
  }

  ngOnDestroy() {
    // Richiamo il destroy del super
    super.ngOnDestroy();
  }

  /**
   * ################################
   * FUNZIONI DI SETUP DEL COMPONENTE
   * ################################
   */

  /**
   * Funzione di setup per il componente.
   */
  private setupComponente() {
    // Lancio la funzione di gestione per gli errori del form
    this.setupMainFormErrors();
    // Lancio la funzione di setup delle input del form
    this.setupFormInputs();
    // Lancio la funzione di setup per la struttura del form
    this.setupFormConfigs();
    // Lancio la funzione di set per le liste del componente
    this.setupLists();
  }

  /**
   * Funzione di setup per gli errori da verificare al momento della submit della form principale.
   */
  private setupMainFormErrors() {
    // Imposto la lista di errori
    this.formErrors = [...this.EM.MAP_FORM_GROUP_REQUIRED];
  }

  /**
   * Funzione di setup per la configurazione dei dati per la costruzione delle input del form.
   */
  private setupFormInputs() {
    // Recupero il servizio di configurazione
    const s = this._riscaFormBuilder;
    // Classe di supporto che definisce la configurazioni per le input
    this.formInputs = new CanoniFormFieldsConfig(s);
  }

  /**
   * Funzione di setup per la configurazione strutturale del form group.
   */
  private setupFormConfigs() {
    // Recupero il servizio di configurazione
    const s = this._formBuilder;
    // Istanzio l'oggetto di configurazione
    this.formConfigs = new CanoniFormFormConfigs(s);
    // Tramite la classe di configurazione, definisco la struttura per il main form
    this.mainForm = this.formConfigs.instantiateForm();
  }

  /**
   * Funzione di setup per lo scarico dei dati delle liste del componente.
   */
  private setupLists() {
    // Richiamo la funzione di scarico degli anni per id ambito
    this.aggiornaListaCanoniUfficiali().subscribe({
      error: (e: RiscaServerError) => {
        // Lancio la gestione dell'errore
        this.onEmitServiziError$.emit(e);
      },
    });
  }

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  /**
   * Funzione di init per le informazioni del componente.
   */
  private initComponente() {
    // Non ci sono logiche per il momento
  }


  /**
   * #######################
   * FUNZIONI DEL COMPONENTE
   * #######################
   */

  /**
   * Funzione che richiede i dati riferiti alla lista dei canoni ufficiali.
   * La funzione prevede l'aggiornamento automatico della lista omonima.
   * @returns Observable<IRiscaAnnoSelect[]> con le informazioni scaricate.
   */
  aggiornaListaCanoniUfficiali(): Observable<IRiscaAnnoSelect[]> {
    // Recupero l'id ambito dell'utente
    const idAmbito: number = this._user.idAmbito;

    // Richiamo la funzione di scarico degli anni per id ambito
    return this._canoniForm.getAnniUsiRegole(idAmbito).pipe(
      tap((anni: IRiscaAnnoSelect[]) => {
        // Definisco la lista di anni per la select
        this.listaCanoniUfficiali = anni;
        // #
      })
    );
  }

  /**
   * Funzione che richiede i dati riferiti alla lista dei canoni ufficiali.
   * La funzione prevede l'aggiornamento automatico della lista omonima ed effettua un submit dei dati con i dati di default.
   * @returns Observable<IRiscaAnnoSelect[]> con le informazioni scaricate.
   */
  aggiornaListaCanoniUfficialiSubmit() {
    // Richiamo la funzione di scarico degli anni per id ambito
    this.aggiornaListaCanoniUfficiali().subscribe({
      next: (listaCanoniUfficiali: IRiscaAnnoSelect[]) => {
        // Imposto un timeout, per poter permettere alla funzione di aggiornare i dati nella form
        setTimeout(() => {
          // Lancio il submit della form
          this.onFormSubmit();
          // #
        }, 100);
        // #
      },
      error: (e: RiscaServerError) => {
        // Lancio la gestione dell'errore
        this.onEmitServiziError$.emit(e);
      },
    });
  }

  /**
   * ###############
   * GETTER E SETTER
   * ###############
   */

  /**
   * Getter per l'id dell'ambito.
   * @returns number con l'id.
   */
  get idAmbito(): number {
    // Ritorno l'id ambito
    return this._user.idAmbito;
  }
}
