package com.mycompany.myapp.service.criteria;

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
 * Criteria class for the {@link com.mycompany.myapp.domain.Localite} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.LocaliteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /localites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LocaliteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private LongFilter souslocaliteId;

    private LongFilter cellulesId;

    private Boolean distinct;

    public LocaliteCriteria() {}

    public LocaliteCriteria(LocaliteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.souslocaliteId = other.souslocaliteId == null ? null : other.souslocaliteId.copy();
        this.cellulesId = other.cellulesId == null ? null : other.cellulesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LocaliteCriteria copy() {
        return new LocaliteCriteria(this);
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

    public LongFilter getCellulesId() {
        return cellulesId;
    }

    public LongFilter cellulesId() {
        if (cellulesId == null) {
            cellulesId = new LongFilter();
        }
        return cellulesId;
    }

    public void setCellulesId(LongFilter cellulesId) {
        this.cellulesId = cellulesId;
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
        final LocaliteCriteria that = (LocaliteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(souslocaliteId, that.souslocaliteId) &&
            Objects.equals(cellulesId, that.cellulesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, souslocaliteId, cellulesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocaliteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (souslocaliteId != null ? "souslocaliteId=" + souslocaliteId + ", " : "") +
            (cellulesId != null ? "cellulesId=" + cellulesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
