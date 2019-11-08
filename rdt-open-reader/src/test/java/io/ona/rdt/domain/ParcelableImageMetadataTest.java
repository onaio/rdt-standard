package io.ona.rdt.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vincent Karuri on 08/11/2019
 */
public class ParcelableImageMetadataTest {

    @Test
    public void testParcelableImageMetadataBuilder() {
        LineReadings lineReadings = new LineReadings(false, true, false);

        ParcelableImageMetadata parcelableImageMetadata = new ParcelableImageMetadata();
        parcelableImageMetadata.withBaseEntityId("baseEntityId")
                .withProviderId("providerID")
                .withTimeTaken(0)
                .withFlashOn(false)
                .withManualCapture(true)
                .withCassetteBoundary("(0, 0), (0, 0), (0, 0), (0, 0)")
                .withLineReadings(lineReadings);

        assertEquals("baseEntityId", parcelableImageMetadata.getBaseEntityId());
        assertEquals("providerID", parcelableImageMetadata.getProviderId());
        assertEquals(0, parcelableImageMetadata.getCaptureDuration());
        assertEquals(false, parcelableImageMetadata.isFlashOn());
        assertEquals(true, parcelableImageMetadata.isManualCapture());
        assertEquals("(0, 0), (0, 0), (0, 0), (0, 0)", parcelableImageMetadata.getCassetteBoundary());
        assertEquals(lineReadings, parcelableImageMetadata.getLineReadings());
    }
}
