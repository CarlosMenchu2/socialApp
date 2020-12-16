package corporation.app.menchus.com.socialmediagamer.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


import corporation.app.menchus.com.socialmediagamer.R;
import corporation.app.menchus.com.socialmediagamer.activities.EditProfileActivity;
import corporation.app.menchus.com.socialmediagamer.providers.AuthProvider;
import corporation.app.menchus.com.socialmediagamer.providers.PostProvider;
import corporation.app.menchus.com.socialmediagamer.providers.UserProvider;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    LinearLayout mLinearLayoutEditProfile;
    View mView;
    ImageView mImageViewCover;
    CircleImageView mCircleImageViewProfile;
    TextView mTextViewUserName;
    TextView mTextViewPhoneNumber;
    TextView mTextViewNumberPublications;
    TextView mTextViewEmail;

    UserProvider mUserProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        mImageViewCover = mView.findViewById(R.id.imageViewCoverProfile);
        mCircleImageViewProfile = mView.findViewById(R.id.circleImageViewProfile2);
        mTextViewUserName = mView.findViewById(R.id.textViewUserName);
        mTextViewPhoneNumber = mView.findViewById(R.id.textViewPhoneNumber);
        mTextViewNumberPublications = mView.findViewById(R.id.textViewNumberPublications);
        mTextViewEmail = mView.findViewById(R.id.textViewEmail);

        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();
        mLinearLayoutEditProfile = mView.findViewById(R.id.linearLayoutEditProfile);
        mLinearLayoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoEditProfile();
            }
        });
        getUser();
        getPostNumber();
        return mView;
    }

    private void gotoEditProfile() {

        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);

    }

    private void getUser(){
        mUserProvider.getUser(mAuthProvider.getUId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                            Picasso.with(getContext()).load(imageProfile).into(mCircleImageViewProfile);
                        }
                    }
                    if(documentSnapshot.contains("image_cover")){
                        String imageCover = documentSnapshot.getString("image_cover");
                        if(imageCover!=null && !imageCover.isEmpty()){
                            Picasso.with(getContext()).load(imageCover).into(mImageViewCover);
                        }
                    }
                }
            }
        });
    }

    private void getPostNumber(){
        mPostProvider.getPostByUser(mAuthProvider.getUId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberPost = queryDocumentSnapshots.size();
                mTextViewNumberPublications.setText(String.valueOf(numberPost));
            }
        });
    }

}
