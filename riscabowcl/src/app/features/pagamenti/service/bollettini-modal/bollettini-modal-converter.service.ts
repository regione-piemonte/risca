import { Injectable } from '@angular/core';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { ElaborazioneVo } from '../../../../core/commons/vo/elaborazione-vo';
import { ParametroElaborazioneVo } from '../../../../core/commons/vo/parametro-elaborazione-vo';
import { TipoElaborazioneVo } from '../../../../core/commons/vo/tipo-elaborazione-vo';
import { UserService } from '../../../../core/services/user.service';
import { RiscaUtilitiesService } from '../../../../shared/services/risca/risca-utilities/risca-utilities.service';
import { BollettiniModalConsts } from '../../consts/bollettini-modal/bollettini-modal.consts';
import { TipoParametroElaborazione } from '../../enums/pagamenti/pagamenti.enums';
import { IABModalForm } from '../../interfaces/avviso-bonario-modal/avviso-bonario-modal.interfaces';
import { IBGIModalForm } from '../../interfaces/bollettazione-grande-idroelettrico-modal/bollettazione-grande-idroelettrico-modal.interfaces';
import { IBOModalForm } from '../../interfaces/bollettazione-ordinaria-modal/bollettazione-ordinaria-modal.interfaces';
import { IBSModalForm } from '../../interfaces/bollettazione-speciale-modal/bollettazione-speciale-modal.interfaces';
import { IBollettiniModalForm } from '../../interfaces/bollettini/bollettini.interfaces';
import { ISPModalForm } from '../../interfaces/sollecito-pagamento-modal/sollecito-pagamento-modal.interfaces';

/**
 * Servizio di comodo per convertire gli oggetti delle form della bollettazione.
 */
@Injectable({
  providedIn: 'root',
})
export class BollettiniModalConverterService {
  /** Oggetto contenente le costanti per il componente attuale. */
  BM_C = new BollettiniModalConsts();

  /**
   * Costruttore
   */
  constructor(
    private _riscaUtilities: RiscaUtilitiesService,
    private _user: UserService
  ) {}

  /**
   * ################################################################
   * CONVERSIONE OGGETTO IBollettiniModalForm AD OGGETTO Elaborazione
   * ################################################################
   */

  /**
   * Funzione di conversione da un oggetto IBollettiniModalForm a Elaborazione.
   * @param bmf IBollettiniModalForm da convertire.
   * @param tipoElaborazione TipoElaborazione da inserire all'interno dell'oggetto Elaborazione.
   * @param raggruppamento string che definisce il raggruppamento per un parametro di elaborazione.
   * @returns Elaborazione convertito.
   */
  convertIBollettiniModalFormToElaborazione(
    bmf: IBollettiniModalForm,
    tipoElaborazione: TipoElaborazioneVo,
    raggruppamento: string
  ): ElaborazioneVo {
    // Verifico l'input
    if (!bmf) {
      // Niente da convertire
      return undefined;
    }

    // Estraggo le proprietà dall'oggetto
    const { dataProtocollo, dataScadenzaPagamento, numeroProtocollo } = bmf;

    // Creo l'oggetto del target
    const elaborazione = new ElaborazioneVo();
    // Definisco l'ambito
    elaborazione.ambito = this._user.ambito;
    // Definisco il tipo elaborazione
    elaborazione.tipo_elabora = tipoElaborazione;

    // Variabile di comodo per la gestione dei parametri elaborazione
    const r = raggruppamento;
    let v: any;
    let k: any;

    // Gestisco la data protocollo
    v = dataProtocollo;
    k = TipoParametroElaborazione.DT_PROTOCOLLO;
    const dtP = this.convertNbgDateStructToParametroElaborazioneVo(k, v, r);
    elaborazione.parametri.push(dtP);

    // Gestisco la data scadenza pagamento
    v = dataScadenzaPagamento;
    k = TipoParametroElaborazione.DT_SCAD_PAG;
    const dsP = this.convertNbgDateStructToParametroElaborazioneVo(k, v, r);
    elaborazione.parametri.push(dsP);

    // Gestisco il numero protocollo
    let valore = numeroProtocollo;
    let chiave = TipoParametroElaborazione.NUM_PROTOCOLLO;
    const numP = this.createParametroElaborazioneVo(chiave, valore, r);
    elaborazione.parametri.push(numP);

    // Aggiungo agli oggetti le informazioni per i metadata
    return elaborazione;
  }

