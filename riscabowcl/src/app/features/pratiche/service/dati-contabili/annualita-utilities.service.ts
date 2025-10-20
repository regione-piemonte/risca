import { Injectable } from '@angular/core';
import { AnnualitaSDVo } from '../../../../core/commons/vo/annualita-sd-vo';
import { findAnnaulitaBEFE } from './dati-contabili/dati-contabili-utility.functions';

@Injectable({ providedIn: 'root' })
export class AnnualitaUtilitiesService {
  /**
   * Costruttore.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione di utility che ricerca un'annualità all'interno di un array di annualita.
   * @param annualita AnnualitaSDVo con le informazioni dell'annualità da cercare.
   * @param annualitaList AnnualitaSDVo[] con la lista d'informazioni delle annualità per la ricerca.
   * @return number con l'indice posizionale dell'oggetto nella lista.
   */
  indexAnnualitaInListByAnno(
    annualita: AnnualitaSDVo,
    annualitaList: AnnualitaSDVo[]
  ): number {
    // Definisco la lista di annualita per la ricerca
    const annList: AnnualitaSDVo[] = annualitaList ?? [];
    // Cerco l'annualità modificata
    const iAnn = annList.findIndex((at: AnnualitaSDVo) => {
      // Utilizzo la funzione di utility per il match tra annualita
      return annualita.anno === at.anno;
      // #
    });

    // Ritorno l'indice
    return iAnn;
  }

  /**
   * Funzione di utility che ricerca un'annualità all'interno di un array di annualita.
   * @param annualita AnnualitaSDVo con le informazioni dell'annualità da cercare.
   * @param annualitaList AnnualitaSDVo[] con la lista d'informazioni delle annualità per la ricerca.
   * @return number con l'indice posizionale dell'oggetto nella lista.
   */
  indexAnnualitaInListById(
    annualita: AnnualitaSDVo,
    annualitaList: AnnualitaSDVo[]
  ): number {
    // Recupero la lista di annualità dalla tabella
    const annTable: AnnualitaSDVo[] = annualitaList ?? [];
    // Cerco l'annualità modificata
    const iAnn = annTable.findIndex((at: AnnualitaSDVo) => {
      // Utilizzo la funzione di utility per il match tra annualita
      return findAnnaulitaBEFE(at, annualita);
      // #
    });

    // Ritorno l'indice
    return iAnn;
  }
}
