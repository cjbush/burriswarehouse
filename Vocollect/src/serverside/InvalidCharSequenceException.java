package serverside;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Josh Krupka
 * @version 1.0
 */

public class InvalidCharSequenceException extends Exception {

   public InvalidCharSequenceException() {
      super();
   }

   public InvalidCharSequenceException(String err) {
      super(err);
   }

}
