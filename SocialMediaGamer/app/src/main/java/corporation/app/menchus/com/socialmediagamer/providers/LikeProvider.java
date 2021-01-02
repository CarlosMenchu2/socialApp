package corporation.app.menchus.com.socialmediagamer.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import corporation.app.menchus.com.socialmediagamer.models.Like;

public class LikeProvider {

    CollectionReference mCollection;

    public LikeProvider() {

        mCollection = FirebaseFirestore.getInstance().collection("likes");
    }

    public Task<Void> create(Like like){
        DocumentReference documentReference = mCollection.document();
        String id = documentReference.getId();
        like.setId(id);
        return documentReference.set(like);
    }

    public Query getLikeByPostAndUser(String idPost, String idUser) {
        return  mCollection.whereEqualTo("idPost",idPost).whereEqualTo("idUser",idUser);
    }

    public Task<Void> delete(String id){

        return mCollection.document(id).delete();
    }

    public Query getLikesByPost(String idPost){

        return mCollection.whereEqualTo("idPost",idPost);
    }
}
