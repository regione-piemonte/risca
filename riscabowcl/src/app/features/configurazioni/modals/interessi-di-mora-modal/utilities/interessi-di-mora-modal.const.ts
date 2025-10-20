/**
 * Oggetto contenente una serie di costanti per il componente omonimo.
 */
export class InteressiDiMoraModalConsts {
    MODAL_TITLE: string = 'Modifica tasso d’interesse';
    MODAL_SOTTOTITOLO: string = 'Tasso d\'interesse di mora';
    // boolean che determina se il button aggiungi dev'essere visualizzato o meno
    readonly SHOW_BUTTON_AGGIUNGI: boolean = false;
    // boolean che determina se il campo data fine dev'essere visualizzato o meno
    readonly SHOW_FIELD_DATA_FINE: boolean = true;
    // boolean che determina se il campo data fine dev'essere visualizzato o meno
    readonly DISABLED_DATA_INIZIO: boolean = true;
    // boolean che determina se il campo giorni dev'essere visualizzato o meno
    readonly SHOW_FIELD_GIORNI: boolean = false;
    // boolean che determina se i campi della form devono essere disabilitati o meno
    readonly IS_MODAL: boolean = true;
  }