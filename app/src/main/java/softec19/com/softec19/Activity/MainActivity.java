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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import softec19.com.softec19.Interfaces.OnListFragmentInteractionListener;
import softec19.com.softec19.Model.VideoModel;
import softec19.com.softec19.R;

import static android.support.v4.view.MenuItemCompat.getActionView;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener , AdapterView.OnItemSelectedListener {

    ArrayList<VideoModel> videos;
    private VideoRecyclerViewAdapter adapter;
    OnListFragmentInteractionListener mListener;
    ArrayList<String> categories;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MovieHub");

        categories = new ArrayList<>();
        categories.add("Horror");
        categories.add("Sports");


        mListener = this;
        RecyclerView recyclerView = findViewById(R.id.videoRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoRecyclerViewAdapter(videos, this, mListener);
        recyclerView.setAdapter(adapter);

        videos = new ArrayList<>();
        VideoModel vid = new VideoModel("thehobbit-thumb", "The Hobbit", "asdas", "43", "4", " ","");
        videos.add(vid);

        vid = new VideoModel("thehobbit-thumb", "The Hobbit", "asdas", "43", "4", " ","");
        videos.add(vid);

        vid = new VideoModel("thehobbit-thumb", "The Hobbit", "asdas", "43", "4", " ","");
        videos.add(vid);
        AndroidStreamable.setCredentials("maxer232@gmail.com", "hamza5213");
       // startActivity(new Intent(this, PlayVideo.class));

    }


    @Override
    public void onListFragmentInteraction(Bundle details, String action, boolean isFabClicked) {

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_spinner, menu);



        // Spinner click listener



        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) item.getActionView();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);
        return true;
    }
}
