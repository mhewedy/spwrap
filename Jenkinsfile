pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        node(label: 'master') {
          mvn clean package
        }
      }
    }
  }
}
