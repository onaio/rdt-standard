package io.ona.rdt.domain;

/**
 * Created by Vincent Karuri on 19/08/2020
 */
public class Visit {

    private String visitName;
    private String dateOfVisit;

    public Visit(String visitName, String dateOfVisit) {
        this.visitName = visitName;
        this.dateOfVisit = dateOfVisit;
    }

    public String getVisitName() {
        return visitName;
    }

    public void setVisitName(String visitName) {
        this.visitName = visitName;
    }

    public String getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(String dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }
}
