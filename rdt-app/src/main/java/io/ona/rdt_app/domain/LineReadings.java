package io.ona.rdt_app.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vincent Karuri on 16/10/2019
 */
public class LineReadings implements Parcelable {

    private boolean topLine;
    private boolean middleLine;
    private boolean bottomLine;

    protected LineReadings(Parcel in) {
        topLine = in.readByte() != 0;
        middleLine = in.readByte() != 0;
        bottomLine = in.readByte() != 0;
    }

    public LineReadings(boolean topLine, boolean middleLine, boolean bottomLine) {
        this.topLine = topLine;
        this.middleLine = middleLine;
        this.bottomLine = bottomLine;
    }

    public static final Creator<LineReadings> CREATOR = new Creator<LineReadings>() {
        @Override
        public LineReadings createFromParcel(Parcel in) {
            return new LineReadings(in);
        }

        @Override
        public LineReadings[] newArray(int size) {
            return new LineReadings[size];
        }
    };

    public boolean isTopLine() {
        return topLine;
    }

    public void setTopLine(boolean topLine) {
        this.topLine = topLine;
    }

    public boolean isMiddleLine() {
        return middleLine;
    }

    public void setMiddleLine(boolean middleLine) {
        this.middleLine = middleLine;
    }

    public boolean isBottomLine() {
        return bottomLine;
    }

    public void setBottomLine(boolean bottomLine) {
        this.bottomLine = bottomLine;
    }

    public LineReadings withTopLine(boolean topLine) {
        setTopLine(topLine);
        return this;
    }

    public LineReadings withMiddleLine(boolean middleLine) {
        setMiddleLine(middleLine);
        return this;
    }

    public LineReadings withBottomLine(boolean bottomLine) {
        setBottomLine(bottomLine);
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (this.topLine ? 1 : 0));
        dest.writeByte((byte) (this.middleLine ? 1 : 0));
        dest.writeByte((byte) (this.bottomLine ? 1 : 0));
    }
}
