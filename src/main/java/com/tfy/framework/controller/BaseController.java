package com.tfy.framework.controller;

import com.tfy.framework.common.utils.G;

public abstract class BaseController {

    public Object ok(Object data) {
        return G.ok(data);
    }
}