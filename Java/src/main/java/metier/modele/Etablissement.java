/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author selghissas
 */
@Entity
public class Etablissement {
    @Id
    private String uai;
    private String nomEtablissement;
    private String secteur;
    private String nomCommune;
    private String codeCommune;
    private String codeDepartement;
    private String departement;
    private String academie;
    private String ips;
    
    public Etablissement() {
        
    }

    public Etablissement(String uai, String nomEtablissement, String secteur, String nomCommune, String codeCommune, String codeDepartement, String departement, String academie, String ips) {
        this.uai = uai;
        this.nomEtablissement = nomEtablissement;
        this.secteur = secteur;
        this.nomCommune = nomCommune;
        this.codeCommune = codeCommune;
        this.codeDepartement = codeDepartement;
        this.departement = departement;
        this.academie = academie;
        this.ips = ips;
    }
    
    

    public String getUai() {
        return uai;
    }

    public void setUai(String uai) {
        this.uai = uai;
    }

    public String getNomEtablissement() {
        return nomEtablissement;
    }

    public void setNomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
    }

    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public String getNomCommune() {
        return nomCommune;
    }

    public void setNomCommune(String nomCommune) {
        this.nomCommune = nomCommune;
    }

    public String getCodeCommune() {
        return codeCommune;
    }

    public void setCodeCommune(String codeCommune) {
        this.codeCommune = codeCommune;
    }

    public String getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getAcademie() {
        return academie;
    }

    public void setAcademie(String academie) {
        this.academie = academie;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    @Override
    public String toString() {
        return "Etablissement{" + "uai=" + uai + ", nomEtablissement=" + nomEtablissement + ", secteur=" + secteur + ", nomCommune=" + nomCommune + ", codeCommune=" + codeCommune + ", codeDepartement=" + codeDepartement + ", departement=" + departement + ", academie=" + academie + ", ips=" + ips + '}';
    }
    
    
    
    
    
}
