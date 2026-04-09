=============================================
   LOST AND FOUND SYSTEM (NetBeans GUI XML)
=============================================

LAHAT NG .java FILES MAY KASAMANG .form XML FILE
Pwede mo i-open sa NetBeans DESIGN VIEW!

FILES:
  src/lostandfound/DBConnection.java        - MySQL connection
  src/lostandfound/Main.java                - Entry point
  src/lostandfound/LoginForm.java + .form   - Login (Design View ready)
  src/lostandfound/RegisterForm.java + .form - Register (Design View ready)
  src/lostandfound/ReporterDashboard.java + .form - CRUD (Design View ready)
  src/lostandfound/ClaimerDashboard.java + .form  - Claim (Design View ready)

SETUP:
  1. Run database.sql sa phpMyAdmin
  2. NetBeans: File -> New Project -> Java with Ant -> Java Application
     Package name: lostandfound
  3. Copy ALL files from src/lostandfound/ into your project's
     Source Packages -> lostandfound folder
     IMPORTANT: .java AND .form files must be TOGETHER in the same folder
  4. Right-click Libraries -> Add Library -> MySQL JDBC Driver
  5. Run (Main Class: lostandfound.Main)

DESIGN VIEW:
  Double-click any .java file with a .form -> click "Design" tab
  You can now drag-and-drop edit the GUI!

DEFAULT ACCOUNTS:
  Reporter: reporter1 / pass123
  Claimer:  claimer1  / pass123
=============================================
