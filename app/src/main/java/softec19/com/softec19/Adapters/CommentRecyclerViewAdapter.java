package softec19.com.softec19.Adapters;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.List;

import softec19.com.softec19.Interfaces.OnListFragmentInteractionListener;
import softec19.com.softec19.Model.CommentsModel;
import softec19.com.softec19.R;


public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {

    private final List<CommentsModel> mValues;
    private final OnListFragmentInteractionListener mListener;
    private TextDrawable.IBuilder builder;

    public CommentRecyclerViewAdapter(ArrayList<CommentsModel> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(1)
                .endConfig()
                .rect();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mComment = mValues.get(position);
        holder.mCommentText.setText(mValues.get(position).getText());
        holder.mUsername.setText(mValues.get(position).getUsername());

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(mValues.get(position).getUsername());
        TextDrawable ic1 = builder.build(mValues.get(position).getUsername().toUpperCase().substring(0, 1), color);
        holder.mUserIcon.setImageDrawable(ic1);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mUserIcon;
        public final TextView mUsername;
        public final TextView mCommentText;
        public CommentsModel mComment;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUserIcon = view.findViewById(R.id.user_icon);
            mUsername = view.findViewById(R.id.user_name);
            mCommentText = view.findViewById(R.id.comment_detail);

        }


        @Override
        public String toString() {
            return super.toString() + " '" + mCommentText.getText() + "'";
        }
    }
}

