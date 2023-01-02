package top.yousj.core.exception;

import top.yousj.core.entity.R;

public interface ExceptionAdviceHandler {

	default R<String> handle(Exception ex){
		return null;
	}

}
