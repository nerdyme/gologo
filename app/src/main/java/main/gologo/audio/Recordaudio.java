package main.gologo.audio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import main.gologo.R;
import main.gologo.constants.Constants;
import main.gologo.sendoptions.ContactOptions;

public class Recordaudio extends Activity {

    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private MediaRecorder recorder = null;
    private int currentFormat = 0;
    private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP };
    private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP };
    private String myfile=null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordaudio);

        setButtonHandlers();

        enableButtons(false);

        setFormatButtonCaption();

    }

    private void setButtonHandlers() {
        ((Button) findViewById(R.id.btnStart)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.btnStop)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.btnFormat)).setOnClickListener(btnClick);
    }

    private void enableButton(int id, boolean isEnable) {
        ((Button) findViewById(id)).setEnabled(isEnable);
    }

    private void enableButtons(boolean isRecording) {
        enableButton(R.id.btnStart, !isRecording);
        enableButton(R.id.btnFormat, !isRecording);
        enableButton(R.id.btnStop, isRecording);
    }

    private void setFormatButtonCaption() {
        ((Button) findViewById(R.id.btnFormat)).setText(getString(R.string.audio_format) + " (" + file_exts[currentFormat] + ")");
    }

    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
    }

    private void startRecording() {

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(output_formats[currentFormat]);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        myfile = getFilename();
        recorder.setOutputFile(myfile);
        Log.w("filename", getFilename());
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if ( recorder!=null) {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;

            new MyAsyncTask().execute(myfile);
            Intent i = new Intent(getApplicationContext(),ContactOptions.class);
            Bundle b1=new Bundle();
            b1.putString("ActivityName", "Recordaudio");
            b1.putString("filename",myfile);

            //Add the bundle to the intent
            i.putExtras(b1);
            startActivity(i);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Error in Loading Recorder",Toast.LENGTH_SHORT ).show();
        }

    }

    private void displayFormatDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String formats[] = { "MPEG 4", "3GP" };
        builder.setTitle(getString(R.string.choose_format_title)).setSingleChoiceItems(formats, currentFormat, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentFormat = which;
                setFormatButtonCaption();
                dialog.dismiss();
            }
        }).show();
    }

    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Toast.makeText(Recordaudio.this, "Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Toast.makeText(Recordaudio.this, "Warning: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener btnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnStart: {
                    Toast.makeText(Recordaudio.this, "Start Recording", Toast.LENGTH_SHORT).show();
                    enableButtons(true);
                    startRecording();
                    break;
                }
                case R.id.btnStop: {
                    Toast.makeText(Recordaudio.this, "Stop Recording", Toast.LENGTH_SHORT).show();
                    enableButtons(false);
                    stopRecording();
                    break;
                }
                case R.id.btnFormat: {
                    displayFormatDialog();
                    break;
                }
            }
        }
    };

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String res=	doFileUpload(params[0]);
            return res;
        }

        protected void onPostExecute(String str)
        {
            if (str != null) {

                if ( str.equalsIgnoreCase( "success"))
                {
                    Toast.makeText(getApplicationContext(), "Upload Successful" + str, Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Error in uploading" + str, Toast.LENGTH_LONG).show();
                }
            }

            else
            {
                Toast.makeText(getApplicationContext(), "Connection Error" + str, Toast.LENGTH_LONG).show();
            }
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
                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fname + "\"" + lineEnd);
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
                    Log.d("Error",conn.getResponseMessage());
                    Toast.makeText(getApplicationContext(),conn.getResponseMessage(),Toast.LENGTH_LONG);
                    return "failure";
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
                return "failure";
            }

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
