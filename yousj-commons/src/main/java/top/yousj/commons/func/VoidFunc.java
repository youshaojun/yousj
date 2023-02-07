package top.yousj.commons.func;

import java.io.Serializable;

@FunctionalInterface
public interface VoidFunc extends Serializable {

	void call() throws Exception;

	default void callWithRuntimeException() {
		try {
			call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	default void callWithIgnoreException() {
		try {
			call();
		} catch (Exception ignored) {
		}
	}

}