  /**
   * ########################################################
   * CONVERSIONE OGGETTO IBOModalForm AD OGGETTO Elaborazione
   * ########################################################
   */

  /**
   * Funzione di conversione da un oggetto IBOModalForm a Elaborazione.
   * La funzione è dedicata ad: BOLLETTAZIONE ORDINARIA.
   * @param bomf IBOModalForm da convertire.
   * @param tipoElaborazione TipoElaborazione da inserire all'interno dell'oggetto Elaborazione.
   * @param raggruppamento string che definisce il raggruppamento per un parametro di elaborazione.
   * @returns Elaborazione convertito.
   */
  convertIBOModalFormToElaborazione(
    bomf: IBOModalForm,
    tipoElaborazione: TipoElaborazioneVo,
    raggruppamento: string
  ): ElaborazioneVo {
    // Verifico l'input
    if (!bomf) {
      // Niente da convertire
      return undefined;
    }
    // Variabile di comodo
    const te = tipoElaborazione;
    const r = raggruppamento;
    // Converto le informazioni comuni
    const e = this.convertIBollettiniModalFormToElaborazione(bomf, te, r);
    // Gestisco l'anno
    let valore = bomf.anno?.anno?.toString();
    let chiave = TipoParametroElaborazione.ANNO;
    const numP = this.createParametroElaborazioneVo(chiave, valore, r);
    e.parametri.push(numP);

    // Ritorno l'oggetto convertito
    return e;
  }

  /**
   * ########################################################
   * CONVERSIONE OGGETTO IBSModalForm AD OGGETTO Elaborazione
   * ########################################################
   */

  /**
   * Funzione di conversione da un oggetto IBSModalForm a Elaborazione.
   * La funzione è dedicata ad: BOLLETTAZIONE SPECIALE.
   * @param bsmf IBSModalForm da convertire.
   * @param tipoElaborazione TipoElaborazione da inserire all'interno dell'oggetto Elaborazione.
   * @param raggruppamento string che definisce il raggruppamento per un parametro di elaborazione.
   * @returns Elaborazione convertito.
   */
  convertIBSModalFormToElaborazione(
    bsmf: IBSModalForm,
    tipoElaborazione: TipoElaborazioneVo,
    raggruppamento: string
  ): ElaborazioneVo {
    // Verifico l'input
    if (!bsmf) {
      // Niente da convertire
      return undefined;
    }
    // Variabile di comodo
    const te = tipoElaborazione;
    const r = raggruppamento;
    // Converto le informazioni comuni
    const e = this.convertIBollettiniModalFormToElaborazione(bsmf, te, r);

    // Ritorno l'oggetto convertito
    return e;
  }

  /**
   * ########################################################
   * CONVERSIONE OGGETTO IABModalForm AD OGGETTO Elaborazione
   * ########################################################
   */

  /**
   * Funzione di conversione da un oggetto IABModalForm a Elaborazione.
   * La funzione è dedicata ad: AVVISO BONARIO.
   * @param abmf IABModalForm da convertire.
   * @param tipoElaborazione TipoElaborazione da inserire all'interno dell'oggetto Elaborazione.
   * @param raggruppamento string che definisce il raggruppamento per un parametro di elaborazione.
   * @returns Elaborazione convertito.
   */
  convertIABModalFormToElaborazione(
    abmf: IABModalForm,
    tipoElaborazione: TipoElaborazioneVo,
    raggruppamento: string
  ): ElaborazioneVo {
    // Verifico l'input
    if (!abmf) {
      // Niente da convertire
      return undefined;
    }
    // Variabile di comodo
    const te = tipoElaborazione;
    const r = raggruppamento;
    // Converto le informazioni comuni
    const e = this.convertIBollettiniModalFormToElaborazione(abmf, te, r);

    // Ritorno l'oggetto convertito
    return e;
  }

  /**
   * ########################################################
   * CONVERSIONE OGGETTO IABModalForm AD OGGETTO Elaborazione
   * ########################################################
   */

