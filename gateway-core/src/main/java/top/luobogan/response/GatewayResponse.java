package top.luobogan.response;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.handler.codec.http.*;
import lombok.Data;
import org.asynchttpclient.Response;
import top.luobogan.enums.ResponseCode;
import top.luobogan.utils.JSONUtil;

@Data
public class GatewayResponse {

    /**
     * 响应头
     */
    private HttpHeaders responseHeaders = new DefaultHttpHeaders();

    /**
     * 额外的响应结果
     */
    private final HttpHeaders extraResponseHeaders = new DefaultHttpHeaders();
    /**
     * 响应内容
     */
    private String content;

    /**
     * 异步返回对象
     */
    private Response futureResponse;

    /**
     * 响应返回码
     */
    private HttpResponseStatus httpResponseStatus;


    public GatewayResponse(){

    }

    /**
     * 设置响应头信息
     * CharSequence接口提供了一些方法，如charAt、length、subSequence等，用于获取字符序列中的字符和子序列。
     * CharSequence接口的设计使得它适用于许多应用场景，如文本处理、正则表达式匹配等。
     */
    public void putHeader(CharSequence key, CharSequence val){
        responseHeaders.add(key,val);
    }

    /**
     * 构建异步响应对象
     * 在异步HTTP请求中，Response接口是非常重要的，通过该接口可以获取HTTP响应的各种信息，并对响应进行处理。
     * Response 接口是AsyncHttpClient异步HTTP客户端库中的一个接口，它表示HTTP响应。
     * 通过调用AsyncHttpClient发起HTTP请求后，会返回一个ListenableFuture<Response>对象，
     * 该对象包含一个 Response 接口的实例，可以通过该实例获取HTTP响应的状态码、头部信息、响应体等信息。
     */
    public static GatewayResponse buildGatewayResponse(Response futureResponse){
        GatewayResponse response = new GatewayResponse();
        response.setFutureResponse(futureResponse);
        response.setHttpResponseStatus(HttpResponseStatus.valueOf(futureResponse.getStatusCode()));
        return response;
    }

    /**
     * 处理返回json对象，失败时调用
     */
    public static GatewayResponse buildGatewayResponse(ResponseCode code, Object...args){
        ObjectNode objectNode = JSONUtil.createObjectNode();
        objectNode.put(JSONUtil.STATUS,code.getStatus().code());
        objectNode.put(JSONUtil.CODE,code.getCode());
        objectNode.put(JSONUtil.MESSAGE,code.getMessage());

        GatewayResponse response = new GatewayResponse();
        response.setHttpResponseStatus(code.getStatus());
        response.putHeader(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.APPLICATION_JSON+";charset=utf-8");
        response.setContent(JSONUtil.toJSONString(objectNode));
        return response;
    }

    /**
     * 处理返回json对象，成功时调用
     */
    public static GatewayResponse buildGatewayResponse(Object data){
        ObjectNode objectNode = JSONUtil.createObjectNode();
        objectNode.put(JSONUtil.STATUS,ResponseCode.SUCCESS.getStatus().code());
        objectNode.put(JSONUtil.CODE,ResponseCode.SUCCESS.getCode());
        objectNode.putPOJO(JSONUtil.DATA,data);

        GatewayResponse response = new GatewayResponse();
        response.setHttpResponseStatus(ResponseCode.SUCCESS.getStatus());
        response.putHeader(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.APPLICATION_JSON+";charset=utf-8");
        response.setContent(JSONUtil.toJSONString(objectNode));
        return response;
    }

}
