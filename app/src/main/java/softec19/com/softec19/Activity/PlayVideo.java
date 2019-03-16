package softec19.com.softec19.Activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import cafe.adriel.androidstreamable.AndroidStreamable;
import cafe.adriel.androidstreamable.callback.VideoCallback;
import cafe.adriel.androidstreamable.model.Video;
import softec19.com.softec19.R;

public class PlayVideo extends AppCompatActivity implements OnPreparedListener {

    private VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        videoView = findViewById(R.id.video_view);


      /*  String shortCode = "oapvz";
        AndroidStreamable.getVideo(shortCode, new VideoCallback() {
            @Override
            public void onSuccess(int statusCode, Video video) {
                videoView.setOnPreparedListener(PlayVideo.this);
                String url = video.getFiles().get("mp4").getUrl();
                url = "https:"+url.split(":",2)[1];
                System.out.println(url);
                videoView.setVideoURI(Uri.parse(url));
            }
            @Override
            public void onFailure(int statusCode, Throwable error) {
                // :(
            }
        });*/
        videoView.setVideoURI(Uri.parse("rtmp://livestfslivefs.fplive.net/livestfslive-live/"));


    }
    @Override
    public void onPrepared() {
        //Starts the video playback as soon as it is ready
        videoView.start();
    }
}
