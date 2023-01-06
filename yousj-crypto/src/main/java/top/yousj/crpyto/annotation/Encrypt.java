package top.yousj.crpyto.annotation;

import top.yousj.crpyto.handler.AesCryptHandler;
import top.yousj.crpyto.handler.CryptHandler;

import java.lang.annotation.*;

@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Encrypt {

	Class<? extends CryptHandler> handler() default AesCryptHandler.class;

}