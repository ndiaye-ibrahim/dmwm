import { ILocalite } from 'app/entities/localite/localite.model';
import { ISection } from 'app/entities/section/section.model';
import { ICellule } from 'app/entities/cellule/cellule.model';

export interface ISousLocalite {
  id?: number;
  nom?: string | null;
  localites?: ILocalite[] | null;
  section?: ISection | null;
  cellule?: ICellule | null;
}

export class SousLocalite implements ISousLocalite {
  constructor(
    public id?: number,
    public nom?: string | null,
    public localites?: ILocalite[] | null,
    public section?: ISection | null,
    public cellule?: ICellule | null
  ) {}
}

export function getSousLocaliteIdentifier(sousLocalite: ISousLocalite): number | undefined {
  return sousLocalite.id;
}
