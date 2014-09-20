package com.openwords.learningmodule;

import android.content.Context;
import com.openwords.model.LeafCard;
import com.openwords.sound.WordAudioManager;
import com.openwords.util.log.LogUtil;
import java.util.List;

public class ActivityInstantiationCallbackBundle {

    private static boolean bundleIsSet = false;
    private static LearningModuleType type;
    private static int layoutId, pagerId;
    private static boolean reverseNav;
    private static List<LeafCard> cardsPool;
    private static int currentCard = -1;

    public synchronized static void setBundle(LearningModuleType type, int layoutId, int pagerId, boolean reverseNav, List<LeafCard> cardsPool, int currentCard, boolean getAudio, Context context) {
        if (bundleIsSet) {
            LogUtil.logWarning(ActivityInstantiationCallbackBundle.class, "Bundle is overwritten");
        }
        ActivityInstantiationCallbackBundle.type = type;
        ActivityInstantiationCallbackBundle.layoutId = layoutId;
        ActivityInstantiationCallbackBundle.pagerId = pagerId;
        ActivityInstantiationCallbackBundle.reverseNav = reverseNav;
        ActivityInstantiationCallbackBundle.cardsPool = cardsPool;
        ActivityInstantiationCallbackBundle.currentCard = currentCard;
        getAudiosForCardsPool(cardsPool, getAudio, context);

        bundleIsSet = true;
        LogUtil.logDeubg(ActivityInstantiationCallbackBundle.class, "setBundle");
    }

    public synchronized static Object[] getBundle() throws Exception {
        if (!bundleIsSet) {
            throw new Exception("ActivityInstantiationCallbackBundle must be set first!");
        }
        Object[] bundle = new Object[]{type, layoutId, pagerId, reverseNav, cardsPool, currentCard};
        type = null;
        layoutId = 0;
        pagerId = 0;
        reverseNav = false;
        cardsPool = null;
        currentCard = -1;

        bundleIsSet = false;
        LogUtil.logDeubg(ActivityInstantiationCallbackBundle.class, "getBundle");
        return bundle;
    }

    private static void getAudiosForCardsPool(List<LeafCard> CardsPool, boolean getAudio, Context context) {
        if (getAudio) {
            int[] ids = new int[CardsPool.size()];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = CardsPool.get(i).getWordTwoId();
            }
            WordAudioManager.addAudioFiles(ids, context);
        }
    }

    private ActivityInstantiationCallbackBundle() {
    }
}
