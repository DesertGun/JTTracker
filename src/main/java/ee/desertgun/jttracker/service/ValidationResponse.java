package ee.desertgun.jttracker.service;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class ValidationResponse {
    private boolean validated;
    private String errorMessage;
    private String successMessage;
}
