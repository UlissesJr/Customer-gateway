package top.luobogan.context;

import io.netty.channel.ChannelHandlerContext;
import top.luobogan.config.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * 核心上下文基础类
 */
public class BasicContext implements IContext {

    /**
     * 转发协议
     */
    protected final String protocol;

    /**
     * 上下文状态
     */
    protected volatile int status = IContext.RUNNING;
    /**
     * Netty上下文
     */
    protected final ChannelHandlerContext nettyCtx;

    /**
     * 上下文参数集合
     */
    protected final Map<String,Object> attributes = new HashMap<String,Object>();

    /**
     * 请求过程中发生的异常
     */
    protected Throwable throwable;
    /**
     * 是否保持长连接
     */
    protected final boolean keepAlive;

    /**
     * 是否已经释放资源
     */
    protected final AtomicBoolean requestReleased = new AtomicBoolean(false);

    /**
     * 存放回调函数的集合
     */
    protected List<Consumer<IContext>> completedCallbacks;

    /**
     * 构造函数
     * @param protocol
     * @param nettyCtx
     * @param keepAlive
     */
    public BasicContext(String protocol, ChannelHandlerContext nettyCtx, boolean keepAlive) {
        this.protocol = protocol;
        this.nettyCtx = nettyCtx;
        this.keepAlive = keepAlive;
    }


    @Override
    public void running() {
        status =  IContext.RUNNING;
    }

    @Override
    public void written() {
        status =  IContext.WRITTEN;
    }

    @Override
    public void completed() {
        status =  IContext.COMPLETED;
    }

    @Override
    public void terminated() {
        status =  IContext.TERMINATED;
    }

    @Override
    public boolean isRunning() {
        return status ==  IContext.RUNNING;
    }

    @Override
    public boolean isWritten() {
        return  status ==  IContext.WRITTEN;
    }

    @Override
    public boolean isCompleted() {
        return status ==  IContext.COMPLETED;
    }

    @Override
    public boolean isTerminated() {
        return status ==  IContext.TERMINATED;
    }

    @Override
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public Rule getRule() {
        return null;
    }

    @Override
    public Object getRequest() {
        return null;
    }

    @Override
    public Object getResponse() {
        return null;
    }

    @Override
    public Throwable getThrowable() {
        return this.throwable;
    }

    @Override
    public Object getAttribute(Map<String, Object> key) {
        return attributes.get(key);
    }

    @Override
    public void setRule() {

    }

    @Override
    public void setResponse() {

    }

    @Override
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public void setAttribute(String key,Object obj) {
        attributes.put(key,obj);
    }

    @Override
    public ChannelHandlerContext getNettyCtx() {
        return this.nettyCtx;
    }

    @Override
    public boolean isKeepAlive() {
        return this.keepAlive;
    }

    @Override
    public void releaseRequest() {
        this.requestReleased.compareAndSet(false,true);
    }

    @Override
    public void setCompletedCallBack(Consumer<IContext> consumer) {
         if(completedCallbacks == null){
             completedCallbacks = new ArrayList<>();
         }
        completedCallbacks.add(consumer);
    }

    /**
     * Java中的 forEach() 方法是一种遍历集合元素的方式。该方法是在 Java 8 中引入的，它可以迭代集合中的每个元素，并对每个元素执行一个操作。
     * forEach() 方法是一种函数式编程的方式，可以使用 lambda 表达式来实现该操作。
     * 下面是一个示例，演示如何使用 forEach() 方法遍历 List 中的所有元素
     * List<String> list = new ArrayList<>();
     * list.add("apple");
     * list.add("banana");
     * list.add("orange");
     * list.forEach(item -> System.out.println(item));
     */
    @Override
    public void invokeCompletedCallBack() {
        if(completedCallbacks != null){
            // 这里的call 就是for循环遍历里面的 Consumer<IContext> 实例对象;
            // this 是当前 BasicContext 实例;
            completedCallbacks.forEach(call->call.accept(this));
        }
    }
}
