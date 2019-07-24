package io.ona.rdt_app.interactor;

import org.smartregister.job.SyncServiceJob;
import org.smartregister.login.interactor.BaseLoginInteractor;
import org.smartregister.view.contract.BaseLoginContract;

import io.ona.rdt_app.BuildConfig;
import io.ona.rdt_app.job.ImageUploadSyncServiceJob;

/**
 * Created by Vincent Karuri on 16/07/2019
 */
public class LoginInteractor extends BaseLoginInteractor implements BaseLoginContract.Interactor {

    public LoginInteractor(BaseLoginContract.Presenter loginPresenter) {
        super(loginPresenter);
    }

    protected void scheduleJobsPeriodically() {
        ImageUploadSyncServiceJob
                .scheduleJob(ImageUploadSyncServiceJob.TAG, BuildConfig.SYNC_INTERVAL_MINUTES,
                        getFlexValue(BuildConfig.SYNC_INTERVAL_MINUTES));

        SyncServiceJob
                .scheduleJob(SyncServiceJob.TAG, BuildConfig.SYNC_INTERVAL_MINUTES,
                        getFlexValue(BuildConfig.SYNC_INTERVAL_MINUTES));
    }

    public void scheduleJobsImmediately() {
        ImageUploadSyncServiceJob
                .scheduleJobImmediately(ImageUploadSyncServiceJob.TAG);

        SyncServiceJob
                .scheduleJobImmediately(SyncServiceJob.TAG);
    }

    private long getFlexValue(long value) {
        final long MINIMUM_JOB_FLEX_VALUE = 1;
        long minutes = MINIMUM_JOB_FLEX_VALUE;
        if (value > MINIMUM_JOB_FLEX_VALUE) {
            minutes = (long) Math.ceil(value / 3);
        }
        return minutes;
    }
}
