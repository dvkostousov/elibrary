package elib.mapper;

import elib.dto.BookDto;
import elib.dto.NewBookDto;
import elib.dto.WritersBookDto;
import elib.jdbc.entity.Book;

public class JdbcBookMapper {
	public static WritersBookDto toWritersBookDto(Book book) {
		return new WritersBookDto(book.getId(), book.getName(), book.getDescription(), book.getGenre());
	}
	
	public static BookDto toBookDto(Book book, String writerFullName) {
		return new BookDto(book.getId(), book.getWriterId(), writerFullName, book.getName(), book.getDescription(), book.getTotalCopies(), book.getBorrowCopies(), book.getGenre());
	}
	
	public static Book toBook(NewBookDto book) {
		return new Book(null, book.getWriterId(), book.getName(), book.getDescription(), book.getTotalCopies(), 0, book.getGenre());
	}
}
