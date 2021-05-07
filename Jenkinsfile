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
                echo "maven" 
                bat 'mvn install'
            }
        }
      }
      post {
        always {
            junit '**/target/surefire-reports/*.xml'
        }  
    }
}
