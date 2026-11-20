pipeline {
    agent any

    tools {
        // Requires Java 17 and Maven configured under Jenkins Global Tool Configuration
        jdk 'Java-17'
        maven 'Maven-3'
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
    }

    triggers {
        // Trigger build on GitHub webhook push event
        githubPush()
        // Periodic SCM polling fallback every 15 minutes
        pollSCM('H/15 * * * *')
    }

    environment {
        APP_NAME = 'todo-management-api'
        BUILD_TAG_VERSION = "1.0.0-build.${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo '=== Stage 1: Checkout Source Code ==='
                checkout scm
                sh '''
                    echo "Checked out branch: $(git rev-parse --abbrev-ref HEAD)"
                    echo "Current commit SHA: $(git rev-parse HEAD)"
                '''
            }
        }

        stage('Compile') {
            steps {
                echo '=== Stage 2: Compile Java 17 Source Code ==='
                sh 'mvn compile'
            }
        }

        stage('Automated JUnit Tests') {
            steps {
                echo '=== Stage 3: Automated JUnit Tests ==='
                // Runs full JUnit 5 test suite across all REST endpoints
                sh 'mvn clean test'
            }
        }

        stage('Maven Package') {
            steps {
                echo '=== Stage 4: Maven Package Application ==='
                // Packages executable JAR into target/todo-management-api-1.0.0.jar
                sh 'mvn package -DskipTests=true'
            }
        }

        stage('Archive JAR Artifact') {
            steps {
                echo '=== Stage 5: Archive JAR Artifact ==='
                // Archives the generated Spring Boot fat JAR
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            echo '=== Post Action: Publish JUnit Test Reports ==='
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
        }
        success {
            echo "CI Pipeline completed successfully! Artifact version: ${env.BUILD_TAG_VERSION}"
        }
        failure {
            echo 'CI Pipeline failed! Check test results and console output for details.'
        }
    }
}
