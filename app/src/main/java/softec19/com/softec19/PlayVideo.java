package softec19.com.softec19;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PlayVideo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        String shortCode = "ifjh";
        AndroidStreamable.getVideo(shortCode, new VideoCallback() {
            @Override
            public void onSuccess(int statusCode, MediaStore.Video video) {
                // :D
            }
            @Override
            public void onFailure(int statusCode, Throwable error) {
                // :(
            }
        });
    }
}
