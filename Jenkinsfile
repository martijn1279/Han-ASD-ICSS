pipeline {
  agent none
  stages {
    stage("Build") {
      agent any
      steps {
        echo "Building"
        stash includes: 'path/to/things/*', name: 'my-files'
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
        unstash 'my-files'
        sh 'deploy.sh'
      }
    }
  }
}
