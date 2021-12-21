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
 * Criteria class for the {@link com.mycompany.myapp.domain.SousLocalite} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.SousLocaliteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sous-localites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SousLocaliteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private LongFilter localitesId;

    private LongFilter sectionId;

    private LongFilter celluleId;

    private Boolean distinct;

    public SousLocaliteCriteria() {}

    public SousLocaliteCriteria(SousLocaliteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.localitesId = other.localitesId == null ? null : other.localitesId.copy();
        this.sectionId = other.sectionId == null ? null : other.sectionId.copy();
        this.celluleId = other.celluleId == null ? null : other.celluleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SousLocaliteCriteria copy() {
        return new SousLocaliteCriteria(this);
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

    public LongFilter getSectionId() {
        return sectionId;
    }

    public LongFilter sectionId() {
        if (sectionId == null) {
            sectionId = new LongFilter();
        }
        return sectionId;
    }

    public void setSectionId(LongFilter sectionId) {
        this.sectionId = sectionId;
    }

    public LongFilter getCelluleId() {
        return celluleId;
    }

    public LongFilter celluleId() {
        if (celluleId == null) {
            celluleId = new LongFilter();
        }
        return celluleId;
    }

    public void setCelluleId(LongFilter celluleId) {
        this.celluleId = celluleId;
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
        final SousLocaliteCriteria that = (SousLocaliteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(localitesId, that.localitesId) &&
            Objects.equals(sectionId, that.sectionId) &&
            Objects.equals(celluleId, that.celluleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, localitesId, sectionId, celluleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousLocaliteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (localitesId != null ? "localitesId=" + localitesId + ", " : "") +
            (sectionId != null ? "sectionId=" + sectionId + ", " : "") +
            (celluleId != null ? "celluleId=" + celluleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
