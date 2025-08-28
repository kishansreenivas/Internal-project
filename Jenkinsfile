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
<<<<<<< HEAD
                       bat 'mvn clean install'
=======
                       bat 'mvn clean install '
>>>>>>> 60ce08121f0e6bf9c4fa74540d87d33f27100fe3
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
