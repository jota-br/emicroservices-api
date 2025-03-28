package ostro.veda.product_ms.exception;

public class DocumentAlreadyExists extends RuntimeException {
    public DocumentAlreadyExists(String message) {
        super(message);
    }
}
