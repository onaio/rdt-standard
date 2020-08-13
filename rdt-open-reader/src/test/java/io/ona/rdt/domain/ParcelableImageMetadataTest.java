package io.ona.rdt.domain;

import android.os.Parcel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by Vincent Karuri on 08/11/2019
 */
public class ParcelableImageMetadataTest {

    private ParcelableImageMetadata parcelableImageMetadata;
    private LineReadings lineReadings = new LineReadings(false, true, false);
    private final String FULL_IMG_HASH = "full_img_hash";

    @Before
    public void setUp() {
        parcelableImageMetadata = new ParcelableImageMetadata();
    }

    @Test
    public void testParcelableImageMetadataBuilder() {
        parcelableImageMetadata.withBaseEntityId("baseEntityId")
                .withProviderId("providerID")
                .withTimeTaken(0)
                .withFlashOn(false)
                .withManualCapture(true)
                .withCassetteBoundary("(0, 0), (0, 0), (0, 0), (0, 0)")
                .withLineReadings(lineReadings)
                .withManualCapture(false)
                .withCroppedImageMD5Hash("cropped_img_hash")
                .withFullImageMD5Hash(FULL_IMG_HASH);
        
        assertParcelableImageContent(parcelableImageMetadata);
    }

    @Test
    public void testParcelling() {
        // Obtain a Parcel object and write the parcelable object to it:
        Parcel parcel = mock(Parcel.class);
        doReturn("string").when(parcel).readString();
        doReturn(0l).when(parcel).readLong();
        doReturn((byte) 0).when(parcel).readByte();
        doReturn(lineReadings).when(parcel).readParcelable(any(ClassLoader.class));
        parcelableImageMetadata.writeToParcel(parcel, 0);

        // After you're done with writing, you need to reset the parcel for reading:
        parcel.setDataPosition(0);

        // Reconstruct object from parcel and asserts:
        ParcelableImageMetadata actualParcealableImageMetadata = ParcelableImageMetadata.CREATOR.createFromParcel(parcel);
        assertEquals("string", actualParcealableImageMetadata.getBaseEntityId());
        assertEquals("string", actualParcealableImageMetadata.getProviderId());
        assertEquals(0, actualParcealableImageMetadata.getCaptureDuration());
        assertEquals(false, actualParcealableImageMetadata.isFlashOn());
        assertEquals(false, actualParcealableImageMetadata.isManualCapture());
        assertEquals("string", actualParcealableImageMetadata.getCassetteBoundary());
        assertEquals(lineReadings, actualParcealableImageMetadata.getLineReadings());
    }
    
    private void assertParcelableImageContent(ParcelableImageMetadata parcelableImageMetadata) {
        assertEquals("baseEntityId", parcelableImageMetadata.getBaseEntityId());
        assertEquals("providerID", parcelableImageMetadata.getProviderId());
        assertEquals(0, parcelableImageMetadata.getCaptureDuration());
        assertEquals(false, parcelableImageMetadata.isFlashOn());
        assertEquals(false, parcelableImageMetadata.isManualCapture());
        assertEquals("(0, 0), (0, 0), (0, 0), (0, 0)", parcelableImageMetadata.getCassetteBoundary());
        assertEquals(lineReadings, parcelableImageMetadata.getLineReadings());
        assertEquals("cropped_img_hash", parcelableImageMetadata.getCroppedImageMD5Hash());
        assertEquals(FULL_IMG_HASH, parcelableImageMetadata.getFullImageMD5Hash());
    }
}
