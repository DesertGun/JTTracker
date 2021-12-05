package ee.desertgun.jttracker.response;

import lombok.Data;

@Data
public class ValidationResponse {
    private boolean validated;
    private String errorMessage;
    private String successMessage;
    private String validationMessage;
}
