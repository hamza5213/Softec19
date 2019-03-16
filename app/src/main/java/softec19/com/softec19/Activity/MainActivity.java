package softec19.com.softec19.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import softec19.com.softec19.R;

import static android.support.v4.view.MenuItemCompat.getActionView;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener , AdapterView.OnItemSelectedListener {

    ArrayList<VideoModel> videos;
    private VideoRecyclerViewAdapter adapter;
    OnListFragmentInteractionListener mListener;
    ArrayList<String> categories;

    VideoPicker videoPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MovieHub");

        categories = new ArrayList<>();
        categories.add("Horror");
        categories.add("Sports");

        videos = new ArrayList<>();
        VideoModel vid = new VideoModel("thehobbit-thumb", "The Hobbit", "asdas", "43", "4", " ");
        videos.add(vid);

        vid = new VideoModel("thehobbit-thumb", "The Hobbit", "asdas", "43", "4", " ");
        videos.add(vid);

        vid = new VideoModel("thehobbit-thumb", "The Hobbit", "asdas", "43", "4", " ");
        videos.add(vid);
        AndroidStreamable.setCredentials("maxer232@gmail.com", "hamza5213");
       // startActivity(new Intent(this, PlayVideo.class));
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
            final String videoName="Happy";
            ArrayList<String> mPaths =  data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
            final String tempPath=mPaths.get(0);
            try {
               // File file = new File(mPaths.get(0));
                InputStream is = new FileInputStream(mPaths.get(0));
                AndroidStreamable.uploadVideo(is, videoName, new NewVideoCallback() {
                    @Override
                    public void onSuccess(int statusCode, NewVideo newVideo) {
                        Toast.makeText(MainActivity.this,"Hurray",Toast.LENGTH_LONG ).show();
                        VideoModel videoModel=new VideoModel(null,videoName,"","0","0", FirebaseAuth.getInstance().getUid(),newVideo.getShortCode());
                        DatabaseReference videoRf= FirebaseDatabase.getInstance().getReference().child("Videos");
                        String key=videoRf.push().getKey();
                        videoRf.child(key).setValue(videoModel);
                        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(tempPath,
                                MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
                        StorageReference videoRef= FirebaseStorage.getInstance().getReference().child("VideoThumbnail/"+key+".jpg");
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
                                Toast.makeText(MainActivity.this,"Hurray",Toast.LENGTH_LONG ).show();
                            }
                        });


                    }
                    @Override
                    public void onFailure(int statusCode, Throwable error) {
                        Toast.makeText(MainActivity.this,"ohho",Toast.LENGTH_LONG ).show();
                        System.out.println(error.getMessage());
                        error.printStackTrace();
                    }
                });
            }
            catch (Exception e){

        mListener = this;
        RecyclerView recyclerView = findViewById(R.id.videoRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoRecyclerViewAdapter(videos, this, mListener);
        recyclerView.setAdapter(adapter);


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
