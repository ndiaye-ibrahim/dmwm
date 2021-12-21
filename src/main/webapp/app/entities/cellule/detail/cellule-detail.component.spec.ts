import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CelluleDetailComponent } from './cellule-detail.component';

describe('Cellule Management Detail Component', () => {
  let comp: CelluleDetailComponent;
  let fixture: ComponentFixture<CelluleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CelluleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cellule: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CelluleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CelluleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cellule on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cellule).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
