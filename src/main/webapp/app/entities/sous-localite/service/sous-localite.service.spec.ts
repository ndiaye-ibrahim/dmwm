import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISousLocalite, SousLocalite } from '../sous-localite.model';

import { SousLocaliteService } from './sous-localite.service';

describe('SousLocalite Service', () => {
  let service: SousLocaliteService;
  let httpMock: HttpTestingController;
  let elemDefault: ISousLocalite;
  let expectedResult: ISousLocalite | ISousLocalite[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SousLocaliteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nom: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a SousLocalite', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SousLocalite()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SousLocalite', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SousLocalite', () => {
      const patchObject = Object.assign({}, new SousLocalite());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SousLocalite', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a SousLocalite', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSousLocaliteToCollectionIfMissing', () => {
      it('should add a SousLocalite to an empty array', () => {
        const sousLocalite: ISousLocalite = { id: 123 };
        expectedResult = service.addSousLocaliteToCollectionIfMissing([], sousLocalite);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousLocalite);
      });

      it('should not add a SousLocalite to an array that contains it', () => {
        const sousLocalite: ISousLocalite = { id: 123 };
        const sousLocaliteCollection: ISousLocalite[] = [
          {
            ...sousLocalite,
          },
          { id: 456 },
        ];
        expectedResult = service.addSousLocaliteToCollectionIfMissing(sousLocaliteCollection, sousLocalite);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SousLocalite to an array that doesn't contain it", () => {
        const sousLocalite: ISousLocalite = { id: 123 };
        const sousLocaliteCollection: ISousLocalite[] = [{ id: 456 }];
        expectedResult = service.addSousLocaliteToCollectionIfMissing(sousLocaliteCollection, sousLocalite);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousLocalite);
      });

      it('should add only unique SousLocalite to an array', () => {
        const sousLocaliteArray: ISousLocalite[] = [{ id: 123 }, { id: 456 }, { id: 17562 }];
        const sousLocaliteCollection: ISousLocalite[] = [{ id: 123 }];
        expectedResult = service.addSousLocaliteToCollectionIfMissing(sousLocaliteCollection, ...sousLocaliteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sousLocalite: ISousLocalite = { id: 123 };
        const sousLocalite2: ISousLocalite = { id: 456 };
        expectedResult = service.addSousLocaliteToCollectionIfMissing([], sousLocalite, sousLocalite2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousLocalite);
        expect(expectedResult).toContain(sousLocalite2);
      });

      it('should accept null and undefined values', () => {
        const sousLocalite: ISousLocalite = { id: 123 };
        expectedResult = service.addSousLocaliteToCollectionIfMissing([], null, sousLocalite, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousLocalite);
      });

      it('should return initial array if no SousLocalite is added', () => {
        const sousLocaliteCollection: ISousLocalite[] = [{ id: 123 }];
        expectedResult = service.addSousLocaliteToCollectionIfMissing(sousLocaliteCollection, undefined, null);
        expect(expectedResult).toEqual(sousLocaliteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
