package elib;

import elib.dto.BookDto;

import elib.dto.NewBookDto;
import elib.dto.WritersBookDto;
import elib.jdbc.entity.Book;
import elib.jdbc.entity.Book.Genre;
import elib.mapper.JdbcBookMapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookMapperTest {

    @Test
    void toWritersBookDto_shouldMapFieldsCorrectly() {
        Book book = new Book(1L, 1L, "Book Title", "Description", 10, 5, Genre.FICTION);

        WritersBookDto result = JdbcBookMapper.toWritersBookDto(book);

        assertNotNull(result);
        assertEquals(book.getId(), result.getId());
        assertEquals(book.getName(), result.getName());
        assertEquals(book.getDescription(), result.getDescription());
        assertEquals(book.getGenre(), result.getGenre());
    }

    @Test
    void toBookDto_shouldMapFieldsCorrectly() {
        Book book = new Book(1L, 1L, "Book Title", "Description", 10, 5, Genre.FICTION);
        String writerFullName = "William Shakespeare";

        BookDto result = JdbcBookMapper.toBookDto(book, writerFullName);

        assertNotNull(result);
        assertEquals(book.getId(), result.getId());
        assertEquals(book.getWriterId(), result.getWriterId());
        assertEquals(writerFullName, result.getWriterFullName());
        assertEquals(book.getName(), result.getName());
        assertEquals(book.getDescription(), result.getDescription());
        assertEquals(book.getTotalCopies(), result.getTotalCopies());
        assertEquals(book.getBorrowCopies(), result.getBorrowCopies());
        assertEquals(book.getGenre(), result.getGenre());
    }

    @Test
    void toBook_shouldMapFieldsCorrectly() {
        NewBookDto newBookDto = new NewBookDto(1L, "Book Title", "Description", 10, Genre.FICTION);

        Book result = JdbcBookMapper.toBook(newBookDto);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(newBookDto.getWriterId(), result.getWriterId());
        assertEquals(newBookDto.getName(), result.getName());
        assertEquals(newBookDto.getDescription(), result.getDescription());
        assertEquals(newBookDto.getTotalCopies(), result.getTotalCopies());
        assertEquals(0, result.getBorrowCopies());
        assertEquals(newBookDto.getGenre(), result.getGenre());
    }
}
