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
import corporation.app.menchus.com.socialmediagamer.models.Post;
import corporation.app.menchus.com.socialmediagamer.providers.UserProvider;

public class PostsAdapter  extends FirestoreRecyclerAdapter<Post,PostsAdapter.ViewHolder> {

    Context context;
    UserProvider mUserProvider;

    public  PostsAdapter(FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.context = context;
        mUserProvider = new UserProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post model) {
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

        getUserInfo(model.getIdUser(),holder);
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
        View viewHolder;

        public ViewHolder(View view){
            super(view);
            textViewTitle = view.findViewById(R.id.textVireTitlePostCard);
            textViewDescription = view.findViewById(R.id.textViewDescriptionPostCard);
            imageViewPost = view.findViewById(R.id.imageViewPostCard);
            textViewUserNamePostCard = view.findViewById(R.id.textViewUserNamePostCard);
            viewHolder = view;
        }

    }

}
