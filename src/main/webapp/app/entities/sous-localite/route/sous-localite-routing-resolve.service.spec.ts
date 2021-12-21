jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISousLocalite, SousLocalite } from '../sous-localite.model';
import { SousLocaliteService } from '../service/sous-localite.service';

import { SousLocaliteRoutingResolveService } from './sous-localite-routing-resolve.service';

describe('SousLocalite routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SousLocaliteRoutingResolveService;
  let service: SousLocaliteService;
  let resultSousLocalite: ISousLocalite | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SousLocaliteRoutingResolveService);
    service = TestBed.inject(SousLocaliteService);
    resultSousLocalite = undefined;
  });

  describe('resolve', () => {
    it('should return ISousLocalite returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousLocalite = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousLocalite).toEqual({ id: 123 });
    });

    it('should return new ISousLocalite if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousLocalite = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSousLocalite).toEqual(new SousLocalite());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SousLocalite })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousLocalite = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousLocalite).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
