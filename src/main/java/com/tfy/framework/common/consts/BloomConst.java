package com.tfy.framework.common.consts;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

public class BloomConst {
    /*词霸用户id uid*/

    public static BloomFilter<Integer> dealIdBloomFilter = BloomFilter.create(new Funnel<Integer>() {
        private static final long serialVersionUID = 1L;

        @Override
        public void funnel(Integer arg0, PrimitiveSink arg1) {
            arg1.putInt(arg0);
        }
    }, 5000 * 10000);
}
