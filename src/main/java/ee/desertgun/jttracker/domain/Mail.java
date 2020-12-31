package ee.desertgun.jttracker.domain;

import lombok.Data;

import java.util.Map;

@Data
public class Mail {
    private String from;
    private String mailTo;
    private String subject;
    private Map<String, Object> props;
}
