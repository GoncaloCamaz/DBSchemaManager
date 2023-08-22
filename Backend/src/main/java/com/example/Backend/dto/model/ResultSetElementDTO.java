package com.example.Backend.dto.model;

import java.util.HashMap;

/**
 * This object is created in order to send to the frontend application the rows from the query sent to the target
 * database.
 * Each row is identified by the column name (string)
 * So, considering the following columns: Name, Age, Job; the content of the HashMap would be: Name -> Jorge; Age -> 42; Job -> Engineer
 */
public class ResultSetElementDTO {
    private HashMap<String, Object> row;

    public ResultSetElementDTO() {
        this.row = new HashMap<>();
    }

    public void addRow(String columnname, Object value)
    {
        if(value == null)
        {
            String aux = "";
            this.row.put(columnname, aux);
        }
        else
        {
            this.row.put(columnname, value);
        }
    }

    public HashMap<String, Object> getRow() {
        return row;
    }

    public void setRow(HashMap<String, Object> row) {
        this.row = row;
    }
}
