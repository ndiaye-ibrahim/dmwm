jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LocaliteService } from '../service/localite.service';
import { ILocalite, Localite } from '../localite.model';
import { ISousLocalite } from 'app/entities/sous-localite/sous-localite.model';
import { SousLocaliteService } from 'app/entities/sous-localite/service/sous-localite.service';
import { ICellule } from 'app/entities/cellule/cellule.model';
import { CelluleService } from 'app/entities/cellule/service/cellule.service';

import { LocaliteUpdateComponent } from './localite-update.component';

describe('Localite Management Update Component', () => {
  let comp: LocaliteUpdateComponent;
  let fixture: ComponentFixture<LocaliteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let localiteService: LocaliteService;
  let sousLocaliteService: SousLocaliteService;
  let celluleService: CelluleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LocaliteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(LocaliteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LocaliteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    localiteService = TestBed.inject(LocaliteService);
    sousLocaliteService = TestBed.inject(SousLocaliteService);
    celluleService = TestBed.inject(CelluleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SousLocalite query and add missing value', () => {
      const localite: ILocalite = { id: 456 };
      const souslocalite: ISousLocalite = { id: 46042 };
      localite.souslocalite = souslocalite;

      const sousLocaliteCollection: ISousLocalite[] = [{ id: 94900 }];
      jest.spyOn(sousLocaliteService, 'query').mockReturnValue(of(new HttpResponse({ body: sousLocaliteCollection })));
      const additionalSousLocalites = [souslocalite];
      const expectedCollection: ISousLocalite[] = [...additionalSousLocalites, ...sousLocaliteCollection];
      jest.spyOn(sousLocaliteService, 'addSousLocaliteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ localite });
      comp.ngOnInit();

      expect(sousLocaliteService.query).toHaveBeenCalled();
      expect(sousLocaliteService.addSousLocaliteToCollectionIfMissing).toHaveBeenCalledWith(
        sousLocaliteCollection,
        ...additionalSousLocalites
      );
      expect(comp.sousLocalitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Cellule query and add missing value', () => {
      const localite: ILocalite = { id: 456 };
      const cellules: ICellule = { id: 37279 };
      localite.cellules = cellules;

      const celluleCollection: ICellule[] = [{ id: 60628 }];
      jest.spyOn(celluleService, 'query').mockReturnValue(of(new HttpResponse({ body: celluleCollection })));
      const additionalCellules = [cellules];
      const expectedCollection: ICellule[] = [...additionalCellules, ...celluleCollection];
      jest.spyOn(celluleService, 'addCelluleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ localite });
      comp.ngOnInit();

      expect(celluleService.query).toHaveBeenCalled();
      expect(celluleService.addCelluleToCollectionIfMissing).toHaveBeenCalledWith(celluleCollection, ...additionalCellules);
      expect(comp.cellulesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const localite: ILocalite = { id: 456 };
      const souslocalite: ISousLocalite = { id: 72367 };
      localite.souslocalite = souslocalite;
      const cellules: ICellule = { id: 67709 };
      localite.cellules = cellules;

      activatedRoute.data = of({ localite });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(localite));
      expect(comp.sousLocalitesSharedCollection).toContain(souslocalite);
      expect(comp.cellulesSharedCollection).toContain(cellules);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Localite>>();
      const localite = { id: 123 };
      jest.spyOn(localiteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ localite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: localite }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(localiteService.update).toHaveBeenCalledWith(localite);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Localite>>();
      const localite = new Localite();
      jest.spyOn(localiteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ localite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: localite }));
      saveSubject.complete();

      // THEN
      expect(localiteService.create).toHaveBeenCalledWith(localite);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Localite>>();
      const localite = { id: 123 };
      jest.spyOn(localiteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ localite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(localiteService.update).toHaveBeenCalledWith(localite);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSousLocaliteById', () => {
      it('Should return tracked SousLocalite primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousLocaliteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCelluleById', () => {
      it('Should return tracked Cellule primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCelluleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
