import { OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';

export class AutoUnsubscribe implements OnDestroy {
  destroy$: Subject<boolean> = new Subject<boolean>();

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.unsubscribe();
  }
}
