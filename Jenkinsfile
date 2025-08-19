pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'master',
                    credentialsId: 'Jenkins CI/CD',
                    url: 'https://github.com/Sumeet-khandale/Internal-project.git'
            }
        }

      stage('User Service') {
    steps {
        dir('USER-SERVICE') {   // must match repo exactly
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
