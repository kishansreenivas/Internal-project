pipeline {
    agent any

    tools {
        jdk 'openJDK 21'
        maven '3.9.11'
    }

    environment {
        MAVEN_OPTS = "-Xmx512m"
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
                    sh 'mvn -B clean install'
                }
            }
        }

        stage('Build MOVIE-SERVICE') {
            steps {
                dir('movieservice') {
                    sh 'mvn -B clean install'
                }
            }
        }

        stage('Build BOOKING-SERVICE') {
            steps {
                dir('bookingservice') {
                    sh 'mvn -B clean install'
                }
            }
        }

        stage('Build PAYMENT-SERVICE') {
            steps {
                dir('paymentservice') {
                    sh 'mvn -B clean install'
                }
            }
        }

        stage('Build NOTIFICATION-SERVICE') {
            steps {
                dir('notificationservice') {
                    sh 'mvn -B clean install'
                }
            }
        }

        stage('Build SERVICE-REGISTRY') {
            steps {
                dir('ServiceRegistry') {
                    sh 'mvn -B clean install'
                }
            }
        }
    }

    post {
        success {
            echo '✅ Build succeeded!'
        }
        failure {
            echo '❌ Build failed!'
        }
    }
}
