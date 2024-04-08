package metier.modele;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Matiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nom;

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Matiere() {

    }

    public Matiere(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Matiere{" + "id=" + id + ", nom=" + nom + '}';
    }
    /*
    // On garde la collection des matières valides, mais cela ne sera pas stocké en base
    // Il s'agit juste d'une aide au niveau du modèle pour vérifier les valeurs valides
    private static final Set<String> MATIERES_VALIDES;

    static {
        Set<String> aSet = new HashSet<>();
        aSet.add("Mathématiques");
        aSet.add("Physique-Chimie");
        aSet.add("SVT");
        aSet.add("Histoire-Géographie");
        aSet.add("Français");
        aSet.add("Anglais");
        aSet.add("Espagnol");
        aSet.add("Philosophie");
        MATIERES_VALIDES = Collections.unmodifiableSet(aSet);
    }

    public Matiere() {}

    public Matiere(String libelle) throws IllegalArgumentException {
        if (!MATIERES_VALIDES.contains(libelle)) {
            throw new IllegalArgumentException("Libellé de matière non valide.");
        }
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        if (!MATIERES_VALIDES.contains(libelle)) {
            throw new IllegalArgumentException("Libellé de matière non valide.");
        }
        this.libelle = libelle;
    }

    public static List<Matiere> obtenirMatieresValides() {
        List<Matiere> matieres = new ArrayList<>();
        for (String libelle : MATIERES_VALIDES) {
            matieres.add(new Matiere(libelle));
        }
        return matieres;
    }
     */
}
