package metier.modele;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class Etudiant extends Intervenant {
    private String universite;
    private String specialite;

    public Etudiant(){
        
    }

    public Etudiant(String nom, String prenom, String telephone, String mdp, List<Integer> niveau, String universite, String specialite) {
        super(nom, prenom, telephone, mdp, niveau);
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
