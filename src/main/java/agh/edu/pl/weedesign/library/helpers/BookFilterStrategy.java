package agh.edu.pl.weedesign.library.helpers;

import agh.edu.pl.weedesign.library.entities.category.Category;
import javafx.util.Pair;

public class BookFilterStrategy {
    private String searchString;
    private Integer minRating, maxRating;
    private Boolean availableOnly = false;
    private Category selectedCategory;

    public void setSearchString(String s){
        this.searchString = s;
    }

    public void setCategory(Category c){
        this.selectedCategory = c;
    }

    public Category getCategory(){
        return this.selectedCategory;
    }

    public String getSearchString(){
        return searchString;
    }

    public void setRating(Integer minRating, Integer maxRating){
        this.minRating = minRating;
        this.maxRating = maxRating;
    }

    public Pair<Integer, Integer> getRating(){
        return new Pair<>(this.minRating, this.maxRating);
    }

    public void setAvailable(Boolean bool){
        this.availableOnly = availableOnly != bool;
    }

    public boolean getAvailable(){
        return this.availableOnly;
    }

}
