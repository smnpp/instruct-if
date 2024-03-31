package metier.modele;

import java.util.List;

public class AutreIntervenant extends Intervenant{
    private String activite;

    public AutreIntervenant(String nom, String prenom, String telephone, String mdp, List<Integer> niveau, String activite){
        super(nom, prenom, telephone, mdp, niveau);
        this.activite = activite;
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
