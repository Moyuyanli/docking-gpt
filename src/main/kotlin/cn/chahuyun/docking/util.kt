package cn.chahuyun.docking

import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.SingleMessage

/**
 * 对String的正则回流替换方法
 *
 * @param regex 正则表达式
 * @param conversion (匹配正则块的解构结果) -> 替换字符 的替换方法
 * @return String 按照正则替换后的文本
 */
fun String.replace(
    regex: Regex, conversion: (MatchResult.Destructured) -> String,
): String {
    // 调用 replaceList 方法获取替换后的列表，并将其拼接成最终字符串
    return this.replaceString(regex, conversion).joinToString("")
}


/**
 * 对String进行正则匹配并替换，返回一个包含替换后内容的列表
 *
 * @param regex 正则表达式
 * @param conversion (匹配正则块的解构结果) -> 替换字符 的替换方法
 * @return MutableList<String> 包含替换后内容的列表
 */
fun String.replaceString(
    regex: Regex, conversion: (MatchResult.Destructured) -> String,
): MutableList<String> {
    val resultMessages = mutableListOf<String>() // 用于存储当前行的消息组件
    var original = this

    var lastIndex = 0 // 记录上一次匹配结束的位置

    while (true) {
        // 查找下一个匹配项
        val matchResult = regex.find(original) ?: break

        // 获取匹配前的文本（即普通文本部分）
        val prefixText = original.substring(0, matchResult.range.first)
        if (prefixText.isNotEmpty()) {
            resultMessages.add(prefixText) // 将普通文本添加到结果中
        }

        // 使用 conversion 函数获取替换内容
        val replacement = conversion.invoke(matchResult.destructured)
        if (replacement.isNotEmpty()) {
            resultMessages.add(replacement)
        }

        // 更新剩余文本（去掉已处理的部分）
        lastIndex += matchResult.range.last + 1
        original = original.substring(matchResult.range.last + 1)
    }

    // 如果还有剩余文本，添加到最后
    if (lastIndex < original.length) {
        val suffixText = original.substring(lastIndex)
        resultMessages.add(suffixText)
    }

    return resultMessages
}

/**
 * 对String进行正则匹配并替换，返回一个包含替换后内容的消息列表
 *
 * @param regex 正则表达式
 * @param conversion (匹配正则块的解构结果) -> 替换消息 的替换方法
 * @return MutableList<SingleMessage> 包含替换后内容的消息列表
 */
fun String.replaceMessage(
    regex: Regex, conversion: (MatchResult.Destructured) -> SingleMessage?,
): MutableList<SingleMessage> {
    val resultMessages = mutableListOf<SingleMessage>() // 用于存储当前行的消息组件
    var original = this

    var lastIndex = 0 // 记录上一次匹配结束的位置

    while (true) {
        // 查找下一个匹配项
        val matchResult = regex.find(original) ?: break

        // 获取匹配前的文本（即普通文本部分）
        val prefixText = original.substring(0, matchResult.range.first)
        if (prefixText.isNotEmpty()) {
            resultMessages.add(PlainText(prefixText)) // 将普通文本添加到结果中
        }

        // 使用 conversion 函数获取替换内容
        conversion.invoke(matchResult.destructured)?.let { resultMessages.add(it) }

        // 更新剩余文本（去掉已处理的部分）
        lastIndex += matchResult.range.last + 1
        original = original.substring(matchResult.range.last + 1)
    }

    // 如果还有剩余文本，添加到最后
    if (lastIndex < original.length) {
        val suffixText = original.substring(lastIndex)
        resultMessages.add(PlainText(suffixText))
    }

    return resultMessages
}