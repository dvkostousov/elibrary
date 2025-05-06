package elib.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class WriterDto {
	private Long id;
    private String fullName;
    private String bio;
    private List<WritersBookDto> books;
}
