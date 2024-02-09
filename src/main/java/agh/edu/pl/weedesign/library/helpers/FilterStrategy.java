package agh.edu.pl.weedesign.library.helpers;

public enum FilterStrategy {
    CATEGORY,
    AUTHOR;

    public String toString(){
        return switch (this) {
            case AUTHOR -> "Filtruj po autorze";
            case CATEGORY -> "Filtruj po kategorii";
        };
    }
}
