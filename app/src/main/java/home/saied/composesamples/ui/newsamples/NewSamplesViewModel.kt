package home.saied.composesamples.ui.newsamples

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class NewSamplesViewModel(application: Application) : AndroidViewModel(application) {

    val newModuleSamples: List<NewModuleSamples>

    init {
        val assetsManager = application.assets
        val oldSamples: Set<String> = buildSet {
            assetsManager.open("samples_report_baseline.txt")
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
            assetsManager.open("samples_report_new.txt").bufferedReader().forEachLine { line ->
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


}

data class NewModuleSamples(
    val moduleName: String,
    val samples: List<String>
)