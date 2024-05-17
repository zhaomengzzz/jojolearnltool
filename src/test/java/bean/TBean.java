package bean;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class TBean extends SBean {
    private static final String BEAN_NAME="TBean";
    private Integer id;
    private String tName;
    private Integer age;
    private int height;
    private int weight;
    private String gender;
    private String tAddress;
}
