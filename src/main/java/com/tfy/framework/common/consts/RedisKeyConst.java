package com.tfy.framework.common.consts;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.concurrent.TimeUnit;

/**
 * 在这里统一存放redis key的相关信息
 */
@AllArgsConstructor
@Getter
public class RedisKeyConst {

    interface BaseEnum {
        String getRedisKeyFormat();

        TimeUnit getTimeUnit();

        Integer getTimeValue();

        default KeyVo keyVo(Object... objs) {
            return KeyVo.of(this, objs);
        }
    }

    /**
     * 优惠券
     */
    @AllArgsConstructor
    @Getter
    public enum ExampleEnum implements BaseEnum {
        /*example*/
        USER_DAY_RECEIVE_LIMIT("EXAMPLE", TimeUnit.SECONDS, 24 * 60 * 60);
        private String redisKeyFormat;
        private TimeUnit timeUnit;
        private Integer timeValue;
    }

    @Setter
    @Getter
    public static class KeyVo {
        private String redisKey;
        private TimeUnit timeUnit;
        private Integer timeValue;

        public static KeyVo of(BaseEnum baseEnum, Object... objs) {
            String redisKey = ArrayUtils.isEmpty(objs) ? baseEnum.getRedisKeyFormat() : String.format(baseEnum.getRedisKeyFormat(), objs);
            KeyVo vo = new KeyVo();
            vo.setRedisKey(redisKey);
            vo.setTimeUnit(baseEnum.getTimeUnit());
            vo.setTimeValue(baseEnum.getTimeValue());
            return vo;
        }
    }
}
