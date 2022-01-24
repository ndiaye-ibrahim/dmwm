pipeline{

   agent any
  
   stages{
    
      stage ('Build Docker'){
        steps {
            script {
                if(env.BRANCH_NAME.contains("feature") || env.BRANCH_NAME.equals("develop")){
                    sh "sudo docker-compose build"
                }
            }
        } 
      }
      
      stage ('Unit tests'){
        steps {
            script {
                if(env.BRANCH_NAME.contains("feature") || env.BRANCH_NAME.equals("develop")){
                    sh 'sudo docker-compose run app ng test & ng_test_pid=\"$!\"'
                    echo 'waiting for process with $ng_test_pid for 30s...'
                    sh "sleep 30s"
                    echo 'killing process with pid $ng_test_pid'
                    sh "sudo kill -9 $ng_test_pid"
                }
            }
        } 
      }
      
      stage ('Code quality review'){
        steps {
            script {
                if(env.BRANCH_NAME.contains("feature") || env.BRANCH_NAME.equals("develop")){
                    sh "sudo docker-compose run app sonar-scanner"
                }
            }
        } 
      }
      
   }
   
   post {  

      always {
            script {
                    
              if (manager.logContains('.*FAILURE.*') || manager.logContains('.*failure.*') || manager.logContains('.*FAILED.*') || manager.logContains('.*failed.*') || manager.logContains('.*FAIL.*') || manager.logContains('.*fail.*') ||
              manager.logContains('.*WARNING.*') || manager.logContains('.*WARN.*') || manager.logContains('.*warning.*') || manager.logContains('.*warn.*') ||
              manager.logContains('.*error.*') || manager.logContains('.*ERROR.*') || manager.logContains('.*ERR.*') || manager.logContains('.*err.*')) 
           
                    {
                              emailext (
                                  subject:  "Status of pipeline: ${currentBuild.fullDisplayName}",
                                  mimeType: 'text/html', 
                                  to:       '@email',
                                  recipientProviders: [[$class: 'CulpritsRecipientProvider'],[$class: 'RequesterRecipientProvider']],
                                  attachLog: true,
                                  body:     """<p>EXECUTED: Job <b>\'${env.JOB_NAME}:${env.BUILD_NUMBER})\'
                                            </b></p><p>View console output at "<a href="${env.BUILD_URL}"> 
                                            ${env.JOB_NAME}:${env.BUILD_NUMBER}</a>"</p> 
                                            <p><i>(Build log is attached.)</i></p>"""
                             ) 
                    }
            }
      }

      cleanup {
            sh "sudo docker-compose down"
            sh "sudo docker system prune -af --volumes"    
      } 
   }
   
}