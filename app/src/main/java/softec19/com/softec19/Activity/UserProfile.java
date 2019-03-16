package softec19.com.softec19.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        startActivity(new Intent(this,MainActivity.class));
        keyEdit=findViewById(R.id.Key);
        name="Hamza";
        key="pre";
        PremiumKey="pre";
        genres=new ArrayList<>();
        genres.add("Horror");
        genres.add("History");
        genres.add("bollywood");
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
                       }
                       else
                       {
                           //TODO dialog
                       }
                       dialog.dismiss();
                   }
               });

    }

    public void onAddClick(View view){
        simpleSearchDialogCompat.show();
    }

    public void onUpdateClick(View view)
    {
        DatabaseReference userRf= FirebaseDatabase.getInstance().getReference().child("User");
        if(premiumCheck) {
            if (key.equals(PremiumKey)) {

                UserProfileModel userProfile = new UserProfileModel(name, "Premium", genres);
                userRf.child(FirebaseAuth.getInstance().getUid()).setValue(userProfile);
                updateGenres();
            } else {
                //TODO Dialog your key is not correct
            }
        }
        else
        {
            UserProfileModel userProfile = new UserProfileModel(name, "Basic", genres);
            userRf.child(FirebaseAuth.getInstance().getUid()).setValue(userProfile);
            updateGenres();
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
        items.add(new SampleSearchModel("First item"));
        items.add(new SampleSearchModel("Second item"));
        items.add(new SampleSearchModel("Third item"));
        items.add(new SampleSearchModel("The ultimate item"));
        items.add(new SampleSearchModel("Last item"));
        items.add(new SampleSearchModel("Lorem ipsum"));
        items.add(new SampleSearchModel("Dolor sit"));
        items.add(new SampleSearchModel("Some random word"));
        items.add(new SampleSearchModel("guess who's back"));
        return items;
    }



}
