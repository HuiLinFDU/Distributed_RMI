package com.example;

import java.util.List;
import java.util.Scanner;
import java.sql.SQLException;
//import rmi package by Group11
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * This is the app main class
 * It uses the EMPDAO class to get the data from the database
 * 
 */
public class EmpDBConsoleApp {
    static Scanner scanner = new Scanner(System.in);
    static EMPDAO empDAO = new EMPDAO();

    //declare remote service for emp  by Group11
    static EMPInterface empService;

    // ANSI escape code constants for text colors
    static final String RESET = "\u001B[0m";
    static final String GREEN = "\u001B[32m";
    static final String RED = "\u001B[31m";

    // Application main
    public static void main(String[] args) {
        try {
            //get registry for RMI in local host testing environment by Group11
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            
            //search remote service  by Group11
            empService = (EMPInterface) registry.lookup("EMPService");
            
            String choice = "";
            while (!choice.equals("6")) {
                System.out.println("""

                        Employee Database System (RMI Version)
                        =================================================================
                        1. Show All Employees
                        2. Find an employee by ID
                        3. Add a new employee
                        4. Delete an employee
                        5. Update an employee
                        6. Exit
                        =================================================================
                        Enter your choice: """);
                choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> showAllEmployees();
                    case "2" -> findEmployeeByNo();
                    case "3" -> addNewEmployee();
                    case "4" -> deleteEmployee();
                    case "5" -> updateEmployee();
                    case "6" -> System.exit(0);
                    default -> System.out.println("[*** Warning ***] Please enter a choice between 1 and 6");
                }
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
    public static void showAllEmployees() throws SQLException, RemoteException {
        // get all employees using the DAO

        // use RMI instead of DAO by Group11
        //List<EMP> empList = empDAO.getAllEmployees();
        List<EMP> empList = empService.getAllEmployees();
        // iterate over the empList and print
        for (EMP emp : empList) {
            System.out.println(GREEN + emp + RESET);
        }
    }

    public static void findEmployeeByNo() throws SQLException, RemoteException {
        System.out.println("Enter employee No. to find:");
        String eno = scanner.nextLine();

        // use RMI instead of DAO by Group11
        //EMP emp = empDAO.findEmployeeById(eno);
        EMP emp = empService.findEmployeeById(eno);
        if (emp != null) {
            System.out.println(GREEN + emp + RESET);
        } else {
            System.out.println(RED + "No employee with No.: " + eno + RESET);
        }
    }

    public static void addNewEmployee() throws SQLException, RemoteException {
        System.out.println("Enter new employee No.:");
        String eno = scanner.nextLine();

    // check if employee number already existed
        EMP existingEmp = empService.findEmployeeById(eno);
        if (existingEmp != null) {
            System.out.println(RED + "Error: Employee with No. " + eno + " already exists!" + RESET);
            return;
        }

        System.out.println("Enter new employee name:");
        String ename = scanner.nextLine();

        System.out.println("Enter new employee title:");
        String title = scanner.nextLine();

        // use RMI instead of DAO by Group11
        //int addStatus = empDAO.addNewEmployee(eno, ename, title);
        int addStatus = empService.addNewEmployee(eno, ename, title);
        if (addStatus == 1) {
            System.out.println(GREEN + eno + " " + ename + " " + title + " Added successfully" + RESET);
        } else {
            System.out.println(RED + "Error adding new employee " + eno + " " + ename + RESET);
        }
    }

    public static void deleteEmployee() throws SQLException, RemoteException {
        System.out.println("Enter employee No. to delete:");
        String eno = scanner.nextLine();

        // use RMI instead of DAO by Group11
        //if (empDAO.findEmployeeById(eno) == null) {
        if (empService.findEmployeeById(eno) == null) {
                System.out.println(RED + "No employee with No.: " + eno + RESET);
            return;
        }

        // use RMI instead of DAO by Group11
        //int delStatus = empDAO.deleteEmployee(eno);
        int delStatus = empService.deleteEmployee(eno);
        if (delStatus == 1) {
            System.out.println(GREEN + eno + " deleted successfully" + RESET);
        } else {
            System.out.println(RED + "Error deleting employee " + eno + RESET);
        }
    }

    public static void updateEmployee() throws SQLException, RemoteException {
        System.out.println("Enter employee No. to update:");
        String eno = scanner.nextLine();

        // use RMI instead of DAO by Group11
        //if (empDAO.findEmployeeById(eno) == null) {
        if (empService.findEmployeeById(eno) == null) {
                System.out.println(RED + "No employee with No.: " + eno + RESET);
            return;
        }

        System.out.println("Enter new employee name:");
        String ename = scanner.nextLine();

        System.out.println("Enter new employee title:");
        String title = scanner.nextLine();

        // use RMI instead of DAO by Group11
        //int updateStatus = empDAO.updateEmployee(eno, ename, title);
        int updateStatus = empService.updateEmployee(eno, ename, title);

        if (updateStatus == 1) {
            System.out.println(GREEN + eno + " " + ename + " updated successfully" + RESET);
        } else {
            System.out.println(RED + "Error updating " + eno + " " + ename + RESET);
        }
    }

}
