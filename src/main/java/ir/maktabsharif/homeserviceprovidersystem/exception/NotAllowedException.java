package ir.maktabsharif.homeserviceprovidersystem.exception;

public class NotAllowedException extends IllegalStateException{
    public NotAllowedException(String message){
        super(message);
    }
}
