import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CelluleComponent } from '../list/cellule.component';
import { CelluleDetailComponent } from '../detail/cellule-detail.component';
import { CelluleUpdateComponent } from '../update/cellule-update.component';
import { CelluleRoutingResolveService } from './cellule-routing-resolve.service';

const celluleRoute: Routes = [
  {
    path: '',
    component: CelluleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CelluleDetailComponent,
    resolve: {
      cellule: CelluleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CelluleUpdateComponent,
    resolve: {
      cellule: CelluleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CelluleUpdateComponent,
    resolve: {
      cellule: CelluleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(celluleRoute)],
  exports: [RouterModule],
})
export class CelluleRoutingModule {}
