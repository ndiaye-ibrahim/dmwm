import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISousLocalite, getSousLocaliteIdentifier } from '../sous-localite.model';

export type EntityResponseType = HttpResponse<ISousLocalite>;
export type EntityArrayResponseType = HttpResponse<ISousLocalite[]>;

@Injectable({ providedIn: 'root' })
export class SousLocaliteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sous-localites');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sousLocalite: ISousLocalite): Observable<EntityResponseType> {
    return this.http.post<ISousLocalite>(this.resourceUrl, sousLocalite, { observe: 'response' });
  }

  update(sousLocalite: ISousLocalite): Observable<EntityResponseType> {
    return this.http.put<ISousLocalite>(`${this.resourceUrl}/${getSousLocaliteIdentifier(sousLocalite) as number}`, sousLocalite, {
      observe: 'response',
    });
  }

  partialUpdate(sousLocalite: ISousLocalite): Observable<EntityResponseType> {
    return this.http.patch<ISousLocalite>(`${this.resourceUrl}/${getSousLocaliteIdentifier(sousLocalite) as number}`, sousLocalite, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISousLocalite>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISousLocalite[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSousLocaliteToCollectionIfMissing(
    sousLocaliteCollection: ISousLocalite[],
    ...sousLocalitesToCheck: (ISousLocalite | null | undefined)[]
  ): ISousLocalite[] {
    const sousLocalites: ISousLocalite[] = sousLocalitesToCheck.filter(isPresent);
    if (sousLocalites.length > 0) {
      const sousLocaliteCollectionIdentifiers = sousLocaliteCollection.map(
        sousLocaliteItem => getSousLocaliteIdentifier(sousLocaliteItem)!
      );
      const sousLocalitesToAdd = sousLocalites.filter(sousLocaliteItem => {
        const sousLocaliteIdentifier = getSousLocaliteIdentifier(sousLocaliteItem);
        if (sousLocaliteIdentifier == null || sousLocaliteCollectionIdentifiers.includes(sousLocaliteIdentifier)) {
          return false;
        }
        sousLocaliteCollectionIdentifiers.push(sousLocaliteIdentifier);
        return true;
      });
      return [...sousLocalitesToAdd, ...sousLocaliteCollection];
    }
    return sousLocaliteCollection;
  }
}
