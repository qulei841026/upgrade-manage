package carsmart.upgrade.manager;

import android.os.Parcel;
import android.os.Parcelable;

public class Upgrade implements Parcelable {

    public boolean isForce;

    public String prompt;

    public int rule;

    public String url;

    public Upgrade() {

    }

    protected Upgrade(Parcel in) {
        isForce = in.readByte() != 0;
        prompt = in.readString();
        rule = in.readInt();
        url = in.readString();
    }

    public static final Creator<Upgrade> CREATOR = new Creator<Upgrade>() {
        @Override
        public Upgrade createFromParcel(Parcel in) {
            return new Upgrade(in);
        }

        @Override
        public Upgrade[] newArray(int size) {
            return new Upgrade[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isForce ? 1 : 0));
        dest.writeString(prompt);
        dest.writeInt(rule);
        dest.writeString(url);
    }
}
