package com.example.man.smartshop.DBclass;

public class DBlocation {
    public String DBURL = "http://192.168.0.105:8080/ss/php/SSselect.php";
    public String INSERT = "http://192.168.0.105:8080/ss/php/insert.php";
    public String DELETE = "http://192.168.0.105:8080/ss/php/delete.php";
    public String UPDATE = "http://192.168.0.105:8080/ss/php/update.php";

    public String GetURL() {
        return DBURL;
    }
    public String GetInsertURL() {
        return INSERT;
    }
    public String GETDELETEURL() {
        return DELETE;
    }
    public String GETUPDATEURL() {
        return UPDATE;
    }
}