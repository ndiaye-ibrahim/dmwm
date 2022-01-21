
 node {
     stage('checkout') {
           checkout scm
  }

stage('check java') {
    sh "java -version"
}

gitlabCommitStatus('build') {
        docker.image('jhipster/jhipster:v7.4.1').inside('-u jhipster -e MAVEN_OPTS="-Duser.home=./"') {
 }
}
 }