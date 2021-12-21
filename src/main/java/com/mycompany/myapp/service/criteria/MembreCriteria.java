package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.Rolemembre;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Membre} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.MembreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /membres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MembreCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Rolemembre
     */
    public static class RolemembreFilter extends Filter<Rolemembre> {

        public RolemembreFilter() {}

        public RolemembreFilter(RolemembreFilter filter) {
            super(filter);
        }

        @Override
        public RolemembreFilter copy() {
            return new RolemembreFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter prenom;

    private RolemembreFilter fonctionInterne;

    private StringFilter fonctionExterne;

    private StringFilter structure;

    private StringFilter situationMatrimoniale;

    private StringFilter numeroTelephone;

    private StringFilter email;

    private LongFilter sectionsId;

    private Boolean distinct;

    public MembreCriteria() {}

    public MembreCriteria(MembreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.prenom = other.prenom == null ? null : other.prenom.copy();
        this.fonctionInterne = other.fonctionInterne == null ? null : other.fonctionInterne.copy();
        this.fonctionExterne = other.fonctionExterne == null ? null : other.fonctionExterne.copy();
        this.structure = other.structure == null ? null : other.structure.copy();
        this.situationMatrimoniale = other.situationMatrimoniale == null ? null : other.situationMatrimoniale.copy();
        this.numeroTelephone = other.numeroTelephone == null ? null : other.numeroTelephone.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.sectionsId = other.sectionsId == null ? null : other.sectionsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MembreCriteria copy() {
        return new MembreCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNom() {
        return nom;
    }

    public StringFilter nom() {
        if (nom == null) {
            nom = new StringFilter();
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getPrenom() {
        return prenom;
    }

    public StringFilter prenom() {
        if (prenom == null) {
            prenom = new StringFilter();
        }
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
    }

    public RolemembreFilter getFonctionInterne() {
        return fonctionInterne;
    }

    public RolemembreFilter fonctionInterne() {
        if (fonctionInterne == null) {
            fonctionInterne = new RolemembreFilter();
        }
        return fonctionInterne;
    }

    public void setFonctionInterne(RolemembreFilter fonctionInterne) {
        this.fonctionInterne = fonctionInterne;
    }

    public StringFilter getFonctionExterne() {
        return fonctionExterne;
    }

    public StringFilter fonctionExterne() {
        if (fonctionExterne == null) {
            fonctionExterne = new StringFilter();
        }
        return fonctionExterne;
    }

    public void setFonctionExterne(StringFilter fonctionExterne) {
        this.fonctionExterne = fonctionExterne;
    }

    public StringFilter getStructure() {
        return structure;
    }

    public StringFilter structure() {
        if (structure == null) {
            structure = new StringFilter();
        }
        return structure;
    }

    public void setStructure(StringFilter structure) {
        this.structure = structure;
    }

    public StringFilter getSituationMatrimoniale() {
        return situationMatrimoniale;
    }

    public StringFilter situationMatrimoniale() {
        if (situationMatrimoniale == null) {
            situationMatrimoniale = new StringFilter();
        }
        return situationMatrimoniale;
    }

    public void setSituationMatrimoniale(StringFilter situationMatrimoniale) {
        this.situationMatrimoniale = situationMatrimoniale;
    }

    public StringFilter getNumeroTelephone() {
        return numeroTelephone;
    }

    public StringFilter numeroTelephone() {
        if (numeroTelephone == null) {
            numeroTelephone = new StringFilter();
        }
        return numeroTelephone;
    }

    public void setNumeroTelephone(StringFilter numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LongFilter getSectionsId() {
        return sectionsId;
    }

    public LongFilter sectionsId() {
        if (sectionsId == null) {
            sectionsId = new LongFilter();
        }
        return sectionsId;
    }

    public void setSectionsId(LongFilter sectionsId) {
        this.sectionsId = sectionsId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MembreCriteria that = (MembreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(fonctionInterne, that.fonctionInterne) &&
            Objects.equals(fonctionExterne, that.fonctionExterne) &&
            Objects.equals(structure, that.structure) &&
            Objects.equals(situationMatrimoniale, that.situationMatrimoniale) &&
            Objects.equals(numeroTelephone, that.numeroTelephone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(sectionsId, that.sectionsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nom,
            prenom,
            fonctionInterne,
            fonctionExterne,
            structure,
            situationMatrimoniale,
            numeroTelephone,
            email,
            sectionsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembreCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (prenom != null ? "prenom=" + prenom + ", " : "") +
            (fonctionInterne != null ? "fonctionInterne=" + fonctionInterne + ", " : "") +
            (fonctionExterne != null ? "fonctionExterne=" + fonctionExterne + ", " : "") +
            (structure != null ? "structure=" + structure + ", " : "") +
            (situationMatrimoniale != null ? "situationMatrimoniale=" + situationMatrimoniale + ", " : "") +
            (numeroTelephone != null ? "numeroTelephone=" + numeroTelephone + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (sectionsId != null ? "sectionsId=" + sectionsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
