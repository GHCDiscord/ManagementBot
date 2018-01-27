package de.ghc.managementbot.entity;

import de.ghc.managementbot.content.Strings;

import java.util.Objects;

public class Language {
    public static final Language GERMAN = new Language("de");
    public static final Language ENGLISH = new Language("en");

    private final String language;

    public Language(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Language && Objects.equals(((Language) obj).language, language);
    }

    public String getString(Strings.StringsList s) {
        return Strings.getString(this, s);
    }
}


