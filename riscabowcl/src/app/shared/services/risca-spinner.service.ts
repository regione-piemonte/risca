import { Injectable } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { timer } from 'rxjs';
import { Subscription } from 'rxjs/index';
import { CommonConsts } from '../consts/common-consts.consts';

export class TimerSubscription {
  nome: string;
  sub: Subscription;
  spinnerCounter: number;

  constructor(
    nome: string,
    sub: Subscription = undefined,
    counter: number = 0
  ) {
    this.nome = nome;
    this.sub = sub;
    this.spinnerCounter = counter;
  }
}

@Injectable({
  providedIn: 'root',
})
export class RiscaSpinnerService {
  /** Oggetto contenente i valori costanti dell'applicazione. */
  C_C = new CommonConsts()
  /** Lista di sottoscrizioni all'evento del timer */
  private timerSubscription: TimerSubscription[] = [];

  /** Millisedondi per cui si nasconde il timer dopo la hide */
  private TIMER_HIDE: number = 500;
  /** Millisecondi per cui si nasconde il timer per timeout */
  private TIMER_AUTO_HIDE: number = 300000; // 5 minuti

  constructor(private _spinner: NgxSpinnerService) {}

  /**
   * Mostra lo spinner sulla pagina se non c'è.
   * @param name nome dello spinner
   * @param spinner tipo di spinner
   */
  show(name: string = this.C_C.SPINNER): void {
    // Avvio lo spinner
    this.addAndCheckSpinner(name, 1);
  }

  /**
   * Mostra manualemnte uno spinner
   */
  manualShow(name: string = this.C_C.SPINNER_MANUALE) {
    // Aggiungo lo spinner
    this._spinner.show(name);
  }

  /**
   * Nasconde lo spinner sulla pagina.
   * @param name nome dello spinner
   * @param debounce tipo di spinner
   */
  hide(name: string = this.C_C.SPINNER): void {
    // nascondo dopo un tempo di default
    this.hideAfterTime(this.TIMER_HIDE, name);
  }

  /**
   * Nasconde manualmente uno spinner
   */
  manualHide(name: string = this.C_C.SPINNER_MANUALE): void {
    // Tolgo lo spinner
    const sp = this.getSpinner(name);
    // Controllo l'esistenza
    if (sp) {
      // Azzero il contatore
      sp.spinnerCounter = 0;
      // Elimino la sottoscrizione
      sp.sub?.unsubscribe();
      // Cerco l'indice del timer
      const index = this.timerSubscription.findIndex(x => x.nome == name);
      // Elimino il timer
      this.timerSubscription.splice(index, 1);
    }
    // Nascondo lo spinner
    this._spinner.hide(name);
  }

  /**
   * Nasconde lo spinner sulla pagina dopo un certo tempo.
   * @param tempo
   * @param name
   * @param debounce
   */
  hideAfterTime(tempo?: number, name: string = this.C_C.SPINNER): void {
    // Tempo di default
    if (tempo == undefined) {
      tempo = 0;
    }
    // Prendo il timer
    const sp = this.getSpinner(name) ?? new TimerSubscription(name, null, 1);
    // Lancio il fimer e mi sottoscrivo alla sua scadenza per nascondere lo spinner
    sp.sub = timer(tempo).subscribe(() => {
      this.addAndCheckSpinner(name, -1);
    });
    // Aggiorno lo spinner
    this.insertUpdateSpinner(sp);
  }

  /**
   * Nasconde ogni spinner sulla pagina.
   * @param debounce tipo di spinner
   */
  hideAll(): void {
    // prendo tutti i nomi dei timer
    let names = this.timerSubscription.map((s) => s.nome);
    // Ciclo i nomi
    names.forEach((n) => {
      // Cancello i timer
      this.deleteSpinner(n);
    });
  }

