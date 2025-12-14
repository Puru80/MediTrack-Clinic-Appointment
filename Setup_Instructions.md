# MediTrack Setup Instructions

This guide will walk you through the complete setup process for the MediTrack Console Application, including Java installation, IDE configuration, and project setup.

---

## Table of Contents
1. [System Requirements](#system-requirements)
2. [Java Development Kit (JDK) Installation](#java-development-kit-jdk-installation)
3. [IDE Setup](#ide-setup)
4. [Project Setup](#project-setup)
5. [Running the Application](#running-the-application)
6. [Troubleshooting](#troubleshooting)

---

## System Requirements

### Minimum Requirements
- **Operating System**: Windows 10/11, macOS 10.14+, or Linux (Ubuntu 18.04+)
- **RAM**: 4 GB minimum (8 GB recommended)
- **Disk Space**: 500 MB for JDK + 200 MB for project
- **Processor**: Any modern processor (Intel i3 or equivalent)

### Software Requirements
- Java Development Kit (JDK) 17 or higher
- Maven 3.6+ (for dependency management)
- IDE: IntelliJ IDEA, Eclipse, or VS Code with Java extensions

---

## Java Development Kit (JDK) Installation

### Understanding JDK vs JRE
- **JDK (Java Development Kit)**: Complete development environment with compiler (javac), runtime (JRE), and development tools
- **JRE (Java Runtime Environment)**: Only the runtime needed to run Java applications (not for development)

**For MediTrack development, you need the JDK.**

### Installation Steps

#### Windows Installation

1. **Download JDK**
   - Visit [Oracle JDK Downloads](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
   - Select **Java 17 LTS** or **Java 21 LTS**
   - Download the Windows x64 installer (.exe file)

2. **Run the Installer**
   - Double-click the downloaded `.exe` file
   - Follow the installation wizard
   - Note the installation path (e.g., `C:\Program Files\Java\jdk-17`)

3. **Set JAVA_HOME Environment Variable**
   - Right-click **This PC** → **Properties** → **Advanced system settings**
   - Click **Environment Variables**
   - Under **System Variables**, click **New**
   - Variable name: `JAVA_HOME`
   - Variable value: Your JDK installation path (e.g., `C:\Program Files\Java\jdk-17`)
   - Click **OK**

4. **Update PATH Variable**
   - In **System Variables**, find and select **Path**
   - Click **Edit** → **New**
   - Add: `%JAVA_HOME%\bin`
   - Click **OK** on all windows

5. **Verify Installation**
   - Open Command Prompt (cmd)
   - Run:
     ```cmd
     java -version
     javac -version
     ```
   - You should see version information for both commands

**Screenshot Reference**: You should see output similar to:
```
java version "17.0.x" 2023-xx-xx LTS
Java(TM) SE Runtime Environment (build 17.0.x+x-LTS-xxx)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.x+x-LTS-xxx, mixed mode, sharing)
```

#### macOS Installation

1. **Using Homebrew (Recommended)**
   ```bash
   # Install Homebrew if not already installed
   /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
   
   # Install JDK
   brew install openjdk@17
   ```

2. **Manual Installation**
   - Download the macOS installer from Oracle or Adoptium
   - Open the `.dmg` file and follow the installation wizard

3. **Set JAVA_HOME**
   - Open Terminal
   - Edit your shell profile:
     ```bash
     nano ~/.zshrc  # for zsh (default on macOS Catalina+)
     # or
     nano ~/.bash_profile  # for bash
     ```
   - Add the following line:
     ```bash
     export JAVA_HOME=$(/usr/libexec/java_home -v 17)
     export PATH=$JAVA_HOME/bin:$PATH
     ```
   - Save and reload:
     ```bash
     source ~/.zshrc  # or source ~/.bash_profile
     ```

4. **Verify Installation**
   ```bash
   java -version
   javac -version
   echo $JAVA_HOME
   ```

#### Linux (Ubuntu/Debian) Installation

1. **Using APT Package Manager**
   ```bash
   # Update package index
   sudo apt update
   
   # Install OpenJDK 17
   sudo apt install openjdk-17-jdk
   ```

2. **Set JAVA_HOME**
   ```bash
   # Find Java installation path
   sudo update-alternatives --config java
   
   # Edit environment file
   sudo nano /etc/environment
   
   # Add this line (replace path with your actual path)
   JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64"
   
   # Reload environment
   source /etc/environment
   ```

3. **Verify Installation**
   ```bash
   java -version
   javac -version
   echo $JAVA_HOME
   ```

---

## IDE Setup

### Option 1: IntelliJ IDEA (Recommended)

1. **Download and Install**
   - Visit [JetBrains IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
   - Download Community Edition (free) or Ultimate Edition
   - Run the installer and follow the wizard

2. **Configure JDK in IntelliJ**
   - Open IntelliJ IDEA
   - Go to **File** → **Project Structure** (Ctrl+Alt+Shift+S / Cmd+;)
   - Under **Project**, select **SDK**
   - If your JDK doesn't appear, click **Add SDK** → **JDK**
   - Navigate to your JDK installation folder
   - Click **OK**

3. **Set Language Level**
   - In **Project Structure**, set **Language level** to **17 - Sealed types, always-strict floating-point semantics**

### Option 2: Eclipse

1. **Download and Install**
   - Visit [Eclipse Downloads](https://www.eclipse.org/downloads/)
   - Download Eclipse IDE for Java Developers
   - Extract and run the installer

2. **Configure JDK in Eclipse**
   - Go to **Window** → **Preferences**
   - Navigate to **Java** → **Installed JREs**
   - Click **Add** → **Standard VM**
   - Browse to your JDK installation directory
   - Click **Finish** and make sure it's checked

### Option 3: VS Code

1. **Install VS Code**
   - Download from [code.visualstudio.com](https://code.visualstudio.com/)

2. **Install Java Extension Pack**
   - Open VS Code
   - Go to Extensions (Ctrl+Shift+X / Cmd+Shift+X)
   - Search for "Java Extension Pack"
   - Install it (includes Language Support, Debugger, Maven, etc.)

3. **Configure Java**
   - Open Command Palette (Ctrl+Shift+P / Cmd+Shift+P)
   - Type "Java: Configure Java Runtime"
   - Add your JDK installation path

---

## Project Setup

### 1. Download/Clone the Project

```bash
# If using Git
git clone <repository-url>
cd meditrack

# Or download and extract the ZIP file
```

### 2. Maven Setup

The project uses Maven for dependency management. Maven should be included with your IDE, but you can also install it separately.

**Verify Maven Installation:**
```bash
mvn -version
```

**If Maven is not installed:**

**Windows (Using Chocolatey):**
```cmd
choco install maven
```

**macOS (Using Homebrew):**
```bash
brew install maven
```

**Linux (Using APT):**
```bash
sudo apt install maven
```

### 3. Import Project in IDE

#### IntelliJ IDEA
1. **File** → **Open**
2. Navigate to the MediTrack project folder
3. Select the project root (containing `pom.xml`)
4. Click **OK**
5. IntelliJ will automatically detect it as a Maven project and download dependencies

#### Eclipse
1. **File** → **Import** → **Maven** → **Existing Maven Projects**
2. Browse to the MediTrack project folder
3. Select the project and click **Finish**

#### VS Code
1. **File** → **Open Folder**
2. Select the MediTrack project folder
3. VS Code will automatically detect the Java project

### 4. Verify Project Structure

Your project should have the following structure:
```
meditrack/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── airtribe/
│                   └── meditrack/
│                       ├── entity/
│                       ├── service/
│                       ├── util/
│                       ├── exceptions/
│                       └── interfaces/
├── pom.xml
└── README.md
```

### 5. Build the Project

```bash
# Navigate to project directory
cd meditrack

# Clean and build
mvn clean compile

# Run tests (if any)
mvn test

# Package the application
mvn package
```

---

## Running the Application

### Method 1: From IDE

#### IntelliJ IDEA
1. Navigate to `src/main/java/com/airtribe/meditrack/Main.java`
2. Right-click on the file
3. Select **Run 'Main.main()'**

#### Eclipse
1. Navigate to `Main.java`
2. Right-click → **Run As** → **Java Application**

#### VS Code
1. Open `Main.java`
2. Click the **Run** button above the `main` method
3. Or press **F5**

### Method 2: From Command Line

```bash
# Compile the project
javac -d bin -sourcepath src/main/java src/main/java/com/airtribe/meditrack/Main.java

# Run the application
java -cp bin com.airtribe.meditrack.Main
```

### Method 3: Using Maven

```bash
# Package the application
mvn clean package

# Run the JAR file
java -jar target/meditrack-1.0-SNAPSHOT.jar
```

---

## Troubleshooting

### Issue 1: "java is not recognized as an internal or external command"

**Solution:**
- Ensure JAVA_HOME is set correctly
- Verify PATH includes `%JAVA_HOME%\bin` (Windows) or `$JAVA_HOME/bin` (Mac/Linux)
- Restart your terminal/command prompt after setting environment variables

### Issue 2: "Error: Could not find or load main class"

**Solution:**
- Verify your classpath is correct
- Ensure the package structure matches the directory structure
- Check that the Main class has a proper `main` method:
  ```java
  public static void main(String[] args) { }
  ```

### Issue 3: IDE doesn't recognize Java files

**Solution:**
- Reimport the Maven project: Right-click on project → **Maven** → **Reload Project**
- Invalidate caches: **File** → **Invalidate Caches / Restart**
- Check that JDK is properly configured in IDE settings

### Issue 4: Maven dependencies not downloading

**Solution:**
- Check internet connection
- Clear Maven cache:
  ```bash
  mvn dependency:purge-local-repository
  mvn clean install
  ```
- Check `pom.xml` for syntax errors

### Issue 5: Compilation errors related to Java version

**Solution:**
- Ensure your source and target versions match:
  ```xml
  <properties>
      <maven.compiler.source>17</maven.compiler.source>
      <maven.compiler.target>17</maven.compiler.target>
  </properties>
  ```
- Verify IDE is using the correct JDK version

---

## Additional Resources

### Official Documentation
- [Java SE Documentation](https://docs.oracle.com/en/java/javase/17/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [IntelliJ IDEA Documentation](https://www.jetbrains.com/idea/documentation/)

### Learning Resources
- [Oracle Java Tutorials](https://docs.oracle.com/javase/tutorial/)
- [Baeldung Java Tutorials](https://www.baeldung.com/)
- [Java Point](https://www.javatpoint.com/java-tutorial)

### Video Tutorials
- Java Installation: Search "How to install JDK 17" on YouTube
- Maven Setup: Search "Maven installation and setup"
- IDE Setup: Search "[Your IDE] Java setup tutorial"

---

## Support

If you encounter any issues not covered in this guide:

1. Check the project's README.md for additional information
2. Review the error messages carefully
3. Search for the specific error online
4. Contact the development team or create an issue in the project repository

---

**Document Version**: 1.0  
**Last Updated**: December 2024  
**Author**: MediTrack Development Team  
**Project**: MediTrack Console Application