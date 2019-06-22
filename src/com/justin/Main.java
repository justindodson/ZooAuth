package com.justin;

import com.justin.utils.MD5Hash;
import com.sun.xml.internal.bind.v2.TODO;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception{

        Scanner in = new Scanner(System.in);
        String projectPath;
        String resourcesPath = "/src/com/justin/resources/";

        // setup variables
        String username = null;
        String password = null;
        File credentialsFile;
        boolean exit = false;
        boolean loggedIn = false;
        int logInAttemptCounter = 0;

        // this is to set up the project path to access all of the resource files
        // provide your project path to the program and it will gather the files
        // from the class path
        projectPath = getProjectPath();
        credentialsFile = new File(projectPath + resourcesPath + "credentials.txt");


        // show the loadWelcomeScreen - the loadWelcomeScreen takes the project path and appends the ascii art for the program
        // you can comment out this line if you do not want to use the ascii art. It will still function
        // without this line.
        loadWelcomeScreen(projectPath);

        while (logInAttemptCounter < 3 && !exit) {
            int selection;
            Scanner input = new Scanner(System.in);

            if (loggedIn) {
                System.out.printf("\nWelcome %s. What do you want to do?\n1 - View Credentials File\n2 - Logout\n", username);
                selection = input.nextInt();
                //TODO: convert this to a method call based on the user being logged in.
                switch (selection) {
                    case 1:
                        loadCredentialsDocument(username, password, credentialsFile, projectPath+resourcesPath);
                        break;
                    case 2:
                        loggedIn = false;
                        logInAttemptCounter = 0;
                        break;
                    default:
                        System.out.println("Invalid Menu Selection.");
                        break;
                }
            } else {

                //TODO: convert this to a method  call based on the user NOT being logged in.
                System.out.println("\nSelect an option from the loadWelcomeScreen:\n1 - Log In\n2 - Exit Program");
                selection = input.nextInt();

                switch (selection) {
                    case 1:
                        System.out.print("\nEnter Username: ");
                        username = in.next();
                        in.nextLine();

                        System.out.print("Enter Password: ");
                        password = in.nextLine();

                        if (validateLogin(username, password, credentialsFile)) {
                            loggedIn = true;
                            System.out.println("Valid Login");
                        } else {
                            System.out.println("\n#####################################\n" +
                                    "Invalid Login Credentials. Try Again.\n" +
                                    "#####################################");
                        }
                        logInAttemptCounter++;

                        if (logInAttemptCounter == 3) {
                            System.out.println("You have reached the max amount of login attempts. Exiting program...");
                            System.exit(0);
                        }
                        break;
                    case 2:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid Menu Selection.");
                        break;
                }
            }
        }

    }

    // Private Methods used by the main class

    /*   Get the project folder path from the user
         This is used to setup the correct filepath so the resources load in the correct path
         This could have been done better but for ease of portability you just enter the path to
         where the ZooAuthSystem lives in your file system.
    */
    private static String getProjectPath(){
        Scanner in = new Scanner(System.in);
        String path;

        System.out.println("Enter the project folder path (ex: C:/Users/<username>/<somefolder>/ZooAuthSystem): ");
        path = in.nextLine();
        return path;
    }

    // This method loads the welcome screen and displays the ASCII art
    private static void loadWelcomeScreen(String projectPath) throws FileNotFoundException {
        Scanner in = new Scanner(new File(projectPath + "/src/com/justin/resources/art.txt"));
        while(in.hasNextLine()){
            System.out.println(in.nextLine());
        }
    }

    //TODO: Create login menu

    //TODO: Create user menu


    private static void loadCredentialsDocument(String username, String password, File credentialsFile, String filePath) throws Exception{
        String role = "";
        String hash = hashPassword(password);
        try {
            ArrayList<String> credLines = parseFile(credentialsFile);

            for(String line : credLines){
                if(line.contains(username) && line.contains(hash)){
//                    role = (line.split(" ")[]);
                    String[] words = line.split("\\s+");
                    role = words[words.length - 1];
                }
            }
            String path = filePath + "/" + role + ".txt";
            outputDocumentContents(path);

        }catch (Exception e) {
            System.out.printf("File %s Not Found\n", credentialsFile.toString());
            e.printStackTrace();
        }
    }

    private static void outputDocumentContents(String path) throws Exception{
        Scanner fileScanner = new Scanner(new File(path));
        while(fileScanner.hasNextLine()){
            System.out.println(fileScanner.nextLine());
        }
    }


    private static boolean validateLogin(String username, String password, File credentialsFile) throws Exception{
        boolean validLogin = false;
        String hash = hashPassword(password);

        ArrayList<String> credentialLines = parseFile(credentialsFile);

        for (String line : credentialLines) {
            if(line.contains(username)){
                if(line.contains(hash)){
                    validLogin = true;
                }
            }
        }
        return validLogin;
    }


    private static String hashPassword(String password) throws Exception{
        MD5Hash hash = new MD5Hash(password);
        return hash.getHashString();
    }

    private static ArrayList<String> parseFile(File credentialsFile) throws Exception{
        Scanner fileScanner = new Scanner(credentialsFile);
        ArrayList<String> credentialLines = new ArrayList<>();
        while (fileScanner.hasNextLine()){
            credentialLines.add(fileScanner.nextLine());
        }
        return credentialLines;
    }
}

