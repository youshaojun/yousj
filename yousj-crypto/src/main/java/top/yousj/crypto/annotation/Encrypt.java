package top.yousj.crypto.annotation;

import top.yousj.crypto.handler.CryptHandler;
import top.yousj.crypto.handler.RsaCryptHandler;

import java.lang.annotation.*;

@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Encrypt {

	Class<? extends CryptHandler> handler() default RsaCryptHandler.class;

	boolean onlyData() default false;

}