
 node {
     stage('checkout') {
           checkout scm
  }

stage('check java') {
    sh "java -version"
}

def dockerImage
stage('build docker') {
}

stage('publish docker') {
    docker.withRegistry('https://registry.hub.docker.com', 'docker-login') {
        dockerImage.push 'latest'
    }
}