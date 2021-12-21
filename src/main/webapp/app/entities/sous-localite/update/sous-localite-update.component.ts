import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISousLocalite, SousLocalite } from '../sous-localite.model';
import { SousLocaliteService } from '../service/sous-localite.service';
import { ISection } from 'app/entities/section/section.model';
import { SectionService } from 'app/entities/section/service/section.service';
import { ICellule } from 'app/entities/cellule/cellule.model';
import { CelluleService } from 'app/entities/cellule/service/cellule.service';

@Component({
  selector: 'jhi-sous-localite-update',
  templateUrl: './sous-localite-update.component.html',
})
export class SousLocaliteUpdateComponent implements OnInit {
  isSaving = false;

  sectionsSharedCollection: ISection[] = [];
  cellulesSharedCollection: ICellule[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    section: [],
    cellule: [],
  });

  constructor(
    protected sousLocaliteService: SousLocaliteService,
    protected sectionService: SectionService,
    protected celluleService: CelluleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousLocalite }) => {
      this.updateForm(sousLocalite);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sousLocalite = this.createFromForm();
    if (sousLocalite.id !== undefined) {
      this.subscribeToSaveResponse(this.sousLocaliteService.update(sousLocalite));
    } else {
      this.subscribeToSaveResponse(this.sousLocaliteService.create(sousLocalite));
    }
  }

  trackSectionById(index: number, item: ISection): number {
    return item.id!;
  }

  trackCelluleById(index: number, item: ICellule): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISousLocalite>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(sousLocalite: ISousLocalite): void {
    this.editForm.patchValue({
      id: sousLocalite.id,
      nom: sousLocalite.nom,
      section: sousLocalite.section,
      cellule: sousLocalite.cellule,
    });

    this.sectionsSharedCollection = this.sectionService.addSectionToCollectionIfMissing(
      this.sectionsSharedCollection,
      sousLocalite.section
    );
    this.cellulesSharedCollection = this.celluleService.addCelluleToCollectionIfMissing(
      this.cellulesSharedCollection,
      sousLocalite.cellule
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sectionService
      .query()
      .pipe(map((res: HttpResponse<ISection[]>) => res.body ?? []))
      .pipe(
        map((sections: ISection[]) => this.sectionService.addSectionToCollectionIfMissing(sections, this.editForm.get('section')!.value))
      )
      .subscribe((sections: ISection[]) => (this.sectionsSharedCollection = sections));

    this.celluleService
      .query()
      .pipe(map((res: HttpResponse<ICellule[]>) => res.body ?? []))
      .pipe(
        map((cellules: ICellule[]) => this.celluleService.addCelluleToCollectionIfMissing(cellules, this.editForm.get('cellule')!.value))
      )
      .subscribe((cellules: ICellule[]) => (this.cellulesSharedCollection = cellules));
  }

  protected createFromForm(): ISousLocalite {
    return {
      ...new SousLocalite(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      section: this.editForm.get(['section'])!.value,
      cellule: this.editForm.get(['cellule'])!.value,
    };
  }
}
