package home.saied.processor

import com.squareup.kotlinpoet.ClassName

data class SampleFile(val fileName: String, val sampleList: List<SampleInfo>)

data class SampleInfo(
    val name: String,
    val body: String,
    val docStr: String?,
    val packageName: String,
    val optInAnnotations: List<ClassName>
)
