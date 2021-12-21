package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Typecellule;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Cellule.
 */
@Entity
@Table(name = "cellule")
public class Cellule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nom")
    private Typecellule nom;

    @OneToMany(mappedBy = "cellules")
    @JsonIgnoreProperties(value = { "souslocalite", "cellules" }, allowSetters = true)
    private Set<Localite> localites = new HashSet<>();

    @OneToMany(mappedBy = "cellule")
    @JsonIgnoreProperties(value = { "localites", "section", "cellule" }, allowSetters = true)
    private Set<SousLocalite> souslocalites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cellule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Typecellule getNom() {
        return this.nom;
    }

    public Cellule nom(Typecellule nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(Typecellule nom) {
        this.nom = nom;
    }

    public Set<Localite> getLocalites() {
        return this.localites;
    }

    public void setLocalites(Set<Localite> localites) {
        if (this.localites != null) {
            this.localites.forEach(i -> i.setCellules(null));
        }
        if (localites != null) {
            localites.forEach(i -> i.setCellules(this));
        }
        this.localites = localites;
    }

    public Cellule localites(Set<Localite> localites) {
        this.setLocalites(localites);
        return this;
    }

    public Cellule addLocalites(Localite localite) {
        this.localites.add(localite);
        localite.setCellules(this);
        return this;
    }

    public Cellule removeLocalites(Localite localite) {
        this.localites.remove(localite);
        localite.setCellules(null);
        return this;
    }

    public Set<SousLocalite> getSouslocalites() {
        return this.souslocalites;
    }

    public void setSouslocalites(Set<SousLocalite> sousLocalites) {
        if (this.souslocalites != null) {
            this.souslocalites.forEach(i -> i.setCellule(null));
        }
        if (sousLocalites != null) {
            sousLocalites.forEach(i -> i.setCellule(this));
        }
        this.souslocalites = sousLocalites;
    }

    public Cellule souslocalites(Set<SousLocalite> sousLocalites) {
        this.setSouslocalites(sousLocalites);
        return this;
    }

    public Cellule addSouslocalite(SousLocalite sousLocalite) {
        this.souslocalites.add(sousLocalite);
        sousLocalite.setCellule(this);
        return this;
    }

    public Cellule removeSouslocalite(SousLocalite sousLocalite) {
        this.souslocalites.remove(sousLocalite);
        sousLocalite.setCellule(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cellule)) {
            return false;
        }
        return id != null && id.equals(((Cellule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cellule{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
