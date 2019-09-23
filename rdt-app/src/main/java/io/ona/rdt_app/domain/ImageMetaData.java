package io.ona.rdt_app.domain;

import android.graphics.Bitmap;

import edu.washington.cs.ubicomplab.rdt_reader.ImageProcessor;

/**
 * Created by Vincent Karuri on 18/09/2019
 */
public class ImageMetaData {

    private Bitmap image;
    private String providerId;
    private String baseEntityId;
    private ImageProcessor.InterpretationResult interpretationResult;
    private long timeTaken;

    public Bitmap getImage() {
        return image;
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

    public void setImage(Bitmap image) {
        this.image = image;
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

    public ImageMetaData withImage(Bitmap image) {
        setImage(image);
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
}
