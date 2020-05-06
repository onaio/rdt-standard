package io.ona.rdt.domain;

import org.junit.Test;
import org.opencv.core.Point;

import edu.washington.cs.ubicomplab.rdt_reader.ImageProcessor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by Vincent Karuri on 08/11/2019
 */
public class UnParcelableImageMetadataTest {

    @Test
    public void testUnParcelableImageMetadataBuilder() {
        Point[] points = new Point[]{};
        ImageProcessor.InterpretationResult interpretationResult = mock(ImageProcessor.InterpretationResult.class);

        UnParcelableImageMetadata unParcelableImageMetadata = new UnParcelableImageMetadata();
        unParcelableImageMetadata.withInterpretationResult(interpretationResult);
        unParcelableImageMetadata.withBoundary(points);

        assertEquals(interpretationResult, unParcelableImageMetadata.getRdtInterpretationResult());
        assertEquals(points, unParcelableImageMetadata.getBoundary());
    }
}
