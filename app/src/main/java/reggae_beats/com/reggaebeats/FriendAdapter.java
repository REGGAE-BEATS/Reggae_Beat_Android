package reggae_beats.com.reggaebeats;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by ron on 03/10/2017.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {


    private Context context;
    private int Layout;

    private ArrayList<FriendsItem> arraylist;
    File dir;
    String[] imageArray;
    private ImageLoader imageLoader;
    private String sdcardOrMemory = null;
    private onItemSelectedListener mlistener;

    public FriendAdapter(Context ctx, ArrayList<FriendsItem> retrievedArrayList, int LayoutId) {
        this.context = ctx;
        this.Layout = LayoutId;
        this.arraylist = retrievedArrayList;
        DisplayImageOptions defaultImageOptions = new DisplayImageOptions.Builder().

                cacheInMemory(true).cacheOnDisk(true).
                showImageForEmptyUri(R.mipmap.ic_launcher_foreground).showImageOnLoading(R.mipmap.ic_launcher_foreground)
                .build();
        ImageLoaderConfiguration imageLoaderConfig = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2).
                        denyCacheImageMultipleSizesInMemory().
                        diskCacheFileNameGenerator(new Md5FileNameGenerator()).
                        tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(defaultImageOptions).
                        build();
        this.imageLoader = ImageLoader.getInstance();
        this.imageLoader.init(imageLoaderConfig);

    }


    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_friend_layout, parent, false);
        FriendViewHolder friendViewHolder = new FriendViewHolder(view, mlistener);

        return friendViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {

        imageLoader.displayImage(Uri.parse("http://www.jpsbillfinder.com/inventory_images/" + arraylist.get(position).getFriendImage()).toString(), holder.friendImage);

        holder.friendName.setText(arraylist.get(position).getFriendName());
        //get selected item


    }

    public void setOnItemClickListener(onItemSelectedListener mItemClicklistener) {
        mlistener = mItemClicklistener;

    }

    public interface onItemSelectedListener {
        void onItemSelected(int position);
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        ImageView friendImage;

        TextView friendName;


        public FriendViewHolder(View v, final onItemSelectedListener listener) {
            super(v);


            friendName = (TextView) v.findViewById(R.id.friend_Name);
            friendImage = (ImageView) v.findViewById(R.id.friend_Image);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {

                        int position = getAdapterPosition();
                        if (position != AdapterView.INVALID_POSITION) {
                            listener.onItemSelected(position);
                        }
                    }
                }

                ;


            });


    /*@Override
    public int getCount() {

        return arraylist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder {
        ImageView friendImage;

        TextView friendName;

        public ViewHolder(View v) {
            friendName = (TextView) v.findViewById(R.id.friend_Name);
            friendImage = (ImageView) v.findViewById(R.id.friend_Image);


        }


    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String itemSuffix = arraylist.get(i).getFriendImage();

        String url = "http://www.jpsbillfinder.com/jam_player/friends_images/";

        View row = view;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_friend_layout, viewGroup, false);
            holder = new ViewHolder(row);

            Toast.makeText(context,"IMAGES IN ADAPTER" + i,Toast.LENGTH_LONG).show();
            row.setTag(holder);


        } else {

            holder = (ViewHolder) row.getTag();

            Toast.makeText(context,"IMAGES IN ADAPTER AFTER CONVERTVIEW" + i,Toast.LENGTH_LONG).show();
            imageLoader.displayImage(Uri.parse("http://www.jpsbillfinder.com/inventory_images/" + itemSuffix).toString(),holder.friendImage);
            //imageLoader.displayImage(Uri.parse("http://www.jpsbillfinder.com/jam_player/friends_images/" ).toString(), holder.friendImage);
            holder.friendName.setText(arraylist.get(i).getFriendName());
        }


        return row;
    }*/
        }
    }
}
