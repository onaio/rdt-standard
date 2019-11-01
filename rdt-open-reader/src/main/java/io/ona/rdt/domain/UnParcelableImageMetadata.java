package io.ona.rdt.domain;

import org.opencv.core.Point;

import edu.washington.cs.ubicomplab.rdt_reader.ImageProcessor;

/**
 * Created by Vincent Karuri on 16/10/2019
 */
public class UnParcelableImageMetadata {

    private ImageProcessor.InterpretationResult interpretationResult;

    private Point[] boundary;

    public Point[] getBoundary() {
        return boundary;
    }

    public ImageProcessor.InterpretationResult getInterpretationResult() {
        return interpretationResult;
    }

    public void setInterpretationResult(ImageProcessor.InterpretationResult interpretationResult) {
        this.interpretationResult = interpretationResult;
    }

    public void setBoundary(Point[] boundary) {
        this.boundary = boundary;
    }

    public UnParcelableImageMetadata withInterpretationResult(ImageProcessor.InterpretationResult interpretationResult) {
        setInterpretationResult(interpretationResult);
        return this;
    }
    
    public UnParcelableImageMetadata withBoundary(Point[] boundary) {
        setBoundary(boundary);
        return this;
    }
}
