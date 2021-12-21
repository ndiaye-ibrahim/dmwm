import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISection, Section } from '../section.model';
import { SectionService } from '../service/section.service';
import { IMembre } from 'app/entities/membre/membre.model';
import { MembreService } from 'app/entities/membre/service/membre.service';

@Component({
  selector: 'jhi-section-update',
  templateUrl: './section-update.component.html',
})
export class SectionUpdateComponent implements OnInit {
  isSaving = false;

  membresSharedCollection: IMembre[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    membre: [],
  });

  constructor(
    protected sectionService: SectionService,
    protected membreService: MembreService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ section }) => {
      this.updateForm(section);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const section = this.createFromForm();
    if (section.id !== undefined) {
      this.subscribeToSaveResponse(this.sectionService.update(section));
    } else {
      this.subscribeToSaveResponse(this.sectionService.create(section));
    }
  }

  trackMembreById(index: number, item: IMembre): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISection>>): void {
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

  protected updateForm(section: ISection): void {
    this.editForm.patchValue({
      id: section.id,
      nom: section.nom,
      membre: section.membre,
    });

    this.membresSharedCollection = this.membreService.addMembreToCollectionIfMissing(this.membresSharedCollection, section.membre);
  }

  protected loadRelationshipsOptions(): void {
    this.membreService
      .query()
      .pipe(map((res: HttpResponse<IMembre[]>) => res.body ?? []))
      .pipe(map((membres: IMembre[]) => this.membreService.addMembreToCollectionIfMissing(membres, this.editForm.get('membre')!.value)))
      .subscribe((membres: IMembre[]) => (this.membresSharedCollection = membres));
  }

  protected createFromForm(): ISection {
    return {
      ...new Section(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      membre: this.editForm.get(['membre'])!.value,
    };
  }
}
