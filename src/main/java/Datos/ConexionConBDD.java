/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import java.sql.*;

/**
 *
 * @author DAM_M
 */
public class ConexionConBDD {

    private final String NameDataBase = "BDD_HundirLaFlota";
    private final String User = "root";
    private final String Password = "root";
    private final String URL = "jdbc:mysql://locatehost:3306/" + NameDataBase;

    public Connection getConexion() {
        try ( Connection conexion = DriverManager.getConnection(URL, User, Password)) {
            return conexion;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
