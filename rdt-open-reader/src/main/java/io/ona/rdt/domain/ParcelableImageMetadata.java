package io.ona.rdt.domain;

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
    private String cassetteBoundary;
    private long imageTimeStamp;
    private long captureDuration;
    private boolean isManualCapture;
    private boolean isFlashOn;
    private LineReadings lineReadings;
    private String fullImageMD5Hash;
    private String croppedImageMD5Hash;

    public ParcelableImageMetadata() {
    }

    protected ParcelableImageMetadata(Parcel in) {
        providerId = in.readString();
        baseEntityId = in.readString();
        fullImageId = in.readString();
        croppedImageId = in.readString();
        imageToSave = in.readString();
        cassetteBoundary = in.readString();
        imageTimeStamp = in.readLong();
        captureDuration = in.readLong();
        isManualCapture = in.readByte() != 0;
        isFlashOn = in.readByte() != 0;
        lineReadings = in.readParcelable(LineReadings.class.getClassLoader());
        fullImageMD5Hash = in.readString();
        croppedImageMD5Hash = in.readString();
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

    public LineReadings getLineReadings() {
        return lineReadings;
    }

    public String getImageToSave() {
        return imageToSave;
    }

    public String getCassetteBoundary() {
        return cassetteBoundary;
    }

    public String getFullImageMD5Hash() {
        return fullImageMD5Hash;
    }

    public String getCroppedImageMD5Hash() {
        return croppedImageMD5Hash;
    }

    public void setCroppedImageMD5Hash(String croppedImageMD5Hash) {
        this.croppedImageMD5Hash = croppedImageMD5Hash;
    }

    public void setFullImageMD5Hash(String fullImageMD5Hash) {
        this.fullImageMD5Hash = fullImageMD5Hash;
    }

    public boolean isManualCapture() {
        return isManualCapture;
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

    public void setLineReadings(LineReadings lineReadings) {
        this.lineReadings = lineReadings;
    }

    public void setCassetteBoundary(String cassetteBoundary) {
        this.cassetteBoundary = cassetteBoundary;
    }

    public void setManualCapture(boolean manualCapture) {
        isManualCapture = manualCapture;
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

    public ParcelableImageMetadata withLineReadings(LineReadings lineReadings) {
        setLineReadings(lineReadings);
        return this;
    }

    public ParcelableImageMetadata withCassetteBoundary(String cassetteBoundary) {
        setCassetteBoundary(cassetteBoundary);
        return this;
    }

    public ParcelableImageMetadata withFullmageId(String fullImageId) {
        setFullImageId(fullImageId);
        return this;
    }

    public ParcelableImageMetadata withCroppedImageId(String croppedImageId) {
        setCroppedImageId(croppedImageId);
        return this;
    }

    public ParcelableImageMetadata withImageTimeStamp(long imageTimeStamp) {
        setImageTimeStamp(imageTimeStamp);
        return this;
    }

    public ParcelableImageMetadata withManualCapture(boolean isManualCapture) {
        setManualCapture(isManualCapture);
        return this;
    }

    public ParcelableImageMetadata withFullImageMD5Hash(String fullImageMD5Hash) {
        setFullImageMD5Hash(fullImageMD5Hash);
        return this;
    }

    public ParcelableImageMetadata withCroppedImageMD5Hash(String croppedImageMD5Hash) {
        setCroppedImageMD5Hash(croppedImageMD5Hash);
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(providerId);
        dest.writeString(baseEntityId);
        dest.writeString(fullImageId);
        dest.writeString(croppedImageId);
        dest.writeString(imageToSave);
        dest.writeString(cassetteBoundary);
        dest.writeLong(imageTimeStamp);
        dest.writeLong(captureDuration);
        dest.writeByte((byte) (isManualCapture ? 1 : 0));
        dest.writeByte((byte) (isFlashOn ? 1 : 0));
        dest.writeParcelable(lineReadings, flags);
        dest.writeString(fullImageMD5Hash);
        dest.writeString(croppedImageMD5Hash);
    }
}
