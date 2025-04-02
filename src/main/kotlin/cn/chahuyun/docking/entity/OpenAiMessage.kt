package cn.chahuyun.docking.entity

import cn.chahuyun.docking.RoleType
import lombok.Data

@Data
class Record {
    /**
     * 消息角色
     */
    private var role: String

    /**
     * 用户名
     */
    private var name: String? = null

    /**
     * 内容
     */
    var content: String

    constructor(role: RoleType, content: String) {
        this.role = role.value
        this.content = content
    }

    constructor(role: RoleType, name: String?, content: String) {
        this.role = role.value
        this.name = name
        this.content = content
    }
}
