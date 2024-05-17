import bean.TBean;
import fun.ltool.core.lang.reflect.FieldWrapper;
import fun.ltool.core.reflect.ReflectUtil;
import lombok.Data;

import java.util.Arrays;

@Data
public class TestReflectUtil {
    private int a;
    private Integer b;

    public TestReflectUtil() {
    }

    public TestReflectUtil(int a, Integer b) {
        this.a = a;
        this.b = b;
    }

    public static void main(String[] args) {
        // 处理实例
        TBean b = TBean.builder().id(1).userName("namess").age(18).weight(200).height(100).build();
        TestReflectUtil testReflectUtil = new TestReflectUtil();
        // ReflectUtil.buildClass(test).executor(null).build();
        System.out.println("getFieldValue: age=" + ReflectUtil.buildClass(b).getFieldValue("age"));
        System.out.println("getStaticFieldValue: BEAN_NAME=" + ReflectUtil.buildClass(b).getStaticFieldValue("BEAN_NAME"));

        ReflectUtil.buildClass(TestReflectUtil.class).newInstance(1, 2).executor(e -> e
                .fields(f -> f
                        .set("a", b::getHeight)
                        .set("b", () -> 3)
                        .get("b", v -> System.out.println("get： b=" + v))
                )
                .methods(m -> m
                        .method("setValue",
                                res -> System.out.println("setValue: " + res),
                                () -> 10,
                                () -> 3,
                                () -> FieldWrapper.valueNull(String.class)
                        )
                        .method("setValue2", res -> System.out.println("setValue2: " + res),
                                20, 4, FieldWrapper.valueNull(String.class)
                        )
                        .method("getValue", result -> System.out.println("getValue: " + result))
                        .method("msg", System.out::println)
                        .method("msg1", System.out::println)
                )
        ).build();

        // 处理类
        ReflectUtil.buildClass(TestReflectUtil.class).executor(e -> e
                .fields(f -> f
                        .set(testReflectUtil, "a", 12)
                        .get(testReflectUtil, "a", v -> System.out.println("class field a" + v))
                )
                .methods(m -> m.staticMethod("msg", System.out::println))
        ).build();

        // 循环赋值
        ReflectUtil.buildClass(testReflectUtil).executor(e -> e.fields(f -> Arrays.asList("a", "b").forEach(v -> f.set(v, 2)))).build();
        System.out.println("循环赋值" + testReflectUtil);
    }

    private static String msg1() {
        return "private static msg";
    }

    public static String msg() {
        return "static msg";
    }

    public int getValue() {
        return a + b;
    }

    public String setValue(int a, Integer b, String msg) {
        this.a = a;
        this.b = b;
        return "a:" + a + " b:" + b + "第三个参数： " + msg;
    }

    public String setValue2(int a, Integer b, String msg) {
        this.a = a;
        this.b = b;
        return "a:" + a + " b:" + b + "第三个参数： " + msg;
    }
}
