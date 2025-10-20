import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { map } from 'rxjs/operators';
import { DataQuadro, Quadro, Template, TemplateQuadro } from '../models';
import { ConfigService } from '../../core/services/config.service';

@Injectable()
export class StepManagerService {
  idIstanzaSub = new BehaviorSubject<number>(null); // or simple Subject?
  stepCompletedSub = new Subject<{
    stepComponent: string;
    completed: boolean;
    idQuadro: number;
  }>();
  avantiClickedSub = new Subject();
  indietroClickedSub = new Subject();

  private beUrl = '';

  constructor(private http: HttpClient, private config: ConfigService) {
    this.beUrl = this.config.getBEUrl();
  }

  getQuadriByProcedimento(codeTemplate: string): Observable<Template> {
    return this.http
      .get<Template[]>(
        `${this.beUrl}/template-quadri/code-template/${codeTemplate}`
      )
      .pipe(map((res) => res[0]));
  }

  getQuadro(idTemplateQuadro: number): Observable<Quadro> {
    return this.http
      .get<Template[]>(
        `${this.beUrl}/template-quadri/id-template-quadro/${idTemplateQuadro}`
      )
      .pipe(map((res) => res[0].quadri[0]));
  }

  getQuadroByIdIstanza(
    idIstanza: number,
    idTemplateQuadro: number
  ): Observable<TemplateQuadro> {
    return this.http
      .get<TemplateQuadro[]>(
        `${this.beUrl}/istanze-template-quadri/id-istanza/${idIstanza}/id-template-quadro/${idTemplateQuadro}`
      )
      .pipe(map((res) => res[0]));
  }

  salvaJsonDataQuadro(
    data: DataQuadro,
    update = false
  ): Observable<DataQuadro> {
    if (update) {
      return this.http.put<DataQuadro>(
        `${this.beUrl}/istanze-template-quadri`,
        data
      );
    } else {
      return this.http.post<DataQuadro>(
        `${this.beUrl}/istanze-template-quadri`,
        data
      );
    }
  }

  setCompleted(
    stepComponent: string,
    completed: boolean,
    idQuadro: number = null
  ) {
    this.stepCompletedSub.next({ stepComponent, completed, idQuadro });
  }

  onAvanti() {
    this.avantiClickedSub.next();
  }

  onIndietro() {
    this.indietroClickedSub.next();
  }
}
