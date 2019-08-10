package org.kudumbam.todo;

import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	
	@Autowired
	HttpServletRequest request;
	
	private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
	
	@Pointcut("execution(* org.kudumbam.todo.controller..*(..))")
	public void controller() {
	}
	
	//All classes that has the controller
//	@Pointcut("within(@org.springframework.stereotype.Controller *)")
//	public void controller() {
//	}
	
	//before -> Any resource annotated with @Controller annotation 	
	@Before("controller()")
    public void logBefore(JoinPoint joinPoint) {
		log.info("Entering in Method :  " + joinPoint.getSignature().getName());
		log.debug("Class Name :  " + joinPoint.getSignature().getDeclaringTypeName());
		log.debug("Arguments :  " + Arrays.toString(joinPoint.getArgs()));
		log.debug("Target class : " + joinPoint.getTarget().getClass().getName());

		if (null != request) {
			log.debug("Start Header Section of request ");
			log.info("Method Type : " + request.getMethod());
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = (String) headerNames.nextElement();
				String headerValue = request.getHeader(headerName);
				log.debug("Header Name: " + headerName + " Header Value : " + headerValue);
			}
			log.info("Request Path info :" + request.getServletPath());
			log.debug("End Header Section of request ");
		}
    }
	
	@AfterReturning(pointcut="controller()", returning="result")
    public void logAfter(JoinPoint point, Object result) {
		String returnValue = result.toString();
		log.debug("Method Return value : " + returnValue);
	}
	
	//After -> Any method within resource annotated with @Controller annotation 
	// throws an exception ...Log it
	@AfterThrowing(pointcut = "controller()", throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
		log.error("An exception has been thrown in " + joinPoint.getSignature().getName() + " ()");
		log.error("Cause : " + exception.getCause());
	}
	
	//Around -> Any method within resource annotated with @Controller annotation
	@Around("controller()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		
		long start = System.currentTimeMillis();
		try {
			String className = joinPoint.getSignature().getDeclaringTypeName();
			String methodName = joinPoint.getSignature().getName();
			Object result = joinPoint.proceed();
			long elapsedTime = System.currentTimeMillis() - start;
			log.debug("Method " + className + "." + methodName + " ()" + " execution time : "
					+ elapsedTime + " ms");
		
			return result;
		} catch (IllegalArgumentException e) {
			log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in "
					+ joinPoint.getSignature().getName() + "()");
			throw e;
		}
	}
	
	
//	private String getValue(Object result) {
//		String returnValue = null;
//		if (null != result) {
//			if (result.toString().endsWith("@" + Integer.toHexString(result.hashCode()))) {
//				returnValue = ReflectionToStringBuilder.toString(result);
//			} else {
//				returnValue = result.toString();
//			}
//		}
//		return returnValue;
//	}
//

}
