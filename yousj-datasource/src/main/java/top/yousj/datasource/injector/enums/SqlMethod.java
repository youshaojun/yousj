package top.yousj.datasource.injector.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SqlMethod {

	REPLACE_INTO("replaceInto", "", "<script>\nREPLACE INTO %s %s VALUES %s\n</script>"),

	INSERT_ON_DUPLICATE_KEY_UPDATE("insertOnDuplicateKeyUpdate", "", "<script>\nINSERT INTO %s %s VALUES %s\n ON DUPLICATE KEY UPDATE %s</script>"),

	;

	private final String method;
	private final String desc;
	private final String sql;

}