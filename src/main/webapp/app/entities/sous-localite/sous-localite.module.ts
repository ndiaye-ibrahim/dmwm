import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SousLocaliteComponent } from './list/sous-localite.component';
import { SousLocaliteDetailComponent } from './detail/sous-localite-detail.component';
import { SousLocaliteUpdateComponent } from './update/sous-localite-update.component';
import { SousLocaliteDeleteDialogComponent } from './delete/sous-localite-delete-dialog.component';
import { SousLocaliteRoutingModule } from './route/sous-localite-routing.module';

@NgModule({
  imports: [SharedModule, SousLocaliteRoutingModule],
  declarations: [SousLocaliteComponent, SousLocaliteDetailComponent, SousLocaliteUpdateComponent, SousLocaliteDeleteDialogComponent],
  entryComponents: [SousLocaliteDeleteDialogComponent],
})
export class SousLocaliteModule {}
