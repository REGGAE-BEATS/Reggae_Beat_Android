package reggae_beats.com.reggaebeats;

import android.widget.TextView;

public class SpinnerItemClass {
    private TextView artist_name;

    public SpinnerItemClass(TextView artist_name) {

        this.artist_name = artist_name;
    }

    public TextView getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(TextView artist_name) {
        this.artist_name = artist_name;
    }
}
