pipeline {
    agent { label 'master' }  // ensure it runs on master node

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
                        dir('user-service') {   // adjust folder name if different
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
            }
        }
    }

    post {
        success {
            echo "✅ Build completed successfully!"
        }
        failure {
            echo "❌ Build failed. Check logs!"
        }
    }
}
