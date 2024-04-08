package metier.modele;

//Pour transformer Eleve en une entité JPA, il faut annoter la classe avec @Entity et utiliser 
//@Id ainsi que @GeneratedValue pour l'identifiant : 
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Eleve implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    //Ajout de la contrainte d'unicité du mail pour la question 6
    //avec l'annotation @Column avec l'attribut unique=true sur le champ mail
    //Et en ayant importé cette classe au préalable
    //grâce a "import javax.persistence.Column;"
    @Column(unique = true)
    private String mail;
    private String motDePasse;
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
    private Integer classe;
    @ManyToOne
    private Etablissement etablissement;
    //private String uai;

    //Constructeur par défaut nécessaire pour JPA
    public Eleve() {
    }

    // Constructeur
    public Eleve(String nom, String prenom, String mail, String motDePasse, Date dateNaissance, Integer classe) {
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.motDePasse = motDePasse;
        this.classe = classe;
        this.dateNaissance = dateNaissance;
    }

    // Getter et Setter pour id
    public Long getId() {
        return id;
    }

    // Getter et Setter pour nom
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // Getter et Setter pour prenom
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    // Getter et Setter pour mail
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    // Getter et Setter pour motDePasse
    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    // Getter et Setter pour adressePostale
    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Integer getClasse() {
        return classe;
    }

    public void setClasse(Integer classe) {
        this.classe = classe;
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    @Override
    public String toString() {
        return "Eleve{" + "id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", mail=" + mail + ", motDePasse=" + motDePasse + ", dateNaissance=" + dateNaissance + ", classe=" + classe + '}';
    }

}
