package com.example.Backend.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * This object is the result of sending a query (view creation / free querying) to a database.
 * If the query is valid, validQuery will be true
 * The columns are aggregated in columns and each row is transformed on a ResultSetElementDTO and added to the list
 */

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ResultSetDTO {

    /**
     * If query is valid
     */
    private boolean validQuery;

    /**
     * To define name of query or name of view
     */
    private String queryName;

    /**
     * String containing error message
     */
    private String errormessage;

    /**
     * String containing warning messages
     */
    private String warningmessage;

    /**
     * List with the name of the columns retrieved
     */
    private List<String> columns;

    /**
     * Object representing each row
     */
    private List<ResultSetElementDTO> rows;

    public ResultSetDTO() {
        this.validQuery = false;
        this.queryName = "";
        this.errormessage = "";
        this.warningmessage = "";
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    public ResultSetDTO(boolean valid, String name, List<String> columns, List<ResultSetElementDTO> rows, String error, String warningmessage) {
        this.validQuery = valid;
        this.queryName = name;
        this.columns = columns;
        this.warningmessage = warningmessage;
        this.rows = rows;
        this.errormessage = error;
    }

    public boolean isValidQuery() {
        return validQuery;
    }

    public void setValidQuery(boolean validQuery) {
        this.validQuery = validQuery;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public void setRows(List<ResultSetElementDTO> rows) {
        this.rows = rows;
    }

    public List<ResultSetElementDTO> getRows()
    {
        return this.rows;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public String getWarningmessage() {
        return warningmessage;
    }

    public void setWarningmessage(String warningmessage) {
        this.warningmessage = warningmessage;
    }
}