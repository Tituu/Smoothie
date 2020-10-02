package com.tituuuu.smoothie;

import android.graphics.Color;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

public class Validator {

    public static boolean Validate(TextView emailView, TextView passwordView, TextView rePasswordView, TextView phoneNumberView, ConstraintLayout layout) {
        if (validateEmail(emailView) && validatePhoneNumber(phoneNumberView) && validatePassword(passwordView, rePasswordView, layout))
            return true;
        return false;
    }

    private static boolean validateEmail(TextView emailView) {
        String email = emailView.getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        }
        emailView.setText("");
        emailView.setHint("Incorrect e-mail");
        emailView.setHintTextColor(Color.RED);
        return false;
    }

    private static boolean validatePassword(TextView passwordView, TextView rePasswordView, ConstraintLayout layout) {

        String[] messages = new String[3];
        messages[0] = "Given passwords doesn't match!";
        messages[1] = "Password is too short! \nFor more information check info button";
        messages[2] = "You need to have at least one digit in your password!\nFor more information check info button";

        String password = passwordView.getText().toString();
        String rePassword = rePasswordView.getText().toString();

        if (!password.equals(rePassword)) {
            createPasswordErrorSnackbar(messages[0], layout, passwordView);
            return false;
        } else if (password.length() < 8) {
            createPasswordErrorSnackbar(messages[1], layout, passwordView);
            return false;
        } else if (password.contains("![0-9]+")) {
            createPasswordErrorSnackbar(messages[2], layout, passwordView);
            return false;
        } else return true;
    }

    private static boolean validatePhoneNumber(TextView phoneNumberView) {
        if (PhoneNumberUtils.normalizeNumber(phoneNumberView.getText().toString()).length() != 9) {
            phoneNumberView.setText("");
            phoneNumberView.setHint("Incorrect phone number");
            phoneNumberView.setHintTextColor(Color.RED);
            return false;
        }
        return true;
    }

    private static void createPasswordErrorSnackbar(String message, ConstraintLayout layout, final TextView passwordView) {
        Snackbar.make(layout, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        passwordView.setSelected(true);
                    }
                }).show();
    }
}
