package com.openwords.utils;

import com.openwords.interfaces.MyAction;

public class MyFieldValidation {

    public static void checkUsernameField(MyAction action, String username) {
        try {
            if (username.isEmpty()) {
                action.setErrorMessage("username is required and cannot be empty");
                validateFail(action);
            } else if (username.length() < 3) {
                action.setErrorMessage("username is too short(<3)");
                validateFail(action);
            }
        } catch (Exception e) {
            action.setErrorMessage(e.toString());
            validateFail(action);
            UtilLog.logWarn(MyFieldValidation.class, e.toString());
        }
    }

    public static void checkEmailField(MyAction action, String email) {
        try {
            if (email.isEmpty()) {
                action.setErrorMessage("email is required and cannot be empty");
                validateFail(action);
            } else if (!email.contains("@") || !email.contains(".")) {
                action.setErrorMessage("email format is not valid");
                validateFail(action);
            }
        } catch (Exception e) {
            action.setErrorMessage(e.toString());
            validateFail(action);
            UtilLog.logWarn(MyFieldValidation.class, e.toString());
        }
    }

    public static void checkPasswordField(MyAction action, String password) {
        try {
            if (password.isEmpty()) {
                action.setErrorMessage("password is required and cannot be empty");
                validateFail(action);
            } else if (password.length() < 6) {
                action.setErrorMessage("password is too short(<6)");
                validateFail(action);
            }
        } catch (Exception e) {
            action.setErrorMessage(e.toString());
            validateFail(action);
            UtilLog.logWarn(MyFieldValidation.class, e.toString());
        }
    }

    private static void validateFail(MyAction action) {
        action.addFieldError("error", "error");//not used
    }

    private MyFieldValidation() {
    }
}
