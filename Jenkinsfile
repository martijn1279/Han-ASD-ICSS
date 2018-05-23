node {
    def app

    stage('Clone repository') {
        echo '----------Stage 1 out of 5----------'
        echo 'Checking if the repository is cloned properly..'
        checkout scm
    }
    stage('Build') {
        echo '----------Stage 2 out of 5----------'
        echo 'Building..'
        app = docker.build("asdgroep3docker/authentication-service")
    }
    stage('Test') {
        echo '----------Stage 3 out of 5----------'
        echo 'Testing..'
        echo 'WARNING THIS STAGE IS NOT IMPLEMENTED YET (testing is currently done in build stage, yes I know thats not the way it should go)'
    }
    if(env.BRANCH_NAME.equals('master')){
        stage('Push') {
            echo '----------Stage 4 out of 5----------'
            echo 'Pushing to DockerHub..'

            docker.withRegistry('https://registry.hub.docker.com', '9b15e7c8-fb52-4792-9a5a-6547c66024bf') {
                app.push("${env.BUILD_NUMBER}")
                app.push("latest")
            }
        }
        stage('Deploy') {
            echo '----------Stage 5 out of 5----------'
            echo 'Deploying..'
            sh 'docker stack deploy --compose-file //deployment/docker-compose.yml OnderwijsOffline'
        }
    }else{
        echo 'Stage 4 (Push) and 5 (deploy) skipped since this branch is not master'
    }
}
