package atechnique.views.interfaces;

import java.util.List;

public interface IGameStateView {
    List<String> getTranslationTags();

    void setTranslationPhrases(List<String> translationPhrases);

    void activate();

    void deactivate();
}
