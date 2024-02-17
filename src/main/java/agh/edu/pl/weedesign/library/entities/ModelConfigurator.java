package agh.edu.pl.weedesign.library.entities;

import agh.edu.pl.weedesign.library.entities.employee.AccessLevel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

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
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.entities.rental.RentalRepository;
import agh.edu.pl.weedesign.library.entities.review.Review;
import agh.edu.pl.weedesign.library.entities.review.ReviewRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.random.*;


@Configuration
public class ModelConfigurator {
    @Bean
    CommandLineRunner commandLineRunner(AuthorRepository authorRepository, BookRepository bookRepository, BookCopyRepository bookCopyRepository, CategoryRepository categoryRepository, EmployeeRepository employeeRepository, ReaderRepository readerRepository, RentalRepository rentalRepository, ReviewRepository reviewRepository) {
        return args -> {
            if (bookRepository.findAll().isEmpty()) {
                System.out.println("TEST");

                Map<Object, Object> bookPrice = Stream.of(
                    new AbstractMap.SimpleEntry<>("very good", 25),
                    new AbstractMap.SimpleEntry<>("bad", 15),
                    new AbstractMap.SimpleEntry<>("very bad", 10),
                    new AbstractMap.SimpleEntry<>("good", 20))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                
                CSVParser parser = new CSVParserBuilder()
                    .withSeparator(',')
                    .build();

                CSVReader reader = new CSVReaderBuilder(new BufferedReader(new FileReader("./src/main/resources/books/books.csv")))
                    .withCSVParser(parser)
                    .build();

                List<String[]> records = new ArrayList<>();
                records = reader.readAll();

                
                for(String[] line: records){

                        // Create new book 
                        Book book1 = new Book(
                                line[2].replaceAll("[-+^]*", ""),
                                line[7].substring(0, Math.min(4000, line[7].length())).replaceAll("[-+^]*", ""),
                                Integer.parseInt(line[10]),
                                "", 
                                line[6].replaceAll("[-+^]*", "")
                        );

                        // Set Authors
                        String[] authors = line[4].split(";");

                        for(String author : authors){
                            int i = author.lastIndexOf(" ");
                            String[] authorDetails = new String[2];

                            if(i == -1){
                                authorDetails[0] = author;
                                authorDetails[1] = author;
                            } else {
                                authorDetails[0] = author.substring(0, i).replaceAll("[-+^]*", "");
                                authorDetails[1] = author.substring(i).replaceAll("[-+^]*", "");
                            }

                            List<Author> authorList = authorRepository.findByNameAndSurname(authorDetails[0], authorDetails[1]);
                            Author authorValue;

                            if(authorList.isEmpty()){
                                authorValue = new Author(
                                    authorDetails[0],
                                    authorDetails[1], 
                                    ""
                                );

                                authorRepository.save(authorValue);
                            } else {
                                authorValue = authorList.get(0);
                            }
                            
                            book1.setAuthor(authorValue);
                        }

                        // Categories
                        String[] categories = line[5].split(", ");
                        for(String category : categories){
                            Category categoryEntity = categoryRepository.findByName(category);

                            if(categoryEntity == null){
                                categoryEntity = new Category(
                                    category.replaceAll("[-+^]*", "")
                                );

                                categoryRepository.save(categoryEntity);
                            }

                            book1.setCategory(categoryEntity);
                        }

                        bookRepository.save(book1);

                        // Creating Book Copies
                        Random rand = new Random();

                        for(int i = 0; i < rand.nextInt(7); i++){

                            int randValue = rand.nextInt(bookPrice.size());
                            Integer randomValue = (Integer)bookPrice.values().toArray()[randValue];
                            String randomKey = (String)bookPrice.keySet().toArray()[randValue];

                            BookCopy bc = new BookCopy(randomValue, randomKey);
                            bc.setBook(book1);

                            bookCopyRepository.save(bc);
                        }

                }

                Employee employee1 = new Employee("employee1", "employee1", 1000, "kk@kk.kk", "ppppppp", AccessLevel.ADMIN);
                Employee employee2 = new Employee("employee2", "employee2", 976, "ll@kk.kk", "ppppppp");
                Employee employee3 = new Employee("employee3", "employee3", 1000, "oo@kk.kk", "ppppppp");
                Employee employee4 = new Employee("employee4", "employee4", 1000, "pp@kk.kk", "ppppppp");

                employee1.setSupervisor(employee3);
                employee2.setSupervisor(employee4);
                employee4.setSupervisor(employee3);

                employeeRepository.saveAll(List.of(employee1, employee2, employee3, employee4));

                Reader reader1 = new Reader("Jan", "Kowalski", "Krakow", "Malopolska", "31-503", "Polska", "example1@email.com", "pass1", "123 456 789", LocalDate.of(1987, 12, 3), "male");
                Reader reader2 = new Reader("Janka", "Kowalska", "Warszawa", "Mazowieckie", "02-677", "Polska", "example2@email.com", "pass2", "987 654 321", LocalDate.of(2003, 5, 30), "female");
                Reader reader3 = new Reader("Janusz", "Nowak", "Krakow", "Malopolska", "31-503", "Polska", "example3@email.com", "pass3", "999 888 777", LocalDate.of(1949, 11, 13), "male");

                readerRepository.saveAll(List.of(reader1, reader2, reader3));

            }
        };
    }
}
