package fun.ltool.core.validator.form.bean;

import lombok.Builder;
import lombok.Data;

/**
 * 表单校检结果
 *
 * @author huangrongsong
 * @since 1.0
 */
@Data
@Builder
public class ValidateResult {
    /**
     * 验证结果
     */
    private Boolean isValid;
    /**
     * 验证信息
     */
    private String message;
}
