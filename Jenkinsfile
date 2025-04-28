def GITHUB_TOKEN = credentials('github-auth')
def POD_LABEL = 'kaniko'
    node(POD_LABEL) {
    def DOCKER_IMAGE_NAME = "gwrgwr/technova-ms-product:${env.BUILD_ID}"
        stage('Checkout') {
            checkout scm
        }

        stage('Build with Kaniko') {
            container('kaniko') {
                sh '''#!/busybox/sh
                                /kaniko/executor \
                                  --context `pwd` \
                                  --dockerfile=./Dockerfile \
                                  --destination ''' + DOCKER_IMAGE_NAME + ''' \
                                  --build-arg GITHUB_TOKEN=$GITHUB_TOKEN
                            '''
            }
        }

        stage('Deploy to Kubernetes') {
            container('kubectl') {
                withKubeConfig([credentialsId: 'jenkins-token', namespace: 'jenkins', serverUrl: 'https://192.168.49.2:8443']) {
                            sh '''
                                helm upgrade --install technova ../helm/ \
                                --namespace technova \
                                --set image.tag=''' + env.BUILD_ID + ''' \
                                --wait \
                                --atomic
                                '''
                        }
                }
        }
    }