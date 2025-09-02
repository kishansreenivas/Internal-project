pipeline {
    agent any

    tools {
        jdk 'openJDK 21'
        maven '3.9.11'
    }

    stages {
        stage('Verify Environment') {
            steps {
                sh '''
                    echo "JAVA_HOME: $JAVA_HOME"
                    $JAVA_HOME/bin/java -version
                    mvn -version
                '''
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
                            sh '''
                                $JAVA_HOME/bin/java -version
                                mvn clean install
                            '''
                        }
                    }
                }

                stage('Build MOVIE-SERVICE') {
                    steps {
                        dir('movieservice') {
                            sh '''
                                $JAVA_HOME/bin/java -version
                                mvn clean install
                            '''
                        }
                    }
                }

                stage('Build BOOKING-SERVICE') {
                    steps {
                        dir('bookingservice') {
                            sh '''
                                $JAVA_HOME/bin/java -version
                                mvn clean install
                            '''
                        }
                    }
                }

                stage('Build PAYMENT-SERVICE') {
                    steps {
                        dir('paymentservice') {
                            sh '''
                                $JAVA_HOME/bin/java -version
                                mvn clean install
                            '''
                        }
                    }
                }

                stage('Build NOTIFICATION-SERVICE') {
                    steps {
                        dir('notificationservice') {
                            sh '''
                                $JAVA_HOME/bin/java -version
                                mvn clean install
                            '''
                        }
                    }
                }

                stage('Build SERVICE-REGISTRY') {
                    steps {
                        dir('ServiceRegistry') {
                            sh '''
                                $JAVA_HOME/bin/java -version
                                mvn clean install
                            '''
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

