package com.gourav.news.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

import androidx.room.ColumnInfo;

public class ArticleSource implements Parcelable {
    @ColumnInfo(name = "id")
    private final String id;
    @ColumnInfo(name = "name")
    private final String name;

    public ArticleSource(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @NotNull
    @Override
    public String toString() {
        return "ArticleSource{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    private ArticleSource(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Creator<ArticleSource> CREATOR = new Creator<ArticleSource>() {
        @Override
        public ArticleSource createFromParcel(Parcel source) {
            return new ArticleSource(source);
        }

        @Override
        public ArticleSource[] newArray(int size) {
            return new ArticleSource[size];
        }
    };
}
