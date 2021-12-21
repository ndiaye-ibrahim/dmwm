import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICellule, Cellule } from '../cellule.model';
import { CelluleService } from '../service/cellule.service';

@Injectable({ providedIn: 'root' })
export class CelluleRoutingResolveService implements Resolve<ICellule> {
  constructor(protected service: CelluleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICellule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cellule: HttpResponse<Cellule>) => {
          if (cellule.body) {
            return of(cellule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cellule());
  }
}
