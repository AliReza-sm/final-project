package ir.maktabsharif.homeserviceprovidersystem.exception;

public class AlreadyExistException extends IllegalArgumentException{

    public AlreadyExistException(String message){
        super(message);
    }
}
