import { TUserRole, UserRoles } from '../../../shared/utilities';

export class UserInfoVo {
  constructor(
    public nome: string,
    public cognome: string,
    public codFisc: string,
    public ente: string,
    public ruolo: TUserRole,
    public codRuolo: UserRoles, // string,
    public livAuth: number,
    public community: string,
    public ambito: number
  ) {}
}
