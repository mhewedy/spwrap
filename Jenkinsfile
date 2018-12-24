pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        node(label: 'master') {
          mvn -Dmaven.test.failure.ignore clean package
        }
      }
    }
  }
}
