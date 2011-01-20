package atechnique.views.interfaces;

import java.util.List;

public interface ITranslator {
    List<String> getTranslatedPhrases(List<String> translationTags);
}
