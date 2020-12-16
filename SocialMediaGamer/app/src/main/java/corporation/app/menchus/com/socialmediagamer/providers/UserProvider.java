package corporation.app.menchus.com.socialmediagamer.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import corporation.app.menchus.com.socialmediagamer.models.User;

public class UserProvider {


    private CollectionReference mCollection;

    public  UserProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("users");
    }

    public Task<DocumentSnapshot> getUser(String id){
        return mCollection.document(id).get();
    }

    public Task<Void> create(User user){
       return mCollection.document(user.getId()).set(user);
    }

    public  Task<Void> update(User user){
        Map<String,Object> map = new HashMap<>();
        map.put("username",user.getUserName());
        map.put("phoneNumber",user.getPhoneNumber());
        map.put("timestamp",user.getTimestamp());
        map.put("image_profile",user.getImage_profile());
        map.put("image_cover",user.getImage_cover());
        return mCollection.document(user.getId()).update(map);
    }






}
