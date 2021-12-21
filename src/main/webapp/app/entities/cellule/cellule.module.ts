import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CelluleComponent } from './list/cellule.component';
import { CelluleDetailComponent } from './detail/cellule-detail.component';
import { CelluleUpdateComponent } from './update/cellule-update.component';
import { CelluleDeleteDialogComponent } from './delete/cellule-delete-dialog.component';
import { CelluleRoutingModule } from './route/cellule-routing.module';

@NgModule({
  imports: [SharedModule, CelluleRoutingModule],
  declarations: [CelluleComponent, CelluleDetailComponent, CelluleUpdateComponent, CelluleDeleteDialogComponent],
  entryComponents: [CelluleDeleteDialogComponent],
})
export class CelluleModule {}
