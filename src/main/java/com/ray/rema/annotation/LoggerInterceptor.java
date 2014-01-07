package com.ray.rema.annotation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Loggable
public class LoggerInterceptor implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Logger log;

	@AroundInvoke
	public Object logMethod(InvocationContext context) throws Exception {
		
		Object target=context.getTarget();
		Object[] parameters = context.getParameters();
        Method method=context.getMethod();
		StringBuilder message = new StringBuilder();
		message.append("calling method: ").append(method);
		message.append('(');
		for (Object object : parameters) {
			message.append("{");
			message.append(object == null ? "null" : object.toString());
			message.append("} , ");

		}
		//message.deleteCharAt(message.length()-1);
		message.append(')');

        Loggable loggable=method.getAnnotation(Loggable.class);
		if(loggable==null){
			loggable=target.getClass().getAnnotation(Loggable.class);
		}

		Level level=Level.INFO;
           
		if(loggable!=null){
			level=Level.parse(loggable.level());
		}

		log.log(level, message.toString());
		return context.proceed();
	}

}
