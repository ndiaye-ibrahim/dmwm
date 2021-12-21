import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICellule } from '../cellule.model';
import { CelluleService } from '../service/cellule.service';

@Component({
  templateUrl: './cellule-delete-dialog.component.html',
})
export class CelluleDeleteDialogComponent {
  cellule?: ICellule;

  constructor(protected celluleService: CelluleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.celluleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
