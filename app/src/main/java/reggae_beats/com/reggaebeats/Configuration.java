package reggae_beats.com.reggaebeats;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by ron on 13/08/2017.
 */

public class Configuration {
    private Context context;

    public Configuration(Context ctx) {
        this.context = ctx;

    }


    public boolean IsNetworkAvailable() {

        NetworkInfo networkInfo;

        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = conManager.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {


            return true;


        } else {
            Toast.makeText(context, "Please check your internet service", Toast.LENGTH_LONG).show();

        }

        return false;


    }

    public boolean isInternetAvailable() throws IOException, InterruptedException {


        String command = "ping -c 1 google.com";


        if ((Runtime.getRuntime().exec(command).waitFor() == 0)) {


        } else {

            Toast.makeText(context.getApplicationContext(), "Please check your internet service", Toast.LENGTH_LONG).show();
        }

        return true;


    }
}
