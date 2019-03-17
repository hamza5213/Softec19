package softec19.com.softec19.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import softec19.com.softec19.Adapters.UserAdapter;
import softec19.com.softec19.Interfaces.OnListFragmentInteractionListener;
import softec19.com.softec19.Model.UserProfileModel;
import softec19.com.softec19.R;

/**
 * Created by hamza on 17-Mar-19.
 */

public class PremiumUser extends Fragment implements OnListFragmentInteractionListener {
    FirebaseDatabase firebaseDatabase;
    ArrayList<UserProfileModel> userProfileArrayList;
    OnListFragmentInteractionListener mListener;
    Context context;
    UserAdapter adapter;

    public PremiumUser() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_premium_user, container, false);
        firebaseDatabase=FirebaseDatabase.getInstance();
        userProfileArrayList =new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.premiumUser_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new UserAdapter(userProfileArrayList,context, this,true);
        recyclerView.setAdapter(adapter);
        fetchUser1();
        return view;
    }

    void fetchUser1()
    {
        FirebaseDatabase.getInstance().getReference("PremiumUser").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getValue()!=null){
                    if(dataSnapshot.getValue(Boolean.class))
                    {
                        fetchUser(dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getValue()!=null){
                    if(dataSnapshot.getValue(Boolean.class))
                    {
                        fetchUser(dataSnapshot.getKey());
                    }
                    else
                    {
                        userProfileArrayList.contains(new UserProfileModel(null,null,null,dataSnapshot.getKey()));
                    }
                }

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

    void fetchUser(String id)
    {
        FirebaseDatabase.getInstance().getReference("User").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    UserProfileModel userProfileModel = dataSnapshot.getValue(UserProfileModel.class);
                    userProfileModel.setUserId(dataSnapshot.getKey());
                    userProfileArrayList.add(userProfileModel);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // mListener=(OnListFragmentInteractionListener)context;
        this.context=context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onListFragmentInteraction(Bundle details, String action, boolean isFabClicked) {

    }
}
