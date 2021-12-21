import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISousLocalite } from '../sous-localite.model';
import { SousLocaliteService } from '../service/sous-localite.service';

@Component({
  templateUrl: './sous-localite-delete-dialog.component.html',
})
export class SousLocaliteDeleteDialogComponent {
  sousLocalite?: ISousLocalite;

  constructor(protected sousLocaliteService: SousLocaliteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sousLocaliteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
