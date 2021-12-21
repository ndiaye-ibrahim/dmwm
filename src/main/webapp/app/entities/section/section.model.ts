import { ISousLocalite } from 'app/entities/sous-localite/sous-localite.model';
import { IMembre } from 'app/entities/membre/membre.model';

export interface ISection {
  id?: number;
  nom?: string | null;
  souslocalites?: ISousLocalite[] | null;
  membre?: IMembre | null;
}

export class Section implements ISection {
  constructor(
    public id?: number,
    public nom?: string | null,
    public souslocalites?: ISousLocalite[] | null,
    public membre?: IMembre | null
  ) {}
}

export function getSectionIdentifier(section: ISection): number | undefined {
  return section.id;
}
