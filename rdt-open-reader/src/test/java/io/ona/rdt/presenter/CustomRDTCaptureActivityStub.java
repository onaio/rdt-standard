package io.ona.rdt.presenter;

import io.ona.rdt.contract.CustomRDTCaptureContract;
import io.ona.rdt.domain.CompositeImage;

public class CustomRDTCaptureActivityStub implements CustomRDTCaptureContract.View {
    @Override
    public void onImageSaved(CompositeImage compositeImage) {

    }

    @Override
    public String getBaseEntityId() {
        return null;
    }

    @Override
    public boolean isManualCapture() {
        return false;
    }
}
