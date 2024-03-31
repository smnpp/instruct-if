package metier.modele;

import java.util.List;

public class Intervenant {
    private String nom;
    private String prenom;
    private String telephone;
    private List<Integer> niveau;
    private Boolean disponibilite;

    public Intervenant(){
        
    }

    public Intervenant(String nom, String prenom, String telephone, List<Integer> niveau, Boolean disponibilite) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.niveau = niveau;
        this.disponibilite = disponibilite;
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

    @Override
    public String toString() {
        return "nom=" + nom + ", prenom=" + prenom + ", telephone=" + telephone + ", niveau=" + niveau + ", disponibilite=" + disponibilite;
    }
}
