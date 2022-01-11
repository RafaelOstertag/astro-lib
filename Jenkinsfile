pipeline {
    agent {
        label 'freebsd&&kotlin'
    }

    triggers {
        pollSCM '@hourly'
        cron '@daily'
    }

    tools {
        maven 'Latest Maven'
    }

    options {
        ansiColor('xterm')
        buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '15')
        timestamps()
        disableConcurrentBuilds()
    }

    stages {
        stage('Build & Test') {
            steps {
                configFileProvider([configFile(fileId: '4f3d0128-0fdd-4de7-8536-5cbdd54a8baf', variable: 'MAVEN_SETTINGS_XML')]) {
                    sh 'mvn -B -s "$MAVEN_SETTINGS_XML" verify'
                }
            }

            post {
                always {
                    junit '**/failsafe-reports/*.xml,**/surefire-reports/*.xml'
                    jacoco()
                }
            }
        }

//        stage('Sonarcloud') {
//            steps {
//                configFileProvider([configFile(fileId: '4f3d0128-0fdd-4de7-8536-5cbdd54a8baf', variable: 'MAVEN_SETTINGS_XML')]) {
//                    withSonarQubeEnv(installationName: 'Sonarcloud', credentialsId: 'e8795d01-550a-4c05-a4be-41b48b22403f') {
//                        sh label: 'sonarcloud', script: "mvn -B -s \"$MAVEN_SETTINGS_XML\" -Dsonar.branch.name=${env.BRANCH_NAME} $SONAR_MAVEN_GOAL"
//                    }
//                }
//            }
//        }
//
//        stage("Quality Gate") {
//            steps {
//                timeout(time: 30, unit: 'MINUTES') {
//                    waitForQualityGate abortPipeline: true
//                }
//            }
//        }

        stage("Check Dependencies") {
            steps {
                configFileProvider([configFile(fileId: '4f3d0128-0fdd-4de7-8536-5cbdd54a8baf', variable: 'MAVEN_SETTINGS_XML')]) {
                    sh 'mvn -B -s "$MAVEN_SETTINGS_XML" -Psecurity-scan dependency-check:check'
                }
                dependencyCheckPublisher failedTotalCritical: 1, failedTotalHigh: 5, failedTotalLow: 8, failedTotalMedium: 8, pattern: '**/dependency-check-report.xml', unstableTotalCritical: 0, unstableTotalHigh: 4, unstableTotalLow: 8, unstableTotalMedium: 8
            }
        }

        stage('Deploy to Nexus') {
            when {
                branch "master"
                not {
                    triggeredBy "TimerTrigger"
                }
            }

            steps {
                configFileProvider([configFile(fileId: '4f3d0128-0fdd-4de7-8536-5cbdd54a8baf', variable: 'MAVEN_SETTINGS_XML')]) {
                    sh 'mvn -B -s "$MAVEN_SETTINGS_XML" -DskipTests deploy'
                }
            }
        }
    }

    post {
        unsuccessful {
            mail to: "rafi@guengel.ch",
                    subject: "${JOB_NAME} (${BRANCH_NAME};${env.BUILD_DISPLAY_NAME}) -- ${currentBuild.currentResult}",
                    body: "Refer to ${currentBuild.absoluteUrl}"
        }
    }
}
