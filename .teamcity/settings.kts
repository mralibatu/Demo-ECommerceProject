import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.dockerCommand
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

version = "2019.2"

project {
    description = "TeamCity Simple Demo Project"

    vcsRoot(GitVcsRoot {
        id("DemoVcsRoot")
        name = "Demo VCS Root"
        url = "https://github.com/your-org/teamcity-simple-demo.git"
        branch = "refs/heads/main"
        branchSpec = "+:refs/heads/*"
    })

    buildType(Build)
    buildType(QualityCheck)
    buildType(Package)
}

object Build : BuildType({
    id("Build")
    name = "01. Build & Test"
    description = "Compile code and run tests"

    vcs {
        root(GitVcsRoot {
            id("DemoVcsRoot")
        })
        cleanCheckout = true
    }

    steps {
        maven {
            name = "Compile & Test"
            goals = "clean compile test"
            pomLocation = "pom.xml"
            runnerArgs = "-B"
            jdkHome = "%env.JDK_11%"
        }
    }

    triggers {
        vcs {
            branchFilter = "+:*"
        }
    }

    artifactRules = """
        target/*.jar => artifacts/
        target/site/jacoco/jacoco.xml => coverage/
    """.trimIndent()

    failureConditions {
        testFailure = false
        nonZeroExitCode = true
    }
})

object QualityCheck : BuildType({
    id("QualityCheck")
    name = "02. Quality Check"
    description = "Code quality analysis and coverage"

    vcs {
        root(GitVcsRoot {
            id("DemoVcsRoot")
        })
    }

    dependencies {
        snapshot(Build) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
    }

    steps {
        maven {
            name = "Code Coverage Report"
            goals = "jacoco:report"
            pomLocation = "pom.xml"
            runnerArgs = "-B"
            jdkHome = "%env.JDK_11%"
        }

        maven {
            name = "Checkstyle Analysis"
            goals = "checkstyle:check"
            pomLocation = "pom.xml"
            runnerArgs = "-B"
            jdkHome = "%env.JDK_11%"
        }
    }

    artifactRules = """
        target/site/jacoco/** => coverage-report/
        target/checkstyle-result.xml => reports/
    """.trimIndent()
})

object Package : BuildType({
    id("Package")
    name = "03. Package & Docker"
    description = "Create JAR file and Docker image"

    vcs {
        root(GitVcsRoot {
            id("DemoVcsRoot")
        })
    }

    dependencies {
        snapshot(Build) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
        snapshot(QualityCheck) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
    }

    steps {
        maven {
            name = "Package Application"
            goals = "package -DskipTests"
            pomLocation = "pom.xml"
            runnerArgs = "-B"
            jdkHome = "%env.JDK_11%"
        }

        dockerCommand {
            name = "Build Docker Image"
            commandType = build {
                source = path {
                    path = "."
                }
                namesAndTags = "teamcity-demo:latest teamcity-demo:%build.number%"
            }
        }
    }

    artifactRules = """
        target/*.jar => final-artifacts/
    """.trimIndent()

    requirements {
        exists("docker")
    }
})