package atechnique.classfactory;

import atechnique.views.interfaces.ITranslator;

import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.List;

public class Translator implements ITranslator {
    private Locale _currentLocale;
    private ResourceBundle _messages;

    public Translator(String language, String country) {
        _currentLocale = new Locale(language, country);
        _messages = ResourceBundle.getBundle("atechnique/data/languages/MessagesBundle", _currentLocale);
    }

    public List<String> getTranslatedPhrases(List<String> translationTags) {
        ArrayList<String> translatedPhrases = new ArrayList<String>();

        if (translationTags != null) {
            for (String translationTag : translationTags) {
                try {
                    translatedPhrases.add(_messages.getString(translationTag));
                } catch (MissingResourceException e) {
                    // Just add the tag surrounded by <>
                    translatedPhrases.add("<" + translationTag + ">");
                }
            }
        }

        return translatedPhrases;
    }
}