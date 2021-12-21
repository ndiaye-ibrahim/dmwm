package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Section.
 */
@Entity
@Table(name = "section")
public class Section implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @OneToMany(mappedBy = "section")
    @JsonIgnoreProperties(value = { "localites", "section", "cellule" }, allowSetters = true)
    private Set<SousLocalite> souslocalites = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sections" }, allowSetters = true)
    private Membre membre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Section id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Section nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<SousLocalite> getSouslocalites() {
        return this.souslocalites;
    }

    public void setSouslocalites(Set<SousLocalite> sousLocalites) {
        if (this.souslocalites != null) {
            this.souslocalites.forEach(i -> i.setSection(null));
        }
        if (sousLocalites != null) {
            sousLocalites.forEach(i -> i.setSection(this));
        }
        this.souslocalites = sousLocalites;
    }

    public Section souslocalites(Set<SousLocalite> sousLocalites) {
        this.setSouslocalites(sousLocalites);
        return this;
    }

    public Section addSouslocalite(SousLocalite sousLocalite) {
        this.souslocalites.add(sousLocalite);
        sousLocalite.setSection(this);
        return this;
    }

    public Section removeSouslocalite(SousLocalite sousLocalite) {
        this.souslocalites.remove(sousLocalite);
        sousLocalite.setSection(null);
        return this;
    }

    public Membre getMembre() {
        return this.membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public Section membre(Membre membre) {
        this.setMembre(membre);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        return id != null && id.equals(((Section) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Section{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
