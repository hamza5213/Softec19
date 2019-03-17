package softec19.com.softec19.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import softec19.com.softec19.Interfaces.OnListFragmentInteractionListener;
import softec19.com.softec19.Model.UserProfileModel;
import softec19.com.softec19.Model.VideoModel;
import softec19.com.softec19.R;

/**
 * Created by hamza on 17-Mar-19.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final ArrayList<UserProfileModel> mValues;
    Context c;
    boolean premium;
    OnListFragmentInteractionListener mListener;
    FirebaseStorage storage;
    private TextDrawable.IBuilder builder;

    public UserAdapter(ArrayList<UserProfileModel> items, Context context, OnListFragmentInteractionListener mListener,boolean p) {
        mValues = items;
        c = context;
        this.premium=p;
        this.mListener = mListener;
        storage = FirebaseStorage.getInstance();
        builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(1)
                .endConfig()
                .rect();
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final UserAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).getName());

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(mValues.get(position).getName());
        TextDrawable ic1 = builder.build(mValues.get(position).getName().toUpperCase().substring(0, 1), color);
        holder.user_icon.setImageDrawable(ic1);
        final int pos=position;
        if(premium)
        {
           holder.user_status.setImageResource(R.drawable.user_basic);
           holder.user_status.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   FirebaseDatabase.getInstance().getReference().child("User").child(mValues.get(pos).getUserId()).child("status").setValue("basic");
                   FirebaseDatabase.getInstance().getReference().child("PremiumUser").child(mValues.get(pos).getUserId()).setValue(false);
                   FirebaseDatabase.getInstance().getReference().child("BasicUser").child(mValues.get(pos).getUserId()).setValue(true);
                   mValues.remove(pos);
                   UserAdapter.this.notifyDataSetChanged();
               }
           });
        }
        else
        {
            holder.user_status.setImageResource(R.drawable.paid_user);
            holder.user_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("User").child(mValues.get(pos).getUserId()).child("status").setValue("premium");
                    FirebaseDatabase.getInstance().getReference().child("BasicUser").child(mValues.get(pos).getUserId()).setValue(false);
                    FirebaseDatabase.getInstance().getReference().child("PremiumUser").child(mValues.get(pos).getUserId()).setValue(true);
                    mValues.remove(pos);
                    UserAdapter.this.notifyDataSetChanged();
                }
            });
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("User").child(mValues.get(pos).getUserId()).setValue(null);
                mValues.remove(pos);
                UserAdapter.this.notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder

    {
        public final View mView;
        public final TextView mName;
        public final ImageView user_icon ;
        public final ImageButton delete;
        public final ImageButton user_status;

        public UserProfileModel mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName=view.findViewById(R.id.user_name);
            user_icon=view.findViewById(R.id.user_icon);
            delete=view.findViewById(R.id.userDelete);
            user_status=view.findViewById(R.id.user_status);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }


    }
}
