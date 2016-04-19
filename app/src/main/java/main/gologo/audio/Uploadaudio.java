package main.gologo.audio;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import main.gologo.constants.Constants;

/**
 * Created by surbhi on 4/18/16.
 */
public class Uploadaudio extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub

        String res=	doFileUpload(params[0]);
        return res;
    }

    protected void onPostExecute(String str)
    {

    }
    protected void onProgressUpdate(Integer... progress){
        //pb.setProgress(progress[0]);
    }


    private String doFileUpload(String filename)

    {
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String existingFileName = filename;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;

        byte[] buffer;

        int maxBufferSize = 1 * 1024 * 1024;
        String responseFromServer = "";
        String urlString = Constants.recording;

        try {

            //------------------ CLIENT REQUEST
            File f = new File(existingFileName);
            String fname= f.getName();
            FileInputStream fileInputStream = new FileInputStream(f);
            Log.d("open","File is opened");
            // open a URL connection to the Servlet
            URL url = new URL(urlString);
            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) url.openConnection();
            Log.d("yay", "able to open  connection1");
            // Allow Inputs
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);

            Log.d("yay", "able to open  connection2");
            // Use a post method.
            conn.setRequestMethod("POST");
            Log.d("yay", "able to open  connection3");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            Log.d("yay","able to open  connection4");
            dos = new DataOutputStream(conn.getOutputStream());
            Log.d("yay", "able to open  connection5");
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            //String s1="\"Content-Disposition: form-data; name=\\\"uploadedfile\\\";filename=\\\"\" + fname + \"\\\"\" + lineEnd"
            dos .writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fname + "\"" + lineEnd);
            //
            dos.writeBytes(lineEnd);
            Log.d("yay","able to open  connection6");
            // create a buffer of maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            Log.d("yay","able to open  connection7");
            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                Log.d("yay","able to open  connection8");
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // close streams
            dos.writeBytes("Content-Disposition: form-data; name=\"filename\";filename=\"" + existingFileName + "\"" + lineEnd);
            Log.e("Debug", "File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();
            Log.d("yay","able to open  connection9");

        } catch (MalformedURLException ex) {
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        } catch (IOException ioe) {
            Log.e("Debug", "error: " + ioe.getMessage(), ioe);
        }

        //------------------ read the SERVER RESPONSE

        try {
            if(conn.getResponseCode()==201 || conn.getResponseCode()==200)
            {
                return "success";

            }
            else
            {
                Log.d("Error", conn.getResponseMessage());
                return "failure";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            return "failure";
        }

    }



    private StringBuilder inputStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        // Read response until the end
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Return full string
        return total;
    }
}