package io.ona.rdt_app.domain;

/**
 * Created by Vincent Karuri on 16/10/2019
 */
public class ParcelableImageMetadata {

    private String providerId;
    private String baseEntityId;
    private String fullImageId;
    private String croppedImageId;
    private String imageToSave;
    private long imageTimeStamp;
    private long captureDuration;
    private boolean isFlashOn;
    
    public String getProviderId() {
        return providerId;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }
    
    public long getCaptureDuration() {
        return captureDuration;
    }

    public boolean isFlashOn() {
        return isFlashOn;
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

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }
    
    public void setCaptureDuration(long captureDuration) {
        this.captureDuration = captureDuration;
    }
    
    public void setFlashOn(boolean flashOn) {
        this.isFlashOn = flashOn;
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

    public ParcelableImageMetadata withProviderId(String providerId) {
        setProviderId(providerId);
        return this;
    }

    public ParcelableImageMetadata withBaseEntityId(String baseEntityId) {
        setBaseEntityId(baseEntityId);
        return this;
    }

    public ParcelableImageMetadata withTimeTaken(long timeTaken) {
        setCaptureDuration(timeTaken);
        return this;
    }
    
    public ParcelableImageMetadata withFlashOn(boolean isFlashOn) {
        setFlashOn(isFlashOn);
        return this;
    }
}
