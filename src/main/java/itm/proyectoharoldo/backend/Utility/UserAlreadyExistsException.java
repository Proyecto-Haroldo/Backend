package itm.proyectoharoldo.backend.Utility;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message){
        super(message);
    }

}
