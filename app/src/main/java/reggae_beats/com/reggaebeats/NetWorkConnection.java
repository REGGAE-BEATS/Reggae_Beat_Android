package reggae_beats.com.reggaebeats;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*  Network connection objects*/
class NetWorkConnection {

    private StringBuilder strBuf;
    private String response = null;

    NetWorkConnection() {

    }

    String ServerConnectionResults(String query, String url) throws MalformedURLException {
        Log.i("querytag", query);

        try {

            URL urlConnect = new URL(url);

            // set the properties on the HttpConnnection object
            HttpURLConnection con = (HttpURLConnection) urlConnect.openConnection();
            con.setRequestMethod("POST");
            con.setReadTimeout(15000);
            con.setConnectTimeout(15000);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.connect();

            OutputStream out = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"), 8192);
            writer.write(query);
            writer.flush();

            int statusCode = con.getResponseCode();

            Log.d("rc", "msg" + statusCode);

            // Create a buffered reader to buffer and read inputstream
            InputStream in = con.getInputStream();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(in, "UTF-8"), 118192);

            // use a string to read the read the input

            String line;

            strBuf = new StringBuilder();
            while ((line = bfr.readLine()) != null)

            {

                strBuf.append(line);

            }
            return strBuf.toString();

        } catch (IOException e1) {
            e1.printStackTrace();

        }
        return response;
    }

    String RetrieveFromServer(String UrlRetrieve, String query2)

    {
        Log.i("querytagRetrieve", query2);
        try {

            URL urlConnect = new URL(UrlRetrieve);

            // set the properties on the HttpConnnection object
            HttpURLConnection con = (HttpURLConnection) urlConnect.openConnection();
            con.setRequestMethod("POST");
            con.setReadTimeout(15000);
            con.setConnectTimeout(15000);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.connect();

            OutputStream out = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new
                    OutputStreamWriter(out, "UTF-8"), 8192);
            writer.write(query2);
            writer.flush();

            int statusCode = con.getResponseCode();

            Log.d("rcReceive", "msg" + statusCode);

            // Create a buffered reader to buffer and read inputstream
            InputStream in = con.getInputStream();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8192);

            // use a string to read the read the input

            String line;

            strBuf = new StringBuilder();
            while ((line = bfr.readLine()) != null)

            {

                strBuf.append(line + "\n");

            }
            return strBuf.toString();

        } catch (IOException e1) {
            e1.printStackTrace();

        }
        return response;
    }

    String NetworkCallNoQuery(String url) throws MalformedURLException {


        try {

            URL urlConnect = new URL(url);

            // set the properties on the HttpConnnection object
            HttpURLConnection con = (HttpURLConnection) urlConnect.openConnection();
            con.setRequestMethod("POST");
            con.setReadTimeout(15000);
            con.setConnectTimeout(15000);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.connect();

            //OutputStream out = con.getOutputStream();
            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"), 8192);


            int statusCode = con.getResponseCode();

            Log.d("rc", "msg" + statusCode);

            // Create a buffered reader to buffer and read inputstream
            InputStream in = con.getInputStream();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8192);

            // use a string to read the read the input

            String line;

            strBuf = new StringBuilder();
            while ((line = bfr.readLine()) != null)

            {

                strBuf.append(line);

            }
            return strBuf.toString();

        } catch (IOException e1) {
            e1.printStackTrace();

        }
        return response;
    }


    int TestNetworkConnectivity(String UrlRetrieve)

    {
        int statusCode = 0;
        try {

            URL urlConnect = new URL(UrlRetrieve);

            // set the properties on the HttpConnnection object
            HttpURLConnection con = (HttpURLConnection) urlConnect.openConnection();
            con.setRequestMethod("POST");
            con.setReadTimeout(15000);
            con.setConnectTimeout(15000);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.connect();


            statusCode = con.getResponseCode();


        } catch (IOException e1) {
            e1.printStackTrace();

        }
        return statusCode;
    }


    public void doFileUpload(final String selectedPath, final Handler handler) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                DataInputStream inStream = null;
                String lineEnd = "rn";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                String responseFromServer = "";
                try {
                    //------------------ CLIENT REQUEST
                    FileInputStream fileInputStream = new FileInputStream(new File(selectedPath));
                    // open a URL connection to the Servlet
                    URL url = new URL("http://www.jpsbillfinder.com/jam_player/upload.php");
                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    // Allow Inputs
                    conn.setDoInput(true);
                    // Allow Outputs
                    conn.setDoOutput(true);
                    // Don't use a cached copy.
                    conn.setUseCaches(false);
                    // Use a post method.
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"UPLOADED_FILE\";filename=\""
                            + selectedPath + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    // close streams
                    Log.e("Debug", "File is written");
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (MalformedURLException ex) {
                    Log.e("Debug", "error: " + ex.getMessage(), ex);
                    sendMessageBack(responseFromServer, 0, handler);
                    return;
                } catch (IOException ioe) {
                    Log.e("Debug", "error: " + ioe.getMessage(), ioe);
                    sendMessageBack(responseFromServer, 0, handler);
                    return;
                }
                responseFromServer = processResponse(conn, responseFromServer);
                sendMessageBack(responseFromServer, 1, handler);
            }
        }).start();

    }

    private String processResponse(HttpURLConnection conn, String responseFromServer) {
        DataInputStream inStream;
        try {
            inStream = new DataInputStream(conn.getInputStream());
            String str;

            while ((str = inStream.readLine()) != null) {
                responseFromServer = str;
            }
            inStream.close();

        } catch (IOException ioex) {
            Log.e("Debug", "error: " + ioex.getMessage(), ioex);
        }
        return responseFromServer;
    }

    void sendMessageBack(String responseFromServer, int success, Handler handler) {
        Message message = new Message();
        message.obj = responseFromServer;
        message.arg1 = success;
        handler.sendMessage(message);
    }
}












