package top.yousj.alert.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MailMessage extends Message {

    private String to;

    private boolean isHtml;

}