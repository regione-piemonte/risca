import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ElaborazioneVo } from '../../../../core/commons/vo/elaborazione-vo';
import { UserService } from '../../../../core/services/user.service';
import { RiscaAlertService } from '../../../../shared/services/risca/risca-alert.service';
import { RiscaFormBuilderService } from '../../../../shared/services/risca/risca-form-builder/risca-form-builder.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { IABModalForm } from '../../interfaces/avviso-bonario-modal/avviso-bonario-modal.interfaces';
import { IBollettiniModalForm } from '../../interfaces/bollettini/bollettini.interfaces';
import { BollettiniModalConverterService } from '../../service/bollettini-modal/bollettini-modal-converter.service';
import { BollettiniModalService } from '../../service/bollettini-modal/bollettini-modal.service';
import { BollettiniService } from '../../service/bollettini/bollettini.service';
import { BollettiniModalComponent } from '../bollettini-modal/bollettini-modal.component';
import { IBollettiniModalDataConfigs } from '../bollettini-modal/utilities/bollettini-modal.interfaces';
import { dataProtocolloEDataScadenzaPagamanetoValidator } from '../bollettini-modal/utilities/form-validators.bollettini-modal';
import { RiscaMessagesService } from './../../../../shared/services/risca/risca-messages.service';

@Component({
  selector: 'avviso-bonario-modal',
  templateUrl: './avviso-bonario-modal.component.html',
  styleUrls: ['./avviso-bonario-modal.component.scss'],
})
export class AvvisoBonarioModalComponent
  extends BollettiniModalComponent<IBollettiniModalForm>
  implements OnInit
{
  /**
   * Costruttore.
   */
  constructor(
    activeModal: NgbActiveModal,
    bmConverter: BollettiniModalConverterService,
    private _bollettini: BollettiniService,
    bollettiniModal: BollettiniModalService,
    formBuilder: FormBuilder,
    riscaAlert: RiscaAlertService,
    riscaFormBuilder: RiscaFormBuilderService,
    riscaMessages: RiscaMessagesService,
    riscaUtilities: RiscaUtilitiesService,
    user: UserService
  ) {
    super(
      activeModal,
      bmConverter,
      bollettiniModal,
      formBuilder,
      riscaAlert,
      riscaFormBuilder,
      riscaMessages,
      riscaUtilities,
      user
    );
  }

  /**
   * ################
   * FUNZIONI DI INIT
   * ################
   */

  /**
   * Funzione che prevede l'inizializzazione della variabile mainForm per la gestione del form d'inserimento.
   * @override
   */
  protected initMainForm() {
    // Recupero la configurazione per la disabilitazione dei campi in base alla modalita
    const dE = this.getDisableEmissione();
    const dC = this.getDisableConferma();
    // Recupero la configurazione dei validatori in base alla modalita
    const vE = this.getValidatorsEmissione();
    const vC = this.getValidatorsConferma();

    // Inizializzo la form
    this.mainForm = this._formBuilder.group(
      {
        tipoElaborazione: new FormControl(
          { value: null, disabled: true },
          { validators: [] }
        ),
        dataScadenzaPagamento: new FormControl(
          { value: null, disabled: dE },
          { validators: [...vE] }
        ),
        dataProtocollo: new FormControl(
          { value: null, disabled: dE },
          { validators: [...vE] }
        ),
        numeroProtocollo: new FormControl(
          { value: null, disabled: dE },
          { validators: [...vE] }
        ),
        dirigenteProTempore: new FormControl(
          { value: null, disabled: dC },
          { validators: [...vC] }
        ),
      },
      // Validatori campi incrociati
      {
        validators: [
          dataProtocolloEDataScadenzaPagamanetoValidator(
            this.BM_C.DATA_PROTOCOLLO,
            this.BM_C.DATA_SCADENZA_PAGAMENTO
          ),
        ],
      }
    );
  }

  /**
   * Funzione che prevede l'inizializzazione dei dati specifici di una tipologia di prenotazione per la conferma.
   * @param dataModal IBollettiniModalConfigs con i dati di configurazione della modale.
   * @override
   */
  protected initMFDConferma(dataModal: IBollettiniModalDataConfigs) {
    // Variabili di comodo
    const f = this.mainForm;
    // Recupero le chiavi dei campi
    const dtPK = this.BM_C.DATA_PROTOCOLLO;
    const dtSPK = this.BM_C.DATA_SCADENZA_PAGAMENTO;
    const numPK = this.BM_C.NUMERO_PROTOCOLLO;

    // Recupero le informazioni per il set dati per la modalità conferma
    const p = dataModal?.elaborazione?.parametri ?? [];
    // Recupero i dati dai parametri
    const dtP = this._bollettini.getPEBDataProtocollo(p)?.valore;
    const dtSP = this._bollettini.getPEBDataScadenzaPagamento(p)?.valore;
    const numP = this._bollettini.getPEBNumeroProtocollo(p)?.valore;

    // Converto le date da date server a ngb date
    const ngbDtP = this._riscaUtilities.convertServerDateToNgbDateStruct(dtP);
    const ngbDtSP = this._riscaUtilities.convertServerDateToNgbDateStruct(dtSP);

    // Verifico e inserisco i dati nella form
    this.setMainFormFieldData(f, dtPK, ngbDtP);
    this.setMainFormFieldData(f, dtSPK, ngbDtSP);
    this.setMainFormFieldData(f, numPK, numP);
  }

  /**
   * ######################################
   * FUNZIONI PER IL RISULTATO DELLA MODALE
   * ######################################
   */

  /**
   * Funzione di supporto che definisce le logiche di generazione dell'oggetto Elaborazione da restituire al componente chiamante.
   * Le logiche sono specifiche per la modalità: emissione.
   * @param formData IABModalForm con i dati generati dalla form.
   * @returns Elaborazione con le informazioni generate dai dati della form.
   * @override
   */
  protected generateElaborazioneEmissione(
    formData: IABModalForm
  ): ElaborazioneVo {
    // Recupero i dati per compilare l'oggetto elaborazione
    const te = this.tipoElaborazione;
    const r = this.raggruppamento;
    // Effettuo un parse locale per il parametro
    const fd = formData;
    // Definisco l'oggetto da restituire come elaborazione
    let e: ElaborazioneVo;

    // Converto il dato della form in un oggetto Elaborazione
    e = this._bmConverter.convertIABModalFormToElaborazione(fd, te, r);
    // Aggiungo il dato per stato_elabora
    this.addEmissioneRichiestaToElaborazione(e);

    // Ritorno l'oggetto generato
    return e;
  }
}
