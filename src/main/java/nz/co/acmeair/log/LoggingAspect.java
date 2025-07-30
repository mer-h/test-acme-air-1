package nz.co.acmeair.log;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoggingAspect {
	
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	@Pointcut("execution(* nz.co.acmeair.flight.service.impl..*(..)) "
			+ "|| execution(* nz.co.acmeair.flight.controller..*(..))"
			+ "|| execution(* nz.co.acmeair.flight.data..*(..))"
			+ "|| execution(* nz.co.acmeair.booking.service.impl..*(..)) "
			+ "|| execution(* nz.co.acmeair.booking.controller..*(..))"
			+ "|| execution(* nz.co.acmeair.booking.data..*(..))")
    public void appLayer() {}

	 @Before("appLayer()")
	    public void logBefore(JoinPoint joinPoint) {
	        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
	        logger.info("→ Starting method: {}.{}() with arguments: {}",
	                signature.getDeclaringType().getSimpleName(),
	                signature.getName(),
	                Arrays.toString(joinPoint.getArgs()));
	    }

	    @AfterReturning(pointcut = "appLayer()", returning = "result")
	    public void logAfterReturning(JoinPoint joinPoint, Object result) {
	        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
	        logger.info("← Exiting method: {}.{}() with result: {}",
	                signature.getDeclaringType().getSimpleName(),
	                signature.getName(),
	                result);
	    }

	    @AfterThrowing(pointcut = "appLayer()", throwing = "ex")
	    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
	        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
	        logger.error("‼ Exception in method: {}.{}() with cause: {}",
	                signature.getDeclaringType().getSimpleName(),
	                signature.getName(),
	                ex.getMessage());
	    }
}