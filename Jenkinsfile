pipeline {
    agent { label 'docker-node' } // Usa el nodo etiquetado
    environment {
        JAVA_TOOL_OPTIONS = "-Duser.home=/home/jenkins"
    }
    stages {
        stage('Checkout') {
            steps {
                echo 'Clonando repositorio...'
                git url: 'https://github.com/Davayme/Proyecto-Alquiler-Vehiculos.git', branch: 'main'
            }
        }
        stage('Build') {
            steps {
                echo 'Construyendo el proyecto...'
                sh 'docker run --rm -v "$PWD":/app -w /app maven:3.8.1-jdk-11 mvn clean install'
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
