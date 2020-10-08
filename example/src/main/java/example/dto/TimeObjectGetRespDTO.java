package example.dto;

import lombok.Data;

@Data
public class TimeObjectGetRespDTO {
    private int years;
    private int months;
    private int date;
    private int hours;
    private int minutes;
    private int seconds;
    private int milliseconds;
}
