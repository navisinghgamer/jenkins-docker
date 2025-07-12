pipeline {
  agent any

  environment {
    IMAGE_NAME = 'fastapi-local'
    CONTAINER  = 'fastapi-app'
    REPO_URL   = 'https://github.com/navisinghgamer/jenkins-docker.git'
    BRANCH     = 'main'
  }

  stages {
    stage('Prepare Workspace') {
      steps {
        echo "⚙️  Marking this workspace as Git-safe..."
        // Use WORKSPACE env var so Git trusts directories with spaces, etc.
        sh "git config --global --add safe.directory '${env.WORKSPACE}'"
      }
    }

    stage('Checkout') {
      steps {
        echo "🔍 Cloning ${REPO_URL}@${BRANCH}..."
        git url: "${REPO_URL}", branch: "${BRANCH}"
      }
    }

    stage('Build Image') {
      steps {
        echo "🔧 Building Docker image..."
        sh "docker build -t ${IMAGE_NAME} ."
      }
    }

    stage('Deploy') {
      steps {
        echo "🛑 Cleaning up any old container..."
        sh "docker rm -f ${CONTAINER} || true"

        echo "🚀 Starting new FastAPI container on port 8000..."
        sh "docker run -d --name ${CONTAINER} -p 8000:8000 ${IMAGE_NAME}"
      }
    }
  }

  post {
    success {
      echo "✅ All done! Visit http://<jenkins-host>:8000"
    }
    failure {
      echo "❌ Something failed. Check the console logs."
    }
  }
}