pipeline {
    environment {
        JAVA_TOOL_OPTIONS = "-Duser.home=/home/jenkins"
    }
    agent any // Cambiado a `any` para construir la imagen en el nodo
    stages {
        stage('Build Docker Image') {
            steps {
                script {
                    echo 'Construyendo la imagen Docker personalizada...'
                }
                // Construir la imagen Docker a partir del Dockerfile
                sh 'docker build -t maven:3.8.5-openjdk-17'
            }
        }
        stage('Build with Maven') {
            agent {
                docker {
                    image 'custom-maven-image' // Usar la imagen construida
                    args '-v /tmp/maven:/home/jenkins/.m2 -e MAVEN_CONFIG=/home/jenkins/.m2'
                }
            }
            steps {
                script {
                    echo 'Construyendo el proyecto usando Maven...'
                }
                // Compilar el proyecto
                sh 'mvn clean install'
            }
        }
    }
    post {
        success {
            echo '¡Build completado con éxito!'
        }
        failure {
            echo 'El build falló.'
        }
    }
}
