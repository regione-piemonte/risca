import { Pipe, PipeTransform } from '@angular/core';
import {
  DomSanitizer,
  SafeHtml,
  SafeResourceUrl,
  SafeScript,
  SafeStyle,
  SafeUrl,
} from '@angular/platform-browser';
import { RiscaMessagesService } from '../../services/risca/risca-messages.service';
import {
  riscaCssHandler,
  riscaExecute,
} from '../../services/risca/risca-utilities/risca-utilities.functions';
import { RiscaUtilitiesService } from '../../services/risca/risca-utilities/risca-utilities.service';
import {
  IRiscaNavItem,
  RiscaCss,
  RiscaCssHandlerTypes,
  SanitizerTypes,
  TRiscaDataPlaceholders,
} from '../../utilities';

/**
 * Pipe che esegue una funzione in input per gestire diverse situazioni.
 */
@Pipe({ name: 'riscaExecute' })
export class RiscaExecutePipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che esegue la funzione passata come parametro.
   * @param f (v?: any, ...p: any[]) => any da eseguire.
   * @param v any con il valore da passare come parametro alla funzione.
   * @param p Array di any che definisce n possibili parametri passati alla funzione.
   * @returns any come risultato dell'operazione.
   */
  transform(
    f: (v?: any, ...p: any[]) => any,
    v: any,
    ...p: any[]
  ): any | undefined {
    // Richiamo il servizio di utility per l'esecuzione della funzione
    return riscaExecute(f, v, p);
  }
}

/**
 * Pipe dedicata alla gestione dell'output per la gestione delle direttive NgClass e NgStyle.
 */
@Pipe({ name: 'riscaCssHandler' })
export class RiscaCssHandlerPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che ritorna la classe di stile o le configurazioni di stile a seconda dell'input.
   * @param cssConfig string o any con i dati del css.
   * @param cssType RiscaCssHandlerTypes il tipo di stile da ritonare.
   * @returns string o oggetto contenuto in colConfig.
   */
  transform(
    cssConfig: string | any,
    cssType: RiscaCssHandlerTypes
  ): string | any {
    // Verifico che gli input siano definiti
    if (!cssType) {
      throw new Error('RiscaCssHandlerPipe - No cssType defined');
    }

    // Verifico e ritorno lo stile
    return riscaCssHandler(cssType, cssConfig);
  }
}

/**
 * Pipe dedicata alla gestione dell'output per la gestione delle direttive NgStyle, avendo in input differenti oggetti di configurazione da mergiare.
 */
@Pipe({ name: 'riscaStylesHandler' })
export class RiscaStylesHandlerPipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che ritorna la classe di stile o le configurazioni di stile a seconda dell'input.
   * Più la configurazione ha un indice posizionale maggiore all'interno di otherConfigs, maggiore sarà la priorità dello stile.
   * @param firstConfig object che definisce il primo oggetto da mergiare.
   * @param otherConfigs Spread operator object con gli oggetti da mergiare.
   * @returns object con le proprietà mergiate.
   */
  transform(firstConfig: object, ...otherConfigs: object[]): object {
    // Verifico l'input
    firstConfig = firstConfig ?? {};
    otherConfigs = otherConfigs ?? [];
    // Creo un array di oggetti per il merge
    let allConfigs: RiscaCss[] = [];
    // Aggiugo la prima configurazione
    allConfigs.push(firstConfig);
    // Aggiungo le altre configurazioni
    allConfigs = [...allConfigs, ...otherConfigs];

    // Definisco il contenitore per gli stili
    let styles: object = {};
    // Ciclo le configurazioni e lancio la conversione
    allConfigs = allConfigs.map((c: RiscaCss) => {
      // Lancio la gestione dei dati, per sicurezza
      return riscaCssHandler(RiscaCssHandlerTypes.style, c);
    });
    // Mergio tutti gli stili insieme
    allConfigs.forEach((c: object) => {
      // Unisco gli stili
      styles = { ...styles, ...c };
    });
    // Ritorno l'oggetto mergiato
    return styles;
  }
}

/**
 * Pipe dedicata alla gestione della colorazione delle input in base ai campi aggiornati dalla fonte.
 */
