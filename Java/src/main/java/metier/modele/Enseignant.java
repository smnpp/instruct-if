package metier.modele;

import java.util.List;

public class Enseignant extends Intervenant {
    private String typeEtablissement;
    public Enseignant() {
        // Default constructor
    }
    
    public Enseignant(String nom, String prenom, String telephone, String mdp, List<Integer> niveau, String typeEtablissement) {
        super(nom, prenom, telephone, mdp, niveau);
        this.typeEtablissement = typeEtablissement;
    }
    
    public String getTypeEtablissement() {
        return typeEtablissement;
    }
    
    public void setTypeEtablissement(String typeEtablissement) {
        this.typeEtablissement = typeEtablissement;
    }

    @Override
    public String toString() {
        return "Enseignant{" + super.toString() + ", typeEtablissement=" + typeEtablissement + '}';
    }
    
}
