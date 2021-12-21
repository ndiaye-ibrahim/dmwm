import { ILocalite } from 'app/entities/localite/localite.model';
import { ISousLocalite } from 'app/entities/sous-localite/sous-localite.model';
import { Typecellule } from 'app/entities/enumerations/typecellule.model';

export interface ICellule {
  id?: number;
  nom?: Typecellule | null;
  localites?: ILocalite[] | null;
  souslocalites?: ISousLocalite[] | null;
}

export class Cellule implements ICellule {
  constructor(
    public id?: number,
    public nom?: Typecellule | null,
    public localites?: ILocalite[] | null,
    public souslocalites?: ISousLocalite[] | null
  ) {}
}

export function getCelluleIdentifier(cellule: ICellule): number | undefined {
  return cellule.id;
}
