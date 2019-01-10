package com.example.norbert.myapplicationgit.Main;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.norbert.myapplicationgit.Login.LoginActivity;
import com.example.norbert.myapplicationgit.Login.User;
import com.example.norbert.myapplicationgit.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class ProfilFragment extends Fragment {

    private TextView firstname_textview;
    private TextView lastname_textview;
    private TextView email_textview;
    private TextView phonenumber_textview;
    private TextView address_textview;
    private ImageView profil_image;
    private DatabaseReference ref;
    private FirebaseDatabase database;
    private StorageReference mStorageRef;
    private String user_number;
    private static final int CAMERA_REQUEST_CODE = 1;
    private Uri mImageUri;
    private String mCurrentPhotoPath;
    private  Button save_button;
    private Button logout_button;
    private Button choose_picture;
    private Button my_ads;
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference mDatabaseref;
    private Uri downloadUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profil, container, false);

        firstname_textview = (EditText) rootView.findViewById(R.id.fragment_profil_firstname);
        lastname_textview = (EditText) rootView.findViewById(R.id.fragment_profil_lastname);
        email_textview = (EditText) rootView.findViewById(R.id.fragment_profil_email);
        phonenumber_textview = (EditText) rootView.findViewById(R.id.fragment_profil_phonenumber);
        address_textview = (EditText) rootView.findViewById(R.id.fragment_profil_address);
        save_button = (Button) rootView.findViewById(R.id.fragment_profil_button_save);
        logout_button = (Button) rootView.findViewById(R.id.fragment_profil_logout);
        profil_image = (ImageView) rootView.findViewById(R.id.fragment_profil_image);
        my_ads = (Button) rootView.findViewById(R.id.fragment_profil_button_my_ads);
        choose_picture = (Button) rootView.findViewById(R.id.profil_fragment_choose);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.apply();
        final String s = settings.getString("username", "Dummy");
        user_number = s;

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseref = FirebaseDatabase.getInstance().getReference("User");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (Objects.equals(dataSnapshot1.getKey(), s)) {
                        User user = dataSnapshot1.getValue(User.class);
                        User listDataUser = new User();
                        assert user != null;
                        String firstname = user.getFirstname();
                        String lastname = user.getLastname();
                        String phonenumber = user.getPhonenumber();
                        String email = user.getEmail();
                        String adress = user.getAdress();
                        String image = user.getImage();
                        listDataUser.setFirstname(firstname);
                        listDataUser.setLastname(lastname);
                        listDataUser.setPhonenumber(phonenumber);
                        listDataUser.setEmail(email);
                        listDataUser.setAdress(adress);
                        listDataUser.setImage(image);
                        setText(firstname, lastname, phonenumber, email, adress,image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity().getApplicationContext(), "Gone wrong", Toast.LENGTH_LONG).show();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //saveChanges(s);
                uploadFile();
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("username");
                editor.apply();
                startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
            }
        });

        my_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), MyadsActivity.class);
                getActivity().startActivity(myIntent);
            }
        });

        profil_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openFileChooser();
            }
        });

        choose_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Starting upload ", Toast.LENGTH_SHORT).show();
                //uploadFile();
                openFileChooser();
            }
        });

        return rootView;
    }


    private void setText(String firstname, String lastname, String phonenumber, String email, String adres,String image) {
        firstname_textview.setText(firstname);
        lastname_textview.setText(lastname);
        phonenumber_textview.setText(phonenumber);
        if (!email.equals("Dummy") && !adres.equals("Dummy") && !image.equals("https://images.idgesg.net/images/article/2017/08/android_robot_logo_by_ornecolorada_cc0_via_pixabay1904852_wide-100732483-large.jpg")) {
            email_textview.setText(email);
            address_textview.setText(adres);
            Glide.with(ProfilFragment.this)
                    .load(image)
                    .apply(new RequestOptions().override(500, 500))
                    .into(profil_image);

        }
    }

    private void setText1(String firstname,String lastname,String phonenumber,String email,String adres) {
        firstname_textview.setText(firstname);
        lastname_textview.setText(lastname);
        phonenumber_textview.setText(phonenumber);
        if (!email.equals("Dummy") && !adres.equals("Dummy")) {
            email_textview.setText(email);
            address_textview.setText(adres);
        }
    }

    private void saveChanges(final String s) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (Objects.equals(dataSnapshot1.getKey(), s)) {
                        User user = dataSnapshot1.getValue(User.class);
                        User listDataUser = new User();
                        assert user != null;
                        String firstname = user.getFirstname();
                        String lastname = user.getLastname();
                        String phonenumber = user.getPhonenumber();
                        String email = user.getEmail();
                        String adress = user.getAdress();
                        listDataUser.setFirstname(firstname_textview.getText().toString());
                        listDataUser.setLastname(lastname_textview.getText().toString());
                        listDataUser.setPhonenumber(phonenumber_textview.getText().toString());
                        listDataUser.setEmail(email_textview.getText().toString());
                        listDataUser.setAdress(address_textview.getText().toString());
                        uploadFile();
                        ref.child(s).setValue(listDataUser);
                        setText1(firstname, lastname, phonenumber, email, adress);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                if( requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                        && data != null && data.getData() != null){
                    mImageUri = data.getData();

                    Glide.with(this)
                            .load(mImageUri)
                            .apply(new RequestOptions().override(500,500))
                            .into(profil_image);
                }
    }

    public void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if(mImageUri != null){
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() +
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
                        downloadUri = task.getResult();
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                        SharedPreferences.Editor editor = settings.edit();
                        final String signed_user = settings.getString("username","Dummy");
                        Upload upload = new Upload(signed_user, downloadUri.toString());
                        User listDataUser = new User();
                        listDataUser.setFirstname(firstname_textview.getText().toString());
                        listDataUser.setLastname(lastname_textview.getText().toString());
                        listDataUser.setPhonenumber(phonenumber_textview.getText().toString());
                        listDataUser.setEmail(email_textview.getText().toString());
                        listDataUser.setAdress(address_textview.getText().toString());
                        listDataUser.setImage(downloadUri.toString());
                        mDatabaseref.child(user_number).setValue(listDataUser);

                    }else
                    {
                        Toast.makeText(getActivity(), "upload failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

