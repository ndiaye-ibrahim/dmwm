import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMembre, getMembreIdentifier } from '../membre.model';

export type EntityResponseType = HttpResponse<IMembre>;
export type EntityArrayResponseType = HttpResponse<IMembre[]>;

@Injectable({ providedIn: 'root' })
export class MembreService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/membres');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(membre: IMembre): Observable<EntityResponseType> {
    return this.http.post<IMembre>(this.resourceUrl, membre, { observe: 'response' });
  }

  update(membre: IMembre): Observable<EntityResponseType> {
    return this.http.put<IMembre>(`${this.resourceUrl}/${getMembreIdentifier(membre) as number}`, membre, { observe: 'response' });
  }

  partialUpdate(membre: IMembre): Observable<EntityResponseType> {
    return this.http.patch<IMembre>(`${this.resourceUrl}/${getMembreIdentifier(membre) as number}`, membre, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMembre>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMembre[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMembreToCollectionIfMissing(membreCollection: IMembre[], ...membresToCheck: (IMembre | null | undefined)[]): IMembre[] {
    const membres: IMembre[] = membresToCheck.filter(isPresent);
    if (membres.length > 0) {
      const membreCollectionIdentifiers = membreCollection.map(membreItem => getMembreIdentifier(membreItem)!);
      const membresToAdd = membres.filter(membreItem => {
        const membreIdentifier = getMembreIdentifier(membreItem);
        if (membreIdentifier == null || membreCollectionIdentifiers.includes(membreIdentifier)) {
          return false;
        }
        membreCollectionIdentifiers.push(membreIdentifier);
        return true;
      });
      return [...membresToAdd, ...membreCollection];
    }
    return membreCollection;
  }
}
