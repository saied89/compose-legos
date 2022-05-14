package home.saied.processor

import com.squareup.kotlinpoet.ClassName

data class SampleModuleInfo(val moduleName: String, val list: List<SampleFileInfo>)

data class SampleFileInfo(
    val fileName: String,
    val moduleName: String,
    val packageName: String,
    val path: String,
    val sampleList: List<SampleInfo>
)

data class SampleInfo(
    val name: String,
    val body: String,
    val docStr: String?,
    val packageName: String,
    val optInAnnotations: List<ClassName>,
    val skipBlockGeneration: SKIP_BLOCK_GENERATION_REASON? = null
)

enum class SKIP_BLOCK_GENERATION_REASON {
    PARAMETERIZED,
    EXTENSION_RECEIVER,
    RESOURCES,
    RUNTIME_EXCEPTION
}
