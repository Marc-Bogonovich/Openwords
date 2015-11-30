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
            textGo, textExitContent,
            textYes, textNo, textLogoutContent, textLangOptionGreet, textLangOptionTitle, textAreYouSure, textLangOptionChange,
            textHintUser, textHintPass, textBye, textSettingProfile, textSettingSetting, textSettingTutor, textSettingLogout,
            textPCNext, textPCEnd, textHintPassRe, textReg,
            TextHintEmail;
    private static String buttonPractice, buttonCreate, buttonResume, buttonSentence;
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
        textLogin = getText(R.array.button_login);
        textRegister = getText(R.array.button_register);
        textRememberMe = getText(R.array.check_remember);
        textValidatingUser = getText(R.array.block_validating_user);
        textLoginError = getText(R.array.error_login);
        textInternetError = getText(R.array.error_internet);
        textGo = getText(R.array.homePage_Button_testPageGo);
        textExitContent = getText(R.array.confirm_exit);
        textLogoutContent = getText(R.array.confirm_logout);
        textYes = getText(R.array.yes);
        textNo = getText(R.array.no);
        textLangOptionGreet = getText(R.array.lang_option_greet);
        textLangOptionTitle = getText(R.array.lang_option_title);
        textAreYouSure = getText(R.array.are_you_sure);
        textLangOptionChange = getText(R.array.lang_option_change);
        textHintUser = getText(R.array.info_username);
        textHintPass = getText(R.array.info_password);
        TextHintEmail = getText(R.array.info_email);
        textHintPassRe = getText(R.array.info_password_re);
        textReg = getText(R.array.info_reg_submit);
        textBye = getText(R.array.bye);
        textSettingProfile = getText(R.array.button_profile);
        textSettingSetting = getText(R.array.button_setting);
        textSettingTutor = getText(R.array.button_tutor);
        textSettingLogout = getText(R.array.button_logout);
        textPCNext = getText(R.array.button_pc_next);
        textPCEnd = getText(R.array.button_pc_end);
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
        buttonSentence = getText(R.array.button_sentence);
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

    public static String getButtonSentence() {
        return buttonSentence;
    }

    private LocalizationManager() {
    }

}
