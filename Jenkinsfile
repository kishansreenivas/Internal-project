pipeline {
    agent any
 
    tools {
        jdk 'jdk-21'
    }
 
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
 
        stage('Build All Microservices') {
            parallel {
                stage('userservice') {
                    steps {
                        dir('userservice') {
                            sh './mvnw clean install -DskipTests'
                        }
                    }
                }
 
                stage('movieservice') {
                    steps {
                        dir('movieservice') {
                            sh './mvnw clean install -DskipTests'
                        }
                    }
                }
 
                stage('bookingservice') {
                    steps {
                        dir('bookingservice') {
                            sh './mvnw clean install -DskipTests'
                        }
                    }
                }
 
                stage('paymentservice') {
                    steps {
                        dir('paymentservice') {
                            sh './mvnw clean install -DskipTests'
                        }
                    }
                }
 
                stage('notificationservice') {
                    steps {
                        dir('notificationservice') {
                            sh './mvnw clean install -DskipTests'
                        }
                    }
                }
 
                stage('apigateway') {
                    steps {
                        dir('apigateway') {
                            sh './mvnw clean install -DskipTests'
                        }
                    }
                }
 
                stage('serviceregistry') {
                    steps {
                        dir('serviceregistry') {
                            sh './mvnw clean install -DskipTests'
                        }
                    }
                }
 
                stage('configservice') {
                    steps {
                        dir('configservice') {
                            sh './mvnw clean install -DskipTests'
                        }
                    }
                }
            }
        }
    }
}

