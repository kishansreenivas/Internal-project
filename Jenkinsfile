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
