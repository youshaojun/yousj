package top.yousj.crypto.annotation;

import top.yousj.crypto.handler.AesCryptHandler;
import top.yousj.crypto.handler.CryptHandler;

import java.lang.annotation.*;

@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Decrypt {

	Class<? extends CryptHandler> handler() default AesCryptHandler.class;

}