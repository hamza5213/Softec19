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


    public CommentsModel() {
    }

    public CommentsModel(String commentId, String userId, String text) {
        this.commentId = commentId;
        this.userId = userId;
        this.text = text;
    }

    @Exclude
    public String getCommentId() {
        return commentId;
    }

    @Exclude
    public void setCommentId(String commentId) {
        this.commentId = commentId;
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
