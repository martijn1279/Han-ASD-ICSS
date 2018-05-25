pipeline {
  agent none
  stages {
    stage("Build") {
      agent any
      steps {
        echo "Building"
      }
    }
    stage("Checkpoint") {
      agent none
      steps {
        checkpoint 'Completed Build'
      }
    }
    stage("Deploy") {
      agent any
      steps {
        sh 'deploy.sh'
      }
    }
  }
}
