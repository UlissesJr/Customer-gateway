package top.luobogan.netty.processor;

import top.luobogan.context.HttpRequestWrapper;

public interface NettyProcessor {

    void process(HttpRequestWrapper wrapper);
}
