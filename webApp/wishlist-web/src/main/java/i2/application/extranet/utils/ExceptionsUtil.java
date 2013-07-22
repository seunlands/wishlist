package i2.application.extranet.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.JDBCException;

/**
 * Classe Util pour les Exceptions
 * @author Bull
 *
 */
public class ExceptionsUtil {
	
	
	public static StringBuffer getMessageForException(Exception e){		
		StringBuffer message = new StringBuffer();		
		message = getMessageForException(e, message);		
		String cause = e.getMessage();
		Throwable t = e.getCause();
		int i = 0;
		while (t!= null){			
			message = getMessageForException(t,message);			
			if(ObjectUtils.equals(cause, t.getMessage()) || i > 10){
				t = null;
			}else{
				cause = t.getMessage();
				t = t.getCause();
			}
			i++;
		}
		return message;
	}
		
	
	private static StringBuffer getMessageForException(Throwable e,StringBuffer message){
		if(e instanceof JDBCException){			
			message.append("Hibernate JDBCException : ")
			.append(e.getMessage())
			.append("\n")
			.append("SQL = ")
			.append(((JDBCException)e).getSQL())
			.append("\n")
			.append("SQLState = ")
			.append(((JDBCException)e).getSQLState())
			.append("\n");
			if (((JDBCException)e).getSQLException() != null ){
				message.append("SQLMessage = ")
				.append(((JDBCException)e).getSQLException().getMessage())
				.append("\n");
			}
		}else{
			message.append(e.getMessage()).append("\n");
		}		
		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        message.append(sw.toString()).append("\n");
			
		return message;
	}

}
