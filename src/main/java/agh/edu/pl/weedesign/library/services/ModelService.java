package agh.edu.pl.weedesign.library.services;
import java.util.*;

import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.entities.reservation.Reservation;
import agh.edu.pl.weedesign.library.entities.reservation.ReservationRepository;
import agh.edu.pl.weedesign.library.helpers.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import agh.edu.pl.weedesign.library.entities.author.Author;
import agh.edu.pl.weedesign.library.entities.author.AuthorRepository;
import agh.edu.pl.weedesign.library.entities.book.Book;
import agh.edu.pl.weedesign.library.entities.book.BookRepository;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopyRepository;
import agh.edu.pl.weedesign.library.entities.category.Category;
import agh.edu.pl.weedesign.library.entities.category.CategoryRepository;
import agh.edu.pl.weedesign.library.entities.employee.Employee;
import agh.edu.pl.weedesign.library.entities.employee.EmployeeRepository;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.entities.reader.ReaderRepository;
import agh.edu.pl.weedesign.library.entities.rental.RentalRepository;
import agh.edu.pl.weedesign.library.entities.review.Review;
import agh.edu.pl.weedesign.library.entities.review.ReviewRepository;

@Service
public class ModelService {

    private int PAGE_SIZE = 1000;

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final CategoryRepository categoryRepository;
    private final EmployeeRepository employeeRepository;
    private final ReaderRepository readerRepository;
    private final RentalRepository rentalRepository;
    private final ReviewRepository reviewRepository;
    private final Random rand = new Random();
    private final ReservationRepository reservationRepository;

    public ModelService(AuthorRepository authorRepository, BookRepository bookRepository, BookCopyRepository bookCopyRepository, CategoryRepository categoryRepository, EmployeeRepository employeeRepository, ReaderRepository readerRepository, RentalRepository rentalRepository, ReviewRepository reviewRepository, ReservationRepository reservationRepository){
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.categoryRepository = categoryRepository;
        this.employeeRepository = employeeRepository;
        this.readerRepository = readerRepository;
        this.rentalRepository = rentalRepository;
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Book> getBooks(){
        return bookRepository.findAll();
    }

    public List<BookCopy> getBooksCopies(){
        return bookCopyRepository.findAll();
    }

    public List<BookCopy> getCopies(Book book){
        return bookCopyRepository.findBookCopiesByBook(book);
    }

    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }
    public List<Reader> getReaders(){
        return readerRepository.findAll();
    }

    public List<Employee> getEmployees(){
        return employeeRepository.findAll();
    }

    public void addNewEmployee(Employee e){
        employeeRepository.save(e);
    }

    public Employee getEmployeeByEmail(String email){
        return employeeRepository.findByEmail(email);
    }
    public List<Reservation> getReservations() {
        return this.reservationRepository.findAll();
    }

    public void addNewReservation(Reservation reservation){
        reservationRepository.save(reservation);
    }

    public Reservation getReservationByBookAndReader(Book book, Reader reader){
        return this.reservationRepository.findByBookAndReader(book,reader);
    }
    public List<Review> getReviews(){
        return reviewRepository.findAll();
    }

    public List<Rental> getRentals() {return rentalRepository.findAll();}

    public List<Rental> getRentalsByBook(Book book) {
        List<Rental> toReturn = new ArrayList<>();
        for(BookCopy bc : bookCopyRepository.findBookCopiesByBook(book))
//            toReturn.addAll(rentalRepository.getRentalsByBookCopy(bc));
        return toReturn;
        return null;
    }

    public List<Rental> getRentalsByCategory(Category category){
        List<Rental> toReturn = new ArrayList<>();
        for(Book book : bookRepository.findBookByCategory(category))
            toReturn.addAll(this.getRentalsByBook(book));
        return toReturn;
    }

    public List<Author> getAuthors() {return authorRepository.findAll();}

    public List<Rental> getRentalsByReaderEmail(String email){
        return rentalRepository.findRentalsByReader(this.getReaderByEmail(email));
    }

    public List<Rental> getRentalsByReader(Reader reader){
        return rentalRepository.findRentalsByReader(reader);
    }

    public List<Rental> getRentalsByBookCopy(BookCopy bookCopy){
        return rentalRepository.findRentalsByBookCopy(bookCopy);
    }

    public List<Rental> getRentalsWithoutAcceptance(){
        return rentalRepository.findRentalsWithoutAcceptance();
    }