@Pipe({ name: 'riscaAggiornatoFonte' })
export class RiscaAggiornatoFontePipe implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Data la lista di campi modificati alla fonte, dice se il campo dato in input è uno di essi.
   * @param campiModificatiFonte Array di string contenente la lista dei campi aggiornati dalla fonte.
   * @param campo string che definisce il nome del campo che può essere interessato alla colorazione.
   * @returns boolean che definisce se il campo è stato aggiornato dalla fonte (ossia, presente nell'array).
   */
  transform(campiModificatiFonte: string[], campo: string): boolean {
    // Verifico l'input
    if (!campiModificatiFonte || !campo) {
      // Ritorno di default false
      return false;
    }

    // Verifico se all'interno dell'array è presente il campo
    return campiModificatiFonte.some((c) => c == campo);
  }
}

/**
 * Pipe impiegata per la sanitizzazione dell'HTML.
 */
@Pipe({ name: 'sanitize' })
export class RiscaSanitizePipe implements PipeTransform {
  /**
   * Pipe Constructor
   * @param _sanitizer: DomSanitezer
   */
  constructor(protected _sanitizer: DomSanitizer) {}

  /**
   * La funzione esegue la sanitizzazione dell'input, sulla base della configurazione.
   * @param value string con l'informazione da sanitizzare.
   * @param type SanitizerTypes con il tipo di sanitizzazione da effettuare.
   * @returns SafeHtml | SafeStyle | SafeScript | SafeUrl | SafeResourceUrl con l'input sanitizzato.
   */
  transform(
    value: string,
    type: SanitizerTypes
  ): SafeHtml | SafeStyle | SafeScript | SafeUrl | SafeResourceUrl {
    switch (type) {
      case SanitizerTypes.html:
        return this._sanitizer.bypassSecurityTrustHtml(value);
      case SanitizerTypes.style:
        return this._sanitizer.bypassSecurityTrustStyle(value);
      case SanitizerTypes.script:
        return this._sanitizer.bypassSecurityTrustScript(value);
      case SanitizerTypes.url:
        return this._sanitizer.bypassSecurityTrustUrl(value);
      case SanitizerTypes.resourceUrl:
        return this._sanitizer.bypassSecurityTrustResourceUrl(value);
      default:
        return this._sanitizer.bypassSecurityTrustHtml(value);
    }
  }
}

/**
 * Pipe impiegata per il recupero di messaggi di notifica tramite RiscaMessagesService.
 */
@Pipe({ name: 'riscaMessage' })
export class RiscaMessagePipe implements PipeTransform {
  /**
   * Costruttore.
   */
  constructor(protected _riscaMessages: RiscaMessagesService) {}

  /**
   * Funzione che recupera un messaggio tramite servizio RiscaMessagesService.
   * @param code string che identifica il codice per il recupero del messaggio.
   * @param fallbackPlaceholder string opzionale che definisce il messaggio da ritornare in caso in cui non venga trovato per il codice.
   * @returns string con il messaggio recuperato.
   */
  transform(code: string, fallbackPlaceholder?: string): string {
    // Richiamo e ritorno il risultato del servizio
    return this._riscaMessages.getMessage(code, fallbackPlaceholder);
  }
}

/**
 * Pipe impiegata per il recupero di messaggi di notifica tramite RiscaMessagesService.
 */
@Pipe({ name: 'riscaMessageWithPlacholderByCode' })
export class RiscaMessageWithPlacholderByCodePipe implements PipeTransform {
  /**
   * Costruttore.
   */
  constructor(protected _riscaMessages: RiscaMessagesService) {}

  /**
   * Funzione che recupera un messaggio tramite servizio RiscaMessagesService.
   * @param code string codice del messaggio.
   * @param dataReplace TRiscaDataPlaceholders con i dati da sostiture per i placeholder.
   * @param fallbackPlaceholder string opzionale che definisce il messaggio da ritornare in caso in cui non venga trovato per il codice.
   * @returns string con il messaggio recuperato.
   */
  transform(
    code: string,
    dataReplace: TRiscaDataPlaceholders,
    fallbackPlaceholder?: string
  ): string {
    // Richiamo e ritorno il risultato del servizio
    return this._riscaMessages.getMessageWithPlacholderByCode(
      code,
      dataReplace,
      fallbackPlaceholder
    );
  }
}

/**
 * Pipe impiegata per la conversione della sintassi con espressione regolare in codice HTML.
 */
