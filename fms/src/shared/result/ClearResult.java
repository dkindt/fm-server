package shared.result;

/** Represents the response message to be returned to the ClearService.  */
public class ClearResult {

    private String message;

    public ClearResult(String message) {
        setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
