package corporation.app.menchus.com.socialmediagamer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import corporation.app.menchus.com.socialmediagamer.R;
import corporation.app.menchus.com.socialmediagamer.models.User;
import corporation.app.menchus.com.socialmediagamer.providers.AuthProvider;
import corporation.app.menchus.com.socialmediagamer.providers.UserProvider;
import dmax.dialog.SpotsDialog;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

public class CompleteProfileActivity extends AppCompatActivity {


    TextInputEditText textInputEditTextUsername;
    TextInputEditText textInputEditTextPhoneNumber;
    Button buttonRegister;
    AuthProvider mAuthProvider;
    UserProvider mUserProvider;
    AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        textInputEditTextUsername = findViewById(R.id.textInputEditTextUsernameC);
        textInputEditTextPhoneNumber = findViewById(R.id.textInputEditTextPhoneNumberC);
        buttonRegister = findViewById(R.id.btnConfirm);
        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();
        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Loading...")
                .setCancelable(false).build();


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void register(){
        String username = textInputEditTextUsername.getText().toString();
        String phoneNumber = textInputEditTextPhoneNumber.getText().toString();

        if(!username.isEmpty() && !phoneNumber.isEmpty()){

            updateUser(username, phoneNumber);

        }else{
            Toast.makeText(this, "Ingrese el nombre de usuario", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateUser(final String username, String phoneNumber) {
        String id = mAuthProvider.getUId();
        User user = new User();
        user.setId(id);
        user.setUserName(username);
        user.setPhoneNumber(phoneNumber);
        user.setTimestamp(new Date().getTime());
        mDialog.show();
        mUserProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){
                    Intent intent = new Intent(CompleteProfileActivity.this,HomeActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(CompleteProfileActivity.this, "Fallo Registrarse", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
