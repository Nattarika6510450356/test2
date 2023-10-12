package cs211.project.controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DataContainer implements Serializable {
    private Map<String, Object> data = new HashMap<>();

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }
}
