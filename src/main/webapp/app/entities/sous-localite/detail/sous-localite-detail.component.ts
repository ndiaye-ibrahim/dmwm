import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISousLocalite } from '../sous-localite.model';

@Component({
  selector: 'jhi-sous-localite-detail',
  templateUrl: './sous-localite-detail.component.html',
})
export class SousLocaliteDetailComponent implements OnInit {
  sousLocalite: ISousLocalite | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousLocalite }) => {
      this.sousLocalite = sousLocalite;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