  /**
   * Nascone lo spinner sulla pagina.
   * @param name nome dello spinner
   */
  hideAllGeneric(): void {
    // prendo tutti i nomi dei timer generici
    const names = this.timerSubscription
      .filter((s) => s.nome == this.C_C.SPINNER)
      .map((s) => s.nome);
    // Ciclo i nomi
    names.forEach((n) => {
      // Cancello i timer
      this.deleteSpinner(n);
    });
  }

  /**
   * Segna che lo spinner è stato chiamato e se il contatore è inferiore a 0 lo porta a 0
   * @param name nome dello spinner da gestire
   * @param spinnerAdded +1 se aggiunge lo spinner, -1 se lo nasconde
   */
  private addAndCheckSpinner(name: string, spinnerAdded: number): void {
    // Cerco lo spinner
    const sp = this.getSpinner(name) ?? {
      nome: name,
      spinnerCounter: 0,
      sub: null,
    };
    // Incremento il counter
    sp.spinnerCounter += spinnerAdded;
    // Se è stato aggiunto uno spinner, devo incrementare il timeout.
    if (spinnerAdded > 0) {
      // Aggiorno lo spinner
      this.insertUpdateSpinner(sp);
    }
    // Se il contatore è 0 o negativo, significa che devo nascondere lo spinner
    if (sp.spinnerCounter <= 0) {
      // Rimuovo lo spinner
      this.deleteSpinner(sp.nome);
    }
  }

  /**
   * Elimina la sottocrizione ad uno spinner e lo nasconde, resettando il timer dopo il quale si autonasconde.
   * @param nome nome del timer da gestire
   * @param resetTimer true se devo far ricominciare il timer per l'auto-hide.
   */
  private setSafetyTimer(nome: string) {
    // Cerco il timer o lo creo
    const spinner = this.getSpinner(nome);
    // Se non esiste lo spinner, ritorno
    if (!spinner) {
      return;
    }
    // Se c'è una sottoscrizione la elimino
    spinner?.sub?.unsubscribe();
    // Riavvio per sicurezza lo spinner
    this._spinner.show(spinner.nome);
    // Creo la sottoscrizione al timer di sicurezza per disabilitarlo
    spinner.sub = timer(this.TIMER_AUTO_HIDE).subscribe(() => {
      this.hideAll();
    });
  }

  /**
   * Cerca una configurazione di uno spinner dal nome.
   * @param name nome del timer.
   * @returns la sottoscrizione allo spinner.
   */
  private getSpinner(name: string): TimerSubscription {
    // Ritorno lo spinner
    return this.timerSubscription.find((t) => t.nome == name);
  }

  /**
   * Inserisce o aggiorna uno spinner timer
   * @param spinner lo spinner timer da aggiornare
   */
  private insertUpdateSpinner(spinner: TimerSubscription) {
    // Cerco lo spinner timer
    let sp = this.getSpinner(spinner.nome);
    // Controllo se c'è
    if (sp) {
      // Se esiste, lo aggiorno
      sp.spinnerCounter = spinner.spinnerCounter;
      sp.sub = spinner.sub;
    } else {
      // Avvio lo spinner
      this._spinner.show(spinner.nome);
      // Lo inserisco
      this.timerSubscription.push(spinner);
      if (!spinner.sub) {
        // Aggiungo/resetto il timer di sicurezza che uccide lo spinner dopo un certo tempo.
        this.setSafetyTimer(spinner.nome);
      }
    }
  }

  /**
   * Elimina uno spinner e lo nasconde
   * @param name nome dello spinner da eliminare
   */
  private deleteSpinner(name: string) {
    // Prendo lo spinner
    const sp = this.getSpinner(name);
    // Se esiste
    if (sp) {
      // Azzero il contatore
      sp.spinnerCounter = 0;
      // tolgo la sottoscrizione
      sp.sub?.unsubscribe();
      // nascondo lo spinner
      this._spinner.hide(name);
      // Cert0 il timer dalla lista
      const index = this.timerSubscription.findIndex((x) => x.nome == name);
      // Se esiste lo rimuovo
      if (index >= 0) {
        this.timerSubscription.splice(index, 1);
      }
    }
  }
}
