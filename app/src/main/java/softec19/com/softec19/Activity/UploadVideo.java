package softec19.com.softec19.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import cafe.adriel.androidstreamable.AndroidStreamable;
import cafe.adriel.androidstreamable.callback.NewVideoCallback;
import cafe.adriel.androidstreamable.model.NewVideo;
import softec19.com.softec19.Adapters.VideoRecyclerViewAdapter;
import softec19.com.softec19.Model.VideoModel;
import softec19.com.softec19.R;

public class UploadVideo extends AppCompatActivity {

    VideoPicker videoPicker;
    ArrayList<String> categories;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        AndroidStreamable.setCredentials("maxer232@gmail.com", "hamza5213");
        videoPicker = new VideoPicker.Builder(UploadVideo.this)
                .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                .directory(VideoPicker.Directory.DEFAULT)
                .extension(VideoPicker.Extension.MP4)
                .enableDebuggingMode(true)
                .build();
        categories = new ArrayList<>();
        categories.add("Horror");
        categories.add("Sports");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            final String videoName = "Happy";
            ArrayList<String> mPaths = data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
            final String tempPath = mPaths.get(0);
            try {
                // File file = new File(mPaths.get(0));
                InputStream is = new FileInputStream(mPaths.get(0));
                AndroidStreamable.uploadVideo(is, videoName, new NewVideoCallback() {
                    @Override
                    public void onSuccess(int statusCode, NewVideo newVideo) {
                        Toast.makeText(UploadVideo.this, "Hurray", Toast.LENGTH_LONG).show();
                        VideoModel videoModel = new VideoModel(null, videoName, "", "0", "0", FirebaseAuth.getInstance().getUid(), newVideo.getShortCode(),categories.get(index));
                        DatabaseReference videoRf = FirebaseDatabase.getInstance().getReference().child("Videos");
                        String key = videoRf.push().getKey();
                        videoRf.child(key).setValue(videoModel);
                        FirebaseDatabase.getInstance().getReference().child("VideoGenre").child(categories.get(index)).child(key).setValue(true);
                        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(tempPath,
                                MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
                        StorageReference videoRef = FirebaseStorage.getInstance().getReference().child("VideoThumbnail/" + key + ".jpg");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        thumb.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        UploadTask uploadTask = videoRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                exception.printStackTrace();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                                Toast.makeText(UploadVideo.this, "Hurray", Toast.LENGTH_LONG).show();
                            }
                        });


                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error) {
                        Toast.makeText(UploadVideo.this, "ohho", Toast.LENGTH_LONG).show();
                        System.out.println(error.getMessage());
                        error.printStackTrace();
                    }
                });
            } catch (Exception e) {




            }


        }
    }
}
