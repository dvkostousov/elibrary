package elib;

import elib.dto.NewReaderDto;
import elib.dto.ReaderDto;
import elib.dto.ReaderInListDto;
import elib.dto.ReadersBookOrderDto;
import elib.jpa.entity.Book;
import elib.jpa.entity.BookOrder;
import elib.jpa.entity.Reader;
import elib.mapper.JpaReaderMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ReaderMapperTest {

    private Reader reader;
    private Book book;
    private BookOrder bookOrder;

    @BeforeEach
    public void setUp() {
        book = new Book(1, null, "Test Book", "Description", 10, 5, Book.Genre.FICTION);
        
        reader = new Reader(1, "John Doe", "123 Test St", "+123456789", new ArrayList<BookOrder>());

        bookOrder = new BookOrder(1, reader, book, LocalDate.now(), LocalDate.now().plusDays(10));
        reader.getBookOrders().add(bookOrder);
    }

    @Test
    public void testToReaderDto() {
        ReaderDto readerDto = JpaReaderMapper.toReaderDto(reader);

        assertNotNull(readerDto);
        assertEquals(reader.getId(), readerDto.getId());
        assertEquals(reader.getFullName(), readerDto.getFullName());
        assertEquals(reader.getAddress(), readerDto.getAddress());
        assertEquals(reader.getContact(), readerDto.getContact());
        assertEquals(1, readerDto.getBookOrders().size());

        ReadersBookOrderDto bookOrderDto = readerDto.getBookOrders().get(0);
        assertEquals(book.getId(), bookOrderDto.getBookId());
        assertEquals(bookOrder.getStartDate(), bookOrderDto.getStartDate());
        assertEquals(bookOrder.getEndDate(), bookOrderDto.getEndDate());
    }

    @Test
    public void testToReaderInListDto() {
        ReaderInListDto readerInListDto = JpaReaderMapper.toReaderInListDto(reader);

        assertNotNull(readerInListDto);
        assertEquals(reader.getId(), readerInListDto.getId());
        assertEquals(reader.getFullName(), readerInListDto.getFullName());
        assertEquals(reader.getAddress(), readerInListDto.getAddress());
        assertEquals(reader.getContact(), readerInListDto.getContact());
    }

    @Test
    public void testToReader() {
        NewReaderDto newReaderDto = new NewReaderDto("Jane Doe", "456 Another St", "+987654321");
        Reader newReader = JpaReaderMapper.toReader(newReaderDto);

        assertNotNull(newReader);
        assertEquals(newReaderDto.getFullName(), newReader.getFullName());
        assertEquals(newReaderDto.getAddress(), newReader.getAddress());
        assertEquals(newReaderDto.getContact(), newReader.getContact());
        assertEquals(0, newReader.getBookOrders().size());
    }
}
