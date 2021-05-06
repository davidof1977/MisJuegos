pipeline {
    agent any
    tools {
       maven 'MyMaven'
       jdk 'MyJava' 
    }
      stages {
        stage('Inicializar') {
            steps {
                echo "PATH = ${PATH}"
            }
        }
        stage('Build') {
            steps {
                bat 'mvn install'
            }
        }  
    }
}
