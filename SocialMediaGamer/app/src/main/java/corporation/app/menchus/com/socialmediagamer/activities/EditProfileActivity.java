package corporation.app.menchus.com.socialmediagamer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import corporation.app.menchus.com.socialmediagamer.R;
import corporation.app.menchus.com.socialmediagamer.models.Post;
import corporation.app.menchus.com.socialmediagamer.models.User;
import corporation.app.menchus.com.socialmediagamer.providers.AuthProvider;
import corporation.app.menchus.com.socialmediagamer.providers.ImageProvider;
import corporation.app.menchus.com.socialmediagamer.providers.UserProvider;
import corporation.app.menchus.com.socialmediagamer.utils.FileUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView mcircleImageView;
    CircleImageView mcircleImageViewProfile;
    ImageView mImageView;
    TextInputEditText mTextInputEditTextUsername;
    TextInputEditText mTextInputEditTextPhoneNumber;
    Button btnEditProfile;
    AlertDialog.Builder mBuilderSelector;
    CharSequence  options[];
    String mUsername = "";
    String mPhoneNumber = "";
    String mImageProfile;
    String mImageCover;
    AlertDialog mDialog;
    ImageProvider mImageProvider;
    UserProvider mUserProvider;
    AuthProvider mAuthProvider;

    String mAbsolutePhotoPhat;
    String mPhotoPath;
    File mPhotoFile;

    String mAbsolutePhotoPhat2;
    String mPhotoPath2;
    File mPhotoFile2;

    File mImageFile;
    File mImageFile2;
    private final int REQUEST_CODE_PROFILE = 1;
    private final int REQUEST_CODE_COVER_2 = 2;
    private final int PHOTO_REQUEST_CODE_PROFILE = 3;
    private final int PHOTO_REQUEST_CODE_COVER_2 = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mcircleImageView = findViewById(R.id.circleImageBack);
        mcircleImageViewProfile = findViewById(R.id.circleImageVewProfile);
        mImageView = findViewById(R.id.imageViewCover);
        mTextInputEditTextUsername = findViewById(R.id.textInputEditTextUsernameEdit);
        mTextInputEditTextPhoneNumber = findViewById(R.id.textInputEditTextPhoneNumberEdit);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        mImageProvider = new ImageProvider();
        mcircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();

        mBuilderSelector = new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Selecciona una opcion");
        options = new CharSequence[]{"Galeria","Foto"};

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Loading...")
                .setCancelable(false).build();

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEditProfile();
            }
        });

        mcircleImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(1);
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(2);
            }
        });

        mcircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getUser();
    }

    private void clickEditProfile() {

        mUsername = mTextInputEditTextUsername.getText().toString();
        mPhoneNumber = mTextInputEditTextPhoneNumber.getText().toString();

        if(!mUsername.isEmpty() && !mPhoneNumber.isEmpty()){

            if(mImageFile!=null && mImageFile2!=null){
                saveImageCoverAndProfile(mImageFile,mImageFile2);
                //Selecciono las dos fotos de la camara
            }else if(mPhotoFile!=null && mPhotoFile2!=null){
                saveImageCoverAndProfile(mPhotoFile,mPhotoFile2);
            } else if(mImageFile!=null && mPhotoFile2!=null){
                saveImageCoverAndProfile(mImageFile,mPhotoFile2);
            }else if(mPhotoFile!=null && mImageFile2!=null){
                saveImageCoverAndProfile(mPhotoFile,mImageFile2);
            }else if(mPhotoFile!=null){
                saveImage(mPhotoFile,true);
            }else if(mPhotoFile2!=null){
                saveImage(mPhotoFile2,false);
            }else if(mImageFile!=null){
                saveImage(mImageFile,true);
            }else if(mImageFile2!=null){
                saveImage(mImageFile2,false);
            } else{
                User user = new User();
                user.setUserName(mUsername);
                user.setPhoneNumber(mPhoneNumber);
                user.setId(mAuthProvider.getUId());
                user.setTimestamp(new Date().getTime());
                updateInfo(user);
            }

        }else{
            Toast.makeText(this, "Complete loa campos", Toast.LENGTH_SHORT).show();
        }

    }

    private void getUser(){
        mUserProvider.getUser(mAuthProvider.getUId()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("username")){
                        mUsername = documentSnapshot.getString("username");
                        mTextInputEditTextUsername.setText(mUsername);
                    }
                    if(documentSnapshot.contains("phoneNumber")){
                        mPhoneNumber = documentSnapshot.getString("phoneNumber");
                        mTextInputEditTextPhoneNumber.setText(mPhoneNumber);
                    }
                    if(documentSnapshot.contains("image_profile")){
                        mImageProfile = documentSnapshot.getString("image_profile");
                        if(mImageProfile!=null && !mImageProfile.isEmpty()){
                            Picasso.with(EditProfileActivity.this).load(mImageProfile).into(mcircleImageViewProfile);
                        }

                    }
                    if(documentSnapshot.contains("image_cover")){
                        mImageCover = documentSnapshot.getString("image_cover");
                        if(mImageCover!=null && !mImageCover.isEmpty()){
                            Picasso.with(EditProfileActivity.this).load(mImageCover).into(mImageView);
                        }
                    }
                }
            }
        });
    }

    private void selectOptionImage(final int numbrerImage) {
        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    if(numbrerImage == 1){
                        openGalery(REQUEST_CODE_PROFILE);
                    }else if(numbrerImage==2){
                        openGalery(REQUEST_CODE_COVER_2);
                    }

                }else if(which == 1){
                    if(numbrerImage == 1){
                        takePhoto(PHOTO_REQUEST_CODE_PROFILE);
                    }else if(numbrerImage==2){
                        takePhoto(PHOTO_REQUEST_CODE_COVER_2);
                    }
                }
            }
        });

        mBuilderSelector.show();
    }

    private void openGalery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,requestCode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_PROFILE && resultCode == RESULT_OK){
            try {
                mPhotoFile=null;
                mImageFile = FileUtil.from(this,data.getData());
                mcircleImageViewProfile.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            }catch (Exception e){
                Log.d("ERROR","Se produjo un error "+e.getMessage());
                Toast.makeText(this, "Se produjo un error", Toast.LENGTH_LONG).show();
            }
        }


        if(requestCode==REQUEST_CODE_COVER_2&& resultCode == RESULT_OK){
            try {
                mPhotoFile2=null;
                mImageFile2 = FileUtil.from(this,data.getData());
                mImageView.setImageBitmap(BitmapFactory.decodeFile(mImageFile2.getAbsolutePath()));
            }catch (Exception e){
                Log.d("ERROR","Se produjo un error "+e.getMessage());
                Toast.makeText(this, "Se produjo un error", Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode==PHOTO_REQUEST_CODE_PROFILE &&resultCode==RESULT_OK){
            mImageFile=null;
            mPhotoFile = new File(mAbsolutePhotoPhat);
            Picasso.with(EditProfileActivity.this).load(mPhotoPath).into(mcircleImageViewProfile);
        }

        if(requestCode==PHOTO_REQUEST_CODE_COVER_2 &&resultCode==RESULT_OK){
            mImageFile2=null;
            mPhotoFile2 = new File(mAbsolutePhotoPhat2);
            Picasso.with(EditProfileActivity.this).load(mPhotoPath2).into(mImageView);
        }
    }

    private void takePhoto(int requestCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createPhotoFile(requestCode);
            } catch (Exception e) {
                Toast.makeText(this, "Hubo un error con el archivo", Toast.LENGTH_LONG).show();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(EditProfileActivity.this, "corporation.app.menchus.com.socialmediagamer", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, PHOTO_REQUEST_CODE_PROFILE);

            }
        }
    }

    private File createPhotoFile(int requestCode) {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File photoFile = File.createTempFile(
                    new Date() + "_photo",
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(requestCode==PHOTO_REQUEST_CODE_PROFILE){
            mPhotoPath = "file:"+mPhotoFile.getAbsolutePath();
            mAbsolutePhotoPhat = mPhotoFile.getAbsolutePath();
        }else if(requestCode==PHOTO_REQUEST_CODE_COVER_2){
            mPhotoPath2 = "file:"+mPhotoFile.getAbsolutePath();
            mAbsolutePhotoPhat2 = mPhotoFile.getAbsolutePath();
        }


        return mPhotoFile;
    }


    private void saveImageCoverAndProfile(File imageFile1, final File imageFile2 ) {
        mDialog.show();
        mImageProvider.save(EditProfileActivity.this,imageFile1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String urlProfle = uri.toString();

                            mImageProvider.save(EditProfileActivity.this,imageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if(taskImage2.isSuccessful()){
                                        mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {
                                                String urlCover = uri2.toString();

                                                User user = new User();
                                                user.setId(mAuthProvider.getUId());
                                                user.setUserName(mUsername);
                                                user.setPhoneNumber(mPhoneNumber);
                                                user.setImage_profile(urlProfle);
                                                user.setImage_cover(urlCover);
                                                user.setTimestamp(new Date().getTime());
                                                updateInfo(user);
                                            }
                                        });
                                    }else{
                                        mDialog.dismiss();
                                        Toast.makeText(EditProfileActivity.this, "La imagen 2 no se almaceno", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }else{
                    mDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Error al almacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateInfo(User user){

        if(mDialog.isShowing()){
            mDialog.show();
        }
        mUserProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this, "Informacion actualizada", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EditProfileActivity.this, "Error al actualizar informacion", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void saveImage(File image, final boolean isProfileImage){

        mDialog.show();
        mImageProvider.save(EditProfileActivity.this,image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = uri.toString();

                            User user = new User();
                            user.setId(mAuthProvider.getUId());
                            user.setUserName(mUsername);
                            user.setPhoneNumber(mPhoneNumber);
                            if(isProfileImage){
                                user.setImage_profile(url);
                                user.setImage_cover(mImageCover);
                            }else{
                                user.setImage_cover(url);
                                user.setImage_profile(mImageProfile);
                            }
                            user.setTimestamp(new Date().getTime());
                            updateInfo(user);

                        }
                    });
                }else{
                    mDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Error al almacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
