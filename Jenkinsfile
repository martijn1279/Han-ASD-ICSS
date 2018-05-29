pipeline {
  agent none
  stages {
    stage("Build") {
      agent any
      steps {
        echo "Building"
      }
    }
    timeout(time:5, unit:'DAYS') {
        input message:'Approve deployment?', submitter: 'it-ops'
    }
    stage("Deploy") {
      agent any
      steps {
        echo "Building"
      }
    }
  }
}
