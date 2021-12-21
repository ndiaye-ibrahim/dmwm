import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Typecellule } from 'app/entities/enumerations/typecellule.model';
import { ICellule, Cellule } from '../cellule.model';

import { CelluleService } from './cellule.service';

describe('Cellule Service', () => {
  let service: CelluleService;
  let httpMock: HttpTestingController;
  let elemDefault: ICellule;
  let expectedResult: ICellule | ICellule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CelluleService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nom: Typecellule.CA,
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

    it('should create a Cellule', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Cellule()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cellule', () => {
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

    it('should partial update a Cellule', () => {
      const patchObject = Object.assign(
        {
          nom: 'BBBBBB',
        },
        new Cellule()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cellule', () => {
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

    it('should delete a Cellule', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCelluleToCollectionIfMissing', () => {
      it('should add a Cellule to an empty array', () => {
        const cellule: ICellule = { id: 123 };
        expectedResult = service.addCelluleToCollectionIfMissing([], cellule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cellule);
      });

      it('should not add a Cellule to an array that contains it', () => {
        const cellule: ICellule = { id: 123 };
        const celluleCollection: ICellule[] = [
          {
            ...cellule,
          },
          { id: 456 },
        ];
        expectedResult = service.addCelluleToCollectionIfMissing(celluleCollection, cellule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cellule to an array that doesn't contain it", () => {
        const cellule: ICellule = { id: 123 };
        const celluleCollection: ICellule[] = [{ id: 456 }];
        expectedResult = service.addCelluleToCollectionIfMissing(celluleCollection, cellule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cellule);
      });

      it('should add only unique Cellule to an array', () => {
        const celluleArray: ICellule[] = [{ id: 123 }, { id: 456 }, { id: 99751 }];
        const celluleCollection: ICellule[] = [{ id: 123 }];
        expectedResult = service.addCelluleToCollectionIfMissing(celluleCollection, ...celluleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cellule: ICellule = { id: 123 };
        const cellule2: ICellule = { id: 456 };
        expectedResult = service.addCelluleToCollectionIfMissing([], cellule, cellule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cellule);
        expect(expectedResult).toContain(cellule2);
      });

      it('should accept null and undefined values', () => {
        const cellule: ICellule = { id: 123 };
        expectedResult = service.addCelluleToCollectionIfMissing([], null, cellule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cellule);
      });

      it('should return initial array if no Cellule is added', () => {
        const celluleCollection: ICellule[] = [{ id: 123 }];
        expectedResult = service.addCelluleToCollectionIfMissing(celluleCollection, undefined, null);
        expect(expectedResult).toEqual(celluleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