    public Reader getReaderByEmail(String email){
        return readerRepository.findByEmail(email);
    }

    public String getReaderPasswordByEmail(String email){
        if (getReaderByEmail(email) == null){
            return null;
        }
        return getReaderByEmail(email).getPassword();
    }

    public Map<Book, Long> getAvailableBookCount(List<Book> books){
        Map<Book, Long> bookCount = new HashMap<>();
        for(Object[] obj: bookCopyRepository.findBooksWithCopyCount(books)) {
            bookCount.put((Book) obj[0],(Long) obj[1]);
        }
        return bookCount;
    }

    public String getEmployeePasswordByEmail(String email) {
        if (getEmployeeByEmail(email) == null){
            return null;
        }
        return getEmployeeByEmail(email).getPassword();
    }
    public List<Book> getMostPopularBooks(int n){
        List<Book> books = bookRepository.findBooksOrderedByRentalCountDesc();
        return books.stream().limit(10L *n).toList();
    }
    public List<Book> getRentedBooks(Reader r){
        return this.bookRepository.findBooksRentedByReader(r);
    }

    public List<Book> getMostPopularNotReadBooksFromMostPopularCategories(Reader reader, int count) {
        Category  c = this.getMostPopularCategory();
        System.out.println("Category: "+c.getName());
//        List<Book> books  = this.bookRepository.findNotReadBooksFromCategoryOrderedByRentalCountDesc(reader, c).stream().limit(count).toList();
        List<Book> books  = this.bookRepository.findNotReadBooksFromCategoryOrderedByRentalCountDesc(reader, c);
        System.out.println("MostPopularNotReadBooksFromMostPopularCategories, count: " + count + " actual size: " + books.size());
        return books;
    }

    public Category getMostPopularCategory() {
        List<Category> l = categoryRepository.findCategoriesSortedByPopularity();
        if (l.isEmpty()){
            return getRandomCategory();
        }
        return l.stream().limit(1).toList().get(0);
    }

    public List<Book> getRandomNotReadBooksFromReaderNthMostPopularCategory(int n, Reader r, int count) {
        Category readerCategory = this.getReaderNthMostPopularCategory(n, r);
        List<Book> books = this.bookRepository.findNotReadBooksFromCategory(r, readerCategory);
        Collections.shuffle(books);
        System.out.println("RandomNotReadBooksFromReaderNthMostPopularCategory, n = " + n + "count = " + count + " actual size = " + books.size());
        return books.subList(0, Math.min(count, books.size()));
    }

    public Category getReaderNthMostPopularCategory(int n, Reader r) {
        List<Category> categories = this.categoryRepository.findCategoryWithMostRentals(r);
        if( categories.size() <= n){
            return getRandomCategory();
        }
        return categories.get(n);
    }

    public Category getRandomCategory() {
        List<Category> categories = this.categoryRepository.findAll();
        Collections.shuffle(categories);
        return categories.get(0);
    }

    public List<Book> getRandomNotReadBooksFromCategory(Reader r, Category c, int count) {
        List<Book> books = this.bookRepository.findNotReadBooksFromCategory(r, c);
        Collections.shuffle(books);
        System.out.println("RandomNotReadBooksFromCategory, count = " + count + " acutal size = " + books.size());
        return books.subList(0, Math.min(count, books.size()));
    }

    public List<Book> getMostPopularBooksFromCategory(Category category, int i) {
        return bookRepository.findBooksFromCategoryOrderedByRentalCountDesc(category).stream().limit(i).toList();
    }

    public List<Book> getMostPopularNotReadBooksFromCategory(Reader reader, Category category, int i) {
        return bookRepository.findNotReadBooksFromCategoryOrderedByRentalCountDesc(reader, category).stream().limit(i).toList();
    }

    public Double getAverageRating(Book book) {
        return reviewRepository.findAverageRating(book);
    }

    public Page<Book> getBooksFilteredSorted(int page, String categoryName, SortOrder sortDirection){
        Sort sort;
        if (sortDirection == null) {
            sort = Sort.unsorted();
        } else {
            sort = Sort.by(sortDirection.toSpringDirection(), "title");
        }

        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);
        return this.bookRepository.findBooksByCategoryAndSort(categoryName, pageable);
    }

    public List<Reservation> getReservationsByBook(Book book){
        return this.reservationRepository.getReservationByBook(book);
    }

    public void deleteAllReservationsByBook(List<Reservation> reservations){
        this.reservationRepository.deleteAll(reservations);
    }
}
