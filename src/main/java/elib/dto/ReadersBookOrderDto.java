package elib.dto;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class ReadersBookOrderDto {
    private Integer id;
    private Integer bookId;
    private LocalDate startDate;
    private LocalDate endDate;
}
