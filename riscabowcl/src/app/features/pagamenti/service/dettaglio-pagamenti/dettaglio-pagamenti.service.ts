import { Injectable } from '@angular/core';
import { clone } from 'lodash';
import { DettaglioPagSearchResultVo } from '../../../../core/commons/vo/dettaglio-pag-search-result-vo';
import { PagamentoVo } from '../../../../core/commons/vo/pagamento-vo';
import { FALivello } from '../../../../shared/components/risca/risca-filo-arianna/utilities/rfa-level.class';
import { AppClaimants } from '../../../../shared/utilities';

@Injectable({ providedIn: 'root' })
export class DettaglioPagamentiService {
  /** PagamentoVo contenente le informazioni da far utilizzare al componente dettaglio-pagamento.component.ts per caricare le informazioni della pagina. */
  private _pagamento: PagamentoVo;
  /** DettaglioPagSearchResultVo[] contenente le informazioni da far utilizzare al componente dettaglio-pagamento.component.ts per caricare le informazioni della pagina. */
  private _dettagliPagamento?: DettaglioPagSearchResultVo[];
  /** FALivello[] con la lista dei livelli da impostare all'interno del filo d'arianna. */
  private _faLivelli: FALivello[] = [];
  /** boolean che indica il comportamento di gestione del componente per alcuni flussi logici. */
  private _standAlone: boolean = true;
  /** AppClaimants con il chiamante definito all'interno del servizio. */
  private _appClaimant: AppClaimants;

  /** boolean che tiene traccia dello stato di salvataggio del pagamento. */
  private _pagamentoSalvato: boolean = false;

  /**
   * ###################
   * RESET DATI SERVIZIO
   * ###################
   */

  /**
   * Funzione che resetta i dati del servizio.
   */
  resetDati() {
    // Richiamo i singoli reset dati
    this.resetPagamento();
    this.resetDettagliPagamento();
    this.resetFALivelli();
    this.resetStandAlone();
    this.resetAppClaimant();
  }

  /**
   * Funzione di comodo che rimuove le informazioni.
   */
  resetPagamento() {
    // Resetto le informazioni
    this._pagamento = undefined;
  }

  /**
   * Funzione di comodo che rimuove le informazioni.
   */
  resetDettagliPagamento() {
    // Resetto le informazioni
    this._dettagliPagamento = undefined;
  }

  /**
   * Funzione di comodo che rimuove le informazioni.
   */
  resetFALivelli() {
    // Resetto le informazioni
    this._faLivelli = [];
  }

  /**
   * Funzione di comodo che rimuove le informazioni.
   */
  resetStandAlone() {
    // Resetto le informazioni
    this._standAlone = true;
  }

  /**
   * Funzione di comodo che rimuove le informazioni.
   */
  resetAppClaimant() {
    // Resetto le informazioni
    this._appClaimant = undefined;
  }

  /**
   * Funzione di comodo che rimuove le informazioni.
   */
  resetPagamentoSalvato() {
    // Resetto le informazioni
    this._pagamentoSalvato = false;
  }

  /**
   * #############################
   * FUNZIONI PER I DATI PAGAMENTO
   * #############################
   */

  /**
   * Get che ritorna le informazioni per il pagamento.
   * @returns PagamentoVo presente nel servizio.
   */
  get pagamento(): PagamentoVo {
    // Recupero le informazioni
    return clone(this._pagamento);
  }

  /**
   * Set che assegna le informazioni per il pagamento.
   * @param pagamento PagamentoVo d'assegnare al componente.
   */
  set pagamento(pagamento: PagamentoVo) {
    // Setto le informazioni
    this._pagamento = clone(pagamento);
  }

  /**
   * Get che ritorna le informazioni per i dettagli pagamento.
   * @returns DettaglioPagSearchResultVo[] presente nel servizio.
   */
  get dettagliPagamento(): DettaglioPagSearchResultVo[] {
    // Recupero le informazioni
    return clone(this._dettagliPagamento) ?? [];
  }

  /**
   * Set che assegna le informazioni per i dettagli pagamento.
   * @param dettagliPagamento DettaglioPagSearchResultVo[] d'assegnare al componente.
   */
  set dettagliPagamento(dettagliPagamento: DettaglioPagSearchResultVo[]) {
    // Setto le informazioni
    this._dettagliPagamento = clone(dettagliPagamento);
  }

  /**
   * Get che ritorna le informazioni per il filo d'arianna.
   * @returns FALivello[] presente nel servizio.
   */
  get faLivelli(): FALivello[] {
    // Recupero le informazioni
    return clone(this._faLivelli) ?? [];
  }

  /**
   * Set che assegna le informazioni per il filo d'arianna.
   * @param faLivelli FALivello[] d'assegnare al componente.
   */
  set faLivelli(faLivelli: FALivello[]) {
    // Setto le informazioni
    this._faLivelli = clone(faLivelli);
  }

  /**
   * Get che ritorna le informazioni per la gestione del componente.
   * @returns boolean presente nel servizio.
   */
  get standAlone(): boolean {
    // Recupero le informazioni
    return this._standAlone;
  }

  /**
   * Set che assegna le informazioni per la gestione del componente.
   * @param standAlone boolean d'assegnare al componente.
   */
  set standAlone(standAlone: boolean) {
    // Setto le informazioni
    this._standAlone = standAlone;
  }

  /**
   * Get che ritorna le informazioni per la gestione del componente.
   * @returns AppClaimants presente nel servizio.
   */
  get appClaimant(): AppClaimants {
    // Recupero le informazioni
    return this._appClaimant;
  }

  /**
   * Set che assegna le informazioni per la gestione del componente.
   * @param appClaimant AppClaimants d'assegnare al componente.
   */
  set appClaimant(appClaimant: AppClaimants) {
    // Setto le informazioni
    this._appClaimant = appClaimant;
  }

  /**
   * Get che ritorna le informazioni per la gestione del componente.
   * @returns boolean presente nel servizio.
   */
  get pagamentoSalvato(): boolean {
    // Recupero le informazioni
    return this._pagamentoSalvato;
  }

  /**
   * Set che assegna le informazioni per la gestione del componente.
   * @param pagamentoSalvato boolean d'assegnare al componente.
   */
  set pagamentoSalvato(pagamentoSalvato: boolean) {
    // Setto le informazioni
    this._pagamentoSalvato = pagamentoSalvato;
  }
}
