jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CelluleService } from '../service/cellule.service';
import { ICellule, Cellule } from '../cellule.model';

import { CelluleUpdateComponent } from './cellule-update.component';

describe('Cellule Management Update Component', () => {
  let comp: CelluleUpdateComponent;
  let fixture: ComponentFixture<CelluleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let celluleService: CelluleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CelluleUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CelluleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CelluleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    celluleService = TestBed.inject(CelluleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cellule: ICellule = { id: 456 };

      activatedRoute.data = of({ cellule });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cellule));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cellule>>();
      const cellule = { id: 123 };
      jest.spyOn(celluleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cellule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cellule }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(celluleService.update).toHaveBeenCalledWith(cellule);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cellule>>();
      const cellule = new Cellule();
      jest.spyOn(celluleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cellule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cellule }));
      saveSubject.complete();

      // THEN
      expect(celluleService.create).toHaveBeenCalledWith(cellule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cellule>>();
      const cellule = { id: 123 };
      jest.spyOn(celluleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cellule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(celluleService.update).toHaveBeenCalledWith(cellule);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
