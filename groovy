pipeline {
  agent any

  environment {
    IMAGE_NAME   = 'fastapi-local'
    CONTAINER    = 'fastapi-app'
  }

  stages {
    stage('Build') {
      steps {
        echo "🔧 Building Docker image..."
        sh "docker build -t ${IMAGE_NAME} ."
      }
    }

    stage('Deploy') {
      steps {
        echo "🛑 Cleaning up any old container..."
        // stops & removes if exists; || true so it never fails
        sh "docker rm -f ${CONTAINER} || true"

        echo "🚀 Starting new container on port 8000..."
        sh "docker run -d --name ${CONTAINER} -p 8000:8000 ${IMAGE_NAME}"
      }
    }
  }

  post {
    success {
      echo "✅ Done! Visit http://<jenkins-host>:8000"
    }
    failure {
      echo "❌ Build or deploy failed. Check the console log."
    }
  }
}