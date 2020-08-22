dependencies {
    api(project(":core"))
    api(project(":utils"))
    api(project(":perms"))
    implementation(kotlin("script-runtime"))
    implementation(kotlin("compiler-embeddable"))
    implementation(kotlin("script-util"))
    implementation(kotlin("scripting-compiler-embeddable"))
}