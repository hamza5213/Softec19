package softec19.com.softec19.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import softec19.com.softec19.Interfaces.OnListFragmentInteractionListener;
import softec19.com.softec19.Model.UserProfileModel;
import softec19.com.softec19.R;

/**
 * Created by hamza on 17-Mar-19.
 */

public class genreAdapter extends RecyclerView.Adapter<genreAdapter.ViewHolder> {

    private final ArrayList<String> mValues;
    Context c;
    boolean premium;
    OnListFragmentInteractionListener mListener;
    FirebaseStorage storage;
    private TextDrawable.IBuilder builder;

    public genreAdapter(ArrayList<String> items, Context context) {
        mValues = items;
        c = context;
    }

    @Override
    public genreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genre_item, parent, false);
        return new genreAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final genreAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position));
        final int pos=position;
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mValues.remove(pos);
                genreAdapter.this.notifyDataSetChanged();
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
        public final ImageButton delete;
        public String mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName=view.findViewById(R.id.genre_name);
            delete=view.findViewById(R.id.genre_delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }


    }
}
