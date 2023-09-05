package top.yousj.alert.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PushPlusMessage extends Message {

    private String topic;

}
