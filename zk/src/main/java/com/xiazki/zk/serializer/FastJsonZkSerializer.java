package com.xiazki.zk.serializer;

import com.alibaba.fastjson.JSON;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

/**
 * @author xiang.
 * @date 2018/3/6
 */
public class FastJsonZkSerializer implements ZkSerializer {

    @Override
    public byte[] serialize(Object data) throws ZkMarshallingError {
        return JSON.toJSONBytes(data);
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        return JSON.parse(bytes);
    }
}
