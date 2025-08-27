pipeline {
    agent any

    tools {
        jdk 'OpenJDK 21'        // Jenkins will automatically install OpenJDK 21
        maven 'Maven 3.9.11'    // Jenkins will automatically install Maven 3.9.11
    }

    stages {
        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/Sumeet-khandale/Internal-project.git'
            }
        }

        stage('Build USER-SERVICE') {
            steps {
                dir('userservice') {
                    script {
                        // The tools will automatically be available based on the configuration
                       bat 'mvn clean install '
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Build succeeded!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
