package top.luobogan.support.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.ServiceBean;
import org.apache.dubbo.config.spring.context.event.ServiceBeanExportedEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import top.luobogan.config.ServiceDefinition;
import top.luobogan.config.ServiceInstance;
import top.luobogan.core.ApiAnnotationScanner;
import top.luobogan.core.ApiProperties;
import top.luobogan.support.AbstractClientRegisterManager;
import top.luobogan.utils.NetUtils;
import top.luobogan.utils.TimeUtil;

import java.util.HashSet;
import java.util.Set;

import static top.luobogan.constants.BasicConst.COLON_SEPARATOR;
import static top.luobogan.constants.GatewayConst.DEFAULT_WEIGHT;

@Slf4j
public class Dubbo27ClientRegisterManager extends AbstractClientRegisterManager implements ApplicationListener<ApplicationEvent> {

    private Set<Object> set = new HashSet<>();

    public Dubbo27ClientRegisterManager(ApiProperties apiProperties) {
        super(apiProperties);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {

        if (applicationEvent instanceof ServiceBeanExportedEvent) {
            try {
                ServiceBean serviceBean = ((ServiceBeanExportedEvent) applicationEvent).getServiceBean();
                doRegisterDubbo(serviceBean);
            } catch (Exception e) {
                log.error("doRegisterDubbo error", e);
                throw new RuntimeException(e);
            }
        } else if (applicationEvent instanceof ApplicationStartedEvent) {
            log.info("dubbo api started");
        }
    }

    private void doRegisterDubbo(ServiceBean serviceBean) {
        Object bean = serviceBean.getRef();

        if (set.contains(bean)) {
            return;
        }

        ServiceDefinition serviceDefinition = ApiAnnotationScanner.getInstance().scanner(bean, serviceBean);

        if (serviceDefinition == null) {
            return;
        }

        serviceDefinition.setEnvType(getApiProperties().getEnv());

        //服务实例
        ServiceInstance serviceInstance = new ServiceInstance();
        String localIp = NetUtils.getLocalIp();
        int port = serviceBean.getProtocol().getPort();
        String serviceInstanceId = localIp + COLON_SEPARATOR + port;
        String uniqueId = serviceDefinition.getUniqueId();
        String version = serviceDefinition.getVersion();

        serviceInstance.setServiceInstanceId(serviceInstanceId);
        serviceInstance.setUniqueId(uniqueId);
        serviceInstance.setIp(localIp);
        serviceInstance.setPort(port);
        serviceInstance.setRegisterTime(TimeUtil.currentTimeMillis());
        serviceInstance.setVersion(version);
        serviceInstance.setWeight(DEFAULT_WEIGHT);

        register(serviceDefinition, serviceInstance);
    }
}
