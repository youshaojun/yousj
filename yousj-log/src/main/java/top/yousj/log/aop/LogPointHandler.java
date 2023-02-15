package top.yousj.log.aop;

/**
 * @author yousj
 * @since 2023-01-04
 */
@FunctionalInterface
public interface LogPointHandler {

	void handle(RequestLog requestLog) throws Exception;

}
