package agh.edu.pl.weedesign.library.helpers;

public enum SearchStrategy {
    CATEGORY,
    AUTHOR;

    public String toString(){
        return switch (this) {
            case AUTHOR -> "Filtruj po autorze";
            case CATEGORY -> "Filtruj po kategorii";
        };
    }
}
