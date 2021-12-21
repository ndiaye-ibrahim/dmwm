import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISousLocalite, SousLocalite } from '../sous-localite.model';
import { SousLocaliteService } from '../service/sous-localite.service';

@Injectable({ providedIn: 'root' })
export class SousLocaliteRoutingResolveService implements Resolve<ISousLocalite> {
  constructor(protected service: SousLocaliteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISousLocalite> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sousLocalite: HttpResponse<SousLocalite>) => {
          if (sousLocalite.body) {
            return of(sousLocalite.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SousLocalite());
  }
}
