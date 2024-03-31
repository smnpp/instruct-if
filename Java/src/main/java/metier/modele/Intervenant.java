package metier.modele;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.List;

@Entity
public class Intervenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    @Column(unique = true)
    private String telephone;
    private String motDePasse;
    private List<Integer> niveau;
    private Boolean disponibilite;
    private Integer nbIntervention;

    public Intervenant(){
        
    }

    public Intervenant(String nom, String prenom, String telephone, String motDePasse,List<Integer> niveau) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.niveau = niveau;
        this.disponibilite = true;
        this.motDePasse = motDePasse;
        this.nbIntervention = 0;
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

    public List<Integer> getNiveau() {
        return niveau;
    }

    public void setNiveau(List<Integer> niveau) {
        this.niveau = niveau;
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
        return "Intervenant{" + "id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", telephone=" + telephone + ", motDePasse=" + motDePasse + ", niveau=" + niveau + ", disponibilite=" + disponibilite + '}';
    }
}
