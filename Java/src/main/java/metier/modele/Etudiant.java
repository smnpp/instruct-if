package metier.modele;

import javax.persistence.Entity;

@Entity
public class Etudiant extends Intervenant {

    private String universite;
    private String specialite;

    public Etudiant() {

    }

    public Etudiant(String nom, String prenom, String telephone, String mdp, Integer niveauMin, Integer niveauMax, String universite, String specialite) {
        super(nom, prenom, telephone, mdp, niveauMin, niveauMax);
        this.universite = universite;
        this.specialite = specialite;
    }

    public String getUniversite() {
        return universite;
    }

    public void setUniversite(String universite) {
        this.universite = universite;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    @Override
    public String toString() {
        return "Etudiant{" + super.toString() + ", universite=" + universite + ", specialite=" + specialite + '}';
    }

}
