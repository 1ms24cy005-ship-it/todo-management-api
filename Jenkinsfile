pipeline {
    agent any

    tools {
        // Requires Java 17 and Maven configured under Jenkins Global Tool Configuration
        jdk 'Java-17'
        maven 'Maven-3'
    }

    options {
        // Limit console log size and set timeout
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
    }

    triggers {
        // Trigger automated build when GitHub push event is received via webhook
        githubPush()
        // Fallback polling every 15 minutes in case webhooks are delayed
        pollSCM('H/15 * * * *')
    }

    environment {
        APP_NAME = 'todo-management-api'
        // Dynamic build versioning incorporating Jenkins build number and Git SHA
        BUILD_TAG_VERSION = "1.0.0-build.${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo '=== Stage 1: Checking out source code from Git ==='
                checkout scm
                sh '''
                    echo "Checked out branch: $(git rev-parse --abbrev-ref HEAD)"
                    echo "Current commit SHA: $(git rev-parse HEAD)"
                '''
            }
        }

        stage('Compile') {
            steps {
                echo '=== Stage 2: Compiling Java 17 Source Code ==='
                sh 'mvn compile'
            }
        }

        stage('Run Automated JUnit Tests') {
            steps {
                echo '=== Stage 3: Running Automated JUnit 5 Test Suite ==='
                // Runs all unit and integration tests (GET /todos, POST /todos, GET /todos/{id}, /todos/count, /health)
                sh 'mvn clean test'
            }
        }

        stage('Package') {
            steps {
                echo '=== Stage 4: Packaging Spring Boot Executable JAR ==='
                // Packages application into target/todo-management-api-1.0.0.jar
                sh 'mvn package -DskipTests=true'
            }
        }

        stage('Archive Artifacts') {
            steps {
                echo '=== Stage 5: Archiving Generated JAR Artifact ==='
                // Archives the generated Spring Boot fat JAR for deployment
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            echo '=== Post Action: Publishing JUnit Test Reports ==='
            // Ingests Surefire XML reports generated during test execution
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
        }
        success {
            echo "CI Pipeline completed successfully! Artifact ready for Version: ${env.BUILD_TAG_VERSION}"
        }
        failure {
            echo 'CI Pipeline failed! Check Surefire reports and console output for failure details.'
        }
    }
}
