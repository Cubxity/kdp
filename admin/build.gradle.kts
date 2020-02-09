dependencies {
    implementation(project(":core"))
    api(project(":utils"))
    api(project(":perms"))
    implementation(kotlin("script-util"))
    implementation(kotlin("script-runtime"))
    implementation(kotlin("scripting-compiler-embeddable"))
    implementation(kotlin("compiler-embeddable"))
}