package corporation.app.menchus.com.socialmediagamer.activities;

import androidx.appcompat.app.AppCompatActivity;
import corporation.app.menchus.com.socialmediagamer.R;
import corporation.app.menchus.com.socialmediagamer.adapters.SliderAdapter;
import corporation.app.menchus.com.socialmediagamer.models.SliderItem;
import corporation.app.menchus.com.socialmediagamer.providers.PostProvider;
import corporation.app.menchus.com.socialmediagamer.providers.UserProvider;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    SliderView sliderView;
    SliderAdapter mSliderAdapter;
    List<SliderItem> mSliderItems = new ArrayList<>();
    PostProvider mPostProvider;
    String mExtraPostId;
    UserProvider mUserProvider;
    CircleImageView mcircleImageViewBack;

    String mIdUser="";

    TextView mTextViewTitle;
    TextView mTextViewDescription;
    TextView mTextViewUserName;
    TextView mTextViewPhone;
    TextView mTextViewCategory;
    ImageView mImageViewCategory;
    CircleImageView mCircleImageViewProfile;
    Button mButtonShowProfile;
    FloatingActionButton mFloatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_post_detail);

        mPostProvider = new PostProvider();
        mExtraPostId = getIntent().getStringExtra("id");
        mUserProvider = new UserProvider();

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

            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
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
