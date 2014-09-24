package com.openwords.util.localization;

import android.content.Context;
import android.content.res.Resources;
import com.openwords.R;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LocalizationManager {

    public static Object[][] LanguageNamesTypesIdsLocales = new Object[][]{
        new Object[]{"English", LocalLanguage.English, 1, Locale.ENGLISH.getDisplayLanguage()},
        new Object[]{"简体中文", LocalLanguage.Chinese, 11, Locale.CHINA.getDisplayLanguage()}
    };

    private static Context c;
    private static Map<LocalLanguage, Integer> textIndexMapping = new HashMap<LocalLanguage, Integer>(100);
    private static LocalLanguage lang;
    private static Resources res;
    private static String textLogin, textRegister, textRememberMe, textValidatingUser, textLoginError, textInternetError,
            textGo, textOptionReview, textOptionSelf, textOptionType, textOptionHearing, textExitContent,
            textYes, textNo, textLogoutContent, textLangOptionGreet, textLangOptionTitle, textAreYouSure, textLangOptionChange,
            textHitUser, textHitPass, textBye;

    public static void init(Context context) {
        c = context;
        textIndexMapping.clear();
        textIndexMapping.put(LocalLanguage.English, 0);
        textIndexMapping.put(LocalLanguage.Chinese, 1);
    }

    public static void setLocalLanguage(LocalLanguage localLanguage) {
        res = c.getResources();
        lang = localLanguage;
        textLogin = getText(R.array.pushMe_start_activity_button_loginPageGo);
        textRegister = getText(R.array.loginPage_Button_registerGo);
        textRememberMe = getText(R.array.loginPage_CheckBox_rememberMe);
        textValidatingUser = getText(R.array.loginPage_ProgressDialog_validating_user);
        textLoginError = getText(R.array.loginPage_Toast_login_error);
        textInternetError = getText(R.array.loginPage_Toast_login_error);
        textGo = getText(R.array.homePage_Button_testPageGo);
        textOptionReview = getText(R.array.homePage_Spinner_review);
        textOptionSelf = getText(R.array.homePage_Spinner_self);
        textOptionType = getText(R.array.homePage_Spinner_type);
        textOptionHearing = getText(R.array.homePage_Spinner_hear);
        textExitContent = getText(R.array.dialog_Exit_content);
        textYes = getText(R.array.confirm_yes);
        textNo = getText(R.array.confirm_no);
        textLogoutContent = getText(R.array.dialog_Logout_content);
        textLangOptionGreet = getText(R.array.lang_option_greet);
        textLangOptionTitle = getText(R.array.lang_option_title);
        textAreYouSure = getText(R.array.are_you_sure);
        textLangOptionChange = getText(R.array.lang_option_change);
        textHitUser = getText(R.array.loginPage_EditText_username);
        textHitPass = getText(R.array.loginPage_EditText_password);
        textBye = getText(R.array.bye);
    }

    private static String getText(int id) {
        return res.getStringArray(id)[textIndexMapping.get(lang)];
    }

    public static String getTextLogin() {
        return textLogin;
    }

    public static String getTextRegister() {
        return textRegister;
    }

    public static String getTextRememberMe() {
        return textRememberMe;
    }

    public static String getTextValidatingUser() {
        return textValidatingUser;
    }

    public static String getTextLoginError() {
        return textLoginError;
    }

    public static String getTextInternetError() {
        return textInternetError;
    }

    public static String getTextGo() {
        return textGo;
    }

    public static String getTextOptionReview() {
        return textOptionReview;
    }

    public static String getTextOptionSelf() {
        return textOptionSelf;
    }

    public static String getTextOptionType() {
        return textOptionType;
    }

    public static String getTextOptionHearing() {
        return textOptionHearing;
    }

    public static String getTextExitContent() {
        return textExitContent;
    }

    public static String getTextYes() {
        return textYes;
    }

    public static String getTextNo() {
        return textNo;
    }

    public static String getTextLogoutContent() {
        return textLogoutContent;
    }

    public static String getTextLangOptionGreet() {
        return textLangOptionGreet;
    }

    public static String getTextLangOptionTitle() {
        return textLangOptionTitle;
    }

    public static String getTextAreYouSure() {
        return textAreYouSure;
    }

    public static String getTextLangOptionChange() {
        return textLangOptionChange;
    }

    public static String getTextHitUser() {
        return textHitUser;
    }

    public static String getTextHitPass() {
        return textHitPass;
    }

    public static String getTextBye() {
        return textBye;
    }

    private LocalizationManager() {
    }

}
