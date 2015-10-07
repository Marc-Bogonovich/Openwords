package com.openwords.util.localization;

import android.content.Context;
import android.content.res.Resources;
import com.openwords.R;
import com.openwords.model.LocalSettings;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LocalizationManager {

    public static Object[][] LanguageNamesTypesIdsLocales = new Object[][]{
        new Object[]{"English", LocalLanguage.English, 1, Locale.ENGLISH.getDisplayLanguage()},
        new Object[]{"简体中文", LocalLanguage.Chinese, 98, Locale.CHINA.getDisplayLanguage()}
    };

    private static Context c;
    private static final Map<LocalLanguage, Integer> textIndexMapping = new HashMap<LocalLanguage, Integer>(100);
    private static LocalLanguage lang;
    private static Resources res;
    private static String textLogin, textRegister, textRememberMe, textValidatingUser, textLoginError, textInternetError,
            textGo, textOptionReview, textOptionSelf, textOptionType, textOptionHearing, textExitContent,
            textYes, textNo, textLogoutContent, textLangOptionGreet, textLangOptionTitle, textAreYouSure, textLangOptionChange,
            textHintUser, textHintPass, textBye, textMoreLang, textSettingProfile, textSettingSetting, textSettingTutor, textSettingLogout,
            textPCNext, textPCEnd, textWordsNext, textWordsSearch, textWordsSets, textWordsView, textHintPassRe, textReg,
            TextHintEmail;
    private static String buttonPractice, buttonCreate, buttonResume;
    private static String titleWordSets, titlePractice;
    private static String hintSearchSets;
    private static String infoPractice;
    private static String nameReview, nameSelf, nameType, nameHearing, nameSentence;

    public static void init(Context context) {
        c = context;
        textIndexMapping.clear();
        textIndexMapping.put(LocalLanguage.English, 0);
        textIndexMapping.put(LocalLanguage.Chinese, 1);
    }

    public static LocalLanguage getCurrentLang() {
        return lang;
    }

    /**
     * This method procedure should be made dynamic later!
     *
     * @param localLanguage
     */
    private static void setBaseLanguage(LocalLanguage localLanguage) {
        if (localLanguage.equals(LocalLanguage.English)) {
            LocalSettings.setBaseLanguageId(1);
        } else if (localLanguage.equals(LocalLanguage.Chinese)) {
            LocalSettings.setBaseLanguageId(98);
        }
    }

    public static void setLocalLanguage(LocalLanguage localLanguage) {
        setBaseLanguage(localLanguage);
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
        TextHintEmail = getText(R.array.email);
        buttonPractice = getText(R.array.button_practice);
        buttonCreate = getText(R.array.button_create_set);
        buttonResume = getText(R.array.button_resume);
        titleWordSets = getText(R.array.title_word_sets);
        titlePractice = getText(R.array.title_practice);
        hintSearchSets = getText(R.array.hint_search_wordsets);
        infoPractice = getText(R.array.info_practice);
        nameReview = getText(R.array.name_review);
        nameSelf = getText(R.array.name_self);
        nameHearing = getText(R.array.name_hearing);
        nameType = getText(R.array.name_type);
        nameSentence = getText(R.array.name_sentence);
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

    public static String getTextHintEmail() {
        return TextHintEmail;
    }

    public static String getButtonPractice() {
        return buttonPractice;
    }

    public static String getButtonCreate() {
        return buttonCreate;
    }

    public static String getButtonResume() {
        return buttonResume;
    }

    public static String getTitleWordSets() {
        return titleWordSets;
    }

    public static String getTitlePractice() {
        return titlePractice;
    }

    public static String getHintSearchSets() {
        return hintSearchSets;
    }

    public static String getInfoPractice() {
        return infoPractice;
    }

    public static String getNameReview() {
        return nameReview;
    }

    public static String getNameSelf() {
        return nameSelf;
    }

    public static String getNameType() {
        return nameType;
    }

    public static String getNameHearing() {
        return nameHearing;
    }

    public static String getNameSentence() {
        return nameSentence;
    }

    private LocalizationManager() {
    }

}
