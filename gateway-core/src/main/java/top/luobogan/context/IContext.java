package top.luobogan.context;

import io.netty.channel.ChannelHandlerContext;
import top.luobogan.config.Rule;

import java.util.Map;
import java.util.function.Consumer;

/**
 * 核心上下文接口定义
 * 网关的状态就是网关的生命周期
 */
public interface IContext {

    /**
     * 一个请求正在执行中的状态
     */
    int RUNNING = 0;
    /**
     * 标志请求结束，写回Response
     */
    int WRITTEN = 1;
    /**
     * 写回成功后，设置该标识，如果是Netty ，ctx.WriteAndFlush(response)
     */
    int COMPLETED = 2;
    /**
     * 整个网关请求完毕，彻底结束
     */
    int TERMINATED = -1;

    /**
     * 设置上下文状态为正常运行状态
     */
    void running();

    /**
     * 设置上下文状态为标记写回
     */
    void written();
    /**
     * 设置上下文状态为标记写回成功
     */
    void completed();
    /**
     * 设置上下文状态为标记写回成功
     */
    void terminated();

    /**
     * 判断网关状态运行状态
     */
    boolean isRunning();
    boolean isWritten();
    boolean isCompleted();
    boolean isTerminated();

    /**
     * 获取请求转换协议
     */
    String getProtocol();
    /**
     * 获取请求转换协议
     */
    Rule getRule();
    /**
     * 获取请求对象
     */
    Object getRequest();
    /**
     * 获取请求结果
     */
    Object getResponse();

    /**
     * 获取异常信息
     */
    Throwable getThrowable();
    /**
     * 获取上下文参数
     */
    Object getAttribute(Map<String,Object> key);

    /**
     * 设置请求规则
     */
    void setRule();
    /**
     * 设置请求返回结果
     */
    void setResponse();
    /**
     * 设置请求异常信息
     */
    void setThrowable(Throwable throwable);
    /**
     * 设置上下文参数
     */
    void setAttribute(String key,Object obj);

    /**
     * 获取Netty上下文
     */
    ChannelHandlerContext getNettyCtx();

    /**
     * 是否保持连接
     */
    boolean isKeepAlive();
    /**
     * 释放资源
     */
    void releaseRequest();

    /**
     * 设置回调函数
     */
    void setCompletedCallBack(Consumer<IContext> consumer);

    /**
     * 执行回调函数
     */
    void invokeCompletedCallBack();

}
