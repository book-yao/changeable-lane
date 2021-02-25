package com.supcon.changeablelane.constant;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 枚举序列化类
 * @author JWF
 * @date 2019/7/18
 */
public class EnumSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer jsonSerializer, Object o, Object o1, Type type, int i) throws IOException {
        SerializeWriter out = jsonSerializer.out;

        if(o instanceof DisplayedEnum) {
            out.writeInt(((DisplayedEnum) o).getId());
        }else if(o instanceof Enum){
            out.writeEnum((Enum<?>) o);
        }else {
            out.write(o.toString());
        }
    }
}
