package bean;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class SourceBean extends SBean {
    private Integer id;
    private String name;
    private Integer age;
    private int height;
    private int weight;
    private String gender;
    private String address;
}
