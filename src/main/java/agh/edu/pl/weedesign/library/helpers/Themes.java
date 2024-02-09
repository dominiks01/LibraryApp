package agh.edu.pl.weedesign.library.helpers;

public enum Themes {
    CUPERTINO_DARK, 
    CUPERTINO_LIGHT, 
    DRACULA, 
    NORD_DARK, 
    NORD_LIGHT, 
    PRIMER_DARK, 
    PRIMER_LIGHT;

    @Override 
    public String toString(){
        return switch(this){
            case CUPERTINO_DARK -> "Cupertino Dark";
            case CUPERTINO_LIGHT ->"Cupertino Light";
            case DRACULA ->"Dracula";
            case NORD_DARK ->"Nord Dark"; 
            case NORD_LIGHT ->"Nord Light"; 
            case PRIMER_DARK ->"Primer Dark"; 
            case PRIMER_LIGHT ->"Primer Light";
        };
    };

    public static String[] getAllThemes(){
        String[] themes = {
            "Cupertino Dark",
            "Cupertino Light",
            "Dracula",
            "Nord Dark", 
            "Nord Light", 
            "Primer Dark", 
            "Primer Light"
        };   

        return themes;
    }
    
}
