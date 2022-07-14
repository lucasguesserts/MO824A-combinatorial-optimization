package problem;

public class InvalidInputException extends Exception {

    final String fileName;

    InvalidInputException(final String fileName, final String message) {
        super(String.format(
            "In file '%s': %s",
            fileName,
            message
        ));
        this.fileName = fileName;
        return;
    }

}
