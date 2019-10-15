package io.ona.rdt_app.domain;

import android.graphics.Bitmap;

import org.opencv.core.Point;

import edu.washington.cs.ubicomplab.rdt_reader.ImageProcessor;

/**
 * Created by Vincent Karuri on 18/09/2019
 */
public class ImageMetaData {

    private Bitmap fullImage;
    private Bitmap croppedImage;
    private String providerId;
    private String baseEntityId;
    private String fullImageId;
    private String croppedImageId;
    private String imageToSave;
    private long imageTimeStamp;
    private long captureDuration;
    private boolean isFlashOn;
    private ImageProcessor.InterpretationResult interpretationResult;
    private Point[] boundary;

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

    public long getCaptureDuration() {
        return captureDuration;
    }

    public Bitmap getCroppedImage() {
        return croppedImage;
    }

    public boolean isFlashOn() {
        return isFlashOn;
    }

    public Point[] getBoundary() {
        return boundary;
    }

    public String getFullImageId() {
        return fullImageId;
    }

    public String getCroppedImageId() {
        return croppedImageId;
    }

    public long getImageTimeStamp() {
        return imageTimeStamp;
    }

    public String getImageToSave() {
        return imageToSave;
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

    public void setCaptureDuration(long captureDuration) {
        this.captureDuration = captureDuration;
    }

    public void setCroppedImage(Bitmap croppedImage) {
        this.croppedImage = croppedImage;
    }

    public void setFlashOn(boolean flashOn) {
        this.isFlashOn = flashOn;
    }

    public void setBoundary(Point[] boundary) {
        this.boundary = boundary;
    }

    public void setFullImageId(String fullImageId) {
        this.fullImageId = fullImageId;
    }

    public void setCroppedImageId(String croppedImageId) {
        this.croppedImageId = croppedImageId;
    }

    public void setImageTimeStamp(long imageTimeStamp) {
        this.imageTimeStamp = imageTimeStamp;
    }

    public void setImageToSave(String imageToSave) {
        this.imageToSave = imageToSave;
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
        setCaptureDuration(timeTaken);
        return this;
    }

    public ImageMetaData withCroppedImage(Bitmap croppedImage) {
        setCroppedImage(croppedImage);
        return this;
    }

    public ImageMetaData withFlashOn(boolean isFlashOn) {
        setFlashOn(isFlashOn);
        return this;
    }

    public ImageMetaData withBoundary(Point[] boundary) {
        setBoundary(boundary);
        return this;
    }
}
