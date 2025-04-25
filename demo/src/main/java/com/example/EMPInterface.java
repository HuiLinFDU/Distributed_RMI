/* NewAdd RMI interfaces delaration by Group11
 * [function] keep the basical AUDI function as before 
 */
package com.example;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface EMPInterface extends Remote {
    EMP findEmployeeById(String eno) throws RemoteException, SQLException;
    int addNewEmployee(String eno, String ename, String title) throws RemoteException, SQLException;
    int updateEmployee(String eno, String ename, String title) throws RemoteException, SQLException;
    int deleteEmployee(String eno) throws RemoteException, SQLException;
    List<EMP> getAllEmployees() throws RemoteException, SQLException;
}