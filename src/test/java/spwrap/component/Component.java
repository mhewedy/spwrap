package spwrap.component;

import spwrap.mappers.ResultSetMapper;
import spwrap.result.Result;

public class Component implements ResultSetMapper<Component> {

    private String field0, field1, field2, field3, field4, field5, field6, field7, field8, field9;

    public Component(){}

    public Component(String field0, String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9) {
        this.field0 = field0;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
        this.field5 = field5;
        this.field6 = field6;
        this.field7 = field7;
        this.field8 = field8;
        this.field9 = field9;
    }

    public Component map(Result<?> result) {
        return new Component(
                result.getString(1),
                result.getString(2),
                result.getString(3),
                result.getString(4),
                result.getString(5),
                result.getString(6),
                result.getString(7),
                result.getString(8),
                result.getString(9),
                result.getString(10)
                );
    }

    @Override
    public String toString() {
        return "Component{" +
                "field0='" + field0 + '\'' +
                ", field1='" + field1 + '\'' +
                ", field2='" + field2 + '\'' +
                ", field3='" + field3 + '\'' +
                ", field4='" + field4 + '\'' +
                ", field5='" + field5 + '\'' +
                ", field6='" + field6 + '\'' +
                ", field7='" + field7 + '\'' +
                ", field8='" + field8 + '\'' +
                ", field9='" + field9 + '\'' +
                '}';
    }
}
