package corporation.app.menchus.com.socialmediagamer.activities;

import androidx.appcompat.app.AppCompatActivity;
import corporation.app.menchus.com.socialmediagamer.R;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.view.View;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView mcircleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mcircleImageView = findViewById(R.id.circleImageBack);

        mcircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
