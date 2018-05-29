pipeline {
  agent none
  stages {
    stage("Build") {
      agent any
      steps {
        echo "Building"
      }
    }
    stage('Wait') {
      steps {
        timeout(time:5, unit:'DAYS') {
          input message:'Approve deployment?'
        }
      }
    }
    stage("Deploy") {
      agent any
      steps {
        echo "Building"
      }
    }
  }
}
