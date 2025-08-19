pipeline {
    agent { label 'master' }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'master',
                    credentialsId: 'Jenkins CI/CD',
                    url: 'https://github.com/Sumeet-khandale/Internal-project.git'
            }
        }

        stage('Build User Service') {
            steps {
                dir('user-service') {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }
    }

    post {
        success {
            echo "✅ User Service build completed successfully!"
        }
        failure {
            echo "❌ User Service build failed. Please check logs."
        }
    }
}
