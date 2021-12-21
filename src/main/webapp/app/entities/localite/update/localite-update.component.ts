import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILocalite, Localite } from '../localite.model';
import { LocaliteService } from '../service/localite.service';
import { ISousLocalite } from 'app/entities/sous-localite/sous-localite.model';
import { SousLocaliteService } from 'app/entities/sous-localite/service/sous-localite.service';
import { ICellule } from 'app/entities/cellule/cellule.model';
import { CelluleService } from 'app/entities/cellule/service/cellule.service';

@Component({
  selector: 'jhi-localite-update',
  templateUrl: './localite-update.component.html',
})
export class LocaliteUpdateComponent implements OnInit {
  isSaving = false;

  sousLocalitesSharedCollection: ISousLocalite[] = [];
  cellulesSharedCollection: ICellule[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    souslocalite: [],
    cellules: [],
  });

  constructor(
    protected localiteService: LocaliteService,
    protected sousLocaliteService: SousLocaliteService,
    protected celluleService: CelluleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ localite }) => {
      this.updateForm(localite);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const localite = this.createFromForm();
    if (localite.id !== undefined) {
      this.subscribeToSaveResponse(this.localiteService.update(localite));
    } else {
      this.subscribeToSaveResponse(this.localiteService.create(localite));
    }
  }

  trackSousLocaliteById(index: number, item: ISousLocalite): number {
    return item.id!;
  }

  trackCelluleById(index: number, item: ICellule): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocalite>>): void {
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

  protected updateForm(localite: ILocalite): void {
    this.editForm.patchValue({
      id: localite.id,
      nom: localite.nom,
      souslocalite: localite.souslocalite,
      cellules: localite.cellules,
    });

    this.sousLocalitesSharedCollection = this.sousLocaliteService.addSousLocaliteToCollectionIfMissing(
      this.sousLocalitesSharedCollection,
      localite.souslocalite
    );
    this.cellulesSharedCollection = this.celluleService.addCelluleToCollectionIfMissing(this.cellulesSharedCollection, localite.cellules);
  }

  protected loadRelationshipsOptions(): void {
    this.sousLocaliteService
      .query()
      .pipe(map((res: HttpResponse<ISousLocalite[]>) => res.body ?? []))
      .pipe(
        map((sousLocalites: ISousLocalite[]) =>
          this.sousLocaliteService.addSousLocaliteToCollectionIfMissing(sousLocalites, this.editForm.get('souslocalite')!.value)
        )
      )
      .subscribe((sousLocalites: ISousLocalite[]) => (this.sousLocalitesSharedCollection = sousLocalites));

    this.celluleService
      .query()
      .pipe(map((res: HttpResponse<ICellule[]>) => res.body ?? []))
      .pipe(
        map((cellules: ICellule[]) => this.celluleService.addCelluleToCollectionIfMissing(cellules, this.editForm.get('cellules')!.value))
      )
      .subscribe((cellules: ICellule[]) => (this.cellulesSharedCollection = cellules));
  }

  protected createFromForm(): ILocalite {
    return {
      ...new Localite(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      souslocalite: this.editForm.get(['souslocalite'])!.value,
      cellules: this.editForm.get(['cellules'])!.value,
    };
  }
}
