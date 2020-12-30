package corporation.app.menchus.com.socialmediagamer.activities;

import androidx.appcompat.app.AppCompatActivity;
import corporation.app.menchus.com.socialmediagamer.R;
import corporation.app.menchus.com.socialmediagamer.providers.AuthProvider;
import corporation.app.menchus.com.socialmediagamer.providers.PostProvider;
import corporation.app.menchus.com.socialmediagamer.providers.UserProvider;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    LinearLayout mLinearLayoutEditProfile;
    ImageView mImageViewCover;
    CircleImageView mCircleImageViewProfile;
    TextView mTextViewUserName;
    TextView mTextViewPhoneNumber;
    TextView mTextViewNumberPublications;
    TextView mTextViewEmail;
    CircleImageView mcircleImageViewBack;

    UserProvider mUserProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;

    String mExtraIdUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mImageViewCover = findViewById(R.id.imageViewCoverProfile);
        mCircleImageViewProfile = findViewById(R.id.circleImageViewProfile2);
        mTextViewUserName = findViewById(R.id.textViewUserName);
        mTextViewPhoneNumber = findViewById(R.id.textViewPhoneNumber);
        mTextViewNumberPublications = findViewById(R.id.textViewNumberPublications);
        mTextViewEmail = findViewById(R.id.textViewEmail);
        mcircleImageViewBack = findViewById(R.id.circleImageBack);

        mExtraIdUser = getIntent().getStringExtra("idUser");

        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

        mcircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getUser();
        getPostNumber();
    }

    private void getUser(){
        mUserProvider.getUser(mExtraIdUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("email")){
                        String email = documentSnapshot.getString("email");
                        mTextViewEmail.setText(email);
                    }
                    if(documentSnapshot.contains("phoneNumber")){
                        String phoneNumber = documentSnapshot.getString("phoneNumber");
                        mTextViewPhoneNumber.setText(phoneNumber);
                    }
                    if(documentSnapshot.contains("username")){
                        String username = documentSnapshot.getString("username");
                        mTextViewUserName.setText(username);
                    }
                    if(documentSnapshot.contains("image_profile")){
                        String imageProfile = documentSnapshot.getString("image_profile");
                        if(imageProfile!=null && !imageProfile.isEmpty()){
                            Picasso.with(UserProfileActivity.this).load(imageProfile).into(mCircleImageViewProfile);
                        }
                    }
                    if(documentSnapshot.contains("image_cover")){
                        String imageCover = documentSnapshot.getString("image_cover");
                        if(imageCover!=null && !imageCover.isEmpty()){
                            Picasso.with(UserProfileActivity.this).load(imageCover).into(mImageViewCover);
                        }
                    }
                }
            }
        });
    }

    private void getPostNumber(){
        mPostProvider.getPostByUser(mExtraIdUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberPost = queryDocumentSnapshots.size();
                mTextViewNumberPublications.setText(String.valueOf(numberPost));
            }
        });
    }

}
