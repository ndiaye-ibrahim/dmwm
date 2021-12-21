package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A SousLocalite.
 */
@Entity
@Table(name = "sous_localite")
public class SousLocalite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @OneToMany(mappedBy = "souslocalite")
    @JsonIgnoreProperties(value = { "souslocalite", "cellules" }, allowSetters = true)
    private Set<Localite> localites = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "souslocalites", "membre" }, allowSetters = true)
    private Section section;

    @ManyToOne
    @JsonIgnoreProperties(value = { "localites", "souslocalites" }, allowSetters = true)
    private Cellule cellule;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SousLocalite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public SousLocalite nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Localite> getLocalites() {
        return this.localites;
    }

    public void setLocalites(Set<Localite> localites) {
        if (this.localites != null) {
            this.localites.forEach(i -> i.setSouslocalite(null));
        }
        if (localites != null) {
            localites.forEach(i -> i.setSouslocalite(this));
        }
        this.localites = localites;
    }

    public SousLocalite localites(Set<Localite> localites) {
        this.setLocalites(localites);
        return this;
    }

    public SousLocalite addLocalites(Localite localite) {
        this.localites.add(localite);
        localite.setSouslocalite(this);
        return this;
    }

    public SousLocalite removeLocalites(Localite localite) {
        this.localites.remove(localite);
        localite.setSouslocalite(null);
        return this;
    }

    public Section getSection() {
        return this.section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public SousLocalite section(Section section) {
        this.setSection(section);
        return this;
    }

    public Cellule getCellule() {
        return this.cellule;
    }

    public void setCellule(Cellule cellule) {
        this.cellule = cellule;
    }

    public SousLocalite cellule(Cellule cellule) {
        this.setCellule(cellule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SousLocalite)) {
            return false;
        }
        return id != null && id.equals(((SousLocalite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousLocalite{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
