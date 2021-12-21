package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Rolemembre;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Membre.
 */
@Entity
@Table(name = "membre")
public class Membre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Enumerated(EnumType.STRING)
    @Column(name = "fonction_interne")
    private Rolemembre fonctionInterne;

    @Column(name = "fonction_externe")
    private String fonctionExterne;

    @Column(name = "structure")
    private String structure;

    @Column(name = "situation_matrimoniale")
    private String situationMatrimoniale;

    @Column(name = "numero_telephone")
    private String numeroTelephone;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "membre")
    @JsonIgnoreProperties(value = { "souslocalites", "membre" }, allowSetters = true)
    private Set<Section> sections = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Membre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Membre nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Membre prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Rolemembre getFonctionInterne() {
        return this.fonctionInterne;
    }

    public Membre fonctionInterne(Rolemembre fonctionInterne) {
        this.setFonctionInterne(fonctionInterne);
        return this;
    }

    public void setFonctionInterne(Rolemembre fonctionInterne) {
        this.fonctionInterne = fonctionInterne;
    }

    public String getFonctionExterne() {
        return this.fonctionExterne;
    }

    public Membre fonctionExterne(String fonctionExterne) {
        this.setFonctionExterne(fonctionExterne);
        return this;
    }

    public void setFonctionExterne(String fonctionExterne) {
        this.fonctionExterne = fonctionExterne;
    }

    public String getStructure() {
        return this.structure;
    }

    public Membre structure(String structure) {
        this.setStructure(structure);
        return this;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getSituationMatrimoniale() {
        return this.situationMatrimoniale;
    }

    public Membre situationMatrimoniale(String situationMatrimoniale) {
        this.setSituationMatrimoniale(situationMatrimoniale);
        return this;
    }

    public void setSituationMatrimoniale(String situationMatrimoniale) {
        this.situationMatrimoniale = situationMatrimoniale;
    }

    public String getNumeroTelephone() {
        return this.numeroTelephone;
    }

    public Membre numeroTelephone(String numeroTelephone) {
        this.setNumeroTelephone(numeroTelephone);
        return this;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public String getEmail() {
        return this.email;
    }

    public Membre email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Section> getSections() {
        return this.sections;
    }

    public void setSections(Set<Section> sections) {
        if (this.sections != null) {
            this.sections.forEach(i -> i.setMembre(null));
        }
        if (sections != null) {
            sections.forEach(i -> i.setMembre(this));
        }
        this.sections = sections;
    }

    public Membre sections(Set<Section> sections) {
        this.setSections(sections);
        return this;
    }

    public Membre addSections(Section section) {
        this.sections.add(section);
        section.setMembre(this);
        return this;
    }

    public Membre removeSections(Section section) {
        this.sections.remove(section);
        section.setMembre(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Membre)) {
            return false;
        }
        return id != null && id.equals(((Membre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Membre{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", fonctionInterne='" + getFonctionInterne() + "'" +
            ", fonctionExterne='" + getFonctionExterne() + "'" +
            ", structure='" + getStructure() + "'" +
            ", situationMatrimoniale='" + getSituationMatrimoniale() + "'" +
            ", numeroTelephone='" + getNumeroTelephone() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
