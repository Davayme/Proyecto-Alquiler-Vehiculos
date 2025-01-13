pipeline {
    environment {
        JAVA_TOOL_OPTIONS = "-Duser.home=/home/jenkins"
    }
    agent {
        docker {
            image 'maven:3.8.1-jdk-11'
            args '-v /tmp/maven:/home/jenkins/.m2 -e MAVEN_CONFIG=/home/jenkins/.m2'
        }
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo 'Clonando el repositorio desde GitHub...'
                }
                // Clonar el repositorio desde GitHub
                git url: 'https://github.com/3ct-mx/spring-boot-computadoras.git', branch: 'main'
            }
        }
        stage('Build') {
            steps {
                script {
                    echo 'Compilando el proyecto usando Maven...'
                }
                // Compilar el proyecto usando Maven
                sh 'mvn clean install'
            }
        }
    }
    post {
        success {
            script {
                echo '¡Build completado con éxito!'
            }
        }
        failure {
            script {
                echo 'El build falló.'
            }
        }
    }
}
