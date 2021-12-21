import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICellule, Cellule } from '../cellule.model';
import { CelluleService } from '../service/cellule.service';
import { Typecellule } from 'app/entities/enumerations/typecellule.model';

@Component({
  selector: 'jhi-cellule-update',
  templateUrl: './cellule-update.component.html',
})
export class CelluleUpdateComponent implements OnInit {
  isSaving = false;
  typecelluleValues = Object.keys(Typecellule);

  editForm = this.fb.group({
    id: [],
    nom: [],
  });

  constructor(protected celluleService: CelluleService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cellule }) => {
      this.updateForm(cellule);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cellule = this.createFromForm();
    if (cellule.id !== undefined) {
      this.subscribeToSaveResponse(this.celluleService.update(cellule));
    } else {
      this.subscribeToSaveResponse(this.celluleService.create(cellule));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICellule>>): void {
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

  protected updateForm(cellule: ICellule): void {
    this.editForm.patchValue({
      id: cellule.id,
      nom: cellule.nom,
    });
  }

  protected createFromForm(): ICellule {
    return {
      ...new Cellule(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
    };
  }
}
