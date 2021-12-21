jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SectionService } from '../service/section.service';
import { ISection, Section } from '../section.model';
import { IMembre } from 'app/entities/membre/membre.model';
import { MembreService } from 'app/entities/membre/service/membre.service';

import { SectionUpdateComponent } from './section-update.component';

describe('Section Management Update Component', () => {
  let comp: SectionUpdateComponent;
  let fixture: ComponentFixture<SectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sectionService: SectionService;
  let membreService: MembreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SectionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sectionService = TestBed.inject(SectionService);
    membreService = TestBed.inject(MembreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Membre query and add missing value', () => {
      const section: ISection = { id: 456 };
      const membre: IMembre = { id: 67515 };
      section.membre = membre;

      const membreCollection: IMembre[] = [{ id: 99497 }];
      jest.spyOn(membreService, 'query').mockReturnValue(of(new HttpResponse({ body: membreCollection })));
      const additionalMembres = [membre];
      const expectedCollection: IMembre[] = [...additionalMembres, ...membreCollection];
      jest.spyOn(membreService, 'addMembreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ section });
      comp.ngOnInit();

      expect(membreService.query).toHaveBeenCalled();
      expect(membreService.addMembreToCollectionIfMissing).toHaveBeenCalledWith(membreCollection, ...additionalMembres);
      expect(comp.membresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const section: ISection = { id: 456 };
      const membre: IMembre = { id: 15653 };
      section.membre = membre;

      activatedRoute.data = of({ section });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(section));
      expect(comp.membresSharedCollection).toContain(membre);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Section>>();
      const section = { id: 123 };
      jest.spyOn(sectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ section });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: section }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sectionService.update).toHaveBeenCalledWith(section);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Section>>();
      const section = new Section();
      jest.spyOn(sectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ section });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: section }));
      saveSubject.complete();

      // THEN
      expect(sectionService.create).toHaveBeenCalledWith(section);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Section>>();
      const section = { id: 123 };
      jest.spyOn(sectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ section });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sectionService.update).toHaveBeenCalledWith(section);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMembreById', () => {
      it('Should return tracked Membre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMembreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
