package io.ona.rdt.domain;

import org.junit.Test;
import org.opencv.core.Point;

import edu.washington.cs.ubicomplab.rdt_reader.core.RDTInterpretationResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by Vincent Karuri on 08/11/2019
 */
public class UnParcelableImageMetadataTest {

    @Test
    public void testUnParcelableImageMetadataBuilder() {
        Point[] points = new Point[]{};
        RDTInterpretationResult rdtInterpretationResult = mock(RDTInterpretationResult.class);

        UnParcelableImageMetadata unParcelableImageMetadata = new UnParcelableImageMetadata();
        unParcelableImageMetadata.withInterpretationResult(rdtInterpretationResult);
        unParcelableImageMetadata.withBoundary(points);

        assertEquals(rdtInterpretationResult, unParcelableImageMetadata.getRdtInterpretationResult());
        assertEquals(points, unParcelableImageMetadata.getBoundary());
    }
}
