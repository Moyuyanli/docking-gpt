package cn.chahuyun.dockingGpt.event;

import cn.chahuyun.authorize.annotation.EventComponent;
import cn.chahuyun.authorize.annotation.MessageAuthorize;
import cn.chahuyun.authorize.enums.MessageMatchingEnum;
import cn.chahuyun.dockingGpt.docking.RequestFactory;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * 指令入口
 *
 * @author Moyuyanli
 * @date 2023/10/19 17:00
 */
@EventComponent
public class Operation {


    /**
     * 修改提问模型
     * 需求权限:
     * <li> 群:chat
     * <li> 人:chat-admin
     *
     * @param event 消息事件
     */
    @MessageAuthorize(
            userPermissions = "chat-admin",
            text = "切换模型 (gpt3.5|gpt4.0|通义千问|文心一言)",
            messageMatching = MessageMatchingEnum.REGULAR,
            groupPermissions = "chat")
    public void switchModel(GroupMessageEvent event) {
        String content = event.getMessage().contentToString();
        String[] split = content.split(" ");
        RequestFactory.create().setModel(split[1]);
        event.getSubject().sendMessage("已将模型修改为:" + split[1]);
    }

    /**
     * 查看当前模型
     *
     * @param event 消息事件
     * @author Moyuyanli
     * @date 2023/10/23 10:10
     */
    @MessageAuthorize(
            userPermissions = "chat-admin",
            text = "当前模型",
            messageMatching = MessageMatchingEnum.TEXT,
            groupPermissions = "chat")
    public void viewModel(GroupMessageEvent event) {
        String model = RequestFactory.create().getModel();
        event.getSubject().sendMessage("当前ai模型为:" + model);
    }

    /**
     * 查询模型列表
     * <li> ChatGPT-3.5
     * <li> ChatGPT-4.0
     * <li> 通义千问
     * <li> 文心一言
     *
     * @param event 消息事件
     * @author Moyuyanli
     * @date 2023/10/23 10:15
     */
    @MessageAuthorize(
            userPermissions = "chat-admin",
            text = "模型列表",
            messageMatching = MessageMatchingEnum.TEXT,
            groupPermissions = "chat")
    public void listModel(GroupMessageEvent event) {
        Group subject = event.getSubject();
        subject.sendMessage("目前支持模型列表:\ngpt3.5\ngpt4.0\n通义千问\n文心一言");
    }

}
