import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'localite',
        data: { pageTitle: 'Localites' },
        loadChildren: () => import('./localite/localite.module').then(m => m.LocaliteModule),
      },
      {
        path: 'sous-localite',
        data: { pageTitle: 'SousLocalites' },
        loadChildren: () => import('./sous-localite/sous-localite.module').then(m => m.SousLocaliteModule),
      },
      {
        path: 'section',
        data: { pageTitle: 'Sections' },
        loadChildren: () => import('./section/section.module').then(m => m.SectionModule),
      },
      {
        path: 'membre',
        data: { pageTitle: 'Membres' },
        loadChildren: () => import('./membre/membre.module').then(m => m.MembreModule),
      },
      {
        path: 'cellule',
        data: { pageTitle: 'Cellules' },
        loadChildren: () => import('./cellule/cellule.module').then(m => m.CelluleModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
