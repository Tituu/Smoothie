package com.tituuuu.smoothie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.abdularis.civ.CircleImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainLoginActivity extends AppCompatActivity implements View.OnClickListener {

    //region properties
    private CircleImageView logo;
    private EditText emailTextView;
    private EditText passwordTextView;
    private Button loginButton;
    private TextView registerButton;
    private FirebaseAuth fAuth;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instantiateViews();

        appStartLogoAnimation();

        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                loggingInLogoAnimation();
                String email = emailTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                try {
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent mainMapIntent = new Intent(MainLoginActivity.this, MainMapsActivity.class);
                                startActivity(mainMapIntent);
                            } else {
                                logo.setAnimation(null);
                                Snackbar.make(findViewById(R.id.mainActivityView),
                                        task.getException().getMessage(),
                                        Snackbar.LENGTH_LONG).show();

                            }
                        }
                    });
                } catch (IllegalArgumentException e) {
                    if (password.equals(""))
                        passwordTextView.setError("Password field can not be empty!");
                    if (email.equals(""))
                        emailTextView.setError("Email field can not be empty!");
                }

                break;
            case R.id.textRegisterButton:
                Intent registrationIntent = new Intent(MainLoginActivity.this, RegistrationActivity.class);
                startActivity(registrationIntent);
                break;
            default:
                break;
        }
    }

    private void instantiateViews() {
        logo = findViewById(R.id.circleImageViewLogo);
        emailTextView = findViewById(R.id.editTextTextEmailAddress);
        passwordTextView = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        registerButton = findViewById(R.id.textRegisterButton);
        fAuth = FirebaseAuth.getInstance();
    }

    private void appStartLogoAnimation() {
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new android.view.animation.LinearInterpolator());
        anim.setRepeatCount(1);
        anim.setDuration(250);
        logo.startAnimation(anim);
    }

    private void loggingInLogoAnimation() {
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new android.view.animation.LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);
        logo.startAnimation(anim);
    }
}