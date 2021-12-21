jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MembreService } from '../service/membre.service';
import { IMembre, Membre } from '../membre.model';

import { MembreUpdateComponent } from './membre-update.component';

describe('Membre Management Update Component', () => {
  let comp: MembreUpdateComponent;
  let fixture: ComponentFixture<MembreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let membreService: MembreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MembreUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(MembreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MembreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    membreService = TestBed.inject(MembreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const membre: IMembre = { id: 456 };

      activatedRoute.data = of({ membre });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(membre));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Membre>>();
      const membre = { id: 123 };
      jest.spyOn(membreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ membre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: membre }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(membreService.update).toHaveBeenCalledWith(membre);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Membre>>();
      const membre = new Membre();
      jest.spyOn(membreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ membre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: membre }));
      saveSubject.complete();

      // THEN
      expect(membreService.create).toHaveBeenCalledWith(membre);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Membre>>();
      const membre = { id: 123 };
      jest.spyOn(membreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ membre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(membreService.update).toHaveBeenCalledWith(membre);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
