package home.saied.composesamples.ui.utils

import home.saied.samples.Sample
import home.saied.samples.SampleFile
import home.saied.samples.SampleModule

val mockSampleModuleList = List(10) { moduleIndex ->
    SampleModule(
        "module$moduleIndex",
        "package",
        List(10) { fileIndex ->
            SampleFile(
                "file$moduleIndex$fileIndex",
                "",
                List(10) { sampleIndex ->
                    Sample("sample$moduleIndex$fileIndex$sampleIndex", "", "")
                }
            )
        }
    )
}