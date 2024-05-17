import bean.TBean;
import fun.ltool.core.validator.form.GroupGetterMultiple;
import fun.ltool.core.validator.form.ValidatorForm;
import fun.ltool.core.validator.form.bean.ValidateResult;

public class TestValidatorFormUtil {
    public static void main(String[] args) {
        TBean target = TBean.builder().id(22).tName("323").userName("sname").height(400).weight(200).build();
        long startTime = System.currentTimeMillis();
        System.out.println("表单验证: " + ValidatorForm.buildForm(target)
                .field(TBean::getTName)
                .condition(c ->
                        c.email("weight email校检失败",TBean::getWeight)
                ).validate()
        );
        // 表单验证 分组验证
        ValidateResult validate = ValidatorForm.buildForm(target)
                .field()
                .condition(
                        c -> c.group(TBean::getTName,
                                g -> g.condition(
                                        gc -> gc.notEmpty(TBean::getAge, TBean::getWeight, TBean::getHeight)
                                )
                        ),
                        c -> c.group((GroupGetterMultiple<TBean, Object> gc) -> gc.property(TBean::getTName, TBean::getId),
                                g -> g.condition(
                                        gc -> gc.notEmpty(TBean::getAge, TBean::getWeight, TBean::getHeight)
                                )
                        ),
                        c -> c.group((GroupGetterMultiple<TBean, Object> gc) -> gc.condition(group -> group.notEmpty(TBean::getTName)),
                                g -> g.condition(
                                        gc -> gc.notEmpty(TBean::getAge, TBean::getWeight, TBean::getHeight)
                                )
                        )
                ).validate();
        System.out.printf("用时:" + (System.currentTimeMillis() - startTime) + "ms validate: " + validate.toString());
    }
}
