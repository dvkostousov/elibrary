package elib;

import elib.controller.BookOrderController;

import elib.jpa.entity.Book;
import elib.jpa.entity.BookOrder;
import elib.jpa.entity.Reader;
import elib.jpa.repository.BookOrderRepository;
import elib.jpa.repository.BookRepository;
import elib.jpa.repository.ReaderRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookOrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookOrderRepository bookOrderRepo;

    @Mock
    private BookRepository bookRepo;

    @Mock
    private ReaderRepository readerRepo;

    @InjectMocks
    private BookOrderController bookOrderController;

    private BookOrder bookOrder;
    private Book book;
    private Reader reader;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookOrderController).build();
        book = new Book(1, null, "Test Book", "Description", 10, 5, Book.Genre.FICTION);
        reader = new Reader(1, "Test Reader", "Test Address", "123456789", null);
        bookOrder = new BookOrder(1, reader, book, LocalDate.now(), LocalDate.now().plusDays(7));
    }

    @Test
    void testGetBookOrders() throws Exception {
        when(bookOrderRepo.findAll()).thenReturn(List.of(bookOrder));
        
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(bookOrder.getId()));
    }

    @Test
    void testGetOrderById() throws Exception {
        when(bookOrderRepo.findById(1)).thenReturn(Optional.of(bookOrder));

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookOrder.getId()));
    }

    @Test
    void testGetOrderByIdNotFound() throws Exception {
        when(bookOrderRepo.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order not found with id = 1"));
    }

    @Test
    void testPostNewBookOrder() throws Exception {
        when(bookRepo.findById(1)).thenReturn(Optional.of(book));
        when(readerRepo.findById(1)).thenReturn(Optional.of(reader));
        when(bookRepo.checkAvailable(1)).thenReturn(true);
        when(bookOrderRepo.save(any(BookOrder.class))).thenReturn(bookOrder);

        mockMvc.perform(post("/orders")
                .contentType("application/json")
                .content("{\"readerId\":1,\"bookId\":1,\"endDate\":\"2025-05-13\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(bookOrder.getId()));
    }

    @Test
    void testPostNewBookOrderBookNotFound() throws Exception {
        when(bookRepo.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(post("/orders")
                .contentType("application/json")
                .content("{\"readerId\":1,\"bookId\":999,\"endDate\":\"2025-05-13\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found with id = 999"));
    }

    @Test
    void testPatchBookOrder() throws Exception {
        when(bookOrderRepo.findById(1)).thenReturn(Optional.of(bookOrder));
        when(bookOrderRepo.save(any(BookOrder.class))).thenReturn(bookOrder);

        mockMvc.perform(patch("/orders/1")
                .contentType("application/json")
                .content("{\"endDate\":\"2025-05-06\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookOrder.getId()))
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                });
    }

    @Test
    void testDeleteBookOrder() throws Exception {
        when(bookOrderRepo.findById(1)).thenReturn(Optional.of(bookOrder));

        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(bookOrderRepo, Mockito.times(1)).deleteById(1);
        Mockito.verify(bookRepo, Mockito.times(1)).substractBorrowCopies(1, 1);
    }

    @Test
    void testDeleteBookOrderNotFound() throws Exception {
        when(bookOrderRepo.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order not found with id = 1"));
    }
}
