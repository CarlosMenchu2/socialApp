package corporation.app.menchus.com.socialmediagamer.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import corporation.app.menchus.com.socialmediagamer.R;
import corporation.app.menchus.com.socialmediagamer.activities.EditProfileActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    LinearLayout mLinearLayoutEditProfile;
    View mView;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        mLinearLayoutEditProfile = mView.findViewById(R.id.linearLayoutEditProfile);
        mLinearLayoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoEditProfile();
            }
        });
        return mView;
    }

    private void gotoEditProfile() {

        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);

    }

}
