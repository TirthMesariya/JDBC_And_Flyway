pipeline {
    agent { label 'test-java' }

    environment {
        DOCKER_CREDS = credentials('docker-hub-creds')
    }

    stages {
        stage('Clone Repository') {
            steps {
                sh 'git clone https://github.com/TirthMesariya/JDBC_And_Flyway.git'
            }
        }

        stage('Docker Login') {
            steps {
                sh """
                echo \$DOCKER_CREDS_PSW | docker login -u \$DOCKER_CREDS_USR --password-stdin
                """
            }
        }

        stage('Docker Compose Up') {
            steps {
                sh 'docker compose -f JDBC_And_Flyway/docker-compose.yml up -d --build'
            }
        }
    }
}
