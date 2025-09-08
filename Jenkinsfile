pipeline {
    agent any

    tools {
        jdk 'OpenJDK 21'
        maven 'Maven 3.9.11'
    }

    options {
        timestamps()
        // Removed ansiColor('xterm') — it's not valid here unless plugin fully supports it
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
                            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
                                sh 'mvn clean install'
                            }
                        }
                    }
                }

                stage('Build MOVIE-SERVICE') {
                    steps {
                        dir('movieservice') {
                            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
                                sh 'mvn clean install'
                            }
                        }
                    }
                }

                stage('Build BOOKING-SERVICE') {
                    steps {
                        dir('bookingservice') {
                            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
                                sh 'mvn clean install'
                            }
                        }
                    }
                }

                stage('Build PAYMENT-SERVICE') {
                    steps {
                        dir('paymentservice') {
                            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
                                sh 'mvn clean install'
                            }
                        }
                    }
                }

                stage('Build NOTIFICATION-SERVICE') {
                    steps {
                        dir('notificationservice') {
                            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
                                sh 'mvn clean install'
                            }
                        }
                    }
                }

                stage('Build SERVICE-REGISTRY') {
                    steps {
                        dir('ServiceRegistry') {
                            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
                                sh 'mvn clean install'
                            }
                        }
                    }
                }

                stage('Build API-GATEWAY') {
                    steps {
                        dir('apigateway') {
                            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
                                sh 'mvn clean install'
                            }
                        }
                    }
                }

                stage('Build CONFIG-SERVICE') {
                    steps {
                        dir('configservice') {
                            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
                                sh 'mvn clean install'
                            }
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
