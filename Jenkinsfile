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

        stage('Build All Microservices') {
            parallel {
                stage('Build USER-SERVICE') {
                    steps {
                        dir('userservice') {
                            bat 'mvn clean install'
                        }
                    }
                }

                stage('Build MOVIE-SERVICE') {
                    steps {
                        dir('movieservice') {
                            bat 'mvn clean install'
                        }
                    }
                }

                stage('Build BOOKING-SERVICE') {
                    steps {
                        dir('bookingservice') {
                            bat 'mvn clean install'
                        }
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
