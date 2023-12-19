/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author DAM_M
 */
public class ConexionConBDD {

    private final String NameDataBase = "";
    private final String User = "root";
    private final String Password = "root";

    public void Conexion() {
        try ( Connection conexion = DriverManager.getConnection(NameDataBase, User, Password)) {
            // TODO
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
