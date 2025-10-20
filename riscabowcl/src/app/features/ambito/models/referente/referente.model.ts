import { Istanza } from 'src/app/shared/models';

export interface Referente {
  id_referente_istanza?: number;
  gestUID?: string;
  istanza: Istanza;
  cognome_referente: string;
  nome_referente: string;
  num_tel_referente: string;
  des_email_referente: string;
  des_mansione_referente?: string;
}
