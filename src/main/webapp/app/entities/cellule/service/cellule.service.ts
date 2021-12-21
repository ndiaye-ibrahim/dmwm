import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICellule, getCelluleIdentifier } from '../cellule.model';

export type EntityResponseType = HttpResponse<ICellule>;
export type EntityArrayResponseType = HttpResponse<ICellule[]>;

@Injectable({ providedIn: 'root' })
export class CelluleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cellules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cellule: ICellule): Observable<EntityResponseType> {
    return this.http.post<ICellule>(this.resourceUrl, cellule, { observe: 'response' });
  }

  update(cellule: ICellule): Observable<EntityResponseType> {
    return this.http.put<ICellule>(`${this.resourceUrl}/${getCelluleIdentifier(cellule) as number}`, cellule, { observe: 'response' });
  }

  partialUpdate(cellule: ICellule): Observable<EntityResponseType> {
    return this.http.patch<ICellule>(`${this.resourceUrl}/${getCelluleIdentifier(cellule) as number}`, cellule, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICellule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICellule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCelluleToCollectionIfMissing(celluleCollection: ICellule[], ...cellulesToCheck: (ICellule | null | undefined)[]): ICellule[] {
    const cellules: ICellule[] = cellulesToCheck.filter(isPresent);
    if (cellules.length > 0) {
      const celluleCollectionIdentifiers = celluleCollection.map(celluleItem => getCelluleIdentifier(celluleItem)!);
      const cellulesToAdd = cellules.filter(celluleItem => {
        const celluleIdentifier = getCelluleIdentifier(celluleItem);
        if (celluleIdentifier == null || celluleCollectionIdentifiers.includes(celluleIdentifier)) {
          return false;
        }
        celluleCollectionIdentifiers.push(celluleIdentifier);
        return true;
      });
      return [...cellulesToAdd, ...celluleCollection];
    }
    return celluleCollection;
  }
}
