import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMembre, Membre } from '../membre.model';
import { MembreService } from '../service/membre.service';
import { Rolemembre } from 'app/entities/enumerations/rolemembre.model';

@Component({
  selector: 'jhi-membre-update',
  templateUrl: './membre-update.component.html',
})
export class MembreUpdateComponent implements OnInit {
  isSaving = false;
  rolemembreValues = Object.keys(Rolemembre);

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    fonctionInterne: [],
    fonctionExterne: [],
    structure: [],
    situationMatrimoniale: [],
    numeroTelephone: [],
    email: [],
  });

  constructor(protected membreService: MembreService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ membre }) => {
      this.updateForm(membre);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const membre = this.createFromForm();
    if (membre.id !== undefined) {
      this.subscribeToSaveResponse(this.membreService.update(membre));
    } else {
      this.subscribeToSaveResponse(this.membreService.create(membre));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMembre>>): void {
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

  protected updateForm(membre: IMembre): void {
    this.editForm.patchValue({
      id: membre.id,
      nom: membre.nom,
      prenom: membre.prenom,
      fonctionInterne: membre.fonctionInterne,
      fonctionExterne: membre.fonctionExterne,
      structure: membre.structure,
      situationMatrimoniale: membre.situationMatrimoniale,
      numeroTelephone: membre.numeroTelephone,
      email: membre.email,
    });
  }

  protected createFromForm(): IMembre {
    return {
      ...new Membre(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      fonctionInterne: this.editForm.get(['fonctionInterne'])!.value,
      fonctionExterne: this.editForm.get(['fonctionExterne'])!.value,
      structure: this.editForm.get(['structure'])!.value,
      situationMatrimoniale: this.editForm.get(['situationMatrimoniale'])!.value,
      numeroTelephone: this.editForm.get(['numeroTelephone'])!.value,
      email: this.editForm.get(['email'])!.value,
    };
  }
}
