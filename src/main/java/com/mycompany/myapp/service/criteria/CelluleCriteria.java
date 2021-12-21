package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.Typecellule;
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
 * Criteria class for the {@link com.mycompany.myapp.domain.Cellule} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CelluleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cellules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CelluleCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Typecellule
     */
    public static class TypecelluleFilter extends Filter<Typecellule> {

        public TypecelluleFilter() {}

        public TypecelluleFilter(TypecelluleFilter filter) {
            super(filter);
        }

        @Override
        public TypecelluleFilter copy() {
            return new TypecelluleFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TypecelluleFilter nom;

    private LongFilter localitesId;

    private LongFilter souslocaliteId;

    private Boolean distinct;

    public CelluleCriteria() {}

    public CelluleCriteria(CelluleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.localitesId = other.localitesId == null ? null : other.localitesId.copy();
        this.souslocaliteId = other.souslocaliteId == null ? null : other.souslocaliteId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CelluleCriteria copy() {
        return new CelluleCriteria(this);
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

    public TypecelluleFilter getNom() {
        return nom;
    }

    public TypecelluleFilter nom() {
        if (nom == null) {
            nom = new TypecelluleFilter();
        }
        return nom;
    }

    public void setNom(TypecelluleFilter nom) {
        this.nom = nom;
    }

    public LongFilter getLocalitesId() {
        return localitesId;
    }

    public LongFilter localitesId() {
        if (localitesId == null) {
            localitesId = new LongFilter();
        }
        return localitesId;
    }

    public void setLocalitesId(LongFilter localitesId) {
        this.localitesId = localitesId;
    }

    public LongFilter getSouslocaliteId() {
        return souslocaliteId;
    }

    public LongFilter souslocaliteId() {
        if (souslocaliteId == null) {
            souslocaliteId = new LongFilter();
        }
        return souslocaliteId;
    }

    public void setSouslocaliteId(LongFilter souslocaliteId) {
        this.souslocaliteId = souslocaliteId;
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
        final CelluleCriteria that = (CelluleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(localitesId, that.localitesId) &&
            Objects.equals(souslocaliteId, that.souslocaliteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, localitesId, souslocaliteId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CelluleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (localitesId != null ? "localitesId=" + localitesId + ", " : "") +
            (souslocaliteId != null ? "souslocaliteId=" + souslocaliteId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
