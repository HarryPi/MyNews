package com.example.harry.mynews.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class IntentExtraRegistration implements Parcelable {

    private String email;
    private String password;
    private boolean returnedBeforeEnd;

    public IntentExtraRegistration(){}
    public IntentExtraRegistration(Parcel in) {
        this.email = in.readString();
        this.password = in.readString();
        this.returnedBeforeEnd = in.readByte() != 0;
    }

    public IntentExtraRegistration(boolean returnedBeforeEnd, String email, String password) {
        this.email = email;
        this.password = password;
        this.returnedBeforeEnd = returnedBeforeEnd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeByte((byte) (returnedBeforeEnd ? 1 : 0));
    }

    public static final Parcelable.Creator<IntentExtraRegistration> CREATOR = new Parcelable.Creator<IntentExtraRegistration>() {

        @Override
        public IntentExtraRegistration createFromParcel(Parcel parcel) {
            return new IntentExtraRegistration(parcel);
        }

        @Override
        public IntentExtraRegistration[] newArray(int i) {
            return new IntentExtraRegistration[i];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isReturnedBeforeEnd() {
        return returnedBeforeEnd;
    }

    public void setReturnedBeforeEnd(boolean returnedBeforeEnd) {
        this.returnedBeforeEnd = returnedBeforeEnd;
    }

}
