package cn.chahuyun.dockingGpt.event;

import cn.chahuyun.authorize.annotation.MessageAuthorize;
import cn.chahuyun.authorize.annotation.MessageComponent;

/**
 * 提问入口
 *
 * @author Moyuyanli
 * @Date 2023/7/30 0:50
 */
@MessageComponent
public class AskQuestions {




    @MessageAuthorize
    public void question() {

    }

}
