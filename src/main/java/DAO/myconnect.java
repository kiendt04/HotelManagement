/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author ADMIN
 */
public class myconnect {
    private String host ;
    private String user ;
    private String pass;
    private String url ;
    Connection conn ;
    public myconnect(){
                host = "localhost";
                user = "root";
                pass = "";
                url = "jdbc:mysql://localhost:3306/qlyksan?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    }
//    public myconnect(){
//                host = "localhost";
//                user = "root";
//                pass = "root";
//                url = "jdbc:mysql://" + host + ":3306/" + "qlyksan";
//    
//    }
    public myconnect(String host , String user , String pass , String url){
        this.host= host ;
        this.user= user ;
        this.pass= pass ;
        this.url= url ;
    }
    public Connection getConnection(){
        try{
            conn= DriverManager.getConnection(url,user,pass);
            if(conn !=null){
                return conn;
            }
        }catch(Exception e){
        }
        return conn;
    }
}
