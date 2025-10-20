import { ValidationErrors } from '@angular/forms';
import { IDatiTecniciAmbiente } from '../../../features/pratiche/components/quadri-tecnici/utilities/interfaces/dt-ambiente-pratica.interfaces';
import {
  EnumCRUD,
  RiscaInfoLevels,
  RiscaLoadStatus,
  RiscaTableBodyTabMethods,
  ServerStringAsBoolean,
  UserRoles,
} from '../enums/utilities.enums';
import {
  DynamicObjAny,
  IRTInputValidatorParams,
  NuovaIstanzaFormData,
  NuovoProvvedimentoFormData,
} from '../interfaces/utilities.interfaces';

/**
 * Tipo personalizzato per la gestione dello stato di caricamento dell'applicazione.
 */
export type TRiscaLoadStatus = RiscaLoadStatus;

/**
 * Tipizzazione che definisce il template gestito dal componente "risca-alert"
 */
export type TRiscaAlertType =
  | RiscaInfoLevels.none
  | RiscaInfoLevels.success
  | RiscaInfoLevels.danger
  | RiscaInfoLevels.info
  | RiscaInfoLevels.warning;

/**
 * Type personalizzato per la gestione dei metodi usabili dal componente "risca-table".
 */
export type RiscaTableBodyTabMethodType =
  | RiscaTableBodyTabMethods.check
  | RiscaTableBodyTabMethods.delete
  | RiscaTableBodyTabMethods.close
  | RiscaTableBodyTabMethods.detail
  | RiscaTableBodyTabMethods.modify
  | RiscaTableBodyTabMethods.radio;

/**
 * Type personalizzato che definisce le possibili interfacce che può assumere un dato tecnico generico.
 */
export type DatiTecnici = IDatiTecniciAmbiente;

/**
 * Type personalizzato che definisce i possibili ruoli utente.
 */
export type TUserRole = UserRoles;

/**
 * Type personalizzato che definisce l'orientamento per il componente: risca-checkbox | risca-radio.
 */
export type TRiscaFormInputChoiceOrientation = 'horizontal' | 'vertical';

/**
 * Type personalizzato che definisce la tipologia di sorting usato per le tabelle risca-table.
 */
export type TRiscaTableTypeSorting = 'date' | 'string' | 'number';

/**
 * Type che definisce il tipo di operazioni CRUD in ambito generico nell'applicazione.
 */
export type TypeCRUD = EnumCRUD;

/**
 * Type che definisce la tipologia delle istanze/provvedimenti gestiti dal frontend.
 */
export type TNIPFormData = NuovaIstanzaFormData | NuovoProvvedimentoFormData;

/**
 * Type che definisce la tipologia dei boolean gestiti lato server.
 */
export type TRiscaServerBoolean =
  | string
  | ServerStringAsBoolean.true
  | ServerStringAsBoolean.false;

/**
 * Type che definisce la tipologia delle informazioni per il replace nei messaggi.
 */
export type TRiscaDataPlaceholder =
  | string
  | number
  | string[]
  | number[]
  | (string | number)[];

/**
 * Type che definisce la tipologia delle informazioni per il replace nei messaggi, come array.
 */
export type TRiscaDataPlaceholders = TRiscaDataPlaceholder[];

/**
 * Type che definisce un methodo senza parametri.
 */
export type RiscaMethod = () => void;

/**
 * Type che definisce una variabile compatibile con la direttiva NgClass (string) o NgStyle (oggetto any con le proprietà css d'applicare)
 */
export type RiscaCss = string | DynamicObjAny;

/**
 * Type che definisce il valore boolean del server come number.
 */
export type RiscaBooleanNumber = number | 0 | 1;

/**
 * Type che definisce il valore boolean del server come string.
 */
export type RiscaBooleanString = string | 'N' | 'S';

/**
 * Type che definisce il tipo di funzione accettata per la gestione dei controlli sul form control della input della tabella.
 */
export type TRTInputValidator<T> = (
  params: IRTInputValidatorParams<T>
) => ValidationErrors | null;
