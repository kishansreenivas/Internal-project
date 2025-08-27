pipeline {
    agent any

    tools {
        jdk 'OpenJDK 21'
        maven 'Maven 3.9.11'
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
                        def javaHomePath = tool 'OpenJDK 21'
                        def mavenHomePath = tool 'Maven 3.9.11'
                        withEnv([
                            "JAVA_HOME=${javaHomePath}",
                            "PATH=${javaHomePath}\\bin;${mavenHomePath}\\bin;${env.PATH}"
                        ]) {
                            bat 'mvn clean install'
                        }
                    }
                }
            }
        }
    }
}
