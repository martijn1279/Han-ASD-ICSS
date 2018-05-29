pipeline {
  agent none
  stages {
    stage("Build") {
      agent any
      steps {
        echo "Building"
      }
    }
    stage("Deploy") {
      timeout(time:5, unit:'DAYS') {
        input message:'Approve deployment?', submitter: 'it-ops'
      }
      agent any
      steps {
        echo "Building"
      }
    }
  }
}
