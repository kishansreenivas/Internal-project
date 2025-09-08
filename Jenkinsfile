pipeline {
    agent any

    tools {
        jdk 'OpenJDK 21'
        maven 'Maven 3.9.11'
    }

    options {
        timestamps()
        ansiColor('xterm')
    }

    stages {
        stage('Checkout Code') {
            steps {
                git 'https://github.com/kishansreenivas/Internal-project.git'
            }
        }

        stage('Build All Microservices') {
            parallel {
                stage('Build USER-SERVICE') {
                    steps {
                        dir('userservice') {
                            sh 'mvn clean verify'
                        }
                    }
                }

                stage('Build MOVIE-SERVICE') {
                    steps {
                        dir('movieservice') {
                            sh 'mvn clean verify'
                        }
                    }
                }

                stage('Build BOOKING-SERVICE') {
                    steps {
                        dir('bookingservice') {
                            sh 'mvn clean verify'
                        }
                    }
                }

                stage('Build PAYMENT-SERVICE') {
                    steps {
                        dir('paymentservice') {
                            sh 'mvn clean verify'
                        }
                    }
                }

                stage('Build NOTIFICATION-SERVICE') {
                    steps {
                        dir('notificationservice') {
                            sh 'mvn clean verify'
                        }
                    }
                }

                stage('Build SERVICE-REGISTRY') {
                    steps {
                        dir('ServiceRegistry') {
                            sh 'mvn clean verify'
                        }
                    }
                }

                stage('Build API-GATEWAY') {
                    steps {
                        dir('apigateway') {
                            sh 'mvn clean verify'
                        }
                    }
                }

                stage('Build CONFIG-SERVICE') {
                    steps {
                        dir('configservice') {
                            sh 'mvn clean verify'
                        }
                    }
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
