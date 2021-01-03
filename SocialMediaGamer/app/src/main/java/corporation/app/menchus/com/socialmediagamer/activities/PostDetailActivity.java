package corporation.app.menchus.com.socialmediagamer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import corporation.app.menchus.com.socialmediagamer.R;
import corporation.app.menchus.com.socialmediagamer.adapters.CommentAdapter;
import corporation.app.menchus.com.socialmediagamer.adapters.SliderAdapter;
import corporation.app.menchus.com.socialmediagamer.models.Comment;
import corporation.app.menchus.com.socialmediagamer.models.SliderItem;
import corporation.app.menchus.com.socialmediagamer.providers.AuthProvider;
import corporation.app.menchus.com.socialmediagamer.providers.CommentsProvider;
import corporation.app.menchus.com.socialmediagamer.providers.LikeProvider;
import corporation.app.menchus.com.socialmediagamer.providers.PostProvider;
import corporation.app.menchus.com.socialmediagamer.providers.UserProvider;
import corporation.app.menchus.com.socialmediagamer.utils.RelativeTime;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    SliderView sliderView;
    SliderAdapter mSliderAdapter;
    List<SliderItem> mSliderItems = new ArrayList<>();
    PostProvider mPostProvider;
    String mExtraPostId;
    UserProvider mUserProvider;
    CircleImageView mcircleImageViewBack;
    CommentsProvider mCommentsProvider;
    AuthProvider mAuthProvider;
    LikeProvider mLikeProvider;
    CommentAdapter mCommentAdapter;

    String mIdUser="";

    TextView mTextViewTitle;
    TextView mTextViewDescription;
    TextView mTextViewUserName;
    TextView mTextViewPhone;
    TextView mTextViewCategory;
    TextView mTextViewRelativeTime;
    TextView mTextViewLikes;
    ImageView mImageViewCategory;
    CircleImageView mCircleImageViewProfile;
    Button mButtonShowProfile;
    FloatingActionButton mFloatingActionButton;
    RecyclerView mrecyclerViewComments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_post_detail);

        mPostProvider = new PostProvider();
        mExtraPostId = getIntent().getStringExtra("id");
        mUserProvider = new UserProvider();
        mCommentsProvider = new CommentsProvider();
        mAuthProvider = new AuthProvider();
        mLikeProvider = new LikeProvider();

        sliderView = findViewById(R.id.imageSlider);
        mTextViewTitle = findViewById(R.id.textViewTitleGame);
        mTextViewCategory = findViewById(R.id.textViewCategory);
        mTextViewDescription = findViewById(R.id.textViewDescription);
        mTextViewUserName = findViewById(R.id.textViewUserName);
        mTextViewPhone = findViewById(R.id.textViewPhone);
        mImageViewCategory = findViewById(R.id.imageViewCategory);
        mCircleImageViewProfile = findViewById(R.id.circleImageViewProfile);
        mButtonShowProfile = findViewById(R.id.btnShowProfile);
        mcircleImageViewBack = findViewById(R.id.circleImageBack);
        mFloatingActionButton = findViewById(R.id.fabComment);
        mrecyclerViewComments = findViewById(R.id.recyclerViewComments);
        mTextViewRelativeTime = findViewById(R.id.textViewRelativeTime);
        mTextViewLikes = findViewById(R.id.textViewLikes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PostDetailActivity.this);
        mrecyclerViewComments.setLayoutManager(linearLayoutManager);

        mcircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mButtonShowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToShowProfile();
            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogComment();
            }
        });

        getPost();
        getNumberLikes();

    }

    private void getNumberLikes() {

        mLikeProvider.getLikesByPost(mExtraPostId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int numberLikes = value.size();

                if(numberLikes==1){
                    mTextViewLikes.setText(String.valueOf(numberLikes)+" Me gusta");
                }else{
                    mTextViewLikes.setText(String.valueOf(numberLikes)+" Me gustas");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = mCommentsProvider.getCommentsByPost(mExtraPostId);
        FirestoreRecyclerOptions<Comment> options = new FirestoreRecyclerOptions.Builder<Comment>().setQuery(query,Comment.class).build();
        mCommentAdapter = new CommentAdapter(options,PostDetailActivity.this);
        mrecyclerViewComments.setAdapter(mCommentAdapter);
        mCommentAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCommentAdapter.stopListening();
    }

    private void showDialogComment() {
        AlertDialog.Builder alert = new AlertDialog.Builder(PostDetailActivity.this);
        alert.setTitle("!Comentario");
        alert.setMessage("Ingresa tu comentario");

        final EditText mEditText = new EditText(PostDetailActivity.this);
        mEditText.setHint("texto");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(36,0,36,0);
        mEditText.setLayoutParams(params);
        RelativeLayout container = new RelativeLayout(PostDetailActivity.this);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        container.setLayoutParams(relativeParams);
        container.addView(mEditText);


        alert.setView(container);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = mEditText.getText().toString();
                if(!value.isEmpty()){
                    createComment(value);
                }else{
                    Toast.makeText(PostDetailActivity.this, "Ingrese un comentario", Toast.LENGTH_LONG).show();
                }


            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    private void createComment(String commentario) {

        Comment comment = new Comment();
        comment.setComment(commentario);
        comment.setIdPost(mExtraPostId);
        comment.setIdUser(mAuthProvider.getUId());
        comment.setTimestamp(new Date().getTime());
        mCommentsProvider.create(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PostDetailActivity.this, "Comentario creado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(PostDetailActivity.this, "No se creo el comentario", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void goToShowProfile() {

        if(!mIdUser.equals("")){
            Intent intent = new Intent(PostDetailActivity.this,UserProfileActivity.class);
            intent.putExtra("idUser",mIdUser);
            startActivity(intent);
        }
    }

    private void getPost(){

        mPostProvider.getPostById(mExtraPostId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("image1")){
                        String image1 = documentSnapshot.getString("image1");
                        SliderItem item = new SliderItem();
                        item.setImageUrL(image1);
                        mSliderItems.add(item);
                    }
                    if(documentSnapshot.contains("image2")){
                        String image2 = documentSnapshot.getString("image2");
                        SliderItem item = new SliderItem();
                        item.setImageUrL(image2);
                        mSliderItems.add(item);
                    }
                    if(documentSnapshot.contains("title")){
                        String title = documentSnapshot.getString("title");
                        mTextViewTitle.setText(title.toUpperCase());
                    }
                    if(documentSnapshot.contains("description")){
                        String description = documentSnapshot.getString("description");
                        mTextViewDescription.setText(description);
                    }
                    if(documentSnapshot.contains("category")){
                        String category = documentSnapshot.getString("category");
                        mTextViewCategory.setText(category);
                        typeCategory(category);
                    }
                    if(documentSnapshot.contains("idUser")){
                        mIdUser = documentSnapshot.getString("idUser");
                        getUserInfo(mIdUser);
                    }
                    if(documentSnapshot.contains("timestamp")){
                        long timestamp = documentSnapshot.getLong("timestamp");
                        String relativeTime = RelativeTime.getTimeAgo(timestamp,PostDetailActivity.this);
                        mTextViewRelativeTime.setText(relativeTime);
                    }

                    instanceSlider();
                }
            }
        });
    }

    private void typeCategory(String category){

        if(category.equals("PS4")){
            mImageViewCategory.setImageResource(R.drawable.playstation);
        }else if(category.equals("XBOX")) {
            mImageViewCategory.setImageResource(R.drawable.xbox);
        }else if(category.equals("Nintendo")){
            mImageViewCategory.setImageResource(R.drawable.nintendo);
        }else if(category.equals("PC")){
            mImageViewCategory.setImageResource(R.drawable.pc);
        }
    }

    private void getUserInfo(String idUser) {
        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    if(documentSnapshot.contains("username")){
                        String username = documentSnapshot.getString("username");
                        mTextViewUserName.setText(username);
                    }
                    if(documentSnapshot.contains("phonenumber")){
                        String phonenumber = documentSnapshot.getString("phonenumber");
                        mTextViewCategory.setText(phonenumber);
                    }
                    if(documentSnapshot.contains("image_profile")){
                        String image_profile = documentSnapshot.getString("image_profile");
                        Picasso.with(PostDetailActivity.this).load(image_profile).into(mCircleImageViewProfile);
                    }
                }
            }
        });
    }

    private void instanceSlider(){
        mSliderAdapter = new SliderAdapter(PostDetailActivity.this,mSliderItems);
        sliderView.setSliderAdapter(mSliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }
}
