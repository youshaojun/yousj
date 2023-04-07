package top.yousj.crypto.annotation;

import top.yousj.crypto.handler.CryptoHandler;
import top.yousj.crypto.handler.RsaCryptoHandler;

import java.lang.annotation.*;

@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Decrypt {

	Class<? extends CryptoHandler> handler() default RsaCryptoHandler.class;

}