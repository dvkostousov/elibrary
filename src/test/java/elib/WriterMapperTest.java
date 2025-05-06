package elib;

import elib.dto.NewWriterDto;

import elib.dto.WriterDto;
import elib.dto.WritersBookDto;
import elib.jdbc.entity.Book;
import elib.jdbc.entity.Writer;
import elib.mapper.JdbcWriterMapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JdbcWriterMapperTest {
    @Test
    void toWriterDto_shouldMapAllFieldsCorrectly() {
        Writer writer = new Writer(1L, "William Shakespeare", "English playwright and poet");
        Book book1 = new Book(101L, 1L, "Hamlet", "Tragedy", 10, 2, Book.Genre.FICTION);
        Book book2 = new Book(102L, 1L, "Macbeth", "Tragedy", 8, 1, Book.Genre.FICTION);
        List<Book> books = List.of(book1, book2);


        WriterDto dto = JdbcWriterMapper.toWriterDto(writer, books);

        assertNotNull(dto);
        assertEquals(writer.getId(), dto.getId());
        assertEquals(writer.getFullName(), dto.getFullName());
        assertEquals(writer.getBio(), dto.getBio());
        assertNotNull(dto.getBooks());
        assertEquals(2, dto.getBooks().size());

        WritersBookDto dtoBook1 = dto.getBooks().get(0);
        assertEquals(book1.getId(), dtoBook1.getId());
        assertEquals(book1.getName(), dtoBook1.getName());
        assertEquals(book1.getDescription(), dtoBook1.getDescription());
        assertEquals(book1.getGenre(), dtoBook1.getGenre());

        WritersBookDto dtoBook2 = dto.getBooks().get(1);
        assertEquals(book2.getId(), dtoBook2.getId());
        assertEquals(book2.getName(), dtoBook2.getName());
        assertEquals(book2.getDescription(), dtoBook2.getDescription());
        assertEquals(book2.getGenre(), dtoBook2.getGenre());
    }

    @Test
    void toWriter_shouldMapAllFieldsCorrectly() {
        NewWriterDto newWriterDto = new NewWriterDto("J. R. R. Tolkien", "Author of The Lord of the Rings");

        Writer writer = JdbcWriterMapper.toWriter(newWriterDto);

        assertNotNull(writer);
        assertNull(writer.getId());
        assertEquals(newWriterDto.getFullName(), writer.getFullName());
        assertEquals(newWriterDto.getBio(), writer.getBio());
    }

    @Test
    void toWriterDto_shouldHandleNullBooks() {
        Writer writer = new Writer(2L, "George Orwell", "Author of 1984");

        WriterDto dto = JdbcWriterMapper.toWriterDto(writer, null);

        assertNotNull(dto);
        assertEquals(writer.getId(), dto.getId());
        assertEquals(writer.getFullName(), dto.getFullName());
        assertEquals(writer.getBio(), dto.getBio());
        assertNotNull(dto.getBooks());
        assertTrue(dto.getBooks().isEmpty());
    }
}
