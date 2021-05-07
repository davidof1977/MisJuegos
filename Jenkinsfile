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
         stage('Test') {
            steps {
                echo "maven test" 
                bat 'mvn test'
            }
        }
      }
      post {
        always {
            junit '**/target/surefire-reports/*.xml'
        }  
    }
}
