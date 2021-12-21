import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SousLocaliteComponent } from '../list/sous-localite.component';
import { SousLocaliteDetailComponent } from '../detail/sous-localite-detail.component';
import { SousLocaliteUpdateComponent } from '../update/sous-localite-update.component';
import { SousLocaliteRoutingResolveService } from './sous-localite-routing-resolve.service';

const sousLocaliteRoute: Routes = [
  {
    path: '',
    component: SousLocaliteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SousLocaliteDetailComponent,
    resolve: {
      sousLocalite: SousLocaliteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SousLocaliteUpdateComponent,
    resolve: {
      sousLocalite: SousLocaliteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SousLocaliteUpdateComponent,
    resolve: {
      sousLocalite: SousLocaliteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sousLocaliteRoute)],
  exports: [RouterModule],
})
export class SousLocaliteRoutingModule {}
