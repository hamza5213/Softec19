package softec19.com.softec19.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;

import softec19.com.softec19.Model.UserProfileModel;
import softec19.com.softec19.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            checkProfile();
        }
        else
        {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.PhoneBuilder().build()
                            ))
                            .build(),
                    123);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==123&&resultCode==RESULT_OK)
        {
            DatabaseReference dR = FirebaseDatabase.getInstance().getReference("FCM_InstanceID").child(FirebaseAuth.getInstance().getUid());
            dR.setValue(FirebaseInstanceId.getInstance().getToken());
            checkProfile();
        }

    }

    void checkProfile() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("User").child(uid);
        dR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileModel userProfile = dataSnapshot.getValue(UserProfileModel.class);
                if (userProfile == null) {
                    startActivity(new Intent(Login.this, UserProfile.class));

                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("UserId", dataSnapshot.getKey());
                    editor.putString("Name", userProfile.getName());
                    editor.putString("status", userProfile.getStatus());
                    editor.commit();
                    if(userProfile.getStatus().equals("admin"))
                    {
                        Intent intent=new Intent(Login.this, AdminActivity.class);
                        intent.putExtra("status",userProfile.getStatus());
                        startActivity(intent);
                    }
                    else{
                    Intent intent=new Intent(Login.this, MainActivity.class);
                    intent.putExtra("status",userProfile.getStatus());
                    startActivity(intent);}
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

}
