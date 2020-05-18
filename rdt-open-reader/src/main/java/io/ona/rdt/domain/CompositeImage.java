package io.ona.rdt.domain;

import android.graphics.Bitmap;

/**
 * Created by Vincent Karuri on 18/09/2019
 */
public class CompositeImage {

    private Bitmap fullImage;

    private Bitmap croppedImage;

    private ParcelableImageMetadata parcelableImageMetadata;

    private UnParcelableImageMetadata unParcelableImageMetadata;

    private String fullImageMD5Hash;

    private String croppedImageMD5Hash;

    public Bitmap getFullImage() {
        return fullImage;
    }

    public Bitmap getCroppedImage() {
        return croppedImage;
    }

    public ParcelableImageMetadata getParcelableImageMetadata() {
        return parcelableImageMetadata;
    }

    public UnParcelableImageMetadata getUnParcelableImageMetadata() {
        return unParcelableImageMetadata;
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

    public void setFullImage(Bitmap fullImage) {
        this.fullImage = fullImage;
    }

    public void setCroppedImage(Bitmap croppedImage) {
        this.croppedImage = croppedImage;
    }

    public void setParcelableImageMetadata(ParcelableImageMetadata parcelableImageMetadata) {
        this.parcelableImageMetadata = parcelableImageMetadata;
    }

    public void setUnParcelableImageMetadata(UnParcelableImageMetadata unParcelableImageMetadata) {
        this.unParcelableImageMetadata = unParcelableImageMetadata;
    }

    public CompositeImage withFullImage(Bitmap fullImage) {
        setFullImage(fullImage);
        return this;
    }

    public CompositeImage withCroppedImage(Bitmap croppedImage) {
        setCroppedImage(croppedImage);
        return this;
    }

    public CompositeImage withParcelableImageMetadata(ParcelableImageMetadata parcelableImageMetadata) {
        setParcelableImageMetadata(parcelableImageMetadata);
        return this;
    }

    public CompositeImage withUnParcelableImageMetadata(UnParcelableImageMetadata unParcelableImageMetadata) {
        setUnParcelableImageMetadata(unParcelableImageMetadata);
        return this;
    }

    public CompositeImage withFullImageMD5Hash(String fullImageMD5Hash) {
        setFullImageMD5Hash(fullImageMD5Hash);
        return this;
    }

    public CompositeImage withImageMD5Hash(String croppedImageMD5Hash) {
        setCroppedImageMD5Hash(croppedImageMD5Hash);
        return this;
    }
}
