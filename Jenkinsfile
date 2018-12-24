pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        node(label: 'master') {
          sh 'gradlew build'
        }

      }
    }
  }
}