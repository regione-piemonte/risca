import { Injectable } from '@angular/core';
import { Moment } from 'moment';
import { Observable, of } from 'rxjs';
import { AnnualitaConsts } from '../../consts/dati-contabili/annualita.consts';

@Injectable({ providedIn: 'root' })
export class AnnualitaService {
  /** Costante con le informazioni della sezione. */
  private A_C = AnnualitaConsts;

  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione di calcolo delle mensilità da pagare dell'anno selezionato.
   * @param dataInizio data da cui va fatto il conteggio dei mesi da pagare
   * @returns number mensilità da pagare
   */
  public mensilitaDaPagare(dataInizio: Moment): Observable<number> {
    // Prendo i valori della data per i controlli
    const day = dataInizio.date();
    const month = dataInizio.month(); // Gennaio=0, ma aggiungo il +1 per riallineare il mese durante il calcolo delle mensilità.
    // Prendo i giorni del mese
    const giorniDelMese = dataInizio.daysInMonth();
    // Prendo le mensilità oltre la prima fino alla fine dell'anno.
    let mensilita = 12 - month; // Conta anche il primo mese. Semplifico: 12 - (month - 1 + 1(gennaio=0)) = 12 - month
    // Controllo se il primo mese va conteggiato
    // * Giorni del mese	  Giorno data inizio
    // * Mese di 28 giorni	Dall’1 fino als 14 compreso
    // * Mese di 29 giorni	Dall’1 fino al 15 compreso
    // * Mese di 30 giorni	Dall’1 fino al 16 compreso
    // * Mese di 31 giorni	Dall’1 fino al 17 compreso
    const contaMese = day <= giorniDelMese - 14; // Semplifico: l'intervallo è sempre a 14 giorni prima della fine del mese.
    // Calcolo la mensilità
    if (!contaMese) {
      // Se non conto anche il primo mese, lo tolgo
      mensilita--;
    }
    // return
    return of(mensilita);
  }
}
