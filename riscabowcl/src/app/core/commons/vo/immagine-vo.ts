export class IImmagineVo {
  id_immagine: number;
  immagine: string;
  flg_validita: number;
  path_immagine: string;
}

export class ImmagineVo {
  id_immagine: number;
  immagine: string;
  flg_validita: number;
  path_immagine: string;

  constructor(i: IImmagineVo) {
    // Verifico l'input
    if (!i) {
      // Niente da fare
      return;
    }

    this.id_immagine = i.id_immagine;
    this.immagine = i.immagine;
    this.flg_validita = i.flg_validita;
    this.path_immagine = i.path_immagine;
  }
}
