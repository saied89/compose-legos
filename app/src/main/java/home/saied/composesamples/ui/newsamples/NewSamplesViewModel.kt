package home.saied.composesamples.ui.newsamples

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class NewSamplesViewModel(application: Application) : AndroidViewModel(application) {

    val newModuleSamples: List<NewModuleSamples>
    val baseLineVersion: String

    init {
        val assetsManager = application.assets
        val baseLineName =
            assetsManager.list("")?.first { it.startsWith("samples_report_baseline-") }
        if (baseLineName != null) {
            baseLineVersion = baseLineName.split('-').last().dropLast(4)
            val oldSamples: Set<String> = buildSet {
                assetsManager.open(baseLineName)
                    .bufferedReader().forEachLine { line ->
                        if (line.endsWith(" Processed")) {
                            val sampleName = line.trim().split(' ').first()
                            add(sampleName)
                        }
                    }
            }
            newModuleSamples = buildList {
                var moduleName = ""
                val curSamples = mutableListOf<String>()
                assetsManager.open("samples_report.txt").bufferedReader().forEachLine { line ->
                    if (line.endsWith(" Module:")) {
                        val newModuleName = line.trim().split(' ').first()
                        if (moduleName.isNotEmpty() && curSamples.isNotEmpty()) {
                            add(NewModuleSamples(moduleName, buildList { addAll(curSamples) }))
                            curSamples.clear()
                        }
                        moduleName = newModuleName
                    } else if (line.endsWith(" Processed")) {
                        val sampleName = line.trim().split(' ').first()
                        if (sampleName !in oldSamples)
                            curSamples.add(sampleName)
                    }
                }
            }
        }
        else {
            newModuleSamples = emptyList()
            baseLineVersion = ""
        }
    }


}

data class NewModuleSamples(
    val moduleName: String,
    val samples: List<String>
)