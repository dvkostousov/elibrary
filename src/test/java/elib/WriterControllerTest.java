package elib;

import com.fasterxml.jackson.databind.ObjectMapper;

import elib.dto.NewWriterDto;
import elib.dto.UpdateWriterDto;
import elib.jdbc.entity.Writer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WriterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long writerId;

    @Test
    @Order(1)
    void createWriter() throws Exception {
        NewWriterDto writerDto = new NewWriterDto("Mock Writer", "Bio test");

        ResultActions result = mockMvc.perform(post("/writers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(writerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        String response = result.andReturn().getResponse().getContentAsString();
        Writer created = objectMapper.readValue(response, Writer.class);
        writerId = created.getId();
    }

    @Test
    @Order(2)
    void getWriterById() throws Exception {
        mockMvc.perform(get("/writers/{id}", writerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Mock Writer"));
    }

    @Test
    @Order(3)
    void updateWriter() throws Exception {
        UpdateWriterDto update = new UpdateWriterDto("Updated Mock Writer", "Updated Bio");

        mockMvc.perform(patch("/writers/{id}", writerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Updated Mock Writer"));
    }

    @Test
    @Order(4)
    void getAllWriters() throws Exception {
        mockMvc.perform(get("/writers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(5)
    void deleteWriter() throws Exception {
        mockMvc.perform(delete("/writers/{id}", writerId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/writers/{id}", writerId))
                .andExpect(status().isNotFound());
    }
}
