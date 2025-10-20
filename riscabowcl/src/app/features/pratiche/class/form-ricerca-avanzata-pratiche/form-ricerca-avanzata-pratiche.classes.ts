import { FormGroup } from '@angular/forms';
import { NazioneVo } from '../../../../core/commons/vo/nazione-vo';
import { ProvinciaCompetenzaVo } from '../../../../core/commons/vo/provincia-competenza-vo';
import { ProvinciaVo } from '../../../../core/commons/vo/provincia-vo';
import { StatoRiscossioneVo } from '../../../../core/commons/vo/stati-riscossione-vo';
import { TipoIstanzaProvvedimentoVo } from '../../../../core/commons/vo/tipo-istanza-provvidemento-vo';
import { TipoTitoloVo } from '../../../../core/commons/vo/tipo-titolo-vo';
import { TipoSoggettoVo } from '../../../ambito/models';
import { IRicercaAvanzataPraticheSDFilters } from '../../components/ricerca-avanzata/form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.classes';
import { IFRAPModalitaRicerca } from '../../components/ricerca-avanzata/form-ricerca-avanzata-pratiche-sd/utilities/form-ricerca-avanzata-pratiche-sd.interfaces';

/**
 * Interfaccia che rappresenta la struttura dati per la pre-valorizzazione degli elementi nella form di ricerca.
 */
export interface IFRAPInitFormFields {
  form: FormGroup;
  source: IRicercaAvanzataPraticheSDFilters;

  listaTipiModalitaRicerca?: IFRAPModalitaRicerca[];
  listaTipiUtente?: TipoSoggettoVo[];
  listaNazioni?: NazioneVo[];
  listaProvince?: ProvinciaVo[];
  listaProvinceCompetenza?: ProvinciaCompetenzaVo[];
  listaTipiTitoli?: TipoTitoloVo[];
  listaTipiProvvedimenti?: TipoIstanzaProvvedimentoVo[];
  listaStatiPratiche?: StatoRiscossioneVo[];
  listaTipiIstanza?: TipoIstanzaProvvedimentoVo[];
}
