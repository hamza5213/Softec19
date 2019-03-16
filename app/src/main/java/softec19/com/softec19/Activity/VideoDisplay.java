package softec19.com.softec19.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import softec19.com.softec19.Model.CommentsModel;
import softec19.com.softec19.Model.VideoModel;
import softec19.com.softec19.R;

public class VideoDisplay extends AppCompatActivity {
    String videoId;
    EditText commentBox;
    ArrayList<CommentsModel> commentsArray;
    String commentBucketId;
    ArrayList<String> userUpVotes;
    ArrayList<String> userDownVotes;
    int voteState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_display);
        videoId=getIntent().getStringExtra("videoId");
        commentsArray=new ArrayList<>();
        commentBox=findViewById(R.id.UserComment);
        userUpVotes=new ArrayList<>();
        userDownVotes=new ArrayList<>();
        fetchComments(videoId);
        fetchUserVotes();

    }

    void fetchComments(String id)
    {
       FirebaseDatabase.getInstance().getReference().child("Videos").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                VideoModel videoModel=dataSnapshot.getValue(VideoModel.class);
                if(videoModel.getCommentBucketId().equals(""))
                {
                    String s=FirebaseDatabase.getInstance().getReference().child("Video").child(videoId).push().getKey();
                    FirebaseDatabase.getInstance().getReference().child("Video").child(videoId).setValue(s);
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
                                commentsArray.add(dataSnapshot.getValue(CommentsModel.class));
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
            //TODO change selection of upvote and down vote
        }
        else if(userDownVotes.contains(videoId)){
            voteState=2;
            //TODO change selection of upvote and down vote
        }
    }

    public void onUpVoteClick(View view){
        DatabaseReference upDr=FirebaseDatabase.getInstance().getReference().child("UserUpVotes").child(FirebaseAuth.getInstance().getUid());
        DatabaseReference downDr=FirebaseDatabase.getInstance().getReference().child("UserDownVotes").child(FirebaseAuth.getInstance().getUid());
        if(voteState!=1)
        {
            if(voteState==0)
            {
                upDr.child(videoId).setValue(true);
                //TODO change selection of upvote and down vote
            }
            else if(voteState==2)
            {
                downDr.child(videoId).setValue(true);
                upDr.child(videoId).setValue(false);
                //TODO change selection of upvote and down vote

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
                //TODO change selection of upvote and down vote
            }
            else if(voteState==2)
            {
                upDr.child(videoId).setValue(true);
                downDr.child(videoId).setValue(false);
                //TODO change selection of upvote and down vote

            }
        }
    }


}
