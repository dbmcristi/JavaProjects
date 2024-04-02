package repository.Exceptions;

public class RepositoryException extends Exception{
    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Exception e) {
        super(message, e);
        System.out.println(e.getMessage());
    }
}
