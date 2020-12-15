package corporation.app.menchus.com.socialmediagamer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import corporation.app.menchus.com.socialmediagamer.R;
import corporation.app.menchus.com.socialmediagamer.models.User;
import corporation.app.menchus.com.socialmediagamer.providers.AuthProvider;
import corporation.app.menchus.com.socialmediagamer.providers.UserProvider;
import de.hdodenhof.circleimageview.CircleImageView;
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
import com.google.firebase.auth.AuthResult;


import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    CircleImageView circleImageViewBack;
    TextInputEditText textInputEditTextUsername;
    TextInputEditText textInputEditTextEmail;
    TextInputEditText textInputEditTextPassword;
    TextInputEditText textInputEditTextConfirmPassword;
    TextInputEditText textInputEditTextPhoneNumber;
    Button buttonRegister;
    AuthProvider mAuthProvider;
    UserProvider mUserProvider;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        circleImageViewBack = findViewById(R.id.circleImageBack);
        textInputEditTextUsername = findViewById(R.id.textInputEditTextUsername);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmailR);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPasswordR);
        textInputEditTextConfirmPassword = findViewById(R.id.textInputEditTextConfirmPassword);
        textInputEditTextPhoneNumber = findViewById(R.id.textInputEditTextPhoneNumber);
        buttonRegister = findViewById(R.id.btnRegister);
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

        circleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void register(){
        String username = textInputEditTextUsername.getText().toString();
        String email = textInputEditTextEmail.getText().toString();
        String password = textInputEditTextPassword.getText().toString();
        String confirmPassword = textInputEditTextConfirmPassword.getText().toString();
        String phoneNumber = textInputEditTextPhoneNumber.getText().toString();
        if(!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && !phoneNumber.isEmpty()){

            if(isEmailValid(email)){

                if(validatePassword(password,confirmPassword)){
                    createUser(username,email,password,phoneNumber);
                }
            }
        }else{

        }
    }

    /**
     * Verifica que dea un email valido
     * @param email
     * @return
     */
    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validatePassword(String password, String confirmPassword) {

        if(password.equals(confirmPassword)){
            if(password.length()>=8){
                return true;
            }else{
                Toast.makeText(this, "La contrasela debe ser minimo 8 caracteres", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    private void createUser(final String username, final String email, String password, final String phoneNumber) {
        mDialog.show();
        mAuthProvider.register(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = mAuthProvider.getUId();
                    User user = new User();
                    user.setId(id);
                    user.setUserName(username);
                    user.setEmail(email);
                    user.setPhoneNumber(phoneNumber);
                    user.setTimestamp(new Date().getTime());

                    mUserProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegisterActivity.this, "Fallo Registrarse", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    mDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Fallo al registrarse", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
