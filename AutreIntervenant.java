package metier.modele;

import javax.persistence.Entity;

@Entity
public class AutreIntervenant extends Intervenant {

    private String activite;

    public AutreIntervenant(String nom, String prenom, String telephone, String mdp, Integer niveauMin, Integer niveauMax, String activite) {
        super(nom, prenom, telephone, mdp, niveauMin, niveauMax);
        this.activite = activite;
    }

    public AutreIntervenant() {

    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    @Override
    public String toString() {
        return "Enseignant{" + super.toString() + ", Activite=" + activite + '}';
    }

}
