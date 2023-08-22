package com.example.Fetcher.BackendConnection.PostMethodsBuilder;

import java.util.HashMap;

public interface Builder<T> {

    int update(HashMap<String,String> parameters, T t);

    int save(HashMap<String,String> parameters, T t);

    int delete(HashMap<String,String> parameters, T t);
}
