package top.yousj.web.date;

import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * @author yousj
 * @since 2023-01-11
 */
public class DefaultJavaTimeModule extends SimpleModule {

	public DefaultJavaTimeModule() {
		super(PackageVersion.VERSION);

		// yyyy-MM-dd HH:mm:ss
		this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DatePattern.NORM_DATETIME_FORMATTER));
		// yyyy-MM-dd
		this.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
		// HH:mm:ss
		this.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));
		// Instant 类型序列化
		this.addSerializer(Instant.class, InstantSerializer.INSTANCE);

		// yyyy-MM-dd HH:mm:ss 或 yyyy-MM-dd'T'HH:mm:ss
		DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
			.appendPattern("yyyy-MM-dd")
			.appendOptional(DateTimeFormatter.ofPattern(" "))
			.appendOptional(DateTimeFormatter.ofPattern("'T'"))
			.appendPattern("HH:mm:ss")
			.toFormatter();
		this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
		// yyyy-MM-dd
		this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));
		// HH:mm:ss
		this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME));
		// Instant 反序列化
		this.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
	}

}