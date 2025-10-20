import { IInteressiLegaliFormData } from '../../../features/configurazioni/components/interessi-legali-form/utilities/interessi-legali-form.interfaces';
import {
  ITassiDiInteresseVo,
  TassiDiInteresseVo,
} from './tassi-di-interesse-vo';

export interface IInteressiLegaliVo extends ITassiDiInteresseVo {}

export class InteressiLegaliVo extends TassiDiInteresseVo {
  /**
   * Costruttore.
   */
  constructor(ilVo?: IInteressiLegaliVo) {
    super(ilVo);
  }

  /**
   * Converto le informazioni della classe in un oggetto: IInteressiLegaliFormData.
   * @param interessiLegali InteressiLegaliVo da convertire.
   * @returns IInteressiLegaliFormData come risultato della conversione.
   */
  toInteressiLegaliFormData(
    interessiLegali: InteressiLegaliVo
  ): IInteressiLegaliFormData {
    // Creo l'oggetto con la conversione dei dati
    const interessiLegaliForm: IInteressiLegaliFormData = {
      percentuale: interessiLegali?.percentuale,
      dataInizio: this.momentToNgbDateStruct(interessiLegali?.data_inizio),
      dataFine: this.momentToNgbDateStruct(interessiLegali?.data_fine),
      giorni: interessiLegali?.giorni_legali,
    };

    // Ritorno l'oggetto convertito
    return interessiLegaliForm;
  }
}

/**
 * Funzione di utility che converte una lista di oggetti (interface) con i relativi oggetti FE (class).
 * @param iInteressiLegali IInteressiLegaliVo[] con la lista da convertire.
 * @returns InteressiLegaliVo[] con la lista dati convertita.
 */
export const toListInteressiLegaliVo = (
  iInteressiLegali: IInteressiLegaliVo[]
): InteressiLegaliVo[] => {
  // Verifico l'input
  iInteressiLegali = iInteressiLegali ?? [];
  // Itero la lista in input e converto gli oggetti
  return iInteressiLegali.map((iIL: IInteressiLegaliVo) => {
    // Converto e ritorno l'oggetto
    return new InteressiLegaliVo(iIL);
    // #
  });
};
