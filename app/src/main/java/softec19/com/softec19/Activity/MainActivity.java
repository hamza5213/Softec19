package softec19.com.softec19.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cafe.adriel.androidstreamable.AndroidStreamable;
import cafe.adriel.androidstreamable.callback.NewVideoCallback;
import cafe.adriel.androidstreamable.model.NewVideo;
import softec19.com.softec19.R;

public class MainActivity extends AppCompatActivity {

    VideoPicker videoPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, PlayVideo.class));
        videoPicker= new VideoPicker.Builder(MainActivity.this)
                .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                .directory(VideoPicker.Directory.DEFAULT)
                .extension(VideoPicker.Extension.MP4)
                .enableDebuggingMode(true)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> mPaths =  data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
            try {
                File file = new File(mPaths.get(0));
                AndroidStreamable.uploadVideo(file, "this", new NewVideoCallback() {
                    @Override
                    public void onSuccess(int statusCode, NewVideo newVideo) {
                        Toast.makeText(MainActivity.this,"Hurray",Toast.LENGTH_LONG ).show();
                    }
                    @Override
                    public void onFailure(int statusCode, Throwable error) {
                        Toast.makeText(MainActivity.this,"ohho",Toast.LENGTH_LONG ).show();
                    }
                });
            }
            catch (Exception e){

            }

        }
    }



}
