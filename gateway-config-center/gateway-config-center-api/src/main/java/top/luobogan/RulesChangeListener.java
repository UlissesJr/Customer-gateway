package top.luobogan;

import top.luobogan.config.Rule;
import java.util.List;

public interface RulesChangeListener {
    void onRulesChange(List<Rule> rules);
}
