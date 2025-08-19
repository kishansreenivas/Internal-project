  pipeline {
     agent any  // runs on Jenkins master node (adjust if using agents)

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'master',
                    credentialsId: 'Jenkins CI/CD',
                    url: 'https://github.com/Sumeet-khandale/Internal-project.git'
            }
        }

        stage('Build All Microservices') {
            parallel {
                stage('User Service') {
                    steps {
                        dir('user-service') {
                            sh 'mvn clean install -DskipTests'
                        }
                    }
                }
                stage('Movie Service') {
                    steps {
                        dir('movie-service') {
                            sh 'mvn clean install -DskipTests'
                        }
                    }
                }
                stage('Booking Service') {
                    steps {
                        dir('booking-service') {
                            sh 'mvn clean install -DskipTests'
                        }
                    }
                }
                stage('Payment Service') {
                    steps {
                        dir('payment-service') {
                            sh 'mvn clean install -DskipTests'
                        }
                    }
                }
                stage('Notification Service') {
                    steps {
                        dir('notification-service') {
                            sh 'mvn clean install -DskipTests'
                        }
                    }
                }
                stage('Common Utils') {
                    steps {
                        dir('common-utils') {
                            sh 'mvn clean install -DskipTests'
                        }
                    }
                }
                stage('API Gateway') {
                    steps {
                        dir('api-gateway') {
                            sh 'mvn clean install -DskipTests'
                        }
                    }
                }
                stage('Config Server') {
                    steps {
                        dir('config-server') {
                            sh 'mvn clean install -DskipTests'
                        }
                    }
                }
                stage('Discovery Server') {
                    steps {
                        dir('discovery-server') {
                            sh 'mvn clean install -DskipTests'
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo "✅ Build completed successfully for all microservices!"
        }
        failure {
            echo "❌ Build failed. Please check logs."
        }
    }
}
