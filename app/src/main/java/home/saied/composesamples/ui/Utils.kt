package home.saied.composesamples.ui

import home.saied.samples.SampleModule

val SampleModule.cleanPackageName get() = packageName.substringAfter("GenSampled.").dropLast(8)