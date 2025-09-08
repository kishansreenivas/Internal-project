pipeline {
    agent any

    tools {
        jdk 'jdk21'       // 🔧 Make sure this JDK is configured in Jenkins
        maven 'maven3'    // 🔧 Optional, if Maven is configured by name
    }

    environment {
        MAVEN_OPTS = "-Xmx1024m"
    }

    stages {
        stage('Verify Java Version') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('Build Movie Service') {
            steps {
                dir('movie-service') {
                    sh 'mvn clean install'
                }
            }
        }
    }

    post {
        failure {
            echo '❌ Build failed!'
        }
        success {
            echo '✅ Build successful!'
        }
    }
}
