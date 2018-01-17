# SUTD-bank Webapp - Installation Notes

Set up to run in eclipse
1. Check out source code at https://github.com/sunjun-group/bankwebapp.  Here is the project structure:
	- /app [source code]
	  	+ src/main/java:  	[java code]
		+ src/main/resources: [configuration for database and email]
			+ database.properties
			+ email.properties
			+ create.sql: mysql script to create tables. 
	- /etc	[configuration/sql script]
		+ server.xml: configuration for tomcat server
	
2. Prepare mysql server and create tables
	- [Require] Install mysql by following instructions below:
		+ For OS X: https://dev.mysql.com/doc/refman/5.7/en/osx-installation-pkg.html.
		+ For Windows: https://dev.mysql.com/doc/workbench/en/wb-installing-windows.html.
	- [Optional] install mysql client application:ex: MySql Workbench (https://dev.mysql.com/downloads/workbench/) 

3. Prepare eclipse
	- Make sure that m2e plugin is installed (should already be included by default in newest eclipse version) .
		+ Go to Help/About Eclipse/Installation Details. If it was installed, you must see "m2e-Maven Integration For Eclipse" on the list.
		+ To install m2e plugin: 
			Go to Help/Install New Software, parse "http://download.eclipse.org/technology/m2e/releases" to text field "Work with". 
			In displayed table, select "Maven Integration For Eclipse", click "Next" --> ... --> "Finish"
	- Install tomcat server
		+ The instruction can be found at: http://crunchify.com/step-by-step-guide-to-setup-and-install-apache-tomcat-server-in-eclipse-development-environment-ide/
			- Download and extract Apache Tomcat. (at http://tomcat.apache.org/download-80.cgi)
			- Open Servers view (Window/Show view/Servers). In Servers view, right click, select New->Server, and follow the instruction above.
	- Configure server for sutdbank 	
		+ After finishing those steps, there must be a project folder named Servers created in your workspace.
		+ Under project Servers, open server.xml file, and add Realm configuration at the end of the file: [inside  <Engine> tag]
		example: (modify according to your local database configuration)
		 
			<Realm className="org.apache.catalina.realm.JDBCRealm"
			driverName="org.gjt.mm.mysql.Driver"
			connectionURL="jdbc:mysql://localhost:3306/bankwebapp"
			connectionName="root" connectionPassword="mysql@2017"
			userTable="user" userNameCol="user_name" userCredCol="password"
			userRoleTable="user_role" roleNameCol="role" />
		
4. Import project into eclipse
	Prerequisite: Eclipse with m2e plugin, mysql server, web server (tomcat)
	- Click on File/Import..., Select General/Existing Projects into workspace. Browse to sutdbank source folder, then click "finish".
	- Update configurations under /src/main/resources if necessary:
		+ database.properties.
		+ email.properties.
	
5. Run sutdbank-Webapp:
	- Open class sutdbank.DbCreator run as Java Application to setup database.
	- Select sutdbank project, right click and select Run as/Run on Server.  
	- Access the web page with url: http://localhost:3306/sutdbank/
	- Default account for staff: [username/password]:   staff_1/123456
	
	
	
	
	
