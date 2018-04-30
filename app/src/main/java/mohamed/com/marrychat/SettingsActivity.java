package mohamed.com.marrychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {
private DatabaseReference mUserDatabase;
private FirebaseUser user;
private CircleImageView userimage;
private TextView username;
private TextView userstatus;
private Button change_st;
private Button change_image_btn;
    private ProgressDialog mProgressDialog;

public static final int REQUESTGALLERY = 1;
//Storage Firebase
    private StorageReference mImageStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // java utils
        userimage=(CircleImageView)findViewById(R.id.setting_img_profil);
        username=(TextView)findViewById(R.id.name_settings);
        userstatus=(TextView)findViewById(R.id.message_settings);
        change_st=(Button)findViewById(R.id.change_statut_sta);
        change_image_btn=(Button)findViewById(R.id.chaging_image_btn);
        mImageStorage= FirebaseStorage.getInstance().getReference();
        change_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(SettingsActivity.this,StatutActivity.class));
                finish();
            }
        });


        user= FirebaseAuth.getInstance().getCurrentUser();
        String uid =user.getUid();


        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        mUserDatabase.keepSynced(true);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              //  Toast.makeText(SettingsActivity.this,dataSnapshot.toString(),Toast.LENGTH_LONG).show();

                String name=dataSnapshot.child("Displayname").getValue().toString();
                String status=dataSnapshot.child("statut").getValue().toString();
                final String image=dataSnapshot.child("image").getValue().toString();
                String image_thum=dataSnapshot.child("thum_imge").getValue().toString();
                username.setText(name);
                userstatus.setText(status);
             //   Picasso.with(SettingsActivity.this).load(image).into(userimage);
                if (!image.equals("default")){


                }
                if(!image.equals("default")) {

                    //Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                    Picasso.with(SettingsActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.avatar).into(userimage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.avatar)
                                    .into(userimage);
                        }
                    });

                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;

        change_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start picker to get image for cropping and then use the image in cropping activity
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingsActivity.this);
                //Using Intent
                Intent intentGallery =new Intent();
                intentGallery.setType("image/*");
                intentGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentGallery,"Select image"),REQUESTGALLERY);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUESTGALLERY && resultCode==RESULT_OK){
            //When we use Intent
            String img_uri=data.getDataString();
          //  Toast.makeText(getApplicationContext(),img_uri,Toast.LENGTH_LONG).show();
            //When wue use image cropping
            Uri imageuri = data.getData();
            //rectangle resize
            CropImage.activity().setAspectRatio(1,1).start(this);


        }



        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //progress dialog
                mProgressDialog=new ProgressDialog(SettingsActivity.this);
                mProgressDialog.setTitle("Uploading image...");
                mProgressDialog.setMessage("Please wait to upload the image");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
                Uri resultUri = result.getUri();
                File fil_path= new File(resultUri.getPath());
                String uid =user.getUid();
                //image compressed
                final Bitmap thumb_bitmap=new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(fil_path);
                //firbase image commpressed
                ByteArrayOutputStream boas=new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,100,boas);
                //where we save the data
                final byte[] thumb_byte=boas.toByteArray();



                StorageReference filepath=mImageStorage.child("profile_images").child(uid+"jpg");
                //reference to thumb_image
                final StorageReference thumb_path=mImageStorage.child("profile_image").child("thum_imge").child(uid+"jpg");
                //listener to succ fo task
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            final String downloadURL=task.getResult().getDownloadUrl().toString();
                            //upload_task
                            UploadTask uploadTask=thumb_path.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    String Thumb_downloadUrl=task.getResult().getDownloadUrl().toString();
                                    if (task.isSuccessful()){

                                        Map update_HashMpa=new HashMap();
                                        update_HashMpa.put("image",downloadURL);
                                        update_HashMpa.put("thum_imge",Thumb_downloadUrl);
                                        mUserDatabase.updateChildren(update_HashMpa).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    mProgressDialog.dismiss();

                                                    Toast.makeText(getApplicationContext(),"it's uploading", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                    }else{
                                        Toast.makeText(getApplicationContext(),"error upload", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });


                        }
                        else {
                            Toast.makeText(getApplicationContext(),"dont work", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
