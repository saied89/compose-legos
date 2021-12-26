package home.saied.processor

import com.squareup.kotlinpoet.ClassName

data class SampleModuleInfo(val moduleName: String, val list: List<SampleFileInfo>)

data class SampleFileInfo(val fileName: String, val moduleName: String, val sampleList: List<SampleInfo>)

data class SampleInfo(
    val name: String,
    val body: String,
    val docStr: String?,
    val packageName: String,
    val optInAnnotations: List<ClassName>
)
