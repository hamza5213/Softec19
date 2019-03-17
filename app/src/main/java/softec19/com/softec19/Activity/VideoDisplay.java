package softec19.com.softec19.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import softec19.com.softec19.Adapters.CommentRecyclerViewAdapter;
import softec19.com.softec19.Adapters.VideoRecyclerViewAdapter;
import softec19.com.softec19.Interfaces.OnListFragmentInteractionListener;
import softec19.com.softec19.Model.CommentsModel;
import softec19.com.softec19.Model.UserProfileModel;
import softec19.com.softec19.Model.VideoModel;
import softec19.com.softec19.R;

public class VideoDisplay extends AppCompatActivity implements OnListFragmentInteractionListener {
    String videoId;
    EditText commentBox;
    ArrayList<CommentsModel> commentsArray;
    String commentBucketId;
    ArrayList<String> userUpVotes;
    ArrayList<String> userDownVotes;
    FirebaseStorage storage;
    int voteState;
    String upvotes;
    String downvotes;
    ImageButton upvoteButton;
    ImageButton downvoteButton;
    CommentRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_display);
        videoId=getIntent().getStringExtra("videoId");
        String name = getIntent().getStringExtra("name");
        upvotes= getIntent().getStringExtra("upvotes");
        downvotes = getIntent().getStringExtra("downvotes");
        String uploader = getIntent().getStringExtra("uploader");
        upvoteButton =  (ImageButton) this.findViewById(R.id.upvote);
        downvoteButton =  (ImageButton) this.findViewById(R.id.downvote);


        FirebaseDatabase.getInstance().getReference().child("User").child(uploader).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    UserProfileModel userProfileModel = dataSnapshot.getValue(UserProfileModel.class);
                    ((TextView)findViewById(R.id.uploaded_by)).setText(userProfileModel.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        ((TextView)findViewById(R.id.video_name)).setText(name);
        ((TextView)findViewById(R.id.upvotes)).setText(upvotes);
        ((TextView)findViewById(R.id.downvotes)).setText(downvotes);


        commentsArray=new ArrayList<>();
        commentBox=findViewById(R.id.UserComment);
        userUpVotes=new ArrayList<>();
        userDownVotes=new ArrayList<>();
        fetchComments(videoId);
        fetchUserVotes();
        storage = FirebaseStorage.getInstance();

        StorageReference ref = storage.getReference().child("VideoThumbnail/" + videoId + ".jpg");
        Glide.with(this.getApplicationContext()).load(ref).into((ImageView)findViewById(R.id.video_image));



        RecyclerView recyclerView = findViewById(R.id.comments_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentRecyclerViewAdapter(commentsArray,this);
        recyclerView.setAdapter(adapter);



    }

    void fetchComments(final String id)
    {
        final String ID=id;
       FirebaseDatabase.getInstance().getReference().child("Videos").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                VideoModel videoModel=dataSnapshot.getValue(VideoModel.class);
                videoModel.setCommentBucketId(ID);
                if(videoModel.getCommentBucketId().equals(""))
                {
                    String s=FirebaseDatabase.getInstance().getReference().child("Video").child(videoId).push().getKey();
                    FirebaseDatabase.getInstance().getReference().child("Video").child(videoId).child("commentBucketId").setValue(s);
                    commentBucketId=s;
                }
                else
                {
                    commentBucketId=videoModel.getCommentBucketId();
                    FirebaseDatabase.getInstance().getReference().child("CommentBucket").child(videoModel.getCommentBucketId()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.getValue()!=null)
                            {
                                commentsArray.add(0,dataSnapshot.getValue(CommentsModel.class));
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onCommentAdd(View view){

        String text=commentBox.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("Name",null);
        CommentsModel commentsModel=new CommentsModel(null, FirebaseAuth.getInstance().getUid(),text,username);
        FirebaseDatabase.getInstance().getReference().child("CommentBucket").child(commentBucketId).push().setValue(commentsModel);
    }

    void fetchUserVotes()
    {
        String userId=FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance().getReference().child("UserUpVotes").child(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getValue()!=null){
                    if(dataSnapshot.getValue(Boolean.class)==true){
                        userUpVotes.add(dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("UserDownVotes").child(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getValue()!=null){
                    if(dataSnapshot.getValue(Boolean.class)==true){
                        userDownVotes.add(dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(userUpVotes.contains(videoId)){
            voteState=1;
            //TODO user has upvoted
            upvoteButton.setColorFilter(Color.argb(255, 0, 0, 255));

        }
        else if(userDownVotes.contains(videoId)){
            voteState=2;
            downvoteButton.setColorFilter(Color.argb(255, 0, 0, 255));
        }
    }

    public void upvoteUpdate(boolean increment)
    {
        String newUpvotes;
        if(increment)
        {

            newUpvotes = Integer.toString(Integer.parseInt(upvotes)+1);
        }
        else
        {

            newUpvotes = Integer.toString(Integer.parseInt(upvotes)-1);
        }
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Videos").child(videoId).child("upvoteCount");
        ref.setValue(newUpvotes);
        ((TextView)findViewById(R.id.upvotes)).setText(newUpvotes);
        upvotes=newUpvotes;
        System.out.println(upvotes);

    }

    public void downvoteUpdate(boolean increment)
    {
        String newDownvotes;
        if(increment)
        {

            newDownvotes = Integer.toString(Integer.parseInt(downvotes)+1);
        }
        else
        {

            newDownvotes = Integer.toString(Integer.parseInt(downvotes)-1);
        }
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Videos").child(videoId).child("downnvoteCount");
        ref.setValue(newDownvotes);
        ((TextView)findViewById(R.id.downvotes)).setText(newDownvotes);
        downvotes=newDownvotes;
        System.out.println(downvotes);

    }

    public void onUpVoteClick(View view){
        DatabaseReference upDr=FirebaseDatabase.getInstance().getReference().child("UserUpVotes").child(FirebaseAuth.getInstance().getUid());
        DatabaseReference downDr=FirebaseDatabase.getInstance().getReference().child("UserDownVotes").child(FirebaseAuth.getInstance().getUid());
        if(voteState!=1)
        {
            if(voteState==0)
            {
                upDr.child(videoId).setValue(true);
                upvoteButton.setColorFilter(Color.rgb(0, 0, 255));
                upvoteUpdate(true);
                voteState =1;
                //TODO user has did nothing
            }
            else if(voteState==2)
            {
                downDr.child(videoId).setValue(true);
                upDr.child(videoId).setValue(false);
                //TODO user has clicked first time
                upvoteButton.setColorFilter(Color.rgb(0, 0, 255));
                downvoteButton.clearColorFilter();
                downvoteUpdate(false);
                upvoteUpdate(true);
                if(downvotes.equals(upvotes))
                    voteState =0;
                else
                    voteState=1;

            }
        }



    }

    public void onDownVoteClick(View view){
        DatabaseReference upDr=FirebaseDatabase.getInstance().getReference().child("UserUpVotes").child(FirebaseAuth.getInstance().getUid());
        DatabaseReference downDr=FirebaseDatabase.getInstance().getReference().child("UserDownVotes").child(FirebaseAuth.getInstance().getUid());
        if(voteState!=2)
        {
            if(voteState==0)
            {
                downDr.child(videoId).setValue(true);
                downvoteButton.setColorFilter(Color.rgb(0, 0, 255));
                downvoteUpdate(true);
                voteState =2;
                //TODO user has clicked first time
            }
            else if(voteState==1)
            {
                downDr.child(videoId).setValue(true);
                upDr.child(videoId).setValue(false);
                //TODO change uopvote to downvote
                downvoteButton.setColorFilter(Color.rgb( 0, 0, 255));
                upvoteButton.clearColorFilter();
                upvoteUpdate(false);
                downvoteUpdate(true);
                voteState =2;

            }
        }
    }

    public void onPlayClick(View view)
    {
        Intent i=new Intent(this,PlayVideo.class);
        i.putExtra("videoId",videoId);
        startActivity(i);
    }

    @Override
    public void onListFragmentInteraction(Bundle details, String action, boolean isFabClicked) {

    }



}
