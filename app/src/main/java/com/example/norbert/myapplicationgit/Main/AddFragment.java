package com.example.norbert.myapplicationgit.Main;

import android.content.ContentResolver;
import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.example.norbert.myapplicationgit.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class AddFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView textViewTitle;
    private TextView textViewShortDescription;
    private TextView textViewLongDescription;
    private TextView textViewPhoneNumber;
    private TextView textViewLocationText;
    private Button buttonAdd;
    private Button buttonChoose;
    private ImageView imageView;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private StorageReference refStorage;
    private DatabaseReference refDatabaseStorage;
    private Uri mImageUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView = inflater.inflate(R.layout.fragment_add, container, false);
       textViewTitle = (EditText) rootView.findViewById(R.id.fragment_add_title_edit_text);
       textViewLocationText = (EditText) rootView.findViewById(R.id.fragment_add_location_text);
       textViewLongDescription = (EditText) rootView.findViewById(R.id.fragment_add_long_description);
       textViewShortDescription = (EditText) rootView.findViewById(R.id.fragment_add_short_description_edit_text);
       textViewPhoneNumber = (EditText) rootView.findViewById(R.id.fragment_add_phone_number);
       buttonAdd = (Button) rootView.findViewById(R.id.fragment_add_button);
       buttonChoose = (Button) rootView.findViewById(R.id.add_fragment_button_choose);
       imageView = (ImageView) rootView.findViewById(R.id.add_fragment_image);


       mAuth = FirebaseAuth.getInstance();
       database = FirebaseDatabase.getInstance();
       ref = database.getReference("Ads");

       refStorage = FirebaseStorage.getInstance().getReference("Ads");
       refDatabaseStorage = FirebaseDatabase.getInstance().getReference("Ads");

       buttonChoose.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               openFileChooser();
           }
       });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Starting upload",Toast.LENGTH_SHORT).show();
                uploadFile();
            }
        });

        return rootView;
    }


    public void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            mImageUri = data.getData();
            Glide.with(AddFragment.this)
                    .load(mImageUri)
                    .apply(new RequestOptions().override(500,500))
                    .into(imageView);

        }
    }


    private String getFileExtension(Uri uri){
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //Here we upload the file into the firebase storage
    private void uploadFile(){
        if(mImageUri != null){
            final StorageReference fileReference = refStorage.child(System.currentTimeMillis() +
                    "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                         Toast.makeText(getActivity(), "Upload successful: ", Toast.LENGTH_SHORT).show();
                        Uri downloadUri = task.getResult();
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                        SharedPreferences.Editor editor = settings.edit();
                        final String signed_user = settings.getString("username","Dummy");
                        Ads ads = new Ads(textViewTitle.getText().toString(),textViewShortDescription.getText().toString(),
                                textViewLongDescription.getText().toString(),textViewPhoneNumber.getText().toString(),
                                textViewLocationText.getText().toString(),downloadUri.toString(),signed_user);
                        refDatabaseStorage.child(textViewTitle.getText().toString()).setValue(ads);
                    }else
                    {
                        Toast.makeText(getActivity(), "upload failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
