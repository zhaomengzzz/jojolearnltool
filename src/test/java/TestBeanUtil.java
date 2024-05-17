import bean.SBean;
import bean.SourceBean;
import bean.TBean;
import fun.ltool.core.bean.BeanUtil;
import fun.ltool.core.validator.form.GroupGetterMultiple;
import fun.ltool.core.validator.form.ValidatorForm;
import fun.ltool.core.validator.form.bean.ValidateResult;

import java.util.Map;

public class TestBeanUtil {
    public static void main(String[] args) {

        // bean 复制
        SourceBean sourceBean = SourceBean.builder().id(22).address("sdds").userName("sname").name("test").height(400).weight(200).build();
        long startTime = System.currentTimeMillis();
        TBean target = new TBean();
        BeanUtil.buildBean(target, sourceBean).condition(
                c -> c.replaceAll(r -> r
                        .targetProp(TBean::getTAddress)
                        .sourceProp(SourceBean::getAddress)
                ),
                c -> c.replace(TBean::getTName, SourceBean::getName)
        ).build();
        System.out.println("用时:" + (System.currentTimeMillis() - startTime) + "ms target:  " + target);
        startTime = System.currentTimeMillis();
        // bean复制 自动实例化
        TBean target2 = BeanUtil.buildBean(TBean.class, sourceBean).condition(
                c -> c.replaceAll(r -> r
                        .targetProp(TBean::getTName)
                        .sourceProp(SourceBean::getName)
                )
        ).build();
        System.out.println("用时:" + (System.currentTimeMillis() - startTime) + "ms target2: " + target2);
        startTime = System.currentTimeMillis();
        // bean复制到 map
        Map<String, Object> map = BeanUtil.buildMap(sourceBean).condition(c -> c
                .replaceAll(r -> r
                        .targetProp("tName", "user")
                        .sourceProp(SourceBean::getName, SBean::getUserName)
                ).replace("fId", SourceBean::getId)
        ).build();
        System.out.println("用时:" + (System.currentTimeMillis() - startTime) + "ms map: " + map);
        startTime = System.currentTimeMillis();
        // 表单验证
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
