pipeline {
    agent any

    tools {
        jdk 'openJDK 21'
        maven '3.9.11'
    }

    environment {
        JAVA_HOME = tool name: 'openJDK 21', type: 'jdk'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Verify Java and Maven') {
            steps {
                sh 'echo "Using JAVA_HOME=$JAVA_HOME"'
                sh 'java -version'
                sh 'mvn -version'
            }
        }

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
