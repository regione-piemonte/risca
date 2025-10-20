import { IRiscaAnnoSelect } from 'src/app/shared/utilities';
import { IBollettiniModalForm } from '../bollettini/bollettini.interfaces';

/**
 * Interfaccia che rappresenta l'oggetto generato dalla form bollettazione ordinaria.
 */
export interface IBOModalForm extends IBollettiniModalForm {
  anno: IRiscaAnnoSelect;
}
