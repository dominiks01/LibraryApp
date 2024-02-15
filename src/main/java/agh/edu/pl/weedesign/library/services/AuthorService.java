package agh.edu.pl.weedesign.library.services;

import agh.edu.pl.weedesign.library.entities.author.Author;
import agh.edu.pl.weedesign.library.entities.author.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void addNewAuthor(Author author) {
        authorRepository.save(author);
    }

    public Author findByName(String first_name, String surname) {

        for(Author author: authorRepository.findAll())
            if(Objects.equals(author.getName(), first_name) && Objects.equals(author.getSurname(), surname))
                return author;
        
        return null;
    }
}
