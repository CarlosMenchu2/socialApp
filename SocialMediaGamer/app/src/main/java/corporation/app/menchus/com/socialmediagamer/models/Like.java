package corporation.app.menchus.com.socialmediagamer.models;

public class Like {

    private String id;
    private String idPost;
    private String idUser;
    private long timestamp;

    public Like(){}

    public Like(String id, String idPost, String idUser, long timestamp) {
        this.id = id;
        this.idPost = idPost;
        this.idUser = idUser;
        this.timestamp = timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getIdPost() {
        return idPost;
    }

    public String getIdUser() {
        return idUser;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
