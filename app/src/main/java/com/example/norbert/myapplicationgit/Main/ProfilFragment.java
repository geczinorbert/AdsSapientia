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
    private Uri imageUri;
    private Uri uri;
    private String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                        "com.example.norbert.myapplicationgit.provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profil, container, false);

        firstname_textview = (EditText) rootView.findViewById(R.id.fragment_profil_firstname);
        lastname_textview = (EditText) rootView.findViewById(R.id.fragment_profil_lastname);
        email_textview = (EditText) rootView.findViewById(R.id.fragment_profil_email);
        phonenumber_textview = (EditText) rootView.findViewById(R.id.fragment_profil_phonenumber);
        address_textview = (EditText) rootView.findViewById(R.id.fragment_profil_address);
        Button save_button = (Button) rootView.findViewById(R.id.fragment_profil_button_save);
        Button logout_button = (Button) rootView.findViewById(R.id.fragment_profil_logout);
        profil_image = (ImageView) rootView.findViewById(R.id.fragment_profil_image);
        Button my_ads = (Button) rootView.findViewById(R.id.fragment_profil_button_my_ads);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.apply();
        final String s = settings.getString("username", "Dummy");
        user_number = s;

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

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
                        listDataUser.setFirstname(firstname);
                        listDataUser.setLastname(lastname);
                        listDataUser.setPhonenumber(phonenumber);
                        listDataUser.setEmail(email);
                        listDataUser.setAdress(adress);
                        setText(firstname, lastname, phonenumber, email, adress);
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
                saveChanges(s);
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

        profil_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        my_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), MyadsActivity.class);
                getActivity().startActivity(myIntent);
            }
        });

        return rootView;
    }

    private void setText(String firstname, String lastname, String phonenumber, String email, String adres) {
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
                System.out.println("TAG 11" + s);
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
                        setText(firstname, lastname, phonenumber, email, adress);
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
        System.out.println("Tag 1" + "fuggvenyben");
        if (resultCode == RESULT_OK) {
            Glide.with(ProfilFragment.this)
                    .load(mCurrentPhotoPath)
                    .apply(new RequestOptions().override(500, 500))
                    .into(profil_image);

            System.out.println("TAG 1 : " + mCurrentPhotoPath + "    : " + Uri.parse(mCurrentPhotoPath).toString());

        }
    }
    private void uploadFile(){
        imageUri = Uri.parse(String.valueOf(new File(mCurrentPhotoPath)));
        if(mCurrentPhotoPath != null){
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() +
                    "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                        System.out.println("TAG771 profile pic: \t" + downloadUri.toString());
                        DatabaseReference mDatabaseref = FirebaseDatabase.getInstance().getReference("uploads");
                        Upload upload = new Upload("Dummy",
                                downloadUri.toString());
                        mDatabaseref.child(user_number).setValue(upload);
                    }else
                    {
                        Toast.makeText(getActivity(), "upload failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}

