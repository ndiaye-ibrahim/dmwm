import { ISousLocalite } from 'app/entities/sous-localite/sous-localite.model';
import { ICellule } from 'app/entities/cellule/cellule.model';

export interface ILocalite {
  id?: number;
  nom?: string | null;
  souslocalite?: ISousLocalite | null;
  cellules?: ICellule | null;
}

export class Localite implements ILocalite {
  constructor(
    public id?: number,
    public nom?: string | null,
    public souslocalite?: ISousLocalite | null,
    public cellules?: ICellule | null
  ) {}
}

export function getLocaliteIdentifier(localite: ILocalite): number | undefined {
  return localite.id;
}
