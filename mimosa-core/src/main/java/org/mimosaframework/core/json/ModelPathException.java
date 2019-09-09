package org.mimosaframework.core.json;

@SuppressWarnings("serial")
public class ModelPathException extends ModelException {

    public ModelPathException(String message){
        super(message);
    }
    
    public ModelPathException(String message, Throwable cause){
        super(message, cause);
    }
}
