import { ComuneVo } from '../../../../core/commons/vo/comune-vo';
import { NazioneVo } from '../../../../core/commons/vo/nazione-vo';
import { RuoloCompilante } from '../../../../shared/models';
import { RuoloSoggetto } from '../soggetto/ruolo-soggetto.model';
import { TipoNaturaGiuridica } from '../soggetto/tipo-natura-giuridica.model';
import { TipoSoggettoVo } from '../soggetto/tipo-soggetto.model';

export interface FormSoggettoPF {
  anagraficaSoggetto: AnagraficaSoggettoPF;
  ruoloCompilante: RuoloCompilante;
}

export interface FormSoggettoPG {
  anagraficaSoggetto: AnagraficaSoggettoPG;
  ruoloCompilante: RuoloCompilante;
  anagraficaRichiedente: AnagraficaSoggettoPF;
}

interface AnagraficaSoggettoPF {
  id: number;
  gestUID?: string;
  cf: string;
  tipoSoggetto: TipoSoggettoVo;
  cognome: string;
  nome: string;
  statoNascita: NazioneVo;
  provinciaNascita: string;
  comuneNascita: ComuneVo;
  dataNascita: string;
  statoResidenza: NazioneVo;
  provinciaResidenza: string;
  comuneResidenza: ComuneVo;
  indirizzoResidenza: string;
  civicoResidenza: string;
  capResidenza: string;
  email: string;
  pec: string;
  telefono: string;
  ruoloSoggetto: RuoloSoggetto;
}

interface AnagraficaSoggettoPG {
  id: number;
  gestUID?: string;
  regioneSociale: string;
  naturaGiuridica: TipoNaturaGiuridica;
  cf: string;
  pIva: string;
  tipoSoggetto: TipoSoggettoVo;
  statoSedeLegale: string;
  provinciaSedeLegale: string;
  comuneSedeLegale: ComuneVo;
  indirizzoSedeLegale: string;
  civicoSedeLegale: string;
  capSedeLegale: string;
  emailSedeLegale: string;
  pecSedeLegale: string;
  telefonoSedeLegale: string;
  provinciaCciaa: string;
  annoCciaa: number;
  numeroCciaa: string;
}
