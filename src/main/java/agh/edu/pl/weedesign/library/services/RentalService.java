package agh.edu.pl.weedesign.library.services;

import agh.edu.pl.weedesign.library.entities.bookCopy.BookCopy;
import agh.edu.pl.weedesign.library.entities.reader.Reader;
import agh.edu.pl.weedesign.library.entities.reader.ReaderRepository;
import agh.edu.pl.weedesign.library.entities.rental.Rental;
import agh.edu.pl.weedesign.library.entities.rental.RentalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final ReaderRepository readerRepository;

    public RentalService(RentalRepository rentalRepository, ReaderRepository readerRepository) {
        this.rentalRepository = rentalRepository;
        this.readerRepository = readerRepository;
    }

    public Reader getReaderByEmail(String email){
        return readerRepository.findByEmail(email);
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Rental getReaderById(Long id) {
        return rentalRepository.findById(Math.toIntExact(id)).orElse(null);
    }

    public Rental addNewRental(Rental newRental) {
        return rentalRepository.save(newRental);
    }

    public Rental updateRental(Rental rental){
        return rentalRepository.save(rental);
    }
    public void removeRental(Rental rental){
        rentalRepository.delete(rental);
    }

    public List<Rental> findByReader(Reader reader) {
        return rentalRepository.findRentalsByReader(reader);
    }

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


}
