jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICellule, Cellule } from '../cellule.model';
import { CelluleService } from '../service/cellule.service';

import { CelluleRoutingResolveService } from './cellule-routing-resolve.service';

describe('Cellule routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CelluleRoutingResolveService;
  let service: CelluleService;
  let resultCellule: ICellule | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CelluleRoutingResolveService);
    service = TestBed.inject(CelluleService);
    resultCellule = undefined;
  });

  describe('resolve', () => {
    it('should return ICellule returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCellule = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCellule).toEqual({ id: 123 });
    });

    it('should return new ICellule if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCellule = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCellule).toEqual(new Cellule());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Cellule })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCellule = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCellule).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
