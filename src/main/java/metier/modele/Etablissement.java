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
    private Double ips;
    private Double latitude;
    private Double longitude;

    public Etablissement() {

    }

    public Etablissement(String uai, String nomEtablissement, String secteur, String nomCommune, String codeCommune, String codeDepartement, String departement, String academie, Double ips) {
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

    public Double getIps() {
        return ips;
    }

    public void setIps(Double ips) {
        this.ips = ips;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Etablissement{" + "uai=" + uai + ", nomEtablissement=" + nomEtablissement + ", secteur=" + secteur + ", nomCommune=" + nomCommune + ", codeCommune=" + codeCommune + ", codeDepartement=" + codeDepartement + ", departement=" + departement + ", academie=" + academie + ", ips=" + ips + '}';
    }

}
