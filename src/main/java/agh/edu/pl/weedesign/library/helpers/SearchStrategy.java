package agh.edu.pl.weedesign.library.helpers;

public enum SearchStrategy {
    NAME,
    SURNAME,
    TITLE;

    @Override
    public String toString(){
        return switch (this){
            case NAME -> "Po imieniu autora";
            case SURNAME -> "Po nazwisku autora";
            case TITLE -> "Po tytule książki";
        };
    }
}
