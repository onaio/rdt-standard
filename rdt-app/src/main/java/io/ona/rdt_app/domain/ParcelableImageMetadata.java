package io.ona.rdt_app.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vincent Karuri on 16/10/2019
 */
public class ParcelableImageMetadata implements Parcelable {

    private String providerId;
    private String baseEntityId;
    private String fullImageId;
    private String croppedImageId;
    private String imageToSave;
    private long imageTimeStamp;
    private long captureDuration;
    private boolean isFlashOn;

    protected ParcelableImageMetadata(Parcel in) {
        providerId = in.readString();
        baseEntityId = in.readString();
        fullImageId = in.readString();
        croppedImageId = in.readString();
        imageToSave = in.readString();
        imageTimeStamp = in.readLong();
        captureDuration = in.readLong();
        isFlashOn = in.readByte() != 0;
    }

    public static final Creator<ParcelableImageMetadata> CREATOR = new Creator<ParcelableImageMetadata>() {
        @Override
        public ParcelableImageMetadata createFromParcel(Parcel in) {
            return new ParcelableImageMetadata(in);
        }

        @Override
        public ParcelableImageMetadata[] newArray(int size) {
            return new ParcelableImageMetadata[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.providerId);
        dest.writeString(this.baseEntityId);
        dest.writeString(this.fullImageId);
        dest.writeString(this.croppedImageId);
        dest.writeString(this.imageToSave);
        dest.writeLong(this.imageTimeStamp);
        dest.writeLong(this.captureDuration);
        dest.writeByte((byte) (this.isFlashOn ? 1 : 0));
    }
}