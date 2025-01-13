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
                echo 'Clonando repositorio...'
                git url: 'https://github.com/Davayme/Proyecto-Alquiler-Vehiculos.git', branch: 'main'
            }
        }
        stage('Build') {
            steps {
                echo 'Construyendo el proyecto...'
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
