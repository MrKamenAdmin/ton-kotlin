kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.tonKotlinBoc)
                api(projects.tonKotlinHashmap)
                api(projects.tonKotlinTlb)
                api(projects.tonKotlinTl)
                implementation(libs.serialization.json)
            }
        }
    }
}
