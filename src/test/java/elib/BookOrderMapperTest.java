package elib;

import elib.dto.BookOrderDto;

import elib.dto.NewBookOrderDto;
import elib.dto.ReadersBookOrderDto;
import elib.jpa.entity.Book;
import elib.jpa.entity.BookOrder;
import elib.jpa.entity.Reader;
import elib.mapper.JpaBookOrderMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookOrderMapperTest {

    private BookOrder bookOrder;
    private Book book;
    private Reader reader;

    @BeforeEach
    void setUp() {
        book = new Book(1, null, "Test Book", "Description", 10, 5, Book.Genre.FICTION);
        reader = new Reader(1, "John Smith", "123 Street", "555-555-5555", null);
        bookOrder = new BookOrder(1, reader, book, LocalDate.now(), LocalDate.now().plusDays(7));
    }

    @Test
    void testToReadersBookOrderDto() {
        ReadersBookOrderDto dto = JpaBookOrderMapper.toReadersBookOrderDto(bookOrder);
        assertEquals(bookOrder.getId(), dto.getId());
        assertEquals(bookOrder.getBook().getId(), dto.getBookId());
        assertEquals(bookOrder.getStartDate(), dto.getStartDate());
        assertEquals(bookOrder.getEndDate(), dto.getEndDate());
    }

    @Test
    void testToBookOrderDto() {
        BookOrderDto dto = JpaBookOrderMapper.toBookOrderDto(bookOrder);
        assertEquals(bookOrder.getId(), dto.getId());
        assertEquals(reader.getId(), dto.getReaderId());
        assertEquals(reader.getFullName(), dto.getReaderFullName());
        assertEquals(book.getId(), dto.getBookId());
        assertEquals(book.getName(), dto.getBookname());
        assertEquals(bookOrder.getStartDate(), dto.getStartDate());
        assertEquals(bookOrder.getEndDate(), dto.getEndDate());
    }

    @Test
    void testToBookOrder() {
        NewBookOrderDto newBookOrderDto = new NewBookOrderDto(1, 1, LocalDate.now().plusDays(7));
        BookOrder result = JpaBookOrderMapper.toBookOrder(newBookOrderDto, book, reader);
        assertNull(result.getId());
        assertEquals(reader, result.getReader());
        assertEquals(book, result.getBook());
        assertEquals(LocalDate.now(), result.getStartDate());
        assertEquals(newBookOrderDto.getEndDate(), result.getEndDate());
    }
}

