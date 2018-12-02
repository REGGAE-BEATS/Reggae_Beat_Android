package reggae_beats.com.reggaebeats;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

public class MusicPlayerAdapter extends RecyclerView.Adapter<MusicPlayerAdapter.MusicViewHolder> {
    private Context context;

    private ArrayList<Albums> albums;
    private ImageLoader imageLoader;
    private onItemClickListener mListener;

    public interface onItemClickListener {

        void onClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;

    }

    public MusicPlayerAdapter(Context mTCX, ArrayList<Albums> listAlbums) {
        this.albums = listAlbums;
        this.context = mTCX;

        DisplayImageOptions defaultImageOptions = new DisplayImageOptions.Builder().

                cacheInMemory(true).cacheOnDisk(true).
                showImageForEmptyUri(R.mipmap.ic_notification1).showImageOnLoading(R.mipmap.ic_notification1)
                .build();
        ImageLoaderConfiguration imageLoaderConfig = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2).
                        denyCacheImageMultipleSizesInMemory().
                        diskCacheFileNameGenerator(new Md5FileNameGenerator()).
                        tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(defaultImageOptions).
                        build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(imageLoaderConfig);


        //imageLoader = ImageLoader.getInstance();
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* need to onflate the card layout*/
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_playlist_view, parent, false);

        return new MusicViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        imageLoader.displayImage(Uri.parse("http://www.jpsbillfinder.com/jam_player/album_cover_images/" + albums.get(position).getAlbumImage()).toString(), holder.albumImageView);
        holder.albumName.setText(albums.get(position).getAlbumName());
        holder.artist.setText(albums.get(position).getArtist());
        holder.size.setText(albums.get(position).getSize());
        holder.views.setText(albums.get(position).getViews());
    }

    @Override
    public int getItemCount() {
        return albums.size();

    }

    public static class MusicViewHolder extends RecyclerView.ViewHolder {
        private ImageView albumImageView;
        private TextView albumName, artist, views, size;

        public MusicViewHolder(View view, final onItemClickListener listener) {
            super(view);


            albumImageView = view.findViewById(R.id.album_image);
            albumName = view.findViewById(R.id.album_name);
            artist = view.findViewById(R.id.artist);
            views = view.findViewById(R.id.views);
            size = view.findViewById(R.id.size);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onClick(position);
                        }
                    }
                }
            });

        }
    }

}