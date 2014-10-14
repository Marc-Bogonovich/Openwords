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
            textHintUser, textHintPass, textBye, textMoreLang, textSettingProfile, textSettingSetting, textSettingTutor, textSettingLogout,
            textPCNext, textPCEnd, textWordsNext, textWordsSearch, textWordsSets, textWordsView, textHintPassRe, textReg;

    public static void init(Context context) {
        c = context;
        textIndexMapping.clear();
        textIndexMapping.put(LocalLanguage.English, 0);
        textIndexMapping.put(LocalLanguage.Chinese, 1);
    }

    public static LocalLanguage getCurrentLang() {
        return lang;
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
        textHintUser = getText(R.array.username);
        textHintPass = getText(R.array.password);
        textHintPassRe = getText(R.array.password_re);
        textBye = getText(R.array.bye);
        textMoreLang = getText(R.array.add_more_lang);
        textSettingProfile = getText(R.array.setting_profile);
        textSettingSetting = getText(R.array.setting_setting);
        textSettingTutor = getText(R.array.setting_tutor);
        textSettingLogout = getText(R.array.setting_logout);
        textPCNext = getText(R.array.pc_next);
        textPCEnd = getText(R.array.pc_end);
        textWordsNext = getText(R.array.words_next);
        textWordsSearch = getText(R.array.words_search);
        textWordsSets = getText(R.array.words_sets);
        textWordsView = getText(R.array.words_view);
        textReg = getText(R.array.reg_submit);
    }

    private static String getText(int id) {
        return res.getStringArray(id)[textIndexMapping.get(lang)];
    }

    public static String getTextHintPassRe() {
        return textHintPassRe;
    }

    public static String getTextReg() {
        return textReg;
    }

    public static String getTextWordsNext() {
        return textWordsNext;
    }

    public static String getTextWordsSearch() {
        return textWordsSearch;
    }

    public static String getTextWordsSets() {
        return textWordsSets;
    }

    public static String getTextWordsView() {
        return textWordsView;
    }

    public static String getTextPCNext() {
        return textPCNext;
    }

    public static String getTextPCEnd() {
        return textPCEnd;
    }

    public static String getTextSettingProfile() {
        return textSettingProfile;
    }

    public static String getTextSettingSetting() {
        return textSettingSetting;
    }

    public static String getTextSettingTutor() {
        return textSettingTutor;
    }

    public static String getTextSettingLogout() {
        return textSettingLogout;
    }

    public static String getTextMoreLang() {
        return textMoreLang;
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

    public static String getTextHintUser() {
        return textHintUser;
    }

    public static String getTextHintPass() {
        return textHintPass;
    }

    public static String getTextBye() {
        return textBye;
    }

    private LocalizationManager() {
    }

}
