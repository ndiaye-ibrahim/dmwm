jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SousLocaliteService } from '../service/sous-localite.service';
import { ISousLocalite, SousLocalite } from '../sous-localite.model';
import { ISection } from 'app/entities/section/section.model';
import { SectionService } from 'app/entities/section/service/section.service';
import { ICellule } from 'app/entities/cellule/cellule.model';
import { CelluleService } from 'app/entities/cellule/service/cellule.service';

import { SousLocaliteUpdateComponent } from './sous-localite-update.component';

describe('SousLocalite Management Update Component', () => {
  let comp: SousLocaliteUpdateComponent;
  let fixture: ComponentFixture<SousLocaliteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sousLocaliteService: SousLocaliteService;
  let sectionService: SectionService;
  let celluleService: CelluleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SousLocaliteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SousLocaliteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousLocaliteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sousLocaliteService = TestBed.inject(SousLocaliteService);
    sectionService = TestBed.inject(SectionService);
    celluleService = TestBed.inject(CelluleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Section query and add missing value', () => {
      const sousLocalite: ISousLocalite = { id: 456 };
      const section: ISection = { id: 546 };
      sousLocalite.section = section;

      const sectionCollection: ISection[] = [{ id: 23336 }];
      jest.spyOn(sectionService, 'query').mockReturnValue(of(new HttpResponse({ body: sectionCollection })));
      const additionalSections = [section];
      const expectedCollection: ISection[] = [...additionalSections, ...sectionCollection];
      jest.spyOn(sectionService, 'addSectionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousLocalite });
      comp.ngOnInit();

      expect(sectionService.query).toHaveBeenCalled();
      expect(sectionService.addSectionToCollectionIfMissing).toHaveBeenCalledWith(sectionCollection, ...additionalSections);
      expect(comp.sectionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Cellule query and add missing value', () => {
      const sousLocalite: ISousLocalite = { id: 456 };
      const cellule: ICellule = { id: 80128 };
      sousLocalite.cellule = cellule;

      const celluleCollection: ICellule[] = [{ id: 89308 }];
      jest.spyOn(celluleService, 'query').mockReturnValue(of(new HttpResponse({ body: celluleCollection })));
      const additionalCellules = [cellule];
      const expectedCollection: ICellule[] = [...additionalCellules, ...celluleCollection];
      jest.spyOn(celluleService, 'addCelluleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousLocalite });
      comp.ngOnInit();

      expect(celluleService.query).toHaveBeenCalled();
      expect(celluleService.addCelluleToCollectionIfMissing).toHaveBeenCalledWith(celluleCollection, ...additionalCellules);
      expect(comp.cellulesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sousLocalite: ISousLocalite = { id: 456 };
      const section: ISection = { id: 30506 };
      sousLocalite.section = section;
      const cellule: ICellule = { id: 73437 };
      sousLocalite.cellule = cellule;

      activatedRoute.data = of({ sousLocalite });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sousLocalite));
      expect(comp.sectionsSharedCollection).toContain(section);
      expect(comp.cellulesSharedCollection).toContain(cellule);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousLocalite>>();
      const sousLocalite = { id: 123 };
      jest.spyOn(sousLocaliteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousLocalite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousLocalite }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sousLocaliteService.update).toHaveBeenCalledWith(sousLocalite);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousLocalite>>();
      const sousLocalite = new SousLocalite();
      jest.spyOn(sousLocaliteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousLocalite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousLocalite }));
      saveSubject.complete();

      // THEN
      expect(sousLocaliteService.create).toHaveBeenCalledWith(sousLocalite);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousLocalite>>();
      const sousLocalite = { id: 123 };
      jest.spyOn(sousLocaliteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousLocalite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sousLocaliteService.update).toHaveBeenCalledWith(sousLocalite);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSectionById', () => {
      it('Should return tracked Section primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSectionById(0, entity);
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
