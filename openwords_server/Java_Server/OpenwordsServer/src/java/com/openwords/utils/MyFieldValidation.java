package com.openwords.utils;

import com.openwords.actions.accounts.BlackList;
import com.openwords.interfaces.MyAction;
import javax.servlet.http.HttpServletRequest;

public class MyFieldValidation {

    public static void checkBlackList(MyAction action, HttpServletRequest request) {
        String clientId = request.getRemoteAddr();
        try {
            if (!BlackList.requestLegal(clientId)) {
                throw new Exception("Sorry, our current server cannot take such high volume requests (less than 5 seconds frequency) from your IP, please try another time.");
            }
        } catch (Exception e) {
            action.setErrorMessage(e.getMessage());
            validateFail(action);
            UtilLog.logWarn(MyFieldValidation.class, e.toString());
        }
    }

    public static void checkUsernameField(MyAction action, String username) {
        try {
            if (username.isEmpty()) {
                throw new Exception("username is required and cannot be empty");
            } else if (username.length() < 3) {
                throw new Exception("username is too short(<3)");
            }
        } catch (Exception e) {
            action.setErrorMessage(e.getMessage());
            validateFail(action);
            UtilLog.logWarn(MyFieldValidation.class, e.toString());
        }
    }

    public static void checkEmailField(MyAction action, String email) {
        try {
            if (email.isEmpty()) {
                throw new Exception("email is required and cannot be empty");
            } else if (!email.contains("@") || !email.contains(".")) {
                throw new Exception("email format is not valid");
            }
        } catch (Exception e) {
            action.setErrorMessage(e.getMessage());
            validateFail(action);
            UtilLog.logWarn(MyFieldValidation.class, e.toString());
        }
    }

    public static void checkPasswordField(MyAction action, String password) {
        try {
            if (password.isEmpty()) {
                throw new Exception("password is required and cannot be empty");
            } else if (password.length() < 6) {
                throw new Exception("password is too short(<6)");
            }
        } catch (Exception e) {
            action.setErrorMessage(e.getMessage());
            validateFail(action);
            UtilLog.logWarn(MyFieldValidation.class, e.toString());
        }
    }

    public static void checkPageSize(MyAction action, int size, int max, int min) {
        try {
            if (size > max) {
                throw new Exception("pageSize is too large");
            } else if (size < min) {
                throw new Exception("pageSize is too small");
            }
        } catch (Exception e) {
            action.setErrorMessage(e.getMessage());
            validateFail(action);
            UtilLog.logWarn(MyFieldValidation.class, e.toString());
        }
    }

    public static void checkBooleanAndString(MyAction action, String name, boolean doOption, String option) {
        try {
            if (doOption) {
                if (option == null || option.isEmpty()) {
                    throw new Exception("no " + name + " option value is given");
                }
            }
        } catch (Exception e) {
            action.setErrorMessage(e.getMessage());
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
