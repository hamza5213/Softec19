package softec19.com.softec19.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import softec19.com.softec19.Adapters.VideoRecyclerViewAdapter;
import softec19.com.softec19.Adapters.genreAdapter;
import softec19.com.softec19.Model.SampleSearchModel;
import softec19.com.softec19.Model.UserProfileModel;
import softec19.com.softec19.R;

public class UserProfile extends AppCompatActivity {
    String name;
    String key;
    ArrayList<String> genres;
    String PremiumKey;
    boolean premiumCheck;
    EditText keyEdit;
    ToggleSwitch toggleSwitch;
    ArrayList<SampleSearchModel> genresList;
    SimpleSearchDialogCompat simpleSearchDialogCompat;
    genreAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        keyEdit=findViewById(R.id.Key);
        name="Hamza";
        key="pre";
        PremiumKey="pre";
        genres=new ArrayList<>();
        toggleSwitch=findViewById(R.id.user_profile_toggle);
        toggleSwitch.setCheckedTogglePosition(0);
        toggleSwitch.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position==1)
                {
                    premiumCheck=true;
                    keyEdit.setVisibility(View.VISIBLE);
                }
                else
                {
                    premiumCheck=false;
                    keyEdit.setVisibility(View.GONE);
                }
            }
        });

       genresList=createSampleData();

       simpleSearchDialogCompat=new SimpleSearchDialogCompat(UserProfile.this, "Search...",
               "What are you looking for...?", null, createSampleData(),
               new SearchResultListener<SampleSearchModel>() {
                   @Override
                   public void onSelected(BaseSearchDialogCompat dialog,
                                          SampleSearchModel item, int position) {
                       Toast.makeText(UserProfile.this, item.getTitle(),
                               Toast.LENGTH_SHORT).show();
                       if(!genres.contains(item.getTitle()))
                       {
                           genres.add(item.getTitle());
                           adapter.notifyDataSetChanged();
                       }
                       else
                       {
                           //TODO dialog
                       }
                       dialog.dismiss();
                   }
               });

        RecyclerView recyclerView = findViewById(R.id.genresRC);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new genreAdapter(genres, this);
        recyclerView.setAdapter(adapter);

    }

    public void onAddClick(View view){
        simpleSearchDialogCompat.show();
    }

    public void onUpdateClick(View view)
    {
        DatabaseReference userRf= FirebaseDatabase.getInstance().getReference().child("User");
        if(premiumCheck) {
            if (key.equals(PremiumKey)) {

                UserProfileModel userProfile = new UserProfileModel(name, "premium", genres);
                userRf.child(FirebaseAuth.getInstance().getUid()).setValue(userProfile);
                FirebaseDatabase.getInstance().getReference().child("PremiumUser").child(FirebaseAuth.getInstance().getUid()).setValue(true);
                updateGenres();
                Intent i=new Intent(this,MainActivity.class);
                i.putExtra("","premium");
                startActivity(i);
                finish();
            } else {
                //TODO Dialog your key is not correct
            }
        }
        else
        {
            UserProfileModel userProfile = new UserProfileModel(name, "basic", genres);
            userRf.child(FirebaseAuth.getInstance().getUid()).setValue(userProfile);
            FirebaseDatabase.getInstance().getReference().child("BasicUser").child(FirebaseAuth.getInstance().getUid()).setValue(true);
            updateGenres();
            Intent i=new Intent(this,MainActivity.class);
            i.putExtra("","basic");
            startActivity(i);
            finish();
        }
    }

    void updateGenres(){
        DatabaseReference genreRf= FirebaseDatabase.getInstance().getReference().child("Genres");
        String userId=FirebaseAuth.getInstance().getUid();
        for (int i=0; i<genres.size();i++)
        {
            genreRf.child(genres.get(i)).child(userId).setValue(true);
        }
    }

    private ArrayList<SampleSearchModel> createSampleData(){
        ArrayList<SampleSearchModel> items = new ArrayList<>();
        items.add(new SampleSearchModel("Horror"));
        items.add(new SampleSearchModel("Science"));
        return items;
    }



}
