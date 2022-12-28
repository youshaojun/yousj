package top.yousj.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

@Slf4j
@Component
public class LoadDocket {

    public LoadDocket(List<Docket> dockets){
        dockets.forEach(e -> log.debug(" load swagger group [" + e.getGroupName() + "]"));
    }

}
