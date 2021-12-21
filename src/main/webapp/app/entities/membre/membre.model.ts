import { ISection } from 'app/entities/section/section.model';
import { Rolemembre } from 'app/entities/enumerations/rolemembre.model';

export interface IMembre {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  fonctionInterne?: Rolemembre | null;
  fonctionExterne?: string | null;
  structure?: string | null;
  situationMatrimoniale?: string | null;
  numeroTelephone?: string | null;
  email?: string | null;
  sections?: ISection[] | null;
}

export class Membre implements IMembre {
  constructor(
    public id?: number,
    public nom?: string | null,
    public prenom?: string | null,
    public fonctionInterne?: Rolemembre | null,
    public fonctionExterne?: string | null,
    public structure?: string | null,
    public situationMatrimoniale?: string | null,
    public numeroTelephone?: string | null,
    public email?: string | null,
    public sections?: ISection[] | null
  ) {}
}

export function getMembreIdentifier(membre: IMembre): number | undefined {
  return membre.id;
}
