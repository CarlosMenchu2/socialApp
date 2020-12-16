package corporation.app.menchus.com.socialmediagamer.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import corporation.app.menchus.com.socialmediagamer.R;
import corporation.app.menchus.com.socialmediagamer.models.Post;
import corporation.app.menchus.com.socialmediagamer.providers.AuthProvider;
import corporation.app.menchus.com.socialmediagamer.providers.ImageProvider;
import corporation.app.menchus.com.socialmediagamer.providers.PostProvider;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.util.Date;

public class PostActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 1;
    private final int REQUEST_CODE_2 = 2;
    private final int PHOTO_REQUEST_CODE = 3;
    private final int PHOTO_REQUEST_CODE_2 = 4;
    ImageView mImageViewPost1;
    ImageView mImageViewPost2;
    ImageView mImageViewPc;
    ImageView mimageViewPS;
    ImageView mImageViewXbox;
    ImageView mImageViewN;
    File mImageFile;
    File mImageFile2;
    Button mButton;
    ImageProvider mImageProvider;
    TextInputEditText mTextInpuNameVideoGame;
    TextInputEditText mTextInpuDescriptionVideoGame;
    TextView mTetxyViewCategory;
    PostProvider mPostProvider;
    String mCateogry ="";
    String title;
    String description;
    AuthProvider mAuthProvider;
    AlertDialog mDialog;
    CircleImageView mCircleImageBack;

    AlertDialog.Builder mBuilderSelector;
    CharSequence  options[];

    String mAbsolutePhotoPhat;
    String mPhotoPath;
    File mPhotoFile;

    String mAbsolutePhotoPhat2;
    String mPhotoPath2;
    File mPhotoFile2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mImageViewPost1 = findViewById(R.id.imageViewPost1);
        mImageViewPost1 = findViewById(R.id.imageViewPost2);
        mButton = findViewById(R.id.btnPost);
        mImageProvider = new ImageProvider();
        mTextInpuNameVideoGame = findViewById(R.id.textInputVideogame);
        mTextInpuDescriptionVideoGame = findViewById(R.id.textInputDescription);
        mImageViewPc = findViewById(R.id.imageViewPc);
        mimageViewPS = findViewById(R.id.imageViewPS);
        mImageViewXbox = findViewById(R.id.imageViewXBox);
        mImageViewN = findViewById(R.id.imageViewN);
        mTetxyViewCategory = findViewById(R.id.textViewCategory);
        mCircleImageBack = findViewById(R.id.circleImageBack);
        mPostProvider = new PostProvider();
        mAuthProvider = new AuthProvider();
        mBuilderSelector = new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Selecciona una opcion");
        options = new CharSequence[]{"Galeria","Foto"};
        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Loading...")
                .setCancelable(false).build();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPost();
            }
        });

        mCircleImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mImageViewPc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCateogry = "PC";
                mTetxyViewCategory.setText(mCateogry);
            }
        });

        mimageViewPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCateogry = "PS4";
                mTetxyViewCategory.setText(mCateogry);
            }
        });

        mImageViewXbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCateogry = "XBox";
                mTetxyViewCategory.setText(mCateogry);
            }
        });

        mImageViewN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCateogry = "Nintendo";
                mTetxyViewCategory.setText(mCateogry);
            }
        });


        mImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(1);

            }
        });

        mImageViewPost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(2);
            }
        });
    }

    private void selectOptionImage(final int numbrerImage) {
        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    if(numbrerImage == 1){
                        openGalery(REQUEST_CODE);
                    }else if(numbrerImage==2){
                        openGalery(REQUEST_CODE_2);
                    }

                }else if(which == 1){
                    if(numbrerImage == 1){
                        takePhoto(PHOTO_REQUEST_CODE);
                    }else if(numbrerImage==2){
                        takePhoto(PHOTO_REQUEST_CODE_2);
                    }
                }
            }
        });

        mBuilderSelector.show();
    }

    private void takePhoto(int requestCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            File photoFile = null;

            try {
                photoFile = createPhotoFile(requestCode);
            }catch (Exception e){
                Toast.makeText(this, "Hubo un error con el archivo", Toast.LENGTH_LONG).show();
            }

            if(photoFile!=null){
                Uri photoUri = FileProvider.getUriForFile(PostActivity.this, "corporation.app.menchus.com.socialmediagamer",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(takePictureIntent,PHOTO_REQUEST_CODE);

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

        if(requestCode==PHOTO_REQUEST_CODE){
            mPhotoPath = "file:"+mPhotoFile.getAbsolutePath();
            mAbsolutePhotoPhat = mPhotoFile.getAbsolutePath();
        }else if(requestCode==PHOTO_REQUEST_CODE_2){
            mPhotoPath2 = "file:"+mPhotoFile.getAbsolutePath();
            mAbsolutePhotoPhat2 = mPhotoFile.getAbsolutePath();
        }


        return mPhotoFile;
    }

    private void clickPost() {
        title = mTextInpuNameVideoGame.getText().toString();
        description = mTextInpuDescriptionVideoGame.getText().toString();

        if(!title.isEmpty() && !description.isEmpty() && !mCateogry.isEmpty()){
            if(mImageFile!=null && mImageFile2!=null){
                saveImage(mImageFile,mImageFile2);
            //Selecciono las dos fotos de la camara
            }else if(mPhotoFile!=null && mPhotoFile2!=null){
                saveImage(mPhotoFile,mPhotoFile2);
            } else if(mImageFile!=null && mPhotoFile2!=null){
                saveImage(mImageFile,mPhotoFile2);
            }else if(mPhotoFile!=null && mImageFile2!=null){
                saveImage(mPhotoFile,mImageFile2);
            }else{
                Toast.makeText(this, "Slecciona una imagen", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show();
        }


    }

    private void saveImage(File imageFile1, final File imageFile2 ) {
        mDialog.show();
        mImageProvider.save(PostActivity.this,imageFile1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = uri.toString();

                            mImageProvider.save(PostActivity.this,imageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if(taskImage2.isSuccessful()){
                                        mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {
                                                String url2 = uri2.toString();

                                                Post post = new Post();
                                                post.setImage1(url);
                                                post.setImage2(url2);
                                                post.setTitle(title);
                                                post.setDescription(description);
                                                post.setCategory(mCateogry);
                                                post.setIdUser(mAuthProvider.getUId());
                                                post.setTimestamp(new Date().getTime());
                                                mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> taskSave) {
                                                        mDialog.dismiss();
                                                        if(taskSave.isSuccessful()){
                                                            clearForm();
                                                            Toast.makeText(PostActivity.this, "Informacion almacenada correctamente", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            Toast.makeText(PostActivity.this, "No se pudo almacenar la informacion", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }else{
                                        mDialog.dismiss();
                                        Toast.makeText(PostActivity.this, "La imagen 2 no se almaceno", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });
                }else{
                    mDialog.dismiss();
                    Toast.makeText(PostActivity.this, "Error al almacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void clearForm() {
        mTextInpuNameVideoGame.setText("");
        mTextInpuDescriptionVideoGame.setText("");
        mTetxyViewCategory.setText("");
        mImageViewPost1.setImageResource(R.drawable.photo);
        mImageViewPost2.setImageResource(R.drawable.photo);
        title="";
        description="";
        mImageFile=null;
        mImageFile2 =null;
        mCateogry="";
    }

    private void openGalery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,requestCode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode == RESULT_OK){
            try {
                mPhotoFile=null;
                mImageFile = FileUtil.from(this,data.getData());
                mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            }catch (Exception e){
                Log.d("ERROR","Se produjo un error "+e.getMessage());
                Toast.makeText(this, "Se produjo un error", Toast.LENGTH_LONG).show();
            }
        }


        if(requestCode==REQUEST_CODE_2 && resultCode == RESULT_OK){
            try {
                mPhotoFile2=null;
                mImageFile2 = FileUtil.from(this,data.getData());
                mImageViewPost2.setImageBitmap(BitmapFactory.decodeFile(mImageFile2.getAbsolutePath()));
            }catch (Exception e){
                Log.d("ERROR","Se produjo un error "+e.getMessage());
                Toast.makeText(this, "Se produjo un error", Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode==PHOTO_REQUEST_CODE &&resultCode==RESULT_OK){
            mImageFile=null;
            mPhotoFile = new File(mAbsolutePhotoPhat);
            Picasso.with(PostActivity.this).load(mPhotoPath).into(mImageViewPost1);
        }

        if(requestCode==PHOTO_REQUEST_CODE_2 &&resultCode==RESULT_OK){
            mImageFile2=null;
            mPhotoFile2 = new File(mAbsolutePhotoPhat2);
            Picasso.with(PostActivity.this).load(mPhotoPath2).into(mImageViewPost2);
        }
    }
}
