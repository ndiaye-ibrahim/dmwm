import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SousLocaliteDetailComponent } from './sous-localite-detail.component';

describe('SousLocalite Management Detail Component', () => {
  let comp: SousLocaliteDetailComponent;
  let fixture: ComponentFixture<SousLocaliteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SousLocaliteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sousLocalite: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SousLocaliteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SousLocaliteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sousLocalite on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sousLocalite).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