@Pipe({ name: 'riscaInnerHTML' })
export class RiscaInnerHTMLPipe implements PipeTransform {
  /**
   * Costruttore.
   */
  constructor(protected _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * Funzione che converte determinate codifiche all'interno di una stringa in maniera tale da convertirle in tag HTML.
   * @param stringHTML string che identifica il testo che verrà convertito tramite funzione InnerHTML.
   * @returns string con la stringa formattate gestibile tramite HTML.
   */
  transform(stringHTML: string): string {
    // Richiamo e ritorno il risultato del servizio
    return this._riscaUtilities.parseInnerHTML(stringHTML);
  }
}

/**
 * Pipe impiegata per la formattazione di un importo in stile "italiano".
 */
@Pipe({ name: 'formatoImportoITA' })
export class RiscaFormatoImportoITAPipe implements PipeTransform {
  /**
   * Costruttore.
   */
  constructor(protected _riscaUtilities: RiscaUtilitiesService) {}

  /**
   * Funzione che converte determinate un importo numerico in una stringa formattata per gli importi italiani.
   * @param importo number con l'importo da convertire.
   * @returns string con l'importo formattato.
   */
  transform(importo: number): string {
    // Richiamo e ritorno il risultato del servizio
    return this._riscaUtilities.formatoImportoITA(importo);
  }
}

/**
 * Pipe che verifica le configurazioni per il caricamento dei contenuti di menù rispetto alle configurazioni.
 */
@Pipe({ name: 'caricaNavContent' })
export class RiscaCaricaNavContentPipe<T> implements PipeTransform {
  /**
   * Costruttore del Pipe.
   */
  constructor() {
    // Sonarqube segna come critico una funzione vuota, anche se è costruttore o hook di Angular. Il commento serve per bypassare il controllo.
  }

  /**
   * Funzione che verifica le configurazioni per il caricamento dei contenuti di un menù con nav.
   * Se "pageTarget" non è definito, la logica di caricamento della pagina è direttamente associata alla nav.
   * Altrimenti viene verificata quale pagina dati caricare indipendentemente dalla nav attiva.
   * @param navTarget string che definisce il target specifico di una nav. E' solitamente una costante che viene comparata con "navComponent".
   * @param nav IRiscaNavItem<T> che definisce la configurazione della nav attualmente selezionata.
   * @param pageTarget T che definisce uno specifico target per la pagina da caricare. Questa variabile indica un contenuto diverso rispetto lo standard definito da "nav".
   * @param page T che definisce la configurazione per la page attualmente richiesta.
   * @returns boolean che definisce se la pagina è da caricare.
   */
  transform(
    navTarget: string,
    nav: IRiscaNavItem<T>,
    pageTarget?: T,
    page?: T
  ): boolean {
    // Controllo se è definito un page target
    if (pageTarget && page) {
      // E' definito, ritorno il controllo basato su questo target
      return this.checkPageTarget(pageTarget, page);
      // #
    } else {
      // Non è definito, ritorno il controllo basato sulla nav selezionata
      return this.checkNavTarget(navTarget, nav);
    }
  }

  /**
   * Funzione di check semplice, che ritorna il compare tra il target della pagina e la pagina specifica passata per parametro.
   * @param pageTarget T che definisce uno specifico target per la pagina da caricare. Questa variabile indica un contenuto diverso rispetto lo standard definito da "nav".
   * @param page T che definisce la configurazione per la page attualmente richiesta.
   * @returns boolean che definisce se la pagina è da caricare.
   */
  private checkPageTarget(pageTarget: T, page: T): boolean {
    // Ritorno il check tra pagina target e configurazione specifica
    return pageTarget === page;
  }

  /**
   * Funzione di check semplice, che ritorna il compare tra il nav selezionato e la nav specifica passata per parametro.
   * @param navTarget string che definisce il target specifico di una nav. E' solitamente una costante che viene comparata con "navComponent".
   * @param nav IRiscaNavItem<T> che definisce la configurazione della nav attualmente selezionata.
   * @returns boolean che definisce se la pagina è da caricare.
   */
  private checkNavTarget(navTarget: string, nav: IRiscaNavItem<T>): boolean {
    // Ritorno il check tra la nav target e configurazione specifica
    return navTarget === nav.component;
  }
}
