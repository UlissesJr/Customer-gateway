package top.luobogan;

import top.luobogan.config.ServiceDefinition;
import top.luobogan.config.ServiceInstance;
import java.util.Set;

public interface RegisterCenterListener {

    void onChange(ServiceDefinition serviceDefinition,
                  Set<ServiceInstance> serviceInstanceSet);
}