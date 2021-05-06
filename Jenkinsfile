pipeline {
    agent any
    
      stages {
        stage('Inicializar') {
            steps {
                echo "PATH = ${PATH}"
                echo "M2_HOME = ${M2_HOME}"
            }
        }
        stage('Build') {
            steps {
                sh 'mvn install'
            }
        }  
    }
}
