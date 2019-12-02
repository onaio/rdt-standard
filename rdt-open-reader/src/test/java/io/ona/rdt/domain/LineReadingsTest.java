package io.ona.rdt.domain;

import android.os.Parcel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by Vincent Karuri on 20/11/2019
 */
public class LineReadingsTest {

    private LineReadings lineReadings;

    @Before
    public void setUp() {
        lineReadings = new LineReadings(false, true, true);
    }

    @Test
    public void testLineReadingsBuilder() {
        assertFalse(lineReadings.isTopLine());
        assertTrue(lineReadings.isMiddleLine());
        assertTrue(lineReadings.isBottomLine());
        lineReadings.withBottomLine(false)
                .withMiddleLine(false)
                .withTopLine(true);
        assertTrue(lineReadings.isTopLine());
        assertFalse(lineReadings.isBottomLine());
        assertFalse(lineReadings.isMiddleLine());
    }

    @Test
    public void testParcelling() {
        // Obtain a Parcel object and write the parcelable object to it:
        Parcel parcel = mock(Parcel.class);
        doReturn((byte) 1).when(parcel).readByte();
        doReturn(lineReadings).when(parcel).readParcelable(any(ClassLoader.class));
        lineReadings.writeToParcel(parcel, 0);

        // After you're done with writing, you need to reset the parcel for reading:
        parcel.setDataPosition(0);

        // Reconstruct object from parcel and asserts:
        LineReadings actualLineReadings = LineReadings.CREATOR.createFromParcel(parcel);
        assertTrue(actualLineReadings.isMiddleLine());
        assertTrue(actualLineReadings.isBottomLine());
        assertTrue(actualLineReadings.isTopLine());
    }
}
