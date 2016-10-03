package error;

/**
 * 
 * @authors 
 * Saarthak Chandra
 * Shweta Srivastava
 * Vikas P Nelamangala
 *
 * This class is used to call custom error handing, in the project..  
 * We can print custom messages by using this.
 */


public class SQLCustomErrorHandler extends Exception {
    public SQLCustomErrorHandler () {
    
    }

    public SQLCustomErrorHandler (String message) {
        super (message);
    }

    public SQLCustomErrorHandler (Throwable cause,String message) {
        System.out.println("Class is - "+ message);
        System.out.println("Error is - "+ cause.getMessage());
    }

    public SQLCustomErrorHandler (String message, Throwable cause) {
        super (message, cause);
    }
}