package com.dayat.submission3.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserItems implements Parcelable {

    private int id;
    private String login;
    private String avatar_url;

    public UserItems(int id, String login, String avatar_url) {
        this.id = id;
        this.login = login;
        this.avatar_url = avatar_url;
    }

    public UserItems() {
    }

    protected UserItems(Parcel in) {
        id = in.readInt();
        login = in.readString();
        avatar_url = in.readString();
    }

    public static final Creator<UserItems> CREATOR = new Creator<UserItems>() {
        @Override
        public UserItems createFromParcel(Parcel in) {
            return new UserItems(in);
        }

        @Override
        public UserItems[] newArray(int size) {
            return new UserItems[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(login);
        dest.writeString(avatar_url);
    }
}
