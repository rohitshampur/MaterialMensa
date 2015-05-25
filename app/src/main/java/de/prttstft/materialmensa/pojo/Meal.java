package de.prttstft.materialmensa.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Meal implements Parcelable {
    private long id;
    private String title;
    private Date releaseDateTheater;
    private int audienceScore;
    private String synopsis;
    private String urlThumbnail;
    private String urlSelf;
    private String urlCast;
    private String urlReviews;
    private String urlSimilar;

    public Meal() {

    }

    public Meal(Parcel input) {
        id = input.readLong();
        title = input.readString();
        releaseDateTheater = new Date (input.readLong());
        audienceScore = input.readInt();
        synopsis = input.readString();
        urlThumbnail = input.readString();
        urlSelf = input.readString();
        urlCast = input.readString();
        urlReviews = input.readString();
        urlSimilar = input.readString();

    }

    public Meal(long id,
                String title,
                Date releaseDateTheater,
                int audienceScore,
                String synopsis,
                String urlThumbnail,
                String urlSelf,
                String urlCast,
                String urlReviews,
                String urlSimilar) {
        this.id = id;
        this.title = title;
        this.releaseDateTheater = releaseDateTheater;
        this.audienceScore = audienceScore;
        this.synopsis = synopsis;
        this.urlThumbnail = urlThumbnail;
        this.urlSelf = urlSelf;
        this.urlCast = urlCast;
        this.urlReviews = urlReviews;
        this.urlSimilar = urlSimilar;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDateTheater() {
        return releaseDateTheater;
    }

    public void setReleaseDateTheater(Date releaseDateTheater) {
        this.releaseDateTheater = releaseDateTheater;
    }

    public int getAudienceScore() {
        return audienceScore;
    }

    public void setAudienceScore(int audienceScore) {
        this.audienceScore = audienceScore;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
    }

    public String getUrlSelf() {
        return urlSelf;
    }

    public void setUrlSelf(String urlSelf) {
        this.urlSelf = urlSelf;
    }

    public String getUrlCast() {
        return urlCast;
    }

    public void setUrlCast(String urlCast) {
        this.urlCast = urlCast;
    }

    public String getUrlReviews() {
        return urlReviews;
    }

    public void setUrlReviews(String urlReviews) {
        this.urlReviews = urlReviews;
    }

    public String getUrlSimilar() {
        return urlSimilar;
    }

    public void setUrlSimilar(String urlSimilar) {
        this.urlSimilar = urlSimilar;
    }

    @Override
    public String toString() {
        return "\nID: " + id +
                "\nTitle " + title +
                "\nDate " + releaseDateTheater +
                "\nSynopsis " + synopsis +
                "\nScore " + audienceScore +
                "\nurlThumbnail " + urlThumbnail +
                "\nurlSelf " + urlSelf +
                "\nurlCast " + urlCast +
                "\nurlReviews " + urlReviews +
                "\nurlSimilar " + urlSimilar +
                "\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeLong(releaseDateTheater.getTime());
        dest.writeInt(audienceScore);
        dest.writeString(synopsis);
        dest.writeString(urlThumbnail);
        dest.writeString(urlSelf);
        dest.writeString(urlCast);
        dest.writeString(urlReviews);
        dest.writeString(urlSimilar);
    }

    public static final Parcelable.Creator<Meal> CREATOR = new Parcelable.Creator<Meal>() {
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        public Meal[] newArray (int size) {
            return new Meal[size];
        }
    };
}