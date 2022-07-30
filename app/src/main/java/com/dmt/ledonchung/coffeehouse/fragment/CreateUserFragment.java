package com.dmt.ledonchung.coffeehouse.fragment;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dmt.ledonchung.coffeehouse.MainActivity;
import com.dmt.ledonchung.coffeehouse.R;
import com.dmt.ledonchung.coffeehouse.model.Date;
import com.dmt.ledonchung.coffeehouse.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateUserFragment extends Fragment {
    public interface SendUserFragment {
        void send(User user);
    }

    final private int MY_REQUEST_CODE = 999;
    private CircleImageView img_user;
    private EditText firstname_user, lastname_user
            , phonenumber_user, dayofbirth_user;
    private Spinner gender_user;
    private Button btnUpdate;
    private Uri resultUri;
    private View view;
    private TextView email_user;
    private MainActivity mainActivity;
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == mainActivity.RESULT_OK) {
                        Intent intent = result.getData();
                        if(intent == null) return;
                        resultUri = intent.getData();
                        img_user.setImageURI(resultUri);
                    }
                }
            }
    );
    private SendUserFragment sendUserFragment;

    public CreateUserFragment(SendUserFragment sendUserFragment) {
        this.sendUserFragment = sendUserFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_create_user, container, false);
        initUI();
        onActionButtonListener();
        getInformationUser();
        return view;
    }
    public void getInformationUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email_user.setText(snapshot.child("email").getValue().toString().trim());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void initUI() {
        mainActivity = (MainActivity) getActivity();
        img_user = view.findViewById(R.id.img_user);
        firstname_user = view.findViewById(R.id.firstname_user);
        lastname_user = view.findViewById(R.id.lastname_user);
        phonenumber_user = view.findViewById(R.id.phonenumber_user);
        dayofbirth_user = view.findViewById(R.id.dayofbirth_user);
        gender_user = view.findViewById(R.id.gender_user);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        email_user = view.findViewById(R.id.email_user);

    }
    public void onActionButtonListener() {
        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });
        dayofbirth_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

    }

    private void createUser() {
        String firstName = firstname_user.getText().toString().trim();
        if(TextUtils.isEmpty(firstName)) {
            firstname_user.setError("Vui lòng nhập họ và tên đệm");
            return;
        }

        String lastName = lastname_user.getText().toString().trim();
        if(TextUtils.isEmpty(lastName)) {
            lastname_user.setError("Vui lòng nhập tên");
            return;
        }


        String phone = phonenumber_user.getText().toString().trim();
        if(TextUtils.isEmpty(phone)) {
            phonenumber_user.setError("Vui lòng nhập số điện thoại");
            return;
        }
        String dateOfBirth = dayofbirth_user.getText().toString().trim();
        if(TextUtils.isEmpty(dateOfBirth)) {
            dayofbirth_user.setError("Vui lòng chọn ngày sinh");
            return;
        }

        String gender = gender_user.getSelectedItem().toString().trim();
        if(gender.equals("gender_groups")) {
            Toast.makeText(mainActivity, "Vui lòng chọn ngày sinh", Toast.LENGTH_SHORT).show();
            return;
        }


        String[] arrayDate = dateOfBirth.split("/");
        Date date = new Date(Integer.parseInt(arrayDate[0]), Integer.parseInt(arrayDate[1]), Integer.parseInt(arrayDate[2]));


        User user = new User(firstName, lastName, phone,
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                resultUri.toString(), date,
                gender.equals("Nam"));

        sendUserFragment.send(user);


    }

    private void openDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(mainActivity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dayofbirth_user.setText(new Date(dayOfMonth, month, year).toString());
            }
        }, 2022, 1, 1);
        datePickerDialog.show();
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activityResultLauncher.launch(Intent.createChooser(intent, "Get Picture"));
    }
    private void onClickRequestPermission() {
        if(mainActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions, MY_REQUEST_CODE);
        }
    }
}