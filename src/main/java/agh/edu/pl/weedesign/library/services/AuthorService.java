package agh.edu.pl.weedesign.library.services;

import agh.edu.pl.weedesign.library.entities.author.Author;
import agh.edu.pl.weedesign.library.entities.author.AuthorRepository;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.entities.reader.ReaderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author addNewAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author findByName(String first_name, String surname) {

        for(Author author: authorRepository.findAll())
            if(author.getName() == first_name && author.getSurname() == surname)
                return author;
        
        return null;
    }
}
