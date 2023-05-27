package top.luobogan.filter;

import top.luobogan.context.GatewayContext;

/**
 * 过滤器顶级接口
 */
public interface Filter {

    void doFilter(GatewayContext ctx) throws  Exception;

    default int getOrder(){
        FilterAspect annotation = this.getClass().getAnnotation(FilterAspect.class);
        if(annotation != null){
            return annotation.order();
        }
        return Integer.MAX_VALUE;
    };
}
