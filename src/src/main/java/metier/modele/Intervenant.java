package metier.modele;

import javax.persistence.Column;
import javax.persistence.InheritanceType;
import javax.persistence.Inheritance;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Intervenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    @Column(unique = true)
    private String telephone;
    private String motDePasse;
    private Integer niveauMin;
    private Integer niveauMax;
    private Boolean disponibilite;
    private Integer nbIntervention;

    public Intervenant() {

    }

    public Intervenant(String nom, String prenom, String telephone, String motDePasse, Integer niveauMin, Integer niveauMax) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.niveauMin = niveauMin;
        this.niveauMax = niveauMax;
        this.disponibilite = true;
        this.motDePasse = motDePasse;
        this.nbIntervention = 0;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getNiveauMin() {
        return niveauMin;
    }

    public void setNiveauMin(Integer niveauMin) {
        this.niveauMin = niveauMin;
    }

    public Integer getNiveauMax() {
        return niveauMax;
    }

    public void setNiveauMax(Integer niveauMax) {
        this.niveauMax = niveauMax;
    }

    public Boolean getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(Boolean disponibilite) {
        this.disponibilite = disponibilite;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Integer getNbIntervention() {
        return nbIntervention;
    }

    public void setNbIntervention(Integer nbIntervention) {
        this.nbIntervention = nbIntervention;
    }


    @Override
    public String toString() {
        return "Intervenant{" + "id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", telephone=" + telephone + ", motDePasse=" + motDePasse + ", niveauMin=" + niveauMin + ", niveauMax=" + niveauMax + ", disponibilite=" + disponibilite + '}';
    }
}
