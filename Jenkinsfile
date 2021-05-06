pipeline {
    agent any
    
      stages {
        stage('Inicializar') {
            steps {
                echo "PATH = ${PATH}"
            }
        }
        stage('Build') {
            steps {
                sh 'mvn install'
            }
        }  
    }
}
