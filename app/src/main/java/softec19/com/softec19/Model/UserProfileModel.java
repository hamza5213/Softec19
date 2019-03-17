package softec19.com.softec19.Model;

import java.util.ArrayList;

/**
 * Created by hamza on 16-Mar-19.
 */
import com.google.firebase.database.Exclude;
public class UserProfileModel {
    String name;
    String status;
    ArrayList<String> genres;
    @Exclude
    String userId;


    public UserProfileModel(String name, String status, ArrayList<String> genres, String userId) {
        this.name = name;
        this.status = status;
        this.genres = genres;
        this.userId = userId;
    }

    public UserProfileModel(String name, String status, ArrayList<String> genres) {
        this.name = name;
        this.status = status;
        this.genres = genres;
    }

    public UserProfileModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    @Exclude
    public String getUserId() {
        return userId;
    }

    @Exclude
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UserProfileModel)
        {
            return this.userId.equals(((UserProfileModel)obj).getUserId());
        }
        return false;
    }
}
