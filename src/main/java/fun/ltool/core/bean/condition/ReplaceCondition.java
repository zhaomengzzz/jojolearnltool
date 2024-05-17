package fun.ltool.core.bean.condition;

import fun.ltool.core.bean.bean.SourcePropBean;
import fun.ltool.core.exceptions.BeanException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 替换自定义属性条件构造器
 *
 * @author huangrongsong
 * @since 1.1
 */
public class ReplaceCondition {
    /**
     * ReplaceAll方法 调用后保存属性名称数组
     */
    protected static List<String> CONDITION_REPLACE_ALL_PROPERTY_NAMES = new ArrayList<>();
    /**
     * ReplaceAll方法 调用后保存属性值数组
     */
    protected static List<SourcePropBean> CONDITION_REPLACE_ALL_VALUES = new ArrayList<>();

    /**
     * 获取条件执行结果map
     *
     * @return 返回结果map
     */
    public Map<String, SourcePropBean> getReplaceCondition() {
        Map<String, SourcePropBean> replaceCondition = new HashMap<>();
        if (CONDITION_REPLACE_ALL_PROPERTY_NAMES.size() != CONDITION_REPLACE_ALL_VALUES.size()) {
            throw new BeanException("replaceAll方法目标属性和源属性数量不匹配请检查");
        }
        for (int i = 0; i < CONDITION_REPLACE_ALL_PROPERTY_NAMES.size(); i++) {
            String propertyName = CONDITION_REPLACE_ALL_PROPERTY_NAMES.get(i);
            replaceCondition.put(propertyName, CONDITION_REPLACE_ALL_VALUES.get(i));
        }
        return replaceCondition;
    }

    /**
     * 清理条件 避免影响下次调用
     */
    public void clearCondition() {
        CONDITION_REPLACE_ALL_PROPERTY_NAMES.clear();
        CONDITION_REPLACE_ALL_VALUES.clear();
    }
}
