package elib;

import elib.dto.NewBookDto;
import elib.dto.UpdateBookDto;
import elib.jdbc.entity.Book;
import elib.jdbc.entity.Writer;
import elib.jdbc.repository.BookRepository;
import elib.jdbc.repository.WriterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private WriterRepository writerRepo;

    private Writer writer;

    @BeforeEach
    void setup() {
        writer = writerRepo.save(new Writer(null, "Author Name", "Some bio"));
    }

    @Test
    void testGetBooks() throws Exception {
        Book newBook = new Book(null, writer.getId(), "Test Book", "Test Description", 10, 0, Book.Genre.FICTION);
        Book savedBook = bookRepo.save(newBook);
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(savedBook.getName()));
    }



    @Test
    void testGetBookById() throws Exception {
        Book savedBook = bookRepo.save(new Book(null, writer.getId(), "Test Book", "Test Description", 10, 0, Book.Genre.FICTION));

        mockMvc.perform(get("/books/{id}", savedBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Book"));
    }

    @Test
    void testGetBookByIdNotFound() throws Exception {
        mockMvc.perform(get("/books/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found with id = 999"));
    }

    @Test
    void testPostBook() throws Exception {
        NewBookDto newBookDto = new NewBookDto(writer.getId(), "New Book", "New Description", 5, Book.Genre.HISTORY);

        String jsonRequest = String.format("{\"writerId\": %d, \"name\": \"%s\", \"description\": \"%s\", \"totalCopies\": %d, \"genre\": \"%s\"}",
                newBookDto.getWriterId(), newBookDto.getName(), newBookDto.getDescription(), newBookDto.getTotalCopies(), newBookDto.getGenre());

        mockMvc.perform(post("/books")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Book"))
                .andExpect(jsonPath("$.borrowCopies").value(0));
    }

    @Test
    void testPostBookWriterNotFound() throws Exception {
        NewBookDto newBookDto = new NewBookDto(999L, "New Book", "New Description", 5, Book.Genre.HISTORY);

        mockMvc.perform(post("/books")
                        .contentType("application/json")
                        .content(String.format("{\"writerId\": %d, \"name\": \"%s\", \"description\": \"%s\", \"totalCopies\": %d, \"genre\": \"%s\"}",
                                newBookDto.getWriterId(), newBookDto.getName(), newBookDto.getDescription(), newBookDto.getTotalCopies(), newBookDto.getGenre())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book's writer not found with id = 999"));
    }

    @Test
    void testPatchBook() throws Exception {
        Book savedBook = bookRepo.save(new Book(null, writer.getId(), "Old Book", "Old Description", 10, 0, Book.Genre.FICTION));

        UpdateBookDto updateBookDto = new UpdateBookDto("Updated Book", "Updated Description", 15, Book.Genre.HISTORY);

        String jsonRequest = String.format("{\"name\": \"%s\", \"description\": \"%s\", \"totalCopies\": %d, \"genre\": \"%s\"}",
                updateBookDto.getName(), updateBookDto.getDescription(), updateBookDto.getTotalCopies(), updateBookDto.getGenre());

        mockMvc.perform(patch("/books/{id}", savedBook.getId())
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Book"));
    }

    @Test
    void testPatchBookNotFound() throws Exception {
        UpdateBookDto updateBookDto = new UpdateBookDto("Updated Book", "Updated Description", 15, Book.Genre.FICTION);

        String jsonRequest = String.format("{\"name\": \"%s\", \"description\": \"%s\", \"totalCopies\": %d, \"genre\": \"%s\"}",
                updateBookDto.getName(), updateBookDto.getDescription(), updateBookDto.getTotalCopies(), updateBookDto.getGenre());

        mockMvc.perform(patch("/books/{id}", 999L)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found with id = 999"));
    }

    @Test
    void testDeleteBook() throws Exception {
        Book savedBook = bookRepo.save(new Book(null, writer.getId(), "Book to Delete", "Test Description", 5, 0, Book.Genre.FICTION));

        mockMvc.perform(delete("/books/{id}", savedBook.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteBookNotFound() throws Exception {
        mockMvc.perform(delete("/books/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found with id = 999"));
    }

    @Test
    void testDeleteBookWithOrders() throws Exception {
        Book savedBook = bookRepo.save(new Book(null, writer.getId(), "Book with Orders", "Test Description", 5, 1, Book.Genre.FICTION));

        mockMvc.perform(delete("/books/{id}", savedBook.getId()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Book with id " + savedBook.getId() + " cannot be deleted because it is currently borrowed by readers"));
    }
}
