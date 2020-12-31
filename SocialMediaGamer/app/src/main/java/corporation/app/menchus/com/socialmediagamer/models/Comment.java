package corporation.app.menchus.com.socialmediagamer.models;

public class Comment {

    private String id;
    private String comment;
    private String idUser;
    private String idPost;
    private long timestamp;

    public Comment(String id, String comment, String idUser, String idPost, long timestamp) {
        this.id = id;
        this.comment = comment;
        this.idUser = idUser;
        this.idPost = idPost;
        this.timestamp = timestamp;
    }

    public  Comment(){ }

    public void setId(String id) {
        this.id = id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getIdPost() {
        return idPost;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
