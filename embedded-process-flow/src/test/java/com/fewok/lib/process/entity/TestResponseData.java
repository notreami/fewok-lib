package com.fewok.lib.process.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author notreami on 18/7/7.
 */
@Data
public class TestResponseData {
    private Map<String, Object> stringObjectMap = new HashMap<>(20);
}
