package softec19.com.softec19.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by hamza on 17-Mar-19.
 */

public class InstanceIdGenerator extends FirebaseMessagingService {

        @Override
        public void onNewToken(String s) {
            UploadToServer(s);
        }

        void UploadToServer(String key)
        {
            FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            if(firebaseUser!=null) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference dR = firebaseDatabase.getReference("FCM_InstanceId").child(firebaseUser.getUid());
                dR.setValue(key);
            }
        }
}

