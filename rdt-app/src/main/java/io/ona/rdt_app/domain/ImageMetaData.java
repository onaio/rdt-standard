package io.ona.rdt_app.domain;

import android.graphics.Bitmap;
import android.graphics.Point;

import edu.washington.cs.ubicomplab.rdt_reader.ImageProcessor;

/**
 * Created by Vincent Karuri on 18/09/2019
 */
public class ImageMetaData {

    private Bitmap fullImage;
    private Bitmap croppedImage;
    private boolean flashStatus;
    private Point[] boundary;
    private String providerId;
    private String baseEntityId;
    private ImageProcessor.InterpretationResult interpretationResult;
    private long timeTaken;

    public Bitmap getFullImage() {
        return fullImage;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public ImageProcessor.InterpretationResult getInterpretationResult() {
        return interpretationResult;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public Bitmap getCroppedImage() {
        return croppedImage;
    }

    public boolean getFlashStatus() {
        return flashStatus;
    }

    public Point[] getBoundary() {
        return boundary;
    }

    public void setFullImage(Bitmap fullImage) {
        this.fullImage = fullImage;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }

    public void setInterpretationResult(ImageProcessor.InterpretationResult interpretationResult) {
        this.interpretationResult = interpretationResult;
    }

    public void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public void setCroppedImage(Bitmap croppedImage) {
        this.croppedImage = croppedImage;
    }

    public void setFlashStatus(boolean flashStatus) {
        this.flashStatus = flashStatus;
    }

    public void setBoundary(Point[] boundary) {
        this.boundary = boundary;
    }

    public ImageMetaData withFullImage(Bitmap fullImage) {
        setFullImage(fullImage);
        return this;
    }

    public ImageMetaData withProviderId(String providerId) {
        setProviderId(providerId);
        return this;
    }

    public ImageMetaData withBaseEntityId(String baseEntityId) {
        setBaseEntityId(baseEntityId);
        return this;
    }

    public ImageMetaData withInterpretationResult(ImageProcessor.InterpretationResult interpretationResult) {
        setInterpretationResult(interpretationResult);
        return this;
    }

    public ImageMetaData withTimeTaken(long timeTaken) {
        setTimeTaken(timeTaken);
        return this;
    }

    public ImageMetaData withCroppedImage(Bitmap croppedImage) {
        setCroppedImage(croppedImage);
        return this;
    }

    public ImageMetaData withFlashStatus(boolean flashStatus) {
        setFlashStatus(flashStatus);
        return this;
    }

    public ImageMetaData withBoundary(Point[] boundary) {
        setBoundary(boundary);
        return this;
    }
}
