package reggae_beats.com.reggaebeats;

import android.os.Parcel;
import android.os.Parcelable;

public class Albums implements Parcelable {
    private String albumName;
    private String artist;
    private String views;
    private String size;
    private String song_url;

    protected Albums(Parcel in) {
        albumName = in.readString();
        artist = in.readString();
        views = in.readString();
        size = in.readString();
        song_url = in.readString();
        albumImage = in.readString();
    }

    public static final Creator<Albums> CREATOR = new Creator<Albums>() {
        @Override
        public Albums createFromParcel(Parcel in) {
            return new Albums(in);
        }

        @Override
        public Albums[] newArray(int size) {
            return new Albums[size];
        }
    };

    public String getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(String albumImage) {
        this.albumImage = albumImage;
    }

    private String albumImage;

    public Albums(String albumName, String artist, String views, String size, String albumImage, String song_url) {
        this.albumName = albumName;
        this.artist = artist;
        this.views = views;
        this.size = size;
        this.albumImage = albumImage;
        this.song_url = song_url;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSong_url() {
        return song_url;
    }

    public void setSong_url(String song_url) {
        this.song_url = song_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumName);
        dest.writeString(artist);
        dest.writeString(views);
        dest.writeString(size);
        dest.writeString(song_url);
        dest.writeString(albumImage);
    }
}
