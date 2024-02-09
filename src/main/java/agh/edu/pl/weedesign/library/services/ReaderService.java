package agh.edu.pl.weedesign.library.services;

import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.entities.reader.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReaderService {
    private final ReaderRepository readerRepository;

    @Autowired
    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public List<Reader> getAllReaders() {
        return readerRepository.findAll();
    }

    public Reader getReaderById(Long id) {
        return readerRepository.findById(Math.toIntExact(id)).orElse(null);
    }

    public Reader addNewReader(Reader newReader) {
        return readerRepository.save(newReader);
    }

    public Reader findByEmail(String email) {
        return readerRepository.findByEmail(email);
    }

    public boolean isEmailFree(String email){
        return !readerRepository.existsByEmail(email);
    }

    public String getReaderPasswordByEmail(String email){
        if (findByEmail(email) == null){
            return null;
        }
        return findByEmail(email).getPassword();
    }



}
