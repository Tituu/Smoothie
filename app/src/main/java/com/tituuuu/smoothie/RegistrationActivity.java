package com.tituuuu.smoothie;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.abdularis.civ.CircleImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tituuuu.smoothie.models.User;

import java.util.Calendar;
import java.util.Locale;


public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    //region Properties
    private EditText emailView;
    private EditText nameView;
    private EditText phoneNumberView;
    private EditText birthDateView;
    private EditText passwordView;
    private EditText rePasswordView;
    private RadioGroup sexRadioGroup;
    private Button registerButton;
    private FirebaseAuth fAuth;
    private DatabaseReference databaseToUsersReference;
    private CircleImageView logo;
    private androidx.constraintlayout.widget.ConstraintLayout layout;
    //endregion

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editTextDate:
                Calendar calendar = Calendar.getInstance();

                DatePickerDialog dialog = new DatePickerDialog(
                        RegistrationActivity.this,
                        AlertDialog.THEME_HOLO_DARK,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month++;
                                birthDateView.setText(year + "/" + month + "/" + day);
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;

            case R.id.buttonRegister:
                if (Validator.Validate(emailView, passwordView, rePasswordView, phoneNumberView, layout)) {
                    startLogoAnimation();

                    fAuth.createUserWithEmailAndPassword(emailView.getText().toString(), passwordView.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User newUser = new User();
                                createUserFromEnteredData(newUser);
                                databaseToUsersReference.child(fAuth.getCurrentUser().getUid()).setValue(newUser);

                                logo.setAnimation(null);

                                Snackbar.make(findViewById(R.id.registrationView),
                                        "Congratulations, you signed up successfully! \n" +
                                                "Now you can sign in",
                                        Snackbar.LENGTH_INDEFINITE).setAction("OK!", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                }).show();
                            } else {
                                logo.setAnimation(null);
                                Snackbar.make(findViewById(R.id.registrationView),
                                        "Error! \n" + task.getException().getMessage(),
                                        Snackbar.LENGTH_LONG).setAction("OK!", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).show();
                            }
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        instantiateViews();
        birthDateView.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private void instantiateViews() {
        logo = findViewById(R.id.circleImageViewLogo);
        emailView = findViewById(R.id.editTextTextEmailAddress);
        nameView = findViewById(R.id.editTextTextPersonName);
        phoneNumberView = findViewById(R.id.editTextPhone);
        birthDateView = findViewById(R.id.editTextDate);
        passwordView = findViewById(R.id.editTextPassword);
        rePasswordView = findViewById(R.id.editTextTextPassword);
        registerButton = findViewById(R.id.buttonRegister);
        sexRadioGroup = findViewById(R.id.sexRg);
        fAuth = FirebaseAuth.getInstance();
        databaseToUsersReference = FirebaseDatabase.getInstance().getReference("Users");
        layout = findViewById(R.id.registrationView);
    }

    private void createUserFromEnteredData(User newUser) {
        newUser.setName(nameView.getText().toString().trim());
        newUser.setEmail(emailView.getText().toString());
        newUser.setPassword(passwordView.getText().toString());
        RadioButton sexRadioButton = findViewById(sexRadioGroup.getCheckedRadioButtonId());
        newUser.setSex(sexRadioButton.getText().toString());
        newUser.setPhoneNumber(PhoneNumberUtils.formatNumber(PhoneNumberUtils.normalizeNumber(phoneNumberView.getText().toString()), Locale.getDefault().getCountry()));
        newUser.setBirthDate(birthDateView.getText().toString());
    }

    private void startLogoAnimation() {
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new android.view.animation.LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);
        logo.startAnimation(anim);
    }
}