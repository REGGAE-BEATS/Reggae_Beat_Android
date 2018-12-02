package reggae_beats.com.reggaebeats;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by ron on 27/10/2017.
 */

public class ImageLoaderClass extends Application {
    Context context;
    private ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
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
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(imageLoaderConfig);

    }
}
