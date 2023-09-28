package com.example.application_team3;

import android.view.View;
import android.widget.EditText;

public class UserAccountControl {
    // Definiera reguljärt uttryck för e-postadress
    private final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
    private final String PAASWORD_REGEX =
            "^(?=.*[a-zåäö])(?=.*[A-ZÅÄÖ])(?=.*[0-9]).{8,}$";

    private final String USERNAME_REGEX =
            "^[a-zA-Z0-9_-]{3,20}$";

    private final String NAME_REGEX =
            "^[a-zA-ZåäöÅÄÖ]{3,20}(\\s[a-zA-ZåäöÅÄÖ]{3,20})?$";

    private final String PIN_REGEX = "^[0-9]{4}$";

    private final String PHONENUMBER_REGEX = "^0[1-9][0-9]{7,9}$";


    public boolean isValidEmail(String email) {

        return email.matches(EMAIL_REGEX);
    }

    public boolean isValidPassword(String password) {

        return password != null && password.matches(PAASWORD_REGEX);
    }

    public boolean isValidPin(String pin){
        return pin != null && pin.matches(PIN_REGEX);
    }

    public boolean isValidUsername(String username){

        return username != null && username.matches(USERNAME_REGEX);
    }

    public boolean isValidName(String name){

        return name != null && name.matches(NAME_REGEX);
    }

    public boolean isValidPhoneNumber(String phone_num){
        return phone_num != null && phone_num.matches(PHONENUMBER_REGEX);
    }


}
