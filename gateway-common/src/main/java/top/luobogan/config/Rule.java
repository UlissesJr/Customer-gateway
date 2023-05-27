package top.luobogan.config;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 核心规则类
 *
 * 实现 Serializable 接口的类可以被序列化和反序列化。
 * 在 Java 中，序列化是将对象转换为字节流的过程，可以将对象的状态保存到磁盘或通过网络传输。
 * 而反序列化则是将字节流转换回对象的过程，可以从磁盘或网络中读取对象的状态并重新构建对象。
 * 如果一个类的实例需要被序列化和反序列化，那么该类必须实现 Serializable 接口，这是 Java 序列化机制的规定。
 * 实现 Serializable 接口的类可以将其实例转换为字节流，使其可以在网络上传输或保存到磁盘中。
 * 例如，当使用Java中的对象流（ObjectInputStream和ObjectOutputStream）进行数据传输时，
 * 被传输的对象必须实现Serializable 接口。
 * 需要注意的是，实现 Serializable 接口的类可以被序列化和反序列化，但不保证其安全性。
 * 因此，在序列化敏感数据时，需要采取一些额外的安全措施，如加密、数字签名等。
 *
 * 实现 Comparable 接口的类可以进行对象之间的比较，可以用于排序等操作。
 * 在 Java 中，Comparable 接口定义了一个 compareTo 方法，该方法接受一个类型相同的实例作为参数，返回一个整数值。
 * 该整数值表示当前对象与参数对象之间的比较结果。
 * 需要注意的是，实现 Comparable 接口的类必须考虑到传入 compareTo 方法的参数可能为 null 的情况，
 * 并且在比较时应该考虑到所有的实例变量。
 * 另外，如果一个类实现了 Comparable 接口，那么它也应该实现 equals 方法，以确保在比较对象相等性时的一致性。
 */
public class Rule implements Comparable<Rule>, Serializable {

    /**
     * 规则ID，全局唯一
     */
    private String id;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 协议
     */
    private String protocol;

    /**
     * 后端服务ID
     */
    private String serviceId;

    /**
     * 请求前缀
     */
    private String prefix;

    /**
     * 路径集合
     */
    private List<String> paths;

    /**
     * 规则排序，对应场景：一个路径对应多条规则，然后只执行一条规则的情况
     */
    private Integer order;

    private Set<FilterConfig> filterConfigs =new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Set<FilterConfig> getFilterConfigs() {
        return filterConfigs;
    }

    public void setFilterConfigs(Set<FilterConfig> filterConfigs) {
        this.filterConfigs = filterConfigs;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public Rule(){
        super();
    }

    public Rule(String id, String name, String protocol, String serviceId, String prefix, List<String> paths, Integer order, Set<FilterConfig> filterConfigs) {
        this.id = id;
        this.name = name;
        this.protocol = protocol;
        this.serviceId = serviceId;
        this.prefix = prefix;
        this.paths = paths;
        this.order = order;
        this.filterConfigs = filterConfigs;
    }


    public static class FilterConfig{

        /**
         * 过滤器唯一ID
         */
        private String id;
        /**
         * 过滤器规则描述，{"timeOut":500,"balance":random}
         */
        private String config;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getConfig() {
            return config;
        }

        public void setConfig(String config) {
            this.config = config;
        }

        @Override
        public boolean equals(Object o){
            if (this == o) {
                return true;
            }

            if((o == null) || getClass() != o.getClass()){
                return false;
            }

            FilterConfig that =(FilterConfig)o;
            return id.equals(that.id);
        }

        @Override
        public int hashCode(){
            return Objects.hash(id);
        }
    }

    /**
     * 向规则里面添加过滤器
     * @param filterConfig
     * @return
     */
     public boolean addFilterConfig(FilterConfig filterConfig){
        return filterConfigs.add(filterConfig);
     }

    /**
     * 通过一个指定的FilterID获取FilterConfig
     * @param id
     * @return
     */
     public FilterConfig getFilterConfig(String id){
         for(FilterConfig config : filterConfigs){
             if(config.getId().equalsIgnoreCase(id)){
                return config;
             }
         }
         return null;
     }

    /**
     * 根据 filterID 判断当前 Rule 是否存在
     * @return
     */
    public boolean hasId(String id) {
        for(FilterConfig filterConfig : filterConfigs) {
            if(filterConfig.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Rule o) {
        int orderCompare = Integer.compare(getOrder(),o.getOrder());
        if(orderCompare == 0){
          return getId().compareTo(o.getId());
        }
        return orderCompare;
    }

    @Override
    public  boolean equals(Object o){
        if (this == o) {
            return  true;
        }

        if((o== null) || getClass() != o.getClass()){
            return false;
        }

        FilterConfig that =(FilterConfig)o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
}
