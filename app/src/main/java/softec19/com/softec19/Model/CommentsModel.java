package softec19.com.softec19.Model;

/**
 * Created by hamza on 16-Mar-19.
 */
import com.google.firebase.database.Exclude;
public class CommentsModel {
    @Exclude
    String commentId;
    String userId;
    String text;
    String username;



    public CommentsModel() {
    }

    public CommentsModel(String commentId, String userId, String text, String username) {
        this.commentId = commentId;
        this.userId = userId;
        this.text = text;
        this.username = username;
    }

    @Exclude
    public String getCommentId() {
        return commentId;
    }

    @Exclude
    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
