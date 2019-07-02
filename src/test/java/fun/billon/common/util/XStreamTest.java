package fun.billon.common.util;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.junit.Test;

/**
 * xstream测试,javabean<->xml
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class XStreamTest {

    @XStreamAlias("person")
    private static class Person {
        @XStreamAlias("n")
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person [name=" + name + ", age=" + age + "]";
        }
    }

    @XStreamAlias("stu")
    private static class Student extends Person {
        @XStreamAlias("address")
        private String address;

        public Student(String name, int age, String address) {
            super(name, age);
            this.address = address;
        }

        @Override
        public String toString() {
            return super.toString() + "Student{" +
                    "address='" + address + '\'' +
                    '}';
        }
    }


    @Test
    public void test() {
        Person bean = new Person("张三", 19);
        //XML序列化
        String xml = XStreamUtils.toXml(bean);
        System.out.println(xml);

        //XML反序列化
        bean = XStreamUtils.fromXml(xml, Person.class);
        System.out.println(bean);

        Student student = new Student("学生", 20, "北京");
        //XML序列化
        xml = XStreamUtils.toXml(student);
        System.out.println(xml);

        //XML反序列化
        student = XStreamUtils.fromXml(xml, Student.class);
        System.out.println(student);
    }
}