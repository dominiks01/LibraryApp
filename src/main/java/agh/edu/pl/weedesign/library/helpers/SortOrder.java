package agh.edu.pl.weedesign.library.helpers;


import org.springframework.data.domain.Sort;

public enum SortOrder {
    ASCENDING,
    DESCENDING;

    @Override
    public String toString() {
        return switch (this){
            case ASCENDING -> "Od A do Z";
            case DESCENDING -> "Od Z do A";
        };
    }

    public Sort.Direction toSpringDirection() {
        return switch (this) {
            case ASCENDING -> Sort.Direction.ASC;
            case DESCENDING -> Sort.Direction.DESC;
        };
    }
}
