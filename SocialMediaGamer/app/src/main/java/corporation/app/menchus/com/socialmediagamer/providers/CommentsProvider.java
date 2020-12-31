package corporation.app.menchus.com.socialmediagamer.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import corporation.app.menchus.com.socialmediagamer.models.Comment;

public class CommentsProvider {

    CollectionReference mCollection;

    public CommentsProvider() {
        this.mCollection = FirebaseFirestore.getInstance().collection("commets");
    }

    public Task<Void> create(Comment comment){

        return mCollection.document().set(comment);
    }
}
