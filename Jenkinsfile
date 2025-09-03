
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
                // Optional: clean old workspace to reduce load
                deleteDir()
                git url: 'https://github.com/Sumeet-khandale/Internal-project.git'
            }
        }

        stage('Build Microservices (Sequential)') {
            steps {
                script {
                    def services = ['userservice', 'movieservice', 'bookingservice'] // ✅ only 3 for now

                    for (service in services) {
                        echo "🔧 Building ${service}..."
                        dir(service) {
                            timeout(time: 7, unit: 'MINUTES') {
                                sh 'mvn --batch-mode clean install'
                            }
                        }
                    }
                }
            }
        }

        // Optional: You can add more services in another stage if needed
        // Or manually rotate them depending on use case
        /*
        stage('Build Remaining Services') {
            steps {
                script {
                    def services = ['paymentservice', 'notificationservice', 'ServiceRegistry']

                    for (service in services) {
                        echo "🔧 Building ${service}..."
                        dir(service) {
                            timeout(time: 7, unit: 'MINUTES') {
                                sh 'mvn --batch-mode clean install'
                            }
                        }
                    }
                }
            }
        }
        */
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
