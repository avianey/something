package fr.avianey.lunatech.provider;

import javax.ejb.EJBException;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionProvider implements ExceptionMapper<Exception> {
    
    public Response toResponse(Exception exception) {
        Status status = Status.INTERNAL_SERVER_ERROR;
        Exception cause;
        
        if (exception instanceof EJBException) {
        	do {
        		cause = ((EJBException) exception).getCausedByException();
        		if (cause == null){
        			cause = (Exception) exception.getCause();
        		}
        	} while (cause instanceof EJBException);
        } else {
            cause = exception;
        }
        
        if (cause != null) {
            if (cause instanceof NoResultException) {
                status = Status.NOT_FOUND;
            } else if (cause instanceof IllegalArgumentException) {
                status = Status.BAD_REQUEST;
            }
        }

        return Response.status(status).build();
    }
    
}
