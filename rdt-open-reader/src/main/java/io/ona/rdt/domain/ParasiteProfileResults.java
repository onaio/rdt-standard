package io.ona.rdt.domain;

/**
 * Created by Vincent Karuri on 04/02/2020
 */
public class ParasiteProfileResults {

    private String pFalciparum = "p_falciparum";
    private String pVivax = "p_vivax";
    private String pMalariae = "p_malariae";
    private String pOvale = "p_ovale";
    private String pfGameto = "pf_gameto";
    private String experimentDate = "experiment_date";
    private String rdtId = "rdt_id";


    public String getpFalciparum() {
        return pFalciparum;
    }

    public void setpFalciparum(String pFalciparum) {
        this.pFalciparum = pFalciparum;
    }

    public String getpVivax() {
        return pVivax;
    }

    public void setpVivax(String pVivax) {
        this.pVivax = pVivax;
    }

    public String getpMalariae() {
        return pMalariae;
    }

    public void setpMalariae(String pMalariae) {
        this.pMalariae = pMalariae;
    }

    public String getpOvale() {
        return pOvale;
    }

    public void setpOvale(String pOvale) {
        this.pOvale = pOvale;
    }

    public String getPfGameto() {
        return pfGameto;
    }

    public void setPfGameto(String pfGameto) {
        this.pfGameto = pfGameto;
    }

    public String getExperimentDate() {
        return experimentDate;
    }

    public void setExperimentDate(String experimentDate) {
        this.experimentDate = experimentDate;
    }

    public String getRdtId() {
        return rdtId;
    }

    public void setRdtId(String rdtId) {
        this.rdtId = rdtId;
    }
}
