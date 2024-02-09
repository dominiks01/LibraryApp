package agh.edu.pl.weedesign.library.helpers;

import agh.edu.pl.weedesign.library.LibraryApplication;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.category.Category;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.services.ModelService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Recommender {
    private Reader reader;
    private Set<Category> userCategoryChoice;
    private Set<Category> allCategories;
    private boolean showRecommendations = true;
    private boolean showPopularBooks = true;
    private ModelService modelService;

    public Recommender(ModelService modelService){
        this.reader = LibraryApplication.getReader();
        this.modelService  = modelService;
    }

    private static Set<Category> getMostPopularCategories(int count){
        return new HashSet<>();
    }
    public Set<Book> getRecommendedBooks(int count, Reader reader){
//      skalowanie ilości książek
        double dCount = (1.0/8.0)*count;
//        List<Book> readBooks = modelService.getRentedBooks(reader);
        Set<Book> recommendedBooks = new HashSet<>();
//        wybierz ceil((1/8) * count) najbardziej popularną książkę z najpopularniejszej kategorii
        recommendedBooks.addAll(modelService.getMostPopularNotReadBooksFromMostPopularCategories(reader, (int)Math.ceil(dCount)));
//        wybierz floor((1/8) * count) losową książkę z najpopularniejszej kategorii czytelnika
        recommendedBooks.addAll(modelService.getRandomNotReadBooksFromReaderNthMostPopularCategory(1, reader, (int)Math.floor(dCount)));
//        wybierz floor((1/8) * count) losową książkę z drugiej najpopularniejszej kategorii readera
        recommendedBooks.addAll(modelService.getRandomNotReadBooksFromReaderNthMostPopularCategory(2, reader, (int)Math.floor(dCount)));
//        wybierz losową kategorię i wybierz z niej ceil((1/8) * count)  najpopularniejszą książkę, której reader jeszcze nie przeczytał
        Category randomCategory = modelService.getRandomCategory();
        recommendedBooks.addAll(modelService.getRandomNotReadBooksFromCategory(reader, randomCategory, (int)Math.ceil(dCount)));
//        wybierz floor((3/8) * count) najlepiej oceniane książki z ulubionych kategorii readera, których jeszcze nie przeczytał
        recommendedBooks.addAll(this.topRatedNotReadBooksFromReaderCategories(reader, (int)Math.ceil(3*dCount)));

        return recommendedBooks;
    }

    private List<Book> topRatedNotReadBooksFromReaderCategories(Reader reader, int count) {
        Set<Category> categories = reader == null ? new HashSet<Category>() : reader.getLikedCategories();
        List<Book> books = new ArrayList<>();
//        System.out.println(categories);
//        for(Category category: categories){
//            books.addAll(modelService.getMostPopularNotReadBooksFromCategory(reader, category, 2));
//        }
//        Collections.shuffle(books);
        return books.subList(0, Math.min(count, books.size()));
    }

    public Set<Book> getMostPopularBooks(int count){
        return new HashSet<Book>(this.modelService.getMostPopularBooks(count));
    }
    public boolean showRecommendations(){
        return  this.showRecommendations;
    }
    public boolean showPopularBooks(){
        return this.showPopularBooks;
    }

    public void setShowPopularBooks(boolean value){
        this.showPopularBooks = value;
    }

    public void setShowRecommendations(boolean value){
        this.showRecommendations = value;
    }
}
