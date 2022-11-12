package com.dmt.ledonchung.coffeehouse.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dmt.ledonchung.coffeehouse.MainActivity;
import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.model.User;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateUserActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 999;
    private CircleImageView img_user;
    private EditText firstname_user, lastname_user
            , phonenumber_user;
    private Spinner gender_user;
    private TextView dayofbirth_user, email_user;
    private Button btnUpdate;
    private Toolbar toolbar;
    private Uri resultUri;
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        if(intent == null) return;
                        resultUri = intent.getData();
                        img_user.setImageURI(resultUri);
                    }
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        initUI();
        setToolBar();
        eventButton();
        getInformationUser();
    }
    public void setToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Cập nhật thông tin cá nhân");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void eventButton() {
        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
    }

    private void updateUser() {
        String lastName = lastname_user.getText().toString().trim();
        String firstName = firstname_user.getText().toString().trim();
        String phoneNumber = phonenumber_user.getText().toString().trim();
        boolean gender = gender_user.getSelectedItem().toString().trim().equals("Nam") == true ? true : false;
        User user = new User(firstName, lastName, phoneNumber, gender);
        user.setUrlPhoto(String.valueOf(resultUri));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        // update image
        // Đưa file image vào firebase store
        if(user.getUrlPhoto() != null) {
            final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                    .child("profile images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(user.getUrlPhoto()));
            } catch (IOException e) {
                e.printStackTrace();
            }


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();

            UploadTask uploadTask = filePath.putBytes(data);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {
                        Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Map map = new HashMap();
                                String url = uri.toString().trim();
                                map.put("urlPhoto", url);
                                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);
                            }
                        });
                    }
                }
            });
            reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(user.toMapUpdate())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(UpdateUserActivity.this, "Update thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(UpdateUserActivity.this, "Update thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    public void initUI() {
        toolbar = findViewById(R.id.toolbar);
        img_user = findViewById(R.id.img_user);
        firstname_user = findViewById(R.id.firstname_user);
        lastname_user = findViewById(R.id.lastname_user);
        phonenumber_user = findViewById(R.id.phonenumber_user);
        dayofbirth_user = findViewById(R.id.dayofbirth_user);
        gender_user = findViewById(R.id.gender_user);
        email_user = findViewById(R.id.email_user);
        btnUpdate = findViewById(R.id.btnUpdate);
    }
    private void onClickRequestPermission() {
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions, MY_REQUEST_CODE);
        }
    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activityResultLauncher.launch(Intent.createChooser(intent, "Get Picture"));
    }
    public void getInformationUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                Picasso.get()
                        .load(user.getUrlPhoto())
                        .placeholder(R.drawable.ic_user)
                        .error(R.drawable.ic_user)
                        .into(img_user);

                firstname_user.setText(user.getFirstName());
                lastname_user.setText(user.getLastName());
                phonenumber_user.setText(user.getPhoneNumber());
                dayofbirth_user.setText(user.getDayOfBirth().toString());
                email_user.setText(user.getEmail());

                if(user.getGender()) {
                    // Nam
                    gender_user.setSelection(1);
                } else {
                    gender_user.setSelection(2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        }
    }

}