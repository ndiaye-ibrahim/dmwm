import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICellule } from '../cellule.model';

@Component({
  selector: 'jhi-cellule-detail',
  templateUrl: './cellule-detail.component.html',
})
export class CelluleDetailComponent implements OnInit {
  cellule: ICellule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cellule }) => {
      this.cellule = cellule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
