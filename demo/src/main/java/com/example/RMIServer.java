/* NewAdd RMI Servier by Group11
 * [function] creating registry, building remote instance and binding 
 */
package com.example;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            // create the RMI registry with default port  by Group11
            Registry registry = LocateRegistry.createRegistry(1099);
            
            // create the instance of a remote object by Group11
            EMPInterface empService = new EMPService();
            
            // Bind the remote object to the registry by Group11
            registry.rebind("EMPService", empService);
            
            System.out.println("EMP RMI Server is running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}