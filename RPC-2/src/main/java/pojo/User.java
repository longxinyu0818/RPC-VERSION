package pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder // 自动生成类可实例化的代码
@Data // set,get,toString,equals等方法
@NoArgsConstructor //无参构造器
@AllArgsConstructor //所有的构造器，不包括无参
public class User {
    private Integer id;
    private String userName;
    private Boolean sex;
}
