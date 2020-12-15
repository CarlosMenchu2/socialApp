package corporation.app.menchus.com.socialmediagamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import corporation.app.menchus.com.socialmediagamer.R;
import corporation.app.menchus.com.socialmediagamer.models.Post;

public class PostsAdapter  extends FirestoreRecyclerAdapter<Post,PostsAdapter.ViewHolder> {

    Context context;

    public  PostsAdapter(FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post model) {
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewDescription.setText(model.getDescription());
        if(model.getImage1()!= null && !model.getImage1().isEmpty()){
            Picasso.with(context).load(model.getImage1()).into(holder.imageViewPost);
        }

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

        public ViewHolder(View view){
            super(view);
            textViewTitle = view.findViewById(R.id.textVireTitlePostCard);
            textViewDescription = view.findViewById(R.id.textViewDescriptionPostCard);
            imageViewPost = view.findViewById(R.id.imageViewPostCard);

        }

    }

}
