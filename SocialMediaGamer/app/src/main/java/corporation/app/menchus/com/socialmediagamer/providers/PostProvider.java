package corporation.app.menchus.com.socialmediagamer.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import corporation.app.menchus.com.socialmediagamer.models.Post;


public class PostProvider {

    CollectionReference mcollection;

    public PostProvider(){
        mcollection = FirebaseFirestore.getInstance().collection("posts");
    }

    public Task<Void> save(Post post){

        return mcollection.document().set(post);
    }

    public Query getAll() {
        return mcollection.orderBy("title",Query.Direction.DESCENDING);
    }

    public Query getPostByUser(String idUser){
        return mcollection.whereEqualTo("idUser",idUser);
    }

}
