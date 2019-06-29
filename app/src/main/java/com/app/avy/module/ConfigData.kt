package com.app.avy.module

object ConfigData {

    var configModule: ConfigModule? = null

    fun setConfig(configModule: ConfigModule) {
        this.configModule = configModule
    }

    fun getConfig(): ConfigModule? {
        return this.configModule
    }
}