package io.ona.rdt.domain;

import org.opencv.core.Point;

import edu.washington.cs.ubicomplab.rdt_reader.core.RDTInterpretationResult;

/**
 * Created by Vincent Karuri on 16/10/2019
 */
public class UnParcelableImageMetadata {

    private RDTInterpretationResult rdtInterpretationResult;

    private Point[] boundary;

    public Point[] getBoundary() {
        return boundary;
    }

    public RDTInterpretationResult getRdtInterpretationResult() {
        return rdtInterpretationResult;
    }

    public void setRdtInterpretationResult(RDTInterpretationResult rdtInterpretationResult) {
        this.rdtInterpretationResult = rdtInterpretationResult;
    }

    public void setBoundary(Point[] boundary) {
        this.boundary = boundary;
    }

    public UnParcelableImageMetadata withInterpretationResult(RDTInterpretationResult rdtInterpretationResult) {
        setRdtInterpretationResult(rdtInterpretationResult);
        return this;
    }
    
    public UnParcelableImageMetadata withBoundary(Point[] boundary) {
        setBoundary(boundary);
        return this;
    }
}
