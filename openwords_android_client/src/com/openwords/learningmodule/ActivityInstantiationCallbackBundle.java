package com.openwords.learningmodule;

import android.content.Context;
import com.openwords.model.LeafCard;
import com.openwords.model.WordMeaning;
import com.openwords.services.GetWordMeanings;
import com.openwords.sound.WordAudioManager;
import com.openwords.util.log.LogUtil;
import java.util.List;

public class ActivityInstantiationCallbackBundle {

    private static boolean bundleIsSet = false;
    private static LearningModuleType type;
    private static boolean reverseNav;
    private static List<LeafCard> cardsPool;
    private static int currentCard = -1;

    public synchronized static void setBundle(LearningModuleType type, boolean reverseNav, List<LeafCard> cardsPool, int currentCard, boolean getAudio, Context context, WordAudioManager.AsyncCallback callback) {
        if (bundleIsSet) {
            LogUtil.logWarning(ActivityInstantiationCallbackBundle.class, "Bundle is overwritten");
        }
        ActivityInstantiationCallbackBundle.type = type;
        ActivityInstantiationCallbackBundle.reverseNav = reverseNav;
        ActivityInstantiationCallbackBundle.cardsPool = cardsPool;
        ActivityInstantiationCallbackBundle.currentCard = currentCard;
        getWordsMetadata(cardsPool, getAudio, context, callback);

        bundleIsSet = true;
        LogUtil.logDeubg(ActivityInstantiationCallbackBundle.class, "setBundle");
    }

    public synchronized static Object[] getBundle() throws Exception {
        if (!bundleIsSet) {
            throw new Exception("ActivityInstantiationCallbackBundle must be set first!");
        }
        Object[] bundle = new Object[]{type, reverseNav, cardsPool, currentCard};
        type = null;
        reverseNav = false;
        cardsPool = null;
        currentCard = -1;

        bundleIsSet = false;
        LogUtil.logDeubg(ActivityInstantiationCallbackBundle.class, "getBundle");
        return bundle;
    }

    private static void getWordsMetadata(List<LeafCard> CardsPool, boolean getAudio, Context context, WordAudioManager.AsyncCallback callback) {
        int[] ids = new int[CardsPool.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = CardsPool.get(i).getWordTwoId();
        }

        if (getAudio) {
            WordAudioManager.addAudioFiles(ids, context, callback);
        }

        GetWordMeanings.request(ids, 0, 0, new GetWordMeanings.AsyncCallback() {

            public void callback(WordMeaning[] meanings, Throwable error) {
                if (meanings != null) {
                    for (WordMeaning m : meanings) {//to-do, need to check duplicates
                        m.save();
                    }
                }
            }
        });
    }

    private ActivityInstantiationCallbackBundle() {
    }
}
