package softec19.com.softec19.Activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import softec19.com.softec19.R;

public class LiveStream extends AppCompatActivity implements OnPreparedListener {

    private VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream);
        videoView = findViewById(R.id.video_view1);
        videoView.setVideoURI(Uri.parse("http://45.77.88.101:8080/hls/mystream.m3u8"));
    }



    @Override
    public void onPrepared() {
        //Starts the video playback as soon as it is ready
        videoView.start();
    }


}
