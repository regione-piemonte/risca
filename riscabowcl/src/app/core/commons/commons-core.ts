//messaggi di errori passati alle pagine 
export enum MessageRestError {
    GENERIC = "Attenzione! Si è verificato un errore di comunicazione con il server! Contattare l'assistenza o riprovare tra qualche minuto",//500
    TIMEOUT = "Attenzione! la rete da cui si è collegati risulta essere troppo lenta",  //599
    UNAUTHORIZED = "Attenzione! Si è tentato di accedere ad una risorsa a cui non si è abilitati.",//403
    NOTAUTHENTICATED = "Attenzione! Si è verificato un problema di autenticazione", //401
    NOTFOUND = "Attenzione! Si è cercato di accendere ad una risorsa inesistente", //404
    INPUTERROR = "Attenzione Errore durante il salvataggio, verificare i dati inseriti",
    CONFLICT = "Attenzione! I dati sono stati aggiornati o cancellati da un'altra transazione."
}

export enum Esito {
    SUCCESS = "S",
    ERROR = "E",
}

export class Roles {
    public static HOME: string = "UCSIGAS1";
    public static HOME_FO: string = "ROLE_FO_FRONTEND";
}

export class SubMenu {
    constructor(
        public routing: string,
        public name: string, 
        public disable: boolean
    ) { }
}

/**
 * @version SONARQUBE_22_04_24 Modificato costruttore per non renderlo vuoto.
 */
export class BaseMenu {
    constructor(
        public name: string, 
        public enable: boolean
    ) { }
}


export class GinevraPiemonte {
    public static id: number = 1;
}
