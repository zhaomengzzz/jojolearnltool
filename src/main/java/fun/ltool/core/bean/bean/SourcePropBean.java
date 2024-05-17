package fun.ltool.core.bean.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 源对象属性和值
 *
 * @author huangrongsong
 * @since 1.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SourcePropBean {
    private String propertyName;
    private Object value;
}
