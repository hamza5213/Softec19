package softec19.com.softec19.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.VideoDecoder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import softec19.com.softec19.Interfaces.OnListFragmentInteractionListener;
import softec19.com.softec19.Model.VideoModel;
import softec19.com.softec19.R;
import softec19.com.softec19.Utility.Categories;

import static android.support.v4.view.MenuItemCompat.getActionView;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener  {

    ArrayList<VideoModel> videos;
    private VideoRecyclerViewAdapter adapter;
    OnListFragmentInteractionListener mListener;
    ArrayAdapter<String> categoryAdapter;
    ArrayList<String> categories;
    int category = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MovieHub");
        categories = Categories.getCategories();
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        videos = new ArrayList<>();





        // startActivity(new Intent(this, PlayVideo.class));

        mListener = this;
        RecyclerView recyclerView = findViewById(R.id.videoRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoRecyclerViewAdapter(videos, this, mListener);
        recyclerView.setAdapter(adapter);

        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(categoryAdapter);


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                category = i;
                fetchVideo(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload_icon:


                startActivity(new Intent(this,UploadVideo.class));
                return true;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListFragmentInteraction(Bundle details, String action, boolean isFabClicked) {
        Intent intent = new Intent(this,VideoDisplay.class);
        intent.putExtra("videoId",details.getString("videoId"));
        intent.putExtra("name",details.getString("name"));
        intent.putExtra("upvotes",details.getString("upvotes"));
        intent.putExtra("downvotes",details.getString("downvotes"));
        intent.putExtra("uploader",details.getString("uploader"));
        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_spinner, menu);
        return true;
    }

    void fetchVideo(int index){
        videos.clear();
        adapter.notifyDataSetChanged();
        DatabaseReference vR=FirebaseDatabase.getInstance().getReference().child("VideoGenre").child(categories.get(index));
        vR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.getValue(Boolean.class)==true) {
                            String id = snapshot.getKey();
                            fetchvmodel(id);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void fetchvmodel(final String id)
    {
        FirebaseDatabase.getInstance().getReference().child("Videos").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    VideoModel video = dataSnapshot.getValue(VideoModel.class);
                    video.setVideoId(id);
                    videos.add(video);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("Sample Advertisement");
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdClosed() {

            }
        });
        return interstitialAd;
    }


}
