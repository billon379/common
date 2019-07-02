package fun.billon.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.Test;

/**
 * FastJson测试
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class FastJsonTest {

    @Data
    @EqualsAndHashCode(callSuper = false)
    class A {
        @JSONField(serialize = false)
        private String name;
        @JSONField(serialize = false)
        private int age;

        public A(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    class B extends A {
        private String address;

        public B(String name, int age, String address) {
            super(name, age);
            this.address = address;
        }
    }

    @Test
    public void testFastJsonDeserialize() {
        B b = new B("zhangsan", 10, "北京市");
        System.out.println(JSONObject.toJSONString(b));
    }
}
