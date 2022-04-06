package com.example.bodybeyond.utilities;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
    public static boolean isValidPassword(String password) {
       // 8 characters at least 1 Capital Alphabet, 1 Number and 1 Special Character
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean emailValidator(String email) {
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            return false;
        }
    }
}
