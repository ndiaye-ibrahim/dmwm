import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Rolemembre } from 'app/entities/enumerations/rolemembre.model';
import { IMembre, Membre } from '../membre.model';

import { MembreService } from './membre.service';

describe('Membre Service', () => {
  let service: MembreService;
  let httpMock: HttpTestingController;
  let elemDefault: IMembre;
  let expectedResult: IMembre | IMembre[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MembreService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nom: 'AAAAAAA',
      prenom: 'AAAAAAA',
      fonctionInterne: Rolemembre.MembreSimple,
      fonctionExterne: 'AAAAAAA',
      structure: 'AAAAAAA',
      situationMatrimoniale: 'AAAAAAA',
      numeroTelephone: 'AAAAAAA',
      email: 'AAAAAAA',
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

    it('should create a Membre', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Membre()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Membre', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          prenom: 'BBBBBB',
          fonctionInterne: 'BBBBBB',
          fonctionExterne: 'BBBBBB',
          structure: 'BBBBBB',
          situationMatrimoniale: 'BBBBBB',
          numeroTelephone: 'BBBBBB',
          email: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Membre', () => {
      const patchObject = Object.assign(
        {
          nom: 'BBBBBB',
          prenom: 'BBBBBB',
          fonctionInterne: 'BBBBBB',
          fonctionExterne: 'BBBBBB',
          structure: 'BBBBBB',
          situationMatrimoniale: 'BBBBBB',
          numeroTelephone: 'BBBBBB',
          email: 'BBBBBB',
        },
        new Membre()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Membre', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          prenom: 'BBBBBB',
          fonctionInterne: 'BBBBBB',
          fonctionExterne: 'BBBBBB',
          structure: 'BBBBBB',
          situationMatrimoniale: 'BBBBBB',
          numeroTelephone: 'BBBBBB',
          email: 'BBBBBB',
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

    it('should delete a Membre', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMembreToCollectionIfMissing', () => {
      it('should add a Membre to an empty array', () => {
        const membre: IMembre = { id: 123 };
        expectedResult = service.addMembreToCollectionIfMissing([], membre);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(membre);
      });

      it('should not add a Membre to an array that contains it', () => {
        const membre: IMembre = { id: 123 };
        const membreCollection: IMembre[] = [
          {
            ...membre,
          },
          { id: 456 },
        ];
        expectedResult = service.addMembreToCollectionIfMissing(membreCollection, membre);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Membre to an array that doesn't contain it", () => {
        const membre: IMembre = { id: 123 };
        const membreCollection: IMembre[] = [{ id: 456 }];
        expectedResult = service.addMembreToCollectionIfMissing(membreCollection, membre);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(membre);
      });

      it('should add only unique Membre to an array', () => {
        const membreArray: IMembre[] = [{ id: 123 }, { id: 456 }, { id: 89781 }];
        const membreCollection: IMembre[] = [{ id: 123 }];
        expectedResult = service.addMembreToCollectionIfMissing(membreCollection, ...membreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const membre: IMembre = { id: 123 };
        const membre2: IMembre = { id: 456 };
        expectedResult = service.addMembreToCollectionIfMissing([], membre, membre2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(membre);
        expect(expectedResult).toContain(membre2);
      });

      it('should accept null and undefined values', () => {
        const membre: IMembre = { id: 123 };
        expectedResult = service.addMembreToCollectionIfMissing([], null, membre, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(membre);
      });

      it('should return initial array if no Membre is added', () => {
        const membreCollection: IMembre[] = [{ id: 123 }];
        expectedResult = service.addMembreToCollectionIfMissing(membreCollection, undefined, null);
        expect(expectedResult).toEqual(membreCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
