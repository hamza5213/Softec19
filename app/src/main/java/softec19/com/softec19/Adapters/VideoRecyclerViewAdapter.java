package softec19.com.softec19.Adapters;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



import java.util.ArrayList;

import softec19.com.softec19.Interfaces.OnListFragmentInteractionListener;
import softec19.com.softec19.Model.VideoModel;
import softec19.com.softec19.R;


public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<VideoModel> mValues;
    Context c;
    OnListFragmentInteractionListener mListener;
    FirebaseStorage storage;

    public VideoRecyclerViewAdapter(ArrayList<VideoModel> items, Context context, OnListFragmentInteractionListener mListener) {
        mValues = items;
        c = context;
        this.mListener = mListener;
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mName.setText(mValues.get(position).getVideoName());
        String upvotes = mValues.get(position).getUpvoteCount();
        String downvotes = mValues.get(position).getDownnvoteCount();
        holder.votes.setText(upvotes+" Upvotes | "+downvotes+" Downvotes");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("videoId", holder.mItem.getVideoId());
                mListener.onListFragmentInteraction(bundle, "videoDetails", true);
            }
        });

        //ProfilePicture.setProfilePicture(mValues.get(position).getUserId(), holder.mPicture);
        StorageReference ref = storage.getReference().child("VideoThumbnail/" + mValues.get(position).getVideoId() + ".jpg");
        Glide.with(c.getApplicationContext()).load(ref).into(holder.mPicture);


    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder

    {
        public final View mView;
        public final ImageView mPicture;
        public final TextView mName;
        public final TextView votes;
        public VideoModel mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPicture = view.findViewById(R.id.video_image);
            mName = view.findViewById(R.id.video_name);
            votes = view.findViewById(R.id.votes);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }


    }
}
