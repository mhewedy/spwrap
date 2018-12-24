pipeline {
  agent any
  def mvnHome
  stages {
    stage('Preparation') {
      mvnHome = tool 'M3'
   }
    stage('build') {
      steps {
        node(label: 'master') {
          if (isUnix()) {
            sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
          } else {
             bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean package/)
          }
        }

      }
    }
  }
}
