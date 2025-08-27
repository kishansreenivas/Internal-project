pipeline {
    agent any

    tools {
        jdk 'OpenJDK 21'          // Must match the JDK name in Jenkins > Global Tool Configuration
        maven 'Maven 3.9.11'       // Must match the Maven installation name in Jenkins
    }

    environment {
        JAVA_HOME = tool 'OpenJDK 21'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Checkout Code') {
            steps {
                git(
                    branch: 'master',
                    credentialsId: 'Jenkins CI/CD',   // Replace with your actual Jenkins credential ID
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
