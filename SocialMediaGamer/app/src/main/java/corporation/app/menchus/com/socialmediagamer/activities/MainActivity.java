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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;


public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE_GOOGLE = 1;
    TextView textViewRegister;
    TextInputEditText textInputEditTextEmail;
    TextInputEditText textInputEditTextPassword;
    Button buttonLogin;
    AuthProvider mAuthProvider;
    private GoogleSignInClient mGoogleSignInClient;
    SignInButton mButtonGoogle;
    UserProvider mUsersProvider;
    AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewRegister = findViewById(R.id.textViewRegister);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        buttonLogin = findViewById(R.id.btnInicioSesion);
        mButtonGoogle = findViewById(R.id.btnLoginGoogle);
        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Loading...")
                .setCancelable(false).build();
        mAuthProvider = new AuthProvider();
        mUsersProvider = new UserProvider();



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        mButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Success", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Error", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        mDialog.show();
        mAuthProvider.googleLoging(acct).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            checkUserExists(mAuthProvider.getUId());
                        } else {
                            mDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w("Error", "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });
    }

    private void checkUserExists(final String id) {
        mUsersProvider.getUser(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    mDialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }else{
                    String email = mAuthProvider.getEmail();
                    User user = new User();
                    user.setEmail(email);
                    user.setId(id);
                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if(task.isSuccessful()){
                                Intent intent = new Intent(MainActivity.this, CompleteProfileActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(MainActivity.this, "Fallo al almacenar la información", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void login() {
        String email = textInputEditTextEmail.getText().toString();
        String password = textInputEditTextPassword.getText().toString();
        mDialog.show();
        mAuthProvider.login(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.dismiss();
                if(task.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "Correo o contraseña invalidos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuthProvider.getUserSesion()!=null){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
