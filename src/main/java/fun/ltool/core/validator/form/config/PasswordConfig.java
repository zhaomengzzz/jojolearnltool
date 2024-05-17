package fun.ltool.core.validator.form.config;

import lombok.Builder;
import lombok.Data;

/**
 * 密码验证配置项
 *
 * @author huangrongsong
 * @since 1.0
 */
@Data
@Builder
public class PasswordConfig {
    /**
     * 长度
     */
    private Integer len;
    /**
     * 最小长度
     */
    private Integer min;
    /**
     * 最大长度
     */
    private Integer max;
    /**
     * 是否包含字母
     */
    private Boolean containLetter;
    /**
     * 是否包含大小写
     */
    private Boolean containCaps;
    /**
     * 是否包含数字
     */
    private Boolean containNumber;
    /**
     * 是否包含符号
     */
    private Boolean containSymbol;
}
