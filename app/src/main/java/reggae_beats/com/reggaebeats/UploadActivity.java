package reggae_beats.com.reggaebeats;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RecoverySystem;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView Submit, selectFile;
    File file;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int SELECT_AUDIO = 2;
    private String selectedPath;
    private Handler handler;
    private TextView tvStatus;
    private EditText fileName;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        fileName = (EditText) findViewById(R.id.file_name);
        Submit = (ImageView) findViewById(R.id.submit_button);
        selectFile = (ImageView) findViewById(R.id.selectFile);


        Submit.setOnClickListener(this);
        selectFile.setOnClickListener(this);


        //check if permission granted to read external storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            } else {
                Toast.makeText(getApplicationContext(), "Permision has been granted ", Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:

                UploadAsyncTask uploadAsyncTask = new UploadAsyncTask();
                uploadAsyncTask.execute();
                Toast.makeText(getApplicationContext(), "Upload clicked", Toast.LENGTH_LONG).show();
                break;
            case R.id.selectFile:

                callDialogChooser();

                break;

        }

    }


    private void callDialogChooser() {
        AlertDialog alertDialog = new AlertDialog.Builder(UploadActivity.this)
                .setMessage("Please select the source of the data").setPositiveButton("SdCard ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                }).setNegativeButton("Phone Storage", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new MaterialFilePicker()
                                .withActivity(UploadActivity.this)
                                .withRequestCode(101)
                                .withFilterDirectories(true) // Set directories filterable (false by default)
                                .withHiddenFiles(true) // Show hidden files and folders
                                .start();
                    }
                }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        alertDialog.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults[0] == RESULT_OK) {


        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 101 && resultCode == RESULT_OK) {
            file = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));

        }

    }

    class UploadAsyncTask extends AsyncTask<String, Integer, String> implements RecoverySystem.ProgressListener {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(UploadActivity.this);
            progressDialog.setMessage("Downloading please wait....");
            progressDialog.show();

        }

        @Override
        protected void onCancelled() {
            cancel(true);
        }

        @Override
        protected String doInBackground(String... strings) {

            String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1);
            String contentType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            //Create a fileoutputstream to read the selected file
            String file_path = file.getAbsolutePath();
            OkHttpClient client = new OkHttpClient();
            RequestBody fileBody = RequestBody.create(MediaType.parse(contentType), file);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("type", contentType)
                    .addFormDataPart("UPLOADED_FILE", file_path.substring(file_path.lastIndexOf("/") + 1)

                            , fileBody).addFormDataPart("FILE_NAME", fileName.getText().toString()).build();
            Request request = new Request.Builder().url("http://www.jpsbillfinder.com/jam_player/upload.php")
                    .post(requestBody).build();
            Response response = null;
            String responseString = null;

            try

            {
                response = client.newCall(request).execute();

                try {
                    responseString = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (IOException e) {

            }
            return responseString;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {


            progressDialog.setMax(values[1]);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();

            }

        }

        @Override
        public void onProgress(int progress) {

        }
    }
}





