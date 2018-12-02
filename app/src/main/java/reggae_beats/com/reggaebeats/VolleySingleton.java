package reggae_beats.com.reggaebeats;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ron on 15/10/2017.
 */

public class VolleySingleton {
    private static VolleySingleton vInstance;
    private Context context;
    private RequestQueue requestQue;

    private VolleySingleton(Context ctext) {
        this.context = ctext;
        this.requestQue = getRequestQue();
    }

    private RequestQueue getRequestQue() {
        if (requestQue == null) {
            requestQue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQue;
    }


    public static synchronized VolleySingleton getInstance(Context ctx) {
        VolleySingleton newInstance;
        if (vInstance == null) {
            vInstance = new VolleySingleton(ctx);
        }

        return vInstance;
    }

    public <T> void AddToRequestQue(Request<T> request) {
        if (requestQue == null) {
            getRequestQue().add(request);
        } else {
            requestQue.add(request);
        }
    }
}
