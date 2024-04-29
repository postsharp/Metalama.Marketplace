// This file is automatically generated when you do `Build.ps1 prepare`.

import jetbrains.buildServer.configs.kotlin.v2019_2.*

// Both Swabra and swabra need to be imported
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.sshAgent
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.Swabra
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.swabra
import jetbrains.buildServer.configs.kotlin.v2019_2.failureConditions.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.*

version = "2021.2"

project {

   buildType(PublicUpdateSearch)
   buildTypesOrder = arrayListOf(PublicUpdateSearch)
}

object PublicUpdateSearch : BuildType({

    name = "Deploy [Public]"

    type = Type.DEPLOYMENT

    params {
        text("UpdateSearchArguments", "", label = "Update search Arguments", description = "Arguments to append to the 'Update search' build step.", allowEmpty = true)
        text("TimeOut", "300", label = "Time-Out Threshold", description = "Seconds after the duration of the last successful build.", regex = """\d+""", validationMessage = "The timeout has to be an integer number.")
    }
    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        powerShell {
            name = "Update search"
            scriptMode = file {
                path = "Build.ps1"
            }
            noProfile = false
            param("jetbrains_powershell_scriptArguments", "tools search update https://0fpg9nu41dat6boep.a1.typesense.net metalama-marketplace entries %UpdateSearchArguments%")
        }
    }

    failureConditions {
        failOnMetricChange {
            metric = BuildFailureOnMetric.MetricType.BUILD_DURATION
            units = BuildFailureOnMetric.MetricUnit.DEFAULT_UNIT
            comparison = BuildFailureOnMetric.MetricComparison.MORE
            compareTo = build {
                buildRule = lastSuccessful()
            }
            stopBuildOnFailure = true
            param("metricThreshold", "%TimeOut%")
        }
    }

    requirements {
        equals("env.BuildAgentType", "webdeployCloud")
    }

    features {
        swabra {
            lockingProcesses = Swabra.LockingProcessPolicy.KILL
            verbose = true
        }
    }

    triggers {
        schedule {
            schedulingPolicy = daily {
                hour = 0
                minute = 0
            }
            branchFilter = "+:<default>"
            triggerBuild = always()
            withPendingChangesOnly = false
        }
    }

})

