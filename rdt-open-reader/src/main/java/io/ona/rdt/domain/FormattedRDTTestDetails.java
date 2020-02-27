package io.ona.rdt.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vincent Karuri on 29/01/2020
 */
public class FormattedRDTTestDetails implements Parcelable {

    private String formattedRDTId;
    private String formattedRDTTestDate;
    private String formattedRDTType;
    private String formattedTestResults;
    private String testResult;

    public FormattedRDTTestDetails() {}

    protected FormattedRDTTestDetails(Parcel in) {
        formattedRDTId = in.readString();
        formattedRDTTestDate = in.readString();
        formattedRDTType = in.readString();
        formattedTestResults = in.readString();
        testResult = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(formattedRDTId);
        dest.writeString(formattedRDTTestDate);
        dest.writeString(formattedRDTType);
        dest.writeString(formattedTestResults);
        dest.writeString(testResult);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FormattedRDTTestDetails> CREATOR = new Creator<FormattedRDTTestDetails>() {
        @Override
        public FormattedRDTTestDetails createFromParcel(Parcel in) {
            return new FormattedRDTTestDetails(in);
        }

        @Override
        public FormattedRDTTestDetails[] newArray(int size) {
            return new FormattedRDTTestDetails[size];
        }
    };

    public String getFormattedRDTId() {
        return formattedRDTId;
    }

    public void setFormattedRDTId(String formattedRDTId) {
        this.formattedRDTId = formattedRDTId;
    }

    public String getFormattedRDTTestDate() {
        return formattedRDTTestDate;
    }

    public void setFormattedRDTTestDate(String formattedRDTTestDate) {
        this.formattedRDTTestDate = formattedRDTTestDate;
    }

    public String getFormattedRDTType() {
        return formattedRDTType;
    }

    public void setFormattedRDTType(String formattedRDTType) {
        this.formattedRDTType = formattedRDTType;
    }

    public String getFormattedTestResults() {
        return formattedTestResults;
    }

    public void setFormattedTestResults(String formattedTestResults) {
        this.formattedTestResults = formattedTestResults;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

}
