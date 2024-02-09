package agh.edu.pl.weedesign.library.helpers;

import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.review.Review;


public class DataProvider {
    private static Review selectedReview;
    private static Book selectedBook;

    public static Review getSelectedReview() {
        return selectedReview;
    }

    public static void setSelectedReview(Review selectedReview) {
        DataProvider.selectedReview = selectedReview;
    }

    public static Book getSelectedBook() {
        return selectedBook;
    }

    public static void setSelectedBook(Book selectedBook) {
        DataProvider.selectedBook = selectedBook;
    }
}
