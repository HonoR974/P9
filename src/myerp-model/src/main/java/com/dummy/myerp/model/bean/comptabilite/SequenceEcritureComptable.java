package com.dummy.myerp.model.bean.comptabilite;

import javax.validation.constraints.NotNull;

/**
 * Bean représentant une séquence pour les références d'écriture comptable
 */
public class SequenceEcritureComptable {

    // ==================== Attributs ====================
    /** L'année */
    private Integer annee;
    /** La dernière valeur utilisée */
    private Integer derniereValeur;

    /** code journal Comptable */
    private String code = null;

    // ==================== Constructeurs ====================
    /**
     * Constructeur
     */
    public SequenceEcritureComptable() {

    }

    /**
     * Constructeur
     *
     * @param pAnnee          -
     * @param pDerniereValeur -
     */
    public SequenceEcritureComptable(Integer pAnnee, Integer pDerniereValeur) {
        annee = pAnnee;
        derniereValeur = pDerniereValeur;
    }

    public SequenceEcritureComptable(Integer pAnnee, Integer pDerniereValeur, String pCode) {
        annee = pAnnee;
        derniereValeur = pDerniereValeur;
        code = pCode;
    }

    /**
     * @return Integer
     */
    // ==================== Getters/Setters ====================
    public Integer getAnnee() {
        return annee;
    }

    /**
     * @param pAnnee
     */
    public void setAnnee(Integer pAnnee) {
        annee = pAnnee;
    }

    /**
     * @return Integer
     */
    public Integer getDerniereValeur() {
        return derniereValeur;
    }

    /**
     * @param pDerniereValeur
     */
    public void setDerniereValeur(Integer pDerniereValeur) {
        derniereValeur = pDerniereValeur;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // ==================== Méthodes ====================
    @Override
    public String toString() {
        final StringBuilder vStB = new StringBuilder(this.getClass().getSimpleName());
        final String vSEP = ", ";
        vStB.append("{")
                .append("annee=").append(annee)
                .append(vSEP).append("derniereValeur=").append(derniereValeur)
                .append("}");
        return vStB.toString();
    }
}
