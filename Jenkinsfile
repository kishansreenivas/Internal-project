pipeline {
    agent any

    tools {
        jdk 'OpenJDK 21'          // Must exactly match JDK name in Jenkins Global Tool Configuration
        maven 'Maven 3.9.11'       // Must exactly match Maven installation name in Jenkins
    }

    environment {
        JAVA_HOME = tool 'OpenJDK 21'   // This injects the path to JDK
        PATH = "${JAVA_HOME}\\bin;${env.PATH}"  // Windows path separator is ';'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git(
                    branch: 'master',
                    // If you don't have credentials, remove this line or update with actual credentials ID
                    credentialsId: 'Jenkins CI/CD',  
                    url: 'https://github.com/Sumeet-khandale/Internal-project.git'
                )
            }
        }

        stage('Build USER-SERVICE') {
            steps {
                dir('userservice') {
                    echo "🛠️ Building USER-SERVICE with Maven..."
                    bat 'mvn clean install'
                }
            }
        }
    }

    post {
        success {
            echo "✅ USER-SERVICE built successfully!"
        }
        failure {
            echo "❌ Build failed for USER-SERVICE. Please check the console output."
        }
    }
}
