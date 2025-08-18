pipeline {
    agent any

    environment {
        JDK_VERSION = 'jdk-21'  // Microsoft JDK 21
        dockerimagename = 'Jenkinsfile'  // Docker image name set to 'Jenkinsfile'
        dockerimage = ''  // Blank docker image name as requested
    }

    tools {
        jdk JDK_VERSION
    }

    stages {
        stage('Clone Git Repository') {
            steps {
                script {
                    // Cloning the Git repository
                    echo "Cloning repository..."
                    git url: 'git@github.com:Sumeet-khandale/Internal-project.git'
                }
            }
        }

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

        // Example of using the environment variables
        stage('Deploy') {
            steps {
                script {
                    echo "Using Docker Image Name: ${dockerimagename}"
                    echo "Docker Image: ${dockerimage}"  // This will be blank as per the variable setting
                }
            }
        }
    }
}
