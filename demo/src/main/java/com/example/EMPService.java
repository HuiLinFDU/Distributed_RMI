/* NewAdd RMI Servier by Group11
 * [function] override the existent functions 
 */
package com.example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EMPService extends UnicastRemoteObject implements EMPInterface {
    private EMPDAO empDAO;
    
    protected EMPService() throws RemoteException {
        super();
        empDAO = new EMPDAO();
    }

    @Override
    public EMP findEmployeeById(String eno) throws RemoteException, SQLException {
        //P2upd: manual commit 
        //return empDAO.findEmployeeById(eno);
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);             
            EMP emp = empDAO.findEmployeeById(conn,eno);           
            conn.commit(); 
            return emp;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException ex) {
                    System.err.println("[Exception] Rollback failed: " + ex.getMessage());
                }
            }
            throw new RemoteException("[Exception] Find Employee failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); 
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("[Exception] Close Connection failed: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public int addNewEmployee(String eno, String ename, String title) throws RemoteException, SQLException {
        //P2upd: manual commit
        //return empDAO.addNewEmployee(eno, ename, title);
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 
            // check if employee already existed
            if (empDAO.findEmployeeById(conn,eno) != null) {
                throw new SQLException("[Warning] Employee alread existed: " + eno);
            }            
            int result = empDAO.addNewEmployee(conn,eno, ename, title);
            conn.commit(); 
            return result;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException ex) {
                    System.err.println("[Exception] Rollback failed: " + ex.getMessage());
                }
            }
            throw new RemoteException("[Exception] Add new employee failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); 
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("[Exceoption] Close Connection failed: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public int updateEmployee(String eno, String ename, String title) throws RemoteException, SQLException {
        //P2upd: manual commit
        //return empDAO.updateEmployee(eno, ename, title);
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 
            // check if employee already existed
            if (empDAO.findEmployeeById(conn,eno) == null) {
                throw new SQLException("[Warning] Employee Not Existed: " + eno);
            }
            int result = empDAO.updateEmployee(conn,eno, ename, title);
            conn.commit(); 
            return result;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException ex) {
                    System.err.println("[Exception] Rollback failed: " + ex.getMessage());
                }
            }
            throw new RemoteException("[Exception] Update Employee failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); 
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("[Exception] Close Connection failed: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public int deleteEmployee(String eno) throws RemoteException, SQLException {
        //P2upd: manual commit
        //return empDAO.deleteEmployee(eno);
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 
            // check if employee already existed
            if (empDAO.findEmployeeById(conn,eno) == null) {
                throw new SQLException("[Warning] Employee Not Existed: " + eno);
            }
            int result = empDAO.deleteEmployee(conn,eno);
            conn.commit(); 
            return result;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException ex) {
                    System.err.println("[Exception] Rollback failed: " + ex.getMessage());
                }
            }
            throw new RemoteException("[Exception] Delete Employee failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); 
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("[Exception] Close Connection failed: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public List<EMP> getAllEmployees() throws RemoteException, SQLException {
        //P2upd: manual commit
        //return empDAO.getAllEmployees();
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);             
            List<EMP> employees = empDAO.getAllEmployees(conn);
            conn.commit(); 
            return employees;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException ex) {
                    System.err.println("[Exception] Rollback failed: " + ex.getMessage());
                }
            }
            throw new RemoteException("[Exception] List Employees failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); 
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("[Exception] Close Connection failed: " + e.getMessage());
                }
            }
        }
    }
}