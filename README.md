# Zerli-Flower-App
I developed a system with several friens called "Zerli" during my middle-year project in software engineering studies.
Zerli proggramed in Java with a GUI built using Scene Builder involves combining Java code with FXML files generated by Scene Builder.
For the database we used MySQL workbench

## Summary 
The app will allow customers to browse the chain's product range, select a ready-made bouquet of flowers or a potted plant, or create a custom bouquet, and order delivery. The system includes a component called User Portal, which is an online interface through which system users can perform relevant operations.

## Explain on Files
### Docs
1. ToDoList Files: We created comprehensive ToDoLists with progress and management tasks for the beginning and middle stages of the project.
2. Visual Paradigm Files: We generated Use Case, Class, Sequence, and Activity diagrams using Visual Paradigm.
3. Package Diagrams: These diagrams depict the architecture of all project packages.

### Testing Zerli Project
We created several testing in progress to check our system
1. Acceptance Tests: Verify if the final system meets real-world user needs and business requirements.
2. Unit Testing: Ensure individual code components (functions, classes) work as intended in isolation.
3. Functional Testing: Confirm individual features and functionalities of the application function correctly according to specifications.
In addition, there are Data.sql and README.txt files with Unit Testing and Functional Testing Notes (used Data.sql as the database in testing)

### PROJECT jar Files + Database
1. G6_client.jar & G6_server.jar - jar files to run the project
In addition, there are Database.sql and zerli.txt files with the initial database and summary of all the data

###  PROJECT Code (with javadoc)
1. OCSF - Simplifying Cybersecurity Data, Open Cybersecurity Schema Framework, is an open-source effort standardizing how security data is organized
2. ZerliClientApp - The java code for client side
3. ZerliServerApp - The java code for server side
In addition, there are Database.sql and Database summary zerli.txt files with the initial database and summary of all the data and (Explain on order and reports screens).pptx 

## Running the Zerli App

### Using a Java IDE:

1.Create a Folder and Load Projects:
-Create a folder named "midproject".
-Load the following folders into it: OCSF, ZerliClientApp, and ZerliServerApp.
-Use a Java IDE of your choice (e.g., Eclipse IDE).

2. Load Database:
    - Import the Database.sql file into MySQL Workbench.
    - Update SQL Connection:
        In ZerliServerApp, navigate to src/server/ServerConnSQL.java and Locate the startConn function (line 50).
        Modify the SQL connection string within the following line:
        ```java
        Javaconn = DriverManager.getConnection("jdbc:mysql://localhost/midproject?useLegacyDatetimeCode=false&serverTimezone=Israel","root",mySQLpassword);
        ```
    - Replace the placeholders with your actual MySQL Workbench parameters (link, username, and password).

3. Run the Server and Client:
- Run the ClientApp class within the client/ package of ZerliClientApp.
- Run the ServerApp class within the server/ package of ZerliServerApp (ensure the server runs first).

### Using JAR Files:

> [!IMPORTANT]
> Note: Running directly from JAR files is currently not possible due to a private SQL link.

1. Alternative: Follow Using a Java IDE steps 1-3 from the IDE instructions to create your own JAR files.
2. Load the database as instructed in Using a Java IDE step 2.
3. Run the generated JAR files in the appropriate order (server first, then client).
