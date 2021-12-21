package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Localite.
 */
@Entity
@Table(name = "localite")
public class Localite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @ManyToOne
    @JsonIgnoreProperties(value = { "localites", "section", "cellule" }, allowSetters = true)
    private SousLocalite souslocalite;

    @ManyToOne
    @JsonIgnoreProperties(value = { "localites", "souslocalites" }, allowSetters = true)
    private Cellule cellules;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Localite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Localite nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public SousLocalite getSouslocalite() {
        return this.souslocalite;
    }

    public void setSouslocalite(SousLocalite sousLocalite) {
        this.souslocalite = sousLocalite;
    }

    public Localite souslocalite(SousLocalite sousLocalite) {
        this.setSouslocalite(sousLocalite);
        return this;
    }

    public Cellule getCellules() {
        return this.cellules;
    }

    public void setCellules(Cellule cellule) {
        this.cellules = cellule;
    }

    public Localite cellules(Cellule cellule) {
        this.setCellules(cellule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Localite)) {
            return false;
        }
        return id != null && id.equals(((Localite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Localite{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
