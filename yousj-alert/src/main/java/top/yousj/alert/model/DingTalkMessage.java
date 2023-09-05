package top.yousj.alert.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DingTalkMessage extends Message {

    private List<String> atMobiles;

}
