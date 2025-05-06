package elib;

import com.fasterxml.jackson.databind.ObjectMapper;

import elib.dto.NewReaderDto;
import elib.dto.UpdateReaderDto;
import elib.jpa.entity.Reader;
import elib.jpa.repository.ReaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReaderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReaderRepository readerRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private Reader reader;

    @BeforeEach
    void setUp() {
        reader = new Reader();
        reader.setFullName("John Smith");
        reader.setAddress("High Street London SW1A 1AA United Kingdom");
        reader.setContact("+7 900 000 00 00");
        readerRepo.save(reader);
    }

    @Test
    void testGetReaders() throws Exception {
        mockMvc.perform(get("/readers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("John Smith"));
    }

    @Test
    void testGetReaderById() throws Exception {
        mockMvc.perform(get("/readers/{id}", reader.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Smith"));
    }

    @Test
    void testGetReaderById_NotFound() throws Exception {
        mockMvc.perform(get("/readers/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Reader not found with id = 999"));
    }

    @Test
    void testPostReader() throws Exception {
        NewReaderDto newReader = new NewReaderDto("Jane Doe", "Some Address", "+7 900 111 11 11");

        mockMvc.perform(post("/readers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReader)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Jane Doe"));
    }

    @Test
    void testPatchReader() throws Exception {
        UpdateReaderDto updateReader = new UpdateReaderDto("John Smit", "New Address", "+7 900 222 22 22");

        mockMvc.perform(patch("/readers/{id}", reader.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReader)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Smit"));
    }

    @Test
    void testPatchReader_NotFound() throws Exception {
        UpdateReaderDto updateReader = new UpdateReaderDto("John Smit", "New Address", "+7 900 222 22 22");

        mockMvc.perform(patch("/readers/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReader)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Reader not found with id = 999"));
    }

    @Test
    void testDeleteReader() throws Exception {
        mockMvc.perform(delete("/readers/{id}", reader.getId()))
                .andExpect(status().isNoContent());

        Optional<Reader> deletedReader = readerRepo.findById(reader.getId());
        assert deletedReader.isEmpty();
    }

    @Test
    void testDeleteReader_NotFound() throws Exception {
        mockMvc.perform(delete("/readers/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Reader not found with id = 999"));
    }
}
