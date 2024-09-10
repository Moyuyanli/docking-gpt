package cn.chahuyun.docking.entity

import cn.chahuyun.docking.RoleEnum
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

    constructor(role: RoleEnum, content: String) {
        this.role = role.value
        this.content = content
    }

    constructor(role: RoleEnum, name: String?, content: String) {
        this.role = role.value
        this.name = name
        this.content = content
    }
}
