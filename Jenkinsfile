
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
      stage('Build userservice') {
            steps {
                dir('userservice') {
                    sh './mvnw clean install -DskipTests'
                }
            }
        }
    }

    post {
        success {
            echo "Code fetched successfully from GitHub!"
        }
        failure {
            echo "Failed to fetch code from GitHub!"
        }
    }
}
