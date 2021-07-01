package com.example.justalk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    ImageView setting_save;
    EditText setting_name , setting_status;
    CircleImageView userPic;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    Uri selectedImageUri;
    String email;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setting_name = findViewById(R.id.setting_name);
        setting_save = findViewById(R.id.setting_save);
        setting_status = findViewById(R.id.setting_status);
        userPic = findViewById(R.id.userPic);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("user").child(firebaseAuth.getUid());
        StorageReference storageReference = firebaseStorage.getReference().child("upload").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = snapshot.child("email").getValue().toString();
                String name = snapshot.child("name").getValue().toString();
                String image = snapshot.child("imageUri").getValue().toString();
                String status = snapshot.child("status").getValue().toString();

                setting_name.setText(name);
                setting_status.setText(status);
                Picasso.get().load(image).into(userPic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setting_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                String name = setting_name.getText().toString();
                String status = setting_status.getText().toString();

                if(selectedImageUri != null)
                {
                    storageReference.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalImageUri = uri.toString();
                                    Users users=new Users(firebaseAuth.getUid(),name,email,finalImageUri,status);

                                    databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(SettingsActivity.this, "Data updated", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                                            }
                                            else
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(SettingsActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else
                {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String finalImageUri = uri.toString();
                            Users users=new Users(firebaseAuth.getUid(),name,email,finalImageUri,status);

                            databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(SettingsActivity.this, "Data updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(SettingsActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10)
        {
            if(data!=null)
            {
                selectedImageUri =data.getData();
                userPic.setImageURI(selectedImageUri);
            }
        }
    }
}