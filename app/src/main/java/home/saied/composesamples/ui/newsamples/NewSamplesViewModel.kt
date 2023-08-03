package home.saied.composesamples.ui.newsamples

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class NewSamplesViewModel(application: Application) : AndroidViewModel(application) {

    val newSamples: List<NewSample>

    init {
        val assetsManager = application.assets
        val oldSamples: Set<String> = buildSet {
            assetsManager.open("samples_report_baseline.txt").bufferedReader().forEachLine { line ->
                if (line.endsWith( " Processed")) {
                    val sampleName = line.trim().split(' ').first()
                    add(sampleName)
                }
            }
        }
        newSamples = buildList {
            var moduleName = ""
            assetsManager.open("samples_report_new.txt").bufferedReader().forEachLine { line ->
                if (line.endsWith(" Module:")) {
                    val newModuleName = line.trim().split(' ').first()
                    moduleName = newModuleName
                } else if (line.startsWith("      ")) {
                    val sampleName = line.trim().split(' ').first()
                    if (sampleName !in oldSamples)
                        add(NewSample(sampleName, moduleName))
                }
            }
        }
    }
}

data class NewSample(val sampleName: String, val moduleName: String)