package corporation.app.menchus.com.socialmediagamer.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import corporation.app.menchus.com.socialmediagamer.R;
import corporation.app.menchus.com.socialmediagamer.activities.PostDetailActivity;
import corporation.app.menchus.com.socialmediagamer.models.Comment;
import corporation.app.menchus.com.socialmediagamer.models.Post;
import corporation.app.menchus.com.socialmediagamer.providers.UserProvider;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends FirestoreRecyclerAdapter<Comment, CommentAdapter.ViewHolder> {

    Context context;
    UserProvider mUserProvider;

    public CommentAdapter(FirestoreRecyclerOptions<Comment> options, Context context){
        super(options);
        this.context = context;
        this.mUserProvider = new UserProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Comment model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        final String commetId = documentSnapshot.getId();
        String idUser = documentSnapshot.getString("idUser");
        holder.textViewComment.setText(model.getComment());
        getUserInfo(idUser,holder);


    }

    private void getUserInfo(String userId, final ViewHolder holder){
        mUserProvider.getUser(userId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    if(documentSnapshot.contains("username")){
                        String username = documentSnapshot.getString("username");
                        holder.textViewUsernameComment.setText(username);

                    }

                    if(documentSnapshot.contains("image_profile")){
                        String imageProfile = documentSnapshot.getString("image_profile");
                        if(!imageProfile.isEmpty() && !imageProfile.equals(null)){
                            Picasso.with(context).load(imageProfile).into(holder.circleImageViewComent);
                        }
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_comment,parent,false);
        return new ViewHolder(view);
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewUsernameComment;
        TextView textViewComment;
        CircleImageView circleImageViewComent;
        View viewHolder;

        public ViewHolder(View view){
            super(view);
            textViewUsernameComment = view.findViewById(R.id.textVireTitlePostCard);
            textViewComment = view.findViewById(R.id.textViewDescriptionPostCard);
            circleImageViewComent = view.findViewById(R.id.circleImageVewComment);
            viewHolder = view;
        }

    }

}
