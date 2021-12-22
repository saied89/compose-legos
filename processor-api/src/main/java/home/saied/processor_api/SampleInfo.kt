package home.saied.processor_api

data class SampleFile(val fileName: String, val sampleList: List<SampleInfo>)

data class SampleInfo(
    val name: String,
    val body: String,
    val docStr: String?,
    val packageName: String
)