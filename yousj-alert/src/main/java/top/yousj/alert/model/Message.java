package top.yousj.alert.model;

import lombok.Data;
import top.yousj.alert.enums.AlertType;

import java.util.Map;

@Data
public class Message {

    private AlertType alertType;

    private String appName;

    private String tenantId;

    private Long templateId;

    private Map<String, Object> params;

    private String title;

    private String msg;

    private boolean atOnce;

}