package elib.jdbc.repository;

import java.util.Optional;


import elib.jdbc.entity.Book;

public interface BookRepository {
	Iterable<Book> findAll();
	
	Optional<Book> findById(Long id);
	
	Iterable<Book> findByWriterId(Long id);
	
	Book save(Book book);
	
	Book update(Book book);
	
	void deleteById(Long id);
}