  /**
   * Funzione di conversione da un oggetto IABModalForm a Elaborazione.
   * La funzione è dedicata ad: SOLLECITO PAGAMENTO.
   * @param spmf ISPModalForm da convertire.
   * @param tipoElaborazione TipoElaborazione da inserire all'interno dell'oggetto Elaborazione.
   * @param raggruppamento string che definisce il raggruppamento per un parametro di elaborazione.
   * @returns Elaborazione convertito.
   */
  convertISPModalFormToElaborazione(
    spmf: ISPModalForm,
    tipoElaborazione: TipoElaborazioneVo,
    raggruppamento: string
  ): ElaborazioneVo {
    // Verifico l'input
    if (!spmf) {
      // Niente da convertire
      return undefined;
    }
    // Variabile di comodo
    const te = tipoElaborazione;
    const r = raggruppamento;
    // Converto le informazioni comuni
    const e = this.convertIBollettiniModalFormToElaborazione(spmf, te, r);

    // Ritorno l'oggetto convertito
    return e;
  }

  /**
   * #########################################################
   * CONVERSIONE OGGETTO IBGIModalForm AD OGGETTO Elaborazione
   * #########################################################
   */

  /**
   * Funzione di conversione da un oggetto IBGIModalForm a Elaborazione.
   * La funzione è dedicata ad: BOLLETTAZIONE GRANDE IDROELETTRICO.
   * @param bgimf IBGIModalForm da convertire.
   * @param tipoElaborazione TipoElaborazione da inserire all'interno dell'oggetto Elaborazione.
   * @param raggruppamento string che definisce il raggruppamento per un parametro di elaborazione.
   * @returns Elaborazione convertito.
   */
  convertIBGIModalFormToElaborazione(
    bgimf: IBGIModalForm,
    tipoElaborazione: TipoElaborazioneVo,
    raggruppamento: string
  ): ElaborazioneVo {
    // Verifico l'input
    if (!bgimf) {
      // Niente da convertire
      return undefined;
    }
    // Variabile di comodo
    const te = tipoElaborazione;
    const r = raggruppamento;
    // Converto le informazioni comuni
    const e = this.convertIBollettiniModalFormToElaborazione(bgimf, te, r);

    // Ritorno l'oggetto convertito
    return e;
  }

  /**
   * ######################
   * CONVERSIONI DI UTILITY
   * ######################
   */

  /**
   * Funzione di comodo che crea un oggetto ParametroElaborazioneVo, con chiave e valore.
   * @param chiave string che definisce la chiave per creare l'oggetto target.
   * @param valore string che definisce il valore d'assegnare all'oggetto.
   * @param raggruppamento string che definisce il raggruppamento per un parametro di elaborazione.
   * @returns ParametroElaborazioneVo convertito.
   */
  createParametroElaborazioneVo(
    chiave: string,
    valore: string,
    raggruppamento: string
  ): ParametroElaborazioneVo {
    // Verifico l'input
    if (!valore || !chiave || !raggruppamento) {
      // Ritorno undefined
      return undefined;
    }

    // Creo e ritorno l'oggetto target
    return new ParametroElaborazioneVo({ chiave, valore, raggruppamento });
  }

  /**
   * Funzione di comodo che converte una data NgbDateStruct in un oggetto ParametroElaborazioneVo.
   * @param data NgbDateStruct da convertire.
   * @param chiave string che definisce la chiave per creare l'oggetto target.
   * @param raggruppamento string che definisce il raggruppamento per un parametro di elaborazione.
   * @returns ParametroElaborazioneVo convertito.
   */
  convertNbgDateStructToParametroElaborazioneVo(
    chiave: string,
    data: NgbDateStruct,
    raggruppamento: string
  ): ParametroElaborazioneVo {
    // Verifico l'input
    if (!data || !chiave) {
      // Ritorno undefined
      return undefined;
    }
    // Variabili di supporto
    const r = raggruppamento;
    // Converto la data in una data server stringa
    const valore = this._riscaUtilities.convertNgbDateStructToServerDate(data);

    // Creo e ritorno l'oggetto target
    return this.createParametroElaborazioneVo(chiave, valore, r);
  }

  /**
   * ################
   * GETTER DI COMODO
   * ################
   */

  /**
   * Getter di comodo per il codice fiscale dell'utente loggato.
   */
  get userCf() {
    return this._user.cf;
  }
}
