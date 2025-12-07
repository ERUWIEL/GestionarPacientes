
package com.mycompany.gestionarpacientes;

import com.mycompany.gestionarpacientes.util.JpaUtil;

/**
 *
 * @author gatog
 */
public class App {

    public static void main(String[] args) {
        JpaUtil.getEntityManager();
        System.out.println("iniciando y cerrando!");
        JpaUtil.shutdown();
    }
}
