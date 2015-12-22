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
        new Object[]{"English", LocalLanguage.English, 1, Locale.ENGLISH.getDisplayLanguage()},//index 0
        new Object[]{"简体中文", LocalLanguage.Chinese, 98, Locale.CHINA.getDisplayLanguage()}//index 1
    };

    private static Context c;
    private static final Map<LocalLanguage, Integer> textIndexMapping = new HashMap<LocalLanguage, Integer>(100);
    private static LocalLanguage lang;
    private static Resources res;
    private static String textLogin, textRegister, textRememberMe, textInternetError,
            textExitContent,
            textYes, textNo, textLogoutContent, textLangOptionGreet, textLangOptionTitle, textAreYouSure, textLangOptionChange,
            textHintUser, textHintPass, textBye, textSettingProfile, textSettingSetting, textSettingTutor, textSettingLogout,
            textPCNext, textPCEnd, textHintPassRe, textReg,
            TextHintEmail;
    private static String buttonPractice, buttonCreate, buttonResume, buttonSentence,
            buttonSearchWord, buttonManageSets, buttonStudy, buttonOverview;
    private static String titleWordSets, titlePractice;
    private static String hintSearchSets, hintSetName;
    private static String infoPractice, infoOffline;
    private static String nameReview, nameSelf, nameType, nameHearing, nameSentence;
    private static String confirmNativeTitle, confirmNativeContent, confirmSetTitle,
            confirmSetContent, confirmSetNotChangeTitle, confirmSetNotChangeContent,
            confirmLearnLang, confirmLearnLangTitle, confirmCopySetTitle,
            confirmCopySetContent;
    private static String ok, cancel;
    private static String blockValidatingUser, blockConnectServer, blockRefreshLang, blockSearching;
    private static String error, errorEmailOk, errorUsernameOk, errorPassword,
            errorOneLang, errorLoginFail, erroModuleSupport, errorEditOffline,
            errorSetNameEmpty, errorSetMinItems, errorDone, errorWordExist,
            errorEmpty, errorCopy, errorCopySuccess;

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
        blockValidatingUser = getText(R.array.block_validating_user);
        textInternetError = getText(R.array.error_internet);
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
        confirmNativeTitle = getText(R.array.confirm_native_title);
        confirmNativeContent = getText(R.array.confirm_native_content);
        ok = getText(R.array.ok);
        cancel = getText(R.array.cancel);
        blockConnectServer = getText(R.array.block_connect_server);
        blockRefreshLang = getText(R.array.block_refresh_lang);
        error = getText(R.array.error);
        errorEmailOk = getText(R.array.error_email_valid);
        errorPassword = getText(R.array.error_password_invalid);
        errorUsernameOk = getText(R.array.error_username_valid);
        errorOneLang = getText(R.array.error_one_lang);
        buttonSearchWord = getText(R.array.button_search_word);
        errorLoginFail = getText(R.array.error_login_fail);
        infoOffline = getText(R.array.info_offline);
        erroModuleSupport = getText(R.array.error_module_not_support);
        hintSetName = getText(R.array.hint_set_name);
        errorEditOffline = getText(R.array.error_edit_offline);
        errorSetNameEmpty = getText(R.array.error_set_name_empty);
        errorSetMinItems = getText(R.array.error_set_min_items);
        confirmSetTitle = getText(R.array.confirm_set_title);
        confirmSetContent = getText(R.array.confirm_set_content);
        errorDone = getText(R.array.error_done);
        blockSearching = getText(R.array.block_searching);
        errorWordExist = getText(R.array.error_word_exist);
        confirmSetNotChangeTitle = getText(R.array.confirm_set_not_change_title);
        confirmSetNotChangeContent = getText(R.array.confirm_set_not_change_content);
        confirmLearnLang = getText(R.array.confirm_learn_lang);
        confirmLearnLangTitle = getText(R.array.confirm_learn_lang_title);
        buttonManageSets = getText(R.array.button_manage_set);
        buttonStudy = getText(R.array.button_study);
        buttonOverview = getText(R.array.button_overview);
        confirmCopySetTitle = getText(R.array.confirm_copy_set_title);
        confirmCopySetContent = getText(R.array.confirm_copy_set_content);
        errorEmpty = getText(R.array.error_empty);
        errorCopy = getText(R.array.error_copy);
        errorCopySuccess = getText(R.array.error_copy_success);
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

    public static String getBlockValidatingUser() {
        return blockValidatingUser;
    }

    public static String getTextInternetError() {
        return textInternetError;
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

    public static String getConfirmNativeTitle() {
        return confirmNativeTitle;
    }

    public static String getConfirmNativeContent() {
        return confirmNativeContent;
    }

    public static String getOk() {
        return ok;
    }

    public static String getCancel() {
        return cancel;
    }

    public static String getBlockConnectServer() {
        return blockConnectServer;
    }

    public static String getError() {
        return error;
    }

    public static String getErrorEmailOk() {
        return errorEmailOk;
    }

    public static String getErrorUsernameOk() {
        return errorUsernameOk;
    }

    public static String getErrorPassword() {
        return errorPassword;
    }

    public static String getBlockRefreshLang() {
        return blockRefreshLang;
    }

    public static String getErrorOneLang() {
        return errorOneLang;
    }

    public static String getButtonSearchWord() {
        return buttonSearchWord;
    }

    public static String getErrorLoginFail() {
        return errorLoginFail;
    }

    public static String getInfoOffline() {
        return infoOffline;
    }

    public static String getErroModuleSupport() {
        return erroModuleSupport;
    }

    public static String getHintSetName() {
        return hintSetName;
    }

    public static String getConfirmSetTitle() {
        return confirmSetTitle;
    }

    public static String getConfirmSetContent() {
        return confirmSetContent;
    }

    public static String getConfirmSetNotChangeTitle() {
        return confirmSetNotChangeTitle;
    }

    public static String getConfirmSetNotChangeContent() {
        return confirmSetNotChangeContent;
    }

    public static String getBlockSearching() {
        return blockSearching;
    }

    public static String getErrorEditOffline() {
        return errorEditOffline;
    }

    public static String getErrorSetNameEmpty() {
        return errorSetNameEmpty;
    }

    public static String getErrorSetMinItems() {
        return errorSetMinItems;
    }

    public static String getErrorDone() {
        return errorDone;
    }

    public static String getErrorWordExist() {
        return errorWordExist;
    }

    public static String getConfirmLearnLang() {
        return confirmLearnLang;
    }

    public static String getConfirmLearnLangTitle() {
        return confirmLearnLangTitle;
    }

    public static String getButtonManageSets() {
        return buttonManageSets;
    }

    public static String getButtonStudy() {
        return buttonStudy;
    }

    public static String getButtonOverview() {
        return buttonOverview;
    }

    public static String getConfirmCopySetTitle() {
        return confirmCopySetTitle;
    }

    public static String getConfirmCopySetContent() {
        return confirmCopySetContent;
    }

    public static String getErrorEmpty() {
        return errorEmpty;
    }

    public static String getErrorCopy() {
        return errorCopy;
    }

    public static String getErrorCopySuccess() {
        return errorCopySuccess;
    }

    private LocalizationManager() {
    }

}
