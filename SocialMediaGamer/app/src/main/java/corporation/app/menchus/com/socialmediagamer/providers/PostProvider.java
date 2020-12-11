package corporation.app.menchus.com.socialmediagamer.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import corporation.app.menchus.com.socialmediagamer.models.Post;


public class PostProvider {

    CollectionReference mcollection;

    public PostProvider(){
        mcollection = FirebaseFirestore.getInstance().collection("posts");
    }

    public Task<Void> save(Post post){

        return mcollection.document().set(post);
    }

}
