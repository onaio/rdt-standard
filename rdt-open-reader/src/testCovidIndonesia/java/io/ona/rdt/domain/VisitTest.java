package io.ona.rdt.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.ona.rdt.PowerMockTest;

public class VisitTest extends PowerMockTest {

    private Visit visit;
    private String visitName = "first visit";
    private String visitDate = "12-16-2020";

    @Before
    public void setUp() {
        visit = new Visit(visitName, visitDate);
    }

    @Test
    public void testOnVerifyVisitInfo() {
        Assert.assertEquals(visitName, visit.getVisitName());
        Assert.assertEquals(visitDate, visit.getDateOfVisit());
    }

    @Test
    public void testUpdatedVisitInfo() {
        visitName = "second visit";
        visitDate = "12-17-2020";

        visit.setVisitName(visitName);
        visit.setDateOfVisit(visitDate);

        Assert.assertEquals(visitName, visit.getVisitName());
        Assert.assertEquals(visitDate, visit.getDateOfVisit());
    }
}
