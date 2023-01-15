package top.yousj.security.utils;

/**
 * @author yousj
 * @since 2023-01-02
 */
public class AppNameHolder {

	private static final ThreadLocal<String> holder = new ThreadLocal<>();

	public static String get() {
		return holder.get();
	}

	public static void set(String appName) {
		holder.set(appName);
	}

	public static void clear() {
		holder.remove();
	}

}
