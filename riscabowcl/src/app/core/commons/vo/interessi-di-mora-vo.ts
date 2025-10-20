import { IInteressiDiMoraFormData } from 'src/app/features/configurazioni/components/interessi-di-mora-form/utilities/interessi-di-mora-form.interfaces';
import {
  ITassiDiInteresseVo,
  TassiDiInteresseVo,
} from './tassi-di-interesse-vo';

export interface IInteressiDiMoraVo extends ITassiDiInteresseVo {}

export class InteressiDiMoraVo extends TassiDiInteresseVo {
  /**
   * Costruttore.
   */
  constructor(imVo?: IInteressiDiMoraVo) {
    super(imVo);
  }

  /**
   * Converto le informazioni della classe in un oggetto: IInteressiLegaliFormData.
   * @param interessiDiMora InteressiDiMoraVo con l'oggetto da convertire.
   * @returns IInteressiDiMoraFormData con l'oggetto convertito.
   */
  toInteressiDiMoraFormData(
    interessiDiMora: InteressiDiMoraVo
  ): IInteressiDiMoraFormData {
    // Creo un oggetto specifico per convertito
    const interessiDiMoraForm: IInteressiDiMoraFormData = {
      percentuale: interessiDiMora?.percentuale,
      dataInizio: this.momentToNgbDateStruct(interessiDiMora?.data_inizio),
      dataFine: this.momentToNgbDateStruct(interessiDiMora?.data_fine),
    };

    // Ritorno l'oggetto convertito
    return interessiDiMoraForm;
  }
}

/**
 * Funzione di utility che converte una lista di oggetti (interface) con i relativi oggetti FE (class).
 * @param iInteressiLegali IInteressiDiMoraVo[] con la lista da convertire.
 * @returns InteressiDiMoraVo[] con la lista dati convertita.
 */
export const toListInteressiDiMoraVo = (
  iInteressiDiMora: IInteressiDiMoraVo[]
): InteressiDiMoraVo[] => {
  // Verifico l'input
  iInteressiDiMora = iInteressiDiMora ?? [];
  // Itero la lista in input e converto gli oggetti
  return iInteressiDiMora.map((iIL: IInteressiDiMoraVo) => {
    // Converto e ritorno l'oggetto
    return new InteressiDiMoraVo(iIL);
    // #
  });
};
