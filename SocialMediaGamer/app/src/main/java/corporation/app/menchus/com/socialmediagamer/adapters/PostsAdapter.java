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
import corporation.app.menchus.com.socialmediagamer.providers.UserProvider;

public class PostsAdapter  extends FirestoreRecyclerAdapter<Post,PostsAdapter.ViewHolder> {

    Context context;
    UserProvider mUserProvider;
    LikeProvider mLikeProvider;
    AuthProvider mAuthProvider;

    public  PostsAdapter(FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.context = context;
        mUserProvider = new UserProvider();
        mLikeProvider = new LikeProvider();
        mAuthProvider = new AuthProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Post model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        final String postId = documentSnapshot.getId();
        holder.textViewTitle.setText(model.getTitle().toUpperCase());
        holder.textViewDescription.setText(model.getDescription());
        if(model.getImage1()!= null && !model.getImage1().isEmpty()){
            Picasso.with(context).load(model.getImage1()).into(holder.imageViewPost);
        }

        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("id",postId);
                context.startActivity(intent);
            }
        });
        
        holder.imageViewLike .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Like like = new Like();
                like.setIdUser(mAuthProvider.getUId());
                like.setIdPost(postId);
                like.setTimestamp(new Date().getTime());
                like(like,holder);
            }
        });

        getUserInfo(model.getIdUser(),holder);
        getNumberLikesByPost(postId,holder);
        checkExistlike(postId,mAuthProvider.getUId(),holder);
    }

    private void like(final Like like, final ViewHolder holder) {
        mLikeProvider.getLikeByPostAndUser(like.getIdPost(),mAuthProvider.getUId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocuments = queryDocumentSnapshots.size();
                if(numberDocuments >0){
                    String idLike = queryDocumentSnapshots.getDocuments().get(0).getId();
                    holder.imageViewLike.setImageResource(R.drawable.nolike);
                    mLikeProvider.delete(idLike);
                }else{
                    holder.imageViewLike.setImageResource(R.drawable.like);
                    mLikeProvider.create(like);
                }
            }
        });
    }

    private void checkExistlike(String idPost, String idUser, final ViewHolder holder) {
        mLikeProvider.getLikeByPostAndUser(idPost,idUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocuments = queryDocumentSnapshots.size();
                if(numberDocuments >0){
                    holder.imageViewLike.setImageResource(R.drawable.like);
                }else{
                    holder.imageViewLike.setImageResource(R.drawable.nolike);
                }
            }
        });
    }

    private void getNumberLikesByPost(String idPost, final ViewHolder holder){
        mLikeProvider.getLikesByPost(idPost).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int numerLikes = value.size();
                holder.textViewNumberLikes.setText(String.valueOf(numerLikes)+" Me gustas");
            }
        });
    }

    private void getUserInfo(String idUser, final ViewHolder holder) {
        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    if(documentSnapshot.contains("username")){
                        String username = documentSnapshot.getString("username");
                        holder.textViewUserNamePostCard.setText("By: "+username.toLowerCase());
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post,parent,false);
        return new ViewHolder(view);
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewDescription;
        ImageView imageViewPost;
        TextView textViewUserNamePostCard;
        TextView textViewNumberLikes;
        ImageView imageViewLike;
        View viewHolder;

        public ViewHolder(View view){
            super(view);
            textViewTitle = view.findViewById(R.id.textVireTitlePostCard);
            textViewDescription = view.findViewById(R.id.textViewDescriptionPostCard);
            imageViewPost = view.findViewById(R.id.imageViewPostCard);
            textViewUserNamePostCard = view.findViewById(R.id.textViewUserNamePostCard);
            textViewNumberLikes = view.findViewById(R.id.textViewLikeNumber);
            imageViewLike = view.findViewById(R.id.imageViewLike);
            viewHolder = view;
        }

    }

}
