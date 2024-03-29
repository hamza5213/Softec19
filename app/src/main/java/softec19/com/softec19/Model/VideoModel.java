package softec19.com.softec19.Model;

import com.google.firebase.database.Exclude;

public class VideoModel {

    @Exclude
    private String videoId;
    private String videoName;
    private String commentBucketId;
    private String upvoteCount;
    private String downnvoteCount;
    private String uploaderId;
    private String videoCode;
    private String genre;

    public VideoModel() {
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public VideoModel(String videoId, String videoName, String commentBucketId, String upvoteCount, String downnvoteCount, String uploaderId, String videoCode, String genre) {

        this.videoId = videoId;
        this.videoName = videoName;
        this.commentBucketId = commentBucketId;
        this.upvoteCount = upvoteCount;
        this.downnvoteCount = downnvoteCount;
        this.uploaderId = uploaderId;
        this.videoCode = videoCode;
        this.genre = genre;
    }

    @Exclude
    public String getVideoId() {
        return videoId;
    }

    @Exclude
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }


    public String getVideoCode() {
        return videoCode;
    }

    public void setVideoCode(String videoCode) {
        this.videoCode = videoCode;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getCommentBucketId() {
        return commentBucketId;
    }

    public void setCommentBucketId(String commentBucketId) {
        this.commentBucketId = commentBucketId;
    }

    public String getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(String upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public String getDownnvoteCount() {
        return downnvoteCount;
    }

    public void setDownnvoteCount(String downnvoteCount) {
        this.downnvoteCount = downnvoteCount;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }
}
