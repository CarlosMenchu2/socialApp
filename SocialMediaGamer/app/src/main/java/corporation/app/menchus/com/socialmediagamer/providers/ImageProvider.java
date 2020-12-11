package corporation.app.menchus.com.socialmediagamer.providers;

import android.content.Context;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

import corporation.app.menchus.com.socialmediagamer.utils.CompressorBitmapImage;

public class ImageProvider {

    StorageReference mStorage;

    public ImageProvider(){
        mStorage = FirebaseStorage.getInstance().getReference();
    }

    public UploadTask save(Context context, File file){
        byte[] imageByte = CompressorBitmapImage.getImage(context,file.getPath(),500,500);
        StorageReference storage = FirebaseStorage.getInstance().getReference().child(new Date()+".jpg");
        mStorage=storage;
        UploadTask taxt = storage.putBytes(imageByte);
        return  taxt;
    }

    public StorageReference getStorage(){
        return mStorage;
    }

}
