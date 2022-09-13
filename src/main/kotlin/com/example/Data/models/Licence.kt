package com.example.Data.models

import kotlinx.serialization.Serializable

@Serializable
data class Licence(
    var fullLicence: Boolean,
    var learners: Boolean,
    var restricted: Boolean,
    var wheels: Boolean,
    var tracks: Boolean,
    var rollers: Boolean,
    var  forks: Boolean,
    var  hazardus: Boolean,
    var  class2: Boolean,
    var  class3: Boolean,
    var  class4: Boolean,
    var  class5: Boolean,
)
