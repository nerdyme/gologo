package main.gologo.audio;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import main.gologo.R;
import main.gologo.home.BaseActionbar;
import main.gologo.sendoptions.ContactOptions;

public class Recordaudio extends BaseActionbar {

    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private MediaRecorder recorder = null;
    private int currentFormat = 0;
    //private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP };
    //private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP };
    private String myfile = "";
    EditText txtcount;

    CountDownTimer t;
    int cnt = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordaudio);
        txtcount = (EditText) findViewById(R.id.timer);
        txtcount.setInputType(InputType.TYPE_NULL);
        txtcount.setTextIsSelectable(true);

        setButtonHandlers();

        enableButtons(false);

        t = new CountDownTimer(Long.MAX_VALUE, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                cnt++;
                String time = new Integer(cnt).toString();

                long millis = cnt;
                int seconds = (int) (millis / 60);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                txtcount.setText(String.format("%d:%02d:%02d", minutes, seconds, millis));
            }

            @Override
            public void onFinish() {
                txtcount.setText(R.string.audio_recorded);
            }
        };

    }

    private void setButtonHandlers() {
        ((ImageButton) findViewById(R.id.record1)).setOnClickListener(btnClick);
        ((ImageButton) findViewById(R.id.record2)).setOnClickListener(btnClick);
    }

    private void enableButton(int id, boolean isEnable) {

        if (isEnable)
            ((LinearLayout) findViewById(id)).setVisibility(View.VISIBLE);
        else
            ((LinearLayout) findViewById(id)).setVisibility(View.GONE);
    }

    private void enableButtons(boolean isRecording) {
        enableButton(R.id.start1, !isRecording);
        enableButton(R.id.stop1, isRecording);
    }

    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        System.out.println(sdf.format(cal.getTime()));

        return (file.getAbsolutePath() + "/" + sdf.format(cal.getTime()) + ".mp3");
    }

    private void startRecording() {

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;

            //new MyAsyncTask().execute(myfile);
            Intent i = new Intent(getApplicationContext(), ContactOptions.class);
            Bundle b1 = new Bundle();
            b1.putString("ActivityName", "Recordaudio");
            b1.putString("FileName", myfile);

            //Add the bundle to the intent
            i.putExtras(b1);
            startActivity(i);
        } else {
            Snackbar.make(findViewById(android.R.id.content), R.string.Error_in_Loading_Recorder, Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
        }

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
                case R.id.record1: {
                    Snackbar.make(findViewById(android.R.id.content), R.string.StartRecording, Snackbar.LENGTH_LONG).show();
                    enableButtons(true);
                    t.start();
                    startRecording();
                    break;
                }
                case R.id.record2: {
                    Snackbar.make(findViewById(android.R.id.content), R.string.StopRecording, Snackbar.LENGTH_LONG).show();
                    enableButtons(false);
                    t.cancel();
                    txtcount.setText("00:00:00");
                    stopRecording();
                    finish();
                    break;
                }
            }
        }
    };

}
