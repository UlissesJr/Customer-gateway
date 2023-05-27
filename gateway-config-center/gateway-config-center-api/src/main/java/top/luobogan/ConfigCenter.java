package top.luobogan;

/**
 * Created by LuoboGan
 * Date:2023-05-27
 */
public interface ConfigCenter {

    void init(String serverAddr, String env);

    void subscribeRulesChange(RulesChangeListener listener);

}
