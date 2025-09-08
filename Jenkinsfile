pipeline {
    agent any

    tools {
        jdk 'OpenJDK 21'          // Make sure this exact name exists in Jenkins Global Tool Config
        maven 'Maven 3.9.11'      // Make sure this exact name exists in Jenkins Global Tool Config
    }

    options {
        ansiColor('xterm')
        timestamps()
    }

    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()  // Clean any previous builds to avoid stale files
            }
        }

        stage('Checkout Code') {
            steps {
                git 'https://github.com/kishansreenivas/Internal-project.git'
                // Debug: list files at root and inside ServiceRegistry to confirm pom.xml exists
                sh 'ls -l'
                sh 'ls -l ServiceRegistry || echo "ServiceRegistry folder not found!"'
                sh 'ls -l ServiceRegistry/pom.xml || echo "pom.xml missing in ServiceRegistry!"'
            }
        }

        stage('Print Java and Maven Versions') {
            steps {
                sh 'java -version'
                sh 'mvn -v'
            }
        }

        stage('Build All Microservices') {
            parallel {
                stage('Build USER-SERVICE') {
                    steps {
                        dir('userservice') {
                            sh 'mvn clean install'
                        }
                    }
                }

                stage('Build MOVIE-SERVICE') {
                    steps {
                        dir('movieservice') {
                            sh 'mvn clean install'
                        }
                    }
                }

                stage('Build BOOKING-SERVICE') {
                    steps {
                        dir('bookingservice') {
                            sh 'mvn clean install'
                        }
                    }
                }

                stage('Build PAYMENT-SERVICE') {
                    steps {
                        dir('paymentservice') {
                            sh 'mvn clean install'
                        }
                    }
                }

                stage('Build NOTIFICATION-SERVICE') {
                    steps {
                        dir('notificationservice') {
                            sh 'mvn clean install'
                        }
                    }
                }

                stage('Build SERVICE-REGISTRY') {
                    steps {
                        dir('ServiceRegistry') {
                            sh 'mvn clean install'
                        }
                    }
                }

                stage('Build API-GATEWAY') {
                    steps {
                        dir('apigateway') {
                            sh 'mvn clean install'
                        }
                    }
                }

                stage('Build CONFIG-SERVICE') {
                    steps {
                        dir('configservice') {
                            sh 'mvn clean install'
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
