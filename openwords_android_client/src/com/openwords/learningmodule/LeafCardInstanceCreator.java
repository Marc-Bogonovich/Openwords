package com.openwords.learningmodule;

import com.google.gson.InstanceCreator;
import com.openwords.model.LeafCard;
import com.openwords.model.LeafCardHearing;
import com.openwords.model.LeafCardSelfEval;
import com.openwords.model.LeafCardTypeEval;
import java.lang.reflect.Type;

public class LeafCardInstanceCreator implements InstanceCreator<LeafCard> {

    private LearningModuleTypes lmType;

    public LeafCardInstanceCreator(LearningModuleTypes lmType) {
        this.lmType = lmType;
    }

    public LeafCard createInstance(Type type) {
        switch (lmType) {
            case LM_Review:
                return new LeafCard();
            case LM_SelfEvaluation:
                return new LeafCardSelfEval();
            case LM_TypeEvaluation:
                return new LeafCardTypeEval();
            case LM_HearingEvaluation:
                return new LeafCardHearing();
            default:
                return null;
        }
    }

}
