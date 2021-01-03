package corporation.app.menchus.com.socialmediagamer.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import corporation.app.menchus.com.socialmediagamer.R;
import corporation.app.menchus.com.socialmediagamer.activities.PostDetailActivity;
import corporation.app.menchus.com.socialmediagamer.models.Like;
import corporation.app.menchus.com.socialmediagamer.models.Post;
import corporation.app.menchus.com.socialmediagamer.providers.AuthProvider;
import corporation.app.menchus.com.socialmediagamer.providers.LikeProvider;
import corporation.app.menchus.com.socialmediagamer.providers.PostProvider;
import corporation.app.menchus.com.socialmediagamer.providers.UserProvider;
import corporation.app.menchus.com.socialmediagamer.utils.RelativeTime;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostAdapter extends FirestoreRecyclerAdapter<Post, MyPostAdapter.ViewHolder> {

    Context context;
    UserProvider mUserProvider;
    LikeProvider mLikeProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;

    public MyPostAdapter(FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.context = context;
        mUserProvider = new UserProvider();
        mLikeProvider = new LikeProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Post model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        final String postId = documentSnapshot.getId();
        String relativeTime = RelativeTime.getTimeAgo(model.getTimestamp(),context);
        holder.textViewTimeMyPost.setText(String.valueOf(relativeTime));
        holder.textViewTitleMyPost.setText(model.getTitle().toUpperCase());
        if(model.getImage1()!= null && !model.getImage1().isEmpty()){
            Picasso.with(context).load(model.getImage1()).into(holder.circleImageViewMyPost);
        }

        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("id",postId);
                context.startActivity(intent);
            }
        });

        holder.imageViewCancelMyPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost(postId);
            }
        });


    }

    private void deletePost(String postId) {

        mPostProvider.delete(postId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Post eliminado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context, "No se elimino el post", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_my_post,parent,false);
        return new ViewHolder(view);
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitleMyPost;
        TextView textViewTimeMyPost;
        ImageView imageViewCancelMyPost;
        CircleImageView circleImageViewMyPost;
        View viewHolder;



        public ViewHolder(View view){
            super(view);
            textViewTitleMyPost = view.findViewById(R.id.textViewTitleMyPost);
            textViewTimeMyPost = view.findViewById(R.id.textViewTimeMyPost);
            imageViewCancelMyPost = view.findViewById(R.id.imageViewCancelMyPost);
            circleImageViewMyPost = view.findViewById(R.id.circleImageMyPost);
            viewHolder = view;
        }

    }

}
